package com.plumdo.flow.rest.model.resource;


import org.flowable.engine.ManagementService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.cmd.DeployModelCmd;
import com.plumdo.flow.rest.definition.ProcessDefinitionResponse;


@RestController
public class ModelDeployResource extends BaseModelResource{
	@Autowired
	private ManagementService managementService;
	
	@RequestMapping(value = "/model/{modelId}/deploy", method = RequestMethod.POST, produces = "application/json", name="模型部署")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public ProcessDefinitionResponse deployModel(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		Deployment deployment = managementService.executeCommand(new DeployModelCmd(model.getId()));
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
		return restResponseFactory.createProcessDefinitionResponse(processDefinition);
	}
}
