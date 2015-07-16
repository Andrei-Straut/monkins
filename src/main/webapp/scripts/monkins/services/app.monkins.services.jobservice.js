

monkins.service('JobService', ['WebSocketFactory',
    function (WebSocketFactory) {
        var _jobs = [];

        this.initJobs = function () {
            var pollingJobs = WebSocketFactory.getJobsList();
            pollingJobs.then(function (response) {
                _jobs = response.data.jobs;

                subscribe();
            }).catch(function (e) {
                console.log(e.description);
            });
        };

        var subscribe = function () {
            var pollingJobs = WebSocketFactory.subscribe();

            pollingJobs.then(function (response) {
                if (response.status === 200) {
                } else {
                    console.log(response);
                }
            }, function (error) {
                console.log(error.description);
            }, function (update) {
                var data = update.data;

                for (var i = 0; i < _jobs.length; i++) {
                    if ((_jobs[i]).name === data.name) {
                        _jobs[i] = data;
                    }
                }
            });
        };

        this.getJobs = function () {
            return _jobs;
        };
    }]);
