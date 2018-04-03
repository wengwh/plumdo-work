/**
 * 菜单控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
	'use strict';

	angular.module('adminApp').controller('MenuController',
		function($scope) {
			$scope.menuService = $scope.IdmService($scope.restUrl.menus);
			$scope.queryResult = {};
			$scope.queryParams = {};
			$scope.queryChildResult = {};
			$scope.queryChildParams = {};
			$scope.parentMenu = {};
			$scope.viewChild = false;
			
			$scope.queryMenu = function() {
				$scope.menuService.get({
					params : $scope.viewChild ? $scope.queryChildParams:$scope.queryParams
				}, function(response) {
					if($scope.viewChild){
						$scope.queryChildResult = response;
					}else{
						$scope.queryResult = response;
					}
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
							if($scope.viewChild){
								return {code: 'C'+Math.ceil(Math.random()*10000), type:1, parentId:$scope.parentMenu.id, parentName:$scope.parentMenu.name};
							}else{
								return {code: 'P'+Math.ceil(Math.random()*10000), type:0, parentId:0};
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
				})
			};
			
			$scope.queryChild = function(item) {
				$scope.parentMenu = item;
				$scope.viewChild = true;
				$scope.queryChildParams.parentId = item.id;
				$scope.queryChildParams.name = '';
				$scope.queryChildParams.pageNum = 1;
				$scope.queryMenu();
			};
			
			$scope.returnParent = function() {
				$scope.parentMenu = {};
				$scope.viewChild = false;
				$scope.queryParams.parentId = 0;
				$scope.queryMenu();
			};
			
			$scope.tableOptions = {
	  		id : 'menu',
	  		data : 'queryResult',
	  		colModels : [
	  			{name:'名称',index:'name',sortable:true,width:'10%'},
	  			{name:'编号',index:'code',sortable:true,width:'7%'},
          {name:'路径',index:'url',sortable:true,width:'10%'},
	  			{name:'图标',index:'icon',width:'7%',
	  				formatter:function(){
	  					return '<div class="th-icon"><span class="{{\'fa \'+row.icon}}"></span></div>';
	  				}
	  			},
	  			{name:'排序',index:'order',sortable:true,width:'7%'},
	  			{name:'描述',index:'remark',width:'12%'},
	  			{name:'修改时间',index:'lastUpdateTime',sortable:true,width:'12%'},
	  			{name:'操作',index:'',width:'15%',
	  				formatter:function(){
	  					return '<div class="th-btn-group">'
		  					+'<button type="button" class="btn btn-xs btn-info" ng-click=editMenu(row.id)>'
		  					+'<i class="fa fa-pencil"></i>&nbsp;编辑</button>'
		  					+'<button type="button" class="btn btn-xs btn-success" ng-click=queryChild(row)>'
		  					+'<i class="fa fa-list"></i>&nbsp;下级</button>'
		  					+'<button type="button" class="btn btn-xs btn-danger" ng-click=deleteMenu(row.id)>'
		  					+'<i class="fa fa-trash-o"></i>&nbsp;删除</button>'
		  					+'</div>';
	  				}
	  			}
	  		],
	  		loadFunction : $scope.queryMenu,
	  		queryParams : $scope.queryParams,
	  		sortName : 'name',
	  		sortOrder : 'asc'
	  	};
			
			
			$scope.childTableOptions = {
	  		id : 'childMenu',
	  		data : 'queryChildResult',
	  		colModels : [
	  			{name:'名称',index:'name',sortable:true,width:'10%'},
	  			{name:'编号',index:'code',sortable:true,width:'7%'},
	  			{name:'路径',index:'url',sortable:true,width:'10%'},
	  			{name:'图标',index:'icon',width:'7%',
	  				formatter:function(){
	  					return '<div class="th-icon"><span class="{{\'fa \'+row.icon}}"></span></div>';
	  				}
	  			},
	  			{name:'排序',index:'order',sortable:true,width:'7%'},
	  			{name:'描述',index:'remark',width:'12%'},
	  			{name:'修改时间',index:'lastUpdateTime',sortable:true,width:'12%'},
	  			{name:'操作',index:'',width:'10%',
	  				formatter:function(){
	  					return '<div class="th-btn-group">'
		  					+'<button type="button" class="btn btn-xs btn-info" ng-click=editMenu(row.id)>'
		  					+'<i class="fa fa-pencil"></i>&nbsp;编辑</button>'
		  					+'<button type="button" class="btn btn-xs btn-danger" ng-click=deleteMenu(row.id)>'
		  					+'<i class="fa fa-trash-o"></i>&nbsp;删除</button>'
		  					+'</div>';
	  				}
	  			}
	  		],
	  		loadFunction : $scope.queryMenu,
	  		queryParams : $scope.queryChildParams,
	  		sortName : 'name',
	  		sortOrder : 'asc'
	  	};
			
			$scope.returnParent();
		});

})();
