/**
 * 系统路由配置
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp').config(
    function ($stateProvider, $urlRouterProvider) {
      
      $stateProvider.state('login', {
        url: '/login',
        templateUrl: 'views/login.html',
        controller : 'LoginController'
      }).state('main', {
        url: '',
        templateUrl : 'views/main.html',
        controller : 'MainController',
        abstract : true
      }).state('main.blank', {
        url: '/blank',
        template: '<h1 class="text-center"> 页面不存在 </h1>'
      }).state('main.home', {
        url: '/home',
        templateUrl: 'views/test.html'
      }).state('main.work', {
        url: '/work',
        abstract: true,
        template: '<div ui-view></div>'
      }).state('main.work.process-start', {
        url: '/process-start',
        templateUrl: 'views/test.html'
      }).state('main.work.process-run', {
        url: '/process-run',
        templateUrl: 'views/test2.html'
      }).state('main.work.process-finish', {
        url: '/process-finish',
        templateUrl: 'views/test2.html'
      }).state('main.work.process-involve', {
        url: '/process-involve',
        templateUrl: 'views/test2.html'
      }).state('main.work.task-todo', {
        url: '/task-todo',
        templateUrl: 'views/test2.html'
      }).state('main.work.task-finish', {
        url: '/task-finish',
        templateUrl: 'views/test2.html'
      }).state('main.modeler', {
        url: '/modeler',
        abstract: true,
        template: '<div ui-view></div>'
      }).state('main.modeler.form', {
        url: '/form',
        controller: 'FormModelerController',
        templateUrl: 'views/modeler/form.html'
      }).state('main.modeler.button', {
        url: '/button',
        templateUrl: 'views/test2.html'
      }).state('main.modeler.flow', {
        url: '/flow',
        templateUrl: 'views/test2.html'
      }).state('main.modeler.report', {
        url: '/report',
        templateUrl: 'views/test2.html'
      }).state('main.modeler.app', {
        url: '/app',
        templateUrl: 'views/test2.html'
      }).state('main.idm', {
        url: '/idm',
        abstract: true,
        template: '<div ui-view></div>'
      }).state('main.idm.user', {
        url: '/user',
        controller: 'UserController',
        templateUrl: 'views/idm/user.html'
      }).state('main.idm.group', {
        url: '/group',
        controller: 'GroupController',
        params:{cacheParams:{parentGroupArray:[],queryParamsArray:[]}},
        templateUrl: 'views/idm/group.html'
      }).state('main.idm.menu', {
        url: '/menu',
        controller: 'MenuController',
        params:{cacheParams:{queryParamsArray:[]}},
        templateUrl: 'views/idm/menu.html'
      }).state('main.idm.role', {
        url: '/role',
        controller: 'RoleController',
        templateUrl: 'views/idm/role.html'
      });

      $urlRouterProvider.otherwise('/home');
    });

})();
