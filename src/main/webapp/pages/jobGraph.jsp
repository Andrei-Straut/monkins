    

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="monkins">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Monkins Job Analyzer</title>

        <!-- ANGULAR STYLES-->
        <link rel="stylesheet" href="/monkins/css/lib/angular/angular.ui-notification.css"/>

        <!-- BOOTSTRAP STYLES-->
        <link rel="stylesheet" href="/monkins/css/lib/bootstrap/bootstrap.toggle.css"/>
        <link rel="stylesheet" href="/monkins/css/lib/bootstrap/bootstrap.min.css"/>

        <!-- VIS STYLES-->
        <link rel="stylesheet" href="/monkins/css/lib/vis/vis.min.css"/>
        
        <!-- FONTAWESOME STYLES-->
        <link rel="stylesheet" href="/monkins/css/lib/font-awesome/font-awesome.css"/>

        <!-- MONKINS STYLES-->
        <link rel="stylesheet" href="/monkins/css/monkins/monkins.css"/>

        <!-- JQUERY SCRIPTS -->
        <script src="/monkins/scripts/lib/jquery/jquery-2.1.3.min.js"></script>

        <!-- BOOTSTRAP SCRIPTS -->
        <script src="/monkins/scripts/lib/bootstrap/bootstrap.min.js"></script>
        <script src="scripts/lib/bootstrap/bootstrap.toggle.min.js"></script>

        <!-- ANGULAR SCRIPTS -->
        <script src="/monkins/scripts/lib/angular/angular.min.js"></script>
        <script src="/monkins/scripts/lib/angular/angular.ui.bootstrap.tpls.min.js"></script>
        <script src="/monkins/scripts/lib/angular/angular.draganddrop.js"></script>
        <script src="/monkins/scripts/lib/angular/angular.ui.notification.min.js"></script>
        
        <!-- VIS SCRIPTS -->
        <script src="/monkins/scripts/lib/vis/vis.min.js"></script>        

        <!-- MONKINS SCRIPTS -->
        <script src="/monkins/scripts/monkins/app.monkins.js"></script>
        <script src="/monkins/scripts/monkins/controllers/app.monkins.controllers.analyzercontroller.js"></script>
        <script src="/monkins/scripts/monkins/factories/app.monkins.factories.websocket.js"></script>
    </head>

    <body ng-controller="analyzercontroller" ng-init="init();">
        <div class="col-md-12 col-sm-12 col-xs-12">

            <div class="panel panel-default">
                <div class="panel-heading">
                    Monkins Job Analyzer
                </div>
                <div class="panel-body">

                </div>

            </div>
        </div>

        <!--
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
        -->
        
    </body>
</html>