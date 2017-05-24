<%-- 
    Document   : timer
    Created on : Apr 1, 2016, 10:08:30 AM
    Author     : straut
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TransNet release countdown</title>

        <!-- BOOTSTRAP STYLES-->
        <link rel="stylesheet" href="css/lib/bootstrap/bootstrap.min.css"/>

        <!-- FONTAWESOME STYLES-->
        <link rel="stylesheet" href="css/lib/font-awesome/font-awesome.css"/>

        <!-- MONKINS STYLES-->
        <link rel="stylesheet" href="css/monkins/monkins.css"/>

        <!-- JQUERY SCRIPTS -->
        <script src="scripts/lib/jquery/jquery-2.1.3.min.js"></script>
        <script src="scripts/lib/jquery/jquery.countdown.min.js"></script>

        <!-- BOOTSTRAP SCRIPTS -->
        <script src="scripts/lib/bootstrap/bootstrap.min.js"></script>
        <script src="scripts/lib/bootstrap/bootstrap.toggle.min.js"></script>

        <!-- ANGULAR SCRIPTS -->
        <script src="scripts/lib/angular/angular.min.js"></script>
    </head>
    <body>
        <div class="panel panel-default">
            
            <!--
            <div class="panel-body">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h2 class="timer" id="timer-1-title"></h2>
                    </div>
                    <div class="panel-body first">
                        <div class="panel-heading">
                            <div class="row">
                                <h1>
                                    <b><div class="timer" id="timer1-countdown"></div></b>
                                </h1>
                            </div>
                        </div>
                    </div>                                
                </div>
            </div>

            <div class="panel-body">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h2 class="timer" id="timer-2-title"></h2>
                    </div>
                    <div class="panel-body second">
                        <div class="panel-heading">
                            <div class="row">
                                <h1>
                                    <b><div class="timer" id="timer2-countdown"></div></b>
                                </h1>
                            </div>
                        </div>
                    </div>                                
                </div>
            </div>
            -->

            <div class="panel-body">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h2 class="timer" id="timer-3-title"></h2>
                    </div>
                    <div class="panel-body second">
                        <div class="panel-heading">
                            <div class="row">
                                <h1>
                                    <b><div class="timer" id="timer3-countdown"></div></b>
                                </h1>
                            </div>
                        </div>
                    </div>                                
                </div>
            </div>

            <div class="panel-body">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h2 class="timer" id="timer-4-title"></h2>
                    </div>
                    <div class="panel-body second">
                        <div class="panel-heading">
                            <div class="row">
                                <h1>
                                    <b><div class="timer" id="timer4-countdown"></div></b>
                                </h1>
                            </div>
                        </div>
                    </div>                                
                </div>
            </div>

            <div class="panel-body">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h2 class="timer" id="timer-5-title"></h2>
                    </div>
                    <div class="panel-body second">
                        <div class="panel-heading">
                            <div class="row">
                                <h1>
                                    <b><div class="timer" id="timer5-countdown"></div></b>
                                </h1>
                            </div>
                        </div>
                    </div>                                
                </div>
            </div>
        </div>
    </body>
    
    <style>
        h1 {
            margin-top: 10px;
        }
        h2 {
            font-weight:bold;
            margin-top: 10px;
        }
        .panel {
            margin-bottom: 8px;
        }
        .panel-heading {
            padding-top: 3px;
            padding-bottom: 3px;
            padding-left: 10px;
            padding-right: 10px;
        }
        .panel-body {
            padding-top: 5px;
            padding-bottom: 5px;
            padding-left: 10px;
            padding-right: 10px;
        }
        .panel-default {
            border:6px solid #ddd;            
        }
        .panel-body.first {
            background: red;
        }
        .panel-body.second {
            background: #ffff33;
        }
        h2.timer, div.timer {
            font-size: 3vw;
            font-size: 5.5vh;
        }
    </style>

    <script type="text/javascript">        
        var title1 = "8.50.00";
        var date1 = "2016/07/15";
        
        var title2 = "IOC 8.40.70 BR2-2";
        var date2 = "2016/08/08";
        
        var title3 = "8.50.10";
        var date3 = "2016/10/14";
        
        var title4 = "8.50.20";
        var date4 = "2016/12/14";
        
        var title5 = "8.50.30";
        var date5 = "2017/03/17";
        
        // $("#timer-1-title").text(title1);
        // $("#timer-2-title").text(title2);
        $("#timer-3-title").text(title3);
        $("#timer-4-title").text(title4);
        $("#timer-5-title").text(title5);
                
        /*
        $("#timer1-countdown").countdown(date1, function (event) {
            var $this = $(this).html(event.strftime(''
                    + '<span>%w</span> weeks '
                    + '<span>%d</span> days '
                    + '<span>%H</span> hr '
                    + '<span>%M</span> min '
                    + '<span>%S</span> s '));
        });
        
        $("#timer2-countdown").countdown(date2, function (event) {
            var $this = $(this).html(event.strftime(''
                    + '<span>%w</span> weeks '
                    + '<span>%d</span> days '
                    + '<span>%H</span> hr '
                    + '<span>%M</span> min '
                    + '<span>%S</span> s '));
        });
        */
        
        $("#timer3-countdown").countdown(date3, function (event) {
            var $this = $(this).html(event.strftime(''
                    + '<span>%w</span> weeks '
                    + '<span>%d</span> days '
                    + '<span>%H</span> hr '
                    + '<span>%M</span> min '
                    + '<span>%S</span> s '));
        });
        
        $("#timer4-countdown").countdown(date4, function (event) {
            var $this = $(this).html(event.strftime(''
                    + '<span>%w</span> weeks '
                    + '<span>%d</span> days '
                    + '<span>%H</span> hr '
                    + '<span>%M</span> min '
                    + '<span>%S</span> s '));
        });      
        
        $("#timer5-countdown").countdown(date5, function (event) {
            var $this = $(this).html(event.strftime(''
                    + '<span>%w</span> weeks '
                    + '<span>%d</span> days '
                    + '<span>%H</span> hr '
                    + '<span>%M</span> min '
                    + '<span>%S</span> s '));
        });       
    </script>
</html>
