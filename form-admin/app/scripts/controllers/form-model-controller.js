/**
 * 表单模型数据控制层
 * 
 * @author wengwh
 * @Date 2017-02-06
 * 
 */
angular.module('plumdo.controllers').controller('FormModelCtrl',['$scope','$uibModal','$state', function($scope,$uibModal,$state) { 
	$scope.tableData = {};
	$scope.queryParams = {};
	$scope.formModels = $scope.RestService($scope.restUrl.formModels);
	
	$scope.queryFormModels = function(){
		$scope.formModels.get({params:$scope.queryParams},function(data, status){
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
					'<button class="btn btn-danger btn-xs" ng-click=deleteFormModel(row.id) type="button"><i class="fa fa-trash-o"></i>&nbsp;删除</button>'+
					'</div>';
				}
			}
		],
		loadFunction:$scope.queryFormModels,
		queryParams:$scope.queryParams,
		sortName:'id',
		sortOrder:'asc'
	};

	$scope.deleteFormModel = function(id){
		$scope.confirmModal({
			title:'确认删除表单模型',
			confirm:function(isConfirm){
				if(isConfirm){
					$scope.formModels.delete({urlPath:'/'+id},function(data, status){
						$scope.showSuccessMsg('删除表单模型成功');
						$scope.queryFormModels();
					});
				}
			}
		});
	};
	

	$scope.openModal = function (id) {
		$scope.id = id;
		$uibModal.open({
			templateUrl: 'form-model-edit.html',
			controller: 'FormModelModalCtrl',
			scope: $scope
		});
	};

}]);

angular.module('plumdo.controllers').controller('FormModelModalCtrl',['$scope','$uibModalInstance', function($scope,$uibModalInstance) { 
	$scope.formdata = {};

	if($scope.id){
		$scope.modalTitle="修改表单模型";
		
		$scope.formModels.get({urlPath:'/'+$scope.id},function(data, status){
			$scope.formdata = data;
		});
		

		$scope.ok = function () {
			$scope.formModels.update({urlPath:'/'+$scope.id,data:$scope.formdata},function(data, status){
				$uibModalInstance.close();
				$scope.showSuccessMsg('修改表单模型成功');
				$scope.queryFormModels();
			});
		};

	}else{
		$scope.modalTitle="添加表单模型";
		$scope.ok = function () {
			$scope.formModels.create({data:$scope.formdata},function(data, status){
				$uibModalInstance.close();
				$scope.showSuccessMsg('添加表单模型成功');
				$scope.queryFormModels();
			});
		};
	}

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
}]);