

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="monkins">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Monkins</title>        

        <!-- BOOTSTRAP STYLES-->
        <link rel="stylesheet" href="css/lib/bootstrap/bootstrap.min.css">
        
        <!-- MONKINS STYLES-->
        <link rel="stylesheet" href="css/monkins/monkins.css">
        
        <!-- JQUERY SCRIPTS -->
        <script src="scripts/lib/jquery/jquery-2.1.3.min.js"></script>     
        
        <!-- BOOTSTRAP SCRIPTS -->
        <script src="scripts/lib/bootstrap/bootstrap.min.js"></script>

        <!-- ANGULAR SCRIPTS -->
        <script src="scripts/lib/angular/angular.min.js"></script>
        <script src="scripts/lib/angular/angular.ui.bootstrap.tpls.js"></script>
        <script src="/monkins/scripts/lib/angular/angular.draganddrop.js"></script>

        <!-- MONKINS SCRIPTS -->
        <script src="scripts/monkins/app.monkins.js"></script>
        <script src="scripts/monkins/factories/app.monkins.factories.websocket.js"></script>
        <script src="scripts/monkins/directives/app.monkins.directives.job.js"></script>
        <script src="scripts/monkins/controllers/app.monkins.controllers.monkinscontroller.js"></script>
    </head>

    <body ng-controller="monkinscontroller" ng-init="init();">
        <div id="jobContainer" class="jobContainer container-fluid">
            <div ng-repeat="jobValue in jobs">
                <div display-job
                    class="jobDisplay col-md-4 col-sm-4 col-xs-4" 
                    ng-class="jobValue.associatedJob.lastBuildResult"
                    job="jobValue"
                    number-of-jobs="{{jobs.length}}"
                    number-of-columns="3">
                </div>
            </div>
        </div>

    </body>
</html>
