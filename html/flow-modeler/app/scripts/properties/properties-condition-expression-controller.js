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
 * Condition expression
 */

angular.module('flowableModeler').controller('FlowableConditionExpressionCtrl', [ '$scope', '$modal', function($scope, $modal) {

    // Config for the modal window
    var opts = {
        template: 'views/properties/condition-expression-popup.html',
        scope: $scope
    };

    // Open the dialog
    _internalCreateModal(opts, $modal, $scope);
}]);

angular.module('flowableModeler').controller('FlowableConditionExpressionPopupCtrl',
    [ '$rootScope', '$scope', '$translate', function($rootScope, $scope, $translate) {


      // Put json representing condition on scope
      if ($scope.property.value !== undefined && $scope.property.value !== null) {

        $scope.conditionExpression = {value: $scope.property.value};

      } else {
        $scope.conditionExpression = {value: ''};
      }

      $scope.conditionExpression.type1 = 'variable';
      $scope.conditionExpression.type2 = 'static';
      $scope.conditionExpression.param1 = '';
      $scope.conditionExpression.param2 = '';
      $scope.conditionExpression.expression = 'equals';
      $scope.conditionExpression.link = 'empty';

      $scope.reset = function() {
        $scope.conditionExpression.type1 = 'variable';
        $scope.conditionExpression.type2 = 'static';
        $scope.conditionExpression.param1 = '';
        $scope.conditionExpression.param2 = '';
        $scope.conditionExpression.expression = 'equals';
        $scope.conditionExpression.link = 'empty';
      };


      $scope.add = function() {
        var expressionValue = null;
        var param1 = null;
        var param2 = null;
        var link = null;
        if($scope.conditionExpression.type1 == 'variable'){
          param1 = $scope.conditionExpression.param1;
        }else{
          param1 = "'" + $scope.conditionExpression.param1 + "'";
        }

        if($scope.conditionExpression.type2 == 'variable'){
          param2 = $scope.conditionExpression.param2;
        }else{
          param2 = "'" + $scope.conditionExpression.param2 + "'";
        }

        if($scope.conditionExpression.link == 'and'){
          link = "&&";
        }else if($scope.conditionExpression.link == 'or'){
          link = "||";
        }else{
          link = "";
        }

        if($scope.conditionExpression.expression == 'equals'){
          expressionValue = param1 + " == " + param2;
        }else if($scope.conditionExpression.expression == 'notEquals'){
          expressionValue = param1 + " != " + param2;
        }else if($scope.conditionExpression.expression == 'less'){
          expressionValue = param1 + " < " + param2;
        }else if($scope.conditionExpression.expression == 'greater'){
          expressionValue = param1 + " > " + param2;
        }else if($scope.conditionExpression.expression == 'lessEquals'){
          expressionValue = param1 + " <= " + param2;
        }else if($scope.conditionExpression.expression == 'greaterEquals'){
          expressionValue = param1 + " >= " + param2;
        }else if($scope.conditionExpression.expression == 'contains'){
          expressionValue = "uel.contains("+param1+","+param2+")";
        }else if($scope.conditionExpression.expression == 'notContains'){
          expressionValue = "uel.notContains("+param1+","+param2+")";
        }else if($scope.conditionExpression.expression == 'startsWith'){
          expressionValue = "uel.startsWith("+param1+","+param2+")";
        }else if($scope.conditionExpression.expression == 'endsWith'){
          expressionValue = "uel.endsWith("+param1+","+param2+")";
        }

        $scope.conditionExpression.value = $scope.conditionExpression.value.replace(/(\s*$)/g,"")

        if($scope.conditionExpression.value == ''){
          $scope.conditionExpression.value = "${"+expressionValue+"}";
        }else if($scope.conditionExpression.value.indexOf("}")==$scope.conditionExpression.value.length-1){
          $scope.conditionExpression.value = $scope.conditionExpression.value.substr(0,$scope.conditionExpression.value.length-1)
            + " " + link + " "+ expressionValue+"}";
        }else{
          $scope.conditionExpression.value = $scope.conditionExpression.value + " " + link + " "+ expressionValue;
        }
      };

      $scope.save = function() {
        $scope.property.value = $scope.conditionExpression.value;
        $scope.updatePropertyInModel($scope.property);
        $scope.close();
      };

      // Close button handler
      $scope.close = function() {
        $scope.property.mode = 'read';
        $scope.$hide();
      };
}]);