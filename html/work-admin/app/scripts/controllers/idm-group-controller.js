/**
 * 群组控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
	'use strict';

	angular.module('adminApp').controller('GroupController',
		function($scope,$state,$stateParams) {
			$scope.groupService = $scope.IdmService($scope.restUrl.groups);
			$scope.cacheParams = $stateParams.cacheParams || {};
			$scope.parentGroup = $scope.cacheParams.parentGroup||{id:0};
			$scope.queryParams = $scope.cacheParams.queryParams||{};
			$scope.queryParams.parentId = $scope.parentGroup.id;
			$scope.queryResult = {};
			
			$scope.queryGroup = function() {
				$scope.groupService.get({
					params : $scope.queryParams
				}, function(response) {
						$scope.queryResult = response;
				});
			};

			$scope.deleteGroup = function(id) {
				$scope.confirmModal({
					title : '确认删除群组',
					confirm : function(isConfirm) {
						if (isConfirm) {
							$scope.groupService.delete({
								urlPath : '/' + id
							}, function() {
								$scope.showSuccessMsg('删除群组成功');
								$scope.queryGroup();
							});
						}
					}
				});
			};

			$scope.editGroup = function(id) {
				$scope.editModal({
						id : id,
						data : function() {
								return {parentId:$scope.parentGroup.id, parentName:$scope.parentGroup.name};
						},
						service : $scope.groupService,
						url : function() {
							return angular.copy('group-edit.html');
						},
						title : function() {
							return angular.copy('群组');
						},
						complete : function() {
							return $scope.queryGroup;
						}
				})
			};
			
			$scope.switchStaus = function(id){
				$scope.groupService.put({
					urlPath : '/' + id +'/switch'
				}, function() {
					$scope.showSuccessMsg('修改状态成功');
					$scope.queryGroup();
				});
			};
			
			$scope.queryChild = function(item) {
				$scope.cacheParams.queryParamsArray.push($scope.queryParams);
				$scope.cacheParams.queryParams={};
				$scope.cacheParams.parentGroupArray.push($scope.cacheParams.parentGroup);
				$scope.cacheParams.parentGroup=item;
        $state.go('main.idm.group', {cacheParams: $scope.cacheParams},{reload:true});
			};
			
			$scope.returnParent = function() {
				$scope.cacheParams.queryParams=$scope.cacheParams.queryParamsArray.pop();
				$scope.cacheParams.parentGroup=$scope.cacheParams.parentGroupArray.pop();
        $state.go('main.idm.group', {cacheParams: $scope.cacheParams},{reload:true});
			};

			$scope.queryGroupUser = function(id) {
				$scope.groupId = id;
				$scope.tableModal('QueryGroupUserController',$scope);
			};

			$scope.tableOptions = {
	  		id : 'group',
	  		data : 'queryResult',
	  		colModels : [
	  			{name:'名称',index:'name',sortable:true,width:'10%'},
	  			{name:'类型',index:'type',sortable:true,width:'7%',
	  				formatter:function(){
	  					return '<span ng-if="row.type==0">父级</span>'
	  								+'<span ng-if="row.type==1">子级</span>'
  					}
	  			},
	  			{name:'状态',index:'status',sortable:true,width:'11%',
	  				formatter:function(){
	  					return '<toggle-switch ng-init="switch=(row.status==0)" ng-model="switch" class="switch-small switch-info" '
	  						+'on-label="启用" off-label="停用" on-change="switchStaus(row.id)"></toggle-switch>';
  					}
	  			},
	  			{name:'排序',index:'order',sortable:true,width:'7%'},
	  			{name:'描述',index:'remark',width:'12%'},
	  			{name:'修改时间',index:'lastUpdateTime',sortable:true,width:'12%'},
	  			{name:'操作',index:'',width:'16%',
	  				formatter:function(){
	  					return '<div class="th-btn-group">'
		  					+'<button type="button" class="btn btn-xs btn-info" ng-click=editGroup(row.id)>'
		  					+'<i class="fa fa-pencil"></i>&nbsp;编辑</button>'
		  					+'<button type="button" class="btn btn-xs btn-success" ng-if="row.type==0" ng-click=queryChild(row)>'
		  					+'<i class="fa fa-list"></i>&nbsp;下级</button>'
		  					+'<button type="button" class="btn btn-xs btn-success" ng-if="row.type==1" ng-click=queryGroupUser(row.id)>'
		  					+'<i class="fa fa-list"></i>&nbsp;关联用户</button>'
		  					+'<button type="button" class="btn btn-xs btn-danger" ng-click=deleteGroup(row.id)>'
		  					+'<i class="fa fa-trash-o"></i>&nbsp;删除</button>'
		  					+'</div>';
	  				}
	  			}
	  		],
	  		loadFunction : $scope.queryGroup,
	  		queryParams : $scope.queryParams
	  	};
			
			$scope.queryGroup();
		});
	
	angular.module('adminApp').controller('QueryGroupUserController',
			function($scope) {
				$scope.queryUserResult = [];
				$scope.tableOptions = {
		  		id : 'groupUser',
		  		data : 'queryUserResult',
		  		isPage : false,
		  		colModels : [
		  			{name:'名称',index:'name',width:'10%'},
		  			{name:'电话',index:'phone',width:'10%'},
		  			{name:'状态',index:'status',width:'7%',
		  				formatter:function(){
		  					return '<span class="label label-success" ng-if="row.status==0">启用</span>'
		  								+'<span class="label label-danger" ng-if="row.status==1">停用</span>'
	  					}
		  			},
		  			{name:'操作',index:'',width:'10%',
		  				formatter:function(){
		  					return '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteGroupUser(row.id)>'
			  					+'<i class="fa fa-trash-o"></i>&nbsp;删除关联</button>';
		  				}
		  			}
		  		]
		  	};

				$scope.queryGroupUser = function() {
					$scope.groupService.get({
						urlPath : '/'+$scope.groupId+'/users'
					}, function(response) {
						$scope.queryUserResult = response;
					});
				};
				
				$scope.deleteGroupUser = function(userId) {
					$scope.groupService.delete({
						urlPath : '/'+$scope.groupId+'/users/'+userId
					}, function(response) {
						$scope.showSuccessMsg('删除关联成功');
						$scope.queryGroupUser();
					});
				};
				
				$scope.queryGroupUser();
				
			});

})();
