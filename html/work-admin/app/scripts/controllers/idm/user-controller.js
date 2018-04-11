/**
 * 用户控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('UserController', 
  		function($scope, $q) {
    $scope.userService = $scope.IdmService($scope.restUrl.idmUsers);
		$scope.queryResult = {};
		$scope.queryParams = {};

    $scope.queryUser = function() {
      $scope.userService.get({
        params : $scope.queryParams
      }, function(response) {
				$scope.queryResult = response;
      });
    };

    $scope.deleteUser = function(id){
  		$scope.confirmModal({
  			title:'确认删除用户',
  			confirm:function(){
					$scope.userService.delete({
	          urlPath : '/' + id
	        }, function() {
	          $scope.showSuccessMsg('删除用户成功');
	          $scope.queryUser();
	        });
  			}
  		});
  	};
  	
  	$scope.editUser = function(id) {
  		var formData = {};
  		var rolesPromise = $scope.userService.get({
				urlPath : '/roles',
				params : {id:id}
			}, function(response) {
				formData.roles = response;
			});
  		
  		var groupsPromise = $scope.userService.get({
				urlPath : '/groups',
				params : {id:id}
			}, function(response) {
				formData.groups = response;
			});

  		$q.all([rolesPromise,groupsPromise]).then(function() {  
  			$scope.editModal({
					id : id,
					formUrl : 'user-edit.html',
					title : '用户',
					formData : formData,
					service : $scope.userService,
					complete : $scope.queryUser
  			});
  		});
		};
    
    $scope.queryUser();
  });
})();
