/**
 * 页面总入口，定义依赖的第三方库，定义共有方法，变量
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp', ['ui.router']).run(function ($rootScope, $state, $timeout) {
    $rootScope.$state = $state;
    $rootScope.progressNum = 0;

  });

})();
