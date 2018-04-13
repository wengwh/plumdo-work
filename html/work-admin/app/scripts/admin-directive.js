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
      templateUrl: 'views/common/view-load.html',
      link: function (scope, element) {
        $(element).fadeIn(300);
      }
    };
  }).directive('ngPagination', [ function() {
    return {
      restrict : 'A',
      templateUrl: 'views/common/pagination.html',
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
        $scope.total = $scope.total || 9999999;

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
          $scope.$watch($attrs.ngModel, function () {
              $(element).iCheck('update');
          });

          return $(element).iCheck({
              checkboxClass: 'icheckbox_square-aero',
              radioClass: 'iradio_square-aero',
              increaseArea: '20%' 
          }).on('ifChanged', function (event) {
              if ($(element).attr('type') === 'checkbox' && $attrs.ngModel) {
                  $scope.$apply(function () {
                      return ngModel.$setViewValue(event.target.checked);
                  });
              }
              if ($(element).attr('type') === 'radio' && $attrs.ngModel) {
                  return $scope.$apply(function () {
                      return ngModel.$setViewValue($attrs.value);
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
        
        if(angular.isUndefined(scope[tableId].queryParams.sortName)){
          scope[tableId].queryParams.sortName = conf.sortName || "";
        }
        if(angular.isUndefined(scope[tableId].queryParams.sortOrder)){
          scope[tableId].queryParams.sortOrder = conf.sortOrder || "desc";
        }
        
        if(conf.isPage === false){
          scope[tableId].isPage = false;
        }else{
          scope[tableId].isPage = true;
        }

        scope[tableId].sortChange = scope[tableId].sortChange || function(sortName) {
          if (scope[tableId].queryParams.sortName !== sortName) {
            scope[tableId].queryParams.sortName = sortName;
            scope[tableId].queryParams.sortOrder = "desc";
          } else {
            if (scope[tableId].queryParams.sortOrder === "desc") {
              scope[tableId].queryParams.sortOrder = "asc";
            } else {
              scope[tableId].queryParams.sortOrder = "desc";
            }
          }
          scope[tableId].loadFunction();
        };
        
        var headThStr = '';
        var bodyThStr = '';
        for (var i in conf.colModels) {
          var sortHtml = '';
          if (conf.colModels[i].sortable) {
            sortHtml = 'ng-class="{\'sorting\':' + tableId + '.queryParams.sortName!=\'' + conf.colModels[i].index + '\',' +
             '\'sorting_asc\':' + tableId + '.queryParams.sortName==\'' + conf.colModels[i].index + '\'&&' + tableId + '.queryParams.sortOrder==\'asc\',' +
             '\'sorting_desc\':' + tableId + '.queryParams.sortName==\'' + conf.colModels[i].index + '\'&&' + tableId + '.queryParams.sortOrder==\'desc\'}" '+ 
             'ng-click="' + tableId + '.sortChange(\'' + conf.colModels[i].index + '\')"';
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

        var trHtml = null;
        if(scope[tableId].isPage){
          trHtml = '<tr ng-repeat="row in ' + conf.data + '.data ">';
        }else{
          trHtml = '<tr ng-repeat="row in ' + conf.data +'">';
        }
        
        var tableHtml = '<thead><tr>' + headThStr + '</tr></thead><tbody>'+ trHtml+ bodyThStr + '</tr></tbody>';
        
        element.html('').append($compile(tableHtml)(scope));
        
        if(scope[tableId].isPage){
          var pageHtml = '<div ng-pagination ng-model="'+ conf.data +'.total" changed="'+ tableId +'.loadFunction" param="' + tableId + '.queryParams"></div>';
          element.after($compile(pageHtml)(scope));
        }
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
  }]).directive('colorbox', function () {
    return {
      restrict: 'A',
      link: function (scope, element) {
        var colorbox_params = {
          rel: 'colorbox',
          reposition:true,
          scalePhotos:true,
          scrolling:false,
          current:'{current} of {total}',
          maxWidth:'100%',
          maxHeight:'100%'
        };
        $(element).colorbox(colorbox_params);
      }
    };
  }).directive('fileInput', function () {
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        element.fileinput({
          language: 'zh',
          uploadUrl: '#'
        });
        
        scope.$watch(attrs.ngDisabled, function (ngDisabled) {
          if (ngDisabled) {
            element.fileinput('disable');
          } else {
            element.fileinput('enable');
          }
        });
       
        scope.$watch(attrs.fileInput, function (fileInput) {
          if (angular.isDefined(fileInput)) {
            fileInput = angular.copy(fileInput);
            if(angular.isDefined(fileInput.allowedFileTypes)){
              if (fileInput.allowedFileTypes.indexOf("all") >= 0 || fileInput.allowedFileTypes.length === 0) {
                fileInput.allowedFileTypes = null;
              }
            }
            if(angular.isDefined(fileInput.allowedFileExtensions)){
              if (fileInput.allowedFileExtensions.indexOf("all") >= 0 || fileInput.allowedFileExtensions.length === 0) {
                fileInput.allowedFileExtensions = null;
              }
            }
            if(angular.isDefined(fileInput.fileuploaded)){
              element.on('fileuploaded', fileInput.fileuploaded);
            }
            element.fileinput('refresh', fileInput);
          }
        }, true);
      }
    };
  });

})();
