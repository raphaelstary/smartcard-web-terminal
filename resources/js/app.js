function ConnectViewModel() {
    var self = this;

    // data
    self.readerList = ko.observable({channels:[
        {id:"666"},
        {id:"667"},
        {id:"668"}
    ]});
    self.chosenReaderId = ko.observable();
    self.chosenReaderData = ko.observable();
    self.command = ko.observable();
    self.showReaderArea = ko.observable(false);

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
            data:JSON.stringify({action:'getPcscClient', id:self.chosenReaderId()}),
            success:function (data) {
                self.chosenReaderData(data);
            }
        });
    };

    self.sendCommand = function () {
        $.ajax({
            type:'POST',
            url:'/api',
            contentType:'application/json',
            dataType:'json',
            data:JSON.stringify({action:'sendCommand'}),
            success:function (data) {
                //todo error handling
                //todo call getPcscClient
            }
        });
    };
}