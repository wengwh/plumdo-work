/**
 * 流程实例控制器
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
(function () {
  'use strict';

  angular.module('adminApp').controller('FormInstanceController', function ($scope, $stateParams, $q) {
    $scope.instanceService = $scope.FormService($scope.restUrl.formInstances);
    $scope.definitionService = $scope.FormService($scope.restUrl.formDefinitions);
    $scope.detailId = $stateParams.id || '0';
    $scope.queryParams = $scope.detailId === '0' ? $scope.getCacheParams() : {};
    if (angular.isUndefined($scope.queryParams.formDefinitionId)) {
      $scope.queryParams.formDefinitionId = $stateParams.formDefinitionId;
    }
    $scope.queryResult = {};
    $scope.selectedItem = null;

    $scope.queryDefinition = function () {
      $scope.definitionService.get({}, function (response) {
        $scope.definitions = response.data;
      });
    };

    $scope.queryDetail = function (id) {
      $scope.instanceService.get({
        urlPath: '/' + id
      }, function (response) {
        $scope.selectedItem = response;
        $scope.queryFormData();
      });
    };

    $scope.queryInstance = function () {
      $scope.instanceService.get({
        params: $scope.queryParams
      }, function (response) {
        $scope.queryResult = response;
      });
    };

    $scope.tableOptions = {
      id: 'instance',
      data: 'queryResult',
      colModels: [
        {name: '实例ID', index: 'id', width: '10%'},
        {name: '定义ID', index: 'formDefinitionId', sortable: true, width: '10%'},
        {name: '定义名称', index: 'formDefinitionName', sortable: true, width: '10%'},
        {name: '关联表', index: 'relationTable', sortable: true, width: '10%'},
        {name: '数据主键', index: 'tableRelationId', sortable: true, width: '10%'},
        {name: '创建时间', index: 'createTime', sortable: true, width: '10%'},
        {
          name: '操作', index: '', width: '10%',
          formatter: function () {
            return '<button type="button" class="btn btn-info btn-xs" ng-click=gotoDetail(row.id)><i class="fa fa-eye"></i>&nbsp;明细</button>';
          }
        }
      ],
      loadFunction: $scope.queryInstance,
      queryParams: $scope.queryParams,
      sortName: 'createTime',
      sortOrder: 'desc'
    };

    $scope.queryFormData = function () {
      let dataResult;

      let fieldsPromise = $scope.definitionService.get({
        urlPath: '/' + $scope.selectedItem.formDefinitionId + '/metadata'
      }, function (response) {
        $scope.queryDataResult = response.fields;
      });

      let dataPromise = $scope.instanceService.get({
        urlPath: '/' + $scope.selectedItem.id + '/data'
      }, function (response) {
        dataResult = response;
      });

      $q.all([fieldsPromise, dataPromise]).then(function () {
        angular.forEach($scope.queryDataResult, function (item) {
          item.value = dataResult[item.key];
        });
      });
    };

    $scope.updateFormData = function (id, row) {
      $scope.editConfirmModal({
        formUrl: 'form-data-edit.html',
        title: '编辑表单内容',
        formData: row,
        confirm: function (formData, modalInstance) {
          let putData = {};
          putData[formData.key] = formData.value;

          $scope.instanceService.put({
            urlPath: '/' + id + '/data',
            data: putData
          }, function () {
            $scope.showSuccessMsg('修改字段成功');
            $scope.queryFormData();
            modalInstance.close();
          });
        }
      });
    };

    $scope.dataTableOptions = {
      id: 'instanceData',
      data: 'queryDataResult',
      isPage: false,
      colModels: [
        {name: '名称', index: 'name', width: '20%'},
        {name: '标识', index: 'key', width: '20%'},
        {name: '内容', index: 'value', width: '30%'},
        {name: '备注', index: 'remark', width: '20%'},
        {
          name: '操作', index: '',
          formatter: function () {
            return '<button type="button" class="btn btn-success btn-xs" ng-click=updateFormData(selectedItem.id,row)><i class="fa fa-trash-o"></i>&nbsp;修改</button>';
          }
        }
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
