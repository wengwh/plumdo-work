package com.plumdo.flow.rest.task.resource;

import org.flowable.engine.common.impl.identity.Authentication;
import org.flowable.task.api.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.TaskActionRequest;



@RestController
public class TaskClaimResource extends BaseTaskResource {
  
	@RequestMapping(value="/task/{taskId}/claim", method = RequestMethod.PUT, name="任务认领")
	@ResponseStatus(value = HttpStatus.OK)
	public void claimTask(@PathVariable String taskId,@RequestBody(required=false) TaskActionRequest actionRequest) {
	    Task task = getTaskFromRequest(taskId);
		taskService.claim(task.getId(),Authentication.getAuthenticatedUserId());
	}
}
