/**
 * 系统常量定义
 *
 * @author wengwenhui
 * @date 2018年4月20日
 */
(function () {
  'use strict';

  angular.module('adminApp')
    .constant('contextRoot', {
        flowService:'http://work.plumdo.com/flow-service',
        identityService:'http://work.plumdo.com/identity-service',
    	  formService:'http://work.plumdo.com/form-service'
//      flowService:'http://localhost:8081'
//      identityService:'http://localhost:8082',
// 		formService:'http://localhost:8083'
    })
    .constant('restUrl', {
      formTables: '/form-tables',
      formFields: '/form-fields',
      formLayouts: '/form-layouts',
      formDefinitions: '/form-definitions',
      formInstances: '/form-instances',
      flowModels: '/models',
      flowDefinitions: '/process-definitions',
      flowInstances: '/process-instances',
      flowTasks: '/tasks',
      idmAuths: '/auths',
      idmUsers: '/users',
      idmGroups: '/groups',
      idmRoles: '/roles',
      idmMenus: '/menus',
      formDesgin : function(modelId,token) {
        return 'http://work.plumdo.com/form-modeler/#/design?modelId=' + modelId + '&token='+token;
//        return 'http://localhost:9002/#/design?modelId=' + modelId + '&token='+token;
      },
      formPreview : function(modelId,token) {
        return 'http://work.plumdo.com/form-modeler/#/watch?modelId=' + modelId + '&token='+token;
//        return 'http://localhost:9002/#/watch?modelId=' + modelId + '&token='+token;
      },
      flowDesign : function(modelId,token) {
        return 'http://work.plumdo.com/flow-modeler/#/editor/' + modelId + '?token='+token;
//        return 'http://localhost:9004/#/editor/' + modelId+'?token='+token;
      }
  });

})();