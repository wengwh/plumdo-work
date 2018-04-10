/**
 * 页面总入口，定义依赖的第三方库，定义共有方法，变量
 * 
 * @author wengwh
 */
(function() {
	'use strict';

	angular.module(
		'adminApp',['ui.router', 'ui.router.state.events', 'ui.bootstrap', 'cgNotify', 'isteven-multi-select','toggle-switch','perfect_scrollbar' ])
		.run(function($rootScope, notify, $state, $timeout, $uibModal, RestService, contextRoot, restUrl) {
			$rootScope.contextRoot = contextRoot;
			$rootScope.restUrl = restUrl;
			$rootScope.FlowService = RestService(contextRoot.flowService);
			$rootScope.FormService = RestService(contextRoot.formService);
			$rootScope.IdmService = RestService(contextRoot.identityService);
			$rootScope.$state = $state;
			$rootScope.progressNum = 0;

			$rootScope.showProgress = function(msg) {
				$rootScope.progressNum++;
				if (msg) {
					$rootScope.showMsg(msg);
				}
			};

			$rootScope.hideProgress = function(msg, isFail) {
				$rootScope.progressNum--;
				if (msg) {
					if (isFail && isFail === true) {
						$rootScope.showErrorMsg(msg);
					} else {
						$rootScope.showMsg(msg);
					}
				}
			};

			$rootScope.showSuccessMsg = function(msg) {
				$rootScope.showMsg(msg, 1500, 'notify-success');
			};

			$rootScope.showErrorMsg = function(msg) {
				$rootScope.showMsg(msg, 3000, 'notify-error');
			};

			$rootScope.showMsg = function(msg, duration, classes) {
				notify({
					message : msg,
					duration : duration,
					position : 'center',
					classes : classes || 'notify-success'
				});
			};

			$rootScope.confirmModal = function(args) {
				$uibModal.open({
					templateUrl : 'views/common/confirm-modal.html',
					controller : function($scope, $uibModalInstance) {
						$scope.modalTitle = angular.copy(args.title);
						$scope.cancel = function() {
							$uibModalInstance.dismiss('cancel');
							args.confirm(false);
						};
						$scope.ok = function() {
							$uibModalInstance.close();
							args.confirm(true);
						};
					}
				});
			};
			
			$rootScope.editModal = function(args) {
				$uibModal.open({
					templateUrl : 'views/common/edit-modal.html',
					controller : 'EditModalController',
					resolve : args
				});
			};
			
			$rootScope.editConfirmModal = function (args) {
				$uibModal.open({
	        templateUrl: 'views/common/edit-modal.html',
	        controller: function ($scope, $uibModalInstance) {
	          $scope.modalTitle = angular.copy(args.title);
	          $scope.formData = angular.copy(args.formData) || {};
	          $scope.formUrl = angular.copy(args.formUrl);
	          $scope.cancel = function () {
	    				$uibModalInstance.dismiss('cancel');
	          };
	          $scope.ok = function () {
	            args.confirm($scope.formData,$uibModalInstance);
	          };
	        }
	      });
	    };
	    
			$rootScope.tableModal = function(args) {
				$uibModal.open({
					template : '<div class="modal-body"><table ng-table="tableOptions" class="table table-striped ng-table"></table></div>',
					controller : 'TableModalController',
					resolve : args
				});
			};
			
			$rootScope.multiSelectLang = {
			    selectAll       : "全选",
			    selectNone      : "全部不选",
			    reset           : "重置",
			    search          : "搜索",
			    nothingSelected : "没有选项被选中" 
			};
			
			$rootScope.windowExportFile = function(data,fileName){
	    	$rootScope.showProgress();
				// 加入定时跳出angular本身的检查
	    	$timeout(function() {
					var fileName = decodeURI(fileName);
					var url = URL.createObjectURL(new Blob([ data ]));
					var a = document.createElement('a');
					document.body.appendChild(a); // 此处增加了将创建的添加到body当中
					a.href = url;
					a.download = fileName;
					a.target = '_blank';
					a.click();
					a.remove(); // 将a标签移除
					$rootScope.hideProgress();
				}, 1000);
	    };

	}).filter('to_trusted', [ '$sce', function($sce) {
			return function(text) {
				return $sce.trustAsHtml(text);
			};
	}]).config(['$qProvider', function ($qProvider) {
		  if($qProvider.errorOnUnhandledRejections){
		    $qProvider.errorOnUnhandledRejections(false);
		  }
	}]);

})();
