/**
 * 流程任务控制器
 *
 * @author wengwenhui
 * @date 2018年4月19日
 */
(function () {
  'use strict';

  angular.module('adminApp').controller('OwnerDoneTaskController', function ($scope, $stateParams, $q, $sce) {
    $scope.taskService = $scope.FlowService($scope.restUrl.flowTasks);
    $scope.instanceService = $scope.FlowService($scope.restUrl.flowInstances);
    $scope.definitionService = $scope.FlowService($scope.restUrl.flowDefinitions);
    $scope.userService = $scope.IdmService($scope.restUrl.idmUsers);
    $scope.detailId = $stateParams.id || '0';
    $scope.queryParams = $scope.detailId === '0' ? $scope.getCacheParams() : {};
    $scope.queryResult = {};
    $scope.selectedItem = null;

    $scope.queryDefinition = function () {
      $scope.definitionService.get({}, function (response) {
        $scope.definitions = response.data;
      });
    };

    $scope.queryDetail = function (id) {
      $scope.taskService.get({
        urlPath: '/' + id
      }, function (response) {
        $scope.selectedItem = response;
        $scope.getFormKey(response);
      });
    };

    $scope.getFormKey = function (response) {
      $scope.definitionService.get({
        urlPath: '/' + response.processDefinitionId
      }, function (response) {
        $scope.selectedItem.definition = response;
      });

      $scope.instanceService.get({
        urlPath: '/' + response.processInstanceId
      }, function (response) {
        $scope.selectedItem.instance = response;
      });
    };

    $scope.queryTask = function () {
      $scope.taskService.get({
        urlPath: '/done',
        params: $scope.queryParams
      }, function (response) {
        $scope.queryResult = response;
      });
    };

    $scope.getFormUrl = function (definition, instance) {
      if (angular.isDefined(definition) && angular.isDefined(instance)) {
        return $sce.trustAsResourceUrl($scope.restUrl.formInstancePreview(definition.category, $scope.selectedItem.formKey, instance.businessKey, $scope.loginUser.token));
      }
      return null;
    };

    $scope.getImageUrl = function (id) {
      if (angular.isDefined(id)) {
        return $scope.instanceService.url + '/' + id + '/image.png?token=' + $scope.loginUser.token;
      }
      return null;
    };

    $scope.tableOptions = {
      id: 'task',
      data: 'queryResult',
      colModels: [
        {name: '任务ID', index: 'id', width: '7%'},
        {name: '任务名称', index: 'name', width: '8%'},
        {name: '流程标识', index: 'processDefinitionId', sortable: true, width: '8%'},
        {name: '开始时间', index: 'startTime', sortable: true, width: '10%'},
        {name: '完成时间', index: 'endTime', sortable: true, width: '10%'},
        {name: '优先级', index: 'priority', width: '8%'},
        {
          name: '操作', index: '', width: '7%',
          formatter: function () {
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ],
      loadFunction: $scope.queryTask,
      queryParams: $scope.queryParams,
      sortName: 'endTime',
      sortOrder: 'desc'
    };


    if ($scope.detailId !== '0') {
      $scope.queryDetail($scope.detailId);
    } else {
      $scope.queryDefinition();
      $scope.queryTask();
    }

  });

})();
