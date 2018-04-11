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
			$scope.roleService = $scope.IdmService($scope.restUrl.idmRoles);
			$scope.menuService = $scope.IdmService($scope.restUrl.idmMenus);
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
					confirm : function() {
						$scope.roleService.delete({
							urlPath : '/' + id
						}, function() {
							$scope.showSuccessMsg('删除角色成功');
							$scope.queryRole();
						});
					}
				});
			};

			$scope.editRole = function(id) {
				var formData = {};
	  		var menusPromise = $scope.roleService.get({
					urlPath : '/menus',
					params : {id:id}
				}, function(response) {
					formData.menus = response;
				});
	  		
	  		$q.all([menusPromise]).then(function() {  
		  		$scope.editModal({
						id : id,
						formUrl : 'role-edit.html',
						title : '角色',
						formData : formData,
						service : $scope.roleService,
						complete : $scope.queryRole
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
					service : $scope.IdmService($scope.restUrl.idmRoles+'/'+id+'/users'),
					colModels : [
						{name:'名称',index:'name'},
						{name:'电话',index:'phone'},
						{name:'状态',index:'status',
							formatter:function(){
		  					return '<span class="label label-success" ng-if="row.status==0">启用</span>'+
		  						'<span class="label label-danger" ng-if="row.status==1">停用</span>';
							}
						}
					]
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
