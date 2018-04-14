/**
 * 简单列表框
 *
 * @author wengwenhui
 * @date 2018年4月11日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('TableModalController',
    function($scope) {
      $scope.queryResult = [];
      $scope.colModels = $scope.colModels || [];
      $scope.colModels.push({
        name : '操作',
        index : '',
        formatter : function() {
          return '<button type="button" class="btn btn-danger btn-xs" ng-click=deleteRow(row.id)>'+
             '<i class="fa fa-trash-o"></i>&nbsp;删除关联</button>';
        }
      });

      $scope.tableOptions = {
        id : 'tableModal',
        data : 'queryResult',
        isPage : false,
        colModels : $scope.colModels
      };
  
      $scope.query = function() {
        $scope.service.get({}, function(response) {
          $scope.queryResult = response;
        });
      };

      $scope.deleteRow = function(rowId) {
        $scope.service.delete({
          urlPath : '/' + rowId
        }, function() {
          $scope.showSuccessMsg('删除关联成功');
          $scope.query();
        });
      };
      
      $scope.query();
    });
})();
