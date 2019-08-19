/**
 * 流程任务控制器
 *
 * @author wengwenhui
 * @date 2018年4月19日
 */
(function () {
  'use strict';

  angular.module('adminApp').controller('OwnerQueryTaskController', function ($scope, $stateParams, $q, $sce, $window) {
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
        urlPath: '/todo',
        params: $scope.queryParams
      }, function (response) {
        $scope.queryResult = response;
      });
    };

    $scope.claimTask = function (item) {
      $scope.confirmModal({
        title: '确认认领任务',
        confirm: function () {
          $scope.taskService.put({
            urlPath: '/' + item.id + '/claim'
          }, function () {
            $scope.showSuccessMsg('认领任务成功');
            $scope.queryDetail(item.id);
          });
        }
      });
    };

    $scope.unclaimTask = function (item) {
      $scope.confirmModal({
        title: '确认取消认领任务',
        confirm: function () {
          $scope.taskService.put({
            urlPath: '/' + item.id + '/unclaim'
          }, function () {
            $scope.showSuccessMsg('取消认领任务成功');
            $scope.queryDetail(item.id);
          });
        }
      });
    };

    $scope.assignTask = function (item) {
      $scope.editTaskUser(item, '转派任务', 'assign');
    };

    $scope.delegateTask = function (item) {
      $scope.editTaskUser(item, '委托任务', 'delegate');
    };

    $scope.editTaskUser = function (item, title, action) {
      var users = [];
      var usersPromise = $scope.userService.get({
        params: {status: 0}
      }, function (response) {
        users = response.data;
      });

      $q.all([usersPromise]).then(function () {
        console.info(users)
        $scope.editConfirmModal({
          formUrl: 'task-user-edit.html',
          title: title,
          formData: {name: item.name, users: users},
          confirm: function (formData, modalInstance) {
            $scope.taskService.put({
              urlPath: '/' + item.id + '/' + action + '/' + formData.userId,
            }, function () {
              $scope.showSuccessMsg(title + '成功');
              $scope.queryDetail(item.id);
              modalInstance.close();
            });
          }
        });
      });
    };

    $scope.completeTask = function (item) {
      $scope.taskService.put({
        urlPath: '/' + item.id + '/complete'
      }, function () {
        $scope.showSuccessMsg('完成任务成功');
        $scope.gotoList();
      });
    };

    $scope.saveFormData = function (item) {
      $scope.confirmModal({
        title: '确认提交任务',
        confirm: function () {
          $window.frames[0].frameElement.contentWindow.saveFormData(function () {
            $scope.completeTask(item)
          });
        }
      });
    }

    $scope.getFormUrl = function (definition, instance) {
      if (angular.isDefined(definition) && angular.isDefined(instance)) {
        return $sce.trustAsResourceUrl($scope.restUrl.formDefinitionWork(definition.category, $scope.selectedItem.formKey, instance.businessKey, $scope.loginUser.token))
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
        {
          name: '状态', index: 'suspended', width: '11%',
          formatter: function () {
            return '<span class="pull-left" ng-if="!row.suspended"><i class="fa fa-circle text-info"></i> 激活</span>' +
              '<span class="pull-left" ng-if="row.suspended"><i class="fa fa-circle text-danger"></i> 挂起</span>'
          }
        },
        {name: '开始时间', index: 'createTime', sortable: true, width: '10%'},
        {name: '到期时间', index: 'dueDate', sortable: true, width: '10%'},
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
      sortName: 'createTime',
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
