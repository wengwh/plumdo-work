/**
 * 系统常量定义
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp')
    .constant('contextRoot', {
    	formService:'http://119.29.96.227/form-service',
    	identityService:'http://localhost:8088'
    })
    .constant('restUrl', {
    	formModels: '/form-models',
    	auths: '/auths',
    	users: '/users',
    	groups: '/groups',
    	roles: '/roles',
    	menus: '/menus',
	    formDesgin : function(modelId) {
	      return 'http://119.29.96.227/form-modeler/#/design?modelId=' + modelId 
	    },
	    formPreview : function(modelId) {
	      return 'http://119.29.96.227/form-modeler/#/watch?modelId=' + modelId 
	    }
  });

})();