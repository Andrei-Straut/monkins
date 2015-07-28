    

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="monkins">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Monkins Settings</title>

        <!-- ANGULAR STYLES-->
        <link rel="stylesheet" href="css/lib/angular/angular.draganddrop.css"/>
        <link rel="stylesheet" href="css/lib/angular/angular.ui-notification.css"/>

        <!-- BOOTSTRAP STYLES-->
        <link rel="stylesheet" href="css/lib/bootstrap/bootstrap.toggle.css"/>
        <link rel="stylesheet" href="css/lib/bootstrap/bootstrap.min.css"/>

        <!-- FONTAWESOME STYLES-->
        <link rel="stylesheet" href="css/lib/font-awesome/font-awesome.css"/>

        <!-- MONKINS STYLES-->
        <link rel="stylesheet" href="css/monkins/monkins.css"/>

        <!-- JQUERY SCRIPTS -->
        <script src="scripts/lib/jquery/jquery-2.1.3.min.js"></script>

        <!-- BOOTSTRAP SCRIPTS -->
        <script src="scripts/lib/bootstrap/bootstrap.min.js"></script>
        <script src="scripts/lib/bootstrap/bootstrap.toggle.min.js"></script>

        <!-- ANGULAR SCRIPTS -->
        <script src="scripts/lib/angular/angular.min.js"></script>
        <script src="scripts/lib/angular/angular.ui.bootstrap.tpls.min.js"></script>
        <script src="scripts/lib/angular/angular.draganddrop.js"></script>
        <script src="scripts/lib/angular/angular.ui.notification.min.js"></script>

        <!-- MONKINS SCRIPTS -->
        <script src="scripts/monkins/app.monkins.js"></script>
        <script src="scripts/monkins/factories/app.monkins.factories.websocket.js"></script>
        <script src="scripts/monkins/controllers/app.monkins.controllers.settingscontroller.js"></script>
        <script src="scripts/monkins/directives/app.monkins.directives.validation.jobUrl.js"></script>
    </head>

    <body ng-controller="settingscontroller" ng-init="init();">
        <div class="col-md-12 col-sm-12 col-xs-12">

            <div class="panel panel-default">
                <div class="panel-heading">
                    Monkins Settings
                </div>
                <div class="panel-body">
                    <form name="monkinssettings" novalidate>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                General
                            </div>
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-6 border-right">
                                        <div class="form-group">
                                            <label class="control-label" for="jsonApiSuffix">Jenkins JSON API Suffix</label>
                                            <input required
                                                   id="jsonApiSuffix" 
                                                   name="jsonApiSuffix" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title="This needs to be appended to each job's URL to access the respective job's API"
                                                   type="string"                                     
                                                   ng-model="tempSettings.jsonApiSuffix">
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label" for="jsonApiTestReportSuffix">Jenkins JSON API Test Report Suffix</label>
                                            <input required
                                                   id="jsonApiTestReportSuffix" 
                                                   name="jsonApiTestReportSuffix" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title="Like above, this needs to be appended to each test report's URL to access the respective test report's API"
                                                   type="string"                                     
                                                   ng-model="tempSettings.jsonApiTestReportSuffix">
                                        </div>

                                        <div class="form-group"
                                             ng-class="{'has-error' : 
                                             monkinssettings.pollingIntervalMs.$error.required
                                                                 || !monkinssettings.pollingIntervalMs.$valid}">
                                            <label class="control-label" for="pollingIntervalMs">Polling Interval (ms)</label>
                                            <!-- max -> 60m * 60s * 1000ms = 1h -->
                                            <input required
                                                   id="pollingIntervalMs" 
                                                   name="pollingIntervalMs" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title="Time between subsequent pollings for job status updates"
                                                   type="number"
                                                   min="100"
                                                   max="3600000" 
                                                   ng-model="tempSettings.pollingIntervalMs">
                                            <label class="control-label" 
                                                   for="pollingIntervalMs" 
                                                   ng-show="monkinssettings.pollingIntervalMs.$error.required">
                                                Polling interval must always be specified
                                            </label>
                                            <label class="control-label" 
                                                   for="pollingIntervalMs" 
                                                   ng-show="!monkinssettings.pollingIntervalMs.$valid">
                                                Polling interval must be a number between 100 and 3.600.000 (1h)
                                            </label>
                                        </div>
                                    </div>

                                    <div class="col-md-3 border-right">
                                        <div class="form-group"
                                             ng-class="{'has-error' : 
                                             monkinssettings.failCountBeforeCancel.$error.required
                                                                 || !monkinssettings.failCountBeforeCancel.$valid}">
                                            <label class="control-label" for="failCountBeforeCancel">Fail Count Before Cancel</label>
                                            <input required
                                                   id="failCountBeforeCancel" 
                                                   name="failCountBeforeCancel" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title="If a request for job info fails for some reason, it will fail this many times before being cancelled"
                                                   type="number"
                                                   min="0"
                                                   ng-model="tempSettings.failCountBeforeCancel">
                                            <label class="control-label" 
                                                   for="failCountBeforeCancel" 
                                                   ng-show="monkinssettings.failCountBeforeCancel.$error.required">
                                                Fail limit must always be specified
                                            </label>
                                            <label class="control-label" 
                                                   for="failCountBeforeCancel" 
                                                   ng-show="!monkinssettings.failCountBeforeCancel.$valid">
                                                Fail limit must be a number larger than or equal to 0 (no canceling)
                                            </label>
                                        </div>

                                        <div class="form-group"
                                             ng-class="{'has-error' : 
                                             monkinssettings.numberOfColumns.$error.required
                                                                 || !monkinssettings.numberOfColumns.$valid}">
                                            <label class="control-label" for="numberOfColumns">Number of Columns</label>
                                            <input required
                                                   id="numberOfColumns" 
                                                   name="numberOfColumns" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title="Number of columns to present in the job view"
                                                   type="number"
                                                   min="1"
                                                   max="6"
                                                   ng-model="tempSettings.numberOfColumns">
                                            <label class="control-label" 
                                                   for="numberOfColumns" 
                                                   ng-show="monkinssettings.numberOfColumns.$error.required">
                                                Number of columns must always be specified
                                            </label>
                                            <label class="control-label" 
                                                   for="failCountBeforeCancel" 
                                                   ng-show="!monkinssettings.numberOfColumns.$valid">
                                                Number of columns must be a number between 1 and 6
                                            </label>
                                        </div>

                                        <div class="form-group"
                                             ng-class="{'has-error' : 
                                             monkinssettings.defaultJobDisplayHeight.$error.required
                                                                 || !monkinssettings.defaultJobDisplayHeight.$valid}">
                                            <label class="control-label" for="defaultJobDisplayHeight">Default Job Height (px)</label>
                                            <input required
                                                   id="defaultJobDisplayHeight" 
                                                   name="defaultJobDisplayHeight" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title="How tall each job container is by default"
                                                   type="number"
                                                   min="50"
                                                   max="1024"
                                                   ng-model="tempSettings.defaultJobDisplayHeight">
                                            <label class="control-label" 
                                                   for="defaultJobDisplayHeight" 
                                                   ng-show="monkinssettings.defaultJobDisplayHeight.$error.required">
                                                Default Height must always be specified
                                            </label>
                                            <label class="control-label" 
                                                   for="defaultJobDisplayHeight" 
                                                   ng-show="!monkinssettings.defaultJobDisplayHeight.$valid">
                                                Default Height must be a number between 50 and 1024 (px)
                                            </label>
                                        </div>
                                    </div>

                                    <div class="col-md-3">

                                        <div class="form-group">
                                            <label class="control-label" for="displayDetailsForSuccessfulJobs"
                                                   data-toggle="tooltip" 
                                                   data-original-title="If checked, last build and test report details will be presented for successful jobs">
                                                Details for Successful Jobs
                                            </label><br/>
                                            <label class="checkbox-inline">
                                                <input id="displayDetailsForSuccessfulJobs"
                                                       type="checkbox"
                                                       checked
                                                       data-toggle="toggle"
                                                       data-on="Visible"
                                                       data-off="Hidden"
                                                       data-size="small"
                                                       ng-disabled="false">
                                            </label>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label" for="displayDetailsForUnstableJobs"
                                                   data-toggle="tooltip" 
                                                   data-original-title="If checked, last build and test report details will be presented for unstable jobs">
                                                Details for Unstable Jobs
                                            </label><br/>
                                            <label class="checkbox-inline">
                                                <input id="displayDetailsForUnstableJobs"
                                                       type="checkbox"
                                                       checked
                                                       data-toggle="toggle"
                                                       data-on="Visible"
                                                       data-off="Hidden"
                                                       data-size="small"
                                                       ng-disabled="false">
                                            </label>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label" for="displayDetailsForFailedJobs"
                                                   data-toggle="tooltip" 
                                                   data-original-title="If checked, last build and test report details will be presented for failed jobs">
                                                Details for Failed Jobs
                                            </label><br/>
                                            <label class="checkbox-inline">
                                                <input id="displayDetailsForFailedJobs"
                                                       type="checkbox"
                                                       checked
                                                       data-toggle="toggle"
                                                       data-on="Visible"
                                                       data-off="Hidden"
                                                       data-size="small"
                                                       ng-disabled="false">
                                            </label>
                                        </div>          

                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                Jobs (Drag and Drop to Reorder) 
                                &nbsp;
                                <button id="addJobButton" 
                                        name="addJobButton"
                                        type="button"
                                        class="btn btn-primary"
                                        data-toggle="modal"
                                        data-target="#addJobModal"
                                        ng-disabled="false" 
                                        ng-click="return;">
                                    Add Job</button>

                            </div>
                            <div class="panel-body">
                                <div class="simpleDemo row">
                                    <div class="col-md-12 col-sm-12 col-xs-12">
                                        <div class="panel panel-info">
                                            <ul dnd-list="models.list">
                                                <li ng-repeat="item in models.list"
                                                    dnd-draggable="item"
                                                    dnd-moved="moveCallback($index)"
                                                    dnd-effect-allowed="move"
                                                    dnd-selected="models.selected = item"
                                                    ng-class="{'selected': models.selected === item}">

                                                    <div class="row">
                                                        <div class="col-md-1 col-sm-1 col-xs-1">
                                                            <div class="form-group input-group input-group-sm">
                                                                <span 
                                                                    id="jobDataOrder-{{item.order}}" 
                                                                    name="jobDataOrder-{{item.order}}" 
                                                                    ng-model="item.order"
                                                                    class="input-group-addon">{{item.order}}</span>
                                                            </div>
                                                        </div>

                                                        <div class="col-md-4 col-sm-4 col-xs-4">
                                                            <div class="form-group input-group input-group-sm">
                                                                <span class="input-group-addon">Name</span>
                                                                <input required
                                                                       id="jobDataName-{{item.name}}" 
                                                                       name="jobDataName-{{item.name}}" 
                                                                       class="form-control"
                                                                       data-toggle="tooltip" 
                                                                       data-placement="bottom" 
                                                                       data-original-title=""
                                                                       type="string"
                                                                       ng-model="item.name">
                                                            </div>
                                                        </div>

                                                        <div class="col-md-6 col-sm-6 col-xs-6">
                                                            <div class="form-group input-group input-group-sm">
                                                                <span class="input-group-addon">URL</span>
                                                                <input required
                                                                       id="jobDataUrl-{{item.order}}"
                                                                       name="jobDataUrl-{{item.order}}"
                                                                       class="form-control"
                                                                       data-toggle="tooltip" 
                                                                       data-placement="bottom" 
                                                                       data-original-title=""
                                                                       type="string"
                                                                       ng-model="item.url">
                                                            </div>
                                                        </div>

                                                        <div class="col-md-1 col-sm-1 col-xs-1">
                                                            <button type="button" 
                                                                    class="btn btn-danger btn-circle" 
                                                                    ng-click="removeJob(item.order)"
                                                                    data-toggle="tooltip" 
                                                                    data-placement="top" 
                                                                    data-original-title="Remove Job">
                                                                <i class="fa fa-remove">
                                                                </i>
                                                            </button>
                                                        </div>
                                                    </div>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <HR/>

                        <div class="row">
                            <div class="col-md-12">
                                <button 
                                    id="saveAndReturnButton" 
                                    class="btn btn-primary"
                                    type="submit" 
                                    data-toggle="tooltip" 
                                    data-placement="top" 
                                    data-original-title="Save and return to the job view"
                                    ng-disabled="!models || models.list.length === 0" 
                                    ng-click="saveAndReturnAction();">Save and go to main page</button>

                                &nbsp;

                                <button 
                                    id="saveAndStayButton" 
                                    class="btn btn-primary"
                                    type="submit" 
                                    data-toggle="tooltip" 
                                    data-placement="top" 
                                    data-original-title="Save and stay on this page"
                                    ng-disabled="!models || models.list.length === 0" 
                                    ng-click="saveAndStayAction();">Save and stay on this page</button>

                                &nbsp;

                                <button id="cancelButton" 
                                        name="cancelButton"
                                        type="button"
                                        class="btn btn-warning"
                                        data-toggle="modal"
                                        data-target="#graphSettingsAdvancedModal"
                                        ng-disabled="false" 
                                        ng-click="cancelAction();">
                                    Cancel</button>

                                &nbsp;

                                <button id="cancelButton" 
                                        name="cancelButton"
                                        type="button"
                                        class="btn btn-warning"
                                        data-toggle="modal"
                                        data-target="#graphSettingsAdvancedModal"
                                        ng-disabled="false" 
                                        ng-click="reloadAction();">
                                    Reload Settings</button>
                            </div>
                        </div>
                    </form>
                </div>

            </div>
        </div>

        <div class="row">
            <div class="modal fade" id="modalError" tabindex="-1" role="dialog" aria-labelledby="modalError" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h4 class="modal-title" id="loadingError">Error</h4>
                        </div>
                        <div class="modal-body">
                            An error has occurred:(<br/>
                            {{errorText}}
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="modal fade" id="addJobModal" tabindex="-1" role="dialog" aria-labelledby="addJobModal" aria-hidden="true" style="display: none;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h4 class="modal-title" id="addJobModal">Add Job</h4>
                        </div>
                        <div class="modal-body">
                            <form name="addjobform" novalidate>
                                <div class="form-group">
                                    <label class="control-label" for="addJobName">Name</label>
                                    <input required
                                           id="addJobName" 
                                           name="addJobName" 
                                           class="form-control"
                                           data-toggle="tooltip" 
                                           data-placement="bottom" 
                                           data-original-title=""
                                           type="string"
                                           ng-model="newJob.name">
                                </div>

                                <div class="form-group"
                                     ng-class="{'has-error' : 
                                            addjobform.addJobUrl.$dirty && addjobform.addJobUrl.$error.joburlvalid}">
                                    <label class="control-label" for="addJobUrl">URL</label>
                                    <input required job-url-valid
                                           id="addJobUrl"
                                           name="addJobUrl"
                                           class="form-control"
                                           data-toggle="tooltip" 
                                           data-placement="bottom" 
                                           data-original-title=""
                                           type="string"
                                           ng-model="newJob.url">
                                    <label class="control-label" 
                                           for="addJobUrl" 
                                           ng-show="addjobform.addJobUrl.$dirty && addjobform.addJobUrl.$error.joburlvalid">
                                        URL format is invalid
                                    </label>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" data-dismiss="modal" ng-disabled="!addJobFieldsOk();" ng-click="addJob();">Add</button>
                            <button type="button" class="btn btn-warning" data-dismiss="modal">Cancel</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>