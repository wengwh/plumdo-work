(function () {
  'use strict';

  var compileTemplate = function ($compile, element, templatName, scope) {
    return scope.$watch(templatName, function (template) {
      if (!template) {
        return;
      }
      var view = $compile(template)(scope);
      return $(element).html(view);
    });
  };

  angular.module('builder.directive', []).directive('fbForm', [function () {
    return {
      restrict: 'A',
      require: 'ngModel',
      scope: {
        data: '=ngModel',
        forms: '=fbForm'
      },
      template: '<div ng-repeat="component in forms.components"><div fb-form-object="component"></div></div>'
    };
  }]).directive('fbFormObject', ['$compile', function ($compile) {
    return {
      restrict: 'A',
      scope: {
        component: '=fbFormObject'
      },
      link: function (scope, element, attrs, ngModel) {
        scope.data = scope.$parent.data;
        scope.showForm = true;
        var watchArrayValue = ['checkbox', 'select', 'multiple-select'];

        var overrideId = scope.component.properties.overrideId;
        if (overrideId) {
          if (watchArrayValue.indexOf(scope.component.id) < 0) {
            scope.$watch('data[\'' + overrideId + '\']', function (newValue, oldValue) {
              if (angular.isUndefined(newValue)) {
                return;
              }
              if (scope.component.value == newValue) {
                return;
              }
              scope.component.value = newValue;
            });

            scope.$watch('component.value', function (newValue, oldValue) {
              if (scope.data[overrideId] == newValue) {
                return;
              }
              scope.data[overrideId] = newValue;
            });
          } else {
            scope.changeDataFlag = false;
            scope.changeValueFlag = false;

            scope.$watch('data[\'' + overrideId + '\']', function (newValue, oldValue) {
              if (angular.isUndefined(newValue)) {
                return;
              }
              if (scope.changeDataFlag) {
                scope.changeDataFlag = false;
                return;
              }
              scope.changeValueFlag = true;

              var checkedArrayValue = newValue.split(",");
              if (scope.component.id === 'checkbox') {
                angular.forEach(scope.component.properties.options, function (option, index) {
                  scope.component.arrayValue[index] = false;
                  angular.forEach(checkedArrayValue, function (checkedValue) {
                    if (checkedValue == option.value) {
                      scope.component.arrayValue[index] = true;
                    }
                  });
                });
              } else if (scope.component.id === 'select') {
                angular.forEach(checkedArrayValue, function (checkedValue, index) {
                  console.info(scope.component.arrayValue[index])
                  angular.forEach(scope.component.arrayValue[index].children, function (option) {
                    if (checkedValue == option.value) {
                      scope.component.arrayValue[index + 1] = option;
                    }
                  });
                });
              } else if (scope.component.id === 'multiple-select') {
                scope.component.arrayValue = [];
                angular.forEach(checkedArrayValue, function (checkedValue, index) {
                  angular.forEach(scope.component.properties.options, function (option) {
                    if (checkedValue == option.value) {
                      scope.component.arrayValue[index] = option;
                    }
                  });
                });
              }
            });

            scope.$watch('component.arrayValue', function (newValue, oldValue) {
              if (scope.changeValueFlag) {
                scope.changeValueFlag = false;
                return;
              }
              scope.changeDataFlag = true;

              var checkedArrayValue = [];
              if (scope.component.id === 'checkbox') {
                angular.forEach(newValue, function (checked, index) {
                  if (checked) {
                    checkedArrayValue.push(scope.component.properties.options[index].value);
                  }
                });
              } else if (scope.component.id === 'select') {
                angular.forEach(newValue, function (checked, index) {
                  if (index > 0 && index <= scope.component.properties.level) {
                    if (checked) {
                      checkedArrayValue.push(checked.value);
                    }
                  }
                });

              } else if (scope.component.id === 'multiple-select') {
                angular.forEach(newValue, function (checked) {
                  checkedArrayValue.push(checked.value);
                });
              }

              scope.data[overrideId] = checkedArrayValue.join(',');
            }, true);
          }

        }

        return compileTemplate($compile, element, 'component.template', scope);
      }
    };
  }]).directive('fbBuilder', [function () {
    return {
      restrict: 'A',
      scope: {
        forms: '=fbBuilder'
      },
      templateUrl: 'views/directive/fb-builder.html',
      controller: 'fbBuilderController',
      link: function (scope, element) {
        if (scope.forms.id === 'root') {
          element.children(':first').addClass('fb-builder');
          angular.element(window).resize();
        }
      }

    };
  }]).directive('fbBuilderObject', ['$compile', function ($compile) {
    return {
      restrict: 'A',
      scope: {
        component: '=fbBuilderObject'
      },
      link: function (scope, element) {
        return compileTemplate($compile, element, 'component.template', scope);
      }
    };
  }]).directive('fbComponents', function () {
    return {
      restrict: 'A',
      scope: {
        fbComponents: '='
      },
      templateUrl: 'views/directive/fb-components.html',
      controller: 'fbComponentsController'
    };
  }).directive('fbComponent', ['$compile', function ($compile) {
    return {
      restrict: 'A',
      scope: {
        component: '=fbComponent'
      },
      controller: 'fbComponentController',
      link: function (scope, element) {
        return compileTemplate($compile, element, 'component.template', scope);
      }
    };
  }]).directive('fbProperties', function () {
    return {
      restrict: 'A',
      scope: {
        fbProperties: '='
      },
      templateUrl: 'views/directive/fb-properties.html',
      controller: 'fbPropertiesController'
    };
  }).directive('fbProperty', ['$compile', function ($compile) {
    return {
      restrict: 'A',
      scope: true,
      link: function (scope, element, attrs) {
        scope.property = scope.$eval(attrs.fbProperty);
        return compileTemplate($compile, element, 'property.template', scope);
      }
    };
  }]).directive('fbDynamicModel', ['$compile', function ($compile) {
    return {
      restrict: 'A',
      priority: 1,
      terminal: true,
      link: function (scope, element, attrs) {
        var fbDynamicModel = scope.$eval(attrs.fbDynamicModel);
        if (attrs.ngModel === fbDynamicModel || !fbDynamicModel) {
          return;
        }
        if (attrs.fbDynamicModelPre) {
          if (fbDynamicModel.indexOf(".") >= 0) {
            fbDynamicModel = attrs.fbDynamicModelPre + '.' + fbDynamicModel;
          } else {
            fbDynamicModel = attrs.fbDynamicModelPre + '[\'' + fbDynamicModel + '\']';
          }
        }
        if (attrs.fbDynamicModelSuf) {
          if (fbDynamicModel.indexOf(".") >= 0) {
            fbDynamicModel = fbDynamicModel + '.' + attrs.fbDynamicModelSuf;
          } else {
            fbDynamicModel = fbDynamicModel + '[\'' + attrs.fbDynamicModelSuf + '\']';
          }
        }

        element.attr('ng-model', fbDynamicModel);
        if (fbDynamicModel === '') {
          element.removeAttr('ng-model');
        }
        element.removeAttr('fb-dynamic-model');
        element.unbind();
        $compile(element)(scope);
      }
    };
  }]).directive('fbPopover', [function () {
    return {
      restrict: 'A',
      scope: {
        config: '=fbPopover'
      },
      link: function (scope, element) {
        var config = scope.config || {};
        config.trigger = config.trigger || 'hover';
        config.html = config.html || true;
        config.title = config.title || '';
        config.content = config.content || '';
        config.placement = config.placement || 'auto';
        element.popover(config);
      }
    };
  }]).directive('fbDatetime', function () {
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        var config = scope.$eval(attrs.fbDatetime) || {};
        config.autoclose = config.autoclose || true;
        config.todayBtn = config.todayBtn || true;
        config.showSeconds = config.showSeconds || true;
        config.language = config.language || 'zh-CN', element.datetimepicker(config);
      }
    };
  }).directive('fbHtml', ['$compile', '$timeout', function ($compile, $timeout) {
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        if (attrs.summernote) {
          return;
        }

        scope.fbHtmlConfig = scope.$eval(attrs.fbHtml) || {};
        scope.fbHtmlConfig.lang = scope.fbHtmlConfig.lang || 'zh-CN';
        
        scope.$watch(attrs.ngDisabled, function (ngDisabled,old) {
          $timeout(function(){
            if (ngDisabled) {
              element.summernote('disable');
            } else {
              element.summernote('enable');
            }
          });
        });

        element.removeAttr('fb-html');
        element.attr('summernote', '');
        element.attr('config', 'fbHtmlConfig');
      
        $compile(element)(scope);
      }
    };
  }]).directive('fbFileInput', function () {
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

        scope.$watch(attrs.fbFileInput, function (fbFileInput) {
          if (angular.isDefined(fbFileInput)) {
            fbFileInput = angular.copy(fbFileInput);
            if (fbFileInput.allowedFileTypes.indexOf("all") >= 0 || fbFileInput.allowedFileTypes.length === 0) {
              fbFileInput.allowedFileTypes = null;
            }
            if (fbFileInput.allowedFileExtensions.indexOf("all") >= 0 || fbFileInput.allowedFileExtensions.length === 0) {
              fbFileInput.allowedFileExtensions = null;
            }
            element.fileinput('refresh', fbFileInput);
          }
        }, true);
      }
    };
  }).directive('fbLoad', function() {
		return {
			restrict : 'A',
      templateUrl: 'views/directive/fb-load.html',
			link : function(scope, element) {
				$(element).fadeIn(300);
			}
		};
	});

}).call(this);
