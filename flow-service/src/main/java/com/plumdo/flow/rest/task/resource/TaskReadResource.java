package com.plumdo.flow.rest.task.resource;

import org.flowable.engine.history.HistoricTaskInstance;
import org.flowable.engine.impl.identity.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.TaskActionRequest;



@RestController
public class TaskReadResource extends BaseTaskResource {
	
	@RequestMapping(value="/task/{taskId}/read", method = RequestMethod.PUT, name="任务查阅")
	@ResponseStatus(value = HttpStatus.OK)
	public void readTask(@PathVariable("taskId") String taskId,@RequestBody(required=false) TaskActionRequest actionRequest) {
		HistoricTaskInstance task = getHistoricTaskFromRequest(taskId,false);
	    
	    taskExtService.readTask(task.getId(), Authentication.getAuthenticatedUserId());
	}
}
