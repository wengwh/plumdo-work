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
 * User
 */
'use strict';

angular.module('flowableModeler').controller('FlowableUserCtrl', ['$scope', '$modal', function ($scope, $modal) {

  // Config for the modal window
  var opts = {
    template: 'views/properties/user-popup.html',
    scope: $scope
  };

  // Open the dialog
  _internalCreateModal(opts, $modal, $scope);
}]);

angular.module('flowableModeler').controller('FlowableUserPopupCtrl',
  ['$rootScope', '$scope', '$translate', '$http', 'UserService', function ($rootScope, $scope, $translate, $http, UserService) {

    $scope.popup = {
      assignmentObject: {
        candidateUsers: []
      }
    };

    // Put json representing assignment on scope
    if ($scope.property.value !== undefined && $scope.property.value !== null) {
      UserService.getFilteredUsers("").then(function (result) {
        var userIds = $scope.property.value.toString().split(",");
        for (var i = 0; i < userIds.length; i++) {
          for (var j = 0; j < result.length; j++) {
            if (result[j].id == userIds[i]) {
              $scope.popup.assignmentObject.candidateUsers.push(result[j]);
              break;
            }
          }
        }
      });
    }

    $scope.$watch('popup.filter', function () {
      $scope.updateFilter();
    });

    $scope.updateFilter = function () {
      if ($scope.popup.oldFilter == undefined || $scope.popup.oldFilter != $scope.popup.filter) {
        if (!$scope.popup.filter) {
          $scope.popup.oldFilter = '';
        } else {
          $scope.popup.oldFilter = $scope.popup.filter;
        }

        if ($scope.popup.filter !== null && $scope.popup.filter !== undefined) {
          UserService.getFilteredUsers($scope.popup.filter).then(function (result) {
            var filteredUsers = [];
            for (var i = 0; i < result.length; i++) {
              var filteredUser = result[i];

              var foundCandidateUser = false;
              if ($scope.popup.assignmentObject.candidateUsers !== null && $scope.popup.assignmentObject.candidateUsers !== undefined) {
                for (var j = 0; j < $scope.popup.assignmentObject.candidateUsers.length; j++) {
                  var candidateUser = $scope.popup.assignmentObject.candidateUsers[j];
                  if (candidateUser.id === filteredUser.id) {
                    foundCandidateUser = true;
                    break;
                  }
                }
              }

              if (!foundCandidateUser) {
                filteredUsers.push(filteredUser);
              }

            }

            $scope.popup.userResults = filteredUsers;
            $scope.resetSelection();
          });
        }
      }
    };


    $scope.confirmUser = function (user) {
      if (!user) {
        // Selection is done with keyboard, use selection index
        var users = $scope.popup.userResults;
        if ($scope.popup.selectedIndex >= 0 && $scope.popup.selectedIndex < users.length) {
          user = users[$scope.popup.selectedIndex];
        }
      }

      if (user) {
        // Only add if not yet part of candidate users
        var found = false;
        if ($scope.popup.assignmentObject.candidateUsers) {
          for (var i = 0; i < $scope.popup.assignmentObject.candidateUsers.length; i++) {
            if ($scope.popup.assignmentObject.candidateUsers[i].id === user.id) {
              found = true;
              break;
            }
          }
        }

        if (!found) {
          $scope.addCandidateUser(user);
        }
      }
    };

    $scope.addCandidateUser = function (user) {
      $scope.popup.assignmentObject.candidateUsers.push(user);
    };

    $scope.removeCandidateUser = function (user) {
      var users = $scope.popup.assignmentObject.candidateUsers;
      var indexToRemove = -1;
      for (var i = 0; i < users.length; i++) {
        if (user.id) {
          if (user.id === users[i].id) {
            indexToRemove = i;
            break;
          }
        }
      }
      if (indexToRemove >= 0) {
        users.splice(indexToRemove, 1);
      }
    };

    $scope.resetSelection = function () {
      if ($scope.popup.userResults && $scope.popup.userResults.length > 0) {
        $scope.popup.selectedIndex = 0;
      } else {
        $scope.popup.selectedIndex = -1;
      }
    };

    $scope.nextUser = function () {
      var users = $scope.popup.userResults;
      if (users && users.length > 0 && $scope.popup.selectedIndex < users.length - 1) {
        $scope.popup.selectedIndex += 1;
      }
    };

    $scope.previousUser = function () {
      var users = $scope.popup.userResults;
      if (users && users.length > 0 && $scope.popup.selectedIndex > 0) {
        $scope.popup.selectedIndex -= 1;
      }
    };

    $scope.save = function () {
      var userIds = [];
      angular.forEach($scope.popup.assignmentObject.candidateUsers, function (item) {
        userIds.push(item.id);
      });

      $scope.property.value = userIds.join();
      $scope.updatePropertyInModel($scope.property);
      $scope.close();
    };

    // Close button handler
    $scope.close = function () {
      $scope.property.mode = 'read';
      $scope.$hide();
    };

  }]);
