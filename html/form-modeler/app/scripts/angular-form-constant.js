(function() {
	'use strict';

	angular.module('builder.constant', [])
	.constant('restUrl', {
		getModelJson : function(modelId) {
			return 'http://119.29.96.227/form-service/form-models/' + modelId + '/json';
		},
		saveModelJson : function(modelId) {
			return 'http://119.29.96.227/form-service/form-models/' + modelId + '/json';
		},
		getDefinitionJsonById : function(definitionId) {
			return 'http://119.29.96.227/form-service/form-definitions/' + definitionId + '/json';
		},
		getDefinitionJsonByKey : function(definitionKey) {
			return 'http://119.29.96.227/form-service/form-definitions/' + definitionKey + '/latest/json';
		},
		createInstance : function() {
			return 'http://119.29.96.227/form-service/form-instances';
		},
		updateInstance : function(instanceId) {
			return 'http://119.29.96.227/form-service/form-instances/'+instanceId;
		},
		getInstance : function(instanceId) {
			return 'http://119.29.96.227/form-service/form-instances/'+instanceId;
		},
		getStencilSet : function() {
			return './stencilset.json?version=' + Date.now();
		}
	});

}).call(this);