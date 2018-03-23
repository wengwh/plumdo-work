/**
 * 群组控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('GroupController', [ '$scope', '$uibModal','$window','$sce', function($scope, $uibModal,$window,$sce) {
    $scope.groups = $scope.IdmService($scope.restUrl.groups);

    $scope.queryGroups = function() {
      $scope.groups.get({
        params : $scope.query
      }, function(response) {
      	$scope.tableData = response;
        $scope.data = response.data;
        $scope.dataTotal = response.total;
      });
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
    		loadFunction:$scope.queryGroups,
    		queryParams:$scope.query,
    		sortName:'id',
    		sortOrder:'asc'
    	};
    
    

    $scope.deleteGroup = function(id){
  		$scope.confirmModal({
  			title:'确认删除群组',
  			confirm:function(isConfirm){
  				if(isConfirm){
  					$scope.groups.delete({
  	          urlPath : '/' + id
  	        }, function(data, status) {
  	          $scope.showSuccessMsg('删除群组成功');
  	          $scope.queryGroups();
  	        });
  				}
  			}
  		});
  	};
    
    $scope.openModal = function(id) {
      $scope.id = id;
      $uibModal.open({
        templateUrl : 'group-edit.html',
        controller : 'GroupModalCtrl',
        scope : $scope
      });
    };
    
    
    $scope.queryGroups();
    
    
  } ]);

  angular.module('adminApp').controller('GroupModalCtrl', [ '$scope', '$uibModalInstance', function($scope, $uibModalInstance) {
    $scope.formdata = {};

    if ($scope.id) {
      $scope.modalTitle = "修改群组";

      $scope.groups.get({
        urlPath : '/' + $scope.id
      }, function(data, status) {
        $scope.formdata = data;
      });

      $scope.ok = function() {
        $scope.groups.put({
          urlPath : '/' + $scope.id,
          data : $scope.formdata
        }, function(data, status) {
          $uibModalInstance.close();
          $scope.showSuccessMsg('修改群组成功');
          $scope.queryGroups();
        });
      };

    } else {
      $scope.modalTitle = "添加群组";
      $scope.ok = function() {
        $scope.groups.post({
          data : $scope.formdata
        }, function(data, status) {
          $uibModalInstance.close();
          $scope.showSuccessMsg('添加群组成功');
          $scope.queryGroups();
        });
      };
    }

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  } ]);

})();
