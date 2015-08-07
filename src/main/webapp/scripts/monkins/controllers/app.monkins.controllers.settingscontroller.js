

monkins.controller('settingscontroller', ['$scope', 'WebSocketFactory', 'Notification',
    function ($scope, WebSocketFactory, Notification) {
        $scope.settings = {};
        $scope.tempSettings = {};
        $scope.models = {
            selected: null,
            list: []
        };
        $scope.errorText = '';
        $scope.newJob = {};

        $scope.init = function () {
            $("[data-toggle='tooltip']").tooltip();

            var interval = window.setInterval(function () {
                var settingsRequest = WebSocketFactory.getSettings();
                settingsRequest.then(function (response) {
                    if (response.status === 200) {
                        $scope.settings = response.data;
                        angular.copy($scope.settings, $scope.tempSettings);
                        angular.copy($scope.settings.urls, $scope.models.list);
                        console.log($scope.settings);

                        var $displayDetailsForSuccessfulJobsToggle = $('#displayDetailsForSuccessfulJobs').bootstrapToggle({
                            on: 'Visible',
                            off: 'Hidden'
                        });
                        $('#displayDetailsForSuccessfulJobs').prop('checked', $scope.tempSettings.displayDetailsForSuccessfulJobs).change();
                        $displayDetailsForSuccessfulJobsToggle.change(function () {
                            $scope.tempSettings.displayDetailsForSuccessfulJobs = $('#displayDetailsForSuccessfulJobs').prop('checked');
                        });

                        var $displayDetailsForUnstableJobsToggle = $('#displayDetailsForUnstableJobs').bootstrapToggle({
                            on: 'Visible',
                            off: 'Hidden'
                        });
                        $('#displayDetailsForUnstableJobs').prop('checked', $scope.tempSettings.displayDetailsForUnstableJobs).change();
                        $displayDetailsForUnstableJobsToggle.change(function () {
                            $scope.tempSettings.displayDetailsForUnstableJobs = $('#displayDetailsForUnstableJobs').prop('checked');
                        });

                        var $displayDetailsForFailedJobsToggle = $('#displayDetailsForFailedJobs').bootstrapToggle({
                            on: 'Visible',
                            off: 'Hidden'
                        });
                        $('#displayDetailsForFailedJobs').prop('checked', $scope.tempSettings.displayDetailsForFailedJobs).change();
                        $displayDetailsForFailedJobsToggle.change(function () {
                            $scope.tempSettings.displayDetailsForFailedJobs = $('#displayDetailsForFailedJobs').prop('checked');
                        });

                        Notification.success({message: 'Settings Loaded', delay: 2000});

                    } else {
                        console.log("Error: ", response);
                    }
                }).catch(function (e) {
                    console.log(e.description);
                });

                window.clearInterval(interval);
            }, 1000);
        };

        $scope.saveSettings = function (returnToMainPage) {
            $scope.tempSettings.urls = [];
            angular.copy($scope.models.list, $scope.tempSettings.urls);

            if (!$scope.tempSettings.urls || $scope.tempSettings.urls.length === 0) {
                $scope.notifyError("List of URLs cannot be empty", $('#modalError'));
            } else {
                var interval = window.setInterval(function () {
                    var updateSettingsRequest = WebSocketFactory.updateSettings($scope.tempSettings);
                    updateSettingsRequest.then(function (response) {
                        if (response.status === 200) {
                            Notification.success({message: 'Settings Updated', delay: 2000});

                            if (returnToMainPage) {
                                window.location = "./";
                            }
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
            }

        };

        $scope.moveCallback = function (index) {
            $scope.models.list.splice(index, 1);

            for (var i = 0; i < $scope.models.list.length; i++) {
                ($scope.models.list[i]).order = i + 1;
            }
        };

        $scope.addJob = function () {
            if ($scope.newJob && $scope.newJob.name && $scope.newJob.url) {
                $scope.models.list.push({
                    "name": $scope.newJob.name,
                    "url": $scope.newJob.url,
                    "order": $scope.models.list.length + 1
                });

                $scope.newJob = {};
            }

            $('#addJobModal').modal('hide');
        };

        $scope.addJobFieldsOk = function () {
            return $scope.newJob
                    && $scope.newJob.name !== undefined
                    && $scope.newJob.name !== null
                    && $scope.newJob.name !== ''
                    && $scope.newJob.url !== undefined
                    && $scope.newJob.url !== null
                    && $scope.newJob.url !== '';
        };

        $scope.removeJob = function (index) {
            $scope.models.list.splice(index - 1, 1);

            for (var i = 0; i < $scope.models.list.length; i++) {
                ($scope.models.list[i]).order = i + 1;
            }
        };

        $scope.cancelAction = function () {
            window.location = "./";
        };

        $scope.reloadAction = function () {
            $scope.init();
        };

        $scope.saveAndStayAction = function () {
            $scope.saveSettings(false);
        };

        $scope.saveAndReturnAction = function () {
            $scope.saveSettings(true);
        };

        $scope.notifyError = function (notification, $modalElement) {
            $scope.errorText = notification;
            $modalElement.modal('show');
        };
    }]);

