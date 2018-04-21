/**
 * 弹出框默认的增加和修改操作
 *
 * @author wengwenhui
 * @date 2018年4月11日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('EditModalController', function($scope, $uibModalInstance) {
    $scope.formData = $scope.formData || {};
    
    if ($scope.id) {
      $scope.modalTitle = "修改" + $scope.title;

      $scope.service.get({
        urlPath : '/' + $scope.id
      }, function(data) {
        $scope.formData = angular.extend($scope.formData, data);
      });

      $scope.ok = function() {
        $scope.service.put({
          urlPath : '/' + $scope.id,
          data : $scope.formData
        }, function() {
          $uibModalInstance.close();
          $scope.showSuccessMsg($scope.modalTitle + '成功');
          $scope.complete();
        });
      };

    } else {
      $scope.modalTitle = '添加' + $scope.title;
      $scope.ok = function() {
        $scope.service.post({
          data : $scope.formData
        }, function() {
          $uibModalInstance.close();
          $scope.showSuccessMsg($scope.modalTitle + '成功');
          $scope.complete();
        });
      };
    }

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  });

})();
