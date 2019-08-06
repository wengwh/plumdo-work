/**
 * 页面总入口，定义依赖的第三方库，定义共有方法，变量
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp', [
    'ui.router',
    'ui.router.state.events',
    'ui.bootstrap',
    'cgNotify',
    'isteven-multi-select',
    'toggle-switch',
    'localytics.directives',
    'perfect_scrollbar'
  ]).run(function ($rootScope, notify, $state, $timeout, $uibModal, RestService, contextRoot, restUrl) {
    $rootScope.contextRoot = contextRoot;
    $rootScope.restUrl = restUrl;
    $rootScope.FlowService = RestService(contextRoot.flowService);
    $rootScope.FormService = RestService(contextRoot.formService);
    $rootScope.IdmService = RestService(contextRoot.identityService);
    $rootScope.loginUser = {};
    $rootScope.$state = $state;
    $rootScope.progressNum = 0;
    $rootScope.cacheParams = {};

    $rootScope.getCacheParams = function (key) {
      if (angular.isUndefined(key)) {
        return $rootScope.cacheParams;
      }
      if (angular.isUndefined($rootScope.cacheParams[key])) {
        $rootScope.cacheParams[key] = {};
      }
      return $rootScope.cacheParams[key];
    };

    $rootScope.clearCacheParams = function () {
      for (var key in $rootScope.cacheParams) {
        $rootScope.removeCacheParams(key);
      }
    };

    $rootScope.removeCacheParams = function (key) {
      if (angular.isDefined($rootScope.cacheParams[key])) {
        delete $rootScope.cacheParams[key];
      }
    };

    $rootScope.showProgress = function (msg) {
      $rootScope.progressNum++;
      if (msg) {
        $rootScope.showMsg(msg);
      }
    };

    $rootScope.hideProgress = function (msg, isFail) {
      $rootScope.progressNum--;
      if (msg) {
        if (isFail && isFail === true) {
          $rootScope.showErrorMsg(msg);
        } else {
          $rootScope.showMsg(msg);
        }
      }
    };

    $rootScope.showSuccessMsg = function (msg) {
      $rootScope.showMsg(msg, 1500, 'notify-success');
    };

    $rootScope.showErrorMsg = function (msg) {
      $rootScope.showMsg(msg, 3000, 'notify-error');
    };

    $rootScope.showMsg = function (msg, duration, classes) {
      notify({
        message: msg,
        duration: duration,
        position: 'center',
        classes: classes || 'notify-success'
      });
    };

    $rootScope.confirmModal = function (args) {
      $uibModal.open({
        templateUrl: 'views/common/confirm-modal.html',
        controller: function ($scope, $uibModalInstance) {
          $scope.modalTitle = angular.copy(args.title);
          $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
          };
          $scope.ok = function () {
            $uibModalInstance.close();
            args.confirm();
          };
        }
      });
    };

    $rootScope.editModal = function (args) {
      $uibModal.open({
        templateUrl: 'views/common/edit-modal.html',
        controller: 'EditModalController',
        scope: angular.extend($rootScope.$new(), args)
      });
    };

    $rootScope.editConfirmModal = function (args) {
      $uibModal.open({
        templateUrl: 'views/common/edit-modal.html',
        size: 'lg',
        controller: function ($scope, $uibModalInstance) {
          angular.extend($scope, args.property);

          $scope.modalTitle = angular.copy(args.title);
          $scope.formData = angular.copy(args.formData) || {};
          $scope.formUrl = angular.copy(args.formUrl);
          $scope.hideFooter = angular.copy(args.hideFooter);

          $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
          };
          $scope.ok = function () {
            args.confirm($scope.formData, $uibModalInstance);
          };

          if ($scope.fileOptions && !$scope.fileOptions.fileuploaded) {
            $scope.fileOptions.fileuploaded = function (query, result) {
              args.confirm(result.response, $uibModalInstance);
            }
          }
        }
      });
    };

    $rootScope.tableModal = function (args) {
      $uibModal.open({
        template: '<div class="modal-body"><table ng-table="tableOptions" class="table table-striped ng-table"></table></div>',
        controller: 'TableModalController',
        scope: angular.extend($rootScope.$new(), args)
      });
    };

    $rootScope.gotoDetail = function (id) {
      $state.go($state.current, {id: id});
    };

    $rootScope.gotoList = function (id) {
      $state.go($state.current, {id: id});
    };

    $rootScope.multiSelectLang = {
      selectAll: "全选",
      selectNone: "全部不选",
      reset: "重置",
      search: "搜索",
      nothingSelected: "没有选项被选中"
    };

    $rootScope.windowExportFile = function (data, fileName) {
      $rootScope.showProgress();
      $timeout(function () {
        fileName = decodeURI(fileName);
        var url = URL.createObjectURL(new Blob([data]));
        var a = document.createElement('a');
        document.body.appendChild(a);
        a.href = url;
        a.download = fileName;
        a.target = '_blank';
        a.click();
        a.remove();
        $rootScope.hideProgress();
      }, 1000);
    };


    $rootScope.guid = function () {
      var d = new Date().getTime();
      var uuid = 'xxxx-xxxx-4xxx-yxxx-xxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
      });
      return uuid;
    };


  }).filter('to_trusted', ['$sce', function ($sce) {
    return function (text) {
      return $sce.trustAsHtml(text);
    };
  }]).config(['$qProvider', function ($qProvider) {
    if ($qProvider.errorOnUnhandledRejections) {
      $qProvider.errorOnUnhandledRejections(false);
    }
  }]);

})();
