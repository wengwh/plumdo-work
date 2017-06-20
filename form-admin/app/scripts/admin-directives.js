(function() {
	'use strict';
	angular.module('plumdo.directives').directive('pageTitle', [ '$rootScope', '$timeout', function($rootScope, $timeout) {
		return {
			restrict : 'A',
			link : function(scope, element) {
				var listener = function(event, toState, toParams, fromState, fromParams) {
					var title = toState.title?toState.title:'Plumdo';
					$timeout(function() {
						element.text(title);
					});
				};
				$rootScope.$on('$stateChangeSuccess', listener);
			}
		};
	} ]).directive('layoutWrapper', [ '$timeout', function($timeout) {
		return {
			restrict : 'A',
			controller : function($scope, $element) {
				var finishedViewNum = 0;
				$scope.$on('ngViewFinished', function(ngViewFinished) {
					if (finishedViewNum++ > 2) {
						$timeout(function() {
							$('body').layout();
						});
					}
				});
			},
			templateUrl : 'views/common/layout-wrapper.html'
		};
	} ]).directive('header', [ function() {
		return {
			restrict : 'EA',
			templateUrl : 'views/common/header.html',
			link : function(scope, element) {
				$('[data-toggle="push-menu"]').on('click', function() {
					$(this).pushMenu('toggle')
				});

				$('[data-toggle="control-sidebar"]').on('click', function() {
					$(this).controlSidebar('toggle')
				});

				scope.$emit('ngViewFinished');
			}
		};
	} ]).directive('contentHeader', [ '$rootScope', '$timeout', function($rootScope, $timeout) {
		return {
			restrict : 'A',
			scope:{},
			controller : function($scope, $element) {
				var listener = function(event, toState, toParams, fromState, fromParams) {
					$scope.title = toState.title
					$scope.description = toState.description
					$scope.breadcrumbs = ["首页"]
				};
				$rootScope.$on('$stateChangeSuccess', listener);
			},
			template : '<h1>{{title}}<small>{{description}}</small></h1><ol class="breadcrumb"><li ng-repeat="breadcrum in breadcrumbs">{{breadcrum}}</li><li class="active">{{title}}</li></ol>'
		};
	} ]).directive('footer', [ function() {
		return {
			restrict : 'EA',
			templateUrl : 'views/common/footer.html',
			link : function(scope, element) {
				scope.$emit('ngViewFinished');
			}
		};
	} ]).directive('mainSidebar', [ function() {
		return {
			restrict : 'EA',
			templateUrl : 'views/common/main-sidebar.html',
			link : function(scope, element) {
				$('[data-widget="tree"]').each(function() {
					$(this).tree()
				})
				scope.$emit('ngViewFinished');
			}
		};
	} ]).directive('controlSidebar', [ '$rootScope', function($rootScope) {
		return {
			restrict : 'EA',
			controller : function($scope, $element) {
				$scope.allSkins = [ {
					name : 'Blue',
					key : 'skin-blue',
					headerSiderClass : 'bg-blue',
					headerClass : 'bg-light-blue',
					contentSiderClass : 'bg-black',
					contentClass : 'bg-gray'
				}, {
					name : 'Black',
					key : 'skin-black',
					headerSiderClass : 'bg-gray-light',
					headerClass : 'bg-gray-light',
					contentSiderClass : 'bg-black',
					contentClass : 'bg-gray'
				}, {
					name : 'Purple',
					key : 'skin-purple',
					headerSiderClass : 'bg-purple-active',
					headerClass : 'bg-purple',
					contentSiderClass : 'bg-black',
					contentClass : 'bg-gray'
				}, {
					name : 'Green',
					key : 'skin-green',
					headerSiderClass : 'bg-green-active',
					headerClass : 'bg-green',
					contentSiderClass : 'bg-black',
					contentClass : 'bg-gray'
				}, {
					name : 'Red',
					key : 'skin-red',
					headerSiderClass : 'bg-red-active',
					headerClass : 'bg-red',
					contentSiderClass : 'bg-black',
					contentClass : 'bg-gray'
				}, {
					name : 'Yellow',
					key : 'skin-yellow',
					headerSiderClass : 'bg-yellow-active',
					headerClass : 'bg-yellow',
					contentSiderClass : 'bg-black',
					contentClass : 'bg-gray'
				}, {
					name : 'Blue Light',
					key : 'skin-blue-light',
					headerSiderClass : 'bg-blue',
					headerClass : 'bg-light-blue',
					contentSiderClass : 'bg-gray',
					contentClass : 'bg-gray'
				}, {
					name : 'Black Light',
					key : 'skin-black-light',
					headerSiderClass : 'bg-gray-light',
					headerClass : 'bg-gray-light',
					contentSiderClass : 'bg-gray',
					contentClass : 'bg-gray'
				}, {
					name : 'Purple Light',
					key : 'skin-purple-light',
					headerSiderClass : 'bg-purple-active',
					headerClass : 'bg-purple',
					contentSiderClass : 'bg-gray',
					contentClass : 'bg-gray'
				}, {
					name : 'Green Light',
					key : 'skin-green-light',
					headerSiderClass : 'bg-green-active',
					headerClass : 'bg-green',
					contentSiderClass : 'bg-gray',
					contentClass : 'bg-gray'
				}, {
					name : 'Red Light',
					key : 'skin-red-light',
					headerSiderClass : 'bg-red-active',
					headerClass : 'bg-red',
					contentSiderClass : 'bg-gray',
					contentClass : 'bg-gray'
				}, {
					name : 'Yellow Light',
					key : 'skin-yellow-light',
					headerSiderClass : 'bg-yellow-active',
					headerClass : 'bg-yellow',
					contentSiderClass : 'bg-gray',
					contentClass : 'bg-gray'
				} ];

				$scope.changeSkin = function(skin) {
					$rootScope.skinClass = skin;
				};

			},
			templateUrl : 'views/common/control-sidebar.html',
			link : function(scope, element) {
				var $controlSidebar = $('[data-toggle="control-sidebar"]').controlSidebar().data('lte.controlsidebar')

				// Add the layout manager
				$('[data-layout]').on('click', function() {
					$controlSidebar.changeLayout($(this).data('layout'))
				})

				$('[data-controlsidebar]').on('click', function() {
					$controlSidebar.hover($(this).data('controlsidebar'))
				})

				$('[data-sidebarskin="toggle"]').on('click', function() {
					$controlSidebar.changeSidebarSkin();
				})

				$('[data-enable="expandOnHover"]').on('click', function() {
					$controlSidebar.expandOnHover($(this))
				})

				// Reset options
				if ($('body').hasClass('fixed')) {
					$('[data-layout="fixed"]').attr('checked', 'checked')
				}
				if ($('body').hasClass('layout-boxed')) {
					$('[data-layout="layout-boxed"]').attr('checked', 'checked')
				}
				if ($('body').hasClass('sidebar-collapse')) {
					$('[data-layout="sidebar-collapse"]').attr('checked', 'checked')
				}
				scope.$emit('ngViewFinished');
			}
		};
	} ]).directive('boxTools', [ function() {
		return {
			restrict : 'A',
			scope : true,
			replace : true,
			templateUrl : 'views/common/ibox-tools.html',
			link : function(scope, element) {
				element.closest('div.box').boxWidget();
			}
		};
	} ]).directive('ngLoad', function() {
		return {
			restrict : 'A',
			scope : true,
			templateUrl : 'views/common/load.html',
			link : function(scope, element) {
				$("#float").fadeIn(300);
			}
		};
	}).directive('scrollUpBtn', function() {
		return {
			restrict : 'A',
			template : '<a href="" ng-click="scrollUp()" class="btn-scroll-up btn btn-primary btn-sm "><i class="fa fa-angle-double-up"></i></a>',
			controller : function($scope, $element) {
				var scroll_btn = $('.btn-scroll-up');
				if (scroll_btn.length > 0) {
					var is_visible = false;
					$(window).scroll(function() {
						if ($(window).scrollTop() > parseInt($(window).height() / 4)) {
							if (!is_visible) {
								scroll_btn.addClass('display');
								is_visible = true;
							}
						} else {
							if (is_visible) {
								scroll_btn.removeClass('display');
								is_visible = false;
							}
						}
					});

					$scope.scrollUp = function() {
						$('html,body').animate({
							scrollTop : 0
						}, "slow");
					};
				}
			}
		};
	}).directive('ngTable', [ '$compile', function($compile) {
		return {
			restrict : 'A',
			link : function(scope, element, attrs) {

				var conf = scope[attrs.ngTable];

				var tableId = conf.id;

				var loadFunction = conf.loadFunction || function() {
				};

				var pageList = conf.pageList || [ 5,10, 20, 50, 100 ];

				scope[tableId] = scope[tableId] || {};

				scope[tableId].queryParams = conf.queryParams || {};
				scope[tableId].pageNum = conf.pageNum ? conf.pageNum.toString() : "1";
				scope[tableId].pageSize = conf.pageSize ? conf.pageSize.toString() : "10";
				scope[tableId].sortName = conf.sortName || "";
				scope[tableId].sortOrder = conf.sortOrder || "desc";

				var headThStr = '';
				var bodyThStr = '';
				for ( var i in conf.colModels) {
					var sortHtml = '';
					if (conf.colModels[i].sortable) {
						sortHtml = 'ng-class="{\'sorting\':' + tableId + '.sortName!=\'' + conf.colModels[i].index + '\',' + '\'sorting_asc\':' + tableId + '.sortName==\'' + conf.colModels[i].index + '\'&&' + tableId + '.sortOrder==\'asc\',' + '\'sorting_desc\':' + tableId + '.sortName==\'' + conf.colModels[i].index + '\'&&' + tableId + '.sortOrder==\'desc\'}" ' + 'ng-click="' + tableId + '.sortChange(\'' + conf.colModels[i].index + '\')"';
					}
					var widthHtml = '';
					if (conf.colModels[i].width) {
						widthHtml = 'width=' + conf.colModels[i].width;
					}
					headThStr = headThStr + '<th ' + widthHtml + ' ' + sortHtml + ' >' + conf.colModels[i].name + '</th>\n';

					if (conf.colModels[i].formatter) {
						bodyThStr = bodyThStr + '<td>' + conf.colModels[i].formatter() + '</td>\n';
					} else {
						bodyThStr = bodyThStr + '<td>{{row.' + conf.colModels[i].index + '}}</td>\n';
					}

				}

				var tableHtml = '<thead><tr>' + headThStr + '</tr></thead>' + '<tbody><tr ng-repeat="row in ' + conf.data + '.data ">' + bodyThStr + '</tr></tbody>';

				var optionStr = '';
				for ( var i in pageList) {
					optionStr = optionStr + '<option value="' + pageList[i] + '">' + pageList[i] + '</option>\n';
				}

				var pageHtml = '<div class="row">' 
						+ '<div class="col-xs-5 col-sm-5"><select class="form-control input-sm ng-table" ng-model="' + tableId + '.pageSize" ng-change="' + tableId + '.pageSizeChange()">' + optionStr + '</select>条 ' 
						+ '{{(' + conf.data + '.startNum)}} - {{' + conf.data + '.endNum}}  共 {{' + conf.data + '.dataTotal}} 条</div>' 
						+ '<div class="col-xs-7 col-sm-7"><nav class="ng-table">' + '<ul  uib-pagination class="ng-table" ng-change="' + tableId + '.pageNumChange(' + conf.data + '.pageNum)"'
						+ 'total-items="' + conf.data + '.dataTotal" items-per-page="' + conf.data + '.pageSize" ng-model="' + conf.data + '.pageNum" max-size="5" '
						+ 'class="pagination-sm" boundary-links="true" previous-text="&lsaquo;" next-text="&rsaquo;" first-text="&laquo;" last-text="&raquo;"></ul>' + '</nav></div>' + '</div>';

				scope[tableId].pageNumChange = scope[tableId].pageNumChange || function(pageNum) {
					clickLoad(pageNum);
				};

				scope[tableId].pageSizeChange = scope[tableId].pageSizeChange || function() {
					clickLoad(1);
				};

				scope[tableId].sortChange = scope[tableId].sortChange || function(sortName) {
					if (scope[tableId].sortName != sortName) {
						scope[tableId].sortName = sortName;
						scope[tableId].sortOrder = "desc";
					} else {
						if (scope[tableId].sortOrder == "desc") {
							scope[tableId].sortOrder = "asc";
						} else {
							scope[tableId].sortOrder = "desc";
						}
					}
					clickLoad(1);
				};

				var clickLoad = function(pageNum, pageSize) {
					if (pageNum) {
						scope[tableId].queryParams.pageNum = pageNum;
					} else {
						scope[tableId].queryParams.pageNum = scope[tableId].pageNum;
					}

					if (pageSize) {
						scope[tableId].queryParams.pageSize = pageSize;
					} else {
						scope[tableId].queryParams.pageSize = scope[tableId].pageSize;
					}

					if (scope[tableId].sortName != "") {
						scope[tableId].queryParams.sortName = scope[tableId].sortName;
						scope[tableId].queryParams.sortOrder = scope[tableId].sortOrder;
					}

					loadFunction(scope[tableId].queryParams);
				};

				if (scope[tableId].queryParams.pageNum && scope[tableId].queryParams.pageSize) {
					loadFunction(scope[tableId].queryParams);
				} else {
					clickLoad(1);
				}

				element.html('').append($compile(tableHtml)(scope));
				element.after($compile(pageHtml)(scope));
			}
		};
	} ]).directive('ngDateTime', [ '$compile', function($compile) {
		return {
			restrict : 'A',
			link : function(scope, element, attrs) {
				var format = "yyyy-mm-dd hh:ii:ss";
				var minView = "hour";
				if (attrs.ngDateTime == "date") {
					format = "yyyy-mm-dd";
					minView = "month";
				}
				element.datetimepicker({
					format : format,
					minView : minView,
					autoclose : true,
					todayBtn : true,
					showSeconds : true
				});

			}
		};
	} ]);

}).call(this);