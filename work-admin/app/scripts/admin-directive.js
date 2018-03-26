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
  }).directive('ngPagination', [ function() {
		return {
			restrict : 'A',
			template: `
				<div class="row ng-pagination">
					<div class="col-xs-12 col-sm-5">
						<select class="form-control input-sm" ng-model="param.pageSize"
							ng-change="pageSizeChange()">
							<option ng-repeat="item in pageList" ng-value="item">{{item}} 条/页</option>
						</select>  共 {{total}} 条
					</div>
					<div class="col-xs-12 col-sm-7">
						<nav>
							<ul uib-pagination ng-change="pageNumChange()"
								total-items="total" items-per-page="param.pageSize" ng-model="param.pageNum"
								max-size="5" class="pagination-sm" boundary-links="true"
								previous-text="&lsaquo;" next-text="&rsaquo;" first-text="&laquo;"
								last-text="&raquo;">
							</ul>
						</nav>
					</div>
				</div>
			 ` ,
      scope: {
      	total: '=' ,
      	param: '=' ,
        changed: '=' 
      },
			controller : function($scope) {
				$scope.pageList= [5, 10, 20, 50, 100, 500];
				$scope.param = $scope.param || {};
				$scope.param.pageNum = $scope.param.pageNum || 1;
				$scope.param.pageSize = $scope.param.pageSize || 10;

				$scope.pageNumChange = function() {
					$scope.changed();
				};

				$scope.pageSizeChange = function() {
					$scope.param.pageNum = 1;
					$scope.changed();
				};

			}
		};
	} ]).directive('ngIcheck', ['$timeout',function($timeout) {
    return {
      require: 'ngModel',
      link: function($scope, element, $attrs, ngModel) {
      	return $timeout(function() {
          $scope.$watch($attrs['ngModel'], function (newValue) {
              $(element).iCheck('update');
          });

          return $(element).iCheck({
              checkboxClass: 'icheckbox_square-green',
              radioClass: 'iradio_square-green',
              increaseArea: '20%' 
          }).on('ifChanged', function (event) {
              if ($(element).attr('type') === 'checkbox' && $attrs['ngModel']) {
                  $scope.$apply(function () {
                      return ngModel.$setViewValue(event.target.checked);
                  });
              }
              if ($(element).attr('type') === 'radio' && $attrs['ngModel']) {
                  return $scope.$apply(function () {
                      return ngModel.$setViewValue($attrs['value']);
                  });
              }
          });
        });
      }
    };
	}]).directive('ngTable', [ '$compile', function($compile) {
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
	} ]);

})();
