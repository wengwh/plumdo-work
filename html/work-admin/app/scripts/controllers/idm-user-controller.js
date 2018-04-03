/**
 * 用户控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('UserController', 
  		function($scope, $q) {
    $scope.userService = $scope.IdmService($scope.restUrl.users);
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
  			confirm:function(isConfirm){
  				if(isConfirm){
  					$scope.userService.delete({
  	          urlPath : '/' + id
  	        }, function(data, status) {
  	          $scope.showSuccessMsg('删除用户成功');
  	          $scope.queryUser();
  	        });
  				}
  			}
  		});
  	};
  	
  	$scope.editUser = function(id) {
  		var data = {};
  		var rolesPromise = $scope.userService.get({
				urlPath : '/roles',
				params : {id:id}
			}, function(response) {
				data.roles = response;
			});
  		
  		var groupsPromise = $scope.userService.get({
				urlPath : '/groups',
				params : {id:id}
			}, function(response) {
				data.groups = response;
			});

  		$q.all([rolesPromise,groupsPromise]).then(function(results) {  
  			$scope.editModal({
					id : id,
					data : function() {
							return data;
					},
					service : $scope.userService,
					url : function() {
						return angular.copy('user-edit.html');
					},
					title : function() {
						return angular.copy('用户');
					},
					complete : function() {
						return $scope.queryUser;
					}
			})
  		});
		
		};
    
    $scope.queryUser();
    
    
  } );


})();
