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

  	console.info($scope.modelService.url)
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
  	console.info($scope.modelService.url)
    $scope.deployModel = function(id){
  		$scope.confirmModal({
  			title:'确认部署模型',
  			confirm:function(isConfirm){
  				if(isConfirm){
  					$scope.modelService.post({
  	          urlPath : '/' + id +'/deploy'
  	        }, function() {
  	          $scope.showSuccessMsg('部署模型成功');
  	        });
  				}
  			}
  		});
  	};
  	
  	$scope.importModel = function(id){
  		$scope.modelService.get({
        urlPath : '/' + id +'/xml'
      }, function(response) {
      	$scope.windowExportFile(response,'xml.xml')
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
		
		
		$scope.copyModel = function(id) {
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
	

    $scope.designModel = function(id){
      $window.open($scope.restUrl.flowDesign(id));
    };
    

    $scope.getImageUrl = function(id){
    	return $scope.modelService.url +'/'+id+'/image.jpg';
    }
    
    
    $scope.queryModel();
  });
})();
