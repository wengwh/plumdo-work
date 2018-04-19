package com.plumdo.flow.rest.instance.resource;

import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.instance.ProcessInstanceActionRequest;


@RestController
public class ProcessInstanceSuspendResource extends BaseProcessInstanceResource {
	
	@PutMapping(value = "/process-instances/{processInstanceId}/suspend", name="流程实例挂起")
	@ResponseStatus(value = HttpStatus.OK)
	public void suspendProcessInstance(@PathVariable String processInstanceId,@RequestBody(required=false) ProcessInstanceActionRequest actionRequest) {
		ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);
		    
		if (processInstance.isSuspended()) {
			exceptionFactory.throwConflict(ErrorConstant.INSTANCE_ALREADY_SUSPEND, processInstance.getId());
		}
		runtimeService.suspendProcessInstanceById(processInstance.getId());
	}
}
