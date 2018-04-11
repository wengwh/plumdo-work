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
		$scope.queryParams = {latestVersion:true};
		$scope.selectedItem = null;
		$scope.lastVersion = false;
		$scope.versionNum = 0;

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
  			confirm:function(){
					$scope.modelService.delete({
	          urlPath : '/' + id
	        }, function() {
	          $scope.showSuccessMsg('删除模型成功');
	          $scope.returnList();
	        });
  			}
  		});
  	};

  	$scope.deployModel = function(id){
  		$scope.confirmModal({
  			title:'确认部署模型',
  			confirm:function(){
					$scope.modelService.post({
	          urlPath : '/' + id +'/deploy'
	        }, function() {
	          $scope.showSuccessMsg('部署模型成功');
	        });
  			}
  		});
  	};
  	
  	$scope.exportModel = function(item){
  		$scope.modelService.get({
        urlPath : '/' + item.id +'/xml'
      }, function(response) {
      	$scope.windowExportFile(response,item.name+'-v'+item.version+'.bpmn20.xml')
      });
  	};
  	
  	$scope.editModel = function(id) {
			$scope.editModal({
				id : id,
				formUrl: 'flow-model-edit.html',
				title : '模型',
				service : $scope.modelService,
				complete : function(){
					if(id){
		  			$scope.queryDetail(id);
		  		}else{
						$scope.queryModel();
		  		}
				}
			});
		};
		
		
		$scope.copyModel = function(item) {
    	$scope.editConfirmModal({
    		formUrl: 'flow-model-edit.html',
    		formData: item,
        title: '复制模型',
        confirm: function (formData,modalInstance) {
        	if(formData.key === item.key){
        		$scope.showErrorMsg('模型标识必须变更');
        	}else{
	      		$scope.modelService.post({
	          	urlPath : '/'+item.id+'/copy',
	            data: formData
	          }, function () {
	            $scope.showSuccessMsg('复制模型成功');
	            modalInstance.close();
	          });
        	}
        }
      });
		};
		
		$scope.newestModel = function(item){
  		$scope.confirmModal({
  			title:'确认提升模型为最新版',
  			confirm:function(){
					$scope.modelService.post({
						urlPath : '/'+item.id+'/newest'
	        }, function(response) {
	          $scope.showSuccessMsg('提升模型最新版成功');
	          $scope.selectedItem = response;
	    			$scope.lastVersion = true;
	    			$scope.versionNum = response.version;
	        });
  			}
  		});
  	};
		
		$scope.queryHistory = function(key,version){
			$scope.modelService.get({
        urlPath : '/' + key + '/' +version
      }, function(response) {
  			$scope.selectedItem = response;
  			if($scope.versionNum == response.version){
  				$scope.lastVersion = true;
  			}else{
  				$scope.lastVersion = false;
  			}
      });
		};
		
		$scope.queryDetail = function(id){
			$scope.modelService.get({
        urlPath : '/' + id 
      }, function(response) {
  			$scope.selectedItem = response;
  			$scope.lastVersion = true;
  			$scope.versionNum = response.version;
      });
		};
		
		$scope.returnList = function(){
			$scope.selectedItem = null;
	    $scope.queryModel();
		};
	
    $scope.designModel = function(id){
      $window.open($scope.restUrl.flowDesign(id));
    };

    $scope.getImageUrl = function(id){
    	return $scope.modelService.url +'/'+id+'/image.jpg';
    }
    
    $scope.versionArray = function(){
    	var numArray = [];
    	for(var i=$scope.versionNum; i>0 ;i--){
    		numArray.push(i)
    	}
    	return numArray;
    }
    
    $scope.queryModel();
  });
})();
