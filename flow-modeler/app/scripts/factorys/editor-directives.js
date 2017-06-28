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
angular.module('flowableModeler')
    .directive('loading', ['$translate', function ($translate) {
        return {
            restrict: 'E',
            template: '<div class=\'loading pull-right\' ng-show=\'status.loading\'><div class=\'l1\'></div><div class=\'l2\'></div><div class=\'l2\'></div></div>'
        };
    }]);

angular.module('flowableModeler')
    .directive('loadingLeftPull', ['$translate', function ($translate) {
        return {
            restrict: 'E',
            template: '<div class=\'loading pull-left\' ng-show=\'status.loading\'><div class=\'l1\'></div><div class=\'l2\'></div><div class=\'l2\'></div></div>'
        };
    }]);

// Workaround for https://github.com/twbs/bootstrap/issues/8379 :
// prototype.js interferes with regular dropdown behavior
angular.module('flowableModeler')
    .directive('activitiFixDropdownBug', function () {
        return {
            restrict: 'AEC',
            link: function (scope, element, attrs) {
                if (!element.hasClass('btn-group')) {
                    // Fix applied to button, use parent instead
                    element = element.parent();
                }
                element.on('hidden.bs.dropdown	', function () {
                    element.show(); // evil prototype.js has added display:none to it ...
                })
            }
        };
    });


