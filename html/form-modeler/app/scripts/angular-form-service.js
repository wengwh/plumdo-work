(function() {
  'use strict';
  
  angular.module('builder.service', []).service('FormRestService',['$rootScope','$http', '$stateParams', 'restUrl', function($rootScope,$http, $stateParams, restUrl) {
    return {
      getStencilSet : function() {
        return $http({
          method: 'GET',
          url : restUrl.getStencilSet()
        });
      },
      getModelJson : function() {
        return $http({
          method : 'GET',
          headers : {
            'Token': $stateParams.token
          },
          url : restUrl.getModelJson($stateParams.modelId)
        }).error(function(data) {
          $rootScope.showErrorMsg(data.msg);
        });
      },
      saveModelJson : function(data) {
        $rootScope.showProgress();
        return $http({
          method : 'PUT',
          headers : {
            'Token': $stateParams.token
          },
          url : restUrl.saveModelJson($stateParams.modelId),
          data : data
        }).success(function(data) {
          $rootScope.hideProgressBySucess('保存表单成功');
        }).error(function(data) {
          $rootScope.hideProgressByError(data.msg);
        });
      }
      
    };
  }]);
  
}).call(this);
  
