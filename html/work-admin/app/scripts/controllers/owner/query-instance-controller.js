/**
 * 流程实例控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function () {
  'use strict';

  angular.module('adminApp').controller('OwnerQueryInstanceController', function ($scope, $stateParams, $sce) {
    $scope.instanceService = $scope.FlowService($scope.restUrl.flowInstances);
    $scope.definitionService = $scope.FlowService($scope.restUrl.flowDefinitions);
    $scope.taskService = $scope.FlowService($scope.restUrl.flowTasks);
    $scope.detailId = $stateParams.id || '0';
    $scope.queryParams = $scope.detailId === '0' ? $scope.getCacheParams() : {};
    if (angular.isUndefined($scope.queryParams.processDefinitionId)) {
      $scope.queryParams.processDefinitionId = $stateParams.processDefinitionId;
    }

    $scope.queryParams.startedBy = $scope.loginUser.userId;

    $scope.queryResult = {};
    $scope.selectedItem = null;

    $scope.queryDefinition = function () {
      $scope.definitionService.get({}, function (response) {
        $scope.definitions = response.data;
      });
    };

    $scope.queryInstance = function () {
      $scope.instanceService.get({
        params: $scope.queryParams
      }, function (response) {
        $scope.queryResult = response;
      });
    };

    $scope.queryDetail = function (id) {
      $scope.instanceService.get({
        urlPath: '/' + id
      }, function (response) {
        $scope.selectedItem = response;
        $scope.getFormKey(response.processDefinitionId);
      });
    };

    $scope.getFormKey = function (id) {
      $scope.definitionService.get({
        urlPath: '/' + id
      }, function (response) {
        $scope.selectedItem.definition = response;
      });
    };

    $scope.getImageUrl = function (id) {
      if (angular.isDefined(id)) {
        return $scope.instanceService.url + '/' + id + '/image.png?token=' + $scope.loginUser.token;
      }
      return null;
    };

    $scope.getFormUrl = function (id) {
      if (angular.isDefined(id)) {
        return $sce.trustAsResourceUrl($scope.restUrl.formInstancePreview($scope.selectedItem.definition.category, $scope.selectedItem.definition.formKey, $scope.selectedItem.businessKey, $scope.loginUser.token));
      }
      return null;
    };

    $scope.deleteInstance = function (item) {
      $scope.editConfirmModal({
        formUrl: 'instance-delete.html',
        title: '删除流程申请单',
        formData: {name: item.processDefinitionName},
        confirm: function (formData, modalInstance) {
          $scope.instanceService.delete({
            urlPath: '/' + item.id,
            params: {deleteReason: formData.deleteReason}
          }, function () {
            modalInstance.close();
            $scope.showSuccessMsg('删除流程申请单成功');
            $scope.gotoList();
          });

        }
      });
    };


    $scope.tableOptions = {
      id: 'instance',
      data: 'queryResult',
      colModels: [
        {name: '实例ID', index: 'id', width: '10%'},
        {name: '流程名称', index: 'processDefinitionName', width: '10%'},
        {name: '流程标识', index: 'processDefinitionId', sortable: true, width: '10%'},
        {name: '开始时间', index: 'startTime', sortable: true, width: '10%'},
        {name: '结束时间', index: 'endTime', sortable: true, width: '10%'},
        {
          name: '操作', index: '', width: '10%',
          formatter: function () {
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ],
      loadFunction: $scope.queryInstance,
      queryParams: $scope.queryParams,
      sortName: 'startTime',
      sortOrder: 'desc'
    };

    $scope.queryTask = function (id) {
      $scope.taskService.get({
        params: {processInstanceId: id, sortName: 'startTime', pageNum: 1}
      }, function (response) {
        $scope.queryTaskResult = response.data;
      });
    };

    $scope.taskTableOptions = {
      id: 'instanceTask',
      data: 'queryTaskResult',
      isPage: false,
      colModels: [
        {name: '任务ID', index: 'id'},
        {name: '任务名称', index: 'name'},
        {name: '任务标识', index: 'taskDefinitionKey'},
        {name: '负责人', index: 'assignee'},
        {name: '开始时间', index: 'startTime'},
        {name: '结束时间', index: 'endTime'}
      ]
    };

    $scope.queryChildInstance = function (id) {
      $scope.instanceService.get({
        params: {superProcessInstanceId: id}
      }, function (response) {
        $scope.queryChildrenResult = response.data;
      });
    };

    $scope.childrenTableOptions = {
      id: 'instanceChildren',
      data: 'queryChildrenResult',
      isPage: false,
      colModels: [
        {name: '实例ID', index: 'id'},
        {name: '流程名称', index: 'processDefinitionName'},
        {name: '流程标识', index: 'processDefinitionId'},
        {name: '开始时间', index: 'startTime'},
        {name: '结束时间', index: 'endTime'},
        {name: '业务标识', index: 'businessKey'}
      ]
    };

    if ($scope.detailId !== '0') {
      $scope.queryDetail($scope.detailId);
    } else {
      $scope.queryDefinition();
      $scope.queryInstance();
    }

  });

})();
