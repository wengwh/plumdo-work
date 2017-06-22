package com.plumdo.flow.rest.task.resource;

import org.flowable.engine.impl.identity.Authentication;
import org.flowable.engine.task.Task;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.TaskActionRequest;


@RestController
public class TaskHelpResource extends BaseTaskResource {
  
	@RequestMapping(value="/task/{taskId}/help/{helper}", method = RequestMethod.PUT, name="任务协办")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional(propagation = Propagation.REQUIRED)
	public void helpTask(@PathVariable("taskId") String taskId,@PathVariable("helper") String helper,@RequestBody(required=false) TaskActionRequest actionRequest) {
	    Task task = getTaskFromRequest(taskId);
	    if(task.getAssignee() == null){
			taskService.setAssignee(taskId, Authentication.getAuthenticatedUserId());
		}
    	taskService.delegateTask(task.getId(), helper);
	    taskExtService.setStartTime(task.getId());
	}
}
