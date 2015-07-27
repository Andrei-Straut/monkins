var wsURL = "ws://vm-transnet-009.pt.rd.co-int.net:8080/monkins/controller";

/* global angular */
var monkins = angular.module('monkins', ['ui.bootstrap', 'ui-notification', 'dndLists']);

(function (jQuery) {
    "use strict";
    jQuery(document).ready(function () {
        $(function () {
            $("[data-toggle='tooltip']").tooltip();
        });        
    });

}(jQuery));