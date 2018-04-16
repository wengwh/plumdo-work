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
public class ProcessDefinitionSuspendResource extends BaseProcessDefinitionResource {

	@PutMapping(value = "/process-definitions/{processDefinitionId}/suspend", name = "流程定义挂起")
	@ResponseStatus(value = HttpStatus.OK)
	public void suspendProcessDefinition(@PathVariable String processDefinitionId, @RequestBody(required = false) ProcessDefinitionActionRequest actionRequest) {

		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

		if (processDefinition.isSuspended()) {
			exceptionFactory.throwConflict(ErrorConstant.DEFINITION_ALREADY_SUSPEND, processDefinition.getId());
		}

		if (actionRequest == null) {
			repositoryService.suspendProcessDefinitionById(processDefinitionId);
		} else {
			repositoryService.suspendProcessDefinitionById(processDefinition.getId(), actionRequest.isIncludeProcessInstances(), actionRequest.getDate());
		}

	}
}
