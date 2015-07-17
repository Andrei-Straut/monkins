

monkins.controller('settingscontroller', ['$scope', 'WebSocketFactory',
    function ($scope, WebSocketFactory) {
        $scope.settings = {};
        $scope.tempSettings = {};
        $scope.models = {
            selected: null,
            list: []
        };

        $scope.init = function () {
            var interval = window.setInterval(function () {
                var settingsRequest = WebSocketFactory.getSettings();
                settingsRequest.then(function (response) {
                    if (response.status === 200) {
                        $scope.settings = response.data;
                        angular.copy($scope.settings, $scope.tempSettings);
                        angular.copy($scope.settings.urls, $scope.models.list);
                        
                        console.log($scope.settings);
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

