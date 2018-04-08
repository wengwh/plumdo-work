/**
 * 群组控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
	'use strict';

	angular.module('adminApp').controller('GroupController',
		function($scope,$stateParams) {
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
				});
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
				$scope.$state.go($scope.$state.current, {cacheParams: $scope.cacheParams},{reload:$scope.$state.current});
			};
			
			$scope.returnParent = function() {
				$scope.cacheParams.queryParams=$scope.cacheParams.queryParamsArray.pop();
				$scope.cacheParams.parentGroup=$scope.cacheParams.parentGroupArray.pop();
				$scope.$state.go($scope.$state.current, {cacheParams: $scope.cacheParams},{reload:$scope.$state.current});
			};

			$scope.queryGroupUser = function(id) {
				$scope.tableModal({
					service : function(){
						return $scope.IdmService($scope.restUrl.groups+'/'+id+'/users');
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
	  		id : 'group',
	  		data : 'queryResult',
	  		colModels : [
	  			{name:'名称',index:'name',sortable:true,width:'10%'},
	  			{name:'类型',index:'type',sortable:true,width:'7%',
	  				formatter:function(){
	  					return '<span ng-if="row.type==0">父级</span>'+
								'<span ng-if="row.type==1">子级</span>';
  					}
	  			},
	  			{name:'状态',index:'status',sortable:true,width:'11%',
	  				formatter:function(){
	  					return '<toggle-switch ng-init="switch=(row.status==0)" ng-model="switch" class="switch-small switch-info" '+
	  						'on-label="启用" off-label="停用" on-change="switchStaus(row.id)"></toggle-switch>';
  					}
	  			},
	  			{name:'排序',index:'order',sortable:true,width:'7%'},
	  			{name:'描述',index:'remark',width:'12%'},
	  			{name:'修改时间',index:'lastUpdateTime',sortable:true,width:'12%'},
	  			{name:'操作',index:'',width:'16%',
	  				formatter:function(){
	  					return '<div class="th-btn-group">'+
		  					'<button type="button" class="btn btn-xs btn-info" ng-click=editGroup(row.id)>'+
		  					'<i class="fa fa-pencil"></i>&nbsp;编辑</button>'+
		  					'<button type="button" class="btn btn-xs btn-success" ng-if="row.type==0" ng-click=queryChild(row)>'+
		  					'<i class="fa fa-list"></i>&nbsp;下级</button>'+
		  					'<button type="button" class="btn btn-xs btn-success" ng-if="row.type==1" ng-click=queryGroupUser(row.id)>'+
		  					'<i class="fa fa-list"></i>&nbsp;关联用户</button>'+
		  					'<button type="button" class="btn btn-xs btn-danger" ng-click=deleteGroup(row.id)>'+
		  					'<i class="fa fa-trash-o"></i>&nbsp;删除</button>'+
		  					'</div>';
	  				}
	  			}
	  		],
	  		loadFunction : $scope.queryGroup,
	  		queryParams : $scope.queryParams
	  	};
			
			$scope.queryGroup();
		});
})();
