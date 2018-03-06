/**
 * 系统常量定义
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp')
//   .constant('contextRoot', ".")
    .constant('contextRoot', "http://119.29.96.227/form-service")
    .constant('restUrl', {
    'formModels': '/form-models',
    'stockMonsters': '/stock-monsters',
    'stockReports': '/stock-reports',
    'stockHotPlates': '/stock-hot-plates',
    'lotteryDetails': '/lottery-details',
    'weibos': '/weibos',
    formDesgin : function(modelId) {
      return 'http://119.29.96.227/form-modeler/#/design?modelId=' + modelId 
    },
    formPreview : function(modelId) {
      return 'http://119.29.96.227/form-modeler/#/watch?modelId=' + modelId 
    }
  });

})();