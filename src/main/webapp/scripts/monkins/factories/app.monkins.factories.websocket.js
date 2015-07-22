/**
 * Websocket factory
 */
monkins.factory('WebSocketFactory', ['$q', '$rootScope',
    function ($q, $rootScope) {

        // We return this object to anything injecting our service
        var Service = {};
        // Keep all pending requests here until they get responses
        var callbacks = {};
        // Create a unique callback ID to map requests to responses
        var currentCallbackId = 0;
        // Create our websocket object with the address to the websocket
        var ws = new WebSocket(wsURL);

        var UPDATE_JOB_MESSAGE = 'UPDATEJOB';
        var UPDATE_SETTINGS_MESSAGE = 'UPDATESETTINGS';

        ws.onopen = function (event) {
            console.log("Socket opened!");
        };
        ws.onmessage = function (message) {
            listener(JSON.parse(message.data));
        };
        ws.onclose = function (event) {
            console.log("Connection closed");
        };
        Service.getJobsList = function () {
            var request = {
                type: "GetJobsList",
                data: {}
            };
            var promise = sendRequest(request);
            return promise;
        };
        Service.subscribe = function () {
            var request = {
                type: "Subscribe",
                data: {}
            };
            var promise = sendRequestWithCallbackId(request, 0);
            return promise;
        };
        Service.unSubscribe = function () {
            var request = {
                type: "Unsubscribe",
                data: {}
            };
            var promise = sendRequest(request);
            return promise;
        };
        Service.unsubscribe = function () {
            var request = {
                type: "Unsubscribe",
                data: {}
            };
            var promise = sendRequest(request);
            return promise;
        };
        Service.unSubscribeAll = function () {
            var request = {
                type: "UnsubscribeAll",
                data: {}
            };
            var promise = sendRequest(request);
            return promise;
        };
        Service.getSettings = function () {
            var request = {
                type: "GetSettings",
                data: {}
            };
            var promise = sendRequest(request);
            return promise;
        };
        Service.updateSettings = function (settings) {
            var request = {
                type: "UpdateSettings",
                data: settings
            };
            var promise = sendRequest(request);
            return promise;
        };
        function sendRequestWithCallbackId(request, givenCallbackId) {
            var defer = $q.defer();
            var callbackId = givenCallbackId;
            callbacks[callbackId] = {
                time: new Date(),
                cb: defer
            };
            request.callback_id = callbackId;

            if (ws.readyState == 0 || ws.readyState == 2 || ws.readyState == 3) {
                var interval = window.setInterval(function () {
                    var response = {};
                    response.callback_id = callbackId;
                    response.status = 410;
                    response.isEnded = true;
                    response.description = 'Connection was closed in the meantime '
                            + '( or hasn\'t been successfully opened), please try and refresh the page';
                    listener(response);

                    window.clearInterval(interval);
                }, 1000);
            } else {
                console.log('Sending request', request);
                ws.send(JSON.stringify(request));
            }

            return defer.promise;
        }
        ;
        function sendRequest(request) {
            return sendRequestWithCallbackId(request, getCallbackId());
        }
        ;
        function listener(data) {
            var messageObj = data;
            // If an object exists with callback_id in our callbacks object, resolve it
            console.log("Received: ", messageObj);

            //If we have an UPDATE or UPDATE_SETTINGS message, we don't care much about callback id
            if (messageObj.description
                    && (messageObj.description.toUpperCase() === UPDATE_JOB_MESSAGE
                            || messageObj.description.toUpperCase() === UPDATE_SETTINGS_MESSAGE)) {

                switch (messageObj.description.toUpperCase()) {
                    case UPDATE_JOB_MESSAGE:
                    {
                        $rootScope.$emit('UPDATEJOB', messageObj.data);
                        break;
                    }
                    case UPDATE_SETTINGS_MESSAGE:
                    {
                        $rootScope.$emit('UPDATESETTINGS', messageObj.data);
                        break;
                    }
                    default:
                    {
                        console.log("Message description unknown: ", messageObj.description);
                    }
                }

                delete callbacks[messageObj.callbackID];
                return;
            }

            // If an object exists with callback_id in our callbacks object, resolve it
            if (callbacks.hasOwnProperty(messageObj.callback_id)) {
                $rootScope.$apply(callbacks[messageObj.callback_id].cb.notify(messageObj));
                if (messageObj.callback_id && messageObj.isEnded && messageObj.isEnded === true) {
                    $rootScope.$apply(callbacks[messageObj.callback_id].cb.resolve(messageObj));
                    console.log("Received response: ", messageObj);
                    console.log("Request with id " + messageObj.callback_id + " completed");
                    delete callbacks[messageObj.callbackID];
                }
                //If not, we have a standalone message, log it
            } else {
                console.log("Received message: ", messageObj);
            }
        }

        // This creates a new callback ID for a request
        function getCallbackId() {
            currentCallbackId += 1;
            return currentCallbackId;
        }

        return Service;
    }]);

