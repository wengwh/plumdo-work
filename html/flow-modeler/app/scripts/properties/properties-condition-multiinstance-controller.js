/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Condition expression
 */

angular.module('flowableModeler').controller('FlowableConditionMultiinstanceCtrl', ['$scope', '$modal', function ($scope, $modal) {

  var opts = {
    template: 'views/properties/condition-multiinstance-popup.html',
    scope: $scope
  };

  // Open the dialog
  _internalCreateModal(opts, $modal, $scope);
}]);

angular.module('flowableModeler').controller('FlowableConditionMultiinstancePopupCtrl', ['$scope', '$translate', '$http', function ($scope, $translate, $http) {

  // Put json representing condition on scope
  if ($scope.property.value !== undefined && $scope.property.value !== null) {

    $scope.conditionExpression = {value: $scope.property.value};

  } else {
    $scope.conditionExpression = {value: ''};
  }
  $scope.changeExpression = function () {
    if ($scope.conditionExpression.radio == 'all') {
      $scope.conditionExpression.value = '${nrOfCompletedInstances/nrOfInstances>=1}'
    } else if ($scope.conditionExpression.radio == 'half') {
      $scope.conditionExpression.value = '${nrOfCompletedInstances/nrOfInstances>=0.5}'
    } else if ($scope.conditionExpression.radio == 'custom') {
    }
  };
  $scope.save = function () {
    $scope.property.value = $scope.conditionExpression.value;
    $scope.updatePropertyInModel($scope.property);
    $scope.close();
  };

  // Close button handler
  $scope.close = function () {
    $scope.property.mode = 'read';
    $scope.$hide();
  };
}]);