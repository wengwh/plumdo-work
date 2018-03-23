/**
 * 角色控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('RoleController', [ '$scope', '$uibModal','$window','$sce', function($scope, $uibModal,$window,$sce) {
    $scope.roles = $scope.IdmService($scope.restUrl.roles);

    $scope.queryRoles = function() {
      $scope.roles.get({
        params : $scope.query
      }, function(response) {
      	$scope.tableData = response;
        $scope.data = response.data;
        $scope.dataTotal = response.total;
      });
    };
    $scope.total = 100;
    $scope.load=function(){$scope.total=$scope.total+10;console.info($scope.total)};
    $scope.pageOptions = {
    		id:'model2',
    		data:'total',
    		loadFunction:function(){$scope.total=$scope.total+10;console.info($scope.total)},
    		queryParams:$scope.query
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
    					return '<div class="btn-role">'+
    					'<button class="btn btn-primary btn-xs" ng-click=openModal(row.id) type="button"><i class="fa fa-pencil"></i>&nbsp;修改</button>'+
    					'<button class="btn btn-danger btn-xs" ng-click=deleteRole(row.id) type="button"><i class="fa fa-trash-o"></i>&nbsp;删除</button>'+
    					'</div>';
    				}
    			}
    		],
    		loadFunction:$scope.queryRoles,
    		queryParams:$scope.query,
    		sortName:'id',
    		sortOrder:'asc'
    	};
    
    

    $scope.deleteRole = function(id){
  		$scope.confirmModal({
  			title:'确认删除角色',
  			confirm:function(isConfirm){
  				if(isConfirm){
  					$scope.roles.delete({
  	          urlPath : '/' + id
  	        }, function(data, status) {
  	          $scope.showSuccessMsg('删除角色成功');
  	          $scope.queryRoles();
  	        });
  				}
  			}
  		});
  	};
    
    $scope.openModal = function(id) {
      $scope.id = id;
      $uibModal.open({
        templateUrl : 'role-edit.html',
        controller : 'RoleModalCtrl',
        scope : $scope
      });
    };
    
    
    $scope.queryRoles();
    
    
  } ]);

  angular.module('adminApp').controller('RoleModalCtrl', [ '$scope', '$uibModalInstance', function($scope, $uibModalInstance) {
    $scope.formdata = {};

    if ($scope.id) {
      $scope.modalTitle = "修改角色";

      $scope.roles.get({
        urlPath : '/' + $scope.id
      }, function(data, status) {
        $scope.formdata = data;
      });

      $scope.ok = function() {
        $scope.roles.put({
          urlPath : '/' + $scope.id,
          data : $scope.formdata
        }, function(data, status) {
          $uibModalInstance.close();
          $scope.showSuccessMsg('修改角色成功');
          $scope.queryRoles();
        });
      };

    } else {
      $scope.modalTitle = "添加角色";
      $scope.ok = function() {
        $scope.roles.post({
          data : $scope.formdata
        }, function(data, status) {
          $uibModalInstance.close();
          $scope.showSuccessMsg('添加角色成功');
          $scope.queryRoles();
        });
      };
    }

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  } ]);

})();
