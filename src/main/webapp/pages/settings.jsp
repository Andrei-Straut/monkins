    

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="monkins">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Monkins Settings</title>

        <!-- ANGULAR STYLES-->
        <link rel="stylesheet" href="/monkins/css/lib/angular/angular.draganddrop.css">

        <!-- BOOTSTRAP STYLES-->
        <link rel="stylesheet" href="/monkins/css/lib/bootstrap/bootstrap.toggle.css">
        <link rel="stylesheet" href="/monkins/css/lib/bootstrap/bootstrap.min.css">

        <!-- MONKINS STYLES-->
        <link rel="stylesheet" href="/monkins/css/monkins/monkins.css">

        <!-- JQUERY SCRIPTS -->
        <script src="/monkins/scripts/lib/jquery/jquery-2.1.3.min.js"></script>

        <!-- BOOTSTRAP SCRIPTS -->
        <script src="/monkins/scripts/lib/bootstrap/bootstrap.min.js"></script>
        <script src="scripts/lib/bootstrap/bootstrap.toggle.min.js"></script>

        <!-- ANGULAR SCRIPTS -->
        <script src="/monkins/scripts/lib/angular/angular.min.js"></script>
        <script src="/monkins/scripts/lib/angular/angular.ui.bootstrap.tpls.js"></script>
        <script src="/monkins/scripts/lib/angular/angular.draganddrop.js"></script>

        <!-- MONKINS SCRIPTS -->
        <script src="/monkins/scripts/monkins/app.monkins.js"></script>
        <script src="/monkins/scripts/monkins/factories/app.monkins.factories.websocket.js"></script>
        <script src="/monkins/scripts/monkins/controllers/app.monkins.controllers.settingscontroller.js"></script>
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
                                                   data-original-title=""
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
                                                   data-original-title=""
                                                   type="string"                                     
                                                   ng-model="tempSettings.jsonApiTestReportSuffix">
                                        </div>

                                        <div class="form-group"
                                             ng-class="{
                                             'has-error'
                                             : false}">
                                            <label class="control-label" for="pollingIntervalMs">Polling Interval (ms)</label>
                                            <!-- max -> 60m * 60s * 1000ms = 1h -->
                                            <input required
                                                   id="pollingIntervalMs" 
                                                   name="pollingIntervalMs" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title=""
                                                   type="number"
                                                   min="100"
                                                   max="3600000" 
                                                   ng-model="tempSettings.pollingIntervalMs">
                                        </div>
                                    </div>

                                    <div class="col-md-3 border-right">
                                        <div class="form-group"
                                             ng-class="{
                                             'has-error'
                                             : false}">
                                            <label class="control-label" for="failCountBeforeCancel">Fail Count Before Cancel</label>
                                            <input required
                                                   id="failCountBeforeCancel" 
                                                   name="failCountBeforeCancel" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title=""
                                                   type="number"
                                                   min="0"
                                                   ng-model="tempSettings.failCountBeforeCancel">
                                        </div>

                                        <div class="form-group"
                                             ng-class="{
                                             'has-error'
                                             : false}">
                                            <label class="control-label" for="numberOfColumns">Number of Columns</label>
                                            <input required
                                                   id="numberOfColumns" 
                                                   name="numberOfColumns" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title=""
                                                   type="number"
                                                   min="1"
                                                   max="6"
                                                   ng-model="tempSettings.numberOfColumns">
                                        </div>

                                        <div class="form-group"
                                             ng-class="{
                                             'has-error'
                                             : false}">
                                            <label class="control-label" for="defaultJobDisplayHeight">Default Job Height</label>
                                            <input required
                                                   id="defaultJobDisplayHeight" 
                                                   name="defaultJobDisplayHeight" 
                                                   class="form-control"
                                                   data-toggle="tooltip" 
                                                   data-placement="bottom" 
                                                   data-original-title=""
                                                   type="number"
                                                   min="50"
                                                   max="1024"
                                                   ng-model="tempSettings.defaultJobDisplayHeight">
                                        </div>
                                    </div>

                                    <div class="col-md-3">

                                        <div class="form-group">
                                            <label class="control-label" for="displayDetailsForSuccessfulJobs"
                                                   data-toggle="tooltip" 
                                                   data-original-title="">
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
                                                   data-original-title="">
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
                                                   data-original-title="">
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
                            </div>
                            <div class="panel-body">
                                <div class="simpleDemo row">
                                    <div class="col-md-12 col-sm-12 col-xs-12">
                                        <div class="panel panel-info">
                                            <ul dnd-list="models.list">
                                                <li ng-repeat="item in models.list"
                                                    dnd-draggable="item"
                                                    dnd-moved="models.list.splice($index, 1)"
                                                    dnd-effect-allowed="move"
                                                    dnd-selected="models.selected = item"
                                                    ng-class="{'selected': models.selected === item}">

                                                    <div class="row">
                                                        <div class="col-md-2 col-sm-2 col-xs-2">
                                                            <div class="form-group">
                                                                <label class="control-label" for="jobDataOrder-{{item.order}}">Order</label>
                                                                <input required disabled
                                                                       id="jobDataOrder-{{item.order}}" 
                                                                       name="jobDataOrder-{{item.order}}" 
                                                                       class="form-control"
                                                                       data-toggle="tooltip" 
                                                                       data-placement="bottom" 
                                                                       data-original-title=""
                                                                       type="string"
                                                                       ng-model="item.order">
                                                            </div>
                                                        </div>

                                                        <div class="col-md-3 col-sm-3 col-xs-3">
                                                            <div class="form-group">
                                                                <label class="control-label" for="jobDataName-{{item.name}}">Name</label>
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

                                                        <div class="col-md-5 col-sm-5 col-xs-5">
                                                            <div class="form-group">
                                                                <label class="control-label" for="jobDataUrl-{{item.url}}">URL</label>
                                                                <input required
                                                                       id="jobDataUrl-{{item.url}}" 
                                                                       name="jobDataUrl-{{item.url}}" 
                                                                       class="form-control"
                                                                       data-toggle="tooltip" 
                                                                       data-placement="bottom" 
                                                                       data-original-title=""
                                                                       type="string"
                                                                       ng-model="item.url">
                                                            </div>
                                                        </div>
                                                        <div class="col-md-2 col-sm-2 col-xs-2"></div>
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
                                    data-original-title=""
                                    ng-disabled="false" 
                                    ng-click="return;">Save and return</button>

                                &nbsp;

                                <button 
                                    id="saveAndStayButton" 
                                    class="btn btn-primary"
                                    type="submit" 
                                    data-toggle="tooltip" 
                                    data-original-title=""
                                    ng-disabled="false" 
                                    ng-click="return;">Save and stay on page</button>

                                &nbsp;

                                <button id="cancelButton" 
                                        name="cancelButton"
                                        type="button"
                                        class="btn btn-warning"
                                        data-toggle="modal"
                                        data-target="#graphSettingsAdvancedModal"
                                        ng-disabled="false" 
                                        ng-click="return;">
                                    Cancel</button>
                            </div>
                        </div>
                    </form>
                </div>

            </div>
        </div>
    </body>
</html>