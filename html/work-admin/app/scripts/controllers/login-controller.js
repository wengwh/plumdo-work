/**
 * 登录窗口控制器
 *
 * @author wengwenhui
 * @date 2018年4月2日
 */
(function() {
  'use strict';

  angular.module('adminApp').controller('LoginController', [ '$scope','$window', function($scope,$window) {
    $scope.user = {};
    $scope.user.account='admin';
    $scope.user.password='123456';
    $scope.authService = $scope.IdmService($scope.restUrl.idmAuths);
    
    if(angular.isDefined($window.localStorage.token)){
        $scope.$state.go('main.owner.start');
    }
    
    $scope.login = function() {
      $scope.authService.post({
        urlPath : '/login',
        data : $scope.user
      }, function(response) {
        $window.localStorage.token = 'Bearer ' + response.token;
        $window.localStorage.userId = response.id;
        $window.localStorage.userName = response.name;
        $window.localStorage.userAvatar = response.avatar;
        $scope.$state.go('main.owner.start');
      });
    };
  }]);
})();
