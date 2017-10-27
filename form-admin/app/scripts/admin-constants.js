(function() {
	'use strict';

	angular.module('plumdo.constants')
	.constant('contextRoot',  "http://localhost:8081")
	.constant('restUrl', {
		'formModels' : '/form-models',
		'formDefinitions' : '/form-definitions'
	});

}).call(this);