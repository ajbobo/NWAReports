angular.module('ScholarReports', [])
    .controller('ScholarReportsCtrl', function ($scope, $http) {
        $scope.selectedReport = null;
        $scope.data = null;
        $scope.hidePii = false;


        $http.get("/reports")
            .then(function (response) {
                $scope.reports = response.data;
            });

        $scope.executeReport = function () {
            if ($scope.selectedReport === null) {
                alert("Select a report");
            }
            else {
                var url = "/reports/" + $scope.selectedReport.name + "?hidepii=" + $scope.hidePii;
                $http.get(url)
                    .then(function (response) {
                        $scope.data = response.data;
                    });
            }
        };
    });