

monkins.controller('settingscontroller', ['$scope', 'WebSocketFactory', 'Notification',
    function ($scope, WebSocketFactory, Notification) {
        $scope.settings = {};
        $scope.tempSettings = {};
        $scope.models = {
            selected: null,
            list: []
        };
        $scope.errorText = '';

        $scope.init = function () {
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
                        $displayDetailsForUnstableJobsToggle.change(function () {
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

        $scope.saveSettings = function () {
            var newSettings = $scope.compileSettings();
            if (!newSettings || !newSettings.urls || newSettings.urls.length === 0) {
                $scope.notifyError("List of URLs cannot be empty", $('#modalError'));
            } else {
                var interval = window.setInterval(function () {
                    var updateSettingsRequest = WebSocketFactory.updateSettings(newSettings);
                    updateSettingsRequest.then(function (response) {
                        if (response.status === 200) {
                            Notification.success({message: 'Settings Updated', delay: 2000});
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

        $scope.removeJob = function (index) {
            $scope.models.list.splice(index - 1, 1);

            for (var i = 0; i < $scope.models.list.length; i++) {
                ($scope.models.list[i]).order = i + 1;
            }
        };

        $scope.cancelAction = function () {
            window.location = "/monkins";
        };

        $scope.reloadAction = function () {
            angular.copy($scope.settings, $scope.tempSettings);
            angular.copy($scope.settings.urls, $scope.models.list);
            $('#displayDetailsForSuccessfulJobs').prop('checked', $scope.tempSettings.displayDetailsForSuccessfulJobs).change();
            $('#displayDetailsForUnstableJobs').prop('checked', $scope.tempSettings.displayDetailsForUnstableJobs).change();
            $('#displayDetailsForFailedJobs').prop('checked', $scope.tempSettings.displayDetailsForFailedJobs).change();
        };

        $scope.saveAndStayAction = function () {
            $scope.saveSettings();
        };

        $scope.saveAndReturnAction = function () {
            $scope.saveSettings();

            window.location = "/monkins";
        };

        $scope.compileSettings = function () {
            var newSettings = {};
            angular.copy($scope.tempSettings, newSettings);
            newSettings.urls = $scope.models.list;

            return newSettings;
        };

        $scope.notifyError = function (notification, $modalElement) {
            $scope.errorText = notification;
            $modalElement.modal('show');
        };
    }]);

