/**
 * 时间指令，由于第三方的时间使用默认按钮，input不会触发，自定义通过事件触发
 *
 * @author wengwh
 */
(function () {
  'use strict';

  angular.module('adminApp').directive('viewLoad', function () {
    return {
      restrict: 'A',
      templateUrl: 'views/common/view-load.html',
      link: function (scope, element) {
        $(element).fadeIn(300);
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

				var pageHtml = '<div class="row table-footer">' 
						+ '<div class="col-xs-5 col-sm-5">'
						+ '<select class="form-control input-sm ng-table" ng-model="' + tableId + '.pageSize" ng-change="' + tableId + '.pageSizeChange()">' + optionStr + '</select>条 ' 
						+ '{{(' + conf.data + '.startNum)}} - {{' + conf.data + '.endNum}}  共 {{' + conf.data + '.dataTotal}} 条</div>' 
						+ '<div class="col-xs-7 col-sm-7"><nav class="ng-table">' 
						+ '<ul  uib-pagination class="ng-table" ng-change="' + tableId + '.pageNumChange(' + conf.data + '.pageNum)"'
						+ 'total-items="' + conf.data + '.dataTotal" items-per-page="' + conf.data + '.pageSize" ng-model="' + conf.data + '.pageNum" max-size="5" '
						+ 'class="pagination-sm" boundary-links="true" previous-text="&lsaquo;" next-text="&rsaquo;" first-text="&laquo;" last-text="&raquo;"></ul>'
						+ '</nav></div>'
						+ '</div>';

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
	} ]).directive('ngPage', [ '$compile', function($compile) {
		return {
			restrict : 'A',
			require: 'ngModel',
      scope: {
        total: '=ngModel',
      	load: '=load'
      },
			link : function(scope, element, attrs) {

				console.info(scope.total)
				
				var conf = scope[attrs.ngPage]||{};
				var tableId = 'model';

				var pageList = conf.pageList || [ 5,10, 20, 50, 100 ];
				var loadFunction = conf.loadFunction || function() {
				};
				scope[tableId] = scope[tableId] || {};

				scope[tableId].queryParams = conf.queryParams || {};
				scope[tableId].pageNum = conf.pageNum ? conf.pageNum : 1;
				scope[tableId].pageSize = conf.pageSize ? conf.pageSize : 10;
				scope[tableId].total = 100;
				scope.total = scope.total ? scope.total : 0;
				scope[tableId].startNum = scope[tableId].pageNum*scope[tableId].pageSize;
				if(scope[tableId].startNum+scope[tableId].pageSize>scope[tableId].total){
					scope[tableId].endNum = scope[tableId].total;
				}else{
					scope[tableId].endNum = scope[tableId].startNum+scope[tableId].pageSize;
				}
				
				var optionStr = '';
				for (var i in pageList) {
					optionStr = optionStr + '<option value="' + pageList[i] + '">' + pageList[i] + '</option>\n';
				}
				
				var pageHtml = '<div class="row table-footer">' 
						+ '<div class="col-xs-5 col-sm-5">'
						+ '<select class="form-control input-sm ng-table" ng-model="' + tableId + '.pageSize" ng-change="' + tableId + '.pageSizeChange()">' + optionStr + '</select>条 ' 
						+ ' 共 ' + scope.total + ' 条</div>' 
						+ '<div class="col-xs-7 col-sm-7"><nav class="ng-table">' 
						+ '<ul  uib-pagination class="ng-table" ng-change="' + tableId + '.pageNumChange()"'
						+ 'total-items="' + tableId + '.total" items-per-page="' + tableId + '.pageSize" ng-model="' + tableId + '.pageNum" max-size="5" '
						+ 'class="pagination-sm" boundary-links="true" previous-text="&lsaquo;" next-text="&rsaquo;" first-text="&laquo;" last-text="&raquo;"></ul>'
						+ '</nav></div>'
						+ '</div>';

				scope[tableId].pageNumChange = scope[tableId].pageNumChange || function() {
					clickLoad();
				};

				scope[tableId].pageSizeChange = scope[tableId].pageSizeChange || function() {
					clickLoad(1);
				};

				var clickLoad = function(pageNum, pageSize) {
						scope[tableId].pageNum = scope[tableId].pageNum;
						scope[tableId].pageSize = scope[tableId].pageSize;
						console.info(scope[tableId].pageNum)
						scope[tableId].startNum = scope[tableId].pageNum*scope[tableId].pageSize;
						scope.load();
				};

				if (scope[tableId].queryParams.pageNum && scope[tableId].queryParams.pageSize) {
					loadFunction(scope[tableId].queryParams);
				} else {
					clickLoad(1);
				}

				element.html('').append($compile(pageHtml)(scope));
			}
		};
	} ]);

})();
