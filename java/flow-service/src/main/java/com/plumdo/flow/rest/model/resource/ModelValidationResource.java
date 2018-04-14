package com.plumdo.flow.rest.model.resource;

import java.util.List;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class ModelValidationResource {

	@PostMapping(value = "/models/validate", name="模型检查")
	public List<ValidationError> validate(@RequestBody JsonNode body) {
		BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(body);
		ProcessValidator validator = new ProcessValidatorFactory().createDefaultProcessValidator();
		List<ValidationError> errors = validator.validate(bpmnModel);
		return errors;
	}

}