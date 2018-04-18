/**
 * 流程定义控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('FlowInstanceController',
    function($scope,$stateParams,$q) {
      $scope.instanceService = $scope.FlowService($scope.restUrl.flowInstances);
      $scope.detailId = $stateParams.id || '0';
      $scope.queryParams = $scope.detailId==='0' ? $scope.getCacheParams():{};
      $scope.queryResult = {};
      $scope.selectedItem = null;

      $scope.queryDetail = function(id){
        $scope.instanceService.get({
          urlPath : '/' + id
        }, function(response) {
          $scope.selectedItem = response;
        });
      };
      
      $scope.queryInstance = function() {
        console.info($scope.getCacheParams())
        $scope.instanceService.get({
          params : $scope.queryParams
        }, function(response) {
          $scope.queryResult = response;
        });
      };

      $scope.deleteInstance = function(id) {
        $scope.confirmModal({
          title : '确认删除流程定义',
          confirm : function() {
            $scope.instanceService.delete({
              urlPath : '/' + id
            }, function() {
              $scope.showSuccessMsg('删除流程定义成功');
              $scope.gotoList();
            });
          }
        });
      };

      $scope.switchStaus = function(item,suspended){
        var title = suspended?'激活流程':'挂起流程';
        var action = suspended?'activate':'suspend';
        $scope.editConfirmModal({
          formUrl: 'instance-status-edit.html',
          title: title,
          formData: item,
          confirm: function (formData,modalInstance) {
            $scope.instanceService.put({
              urlPath : '/' + item.id +'/'+action,
              data: formData
            }, function () {
              $scope.showSuccessMsg(title+'成功');
              if($scope.detailId !== '0'){
                $scope.queryDetail(item.id);
              }else{
                $scope.queryInstance();
              }
              modalInstance.close();
            });
          }
        });
      };

      $scope.tableOptions = {
        id : 'instance',
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
              return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
            }
          }
        ],
        loadFunction : $scope.queryInstance,
        queryParams : $scope.queryParams,
        sortName : 'startTime',
        sortOrder : 'asc'
      };

      $scope.authTableOptions = {
        id : 'instanceAuth',
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
        id : 'instanceJob',
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
        $scope.instanceService.get({
          urlPath : '/' + id+ '/authorize'
        }, function(response) {
          $scope.queryAuthResult = response;
        });
      };
      
      $scope.queryJob = function(id) {
        $scope.instanceService.get({
          urlPath : '/' + id+ '/jobs'
        }, function(response) {
          $scope.queryJobResult = response;
        });
      };
      
      $scope.deleteJob = function(id,jobId) {
        $scope.instanceService.delete({
          urlPath : '/' + id + '/jobs/'+jobId
        }, function() {
          $scope.queryJob(id);
        });
      };
      
      $scope.importInstance = function() {
        $scope.editConfirmModal({
          formUrl: 'instance-import.html',
          title: '导入流程',
          hideFooter: true,
          property:{
            fileOptions:{
              fileuploaded : function(){$scope.queryInstance();},
              uploadUrl: $scope.instanceService.url+'/import?token='+$scope.loginUser.token,
              allowedFileExtensions:['bpmn','bpmn20.xml','bar','zip']
            }
          }
        });
      };
      
      $scope.exportInstance = function(item){
        $scope.instanceService.get({
          urlPath : '/' + item.id +'/xml'
        }, function(response) {
          $scope.windowExportFile(response,item.name+'-v'+item.version+'.bpmn20.xml');
        });
      };
      
      $scope.getImageUrl = function(id){
        return $scope.instanceService.url +'/'+id+'/image.png?token='+$scope.loginUser.token;
      };
      
      if($scope.detailId !== '0'){
        $scope.queryDetail($scope.detailId);
      }else{
        $scope.queryInstance();
      }
      
    });
  
})();
