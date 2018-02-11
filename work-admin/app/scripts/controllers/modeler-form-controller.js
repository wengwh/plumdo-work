/**
 * 主窗口控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('FormModelerController', [ '$scope', '$uibModal','$window','$sce', function($scope, $uibModal,$window,$sce) {
    $scope.formModels = $scope.RestService($scope.restUrl.formModels);

    $scope.queryFormModels = function() {
      $scope.formModels.get({
        params : $scope.query
      }, function(response) {
        $scope.data = response.data;
        $scope.dataTotal = response.dataTotal;
      });
    };

    $scope.openModal = function(id) {
      $scope.id = id;
      $uibModal.open({
        templateUrl : 'form-model-edit.html',
        controller : 'FormModelModalCtrl',
        scope : $scope
      });
    };
    
    $scope.desginForm = function(modelId){
      $window.open($scope.restUrl.formDesgin(modelId));
    };
    
    $scope.previewForm = function(modelId){
      $scope.previewUrl = $sce.trustAsResourceUrl($scope.restUrl.formPreview(modelId));
      $uibModal.open({
        templateUrl : 'form-model-watch.html',
        size:'lg',  
        scope : $scope
      });
      
    };
    
    $scope.queryFormModels();
    
    
  } ]);

  angular.module('adminApp').controller('FormModelModalCtrl', [ '$scope', '$uibModalInstance', function($scope, $uibModalInstance) {
    $scope.formdata = {};

    if ($scope.id) {
      $scope.modalTitle = "修改表单模型";

      $scope.formModels.get({
        urlPath : '/' + $scope.id
      }, function(data, status) {
        $scope.formdata = data;
      });

      $scope.ok = function() {
        $scope.formModels.put({
          urlPath : '/' + $scope.id,
          data : $scope.formdata
        }, function(data, status) {
          $uibModalInstance.close();
          $scope.showSuccessMsg('修改表单模型成功');
          $scope.queryFormModels();
        });
      };

    } else {
      $scope.modalTitle = "添加表单模型";
      $scope.ok = function() {
        $scope.formModels.post({
          data : $scope.formdata
        }, function(data, status) {
          $uibModalInstance.close();
          $scope.showSuccessMsg('添加表单模型成功');
          $scope.queryFormModels();
        });
      };
    }

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  } ]);

})();
