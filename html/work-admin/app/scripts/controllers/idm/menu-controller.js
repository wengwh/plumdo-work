/**
 * 菜单控制器
 * 
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
	'use strict';

	angular.module('adminApp').controller('MenuController',
		function($scope,$stateParams) {
			$scope.menuService = $scope.IdmService($scope.restUrl.idmMenus);
			$scope.cacheParams = $stateParams.cacheParams || {};
			$scope.parentMenu = $scope.cacheParams.parentMenu||{id:0};
			$scope.queryParams = $scope.cacheParams.queryParams||{};
			$scope.queryParams.parentId = $scope.parentMenu.id;
			$scope.queryResult = {};
			
			$scope.queryMenu = function() {
				$scope.menuService.get({
					params : $scope.queryParams
				}, function(response) {
						$scope.queryResult = response;
				});
			};

			$scope.deleteMenu = function(id) {
				$scope.confirmModal({
					title : '确认删除菜单',
					confirm : function(isConfirm) {
						if (isConfirm) {
							$scope.menuService.delete({
								urlPath : '/' + id
							}, function() {
								$scope.showSuccessMsg('删除菜单成功');
								$scope.queryMenu();
							});
						}
					}
				});
			};

			$scope.editMenu = function(id) {
				$scope.editModal({
						id : id,
						data : function() {
							if($scope.parentMenu.id!==0){
								return {type:1, parentId:$scope.parentMenu.id, parentName:$scope.parentMenu.name};
							}else{
								return {type:0, parentId:0};
							}
						},
						service : $scope.menuService,
						url : function() {
							return angular.copy('menu-edit.html');
						},
						title : function() {
							return angular.copy('菜单');
						},
						complete : function() {
							return $scope.queryMenu;
						}
				});
			};
			
			$scope.switchStaus = function(id){
				$scope.menuService.put({
					urlPath : '/' + id +'/switch'
				}, function() {
					$scope.showSuccessMsg('修改状态成功');
					$scope.queryMenu();
				});
			};
			
			$scope.queryChild = function(item) {
				$scope.cacheParams.queryParamsArray.push($scope.queryParams);
				$scope.cacheParams.queryParams={};
				$scope.cacheParams.parentMenu=item;
				$scope.$state.go($scope.$state.current, {cacheParams: $scope.cacheParams},{reload:$scope.$state.current});
			};
			
			$scope.returnParent = function() {
				$scope.cacheParams.queryParams=$scope.cacheParams.queryParamsArray.pop();
				$scope.cacheParams.parentMenu=null;
				$scope.$state.go($scope.$state.current, {cacheParams: $scope.cacheParams},{reload:$scope.$state.current});
			};
			
			$scope.queryMenuRole = function(id) {
				$scope.tableModal({
					service : function(){
						return $scope.IdmService($scope.restUrl.idmMenus+'/'+id+'/roles');
					},
					colModels : function(){
						return [
							{name:'名称',index:'name'},
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
	  		id : 'menu',
	  		data : 'queryResult',
	  		colModels : [
	  			{name:'名称',index:'name',sortable:true,width:'10%'},
	  			{name:'路径',index:'route',sortable:true,width:'9%'},
	  			{name:'图标',index:'icon',width:'7%',
	  				formatter:function(){
	  					return '<div class="th-icon"><span class="{{\'fa \'+row.icon}}"></span></div>';
	  				}
	  			},
	  			{name:'状态',index:'status',sortable:true,width:'11%',
	  				formatter:function(){
	  					return '<toggle-switch ng-init="switch=(row.status==0)" ng-model="switch" class="switch-small switch-info" '+
  							'on-label="启用" off-label="停用" on-change="switchStaus(row.id)"></toggle-switch>';
  					}
	  			},
	  			{name:'排序',index:'order',sortable:true,width:'7%'},
	  			{name:'修改时间',index:'lastUpdateTime',sortable:true,width:'12%'},
	  			{name:'操作',index:'',width:'15%',
	  				formatter:function(){
	  					return '<div class="th-btn-group">'+
		  					'<button type="button" class="btn btn-xs btn-info" ng-click=editMenu(row.id)>'+
		  					'<i class="fa fa-pencil"></i>&nbsp;编辑</button>'+
		  					'<button type="button" class="btn btn-xs btn-success" ng-if="row.type==0" ng-click=queryChild(row)>'+
		  					'<i class="fa fa-list"></i>&nbsp;下级</button>'+
		  					'<button type="button" class="btn btn-xs btn-success" ng-if="row.type!=0" ng-click=queryMenuRole(row.id)>'+
		  					'<i class="fa fa-list"></i>&nbsp;关联角色</button>'+
		  					'<button type="button" class="btn btn-xs btn-danger" ng-click=deleteMenu(row.id)>'+
		  					'<i class="fa fa-trash-o"></i>&nbsp;删除</button>'+
		  					'</div>';
	  				}
	  			}
	  		],
	  		loadFunction : $scope.queryMenu,
	  		queryParams : $scope.queryParams,
	  		sortName : 'order',
	  		sortOrder : 'asc'
	  	};

			$scope.queryMenu();
	});

})();
