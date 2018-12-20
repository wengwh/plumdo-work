(function () {
  'use strict';

  angular.module('builder.service', []).service('FormRestService', ['$rootScope', '$http', '$stateParams', 'restUrl', function ($rootScope, $http, $stateParams, restUrl) {
    return {
      getStencilSet: function () {
        return $http({
          method: 'GET',
          url: restUrl.getStencilSet()
        });
      },
      getModelJson: function () {
        return $http({
          method: 'GET',
          headers: {
            'Token': $stateParams.token
          },
          url: restUrl.getModelJson($stateParams.modelId)
        }).error(function (data) {
          $rootScope.showErrorMsg(data.msg);
        });
      },
      saveModelJson: function (data) {
        $rootScope.showProgress();
        return $http({
          method: 'PUT',
          headers: {
            'Token': $stateParams.token
          },
          url: restUrl.saveModelJson($stateParams.modelId),
          data: data
        }).success(function () {
          $rootScope.hideProgressBySuccess('保存表单成功');
        }).error(function (data) {
          $rootScope.hideProgressByError(data.msg);
        });
      },
      getDefinitionJson: function () {
        return $http({
          method: 'GET',
          headers: {
            'Token': $stateParams.token
          },
          url: restUrl.getDefinitionJson($stateParams.formDefinitionId, $stateParams.formLayoutKey)
        }).error(function (data) {
          $rootScope.showErrorMsg(data.msg);
        });
      },
      createInstance: function (postData) {
        return $http({
          method: 'POST',
          headers: {
            'Token': $stateParams.token
          },
          url: restUrl.createInstance(),
          data: postData
        }).error(function (data) {
          $rootScope.showErrorMsg(data.msg);
        });
      },
      updateInstance: function (postData) {
        return $http({
          method: 'PUT',
          headers: {
            'Token': $stateParams.token
          },
          url: restUrl.updateInstanceData($stateParams.formInstanceId),
          data: postData
        }).error(function (data) {
          $rootScope.showErrorMsg(data.msg);
        });
      },
      getInstance: function () {
        return $http({
          method: 'GET',
          headers: {
            'Token': $stateParams.token
          },
          url: restUrl.getInstanceData($stateParams.formInstanceId),
        }).error(function (data) {
          $rootScope.showErrorMsg(data.msg);
        });
      }
    }
      ;
  }]);

}).call(this);
  
