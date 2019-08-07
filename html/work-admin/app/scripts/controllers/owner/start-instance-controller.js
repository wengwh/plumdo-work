/**
 * 流程申请控制器
 *
 * @author wengwenhui
 * @date 2019年8月6日
 */
(function () {
  'use strict';

  angular.module('adminApp').controller('OwnerStartInstanceController', function ($scope, $stateParams, $sce) {
    $scope.definitionService = $scope.FlowService($scope.restUrl.flowDefinitions);
    $scope.detailId = $stateParams.id || '0';

    $scope.queryParams = $scope.detailId === '0' ? $scope.getCacheParams() : {};
    $scope.queryParams.suspended = false;
    $scope.queryParams.latestVersion = true;
    $scope.queryParams.startableByUser = $scope.loginUser.userId;

    $scope.queryResult = {};
    $scope.selectedItem = null;

    $scope.queryDefinition = function () {
      $scope.definitionService.get({
        params: $scope.queryParams
      }, function (response) {
        $scope.queryResult = response;
      });
    };

    $scope.queryDetail = function (id) {
      $scope.definitionService.get({
        urlPath: '/' + id
      }, function (response) {
        $scope.selectedItem = response;
      });
    };

    $scope.getImageUrl = function (id) {
      if (angular.isDefined(id)) {
        return $scope.definitionService.url + '/' + id + '/image.png?token=' + $scope.loginUser.token;
      }
      return null;
    };

    $scope.getFormUrl = function (id) {
      if (angular.isDefined(id)) {
          return $sce.trustAsResourceUrl($scope.restUrl.formDefinitionPreview($scope.selectedItem.category, $scope.selectedItem.formKey, $scope.loginUser.token))
      }
      return null;
    };

    if ($scope.detailId !== '0') {
      $scope.queryDetail($scope.detailId);
    } else {
      $scope.queryDefinition();
    }

  });

})();
