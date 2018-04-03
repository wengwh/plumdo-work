package com.plumdo.flow.rest.task.resource;

import java.util.ArrayList;
import java.util.List;

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
import com.plumdo.flow.rest.task.TaskResponse;



@RestController
public class TaskReturnResource extends BaseTaskResource {
	
	@RequestMapping(value="/task/{taskId}/return", method = RequestMethod.PUT, name="任务回退")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TaskResponse> returnTask(@PathVariable("taskId") String taskId,@RequestBody(required=false) TaskActionRequest actionRequest) {
		List<TaskResponse> responses = new ArrayList<TaskResponse>();
		
		Task task = getTaskFromRequest(taskId);
	    
	    if(task.getAssignee()==null){
			taskService.setAssignee(taskId, Authentication.getAuthenticatedUserId());
		}
	    
	   /* List<Task> tasks = taskExtService.returnTask(task.getId());
	    for(Task nextTask : tasks){
	    	TaskExt taskExt = taskExtService.getTaskExtById(nextTask.getId());
	 		responses.add(restResponseFactory.createTaskResponse(taskExt));
	    }*/
	    return responses;
	}
}
