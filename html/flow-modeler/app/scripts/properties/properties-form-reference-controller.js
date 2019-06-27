/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

angular.module('flowableModeler').controller('FlowableFormReferenceDisplayCtrl',
  ['$scope', '$modal', '$http', 'editorManager', function ($scope, $modal, $http, editorManager) {

    if ($scope.property && $scope.property.value && $scope.property.value.id) {
       $http({
        method: 'GET',
        headers: {
          'Token': editorManager.getToken()
        },
        url: FLOWABLE.CONFIG.formContextRoot + '/form-definitions/' + $scope.property.value.id
      }).success(
          function (response) {
            $scope.form = {
              id: response.id,
              name: response.name
            };
          });
    }

  }]);

angular.module('flowableModeler').controller('FlowableFormReferenceCtrl',
  ['$scope', '$modal', '$http', function ($scope, $modal, $http) {

    // Config for the modal window
    var opts = {
      template: 'views/properties/form-reference-popup.html',
      scope: $scope
    };

    // Open the dialog
    _internalCreateModal(opts, $modal, $scope);
  }]);

angular.module('flowableModeler').controller('FlowableFormReferencePopupCtrl',
  ['$rootScope', '$scope', '$http', '$location', 'editorManager', function ($rootScope, $scope, $http, $location, editorManager) {

    $scope.state = {'loadingForms': true, 'formError': false};

    $scope.popup = {'state': 'formReference'};

    $scope.foldersBreadCrumbs = [];

    // Close button handler
    $scope.close = function () {
      $scope.property.mode = 'read';
      $scope.$hide();
    };

    // Selecting/deselecting a subprocess
    $scope.selectForm = function (form, $event) {
      $event.stopPropagation();
      if ($scope.selectedForm && $scope.selectedForm.id && form.id == $scope.selectedForm.id) {
        // un-select the current selection
        $scope.selectedForm = null;
      } else {
        $scope.selectedForm = form;
      }
    };

    // Saving the selected value
    $scope.save = function () {
      if ($scope.selectedForm) {
        $scope.property.value = {
          'id': $scope.selectedForm.id,
          'name': $scope.selectedForm.name,
          'key': $scope.selectedForm.key
        };

      } else {
        $scope.property.value = null;
      }
      $scope.updatePropertyInModel($scope.property);
      $scope.close();
    };

    // Open the selected value
    $scope.open = function () {
      if ($scope.selectedForm) {
        $scope.property.value = {
          'id': $scope.selectedForm.id,
          'name': $scope.selectedForm.name,
          'key': $scope.selectedForm.key
        };

        $scope.updatePropertyInModel($scope.property);

        var modelMetaData = editorManager.getBaseModelData();
        var json = editorManager.getModel();
        json = JSON.stringify(json);

        var params = {
          modeltype: modelMetaData.model.modelType,
          json_xml: json,
          name: modelMetaData.name,
          key: modelMetaData.key,
          description: modelMetaData.description,
          newversion: false,
          lastUpdated: modelMetaData.lastUpdated
        };

        // Update
        $http({
          method: 'POST',
          data: params,
          ignoreErrors: true,
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
          },
          transformRequest: function (obj) {
            var str = [];
            for (var p in obj) {
              str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
            return str.join("&");
          },
          url: FLOWABLE.URL.putModel(modelMetaData.modelId)
        })

          .success(function (data, status, headers, config) {
            editorManager.handleEvents({
              type: ORYX.CONFIG.EVENT_SAVED
            });

            var allSteps = EDITOR.UTIL.collectSortedElementsFromPrecedingElements($scope.selectedShape);

            $rootScope.addHistoryItem($scope.selectedShape.resourceId);
            $location.path('form-editor/' + $scope.selectedForm.id);

          })
          .error(function (data, status, headers, config) {

          });

        $scope.close();
      }
    };

    $scope.newForm = function () {
    };

    $scope.createForm = function () {
    };

    $scope.cancel = function () {
      $scope.close();
    };

    $scope.loadForms = function () {
      $http({
        method: 'GET',
        headers: {
          'Token': editorManager.getToken()
        },
        url: FLOWABLE.CONFIG.formContextRoot + '/form-definitions'
      }).success(
        function (response) {
          $scope.state.loadingForms = false;
          $scope.state.formError = false;
          $scope.forms = response.data;
        })
        .error(
          function () {
            $scope.state.loadingForms = false;
            $scope.state.formError = true;
          });
    };

    if ($scope.property && $scope.property.value && $scope.property.value.id) {
      $scope.selectedForm = $scope.property.value;
    }

    $scope.loadForms();
  }]);