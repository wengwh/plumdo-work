/**
 * 角色控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
	'use strict';

	angular.module('adminApp').controller('RoleController',
		function($scope,$q) {
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
				var data = {};
	  		var menusPromise = $scope.roleService.get({
					urlPath : '/menus',
					params : {id:id}
				}, function(response) {
					data.menus = response;
				});
	  		
	  		$q.all([menusPromise]).then(function() {  
		  		$scope.editModal({
						id : id,
						service : $scope.roleService,
						data : function(){
							return data;
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
	  		});
			};
			
			$scope.switchStaus = function(id){
				$scope.roleService.put({
					urlPath : '/' + id +'/switch'
				}, function() {
					$scope.showSuccessMsg('修改状态成功');
					$scope.queryRole();
				});
			};

			$scope.queryRoleUser = function(id) {
				$scope.tableModal({
					service : function(){
						return $scope.IdmService($scope.restUrl.roles+'/'+id+'/users');
					},
					colModels : function(){
						return [
							{name:'名称',index:'name'},
							{name:'电话',index:'phone'},
							{name:'状态',index:'status',
								formatter:function(){
			  					return '<span class="label label-success" ng-if="row.status==0">启用</span>'+
			  						'<span class="label label-danger" ng-if="row.status==1">停用</span>';
								}
							}
						];
					}
				});
			};
			
			$scope.tableOptions = {
	  		id : 'role',
	  		data : 'queryResult',
	  		colModels : [
	  			{name:'名称',index:'name',sortable:true,width:'10%'},
	  			{name:'状态',index:'status',sortable:true,width:'11%',
	  				formatter:function(){
	  					return '<toggle-switch ng-init="switch=(row.status==0)" ng-model="switch" class="switch-small switch-info" '+
	  						'on-label="启用" off-label="停用" on-change="switchStaus(row.id)"></toggle-switch>';
  					}
	  			},
	  			{name:'备注',index:'remark',width:'12%'},
	  			{name:'创建时间',index:'createTime',sortable:true,width:'12%'},
	  			{name:'修改时间',index:'lastUpdateTime',sortable:true,width:'12%'},
	  			{name:'操作',index:'',width:'15%',
	  				formatter:function(){
	  					return '<div class="th-btn-group">'+
		  					'<button type="button" class="btn btn-info btn-xs" ng-click=editRole(row.id)>'+
		  					'<i class="fa fa-pencil"></i>&nbsp;编辑</button>'+
		  					'<button type="button" class="btn btn-xs btn-success" ng-click=queryRoleUser(row.id)>'+
		  					'<i class="fa fa-list"></i>&nbsp;关联用户</button>'+
		  					'<button type="button" class="btn btn-danger btn-xs" ng-click=deleteRole(row.id)>'+
		  					'<i class="fa fa-trash-o"></i>&nbsp;删除</button>'+
		  					'</div>';
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
