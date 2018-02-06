/**
 * 主窗口控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('FormModelerController', [ '$scope','$timeout', function($scope,$timeout) {
    $scope.formModels = $scope.RestService($scope.restUrl.formModels);
    
    $scope.queryModels=function(){
      $scope.formModels.get({
        params: $scope.query
      }, function (response) {
        $scope.data = response.data;
        $scope.dataTotal = response.dataTotal;
      });
    };
    
    
   
    
  }]);
})();
