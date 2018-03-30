/**
 * 主窗口控制
 * 
 * @author wengwh
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('LoginController', [ '$scope','$window', function($scope,$window) {
    $scope.user = {};
    $scope.user.account='';
    $scope.user.password='';
    $scope.userService = $scope.IdmService($scope.restUrl.users);

    if(!($window.localStorage.token == null || $window.localStorage.token == 'null'
        || $window.localStorage.token == '')){
//        $scope.$state.go('main.home');
    }
    
    $scope.login = function() {
      $scope.userService.post({
        urlPath : '/login',
        data : $scope.user
      }, function(response) {
        $window.localStorage.token = response.token;
        $scope.$state.go('main.home');
      });
    };
  }]);
})();
