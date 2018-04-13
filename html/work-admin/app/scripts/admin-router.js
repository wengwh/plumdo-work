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
      }).state('main.modeler', {
        url: '/modeler',
        abstract: true,
        template: '<div ui-view></div>'
      }).state('main.modeler.form', {
        url: '/form',
        controller: 'FormModelerController',
        templateUrl: 'views/modeler/form.html'
      }).state('main.modeler.flow', {
        url: '/flow/?:id',
        controller: 'ModelerFlowController',
        templateUrl: 'views/modeler/flow.html'
      }).state('main.idm', {
        url: '/idm',
        abstract: true,
        template: '<div ui-view></div>'
      }).state('main.idm.user', {
        url: '/user',
        controller: 'UserController',
        templateUrl: 'views/idm/user.html'
      }).state('main.idm.group', {
        url: '/group/?:id',
        controller: 'GroupController',
        templateUrl: 'views/idm/group.html'
      }).state('main.idm.menu', {
        url: '/menu/?:id',
        controller: 'MenuController',
        templateUrl: 'views/idm/menu.html'
      }).state('main.idm.role', {
        url: '/role',
        controller: 'RoleController',
        templateUrl: 'views/idm/role.html'
      }).state('main.flow', {
        url: '/flow',
        abstract: true,
        template: '<div ui-view></div>'
      }).state('main.flow.definition', {
        url: '/definition',
        controller: 'FlowDefinitionController',
        templateUrl: 'views/flow/definition.html'
      });

      $urlRouterProvider.otherwise('/flow');
    });

})();
