(function() {
	'use strict';

	angular.module('builder.constant', [])
	.constant('restUrl', {
		getModelJson : function(modelId) {
			return 'http://work.plumdo.com/form-service/form-layouts/' + modelId + '/json';
		},
		saveModelJson : function(modelId) {
			return 'http://work.plumdo.com/form-service/form-layouts/' + modelId + '/json';
		},
		getFormField : function() {
      return 'http://work.plumdo.com/form-service/form-fields';
    },
		getDefinitionJsonById : function(definitionId) {
			return 'http://work.plumdo.com/form-service/form-definitions/' + definitionId + '/json';
		},
		getDefinitionJsonByKey : function(definitionKey) {
			return 'http://work.plumdo.com/form-service/form-definitions/' + definitionKey + '/latest/json';
		},
		createInstance : function() {
			return 'http://work.plumdo.com/form-service/form-instances';
		},
		updateInstance : function(instanceId) {
			return 'http://work.plumdo.com/form-service/form-instances/'+instanceId;
		},
		getInstance : function(instanceId) {
			return 'http://work.plumdo.com/form-service/form-instances/'+instanceId;
		},
		getStencilSet : function() {
			return './stencilset.json?version=' + Date.now();
		}
	});

}).call(this);