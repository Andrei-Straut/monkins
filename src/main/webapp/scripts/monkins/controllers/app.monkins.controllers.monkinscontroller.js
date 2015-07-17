

monkins.controller('monkinscontroller', ['$scope', 'WebSocketFactory',
    function ($scope, WebSocketFactory) {
        $scope.jobs = [];

        $scope.init = function () {
            var interval = window.setInterval(function () {
                var pollingJobs = WebSocketFactory.getJobsList();
                pollingJobs.then(function (response) {
                    if (response.status === 200) {
                        $scope.jobs = response.data.jobs;

                        if ($scope.jobs.length === 0) {
                            $scope.notifyError("Oops! Looks like you haven't defined any URLs to monitor. "
                                    + "Please head over to settings page", $('#modalError'));
                        }

                        $scope.subscribe();
                    } else {
                        $scope.notifyError(response.description, $('#modalError'));
                        console.log("Error: ", response);
                    }
                }).catch(function (e) {
                    $scope.notifyError(e.description, $('#modalError'));
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
                        $scope.notifyError(response.description, $('#modalError'));
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
    }]);

