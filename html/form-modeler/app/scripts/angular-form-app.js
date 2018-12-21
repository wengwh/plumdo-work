(function () {
  'use strict';

  angular.module('builder', [
    'ui.router',
    'validator',
    'summernote',
    'ui.sortable',
    'ui.tree',
    'cgNotify',
    'localytics.directives',
    'builder.constant',
    'builder.config',
    'builder.provider',
    'builder.service',
    'builder.controller',
    'builder.directive'
  ]).run(['$rootScope', 'notify', 'FormRestService', '$injector', function ($rootScope, notify, FormRestService, $injector) {

    $rootScope.progressNum = 0;

    $rootScope.showProgress = function (msg) {
      $rootScope.progressNum++;
      if (msg) {
        $rootScope.showSuccessMsg(msg);
      }
    };

    $rootScope.hideProgressBySuccess = function (msg) {
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
        classes: classes
      });
    };


    const $builder = $injector.get('$builder');
    $builder.forms.id = 'root';
    $builder.forms.selectedComponent = {};
    $builder.forms.fields = {};

    FormRestService.getStencilSet().success(function (data) {
      $rootScope.mainTitle = data.title;
      $rootScope.mainDescription = data.description;

      angular.forEach(data.propertyPackages, function (propertyPackage) {
        $builder.registerPropertyPackage(propertyPackage);
      });

      angular.forEach(data.components, function (component) {
        $builder.registerComponent(component);
      });

      angular.forEach(data.groups, function (group) {
        $builder.registerGroup(group);
      });
    });

    $rootScope.filterSaveComponents = function (formJson) {
      var components = [];
      angular.forEach(formJson, function (component) {
        const copyComponent = {};
        copyComponent.id = component.id;
        copyComponent.arrayValue = component.arrayValue;
        copyComponent.properties = component.properties;
        copyComponent.value = component.value;

        if (component.forms.length > 0) {
          copyComponent.forms = [];
          angular.forEach(component.forms, function (forms, index) {
            copyComponent.forms[index] = {};
            copyComponent.forms[index].components = $rootScope.filterSaveComponents(forms.components);
          });
        }
        components.push(copyComponent);
      });
      return components;
    };

    $rootScope.filterGetComponents = function (formJson) {
      let components = [];
      angular.forEach(formJson, function (component) {
        let selectedComponent = $builder.components[component.id];
        if (angular.isUndefined(selectedComponent)) {
          return;
        }

        let copyComponent = angular.copy(selectedComponent);
        copyComponent.arrayValue = component.arrayValue;
        copyComponent.properties = component.properties;
        copyComponent.value = component.value;

        if (angular.isDefined(selectedComponent.properties.field)) {
          let field = component.properties.field;
          if (angular.isUndefined($builder.forms.fields[field])) {
            return;
          } else {
            $builder.setField(copyComponent, $builder.forms.fields[field]);
          }
        }

        if (component.forms && component.forms.length > 0) {
          angular.forEach(component.forms, function (forms, index) {
            copyComponent.forms[index] = {};
            copyComponent.forms[index].components = $rootScope.filterGetComponents(forms.components);
          });
        }
        components.push(copyComponent);
      });

      return components;
    };


  }]).filter('to_trusted', ['$sce', function ($sce) {
    return function (text) {
      return $sce.trustAsHtml(text);
    }
  }]);

}).call(this);
