/**
 * Http全局拦截器，和rest接口调用封装
 *
 * @author wengwh
 */

(function () {
  'use strict';

  angular.module('adminApp').config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('httpInterceptor');
  }]);

  angular.module('adminApp').factory('httpInterceptor', ['$q', '$injector', '$window', '$rootScope', function ($q, $injector, $window, $rootScope) {
    var interceptor = {
      'request': function (config) {
        // 成功的请求方法
        $rootScope.showProgress();
        return config || $q.when(config);
      },
      'response': function (response) {
        // 响应成功
        $rootScope.hideProgress();
        return response || $q.when(response);
      },
      'requestError': function (rejection) {
        // 请求发生了错误，如果能从错误中恢复，可以返回一个新的请求或promise
        console.info('requestError:' + rejection);
        return $q.reject(rejection);
      },
      'responseError': function (rejection) {
        // 请求发生了错误，如果能从错误中恢复，可以返回一个新的响应或promise
        console.info('response error- rejection:' + rejection);
      	console.info(rejection)
        if(rejection.status==401){
					$window.localStorage.token = null;
					$rootScope.$state.go('login');
					$rootScope.hideProgress('用户校验过期',true);
        }else if(rejection.data && rejection.data.msg){
        	$rootScope.hideProgress(rejection.data.msg, true);
        }else{
          $rootScope.hideProgress('HTTP请求异常', true);
        }
        return $q.reject(rejection);
      }
    };
    return interceptor;
  }]);

  angular.module('adminApp').factory('RestService', ['$http', '$q', '$window',function ($http, $q,$window) {
    var RestService = function (url, defaultConf) {
      this.url = url;
      this.defaultConf = defaultConf;
    };

    RestService.prototype = {
      url: null,
      defaultConf: null,
      http: function (method, conf, callback) {
        if (this.defaultConf) {
          if (this.defaultConf.params) {
            angular.extend(conf.params||{}, this.defaultConf.params);
          }
          if (this.defaultConf.data) {
            angular.extend(conf.data||{}, this.defaultConf.data);
          }
          if (this.defaultConf.headers) {
            angular.extend(conf.headers||{}, this.defaultConf.headers);
          }
          if (this.defaultConf.responseType) {
            angular.extend(conf.responseType||{}, this.defaultConf.responseType);
          }
        }
        console.info(this.url + (conf.urlPath || ''))
        var defer = $q.defer();
        return $http({
          method: method,
          url: this.url + (conf.urlPath || ''),
          params: conf.params,
          data: conf.data,
          responseType: conf.responseType,
          headers: angular.extend(conf.headers||{},{'Token':'Bearer ' + $window.localStorage.token,'User-ID':$window.localStorage.userId})
        })
          .then(function successCallback(response) {
            callback(response.data);
            defer.resolve(response);
          }, function errorCallback(response) {
            defer.reject(response);
          });
      },
      get: function (conf, callback) {
        return this.http('GET', conf, callback);
      },
      post: function (conf, callback) {
        return this.http('POST', conf, callback);
      },
      put: function (conf, callback) {
        return this.http('PUT', conf, callback);
      },
      delete: function (conf, callback) {
        return this.http('DELETE', conf, callback);
      }
    };
    return function (contextRoot, defaultConf) {
      return function (url) {
        return new RestService(contextRoot + url, defaultConf);
      };
    };

  }]);

})();