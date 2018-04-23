/**
 * 流程任务控制器
 *
 * @author wengwenhui
 * @date 2018年4月19日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('FlowTaskController', function($scope,$stateParams,$q) {
    $scope.taskService = $scope.FlowService($scope.restUrl.flowTasks);
    $scope.instanceService = $scope.FlowService($scope.restUrl.flowInstances);
    $scope.definitionService = $scope.FlowService($scope.restUrl.flowDefinitions);
    $scope.userService = $scope.IdmService($scope.restUrl.idmUsers);
    $scope.detailId = $stateParams.id || '0';
    $scope.queryParams = $scope.detailId==='0' ? $scope.getCacheParams():{};
    $scope.queryResult = {};
    $scope.selectedItem = null;

    $scope.queryDefinition = function(){
      $scope.definitionService.get({
      }, function(response) {
        $scope.definitions = response.data;
      });
    };
    
    $scope.queryDetail = function(id){
      $scope.taskService.get({
        urlPath : '/' + id
      }, function(response) {
        $scope.selectedItem = response;
      });
    };
    
    $scope.queryTask = function() {
      $scope.taskService.get({
        params : $scope.queryParams
      }, function(response) {
        $scope.queryResult = response;
      });
    };

    $scope.editTask = function(item) {
      $scope.editConfirmModal({
        formUrl: 'task-edit.html',
        title: '编辑任务',
        formData: item,
        confirm: function (formData,modalInstance) {
          $scope.taskService.put({
            urlPath : '/' + item.id,
            data: formData
          }, function () {
            $scope.showSuccessMsg('编辑任务成功');
            $scope.queryDetail(item.id);
            modalInstance.close();
          });
        }
      });
    };
    
    $scope.assignTask = function(item) {
      $scope.editTaskUser(item,'转派任务','assign');
    };
    
    $scope.delegateTask = function(item) {
      $scope.editTaskUser(item,'委托任务','delegate');
    };
    
    $scope.editTaskUser = function(item,title,action){
      var users = [];
      var usersPromise = $scope.userService.get({
        params : {status: 0}
      }, function (response) {
        users = response.data;
      });
      
      $q.all([usersPromise]).then(function() {  
        $scope.editConfirmModal({
          formUrl: 'task-user-edit.html',
          title: title,
          formData: {name:item.name,users:users},
          confirm: function (formData,modalInstance) {
            $scope.taskService.put({
              urlPath : '/' + item.id+'/'+action+'/'+formData.userId,
            }, function () {
              $scope.showSuccessMsg(title+'成功');
              $scope.queryDetail(item.id);
              modalInstance.close();
            });
          }
        });
      });
    };

    $scope.completeTask = function(item) {
      $scope.confirmModal({
        title : '确认完成任务',
        confirm : function() {
          $scope.taskService.put({
            urlPath : '/' + item.id + '/complete'
          }, function () {
            $scope.showSuccessMsg('完成任务成功');
            $scope.queryDetail(item.id);
          });
        }
      });
    };
    
    $scope.deleteTask = function(item) {
      $scope.confirmModal({
        title : '确认删除任务',
        confirm : function() {
          $scope.taskService.delete({
            urlPath : '/' + item.id
          }, function () {
            $scope.showSuccessMsg('删除任务成功');
            $scope.gotoList();
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
      id : 'task',
      data : 'queryResult',
      colModels : [
        {name:'任务ID',index:'id',width:'7%'},
        {name:'任务名称',index:'name',width:'8%'},
        {name:'流程标识',index:'processDefinitionId',sortable:true,width:'8%'},
        {name:'负责人',index:'assignee',sortable:true,width:'7%'},
        {name:'所有人',index:'owner',sortable:true,width:'7%'},
        {name:'开始时间',index:'startTime',sortable:true,width:'10%'},
        {name:'结束时间',index:'endTime',sortable:true,width:'10%'},
        {name:'操作',index:'',width:'7%',
          formatter:function(){
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ],
      loadFunction : $scope.queryTask,
      queryParams : $scope.queryParams,
      sortName : 'startTime',
      sortOrder : 'desc'
    };
    
    $scope.queryVariable = function(id) {
      $scope.taskService.get({
        urlPath : '/' + id+ '/variables'
      }, function(response) {
        $scope.queryVariableResult = response;
      });
    };

    $scope.createVariable = function(id) {
      $scope.editConfirmModal({
        formUrl: 'variable-create.html',
        title: '添加任务变量',
        confirm: function (formData,modalInstance) {
          $scope.taskService.post({
            urlPath : '/' + id + '/variables',
            data : formData
          }, function() {
            $scope.showSuccessMsg('添加任务变量成功');
            $scope.queryVariable(id);
            modalInstance.close();
          });
        }
      });
    };
    
    $scope.deleteVariable = function(id,name) {
      $scope.taskService.delete({
        urlPath : '/' + id + '/variables/'+name
      }, function() {
        $scope.showSuccessMsg('删除任务变量成功');
        $scope.queryVariable(id);
      });
    };

    $scope.variableTableOptions = {
      id : 'taskVariable',
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
    $scope.queryIdentity = function(id) {
      $scope.taskService.get({
        urlPath : '/' + id+ '/identity-links'
      }, function(response) {
        $scope.queryIdentityResult = response;
      });
    };
    
    $scope.deleteIdentity = function(id,type,identityId) {
      $scope.taskService.delete({
        urlPath : '/' + id + '/identity-links/'+type+'/'+identityId
      }, function() {
        $scope.showSuccessMsg('删除候选信息成功');
        $scope.queryIdentity(id);
      });
    };
    
    $scope.createIdentity = function(id) {
      var item = {};
      var usersPromise = $scope.userService.get({
        params : {status: 0}
      }, function (response) {
        item.users = response.data;
      });
      
      var groupsPromise = $scope.userService.get({
        urlPath : '/groups'
      }, function(response) {
        item.groups = response;
      });
      
      
      $q.all([usersPromise,groupsPromise]).then(function() {  
        $scope.editConfirmModal({
          formUrl: 'task-identity-create.html',
          title: '添加候选信息',
          formData: item,
          confirm: function (formData,modalInstance) {
            var requestBody={type:formData.type};
            if(formData.type=='user'){
              requestBody.identityId=formData.user[0].id;
            }else{
              requestBody.identityId=formData.group[0].id;
            }
            $scope.taskService.post({
              urlPath : '/' + id + '/identity-links',
              data: requestBody
            }, function () {
              $scope.showSuccessMsg('添加候选信息成功');
              $scope.queryIdentity(id);
              modalInstance.close();
            });
          }
        });
      });
    };

    $scope.identityTableOptions = {
      id : 'taskidentity',
      data : 'queryIdentityResult',
      isPage : false,
      colModels : [
        {name:'ID',index:'identityId'},
        {name:'名称',index:'identityName'},
        {name:'类型',index:'type',
          formatter : function() {
            return '<span>{{row.type=="user"?"用户":"群组"}}</span>';
          }
        },
        {name : '操作', index : '',
          formatter : function() {
            return '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteIdentity(selectedItem.id,row.type,row.identityId) ng-disabled="selectedItem.endTime != null"><i class="fa fa-trash-o"></i>&nbsp;删除</button>';
          }
        }
      ]
    };
    
    if($scope.detailId !== '0'){
      $scope.queryDetail($scope.detailId);
    }else{
      $scope.queryDefinition();
      $scope.queryTask();
    }
    
  });
  
})();
