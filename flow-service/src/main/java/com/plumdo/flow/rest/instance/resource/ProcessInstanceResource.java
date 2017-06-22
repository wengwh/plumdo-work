package com.plumdo.flow.rest.instance.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.TaskService;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.HistoricProcessInstanceQueryProperty;
import org.flowable.engine.impl.identity.Authentication;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.exception.FlowableForbiddenException;
import com.plumdo.flow.rest.DataResponse;
import com.plumdo.flow.rest.RequestUtil;
import com.plumdo.flow.rest.instance.HistoricProcessInstancePaginateList;
import com.plumdo.flow.rest.instance.ProcessInstanceStartRequest;
import com.plumdo.flow.rest.instance.ProcessInstanceStartResponse;
import com.plumdo.flow.rest.task.TaskActor;


@RestController
public class ProcessInstanceResource extends BaseProcessInstanceResource {

	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

	@Autowired
	protected TaskService taskService;

	static {
		allowedSortProperties.put("id", HistoricProcessInstanceQueryProperty.PROCESS_INSTANCE_ID_);
		allowedSortProperties.put("processDefinitionId", HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
		allowedSortProperties.put("businessKey", HistoricProcessInstanceQueryProperty.BUSINESS_KEY);
		allowedSortProperties.put("startTime", HistoricProcessInstanceQueryProperty.START_TIME);
		allowedSortProperties.put("endTime", HistoricProcessInstanceQueryProperty.END_TIME);
		allowedSortProperties.put("duration", HistoricProcessInstanceQueryProperty.DURATION);
		allowedSortProperties.put("tenantId", HistoricProcessInstanceQueryProperty.TENANT_ID);
	}

	@RequestMapping(value = "/process-instance", method = RequestMethod.GET, produces = "application/json", name = "流程实例查询")
	public DataResponse getProcessInstances(@RequestParam Map<String, String> allRequestParams) {
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();

		if (allRequestParams.containsKey("processInstanceId")) {
			query.processInstanceId(allRequestParams.get("processInstanceId"));
		}

		if (allRequestParams.containsKey("processDefinitionKey")) {
			query.processDefinitionKey(allRequestParams.get("processDefinitionKey"));
		}

		if (allRequestParams.containsKey("processDefinitionId")) {
			query.processDefinitionId(allRequestParams.get("processDefinitionId"));
		}
		
		if (allRequestParams.containsKey("businessKey")) {
			query.processInstanceBusinessKey(allRequestParams.get("businessKey"));
		}

		if (allRequestParams.containsKey("involvedUser")) {
			query.involvedUser(allRequestParams.get("involvedUser"));
		}

		if (allRequestParams.get("finished") != null) {
			boolean isFinished = Boolean.valueOf(allRequestParams.get("finished"));
			if (isFinished) {
				query.finished();
			} else {
				query.unfinished();
			}
		}

		if (allRequestParams.get("superProcessInstanceId") != null) {
			query.superProcessInstanceId(allRequestParams.get("superProcessInstanceId"));
		}

		if (allRequestParams.get("excludeSubprocesses") != null) {
			query.excludeSubprocesses(Boolean.valueOf(allRequestParams.get("excludeSubprocesses")));
		}

		if (allRequestParams.get("finishedAfter") != null) {
			query.finishedAfter(RequestUtil.getDate(allRequestParams, "finishedAfter"));
		}

		if (allRequestParams.get("finishedBefore") != null) {
			query.finishedBefore(RequestUtil.getDate(allRequestParams, "finishedBefore"));
		}

		if (allRequestParams.get("startedAfter") != null) {
			query.startedAfter(RequestUtil.getDate(allRequestParams, "startedAfter"));
		}

		if (allRequestParams.get("startedBefore") != null) {
			query.startedBefore(RequestUtil.getDate(allRequestParams, "startedBefore"));
		}

		if (allRequestParams.get("startedBy") != null) {
			query.startedBy(allRequestParams.get("startedBy"));
		}

		if (allRequestParams.get("includeProcessVariables") != null) {
			if (Boolean.valueOf(allRequestParams.get("includeProcessVariables"))) {
				query.includeProcessVariables();
			}
		}

		if (allRequestParams.get("tenantId") != null) {
			query.processInstanceTenantIdLike(allRequestParams.get("tenantId"));
		}

		if (allRequestParams.get("withoutTenantId") != null) {
			if (Boolean.valueOf(allRequestParams.get("withoutTenantId")))
				query.processInstanceWithoutTenantId();
		}
		return new HistoricProcessInstancePaginateList(restResponseFactory).paginateList(allRequestParams, query, "id", allowedSortProperties);
	}

	@RequestMapping(value = "/process-instance", method = RequestMethod.POST, produces = "application/json", name = "流程实例创建")
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
				taskExtService.saveTaskAssigneeVar(task.getId());
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

	@RequestMapping(value = "/process-instance/batch", method = RequestMethod.DELETE, name = "流程实例批量删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void batchDeleteProcessInstance(@RequestParam(value = "processInstanceIds") String processInstanceIds, @RequestParam(value = "deleteReason", required = false) String deleteReason) {
		for (String processInstanceId : processInstanceIds.split(",")) {
			this.deleteProcessInstance(processInstanceId, deleteReason,false);
		}
	}

	@RequestMapping(value = "/process-instance/{processInstanceId}", method = RequestMethod.DELETE, name = "流程实例删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteProcessInstance(@PathVariable String processInstanceId, @RequestParam(value = "deleteReason", required = false) String deleteReason, @RequestParam(value = "cascade", required = false) boolean cascade) {
		HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceFromRequest(processInstanceId);
		if (historicProcessInstance.getEndTime() == null) {
			ExecutionEntity executionEntity = (ExecutionEntity) getProcessInstanceFromRequest(processInstanceId);
			if (StringUtils.isEmpty(executionEntity.getSuperExecutionId())) {
				runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
				if(cascade){
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
