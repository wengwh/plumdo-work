package com.plumdo.flow.rest.task.resource;

import org.flowable.task.api.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.constant.TableConstant;

@RestController
public class TaskAssignResource extends BaseTaskResource {

	@PutMapping(value = "/tasks/{taskId}/assign/{assignee}", name = "任务转办")
	@ResponseStatus(value = HttpStatus.OK)
	public void assignTask(@PathVariable("taskId") String taskId, @PathVariable("assignee") String assignee) {
		Task task = getTaskFromRequest(taskId);
		if (TableConstant.ASSIGNEE_NOBODY.equals(assignee)) {
			taskService.setAssignee(taskId, null);
		} else {
			taskService.setAssignee(task.getId(), assignee);
		}
	}
}
