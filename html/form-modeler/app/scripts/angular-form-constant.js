(function () {
  'use strict';

  angular.module('builder.constant', [])
    .constant('restUrl', {
      getModelJson: function (modelId) {
//		  work.plumdo.com/form-service
        return 'http://work.plumdo.com/form-service/form-layouts/' + modelId + '/json';
      },
      saveModelJson: function (modelId) {
        return 'http://work.plumdo.com/form-service/form-layouts/' + modelId + '/json';
      },
      getDefinitionJson: function (definitionId, layoutKey) {
        return 'http://work.plumdo.com/form-service/form-definitions/' + definitionId + '/' + layoutKey + '/json';
        // return 'http://localhost:8083/form-definitions/' + definitionId + '/' + layoutKey + '/json';
      },
      createInstance: function () {
        return 'http://work.plumdo.com/form-service/form-instances';
        // return 'http://localhost:8083/form-instances';
      },
      updateInstanceData: function (instanceId) {
        return 'http://work.plumdo.com/form-service/form-instances/' + instanceId + '/data';
        // return 'http://localhost:8083/form-instances/' + instanceId + '/data';
      },
      getInstanceData: function (instanceId) {
        return 'http://work.plumdo.com/form-service/form-instances/' + instanceId + '/data';
        // return 'http://localhost:8083/form-instances/' + instanceId + '/data';
      },
      getStencilSet: function () {
        return './stencilset.json?version=' + Date.now();
      }
    });

}).call(this);