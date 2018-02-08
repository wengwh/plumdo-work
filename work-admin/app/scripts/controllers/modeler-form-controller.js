/**
 * 主窗口控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('FormModelerController', [ '$scope','$uibModal', function($scope,$uibModal) {
    $scope.formModels = $scope.RestService($scope.restUrl.formModels);
    
    $scope.queryModels=function(){
      $scope.formModels.get({
        params: $scope.query
      }, function (response) {
        $scope.data = response.data;
        $scope.dataTotal = response.dataTotal;
      });
    };
    
    
    $uibModal.open({templateUrl: 'views/modeler/assignment-popup.html',
      scope: $scope})
   
    
  }]);
})();
