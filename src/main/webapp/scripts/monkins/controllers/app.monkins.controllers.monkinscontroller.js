

monkins.controller('monkinscontroller', ['$rootScope', '$scope', 'WebSocketFactory',
    function ($rootScope, $scope, WebSocketFactory) {
        $scope.jobs = [];
        $scope.settings = {};
        $scope.errorText = '';

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

        $rootScope.$on('UPDATE_SETTINGS', function (event, data) {
            console.log("Updating settings");
            $scope.init();
            console.log("Updated settings");
        });

        $scope.notifyError = function (notification, $modalElement) {
            $scope.errorText = notification;
            $modalElement.modal('show');
        };
    }]);

