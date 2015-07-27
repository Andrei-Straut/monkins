

monkins.directive("displayJob", ['$window', function ($window) {
        return {
            restrict: "AE",
            replace: true,
            scope: {
                job: "=",
                numberOfJobs: '@',
                numberOfColumns: '@',
                defaultHeight: '@',
                displayForSuccessful: '=',
                displayForUnstable: '=',
                displayForFailed: '=',
                lastStableDetails: '@',
                lastFailedDetails: '@',
                testDetails: '@'
            },
            link: function (scope, element, attrs) {

                if (scope.displayForSuccessful === undefined || scope.displayForSuccessful == 'null') {
                    scope.displayForSuccessful = false;
                }
                if (scope.displayForUnstable === undefined || scope.displayForUnstable == 'null') {
                    scope.displayForUnstable = true;
                }
                if (scope.displayForFailed === undefined || scope.displayForFailed == 'null') {
                    scope.displayForFailed = true;
                }

                scope.$watch(watchObject, handleUpdate, true);
                element.css('height', getHeight());

                var window = angular.element($window);
                window.bind('resize', function () {
                    element.css('height', getHeight());
                });

                scope.lastStableDetails = lastStable();
                scope.lastFailedDetails = lastFailed();
                scope.testDetails = testDetails();

                function watchObject() {
                    return scope.job;
                };

                function handleUpdate(jobValueOld, jobValueNew, $scope) {
                };

                function getHeight() {
                    var lines = Math.ceil(scope.numberOfJobs / scope.numberOfColumns);
                    return (document.documentElement.clientHeight / (lines)) + "px";
                };

                function lastStable() {
                    var text = '';

                    if (checkData(scope.job)
                            && checkData(scope.job.associatedJob)
                            && checkData(scope.job.associatedJob.lastStableBuildTimeStamp)
                            && isDisplayed(scope.job.associatedJob.lastBuildResult)) {

                        text = 'Last Stable: ';
                        text += scope.job.associatedJob.lastStableBuildTimeStamp;
                    }

                    return text;
                }
                ;

                function lastFailed() {
                    var text = '';
                    if (checkData(scope.job)
                            && checkData(scope.job.associatedJob)
                            && checkData(scope.job.associatedJob.lastFailedBuildTimeStamp)
                            && isDisplayed(scope.job.associatedJob.lastBuildResult)) {

                        text = 'Last Failed: ';
                        text += scope.job.associatedJob.lastFailedBuildTimeStamp;
                    }

                    return text;
                };

                function testDetails() {
                    var text = '';

                    if (checkData(scope.job)
                            && checkData(scope.job.associatedJob)
                            && isDisplayed(scope.job.associatedJob.lastBuildResult)) {

                        var jobData = scope.job.associatedJob;

                        text = 'Test Results: ';

                        if (checkData(jobData.lastTestPassCount) && jobData.lastTestPassCount >= 0) {
                            text += 'Passed: ' + scope.job.associatedJob.lastTestPassCount;
                        }
                        if (checkData(jobData.lastTestFailCount) && jobData.lastTestFailCount >= 0) {
                            if (text.trim() !== '') {
                                text += ', ';
                            }
                            text += 'Failed: ' + scope.job.associatedJob.lastTestFailCount;
                        }
                        if (checkData(jobData.lastTestSkipCount) && jobData.lastTestSkipCount >= 0) {
                            if (text.trim() !== '') {
                                text += ', ';
                            }
                            text += 'Skipped: ' + scope.job.associatedJob.lastTestSkipCount;
                        }
                        if (checkData(jobData.lastTestTotalCount) && jobData.lastTestTotalCount >= 0) {
                            if (text.trim() !== '') {
                                text += ', ';
                            }
                            text += 'Total: ' + jobData.lastTestTotalCount;
                        }
                    }

                    return text;
                };

                function checkData(data) {
                    return data !== undefined && data !== null && data != 'null';
                };

                function isDisplayed(jobClass) {
                    var display = false;

                    if (!checkData(jobClass)) {
                        return false;
                    }


                    switch (jobClass.toString().trim().toLowerCase()) {
                        case 'failure':
                        {
                            if (checkData(scope.displayForFailed) && scope.displayForFailed === true) {
                                display = true;
                                break;
                            }
                        }
                        case 'unstable':
                        {
                            if (checkData(scope.displayForUnstable) && scope.displayForUnstable === true) {
                                display = true;
                                break;
                            }
                        }
                        case 'success':
                        {
                            if (checkData(scope.displayForSuccessful) && scope.displayForSuccessful === true) {
                                display = true;
                                break;
                            }
                        }
                        default:
                            display = false;
                    }

                    return display;
                };
            },
            templateUrl: 'pages/jobTemplate.jsp'
        };
    }]);



