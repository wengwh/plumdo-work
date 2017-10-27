angular.module('plumdo.configs').config(["$httpProvider", function ($httpProvider) {
	$httpProvider.interceptors.push('httpInterceptor');
}]);

angular.module('plumdo.factorys').factory('httpInterceptor',[ '$q',"$injector","$rootScope",  function($q,$injector,$rootScope) {
	var interceptor = {
		'request' : function(config) {
			// 成功的请求方法
			$rootScope.showProgress();
			return config || $q.when(config);
		},
		'response' : function(response) {
			// 响应成功
			$rootScope.hideProgress();
			return response || $q.when(response);
		},
		'requestError' : function(rejection) {
			// 请求发生了错误，如果能从错误中恢复，可以返回一个新的请求或promise
			console.info("requestError:"+rejection);
			return $q.reject(rejection);
		},
		'responseError' : function(rejection) {
			// 请求发生了错误，如果能从错误中恢复，可以返回一个新的响应或promise
			if(rejection.data){
				var errorMsg = rejection.data.message;
				console.info("response error- status:"+rejection.status+",errorMsg:"+errorMsg);
				$rootScope.hideProgress(errorMsg);
			}else{
				console.info("response error- rejection:"+rejection);
				$rootScope.hideProgress();
			}
			
			return $q.reject(rejection);
		}
	};
	return interceptor;
}]);

angular.module('plumdo.factorys').factory('RestService', ['$http', function($http) {
	var RestService = function (url,defaultConf) {
		this.url = url;
		this.defaultConf = defaultConf;
  }
  
	RestService.prototype = {
			url:null,
			defaultConf:null,
      http: function (method, conf, callback) {
          if(this.defaultConf){
          	if(defaultConf.params){
               angular.extend(conf.params, defaultConf.params);
          	}
          	if(defaultConf.data){
              angular.extend(conf.data, defaultConf.data);
          	}
          	if(defaultConf.headers){
              angular.extend(conf.headers, defaultConf.headers);
          	}
          }
          var url = this.url;
          if (conf.urlPath){
          	url = url + conf.urlPath;
          }
          $http({method: method, url: url, params: conf.params, data: conf.data, headers: conf.headers}).
          success(function (data, status) {
              callback(data, status);
          });
      },
      get: function (conf, callback) {
        this.http('GET', conf, callback);
	    },
	    create : function (conf, callback) {
	        this.http('POST', conf, callback);
	    },
	    update : function (conf, callback) {
	        this.http('PUT', conf, callback);
	    },
	    delete : function (conf, callback) {
	        this.http('DELETE', conf, callback);
	    }
  };
	
	return function(contextRoot,defaultConf){
		return function (url) {
	  	return new RestService(contextRoot+url,defaultConf);
	  }; 
	}
 
}])

