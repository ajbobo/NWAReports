angular.module('NWAReports', [])
    .controller('NWAReportsCtrl', function ($scope, $http) {
        $scope.stuff = "Hello there";
        $scope.selectedReport = null;

        $http.get("/reports")
            .then(function(response) {
                $scope.reports = response.data;
            });
    });