/**
 * 流程模型控制器
 *
 * @author wengwenhui
 * @date 2018年4月9日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('FlowController', 
  		function($scope,$window) {
    $scope.modelService = $scope.FlowService($scope.restUrl.flowModels);
		$scope.queryResult = {};
		$scope.queryParams = {};
		$scope.selectedItem = null;
		
    $scope.queryModel = function() {
      $scope.modelService.get({
        params : $scope.queryParams
      }, function(response) {
				$scope.queryResult = response;
      });
    };

    $scope.deleteModel = function(id){
  		$scope.confirmModal({
  			title:'确认删除模型',
  			confirm:function(isConfirm){
  				if(isConfirm){
  					$scope.modelService.delete({
  	          urlPath : '/' + id
  	        }, function() {
  	          $scope.showSuccessMsg('删除模型成功');
  	          $scope.queryModel();
  	        });
  				}
  			}
  		});
  	};
  	
  	$scope.editModel = function(id) {
			$scope.editModal({
				id : function() { return id; },
				data : {},
				service : $scope.modelService,
				url : function() { return 'flow-model-edit.html'; },
				title : function() { return '模型'; },
				complete : function() { return $scope.queryModel; }
			});
		};
		
		$scope.modelDetail = function(item){
			$scope.selectedItem = item;
		};
		
		$scope.returnList = function(item){
			$scope.selectedItem = null;
	    $scope.queryModel();
		};
	

    $scope.designModel = function(modelId){
      $window.open($scope.restUrl.flowDesign(modelId));
    };
    
    
    $scope.queryModel();
  });
})();
