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
    $scope.instanceService = $scope.FormService($scope.restUrl.formInstances);
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
    };

    $scope.queryDefinition = function () {
      $scope.definitionService.get({
        params: $scope.queryParams
      }, function (response) {
        $scope.queryResult = response;
      });
    };

    $scope.createInstance = function (id) {
      $scope.confirmModal({
        title: '确认创建表单实例',
        confirm: function () {
          $scope.instanceService.post({
            data: {formDefinitionId: id}
          }, function () {
            $scope.showSuccessMsg('创建表单实例成功');
            $scope.queryInstance(id);
          });
        }
      });
    };

    $scope.queryMetadata = function (id) {
      $scope.definitionService.get({
        urlPath: '/' + id + '/metadata'
      }, function (response) {
        $scope.queryFieldResult = response.fields;
        $scope.queryLayoutResult = response.layouts;
      });
    };

    $scope.watchFormLayout = function (key) {
      $scope.editConfirmModal({
        formUrl: 'form-layout-watch.html',
        title: '预览布局',
        hideFooter: true,
        property: {
          url: $sce.trustAsResourceUrl($scope.restUrl.formDefinitionPreview($scope.detailId, key, $scope.loginUser.token))
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
            return '<button type="button" class="btn btn-success btn-xs" ng-click=watchFormLayout(row.key)><i class="fa fa-wpforms"></i>&nbsp;预览</button>';
          }
        }
      ]
    };

    $scope.queryInstance = function (id) {
      $scope.instanceService.get({
        params: {formDefinitionId: id, pageNum: 1}
      }, function (response) {
        $scope.queryInstanceResult = response.data;
      });
    };

    $scope.gotoFormDetail = function (id) {
      $scope.$state.go('main.form.instance', {id: id});
    };

    $scope.gotoInstanceList = function (id) {
      $scope.$state.go('main.form.instance', {formDefinitionId: id});
    };

    $scope.instanceTableOptions = {
      id: 'definitionForm',
      data: 'queryInstanceResult',
      isPage: false,
      colModels: [
        {name: '实例ID', index: 'id', width: '20%'},
        {name: '关联表', index: 'relationTable', width: '20%'},
        {name: '数据主键', index: 'tableRelationId', width: '20%'},
        {name: '创建时间', index: 'createTime', width: '20%'},
        {
          name: '操作', index: '',
          formatter: function () {
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoFormDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
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

})
();
