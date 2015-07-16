

monkins.controller('monkinscontroller', ['$scope', 'WebSocketFactory',
    function ($scope, WebSocketFactory) {
        $scope.jobs = [];

        $scope.init = function () {
            var interval = window.setInterval(function () {
                var pollingJobs = WebSocketFactory.getJobsList();
                pollingJobs.then(function (response) {
                    if (response.status === 200) {
                        $scope.jobs = response.data.jobs;

                        $scope.subscribe();
                    } else {
                        console.log("Error: ", response);
                    }
                }).catch(function (e) {
                    console.log(e.description);
                });

                window.clearInterval(interval);
            }, 1000);
        };

        $scope.subscribe = function () {
            if ($scope.jobs.length > 0) {
                var subscribe = WebSocketFactory.subscribe();
                subscribe.then(function (response) {
                    if (response.status === 200) {
                    } else {
                        console.log(response);
                    }
                }, function (error) {
                    console.log(error.description);
                }, function (update) {
                    var data = update.data;

                    for (var i = 0; i < $scope.jobs.length; i++) {
                        if (($scope.jobs[i]).name === data.name) {
                            $scope.jobs[i] = data;
                            console.log("Job updated: ", ($scope.jobs[i]).name);
                        }
                    }
                });
            }
        };

        $scope.unSubscribe = function () {
            /*var interval = window.setInterval(function () {
             
             $scope.updates = WebSocketFactory.unSubscribe();
             $scope.updates.then(function (response) {
             console.log(response);
             }, function (error) {
             console.log(error.description);
             }, function (update) {
             console.log(update);
             });
             window.clearInterval(interval);
             }, 1000);*/
        };

        $scope.unSubscribeAll = function () {
            /*var interval = window.setInterval(function () {
             
             $scope.updates = WebSocketFactory.unSubscribeAll();
             $scope.updates.then(function (response) {
             console.log(response);
             }, function (error) {
             console.log(error.description);
             }, function (update) {
             console.log(update);
             });
             window.clearInterval(interval);
             }, 1000);*/
        };
    }]);

