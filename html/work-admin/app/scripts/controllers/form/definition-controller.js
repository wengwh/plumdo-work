/**
 * 流程定义控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function () {
  'use strict';

  angular.module('adminApp').controller('FormDefinitionController', function ($scope, $stateParams, $sce) {
    $scope.definitionService = $scope.FormService($scope.restUrl.formDefinitions);
    $scope.instanceService = $scope.FlowService($scope.restUrl.flowInstances);
    $scope.userService = $scope.IdmService($scope.restUrl.idmUsers);
    $scope.groupService = $scope.IdmService($scope.restUrl.idmGroups);
    $scope.detailId = $stateParams.id || '0';
    $scope.queryParams = $scope.detailId === '0' ? $scope.getCacheParams() : {};
    $scope.queryResult = {};
    $scope.selectedItem = null;

    $scope.queryDetail = function (id) {
      $scope.definitionService.get({
        urlPath: '/' + id
      }, function (response) {
        $scope.selectedItem = response;
      });

      $scope.definitionService.get({
        urlPath: '/' + id + '/metadata'
      }, function (response) {
        $scope.queryFieldResult = response.fields;
        $scope.queryLayoutResult = response.layouts;
      });

    };

    $scope.queryDefinition = function () {
      $scope.definitionService.get({
        params: $scope.queryParams
      }, function (response) {
        $scope.queryResult = response;
      });
    };

    $scope.createInstance = function (item) {
      $scope.editConfirmModal({
        formUrl: 'definition-start.html',
        title: '启动流程实例',
        formData: {name: item.name, processDefinitionId: item.id},
        confirm: function (formData, modalInstance) {
          $scope.instanceService.post({
            data: formData
          }, function () {
            $scope.showSuccessMsg('启动流程实例成功');
            modalInstance.close();
          });
        }
      });
    };

    $scope.watchFormLayout = function (id) {
      $scope.editConfirmModal({
        formUrl: 'form-layout-watch.html',
        title: '预览布局',
        hideFooter: true,
        property: {
          url: $sce.trustAsResourceUrl($scope.restUrl.formPreview(id, $scope.loginUser.token))
        }
      });
    };

    $scope.tableOptions = {
      id: 'definition',
      data: 'queryResult',
      colModels: [
        {name: '表单名称', index: 'name', sortable: true, width: '10%'},
        {name: '标识', index: 'key', sortable: true, width: '10%'},
        {name: '版本号', index: 'version', sortable: true, width: '10%'},
        {name: '关联表', index: 'relationTable', sortable: true, width: '10%'},
        {name: '分类', index: 'category', width: '11%'},
        {name: '创建时间', index: 'createTime', width: '12%'},
        {
          name: '操作', index: '', width: '10%',
          formatter: function () {
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ],
      loadFunction: $scope.queryDefinition,
      queryParams: $scope.queryParams,
      sortName: 'name',
      sortOrder: 'asc'
    };


    $scope.fieldTableOptions = {
      id: 'definitionField',
      data: 'queryFieldResult',
      isPage: false,
      colModels: [
        {name: '名称', index: 'name', width: '10%'},
        {name: '标识', index: 'key', width: '10%'},
        {name: '创建时间', index: 'createTime', width: '12%'},
        {name: '修改时间', index: 'lastUpdateTime', width: '12%'},
        {name: '备注', index: 'remark', width: '12%'}
      ]
    };


    $scope.layoutTableOptions = {
      id: 'definitionLayout',
      data: 'queryLayoutResult',
      isPage: false,
      colModels: [
        {name: '名称', index: 'name', width: '15%'},
        {name: '标识', index: 'key', width: '10%'},
        {name: '创建时间', index: 'createTime', width: '22%'},
        {name: '修改时间', index: 'lastUpdateTime', width: '22%'},
        {name: '备注', index: 'remark', width: '22%'},
        {
          name: '操作', index: '',
          formatter: function () {
            return '<button type="button" class="btn btn-success btn-xs" ng-click=watchFormLayout(row.id)><i class="fa fa-wpforms"></i>&nbsp;预览</button>';
          }
        }
      ]
    };

    $scope.gotoProcessDetail = function (id) {
      $scope.$state.go('main.flow.instance', {id: id});
    };

    $scope.gotoProcessList = function (id) {
      $scope.$state.go('main.flow.instance', {processDefinitionId: id});
    };

    $scope.processTableOptions = {
      id: 'definitionProcess',
      data: 'queryProcessResult',
      isPage: false,
      colModels: [
        {name: '实例ID', index: 'id'},
        {name: '流程标识', index: 'processDefinitionId'},
        {name: '开始时间', index: 'startTime'},
        {name: '业务标识', index: 'businessKey'},
        {
          name: '操作', index: '',
          formatter: function () {
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoProcessDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ]
    };

    if ($scope.detailId !== '0') {
      $scope.queryDetail($scope.detailId);
    } else {
      $scope.queryDefinition();
    }

  });

})();
