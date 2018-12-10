(function () {
  'use strict';

  var __indexOf = [].indexOf || function (item) {
    for (var i = 0, l = this.length; i < l; i++) {
      if (i in this && this[i] == item)
        return i;
    }
    return -1;
  };

  var guid = function () {
    var d = new Date().getTime();
    var uuid = 'xxxx-xxxx-4xxx-yxxx-xxxx'.replace(/[xy]/g, function (c) {
      var r = (d + Math.random() * 16) % 16 | 0;
      d = Math.floor(d / 16);
      return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
    return uuid;
  };

  angular.module('builder.provider', []).provider('$builder', function () {
    var $http, $injector, $templateCache;
    $injector = null;
    $http = null;
    $templateCache = null;

    this.forms = {};
    this.groups = [];
    this.components = {};
    this.propertyPackages = {};

    this.convertProperty = function (prefix, property) {
      var result, _ref, _ref0, _ref1, _ref2;
      result = {
        key: (_ref = property.key) != null ? _ref : '',
        title: (_ref0 = property.title) != null ? _ref0 : '',
        value: (_ref1 = property.value) != null ? _ref1 : '',
        description: (_ref2 = property.description) != null ? _ref2 : '',
        template: property.template,
        templateUrl: property.templateUrl
      };
      if (prefix) {
        result.name = prefix + "." + result.key;
      } else {
        result.name = result.key;
      }
      if (!result.template && !result.templateUrl) {
        console.error('The template is empty.');
      }
      return result;
    };

    this.convertPropertyPackage = function (propertyPackage) {
      var result, _ref0, _ref1, _ref2;
      result = {
        id: (_ref0 = propertyPackage.id) != null ? _ref0 : '',
        object: (_ref1 = propertyPackage.object) != null ? _ref1 : null,
        properties: (_ref2 = propertyPackage.properties) != null ? _ref2 : []
      };
      return result;
    };

    this.registerPropertyPackage = (function (_this) {
      return function (propertyPackage) {
        propertyPackage = propertyPackage || {};
        var name = propertyPackage.id;
        if (_this.propertyPackages[name] == null) {
          var newPropertyPackage = _this.convertPropertyPackage(propertyPackage);
          angular.forEach(newPropertyPackage.properties, function (property, index) {
            var newProperty = _this.convertProperty(newPropertyPackage.object, property);
            if ($injector != null) {
              _this.loadTemplate(newProperty);
            }
            newPropertyPackage.properties[index] = newProperty;
          });
          _this.propertyPackages[name] = newPropertyPackage;
        } else {
          console.error('The propertyPackage ' + name + ' was registered.');
        }
      };
    })(this);

    this.loadPropertyPackages = (function (_this) {
      return function (component) {
        var propertyPackages = [];
        angular.forEach(component.propertyPackages, function (packageId, index) {
          if (_this.propertyPackages[packageId] != null) {
            var propertyPackage = _this.propertyPackages[packageId];
            if (__indexOf.call(propertyPackages, propertyPackage) < 0) {
              propertyPackages.push(propertyPackage);
              var properties = null;
              if (propertyPackage.object) {
                component.properties[propertyPackage.object] = {};
                properties = component.properties[propertyPackage.object];
              } else {
                properties = component.properties;
              }
              angular.forEach(propertyPackage.properties, function (property, childIndex) {
                properties[property.key] = property.value;
              });
            } else {
              console.error('The propertyPackage ' + packageId + ' was registered.');
            }

          } else {
            console.error('The propertyPackage ' + packageId + ' is not found.');
          }
        });
        component.propertyPackages = propertyPackages;
      };
    })(this);

    this.convertComponent = function (component) {
      var result, _ref, _ref0, _ref1, _ref2, _ref3, _ref4, _ref5, _ref6, _ref7;
      result = {
        id: (_ref0 = component.id) != null ? _ref0 : '',
        value: (_ref1 = component.value) != null ? _ref1 : null,
        arrayValue: (_ref2 = component.arrayValue) != null ? _ref2 : [],
        type: (_ref3 = component.type) != null ? _ref3 : '',
        forms: (_ref4 = component.forms) != null ? _ref4 : [],
        title: (_ref5 = component.title) != null ? _ref5 : '',
        propertyPackages: (_ref6 = component.propertyPackages) != null ? _ref6 : [],
        properties: (_ref7 = component.properties) != null ? _ref7 : {},
        template: component.template,
        templateUrl: component.templateUrl
      };
      if (!result.template && !result.templateUrl) {
        console.error('The template is empty.');
      }
      return result;
    };

    this.registerComponent = (function (_this) {
      return function (component) {
        component = component || {};
        var name = component.id;
        if (_this.components[name] == null) {
          var newComponent = _this.convertComponent(component);
          _this.components[name] = newComponent;

          if (_this.propertyPackages != null) {
            _this.loadPropertyPackages(newComponent);
          }

          if ($injector != null) {
            _this.loadTemplate(newComponent);
          }
        } else {
          console.error('The component ' + name + ' was registered.');
        }
      };
    })(this);

    this.loadComponents = (function (_this) {
      return function (group) {
        var components = [];
        angular.forEach(group.components, function (componentId) {
          if (_this.components[componentId] != null) {
            if (__indexOf.call(components, _this.components[componentId]) < 0) {
              components.push(_this.components[componentId]);
            } else {
              console.error('The component ' + componentId + ' was registered.');
            }
          } else {
            console.error('The component ' + componentId + ' is not found.');
          }
        });
        group.components = components;
      };
    })(this);

    this.convertGroup = function (group) {
      var result, _ref0, _ref1, _ref2, _ref3;
      result = {
        id: (_ref0 = group.id) != null ? _ref0 : '',
        title: (_ref1 = group.title) != null ? _ref1 : '',
        description: (_ref2 = group.description) != null ? _ref2 : '',
        components: (_ref3 = group.components) != null ? _ref3 : []
      };
      return result;
    };

    this.registerGroup = (function (_this) {
      return function (group) {
        group = group || {};
        var newGroup = _this.convertGroup(group);
        _this.loadComponents(newGroup);
        if (__indexOf.call(_this.groups, newGroup) < 0) {
          _this.groups.push(newGroup);
        } else {
          console.error('The group ' + group.id + ' was registered.');
        }
      };
    })(this);

    this.setupProviders = function (injector) {
      $injector = injector;
      $http = $injector.get('$http');
      return $templateCache = $injector.get('$templateCache');
    };

    this.loadTemplate = function (component) {
      if (component.template == null) {
        $http.get(component.templateUrl, {
          cache: $templateCache
        }).success(function (template) {
          return component.template = template;
        });
      }
    };

    this.setField = function (component, field) {
      component.properties.field = field.key;
      component.properties.overrideId = field.key;
      component.properties.label = field.name;
      component.properties.description = field.remark;
    };

    this.$get = ['$injector', (function (_this) {
      return function ($injector) {
        _this.setupProviders($injector);
        return {
          forms: _this.forms,
          groups: _this.groups,
          components: _this.components,
          propertyPackages: _this.propertyPackages,
          registerGroup: _this.registerGroup,
          registerComponent: _this.registerComponent,
          registerPropertyPackage: _this.registerPropertyPackage,
          setField: _this.setField
        };
      };
    })(this)];
  });

}).call(this);
