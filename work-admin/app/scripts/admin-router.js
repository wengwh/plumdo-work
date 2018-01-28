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
        url: '',
        templateUrl: 'views/main.html',
        controller: 'MainController',
        abstract: true
      }).state('home.stock', {
        url: '/stock',
        templateUrl: 'views/test.html'
      }).state('home.stock2', {
        url: '/stock2',
        templateUrl: 'views/test2.html'
      });

      $urlRouterProvider.otherwise('/stock');
    });

})();