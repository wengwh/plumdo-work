/**
 * 用户控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('UserController', [ '$scope', '$uibModal','$window','$sce', function($scope, $uibModal,$window,$sce) {
    $scope.users = $scope.IdmService($scope.restUrl.users);

    $scope.queryUsers = function() {
      $scope.users.get({
        params : $scope.query
      }, function(response) {
        $scope.data = response.data;
        $scope.total = response.total;
      });
    };

    $scope.deleteUser = function(id){
  		$scope.confirmModal({
  			title:'确认删除用户',
  			confirm:function(isConfirm){
  				if(isConfirm){
  					$scope.users.delete({
  	          urlPath : '/' + id
  	        }, function(data, status) {
  	          $scope.showSuccessMsg('删除用户成功');
  	          $scope.queryUsers();
  	        });
  				}
  			}
  		});
  	};
    
    $scope.openModal = function(id) {
      $scope.id = id;
      $uibModal.open({
        templateUrl : 'user-edit.html',
        controller : 'UserModalCtrl',
        scope : $scope
      });
    };
    
    
    $scope.queryUsers();
    
    
  } ]);

  angular.module('adminApp').controller('UserModalCtrl', [ '$scope', '$uibModalInstance', function($scope, $uibModalInstance) {
    $scope.formdata = {};

    if ($scope.id) {
      $scope.modalTitle = "修改用户";

      $scope.users.get({
        urlPath : '/' + $scope.id
      }, function(data, status) {
        $scope.formdata = data;
      });

      $scope.ok = function() {
        $scope.users.put({
          urlPath : '/' + $scope.id,
          data : $scope.formdata
        }, function(data, status) {
          $uibModalInstance.close();
          $scope.showSuccessMsg('修改用户成功');
          $scope.queryUsers();
        });
      };

    } else {
      $scope.modalTitle = "添加用户";
      $scope.ok = function() {
        $scope.users.post({
          data : $scope.formdata
        }, function(data, status) {
          $uibModalInstance.close();
          $scope.showSuccessMsg('添加用户成功');
          $scope.queryUsers();
        });
      };
    }

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  } ]);

})();
