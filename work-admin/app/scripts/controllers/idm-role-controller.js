/**
 * 角色控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
	'use strict';

	angular.module('adminApp').controller('RoleController',
		function($scope) {
			$scope.roleService = $scope.IdmService($scope.restUrl.roles);
			$scope.menuService = $scope.IdmService($scope.restUrl.menus);
			$scope.queryResult = {};
			$scope.queryParams = {};

			$scope.queryRole = function() {
				$scope.roleService.get({
					params : $scope.queryParams
				}, function(response) {
					$scope.queryResult = response;
				});
			};

			$scope.deleteRole = function(id) {
				$scope.confirmModal({
					title : '确认删除角色',
					confirm : function(isConfirm) {
						if (isConfirm) {
							$scope.roleService.delete({
								urlPath : '/' + id
							}, function() {
								$scope.showSuccessMsg('删除角色成功');
								$scope.queryRole();
							});
						}
					}
				});
			};

			$scope.editRole = function(id) {
				if(id){
						$scope.roleService.get({
							urlPath : '/' + id +'/menus'
						}, function(response) {
							$scope.openModal(id,response);
						});
				}else{
					$scope.menuService.get({
						urlPath : '/tree'
					}, function(response) {
						$scope.openModal(id,response);
					});
				}
			};
			
			$scope.openModal = function(id,data) {
				$scope.editModal({
					id : id,
					service : $scope.roleService,
					data : function(){
						return {menus:data};
					},
					url : function() {
						return angular.copy('role-edit.html');
					},
					title : function() {
						return angular.copy('角色');
					},
					complete : function() {
						return $scope.queryRole;
					}
				});
			};
			
			$scope.tableOptions = {
	  		id : 'role',
	  		data : 'queryResult',
	  		colModels : [
	  			{name:'名称',index:'name',sortable:true,width:'10%'},
	  			{name:'备注',index:'remark',width:'10%'},
	  			{name:'创建时间',index:'createTime',sortable:true,width:'15%'},
	  			{name:'修改时间',index:'lastUpdateTime',sortable:true,width:'15%'},
	  			{name:'操作',index:'',width:'10%',
	  				formatter:function(){
	  					return '<div class="th-btn-group">'
		  					+'<button type="button" class="btn btn-info btn-xs" ng-click=editRole(row.id)>'
		  					+'<i class="fa fa-pencil"></i>&nbsp;编辑</button>'
		  					+'<button type="button" class="btn btn-danger btn-xs" ng-click=deleteRole(row.id)>'
		  					+'<i class="fa fa-trash-o"></i>&nbsp;删除</button>'
		  					+'</div>';
	  				}
	  			}
	  		],
	  		loadFunction : $scope.queryRole,
	  		queryParams : $scope.queryParams,
	  		sortName : 'name',
	  		sortOrder : 'asc'
	  	};
			
			$scope.queryRole();
		});

})();
