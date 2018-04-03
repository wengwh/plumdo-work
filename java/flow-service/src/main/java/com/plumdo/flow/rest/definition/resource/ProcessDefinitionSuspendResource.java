package com.plumdo.flow.rest.definition.resource;

import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.exception.FlowableConflictException;
import com.plumdo.flow.rest.definition.ProcessDefinitionActionRequest;


@RestController
public class ProcessDefinitionSuspendResource extends BaseProcessDefinitionResource {

	@RequestMapping(value = "/process-definition/{processDefinitionId}/suspend", method = RequestMethod.PUT, produces = "application/json", name="流程定义挂起")
	@ResponseStatus(value = HttpStatus.OK)
	public void suspendProcessDefinition(@PathVariable String processDefinitionId,@RequestBody(required=false) ProcessDefinitionActionRequest actionRequest) {

		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

		if (processDefinition.isSuspended()) {
			throw new FlowableConflictException("Process definition with id '" + processDefinition.getId() + " ' is already suspend");
		}
		
		if (actionRequest == null) {
			repositoryService.suspendProcessDefinitionById(processDefinitionId);
		}else{
			repositoryService.suspendProcessDefinitionById(processDefinition.getId(), actionRequest.isIncludeProcessInstances(),actionRequest.getDate());
		}

	}
}
