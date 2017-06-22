package com.plumdo.flow.rest.task.resource;

import org.flowable.engine.task.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.TaskActionRequest;

@RestController
public class TaskUnclaimResource extends BaseTaskResource {
  
	@RequestMapping(value="/task/{taskId}/unclaim", method = RequestMethod.PUT, name="取消认领")
	@ResponseStatus(value = HttpStatus.OK)
	public void unclaimTask(@PathVariable String taskId,@RequestBody(required=false) TaskActionRequest actionRequest) {
	    Task task = getTaskFromRequest(taskId);
		taskService.unclaim(task.getId());
	}
}
