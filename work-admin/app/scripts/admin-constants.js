/**
 * 系统常量定义
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp')
//   .constant('contextRoot', ".")
    .constant('contextRoot', "http://127.0.0.1:8081")
    .constant('restUrl', {
    'formModels': '/form-models',
    'stockMonsters': '/stock-monsters',
    'stockReports': '/stock-reports',
    'stockHotPlates': '/stock-hot-plates',
    'lotteryDetails': '/lottery-details',
    'weibos': '/weibos'
  });

})();