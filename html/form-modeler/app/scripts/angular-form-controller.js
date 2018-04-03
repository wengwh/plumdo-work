(function() {
  'use strict';

  angular.module('builder.controller', []).controller('fbWorkController', [ '$scope', '$injector', '$http', '$stateParams', 'restUrl', function($scope, $injector, $http, $stateParams, restUrl) {
    var $builder = $injector.get('$builder');
    $builder.forms.id = 'root';
    $builder.forms.components = [];
    $scope.formData = {};

    var getJsonurl = null;

    if ($stateParams.formDefinitionId != null) {
      getJsonurl = restUrl.getDefinitionJsonById($stateParams.formDefinitionId);
    } else if ($stateParams.formDefinitionKey != null) {
      getJsonurl = restUrl.getDefinitionJsonByKey($stateParams.formDefinitionKey);
    } else {
      $scope.showErrorMsg('参数:formDefinitionId,formDefinitionKey必须填入一个');
    }

    $http({
      method : 'GET',
      url : getJsonurl
    }).success(function(data) {
      $builder.forms.components = data;
      $scope.previewForms = angular.copy($builder.forms);
    }).error(function(data) {
      console.info(data);
      $scope.showErrorMsg('获取表单模型失败');
    });

    $scope.convertToFormDataArray = function() {
      var formDataArray = [];
      angular.forEach($scope.formData, function(value, key) {
        formDataArray.push({
          'key' : key,
          'value' : value
        });
      });
      return formDataArray
    };

    $scope.convertToFormDataObject = function(formDataArray) {
      angular.forEach(formDataArray, function(data, index) {
        $scope.formData[data.key] = data.value;
      });
    };

    $scope.createFormData = function() {
      var postData = {
        'businessKey' : 1,
        'formDatas' : $scope.convertToFormDataArray()
      };
      $http({
        method : 'POST',
        url : restUrl.createInstance(),
        data : postData
      }).success(function(data) {

      }).error(function(data) {
        console.info(data);
        $scope.showErrorMsg('获取表单设计内容失败');
      });
    };

    $scope.updateFormData = function(formInstanceId) {
      var postData = {
        'businessKey' : 1,
        'formDatas' : $scope.convertToFormDataArray()
      };
      $http({
        method : 'PUT',
        url : restUrl.updateInstance(formInstanceId),
        data : postData
      }).success(function(data) {

      }).error(function(data) {
        console.info(data);
        $scope.showErrorMsg('获取表单设计内容失败');
      });
    };

    if ($stateParams.formInstanceId != null) {
      $http({
        method : 'GET',
        url : restUrl.getInstance($stateParams.formInstanceId),
      }).success(function(data) {
        $scope.convertToFormDataObject(data.formDatas);
      }).error(function(data) {
        console.info(data);
        $scope.showErrorMsg('获取表单实例失败');
      });

      $scope.saveFormData = function() {
        $scope.updateFormData($stateParams.formInstanceId);
      };
    } else {
      $scope.saveFormData = function() {
        $scope.createFormData();
      };
    }

  } ]).controller('fbWatchController', [ '$scope', '$injector', '$http', '$stateParams', 'restUrl', function($scope, $injector, $http, $stateParams, restUrl) {
    var $builder = $injector.get('$builder');
    $builder.forms.id = 'root';
    $builder.forms.components = [];

    $http({
      method : 'GET',
      url : restUrl.getModelJson($stateParams.modelId)
    }).success(function(data) {
      $builder.forms.components = data;
      $scope.previewForms = angular.copy($builder.forms);
    }).error(function(data) {
      $scope.showErrorMsg('获取表单模型失败');
    });

    
  } ]).controller('fbDesignController', [ '$scope', '$injector', '$http', '$stateParams', 'restUrl', function($scope, $injector, $http, $stateParams, restUrl) {
    var $builder = $injector.get('$builder');
    $builder.forms.id = 'root';
    $builder.forms.components = [];
    $builder.forms.selectedComponent = {};
    
    $http({
      method : 'GET',
      url : restUrl.getModelJson($stateParams.modelId)
    }).success(function(data) {
      $builder.forms.components = data;
    }).error(function(data) {
      console.info(data);
      $scope.showErrorMsg('获取表单数据失败');
    });

    $scope.leftCollapsed = false;
    $scope.rightCollapsed = false;
    $scope.navActive = "editForm";
    $scope.editForms = $builder.forms;

    $scope.editForm = function() {
      $scope.navActive = "editForm";
    };

    $scope.previewForm = function() {
      $scope.navActive = "previewForm";
      $scope.previewForms = angular.copy($builder.forms);
      $scope.formData = {};
    };
    
    $scope.saveForm = function() {
      $scope.showProgress();
      $http({
        method : 'PUT',
        url : restUrl.saveModelJson($stateParams.modelId),
        data : $builder.forms.components
      }).success(function(data) {
        $scope.hideProgressBySucess('保存表单成功');
      }).error(function(data) {
        console.info(data)
        $scope.hideProgressByError('保存表单失败');
      });
    };

    $scope.clearForm = function() {
      $builder.forms.components = [];
      $builder.forms.selectedComponent = {};
      $scope.editForm();
    };

    $scope.fixSize = function() {
      jQuery('.sidebar-nav').css('height', $(window).height() - $('#navbar').height() - 1);
      jQuery('.nav-body').css('height', $(window).height() - $('#navbar').height() - $('.nav-title').height() - 21);
      jQuery('.fb-builder').css('height', $(window).height() - $('#navbar').height() - 20);
    };

    angular.element(window).bind('resize', function() {
      $scope.fixSize();
    });

    $http({
      method : 'GET',
      url : restUrl.getStencilSet()
    }).success(function(data) {
      $scope.mainTitle = data.title;
      $scope.mainDescription = data.description;

      angular.forEach(data.propertyPackages, function(propertyPackage) {
        $builder.registerPropertyPackage(propertyPackage);
      });

      angular.forEach(data.components, function(component) {
        $builder.registerComponent(component);
      });

      angular.forEach(data.groups, function(group) {
        $builder.registerGroup(group);
      });

    });

  } ]).controller('fbBuilderController', [ '$scope', '$injector', '$timeout', function($scope, $injector, $timeout) {
    var $builder = $injector.get('$builder');
    $scope.builderForms = $builder.forms;

    $scope.selectComponent = function(component, $event) {
      // 阻止事件冒泡
      $event.stopPropagation();
      if (component != $scope.builderForms.selectedComponent) {
        // 先删除触发watch，否则会导致复制的对象，无法触发watch
        $scope.builderForms.selectedComponent = {};
        $timeout(function() {
          $scope.builderForms.selectedComponent = component;
        });
      }
    };

    $scope.removeComponent = function(parentItem, index) {
      var component = parentItem.components.splice(index, 1);
      if ($scope.builderForms.selectedComponent === component[0]) {
        $scope.builderForms.selectedComponent = {};
      }
    };

    $scope.copyComponent = function(parentItem, item, $event) {
      $event.stopPropagation();
      var index = parentItem.components.indexOf(item);
      if (index > -1) {
        parentItem.components.splice(index + 1, 0, angular.copy(item));
      }
    };

    $scope.sortableOptions = {
      connectWith : '.column',
      handle : '.drag'
    };

  } ]).controller('fbComponentsController', [ '$scope', '$injector', function($scope, $injector) {
    var $builder = $injector.get('$builder');
    $scope.groups = $builder.groups;

    $scope.draggableOptions = {
      connectWith : '.column',
      helper : 'clone',
      handle : '.drag',
      start : function(e, t) {
        t.helper.width(400);
        t.helper.addClass('fb-builder');
      },
      stop : function(e, ui) {
        if (jQuery(e.target).hasClass('nav-content') && ui.item.sortable.droptarget && e.target != ui.item.sortable.droptarget[0]) {
          // clone model to components
          ui.item.sortable.sourceModel.splice(ui.item.sortable.index, 0, ui.item.sortable.model);
          // component to container
          ui.item.sortable.droptargetModel.splice(ui.item.sortable.dropindex, 1, angular.copy(ui.item.sortable.model));
        }
      }
    };

  } ]).controller('fbComponentController', [ '$scope', function($scope) {
    $scope.changeColNum = function() {
      var numArray = $scope.component.value.split(" ", 12);
      var sum = 0;
      angular.forEach(numArray, function(num) {
        sum = sum + parseInt(num);
      });
      if (sum == 12) {
        $scope.component.showButton = true;
        $scope.component.arrayValue = numArray;
        $scope.component.forms = [];
        angular.forEach($scope.component.arrayValue, function(component, index) {
          $scope.component.forms[index] = {};
          $scope.component.forms[index].components = [];
        });
      } else {
        $scope.component.showButton = false;
        console.info(' input value sum not 12');
      }
    };

    if ($scope.component.type == 'column' && $scope.component.value) {
      $scope.changeColNum();
    }

  } ]).controller('fbPropertiesController', [ '$scope', '$injector', function($scope, $injector) {
    var $builder = $injector.get('$builder');
    $scope.builderForms = $builder.forms;

    $scope.$watch('builderForms.selectedComponent', function() {
      $scope.component = $scope.builderForms.selectedComponent;
      console.info($scope.component);
    });

    $scope.summernoteConfig = {
      height : 250,
      toolbar : [ [ 'style', [ 'style', 'fontsize', 'bold', 'italic', 'underline', 'clear' ] ], [ 'para', [ 'color', 'paragraph', 'undo', 'redo', 'codeview', 'fullscreen' ] ] ]
    };

  } ]).controller('fbOptionsController', [ '$scope', function($scope) {

    if ($scope.component.properties.treeOptions) {
      $scope.options = $scope.component.properties.treeOptions;
    } else {
      $scope.options = $scope.component.properties.options;
    }

    $scope.sortableOptions = {
      handle : '.glyphicon-move'
    };

    $scope.showValue = false;

    $scope.changeShow = function() {
      $scope.showValue = !$scope.showValue;
    };

    $scope.selectedOption = {};

    $scope.selectOption = function(item) {
      $scope.selectedOption = angular.copy(item);
    };

    $scope.changeOption = function(item) {
      if ($scope.selectedOption.text === $scope.selectedOption.value || $scope.selectedOption.value === '') {
        item.value = item.text;
      }
      $scope.selectOption(item);
    };

    $scope.addOption = function(index) {
      $scope.options.splice(index + 1, 0, {
        value : '',
        text : ''
      });
    };

    $scope.removeOption = function(index) {
      $scope.options.splice(index, 1);
    };

    $scope.addTreeOption = function(scope) {
      var nodeData = scope.$modelValue;
      nodeData.children.push({
        text : '',
        value : '',
        children : []
      });
    };

    $scope.removeTreeOption = function(scope) {
      scope.remove();
    };
    $scope.toggle = function(scope) {
      scope.toggle();
    };

    $scope.collapseAll = function() {
      $scope.$broadcast('angular-ui-tree:collapse-all');
    };

    $scope.expandAll = function() {
      $scope.$broadcast('angular-ui-tree:expand-all');
    };

    $scope.selectDefault = function($event, item) {
      var checkbox = $event.target;
      if (checkbox.checked) {
        $scope.component.arrayValue.push(item);
      } else {
        $scope.component.arrayValue.splice($scope.component.arrayValue.indexOf(item), 1);
      }
    };

  } ]);

}).call(this);
