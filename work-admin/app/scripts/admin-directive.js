/**
 * 时间指令，由于第三方的时间使用默认按钮，input不会触发，自定义通过事件触发
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp').directive('viewLoad', function () {
    return {
      restrict: 'A',
      templateUrl: 'views/common/view-load.html',
      link: function (scope, element) {
        $(element).fadeIn(300);
      }
    };
  });

})();
