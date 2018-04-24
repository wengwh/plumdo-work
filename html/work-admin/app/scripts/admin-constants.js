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
      formService:'http://work.plumdo.com/form-service',
      identityService:'http://work.plumdo.com/identity-service',
//      identityService:'http://localhost:8088',
      flowService:'http://work.plumdo.com/flow-service'
//      flowService:'http://localhost:8082'
    })
    .constant('restUrl', {
      formModels: '/form-models',
      flowModels: '/models',
      flowDefinitions: '/process-definitions',
      flowInstances: '/process-instances',
      flowTasks: '/tasks',
      idmAuths: '/auths',
      idmUsers: '/users',
      idmGroups: '/groups',
      idmRoles: '/roles',
      idmMenus: '/menus',
      formDesgin : function(modelId) {
        return 'http://work.plumdo.com/form-modeler/#/design?modelId=' + modelId;
      },
      formPreview : function(modelId) {
        return 'http://work.plumdo.com/form-modeler/#/watch?modelId=' + modelId;
      },
      flowDesign : function(modelId,token) {
        return 'http://work.plumdo.com/flow-modeler/#/editor/' + modelId+'?token='+token;
//        return 'http://localhost:9004/#/editor/' + modelId+'?token='+token;
      }
  });

})();