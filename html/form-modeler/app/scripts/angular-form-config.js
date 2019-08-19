(function () {
  'use strict';

  angular.module('builder.config', []).config(['$validatorProvider', function ($validatorProvider) {
    $validatorProvider.register('required', {
      invoke: 'watch',
      validator: /.+/,
      error: '字段必填.'
    });
    $validatorProvider.register('number', {
      invoke: 'watch',
      validator: /^[-+]?[0-9]*[\.]?[0-9]*$/,
      error: '字段必须是数字.'
    });
    $validatorProvider.register('email', {
      invoke: 'blur',
      validator: /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
      error: '字段必须是邮件.'
    });
    return $validatorProvider.register('url', {
      invoke: 'blur',
      validator: /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/,
      error: '字段必须是网址.'
    });

  }]).config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/design');

    $stateProvider.state('design', {
      url: '/design?modelId&token',
      data: {pageTitle: ['表单设计器']},
      templateUrl: 'views/fb-design.html'
    }).state('watch', {
      url: '/watch?modelId&formDefinitionId&formLayoutKey&token',
      data: {pageTitle: ['表单明细查看']},
      templateUrl: 'views/fb-watch.html'
    }).state('work', {
      url: '/work?formDefinitionId&formLayoutKey&formInstanceId&disable&token',
      data: {pageTitle: ['表单录入']},
      templateUrl: 'views/fb-work.html'
    })
  }]);

}).call(this);
