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
							if(!id){
								return {code: 'Y'+Math.ceil(Math.random()*10000), type:0,icon:''};
							}else{
								return {code: 'M'+Math.ceil(Math.random()*10000), type:1,icon:''};
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
			
			$scope.tableOptions = {
	  		id : 'menu',
	  		data : 'queryResult',
	  		colModels : [
	  			{name:'名称',index:'name',sortable:true,width:'10%'},
	  			{name:'编号',index:'code',sortable:true,width:'10%'},
	  			{name:'路径',index:'url',sortable:true,width:'10%'},
	  			{name:'图标',index:'icon',width:'10%'},
	  			{name:'排序',index:'order',sortable:true,width:'10%'},
	  			{name:'描述',index:'remark',width:'10%'},
	  			{name:'修改时间',index:'lastUpdateTime',sortable:true,width:'15%'},
	  			{name:'操作',index:'',width:'10%',
	  				formatter:function(){
	  					return '<div class="btn-group">'
		  					+'<button type="button" class="btn btn-info btn-xs" ng-click=editMenu(row.id)>'
		  					+'<i class="fa fa-pencil"></i>&nbsp;修改</button>'
		  					+'<button type="button" class="btn btn-danger btn-xs" ng-click=deleteMenu(row.id)>'
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
			
			$scope.queryMenu();
		});

})();
