

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="monkins">
    <head>
        <!-- Google Tag Manager -->
        <script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
        new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
        j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
        'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
        })(window,document,'script','dataLayer','GTM-N46JBZF');</script>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Monkins</title>        
        <link rel="icon" type="image/png" href="img/monkins.png">

        <!-- BOOTSTRAP STYLES-->
        <link rel="stylesheet" href="css/lib/bootstrap/bootstrap.min.css">

        <!-- ANGULAR STYLES-->
        <link rel="stylesheet" href="css/lib/angular/angular.ui-notification.css">

        <!-- MONKINS STYLES-->
        <link rel="stylesheet" href="css/monkins/monkins.css">

        <!-- JQUERY SCRIPTS -->
        <script src="scripts/lib/jquery/jquery-2.1.3.min.js"></script>

        <!-- BOOTSTRAP SCRIPTS -->
        <script src="scripts/lib/bootstrap/bootstrap.min.js"></script>

        <!-- ANGULAR SCRIPTS -->
        <script src="scripts/lib/angular/angular.min.js"></script>
        <script src="scripts/lib/angular/angular.ui.bootstrap.tpls.min.js"></script>
        <script src="scripts/lib/angular/angular.draganddrop.js"></script>
        <script src="scripts/lib/angular/angular.ui.notification.min.js"></script>

        <!-- MONKINS SCRIPTS -->
        <script src="scripts/monkins/app.monkins.js"></script>
        <script src="scripts/monkins/factories/app.monkins.factories.websocket.js"></script>
        <script src="scripts/monkins/directives/app.monkins.directives.job.js"></script>
        <script src="scripts/monkins/controllers/app.monkins.controllers.monkinscontroller.js"></script>

        <!-- Global site tag (gtag.js) - Google Analytics -->
        <script async src="https://www.googletagmanager.com/gtag/js?id=UA-100610555-1"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag() {
                dataLayer.push(arguments);
            }
            gtag('js', new Date());
            gtag('config', 'UA-100610555-1');
            gtag('send', 'Monkins');
        </script>
    </head>

    <body ng-controller="monkinscontroller" ng-init="init();">
        <!-- Google Tag Manager (noscript) -->
        <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-N46JBZF"
        height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
        <!-- End Google Tag Manager (noscript) -->
        
        <div id="jobContainer" class="jobContainer container-fluid">
            <div ng-repeat="jobValue in jobs">
                <div display-job
                     class="jobDisplay {{getBootstrapColumns(settings.numberOfColumns)}}" 
                     ng-class="jobValue.associatedJob.lastBuildResult"
                     job="jobValue"
                     display-for-successful="settings.displayDetailsForSuccessfulJobs"
                     display-for-unstable="settings.displayDetailsForUnstableJobs"
                     display-for-failed="settings.displayDetailsForFailedJobs"
                     number-of-jobs="{{jobs.length}}"
                     number-of-columns="4">
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

    </body>
</html>
