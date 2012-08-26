function ConnectViewModel() {
    var self = this;

    // data
    self.readerList = ko.observable({channels:[{id:"666"},{id:"667"},{id:"668"}]});
    self.chosenReaderId = ko.observable();
    self.chosenReaderData = ko.observable();
    self.command = ko.observable();
    self.showReaderArea = ko.observable(false);

    // behaviours
    self.connectToReader = function () {

    };
    self.sendCommand = function () {

    };

    Sammy(function () {
        this.get('', function () {
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
        });

    }).run();
}