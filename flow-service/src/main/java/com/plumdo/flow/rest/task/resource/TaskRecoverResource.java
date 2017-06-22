package com.plumdo.flow.rest.task.resource;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.history.HistoricTaskInstance;
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

import cn.starnet.flowable.engine.db.entity.TaskExt;


@RestController
public class TaskRecoverResource extends BaseTaskResource {
	
	@RequestMapping(value="/task/{taskId}/recover", method = RequestMethod.PUT, name="任务回收")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TaskResponse> recoverTask(@PathVariable("taskId") String taskId,@RequestBody(required=false) TaskActionRequest actionRequest) {
		List<TaskResponse> responses = new ArrayList<TaskResponse>();
		
		HistoricTaskInstance historicTaskInstance = getHistoricTaskFromRequest(taskId);
	    
	    List<Task> tasks = taskExtService.recoverTask(historicTaskInstance.getId());

	    for(Task nextTask : tasks){
	    	TaskExt taskExt = taskExtService.getTaskExtById(nextTask.getId());
	 		responses.add(restResponseFactory.createTaskResponse(taskExt));
	    }
	    return responses;
	}
}
