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
    $scope.detailId = $stateParams.id || '0';
    $scope.queryParams = $scope.detailId==='0' ? $scope.getCacheParams():{};
    $scope.queryResult = {};
    $scope.selectedItem = null;

    $scope.definitionService.get({
      params : {pageSize:1000}
    }, function(response) {
      $scope.definitions = response.data;
    });
    
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
      $scope.editConfirmModal({
        formUrl: 'task-user-edit.html',
        title: title,
        formData: {name:item.name},
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
      sortOrder : 'asc'
    };

    $scope.authTableOptions = {
      id : 'taskAuth',
      data : 'queryAuthResult',
      isPage : false,
      colModels : [
        {name:'ID',index:'identityId'},
        {name:'类型',index:'type',
          formatter : function() {
            return '<span>{{row.type=="user"?"用户":"群组"}}</span>';
          }
        },
        {name : '操作', index : '',
          formatter : function() {
            return '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteRow(row.id)>'+
               '<i class="fa fa-trash-o"></i>&nbsp;删除</button>';
          }
        }
      ]
    };
    
    $scope.jobTableOptions = {
      id : 'taskJob',
      data : 'queryJobResult',
      isPage : false,
      colModels : [
        {name:'类型',index:'jobHandlerType'},
        {name:'执行时间',index:'duedate'},
        {name:'创建时间',index:'createTime'},
        {name : '操作', index : '',
          formatter : function() {
            return '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteJob(selectedItem.id,row.id)>'+
               '<i class="fa fa-trash-o"></i>&nbsp;删除</button>';
          }
        }
      ]
    };
    
    $scope.queryAuth = function(id) {
      $scope.taskService.get({
        urlPath : '/' + id+ '/authorize'
      }, function(response) {
        $scope.queryAuthResult = response;
      });
    };
    
    $scope.queryJob = function(id) {
      $scope.taskService.get({
        urlPath : '/' + id+ '/jobs'
      }, function(response) {
        $scope.queryJobResult = response;
      });
    };
    
    $scope.deleteJob = function(id,jobId) {
      $scope.taskService.delete({
        urlPath : '/' + id + '/jobs/'+jobId
      }, function() {
        $scope.queryJob(id);
      });
    };
    
    $scope.importTask = function() {
      $scope.editConfirmModal({
        formUrl: 'task-import.html',
        title: '导入流程',
        hideFooter: true,
        property:{
          fileOptions:{
            fileuploaded : function(){$scope.queryTask();},
            uploadUrl: $scope.taskService.url+'/import?token='+$scope.loginUser.token,
            allowedFileExtensions:['bpmn','bpmn20.xml','bar','zip']
          }
        }
      });
    };
    
    $scope.exportTask = function(item){
      $scope.taskService.get({
        urlPath : '/' + item.id +'/xml'
      }, function(response) {
        $scope.windowExportFile(response,item.name+'-v'+item.version+'.bpmn20.xml');
      });
    };
    
    $scope.getImageUrl = function(id){
      if(angular.isDefined(id)){
        return $scope.instanceService.url +'/'+id+'/image.png?token='+$scope.loginUser.token;
      }
      return null;
    };
    
    if($scope.detailId !== '0'){
      $scope.queryDetail($scope.detailId);
    }else{
      $scope.queryTask();
    }
    
  });
  
})();
