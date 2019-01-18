/**
 * 表单模型控制器
 *
 * @author wengwenhui
 * @date 2018年8月29日
 */
(function () {
  'use strict';

  angular.module('adminApp').controller('ModelerFormController', function ($scope, $stateParams, $q, $window, $sce) {
    $scope.formTableService = $scope.FormService($scope.restUrl.formTables);
    $scope.formFieldService = $scope.FormService($scope.restUrl.formFields);
    $scope.formLayoutService = $scope.FormService($scope.restUrl.formLayouts);

    $scope.detailId = $stateParams.id || '0';
    $scope.selectedItem = null;

    $scope.queryParams = $scope.detailId === '0' ? $scope.getCacheParams() : {};
    $scope.queryResult = {};

    $scope.queryFieldParams = {tableId: $scope.detailId};
    $scope.queryFieldResult = {};

    $scope.queryLayoutParams = {tableId: $scope.detailId};
    $scope.queryLayoutResult = {};

    $scope.queryDetail = function (id) {
      $scope.formTableService.get({
        urlPath: '/' + id
      }, function (response) {
        $scope.selectedItem = response;
      });
    };

    $scope.queryFormTable = function () {
      $scope.formTableService.get({
        params: $scope.queryParams
      }, function (response) {
        $scope.queryResult = response;
      });
    };

    $scope.deleteFormTable = function (id) {
      $scope.confirmModal({
        title: '确认删除表单',
        confirm: function () {
          $scope.formTableService.delete({
            urlPath: '/' + id
          }, function () {
            $scope.showSuccessMsg('删除表单成功');
            $scope.gotoList();
          });
        }
      });
    };

    $scope.deployFormTable = function (id) {
      $scope.confirmModal({
        title: '确认部署表单',
        confirm: function () {
          $scope.formTableService.post({
            urlPath: '/' + id + '/deploy'
          }, function () {
            $scope.showSuccessMsg('部署表单成功');
          });
        }
      });
    };

    $scope.editFormTable = function (id) {
      $scope.editModal({
        id: id,
        formUrl: 'form-table-edit.html',
        title: '表单',
        formData: {},
        service: $scope.formTableService,
        complete: function () {
          if (angular.isDefined(id)) {
            $scope.queryDetail(id);
          } else {
            $scope.queryFormTable();
          }
        }
      });
    };

    $scope.queryFormField = function () {
      $scope.formFieldService.get({
        params: $scope.queryFieldParams
      }, function (response) {
        $scope.queryFieldResult = response;
      });
    };

    $scope.editFormField = function (id) {
      var formData = {tableId: $scope.detailId};
      if (angular.isUndefined(id)) {
        formData.key = $scope.guid();
      }

      $scope.editModal({
        id: id,
        formUrl: 'form-field-edit.html',
        title: '字段',
        formData: formData,
        service: $scope.formFieldService,
        complete: $scope.queryFormField
      });
    };

    $scope.deleteFormField = function (id) {
      $scope.confirmModal({
        title: '确认删除字段',
        confirm: function () {
          $scope.formFieldService.delete({
            urlPath: '/' + id
          }, function () {
            $scope.showSuccessMsg('删除字段成功');
            $scope.queryFormField();
          });
        }
      });
    };

    $scope.fieldTableOptions = {
      id: 'field',
      data: 'queryFieldResult',
      colModels: [
        {name: '名称', index: 'name', sortable: true, width: '10%'},
        {name: '标识', index: 'key', sortable: true, width: '15%'},
        {name: '备注', index: 'remark', width: '12%'},
        {name: '创建时间', index: 'createTime', sortable: true, width: '12%'},
        {name: '修改时间', index: 'lastUpdateTime', sortable: true, width: '12%'},
        {
          name: '操作', index: '', width: '15%',
          formatter: function () {
            return '<div class="th-btn-group">' +
              '<button type="button" class="btn btn-info btn-xs" ng-click=editFormField(row.id)>' +
              '<i class="fa fa-pencil"></i>&nbsp;编辑</button>' +
              '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteFormField(row.id)>' +
              '<i class="fa fa-trash-o"></i>&nbsp;删除</button>' +
              '</div>';
          }
        }
      ],
      loadFunction: $scope.queryFormField,
      queryParams: $scope.queryFieldParams,
      sortName: 'name',
      sortOrder: 'asc'
    };


    $scope.queryFormLayout = function () {
      $scope.formLayoutService.get({
        params: $scope.queryLayoutParams
      }, function (response) {
        $scope.queryLayoutResult = response;
      });
    };

    $scope.editFormLayout = function (id) {
      $scope.editModal({
        id: id,
        formUrl: 'form-layout-edit.html',
        title: '布局',
        formData: {tableId: $scope.detailId},
        service: $scope.formLayoutService,
        complete: $scope.queryFormLayout
      });
    };

    $scope.watchFormLayout = function (id) {
      $scope.editConfirmModal({
        formUrl: 'form-layout-watch.html',
        title: '预览布局',
        hideFooter: true,
        property: {
          url: $sce.trustAsResourceUrl($scope.restUrl.formModelPreview(id, $scope.loginUser.token))
        }
      });
    };

    $scope.deleteFormLayout = function (id) {
      $scope.confirmModal({
        title: '确认删除布局',
        confirm: function () {
          $scope.formLayoutService.delete({
            urlPath: '/' + id
          }, function () {
            $scope.showSuccessMsg('删除布局成功');
            $scope.queryFormLayout();
          });
        }
      });
    };

    $scope.designFormLayout = function (id) {
      $window.open($scope.restUrl.formDesgin(id, $scope.loginUser.token));
    };

    $scope.layoutTableOptions = {
      id: 'layout',
      data: 'queryLayoutResult',
      colModels: [
        {name: '名称', index: 'name', sortable: true, width: '10%'},
        {name: '标识', index: 'key', sortable: true, width: '10%'},
        {name: '备注', index: 'remark', width: '12%'},
        {name: '创建时间', index: 'createTime', sortable: true, width: '12%'},
        {name: '修改时间', index: 'lastUpdateTime', sortable: true, width: '12%'},
        {
          name: '操作', index: '', width: '15%',
          formatter: function () {
            return '<div class="th-btn-group">' +
              '<button type="button" class="btn btn-success btn-xs" ng-click=watchFormLayout(row.id)>' +
              '<i class="fa fa-wpforms"></i>&nbsp;预览</button>' +
              '<button type="button" class="btn btn-info btn-xs" ng-click=editFormLayout(row.id)>' +
              '<i class="fa fa-pencil"></i>&nbsp;编辑</button>' +
              '<button type="button" class="btn btn-success btn-xs" ng-click=designFormLayout(row.id)>' +
              '<i class="fa fa-wpforms"></i>&nbsp;设计</button>' +
              '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteFormLayout(row.id)>' +
              '<i class="fa fa-trash-o"></i>&nbsp;删除</button>' +
              '</div>';
          }
        }
      ],
      loadFunction: $scope.queryFormLayout,
      queryParams: $scope.queryLayoutParams,
      sortName: 'name',
      sortOrder: 'asc'
    };


    if ($scope.detailId !== '0') {
      $scope.queryDetail($scope.detailId);
      $scope.queryFormField();
      $scope.queryFormLayout();
    } else {
      $scope.queryFormTable();
    }

  });

})();
