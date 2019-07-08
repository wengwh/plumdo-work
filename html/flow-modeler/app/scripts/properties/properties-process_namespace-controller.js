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

angular.module('flowableModeler').controller('FlowableProcessNamespaceDisplayCtrl',
  ['$scope', '$modal', '$http', 'editorManager', function ($scope, $modal, $http, editorManager) {

    if ($scope.property && $scope.property.value) {
      $http({
        method: 'GET',
        headers: {
          'Token': editorManager.getToken()
        },
        url: FLOWABLE.CONFIG.formContextRoot + '/form-definitions/' + $scope.property.value
      }).success(
        function (response) {
          $scope.form = response;
        });
    }

  }]);

angular.module('flowableModeler').controller('FlowableProcessNamespaceCtrl',
  ['$scope', '$modal', function ($scope, $modal) {

    // Config for the modal window
    var opts = {
      template: 'views/properties/process_namespace-popup.html',
      scope: $scope
    };

    // Open the dialog
    _internalCreateModal(opts, $modal, $scope);
  }]);

angular.module('flowableModeler').controller('FlowableProcessNamespacePopupCtrl',
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
        $scope.property.value = $scope.selectedForm.id;
      } else {
        $scope.property.value = null;
      }
      $scope.updatePropertyInModel($scope.property);
      $scope.close();
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
        url: FLOWABLE.CONFIG.formContextRoot + '/form-definitions/latest'
      }).success(
        function (response) {
          $scope.state.loadingForms = false;
          $scope.state.formError = false;
          $scope.forms = response;
          $scope.convertForm();
        })
        .error(
          function () {
            $scope.state.loadingForms = false;
            $scope.state.formError = true;
          });
    };

    $scope.convertForm = function () {
      if ($scope.property && $scope.property.value) {
        angular.forEach($scope.forms, function (item) {
          if (item.id == $scope.property.value) {
            $scope.selectedForm = item;
          }
        })
      }
    };

    $scope.loadForms();
  }]);