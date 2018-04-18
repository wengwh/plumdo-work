package com.plumdo.flow.rest.instance.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.TaskService;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.common.impl.identity.Authentication;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.HistoricProcessInstanceQueryProperty;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.resource.PageResponse;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.exception.FlowableForbiddenException;
import com.plumdo.flow.rest.instance.ProcessInstancePaginateList;
import com.plumdo.flow.rest.instance.ProcessInstanceStartRequest;
import com.plumdo.flow.rest.instance.ProcessInstanceStartResponse;
import com.plumdo.flow.rest.task.TaskActor;
import com.plumdo.flow.rest.variable.RestVariable;

@RestController
public class ProcessInstanceResource extends BaseProcessInstanceResource {
	@Autowired
	protected TaskService taskService;

	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

	static {
		allowedSortProperties.put("id", HistoricProcessInstanceQueryProperty.PROCESS_INSTANCE_ID_);
		allowedSortProperties.put("processDefinitionId", HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
		allowedSortProperties.put("processDefinitionKey", HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY);
		allowedSortProperties.put("businessKey", HistoricProcessInstanceQueryProperty.BUSINESS_KEY);
		allowedSortProperties.put("startTime", HistoricProcessInstanceQueryProperty.START_TIME);
		allowedSortProperties.put("endTime", HistoricProcessInstanceQueryProperty.END_TIME);
		allowedSortProperties.put("duration", HistoricProcessInstanceQueryProperty.DURATION);
		allowedSortProperties.put("tenantId", HistoricProcessInstanceQueryProperty.TENANT_ID);
	}

	@GetMapping(value = "/process-instances", name = "流程实例查询")
	public PageResponse getProcessInstances(@RequestParam Map<String, String> requestParams) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();

		if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceId"))) {
			query.processInstanceId(requestParams.get("processInstanceId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionName"))) {
			query.processDefinitionName(requestParams.get("processDefinitionName"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionKey"))) {
			query.processDefinitionKey(requestParams.get("processDefinitionKey"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionId"))) {
			query.processDefinitionId(requestParams.get("processDefinitionId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("businessKey"))) {
			query.processInstanceBusinessKey(requestParams.get("businessKey"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("involvedUser"))) {
			query.involvedUser(requestParams.get("involvedUser"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finished"))) {
			boolean isFinished = ObjectUtils.convertToBoolean(requestParams.get("finished"));
			if (isFinished) {
				query.finished();
			} else {
				query.unfinished();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("superProcessInstanceId"))) {
			query.superProcessInstanceId(requestParams.get("superProcessInstanceId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("excludeSubprocesses"))) {
			query.excludeSubprocesses(ObjectUtils.convertToBoolean(requestParams.get("excludeSubprocesses")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finishedAfter"))) {
			query.finishedAfter(ObjectUtils.convertToDatetime(requestParams.get("finishedAfter")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("finishedBefore"))) {
			query.finishedBefore(ObjectUtils.convertToDatetime(requestParams.get("finishedBefore")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startedAfter"))) {
			query.startedAfter(ObjectUtils.convertToDatetime(requestParams.get("startedAfter")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startedBefore"))) {
			query.startedBefore(ObjectUtils.convertToDatetime(requestParams.get("startedBefore")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startedBy"))) {
			query.startedBy(requestParams.get("startedBy"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
			query.processInstanceTenantIdLike(requestParams.get("tenantId"));
		}

		return new ProcessInstancePaginateList(restResponseFactory).paginateList(getPageable(requestParams), query, allowedSortProperties);
	}

	@RequestMapping(value = "/process-instances", method = RequestMethod.POST, produces = "application/json", name = "流程实例创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public ProcessInstanceStartResponse startProcessInstance(@RequestBody ProcessInstanceStartRequest request) {

		if (request.getProcessDefinitionId() == null && request.getProcessDefinitionKey() == null) {
			throw new FlowableIllegalArgumentException("Either processDefinitionId, processDefinitionKey is required.");
		}

		int paramsSet = ((request.getProcessDefinitionId() != null) ? 1 : 0) + ((request.getProcessDefinitionKey() != null) ? 1 : 0);

		if (paramsSet > 1) {
			throw new FlowableIllegalArgumentException("Only one of processDefinitionId, processDefinitionKey should be set.");
		}

		if (request.isCustomTenantSet()) {
			// Tenant-id can only be used with either key
			if (request.getProcessDefinitionId() != null) {
				throw new FlowableIllegalArgumentException("TenantId can only be used with either processDefinitionKey.");
			}
		}

		Map<String, Object> startVariables = null;
		if (request.getVariables() != null) {
			startVariables = new HashMap<String, Object>();
			for (RestVariable variable : request.getVariables()) {
				if (variable.getName() == null) {
					throw new FlowableIllegalArgumentException("Variable name is required.");
				}
				startVariables.put(variable.getName(), restResponseFactory.getVariableValue(variable));
			}
		}

		// Actually start the instance based on key or id
		ProcessInstance instance = null;
		if (request.getProcessDefinitionId() != null) {
			instance = runtimeService.startProcessInstanceById(request.getProcessDefinitionId(), request.getBusinessKey(), startVariables);
		} else if (request.getProcessDefinitionKey() != null) {
			if (request.isCustomTenantSet()) {
				instance = runtimeService.startProcessInstanceByKeyAndTenantId(request.getProcessDefinitionKey(), request.getBusinessKey(), startVariables, request.getTenantId());
			} else {
				instance = runtimeService.startProcessInstanceByKey(request.getProcessDefinitionKey(), request.getBusinessKey(), startVariables);
			}
		}

		// autoCommit:complete all tasks(not include sub process or father process)
		if (request.isAutoCommitTask()) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();
			for (Task task : tasks) {
				if (StringUtils.isEmpty(task.getAssignee())) {
					taskService.setAssignee(task.getId(), Authentication.getAuthenticatedUserId());
				}
				// taskExtService.saveTaskAssigneeVar(task.getId());
				taskService.complete(task.getId());
			}
		}

		// set next task user
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();
		if (request.getNextActors() != null) {
			for (Task task : tasks) {
				this.addCandidate(task, request.getNextActors());
			}
		}

		return restResponseFactory.createProcessInstanceStartResponse(instance, tasks);
	}

	private void addCandidate(Task task, List<TaskActor> taskActors) {
		for (TaskActor nextActor : taskActors) {
			if (StringUtils.isEmpty(nextActor.getTaskDefinitionKey()) || nextActor.getTaskDefinitionKey().equals(task.getTaskDefinitionKey())) {
				if (nextActor.getType().equals(TaskActor.TYPE_GROUP)) {
					taskService.addCandidateGroup(task.getId(), nextActor.getId());
				} else if (nextActor.getType().equals(TaskActor.TYPE_USER)) {
					taskService.addCandidateUser(task.getId(), nextActor.getId());
				}
			}
		}
	}

	@RequestMapping(value = "/process-instances/batch", method = RequestMethod.DELETE, name = "流程实例批量删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void batchDeleteProcessInstance(@RequestParam(value = "processInstanceIds") String processInstanceIds, @RequestParam(value = "deleteReason", required = false) String deleteReason) {
		for (String processInstanceId : processInstanceIds.split(",")) {
			this.deleteProcessInstance(processInstanceId, deleteReason, false);
		}
	}

	@RequestMapping(value = "/process-instances/{processInstanceId}", method = RequestMethod.DELETE, name = "流程实例删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteProcessInstance(@PathVariable String processInstanceId, @RequestParam(value = "deleteReason", required = false) String deleteReason,
			@RequestParam(value = "cascade", required = false) boolean cascade) {
		HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceFromRequest(processInstanceId);
		if (historicProcessInstance.getEndTime() == null) {
			ExecutionEntity executionEntity = (ExecutionEntity) getProcessInstanceFromRequest(processInstanceId);
			if (StringUtils.isEmpty(executionEntity.getSuperExecutionId())) {
				runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
				if (cascade) {
					historyService.deleteHistoricProcessInstance(processInstanceId);
				}
			} else {
				throw new FlowableForbiddenException("Could not delete a process instance with id '" + processInstanceId + "' have super process.");
			}
		} else {
			historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
		}
	}
}
