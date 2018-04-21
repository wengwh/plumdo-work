package com.plumdo.flow.rest.task.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.FormService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.impl.HistoricTaskInstanceQueryProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.resource.PageResponse;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.task.TaskDetailResponse;
import com.plumdo.flow.rest.task.TaskEditRequest;
import com.plumdo.flow.rest.task.TaskPaginateList;
import com.plumdo.flow.rest.task.TaskResponse;

@RestController
public class TaskResource extends BaseTaskResource {

	@Autowired
	private IdentityService identityService;
	
	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

	static {
		allowedSortProperties.put("deleteReason", HistoricTaskInstanceQueryProperty.DELETE_REASON);
		allowedSortProperties.put("duration", HistoricTaskInstanceQueryProperty.DURATION);
		allowedSortProperties.put("endTime", HistoricTaskInstanceQueryProperty.END);
		allowedSortProperties.put("executionId", HistoricTaskInstanceQueryProperty.EXECUTION_ID);
		allowedSortProperties.put("taskInstanceId", HistoricTaskInstanceQueryProperty.HISTORIC_TASK_INSTANCE_ID);
		allowedSortProperties.put("processDefinitionId", HistoricTaskInstanceQueryProperty.PROCESS_DEFINITION_ID);
		allowedSortProperties.put("processInstanceId", HistoricTaskInstanceQueryProperty.PROCESS_INSTANCE_ID);
		allowedSortProperties.put("assignee", HistoricTaskInstanceQueryProperty.TASK_ASSIGNEE);
		allowedSortProperties.put("taskDefinitionKey", HistoricTaskInstanceQueryProperty.TASK_DEFINITION_KEY);
		allowedSortProperties.put("description", HistoricTaskInstanceQueryProperty.TASK_DESCRIPTION);
		allowedSortProperties.put("dueDate", HistoricTaskInstanceQueryProperty.TASK_DUE_DATE);
		allowedSortProperties.put("name", HistoricTaskInstanceQueryProperty.TASK_NAME);
		allowedSortProperties.put("owner", HistoricTaskInstanceQueryProperty.TASK_OWNER);
		allowedSortProperties.put("priority", HistoricTaskInstanceQueryProperty.TASK_PRIORITY);
		allowedSortProperties.put("tenantId", HistoricTaskInstanceQueryProperty.TENANT_ID_);
		allowedSortProperties.put("startTime", HistoricTaskInstanceQueryProperty.START);
	}

