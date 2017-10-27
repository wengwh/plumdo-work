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
'use strict';

var FLOWABLE = FLOWABLE || {};
FLOWABLE.PROPERTY_CONFIG =
{
    "string": {
        "readModeTemplateUrl": "views/properties/default-value-display-template.html",
        "writeModeTemplateUrl": "views/properties/string-property-write-mode-template.html"
    },
    "boolean": {
        "templateUrl": "views/properties/boolean-property-template.html"
    },
    "text" : {
        "readModeTemplateUrl": "views/properties/default-value-display-template.html",
        "writeModeTemplateUrl": "views/properties/text-property-write-template.html"
    },
    "flowable-multiinstance" : {
        "readModeTemplateUrl": "views/properties/default-value-display-template.html",
        "writeModeTemplateUrl": "views/properties/multiinstance-property-write-template.html"
    },
    "flowable-ordering" : {
        "readModeTemplateUrl": "views/properties/default-value-display-template.html",
        "writeModeTemplateUrl": "views/properties/ordering-property-write-template.html"
    },
    "oryx-formproperties-complex": {
        "readModeTemplateUrl": "views/properties/form-properties-display-template.html",
        "writeModeTemplateUrl": "views/properties/form-properties-write-template.html"
    },
    "oryx-executionlisteners-multiplecomplex": {
        "readModeTemplateUrl": "views/properties/execution-listeners-display-template.html",
        "writeModeTemplateUrl": "views/properties/execution-listeners-write-template.html"
    },
    "oryx-tasklisteners-multiplecomplex": {
        "readModeTemplateUrl": "views/properties/task-listeners-display-template.html",
        "writeModeTemplateUrl": "views/properties/task-listeners-write-template.html"
    },
    "oryx-eventlisteners-multiplecomplex": {
        "readModeTemplateUrl": "views/properties/event-listeners-display-template.html",
        "writeModeTemplateUrl": "views/properties/event-listeners-write-template.html"
    },
    "oryx-usertaskassignment-complex": {
        "readModeTemplateUrl": "views/properties/assignment-display-template.html",
        "writeModeTemplateUrl": "views/properties/assignment-write-template.html"
    },
    "oryx-servicetaskfields-complex": {
        "readModeTemplateUrl": "views/properties/fields-display-template.html",
        "writeModeTemplateUrl": "views/properties/fields-write-template.html"
    },
    "oryx-callactivityinparameters-complex": {
        "readModeTemplateUrl": "views/properties/in-parameters-display-template.html",
        "writeModeTemplateUrl": "views/properties/in-parameters-write-template.html"
    },
    "oryx-callactivityoutparameters-complex": {
        "readModeTemplateUrl": "views/properties/out-parameters-display-template.html",
        "writeModeTemplateUrl": "views/properties/out-parameters-write-template.html"
    },
    "oryx-subprocessreference-subprocess-link": {
        "readModeTemplateUrl": "views/properties/subprocess-reference-display-template.html",
        "writeModeTemplateUrl": "views/properties/subprocess-reference-write-template.html"
    },
    "oryx-formreference-complex": {
        "readModeTemplateUrl": "views/properties/form-reference-display-template.html",
        "writeModeTemplateUrl": "views/properties/form-reference-write-template.html"
    },
    "oryx-sequencefloworder-complex" : {
        "readModeTemplateUrl": "views/properties/sequenceflow-order-display-template.html",
        "writeModeTemplateUrl": "views/properties/sequenceflow-order-write-template.html"
    },
    "oryx-conditionsequenceflow-complex" : {
        "readModeTemplateUrl": "views/properties/condition-expression-display-template.html",
        "writeModeTemplateUrl": "views/properties/condition-expression-write-template.html"
    },
    "oryx-signaldefinitions-multiplecomplex" : {
        "readModeTemplateUrl": "views/properties/signal-definitions-display-template.html",
        "writeModeTemplateUrl": "views/properties/signal-definitions-write-template.html"
    },
    "oryx-signalref-string" : {
        "readModeTemplateUrl": "views/properties/default-value-display-template.html",
        "writeModeTemplateUrl": "views/properties/signal-property-write-template.html"
    },
    "oryx-messagedefinitions-multiplecomplex" : {
        "readModeTemplateUrl": "views/properties/message-definitions-display-template.html",
        "writeModeTemplateUrl": "views/properties/message-definitions-write-template.html"
    },
    "oryx-messageref-string" : {
        "readModeTemplateUrl": "views/properties/default-value-display-template.html",
        "writeModeTemplateUrl": "views/properties/message-property-write-template.html"
    },
    "oryx-duedatedefinition-complex": {
        "readModeTemplateUrl": "views/properties/duedate-display-template.html",
        "writeModeTemplateUrl": "views/properties/duedate-write-template.html"
    },
    "oryx-decisiontaskdecisiontablereference-complex": {
        "readModeTemplateUrl": "views/properties/decisiontable-reference-display-template.html",
        "writeModeTemplateUrl": "views/properties/decisiontable-reference-write-template.html"
    }
};
