(function() {
	'use strict';

	angular.module('builder.constant', [])
	.constant('restUrl', {
		getModelJson : function(modelId) {
			return 'http://localhost:8081/form-models/' + modelId + '/json';
		},
		saveModelJson : function(modelId) {
			return 'http://localhost:8081/form-models/' + modelId + '/json';
		},
		getDefinitionJsonById : function(definitionId) {
			return 'http://localhost:8081/form-definitions/' + definitionId + '/json';
		},
		getDefinitionJsonByKey : function(definitionKey) {
			return 'http://localhost:8081/form-definitions/' + definitionKey + '/latest/json';
		},
		createInstance : function() {
			return 'http://localhost:8081/form-instances';
		},
		updateInstance : function(instanceId) {
			return 'http://localhost:8081/form-instances/'+instanceId;
		},
		getInstance : function(instanceId) {
			return 'http://localhost:8081/form-instances/'+instanceId;
		},
		getStencilSet : function() {
			return './stencilset.json?version=' + Date.now();
		}
	});

}).call(this);