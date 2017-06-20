/**
 * 表单定义控制层
 * 
 * @author wengwh
 * @Date 2017-02-06
 * 
 */
angular.module('plumdo.controllers').controller('FormDefinitionCtrl',['$scope','$uibModal','$state', function($scope,$uibModal,$state) { 
	$scope.tableData = {};
	$scope.queryParams = {};
	$scope.formDefinitions = $scope.RestService($scope.restUrl.formDefinitions);
	
	$scope.queryFormDefinitions = function(){
		$scope.formDefinitions.get({params:$scope.queryParams},function(data, status){
			$scope.tableData = data;
		})
	};

	$scope.tableOptions = {
		id:'model',
		data:'tableData',
		colModels:[
			{name:'名称',index:'name',sortable:true,width:'10%'},
			{name:'标识',index:'key',sortable:true,width:'10%'},
			{name:'分类',index:'category',sortable:true,width:'10%'},
			{name:'创建时间',index:'createTime',sortable:true,width:'15%'},
			{name:'修改时间',index:'lastUpdateTime',sortable:true,width:'15%'},
			{name:'操作',index:'',width:'10%',
				formatter:function(){
					return '<div class="btn-group">'+
					'<button class="btn btn-primary btn-xs" ng-click=openModal(row.id) type="button"><i class="fa fa-pencil"></i>&nbsp;修改</button>'+
					'<button class="btn btn-danger btn-xs" ng-click=deleteFormDefinition(row.id) type="button"><i class="fa fa-trash-o"></i>&nbsp;删除</button>'+
					'</div>';
				}
			}
		],
		loadFunction:$scope.queryFormDefinitions,
		queryParams:$scope.queryParams,
		sortName:'id',
		sortOrder:'asc'
	};

	$scope.deleteFormDefinition = function(id){
		$scope.confirmModal({
			title:'确认删除表单定义',
			confirm:function(isConfirm){
				if(isConfirm){
					$scope.formDefinitions.delete({urlPath:'/'+id},function(data, status){
						$scope.showSuccessMsg('删除表单定义成功');
						$scope.queryFormDefinitions();
					});
				}
			}
		});
	};
	

	$scope.openModal = function (id) {
		$scope.id = id;
		$uibModal.open({
			templateUrl: 'form-model-edit.html',
			controller: 'FormDefinitionModalCtrl',
			scope: $scope
		});
	};

}]);

angular.module('plumdo.controllers').controller('FormDefinitionModalCtrl',['$scope','$uibModalInstance', function($scope,$uibModalInstance) { 
	$scope.formdata = {};

	if($scope.id){
		$scope.modalTitle="修改表单定义";
		
		$scope.formDefinitions.get({urlPath:'/'+$scope.id},function(data, status){
			$scope.formdata = data;
		});
		

		$scope.ok = function () {
			$scope.formDefinitions.update({urlPath:'/'+$scope.id,data:$scope.formdata},function(data, status){
				$uibModalInstance.close();
				$scope.showSuccessMsg('修改表单定义成功');
				$scope.queryFormDefinitions();
			});
		};

	}else{
		$scope.modalTitle="添加表单定义";
		$scope.ok = function () {
			$scope.formDefinitions.create({data:$scope.formdata},function(data, status){
				$uibModalInstance.close();
				$scope.showSuccessMsg('添加表单定义成功');
				$scope.queryFormDefinitions();
			});
		};
	}

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
}]);