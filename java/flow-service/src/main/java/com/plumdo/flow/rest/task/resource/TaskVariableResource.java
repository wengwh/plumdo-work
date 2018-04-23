package com.plumdo.flow.rest.task.resource;

import java.util.List;
import java.util.Map;

import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.variable.RestVariable;

/**
 * 流程变量接口
 * 
 * @author wengwh
 * @date 2018年4月23日
 */
@RestController
public class TaskVariableResource extends BaseTaskResource {

	@GetMapping(value = "/tasks/{taskId}/variables", name = "获取任务变量")
	public List<RestVariable> getExecutionVariables(@PathVariable String taskId) {
		HistoricTaskInstance task = getHistoricTaskFromRequest(taskId);
		if (task.getEndTime() == null) {
			Map<String, Object> variables = taskService.getVariables(task.getId());
			return restResponseFactory.createRestVariables(variables);
		} else {
			List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).list();
			return restResponseFactory.createRestVariables(historicVariableInstances);
		}
	}

	@PostMapping(value = "/tasks/{taskId}/variables", name = "创建流程实例变量")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createExecutionVariable(@PathVariable String taskId, @RequestBody RestVariable restVariable) {
		Task task = getTaskFromRequest(taskId);
		if (restVariable.getName() == null) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.INSTANCE_VAR_NAME_NOT_FOUND);
		}
		taskService.setVariable(task.getId(), restVariable.getName(), restResponseFactory.getVariableValue(restVariable));
	}

	@DeleteMapping(value = "/tasks/{taskId}/variables/{variableName}", name = "删除流程实例变量")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteExecutionVariable(@PathVariable String taskId, @PathVariable("variableName") String variableName) {
		Task task = getTaskFromRequest(taskId);
		taskService.removeVariable(task.getId(), variableName);
	}
}
