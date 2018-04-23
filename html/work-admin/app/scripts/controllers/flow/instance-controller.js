/**
 * 流程实例控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('FlowInstanceController', function($scope,$stateParams,$q) {
    $scope.instanceService = $scope.FlowService($scope.restUrl.flowInstances);
    $scope.definitionService = $scope.FlowService($scope.restUrl.flowDefinitions);
    $scope.taskService = $scope.FlowService($scope.restUrl.flowTasks);
    $scope.detailId = $stateParams.id || '0';
    $scope.queryParams = $scope.detailId==='0' ? $scope.getCacheParams():{};
    if(angular.isUndefined($scope.queryParams.processDefinitionId)){
      $scope.queryParams.processDefinitionId = $stateParams.processDefinitionId;
    }
    $scope.queryResult = {};
    $scope.selectedItem = null;

    $scope.queryDefinition = function(){
      $scope.definitionService.get({
      }, function(response) {
        $scope.definitions = response.data;
      });
    };
    
    $scope.queryDetail = function(id){
      $scope.instanceService.get({
        urlPath : '/' + id
      }, function(response) {
        $scope.selectedItem = response;
      });
    };
    
    $scope.queryInstance = function() {
      $scope.instanceService.get({
        params : $scope.queryParams
      }, function(response) {
        $scope.queryResult = response;
      });
    };

    $scope.deleteInstance = function(item) {
      if(item.endTime !== null){
        $scope.confirmModal({
          title : '确认删除已结束流程实例',
          confirm : function() {
            $scope.deleteInstanceApi(item.id);
          }
        });
      }else{
        $scope.editConfirmModal({
          formUrl: 'instance-delete.html',
          title: '删除正在运行的流程实例',
          formData: {name:item.processDefinitionName},
          confirm: function (formData,modalInstance) {
            $scope.deleteInstanceApi(item.id,formData.deleteReason,formData.cascade);
            modalInstance.close();
          }
        });
      }
    };
    
    $scope.deleteInstanceApi = function(id,deleteReason,cascade) {
      $scope.instanceService.delete({
        urlPath : '/' + id,
        params: {deleteReason:deleteReason,cascade:cascade}
      }, function () {
        $scope.showSuccessMsg('删除流程实例成功');
        $scope.gotoList();
      });
    };

    $scope.switchStaus = function(id,suspended){
      var title = suspended?'激活流程实例':'挂起流程实例';
      var action = suspended?'activate':'suspend';
      $scope.confirmModal({
        title: title,
        confirm: function () {
          $scope.instanceService.put({
            urlPath : '/' + id +'/'+action
          }, function () {
            $scope.showSuccessMsg(title+'成功');
            $scope.queryDetail(id);
          });
        }
      });
    };

    $scope.getImageUrl = function(id){
      if(angular.isDefined(id)){
        return $scope.instanceService.url +'/'+id+'/image.png?token='+$scope.loginUser.token;
      }
      return null;
    };
    
    $scope.tableOptions = {
      id : 'instance',
      data : 'queryResult',
      colModels : [
        {name:'实例ID',index:'id',width:'10%'},
        {name:'流程名称',index:'processDefinitionName',width:'10%'},
        {name:'流程标识',index:'processDefinitionId',sortable:true,width:'10%'},
        {name:'开始时间',index:'startTime',sortable:true,width:'10%'},
        {name:'结束时间',index:'endTime',sortable:true,width:'10%'},
        {name:'业务标识',index:'businessKey',sortable:true,width:'10%'},
        {name:'操作',index:'',width:'10%',
          formatter:function(){
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ],
      loadFunction : $scope.queryInstance,
      queryParams : $scope.queryParams,
      sortName : 'startTime',
      sortOrder : 'desc'
    };
    

    $scope.queryTask = function(id) {
      $scope.taskService.get({
        params : {processInstanceId:id}
      }, function(response) {
        $scope.queryTaskResult = response.data;
      });
    };
    
    $scope.gotoTaskDetail = function(id) {
      $scope.$state.go('main.flow.task',{id:id});
    };
    
    $scope.taskTableOptions = {
      id : 'instanceTask',
      data : 'queryTaskResult',
      isPage : false,
      colModels : [
        {name:'任务ID',index:'id'},
        {name:'任务名称',index:'name'},
        {name:'负责人',index:'assignee'},
        {name:'所有人',index:'owner'},
        {name:'开始时间',index:'startTime'},
        {name:'结束时间',index:'endTime'},
        {name:'操作',index:'',
          formatter:function(){
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoTaskDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ]
    };
      
    $scope.queryVariable = function(id) {
      $scope.instanceService.get({
        urlPath : '/' + id+ '/variables'
      }, function(response) {
        $scope.queryVariableResult = response;
      });
    };

    $scope.createVariable = function(id) {
      $scope.editConfirmModal({
        formUrl: 'variable-create.html',
        title: '添加流程变量',
        confirm: function (formData,modalInstance) {
          $scope.instanceService.post({
            urlPath : '/' + id + '/variables',
            data : formData
          }, function() {
            $scope.showSuccessMsg('添加流程变量成功');
            $scope.queryVariable(id);
            modalInstance.close();
          });
        }
      });
    };
    
    $scope.deleteVariable = function(id,name) {
      $scope.instanceService.delete({
        urlPath : '/' + id + '/variables/'+name
      }, function() {
        $scope.showSuccessMsg('删除流程变量成功');
        $scope.queryVariable(id);
      });
    };

    $scope.variableTableOptions = {
      id : 'instanceVariable',
      data : 'queryVariableResult',
      isPage : false,
      colModels : [
        {name:'变量名称',index:'name'},
        {name:'类型',index:'type'},
        {name:'变量值',index:'value'},
        {name : '操作', index : '',
          formatter : function() {
            return '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteVariable(selectedItem.id,row.name) ng-disabled="selectedItem.endTime != null"><i class="fa fa-trash-o"></i>&nbsp;删除</button>';
          }
        }
      ]
    };

    $scope.queryChildInstance = function(id) {
      $scope.instanceService.get({
        params : {superProcessInstanceId:id}
      }, function(response) {
        $scope.queryChildrenResult = response.data;
      });
    };
      
    $scope.childrenTableOptions = {
      id : 'instanceChildren',
      data : 'queryChildrenResult',
      isPage : false,
      colModels : [
        {name:'实例ID',index:'id'},
        {name:'流程名称',index:'processDefinitionName'},
        {name:'流程标识',index:'processDefinitionId'},
        {name:'开始时间',index:'startTime'},
        {name:'结束时间',index:'endTime'},
        {name:'业务标识',index:'businessKey'},
        {name:'操作',index:'',
          formatter:function(){
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ]
    };
    
    if($scope.detailId !== '0'){
      $scope.queryDetail($scope.detailId);
    }else{
      $scope.queryDefinition();
      $scope.queryInstance();
    }
    
  });

})();
