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

/*
 * Group
 */
'use strict';

angular.module('flowableModeler').controller('FlowableGroupCtrl', ['$scope', '$modal', function ($scope, $modal) {

  // Config for the modal window
  var opts = {
    template: 'views/properties/group-popup.html',
    scope: $scope
  };

  // Open the dialog
  _internalCreateModal(opts, $modal, $scope);
}]);

angular.module('flowableModeler').controller('FlowableGroupPopupCtrl',
  ['$rootScope', '$scope', '$translate', '$http', 'GroupService', function ($rootScope, $scope, $translate, $http, GroupService) {

    $scope.popup = {
      assignmentObject: {
        candidateGroups: []
      }
    };

    if ($scope.property.value !== undefined && $scope.property.value !== null) {
      GroupService.getFilteredGroups("").then(function (result) {
        var groupIds = $scope.property.value.toString().split(",");
        for (var i = 0; i < groupIds.length; i++) {
          for (var j = 0; j < result.length; j++) {
            if (result[j].id == groupIds[i]) {
              $scope.popup.assignmentObject.candidateGroups.push(result[j]);
              break;
            }
          }
        }
      });
    }

    $scope.$watch('popup.groupFilter', function () {
      $scope.updateGroupFilter();
    });

    $scope.updateGroupFilter = function () {
      if ($scope.popup.oldGroupFilter == undefined || $scope.popup.oldGroupFilter != $scope.popup.groupFilter) {
        if (!$scope.popup.groupFilter) {
          $scope.popup.oldGroupFilter = '';
        } else {
          $scope.popup.oldGroupFilter = $scope.popup.groupFilter;
        }

        if ($scope.popup.groupFilter !== null && $scope.popup.groupFilter !== undefined) {
          GroupService.getFilteredGroups($scope.popup.groupFilter).then(function (result) {
            var filterGroups = [];
            for (var i = 0; i < result.length; i++) {
              var filteredGroup = result[i];

              var foundCandidateGroup = false;
              if ($scope.popup.assignmentObject.candidateGroups !== null && $scope.popup.assignmentObject.candidateGroups !== undefined) {
                for (var j = 0; j < $scope.popup.assignmentObject.candidateGroups.length; j++) {
                  var candidateGroup = $scope.popup.assignmentObject.candidateGroups[j];
                  if (candidateGroup.id === filteredGroup.id) {
                    foundCandidateGroup = true;
                    break;
                  }
                }
              }

              if (!foundCandidateGroup) {
                filterGroups.push(filteredGroup);
              }

            }

            $scope.popup.groupResults = filterGroups;
            $scope.resetGroupSelection();
          });
        }
      }
    };

    $scope.confirmGroup = function (group) {
      if (!group) {
        // Selection is done with keyboard, use selection index
        var groups = $scope.popup.groupResults;
        if ($scope.popup.selectedGroupIndex >= 0 && $scope.popup.selectedGroupIndex < groups.length) {
          group = groups[$scope.popup.selectedGroupIndex];
        }
      }

      if (group) {
        // Only add if not yet part of candidate groups
        var found = false;
        if ($scope.popup.assignmentObject.candidateGroups) {
          for (var i = 0; i < $scope.popup.assignmentObject.candidateGroups.length; i++) {
            if ($scope.popup.assignmentObject.candidateGroups[i].id === group.id) {
              found = true;
              break;
            }
          }
        }

        if (!found) {
          $scope.addCandidateGroup(group);
        }
      }
    };

    $scope.addCandidateGroup = function (group) {
      $scope.popup.assignmentObject.candidateGroups.push(group);
    };

    $scope.removeCandidateGroup = function (group) {
      var groups = $scope.popup.assignmentObject.candidateGroups;
      var indexToRemove = -1;
      for (var i = 0; i < groups.length; i++) {
        if (group.id == groups[i].id) {
          indexToRemove = i;
          break;
        }
      }
      if (indexToRemove >= 0) {
        groups.splice(indexToRemove, 1);
      }
    };

    $scope.resetSelection = function () {
      if ($scope.popup.userResults && $scope.popup.userResults.length > 0) {
        $scope.popup.selectedIndex = 0;
      } else {
        $scope.popup.selectedIndex = -1;
      }
    };

    $scope.resetGroupSelection = function () {
      if ($scope.popup.groupResults && $scope.popup.groupResults.length > 0) {
        $scope.popup.selectedGroupIndex = 0;
      } else {
        $scope.popup.selectedGroupIndex = -1;
      }
    };

    $scope.nextGroup = function () {
      var groups = $scope.popup.groupResults;
      if (groups && groups.length > 0 && $scope.popup.selectedGroupIndex < groups.length - 1) {
        $scope.popup.selectedGroupIndex += 1;
      }
    };

    $scope.previousGroup = function () {
      var groups = $scope.popup.groupResults;
      if (groups && groups.length > 0 && $scope.popup.selectedGroupIndex > 0) {
        $scope.popup.selectedGroupIndex -= 1;
      }
    };

    $scope.save = function () {
      var groupIds = [];
      angular.forEach($scope.popup.assignmentObject.candidateGroups, function (item) {
        groupIds.push(item.id);
      });

      $scope.property.value = groupIds.join();
      $scope.updatePropertyInModel($scope.property);
      $scope.close();
    };


    // Close button handler
    $scope.close = function () {
      $scope.property.mode = 'read';
      $scope.$hide();
    };

  }]);
