/**
 * 角色控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('FlowDefinitionController',
    function($scope,$q) {
      $scope.definitionService = $scope.FlowService($scope.restUrl.flowDefinitions);
      $scope.queryResult = {};
      $scope.queryParams = {};

      $scope.queryDefinition = function() {
        $scope.definitionService.get({
          params : $scope.queryParams
        }, function(response) {
          $scope.queryResult = response;
        });
      };

      $scope.deleteDefinition = function(id) {
        $scope.confirmModal({
          title : '确认删除角色',
          confirm : function() {
            $scope.definitionService.delete({
              urlPath : '/' + id
            }, function() {
              $scope.showSuccessMsg('删除角色成功');
              $scope.queryDefinition();
            });
          }
        });
      };

      $scope.editDefinition = function(id) {
        var formData = {};
        var menusPromise = $scope.definitionService.get({
          urlPath : '/menus',
          params : {id:id}
        }, function(response) {
          formData.menus = response;
        });
        
        $q.all([menusPromise]).then(function() {  
          $scope.editModal({
            id : id,
            formUrl : 'definition-edit.html',
            title : '角色',
            formData : formData,
            service : $scope.definitionService,
            complete : $scope.queryDefinition
          });
        });
      };
      
      $scope.switchStaus = function(item,suspended){
        var title = suspended?'激活流程':'挂起流程';
        var action = suspended?'activate':'suspend';
        $scope.editConfirmModal({
          formUrl: 'definition-status-edit.html',
          title: title,
          formData: item,
          confirm: function (formData,modalInstance) {
            $scope.definitionService.put({
              urlPath : '/' + item.id +'/'+action,
              data: formData
            }, function () {
              $scope.showSuccessMsg(title+'成功');
              $scope.queryDefinition();
              modalInstance.close();
            });
          }
        });
      };

      $scope.queryDefinitionUser = function(id) {
        $scope.tableModal({
          service : $scope.IdmService($scope.restUrl.idmDefinitions+'/'+id+'/users'),
          colModels : [
            {name:'名称',index:'name'},
            {name:'电话',index:'phone'},
            {name:'状态',index:'status',
              formatter:function(){
                return '<span class="label label-success" ng-if="row.status==0">启用</span>'+
                  '<span class="label label-danger" ng-if="row.status==1">停用</span>';
              }
            }
          ]
        });
      };
      
      $scope.tableOptions = {
        id : 'definition',
        data : 'queryResult',
        colModels : [
          {name:'名称',index:'name',sortable:true,width:'10%'},
          {name:'标识',index:'key',sortable:true,width:'10%'},
          {name:'版本号',index:'version',sortable:true,width:'10%'},
          {name:'状态',index:'suspended',width:'11%',
            formatter:function(){
              return '<toggle-switch ng-init="switch=(!row.suspended)" ng-model="switch" class="switch-small switch-info" '+
                'on-label="激活" off-label="挂起" on-change="switchStaus(row,switch)"></toggle-switch>';
            }
          },
          {name:'备注',index:'description',width:'12%'},
          {name:'操作',index:'',width:'10%',
            formatter:function(){
              return '<button type="button" class="btn btn-info btn-xs" ng-click=editDefinition(row.id)><i class="fa fa-pencil"></i>&nbsp;明细</button>';
            }
          }
        ],
        loadFunction : $scope.queryDefinition,
        queryParams : $scope.queryParams,
        sortName : 'name',
        sortOrder : 'asc'
      };
      
      $scope.queryDefinition();
    });
  
})();
