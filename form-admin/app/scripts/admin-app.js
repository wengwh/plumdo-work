(function (angular) {
  'use strict';
  
  angular.module('plumdo.configs', []);
  angular.module('plumdo.services', []);
  angular.module('plumdo.controllers', []);
  angular.module('plumdo.directives', []);
  angular.module('plumdo.filters', []);
  angular.module('plumdo.factorys', []);
  angular.module('plumdo.constants', []);

  var $stateProviderRef = null;
  
  angular.module('plumdo', [
    'ui.router', // Routing
    'ui.router.state.events',
    'oc.lazyLoad', // ocLazyLoad
    'ui.bootstrap', // Ui Bootstrap
    'cgNotify',
    'ngIdle', // Idle timer
    'ngSanitize',
    'plumdo.constants',
    'plumdo.factorys',
    'plumdo.directives',
    'plumdo.configs',
    'plumdo.filters',
    'plumdo.controllers',
    'plumdo.services'
  ]).config(["$provide", "$compileProvider", "$controllerProvider", "$filterProvider", function ($provide, $compileProvider, $controllerProvider, $filterProvider) {
		angular.module('plumdo.controllers').controller = $controllerProvider.register;
		angular.module('plumdo.directives').directive = $compileProvider.directive;
		angular.module('plumdo.filters').filter = $filterProvider.register;
		angular.module('plumdo.factorys').factory = $provide.factory;
		angular.module('plumdo.services').service = $provide.service;
		angular.module('plumdo.constants').constant = $provide.constant;
  }]).config(function($locationProvider,$stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider, KeepaliveProvider) {
  	IdleProvider.idle(5); // in seconds
  	IdleProvider.timeout(120); // in seconds
  	
//	$urlRouterProvider.otherwise('/stock-info');
//  	$urlRouterProvider.deferIntercept();
//    $urlRouterProvider.otherwise('/other');

//    $locationProvider.html5Mode({enabled: false});
    
    $stateProviderRef = $stateProvider;
  	
  }).run(function ($rootScope, $urlRouter,$state, $uibModal, RestService,notify,contextRoot,restUrl) {
  	$rootScope.contextRoot=contextRoot;
  	$rootScope.restUrl=restUrl;
    $rootScope.skinClass = 'skin-blue';
    $rootScope.progressNum = 0;
    $rootScope.RestService = RestService(contextRoot);
    $rootScope.$state = $state;
    
    $rootScope.menus = [
     {
         title: '模型管理',
         url: 'form-models',
         templateUrl:'views/template/form-model.html',
         icon: 'fa fa-bar-chart-o'
     },
     {
    	 title: '定义管理',
       url: 'form-definition',
       templateUrl:'views/template/form-definition.html',
       icon: 'fa fa-bar-chart-o'
     }
     ]
    angular.forEach($rootScope.menus, function (menu) {
    	$stateProviderRef.state(menu.url, {
	   		url : '/'+menu.url,
	   		title:menu.title,
	      data: {pageTitle: [menu.title] },
	   		templateUrl: menu.templateUrl
	   	})
	   });
    $urlRouter.sync();
    $urlRouter.listen();

    $rootScope.showProgress = function (msg) {
      $rootScope.progressNum++;
      if (msg) {
        $rootScope.showSuccessMsg(msg);
      }
    };

    $rootScope.hideProgressBySucess = function (msg) {
      $rootScope.hideProgress(msg, 'notify-success');
    };

    $rootScope.hideProgressByError = function (msg) {
      $rootScope.hideProgress(msg, 'notify-error');
    };

    $rootScope.hideProgress = function (msg, classes) {
      $rootScope.progressNum--;
      if (msg) {
        if (classes && classes != null) {
          $rootScope.showMsg(msg, 2000, classes);
        } else {
          $rootScope.showErrorMsg(msg);
        }
      }
    };

    $rootScope.showSuccessMsg = function (msg) {
      $rootScope.showMsg(msg, 1112000, 'notify-success');
    };

    $rootScope.showErrorMsg = function (msg) {
      $rootScope.showMsg(msg, 115000, 'notify-error');
    };

    $rootScope.showMsg = function (msg, duration, classes) {
      notify({
        message: msg,
        duration: duration,
        position: 'right',
        classes: classes
      });
    };

    $rootScope.confirmModal = function (args) {
      $uibModal.open({
        templateUrl: 'views/common/confirm-modal.html',
        controller: function ($scope, $uibModalInstance) {
          $scope.modalTitle = angular.copy(args.title);
          $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
            args.confirm(false);
          };
          $scope.ok = function () {
            $uibModalInstance.close();
            args.confirm(true);
          };
        }
      });
    };

  });
  
})(angular);
