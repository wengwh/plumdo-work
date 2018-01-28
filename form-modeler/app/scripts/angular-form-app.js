(function () {
  'use strict';

  angular.module('builder', [
    'ui.router', 
    'validator',
    'summernote',
    'ui.sortable',
    'ui.tree',
    'cgNotify',
    'localytics.directives',
    'builder.constant',
    'builder.config',
    'builder.provider',
    'builder.controller',
    'builder.directive'
  ]).run(['$rootScope', 'notify', function ($rootScope,notify) {

    $rootScope.progressNum = 0;
    
    $rootScope.showProgress = function (msg) {
      $rootScope.progressNum++;
      if (msg) {
        $rootScope.showSuccessMsg(msg);
      }
    };

    $rootScope.hideProgressBySucess = function (msg) {
      $rootScope.hideProgress(msg, 'notify-success');
    };

    $rootScope.hideProgressByError = function (msg) {
      $rootScope.hideProgress(msg, 'notify-error');
    };

    $rootScope.hideProgress = function (msg, classes) {
      $rootScope.progressNum--;
      if (msg) {
        if (classes && classes != null) {
          $rootScope.showMsg(msg, 2000, classes);
        } else {
          $rootScope.showErrorMsg(msg);
        }
      }
    };
    
    $rootScope.showSuccessMsg = function (msg) {
      $rootScope.showMsg(msg, 15000, 'notify-success');
    };

    $rootScope.showErrorMsg = function (msg) {
      $rootScope.showMsg(msg, 30000, 'notify-error');
    };

    $rootScope.showMsg = function (msg, duration, classes) {
      notify({
        message: msg,
        duration: duration,
        position: 'right',
        classes: classes
      });
    };

  }]).filter('to_trusted', ['$sce', function ($sce) {
    return function (text) {
      return $sce.trustAsHtml(text);
    }
  }]);
  ;

}).call(this);
