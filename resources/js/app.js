function ConnectViewModel() {
    var self = this;

    self.ERROR = "error-msg";
    self.SUCCESS = "success-msg";
    self.commandSendingActive = false;

    // data
    self.readerList = ko.observable({channels:[
        {id:"666"},
        {id:"667"},
        {id:"668"}
    ]});
    self.chosenReader = ko.observable({id: "no reader"});
    self.chosenReaderData = ko.observable();
    self.command = ko.observable();
    self.showReaderArea = ko.observable(false);

    self.showAlert = ko.observable(false);
    self.alertMessage = ko.observable();
    self.alertIsError = ko.observable(false);
    self.alertIsSuccess = ko.observable(false);

    // behaviours
    self.init = function () {
        $.ajax({
            type:'POST',
            url:'/api',
            contentType:"application/json",
            dataType:"json",
            data:JSON.stringify({action:"getPcscClients"}),
            success:function (data) {
                self.readerList(data);
            }
        });
    };

    self.connectToReader = function () {
        $.ajax({
            type:'POST',
            url:'/api',
            contentType:'application/json',
            dataType:'json',
            data:JSON.stringify({action:'getPcscClient', id: self.chosenReader().id}),
            success:function (data) {
                self.chosenReaderData(data);
                if (self.commandSendingActive) {
                    self.setAlert(self.SUCCESS, "Command sent");
                    self.commandSendingActive = false;
                } else {
                    self.setAlert(self.SUCCESS, "Connected to reader");
                }
                self.showReaderArea(true);
            },
            error: function () {
                self.setAlert(self.ERROR, "Can't connect to reader");
            }
        });
    };

    self.sendCommand = function () {
        var commandList = self.command().split(',');
        for (var i=0; i<commandList.length; i++) {
            commandList[i] = parseInt(commandList[i]);
        }

        alert(self.command());
        $.ajax({
            type:'POST',
            url:'/api',
            contentType:'application/json',
            dataType:'json',
            data:JSON.stringify({action:'sendCommandApdu', id: self.chosenReader().id, command: commandList}),
            success: function (data) {
                if (data.status === "error") {
                    self.setAlert(self.ERROR, "error from server: " + data.cause);

                } else {
                    self.commandSendingActive = true;
                    self.connectToReader();
                }
            },
            error: function() {
                self.setAlert(self.ERROR, "error while sending command")
            }
        });
    };

    self.setAlert = function (type, msg) {
        self.resetAlert();

        self.showAlert(true);
        if (type === self.ERROR)
            self.alertIsError(true);
        if (type === self.SUCCESS)
            self.alertIsSuccess(true);

        self.alertMessage(msg);
    };

    self.resetAlert = function () {
        self.showAlert(false);
        self.alertIsError(false);
        self.alertIsSuccess(false);
        self.alertMessage(null);
    };
}