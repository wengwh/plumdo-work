/**
 * 流程申请控制器
 *
 * @author wengwenhui
 * @date 2019年8月6日
 */
(function () {
  'use strict';

  angular.module('adminApp').controller('OwnerStartInstanceController', function ($scope, $stateParams, $sce, $window) {
    $scope.definitionService = $scope.FlowService($scope.restUrl.flowDefinitions);
    $scope.instanceService = $scope.FlowService($scope.restUrl.flowInstances);
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
        return $sce.trustAsResourceUrl($scope.restUrl.formDefinitionWork($scope.selectedItem.category, $scope.selectedItem.formKey, null, $scope.loginUser.token));
      }
      return null;
    };

    $scope.createInstance = function (businessKey) {
      $scope.instanceService.post({
        data: {businessKey: businessKey, processDefinitionId: $scope.selectedItem.id}
      }, function () {
        $scope.showSuccessMsg('流程单申请成功');
        $scope.gotoList();
      });
    };

    $scope.saveFormData = function () {
      $window.frames[0].frameElement.contentWindow.saveFormData(function (data) {
        $scope.createInstance(data.id);
      });
    };

    if ($scope.detailId !== '0') {
      $scope.queryDetail($scope.detailId);
    } else {
      $scope.queryDefinition();
    }

  });

})();
