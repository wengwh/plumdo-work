/**
 * 系统路由配置
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp').config(
    function ($stateProvider, $urlRouterProvider) {

      $stateProvider.state('home', {
        url: '/home',
        templateUrl: 'views/test.html'
      }).state('work', {
        url: '/work',
        abstract: true,
        template: '<div ui-view></div>'
      }).state('work.process-start', {
        url: '/process-start',
        templateUrl: 'views/test.html'
      }).state('work.process-run', {
        url: '/process-run',
        templateUrl: 'views/test2.html'
      }).state('work.process-finish', {
        url: '/process-finish',
        templateUrl: 'views/test2.html'
      }).state('work.process-involve', {
        url: '/process-involve',
        templateUrl: 'views/test2.html'
      }).state('work.task-todo', {
        url: '/task-todo',
        templateUrl: 'views/test2.html'
      }).state('work.task-finish', {
        url: '/task-finish',
        templateUrl: 'views/test2.html'
      }).state('modeler', {
        url: '/modeler',
        abstract: true,
        template: '<div ui-view></div>'
      }).state('modeler.form', {
        url: '/form',
        controller: 'FormModelerController',
        templateUrl: 'views/modeler/form.html'
      }).state('modeler.button', {
        url: '/button',
        templateUrl: 'views/test2.html'
      }).state('modeler.flow', {
        url: '/flow',
        templateUrl: 'views/test2.html'
      }).state('modeler.report', {
        url: '/report',
        templateUrl: 'views/test2.html'
      }).state('modeler.app', {
        url: '/app',
        templateUrl: 'views/test2.html'
      }).state('idm', {
        url: '/idm',
        abstract: true,
        template: '<div ui-view></div>'
      }).state('idm.user', {
        url: '/user',
        controller: 'UserController',
        templateUrl: 'views/idm/user.html'
      }).state('idm.group', {
        url: '/group',
        controller: 'GroupController',
        templateUrl: 'views/idm/group.html'
      }).state('idm.menu', {
        url: '/menu',
        controller: 'MenuController',
        templateUrl: 'views/idm/menu.html'
      }).state('idm.role', {
        url: '/role',
        controller: 'RoleController',
        templateUrl: 'views/idm/role.html'
      });

      $urlRouterProvider.otherwise('/modeler/form');
    });

})();
