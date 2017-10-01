angular.module('NWAReports', [])
    .controller('NWAReportsCtrl', function ($scope, $http, $interval) {
        $scope.stuff = "Hello there";
        $scope.selectedReport = null;
        $scope.data = null;

        var win;
        var checkInterval;

        $http.get("/reports")
            .then(function (response) {
                $scope.reports = response.data;
            });

        $scope.executeReport = function () {
            if ($scope.selectedReport === null) {
                alert("Select a report");
            }
            else {
                var url = "/reports/" + $scope.selectedReport.name;
                $http.get(url)
                    .then(function (response) {
                        $scope.data = response.data;
                    });
            }
        };

        $scope.login = function () {
            $http.get("/login")
                .then(function success(response) {
                        var redirectUrl = response.data.message;
                        win = window.open(redirectUrl, "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,width=400,height=400");
                        checkInterval = $interval(checkForToken, 1000);
                    },
                    function error(response) {
                        alert("Error: " + JSON.stringify(response));
                    });
        };

        var checkForToken = function () {
            $http.get("/isconnected")
                .then(function (response) {
                    var done = response.data.message === "true";
                    if (done) {
                        win.close();
                        $interval.cancel(checkInterval);
                    }
                });
        };
    });