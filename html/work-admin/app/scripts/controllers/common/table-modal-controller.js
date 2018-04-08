/**
 * 编辑框控制
 * 
 * @author wengwh
 */
(function() {
	'use strict';

	angular.module('adminApp').controller('TableModalController',
		function($scope, service,colModels) {
			$scope.queryResult = [];
			
			colModels.push(
				{name:'操作',index:'',
					formatter:function(){
						return '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteRow(row.id)>'+
	  					'<i class="fa fa-trash-o"></i>&nbsp;删除关联</button>';
					}
				}
			);
			
			$scope.tableOptions = {
	  		id : 'tableModal',
	  		data : 'queryResult',
	  		isPage : false,
	  		colModels : colModels
	  	};
	
			$scope.query = function() {
				service.get({
				}, function(response) {
					$scope.queryResult = response;
				});
			};
			
			$scope.deleteRow = function(rowId) {
				service.delete({
					urlPath : '/'+rowId
				}, function() {
					$scope.showSuccessMsg('删除关联成功');
					$scope.query();
				});
			};
			$scope.query();
		});
})();