	@GetMapping(value = "/tasks", name = "任务查询")
	public PageResponse getTasks(@RequestParam Map<String, String> requestParams) {
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();

		if (ObjectUtils.isNotEmpty(requestParams.get("taskId"))) {
			query.taskId(requestParams.get("taskId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceId"))) {
			query.processInstanceId(requestParams.get("processInstanceId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceBusinessKey"))) {
			query.processInstanceBusinessKeyLike(ObjectUtils.convertToLike(requestParams.get("processInstanceBusinessKey")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionKey"))) {
			query.processDefinitionKeyLike(ObjectUtils.convertToLike(requestParams.get("processDefinitionKey")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionId"))) {
			query.processDefinitionId(requestParams.get("processDefinitionId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionName"))) {
			query.processDefinitionNameLike(ObjectUtils.convertToLike(requestParams.get("processDefinitionName")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("executionId"))) {
			query.executionId(requestParams.get("executionId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskName"))) {
			query.taskNameLike(ObjectUtils.convertToLike(requestParams.get("taskName")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskDescription"))) {
			query.taskDescriptionLike(ObjectUtils.convertToLike(requestParams.get("taskDescription")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskDefinitionKey"))) {
			query.taskDefinitionKeyLike(ObjectUtils.convertToLike(requestParams.get("taskDefinitionKey")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskAssignee"))) {
			query.taskAssignee(requestParams.get("taskAssignee"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskOwner"))) {
			query.taskOwner(requestParams.get("taskOwner"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskInvolvedUser"))) {
			query.taskInvolvedUser(requestParams.get("taskInvolvedUser"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskPriority"))) {
			query.taskPriority(ObjectUtils.convertToInteger(requestParams.get("taskPriority")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finished"))) {
			boolean isFinished = ObjectUtils.convertToBoolean(requestParams.get("finished"));
			if (isFinished) {
				query.finished();
			} else {
				query.unfinished();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processFinished"))) {
			boolean isProcessFinished = ObjectUtils.convertToBoolean(requestParams.get("processFinished"));
			if (isProcessFinished) {
				query.processFinished();
			} else {
				query.processUnfinished();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("parentTaskId"))) {
			query.taskParentTaskId(requestParams.get("parentTaskId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("dueDateAfter"))) {
			query.taskDueAfter(ObjectUtils.convertToDatetime(requestParams.get("dueDateAfter")));
		}

		if (ObjectUtils.isNotEmpty(requestParams.get("dueDateBefore"))) {
			query.taskDueBefore(ObjectUtils.convertToDatetime(requestParams.get("dueDateBefore")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskCreatedBefore"))) {
			query.taskCreatedBefore(ObjectUtils.convertToDatetime(requestParams.get("taskCreatedBefore")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskCreatedAfter"))) {
			query.taskCreatedAfter(ObjectUtils.convertToDatetime(requestParams.get("taskCreatedAfter")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskCompletedBefore"))) {
			query.taskCompletedBefore(ObjectUtils.convertToDatetime(requestParams.get("taskCompletedBefore")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskCompletedAfter"))) {
			query.taskCompletedAfter(ObjectUtils.convertToDatetime(requestParams.get("taskCompletedAfter")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
			query.taskTenantId(requestParams.get("tenantId"));
		}

		if (ObjectUtils.isNotEmpty(requestParams.get("taskCandidateUser"))) {
			query.taskCandidateUser(requestParams.get("taskCandidateUser"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskCandidateGroup"))) {
			query.taskCandidateGroup(requestParams.get("taskCandidateGroup"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("taskCandidateGroups"))) {
			String[] candidateGroups = requestParams.get("taskCandidateGroups").split(",");
			List<String> groups = new ArrayList<String>(candidateGroups.length);
			for (String candidateGroup : candidateGroups) {
				groups.add(candidateGroup);
			}
			query.taskCandidateGroupIn(groups);
		}
		return new TaskPaginateList(restResponseFactory).paginateList(getPageable(requestParams), query, allowedSortProperties);
	}

	@GetMapping(value = "/tasks/{taskId}", name = "根据ID任务查询")
	public TaskDetailResponse getTaskById(@PathVariable("taskId") String taskId) {
		Task task = null;
		HistoricTaskInstance historicTaskInstance = getHistoricTaskFromRequest(taskId);
		if (historicTaskInstance.getEndTime() == null) {
			task = getTaskFromRequest(taskId);
		}
		return restResponseFactory.createTaskDetailResponse(historicTaskInstance, task);
	}

	@PutMapping(value = "/tasks/{taskId}", name = "任务修改")
	public TaskResponse updateTask(@PathVariable String taskId, @RequestBody TaskEditRequest taskEditRequest) {
		Task task = getTaskFromRequest(taskId);
		task.setName(taskEditRequest.getName());
		task.setDescription(taskEditRequest.getDescription());
		task.setAssignee(taskEditRequest.getAssignee());
		task.setOwner(taskEditRequest.getOwner());
		task.setDueDate(taskEditRequest.getDueDate());
		task.setPriority(taskEditRequest.getPriority());
		task.setCategory(taskEditRequest.getCategory());
		taskService.saveTask(task);
		return restResponseFactory.createTaskResponse(task);
	}

	@DeleteMapping(value = "/tasks/{taskId}", name = "任务删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteTask(@PathVariable String taskId) {
		HistoricTaskInstance task = getHistoricTaskFromRequest(taskId);
		if (task.getEndTime() == null) {
			exceptionFactory.throwForbidden(ErrorConstant.TASK_RUN_NOT_DELETE, taskId);
		}
		historyService.deleteHistoricTaskInstance(task.getId());
	}
}
