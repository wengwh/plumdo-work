/**
 * 自定义指令
 * 
 * @author wengwh
 * @date 2018-03-26
 */
(function () {
  'use strict';

  angular.module('adminApp').directive('viewLoad', function () {
    return {
      restrict: 'A',
			template: `
				<div class="folat" ng-show="progressNum>0">
					<div class="sk-spinner-wave">
        		<div class="sk-rect{{num}}" ng-repeat="num in [1,2,3,4,5]"></div>   
					</div>
				</div>
			 ` ,
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
      require: 'ngModel',
      scope: {
      	total: '=ngModel' ,
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
				scope[tableId] = scope[tableId] || {};
				scope[tableId].loadFunction = conf.loadFunction || function() {};
				scope[tableId].queryParams = conf.queryParams || {};
				scope[tableId].queryParams.sortName = conf.sortName || "";
				scope[tableId].queryParams.sortOrder = conf.sortOrder || "desc";

				var headThStr = '';
				var bodyThStr = '';
				for (var i in conf.colModels) {
					var sortHtml = '';
					if (conf.colModels[i].sortable) {
						sortHtml = 'ng-class="{\'sorting\':' + tableId + '.queryParams.sortName!=\'' + conf.colModels[i].index + '\',' 
						+ '\'sorting_asc\':' + tableId + '.queryParams.sortName==\'' + conf.colModels[i].index + '\'&&' + tableId + '.queryParams.sortOrder==\'asc\',' 
						+ '\'sorting_desc\':' + tableId + '.queryParams.sortName==\'' + conf.colModels[i].index + '\'&&' + tableId + '.queryParams.sortOrder==\'desc\'}" ' 
						+ 'ng-click="' + tableId + '.sortChange(\'' + conf.colModels[i].index + '\')"';
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

				var tableHtml = '<thead><tr>' 
					+ headThStr 
					+ '</tr></thead>' 
					+ '<tbody><tr ng-repeat="row in ' + conf.data + '.data ">' 
					+ bodyThStr 
					+ '</tr></tbody>';
				
				var pageHtml = '<div ng-pagination ng-model="'+ conf.data +'.total" changed="'+ tableId +'.loadFunction" param="' + tableId + '.queryParams"></div>';

				scope[tableId].sortChange = scope[tableId].sortChange || function(sortName) {
					if (scope[tableId].queryParams.sortName != sortName) {
						scope[tableId].queryParams.sortName = sortName;
						scope[tableId].queryParams.sortOrder = "desc";
					} else {
						if (scope[tableId].queryParams.sortOrder == "desc") {
							scope[tableId].queryParams.sortOrder = "asc";
						} else {
							scope[tableId].queryParams.sortOrder = "desc";
						}
					}
					scope[tableId].loadFunction();
				};

				element.html('').append($compile(tableHtml)(scope));
				element.after($compile(pageHtml)(scope));
			}
		};
	} ]).directive('ngIconpicker', ['$timeout',function ($timeout) {
    return {
      restrict: 'A',
      require: 'ngModel',
      scope: {
        data: '=ngModel'
      },
      link: function (scope, element) {
        var config = {
            arrowClass: 'btn-info',
            arrowPrevIconClass: 'glyphicon glyphicon-chevron-left',
            arrowNextIconClass: 'glyphicon glyphicon-chevron-right',
            cols: 10,
            footer: true,
            header: true,
            iconset: 'fontawesome',
            labelHeader: '第{0}页/共 {1} 页',
            labelFooter: '{0} - {1} 共 {2} 图标',
            placement: 'bottom',
            rows: 5,
            search: true,
            searchText: '搜索',
            selectedClass: 'btn-success',
            unselectedClass: ''
        };
        
        scope.$watch("data", function(newValue) {
        	if(newValue){
            element.iconpicker('setIcon', newValue);
        	}
        });
        
        element.on('change', function(e) {
        	$timeout(function() {
        		scope.data = e.icon;
          });
        });
        element.iconpicker(config);
      }
    };
  }]);

})();
