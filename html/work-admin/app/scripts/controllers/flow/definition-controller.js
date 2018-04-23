/**
 * 流程定义控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('FlowDefinitionController', function($scope,$stateParams,$q) {
    $scope.definitionService = $scope.FlowService($scope.restUrl.flowDefinitions);
    $scope.instanceService = $scope.FlowService($scope.restUrl.flowInstances);
    $scope.userService = $scope.IdmService($scope.restUrl.idmUsers);
    $scope.groupService = $scope.IdmService($scope.restUrl.idmGroups);
    $scope.detailId = $stateParams.id || '0';
    $scope.queryParams = $scope.detailId==='0' ? $scope.getCacheParams():{};
    $scope.queryResult = {};
    $scope.selectedItem = null;
    
    $scope.queryDetail = function(id){
      $scope.definitionService.get({
        urlPath : '/' + id
      }, function(response) {
        $scope.selectedItem = response;
      });
    };
    
    $scope.queryDefinition = function() {
      $scope.definitionService.get({
        params : $scope.queryParams
      }, function(response) {
        $scope.queryResult = response;
      });
    };

    $scope.deleteDefinition = function(item) {
      $scope.editConfirmModal({
        formUrl: 'definition-delete.html',
        title: '删除流程定义',
        formData: {name:item.name},
        confirm: function (formData,modalInstance) {
          $scope.definitionService.delete({
            urlPath : '/' + item.id,
            params: {cascade:formData.cascade}
          }, function() {
            $scope.showSuccessMsg('删除流程定义成功');
            modalInstance.close();
            $scope.gotoList();
          });
        }
      });
    };
    

    $scope.createInstance = function(item) {
      $scope.editConfirmModal({
        formUrl: 'definition-start.html',
        title: '启动流程实例',
        formData: {name:item.name,processDefinitionId:item.id},
        confirm: function (formData,modalInstance) {
          $scope.instanceService.post({
            data: formData
          }, function () {
            $scope.showSuccessMsg('启动流程实例成功');
            modalInstance.close();
          });
        }
      });
    };

    $scope.importDefinition = function() {
      $scope.editConfirmModal({
        formUrl: 'definition-import.html',
        title: '导入流程',
        hideFooter: true,
        property:{
          fileOptions:{
            fileuploaded : function(){$scope.queryDefinition();},
            uploadUrl: $scope.definitionService.url+'/import?token='+$scope.loginUser.token,
            allowedFileExtensions:['bpmn','bpmn20.xml','bar','zip']
          }
        }
      });
    };
    
    $scope.exportDefinition = function(item){
      $scope.definitionService.get({
        urlPath : '/' + item.id +'/xml'
      }, function(response) {
        $scope.windowExportFile(response,item.name+'-v'+item.version+'.bpmn20.xml');
      });
    };
    
    $scope.getImageUrl = function(id){
      if(angular.isDefined(id)){
        return $scope.definitionService.url +'/'+id+'/image.png?token='+$scope.loginUser.token;
      }
      return null;
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
            if($scope.detailId !== '0'){
              $scope.queryDetail(item.id);
            }else{
              $scope.queryDefinition();
            }
            modalInstance.close();
          });
        }
      });
    };

    $scope.tableOptions = {
      id : 'definition',
      data : 'queryResult',
      colModels : [
        {name:'流程ID',index:'id',sortable:true,width:'10%'},
        {name:'流程名称',index:'name',sortable:true,width:'10%'},
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
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ],
      loadFunction : $scope.queryDefinition,
      queryParams : $scope.queryParams,
      sortName : 'name',
      sortOrder : 'asc'
    };
    
    $scope.queryIdentity = function(id) {
      $scope.definitionService.get({
        urlPath : '/' + id+ '/identity-links'
      }, function(response) {
        $scope.queryIdentityResult = response;
      });
    };
    
    $scope.deleteIdentity = function(id,type,identityId) {
      $scope.definitionService.delete({
        urlPath : '/' + id + '/identity-links/'+type+'/'+identityId
      }, function() {
        $scope.showSuccessMsg('删除授权成功');
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
          formUrl: 'definition-identity-create.html',
          title: '添加授权',
          formData: item,
          confirm: function (formData,modalInstance) {
            var requestBody = {type:formData.type};
            if(formData.type === 'user'){
              requestBody.identityId = formData.user[0].id;
            }else{
              requestBody.identityId = formData.group[0].id;
            }
            $scope.definitionService.post({
              urlPath : '/' + id + '/identity-links',
              data: requestBody
            }, function () {
              $scope.showSuccessMsg('添加授权成功');
              $scope.queryIdentity(id);
              modalInstance.close();
            });
          }
        });
      });
    };

    $scope.identityTableOptions = {
      id : 'definitionidentity',
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
            return '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteIdentity(selectedItem.id,row.type,row.identityId)><i class="fa fa-trash-o"></i>&nbsp;删除</button>';
          }
        }
      ]
    };
    
    $scope.queryJob = function(id) {
      $scope.definitionService.get({
        urlPath : '/' + id+ '/jobs'
      }, function(response) {
        $scope.queryJobResult = response;
      });
    };
    
    $scope.deleteJob = function(id,jobId) {
      $scope.definitionService.delete({
        urlPath : '/' + id + '/jobs/'+jobId
      }, function() {
        $scope.showSuccessMsg('删除定时任务成功');
        $scope.queryJob(id);
      });
    };

    $scope.jobTableOptions = {
      id : 'definitionJob',
      data : 'queryJobResult',
      isPage : false,
      colModels : [
        {name:'类型',index:'jobHandlerType'},
        {name:'执行时间',index:'duedate'},
        {name:'创建时间',index:'createTime'},
        {name : '操作', index : '',
          formatter : function() {
            return '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteJob(selectedItem.id,row.id)><i class="fa fa-trash-o"></i>&nbsp;删除</button>';
          }
        }
      ]
    };
    
    $scope.queryProcess = function(id) {
      $scope.instanceService.get({
        params : {processDefinitionId:id,finished:false,pageNum:1}
      }, function(response) {
        $scope.queryProcessResult = response.data;
      });
    };
    

    $scope.gotoProcessDetail = function(id) {
      $scope.$state.go('main.flow.instance',{id:id});
    };
    
    $scope.gotoProcessList = function(id) {
      $scope.$state.go('main.flow.instance',{processDefinitionId:id});
    };
    
    $scope.processTableOptions = {
      id : 'definitionProcess',
      data : 'queryProcessResult',
      isPage : false,
      colModels : [
        {name:'实例ID',index:'id'},
        {name:'流程标识',index:'processDefinitionId'},
        {name:'开始时间',index:'startTime'},
        {name:'业务标识',index:'businessKey'},
        {name:'操作',index:'',
          formatter:function(){
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoProcessDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ]
    };
   
    if($scope.detailId !== '0'){
      $scope.queryDetail($scope.detailId);
    }else{
      $scope.queryDefinition();
    }
    
  });
  
})();
