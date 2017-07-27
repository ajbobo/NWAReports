angular.module('NWAReports', [])
    .controller('NWAReportsCtrl', function ($scope, $http) {
        $scope.stuff = "Hello there";
        $scope.selectedReport = null;
        $scope.data = null;

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

        $http.get("/reports")
            .then(function (response) {
                $scope.reports = response.data;
            });
    });