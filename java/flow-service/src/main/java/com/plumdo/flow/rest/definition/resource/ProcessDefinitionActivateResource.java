package com.plumdo.flow.rest.definition.resource;

import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.definition.ProcessDefinitionActionRequest;

@RestController
public class ProcessDefinitionActivateResource extends BaseProcessDefinitionResource {

	@PutMapping(value = "/process-definitions/{processDefinitionId}/activate", name = "流程定义激活")
	@ResponseStatus(value = HttpStatus.OK)
	public void activateProcessDefinition(@PathVariable String processDefinitionId, @RequestBody(required = false) ProcessDefinitionActionRequest actionRequest) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		
		if (!processDefinition.isSuspended()) {
			exceptionFactory.throwConflict(ErrorConstant.DEFINITION_ALREADY_ACTIVE, processDefinition.getId());
		}

		if (actionRequest == null) {
			repositoryService.activateProcessDefinitionById(processDefinitionId);
		} else {
			repositoryService.activateProcessDefinitionById(processDefinition.getId(), actionRequest.isIncludeProcessInstances(), actionRequest.getDate());
		}

	}
}
