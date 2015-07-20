

monkins.controller('analyzercontroller', ['$rootScope', '$scope', 'WebSocketFactory',
    function ($rootScope, $scope, WebSocketFactory) {
        $scope.settings = {};

        $scope.init = function () {
            var interval = window.setInterval(function () {
                var settings = WebSocketFactory.getSettings();
                settings.then(function (response) {
                    if (response.status === 200) {
                        $scope.settings = response.data;
                    } else {
                        console.log("Error: ", response);
                    }
                }).catch(function (e) {
                    console.log(e.description);
                });

                window.clearInterval(interval);
            }, 1000);
        };
    }]);

