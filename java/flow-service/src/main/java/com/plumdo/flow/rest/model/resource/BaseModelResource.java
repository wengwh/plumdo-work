package com.plumdo.flow.rest.model.resource;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;

import com.plumdo.flow.rest.RestResponseFactory;



public class BaseModelResource {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorResource.class);
	@Autowired
	protected RestResponseFactory restResponseFactory;
	@Autowired
	protected RepositoryService repositoryService;

	protected Model getModelFromRequest(String modelId) {
		Model model = repositoryService.getModel(modelId);
		if (model == null) {
			throw new FlowableObjectNotFoundException("Could not find a model with id '" + modelId + "'.",Model.class);
		}
		return model;
	}
}
