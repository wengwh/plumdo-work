/**
 * 页面总入口，定义依赖的第三方库，定义共有方法，变量
 * 
 * @author wengwh
 */
(function() {
	'use strict';

	angular.module(
		'adminApp',[ 'ui.router', 'ui.router.state.events', 'ui.bootstrap', 'cgNotify', 'perfect_scrollbar' ])
		.run(function($rootScope, notify, $state, $timeout, $uibModal, RestService, contextRoot, restUrl) {
			$rootScope.contextRoot = contextRoot;
			$rootScope.restUrl = restUrl;
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
					position : 'right',
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
			

			$rootScope.menuItems=[
	      { path: 'home', title: '系统首页',  icon: 'ti-panel'},
	      { path: 'work', title: '我的工作台',  icon:'ti-bell',
	        children:[
	          { path: 'work.process-start', title: '发起新流程',  icon: 'ti-user'},
	          { path: 'work.process-run', title: '运行的流程',  icon: 'ti-microsoft-alt'},
	          { path: 'work.process-finish', title: '办结的流程',  icon: 'ti-menu'},
	          { path: 'work.process-involve', title: '参与的流程',  icon: 'ti-ruler-pencil'},
	          { path: 'work.task-todo', title: '待办任务',  icon: 'ti-pencil-alt2'},
	          { path: 'work.task-finish', title: '已办任务',  icon: 'ti-map'},
	        ]
	      },
	      { path: 'modeler', title: '设计器管理',  icon:'ti-pencil-alt2', 
	        children:[
	          { path: 'modeler.form', title: '表单设计器',  icon: 'ti-layout-grid4'},
	          { path: 'modeler.button', title: '按钮设计器',  icon: 'ti-credit-card'},
	          { path: 'modeler.flow', title: '流程设计器',  icon: 'ti-layout-media-overlay-alt-2'},
	          { path: 'modeler.report', title: '报表设计器',  icon: 'ti-layout-cta-left'},
	          { path: 'modeler.app', title: '模块设计器',  icon: 'ti-view-list-alt'}
	        ]
	      },
	      { path: 'flow', title: '流程后台管理',  icon:'ti-blackboard', 
	        children:[
	          { path: 'flow.definition', title: '流程定义管理',  icon: 'ti-package'},
	          { path: 'flow.instance', title: '流程实例管理',  icon: 'ti-menu'},
	          { path: 'flow.task', title: '流程任务管理',  icon: 'ti-server'},
	          { path: 'flow.form', title: '表单定义管理',  icon: 'ti-text'}
	        ]
	      },
	      { path: 'idm', title: '人员权限管理',  icon:'ti-agenda',
	        children:[
	          { path: 'idm.user', title: '人员管理',  icon: 'ti-user'},
	          { path: 'idm.group', title: '部门管理',  icon: 'ti-microsoft-alt'},
	          { path: 'idm.menu', title: '菜单管理',  icon: 'ti-menu'},
	          { path: 'idm.role', title: '角色管理',  icon: 'ti-ruler-pencil'}
	        ]
	      }
	    ]
			
			$rootScope.menuTitle = null;

			$rootScope.$on('$stateChangeSuccess', function(toState, toParams) {
				var statePath = toParams.name;
				for (var index in $rootScope.menuItems) {
					if ($rootScope.menuItems[index].path === statePath) {
						$rootScope.menuTitle = $rootScope.menuItems[index].title;
						break;
					}
				}
			});

		}).filter('to_trusted', [ '$sce', function($sce) {
		return function(text) {
			return $sce.trustAsHtml(text);
		}
	} ]);
	;

})();
