package com.plumdo.flow.rest.task.resource;

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
public class TaskAssignResource extends BaseTaskResource {
  
	@RequestMapping(value="/task/{taskId}/assign/{assignee}", method = RequestMethod.PUT, name="任务转办")
	@ResponseStatus(value = HttpStatus.OK)
	public void assignTask(@PathVariable("taskId") String taskId,@PathVariable("assignee") String assignee,@RequestBody(required=false) TaskActionRequest actionRequest) {
	    Task task = getTaskFromRequest(taskId);
    	
	    taskService.setAssignee(task.getId(),assignee);
	}
}
