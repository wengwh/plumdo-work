package com.plumdo.flow.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.service.IdentityLinkType;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.plumdo.flow.rest.task.HistoricTaskResponse;
import com.plumdo.flow.rest.task.TaskCompleteResponse;
import com.plumdo.flow.rest.task.TaskDetailResponse;
import com.plumdo.flow.rest.task.TaskIdentityResponse;
import com.plumdo.flow.rest.task.TaskNextActorResponse;
import com.plumdo.flow.rest.task.TaskResponse;
import com.plumdo.flow.rest.variable.BooleanRestVariableConverter;
import com.plumdo.flow.rest.variable.DateRestVariableConverter;
import com.plumdo.flow.rest.variable.DoubleRestVariableConverter;
import com.plumdo.flow.rest.variable.IntegerRestVariableConverter;
import com.plumdo.flow.rest.variable.ListRestVariableConverter;
import com.plumdo.flow.rest.variable.LongRestVariableConverter;
import com.plumdo.flow.rest.variable.RestVariable;
import com.plumdo.flow.rest.variable.RestVariableConverter;
import com.plumdo.flow.rest.variable.ShortRestVariableConverter;
import com.plumdo.flow.rest.variable.StringRestVariableConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plumdo.flow.rest.definition.ProcessDefinitionResponse;
import com.plumdo.flow.rest.instance.HistoricProcessInstanceResponse;
import com.plumdo.flow.rest.instance.ProcessInstanceDetailResponse;
import com.plumdo.flow.rest.instance.ProcessInstanceResponse;
import com.plumdo.flow.rest.instance.ProcessInstanceStartResponse;
import com.plumdo.flow.rest.model.ModelResponse;

/**
 * rest接口返回结果工厂类
 *
 * @author wengwenhui
 * @date 2018年4月17日
 */
@Component
public class RestResponseFactory {

	@Autowired
	private ObjectMapper objectMapper;

	protected List<RestVariableConverter> variableConverters = new ArrayList<RestVariableConverter>();

	public RestResponseFactory() {
		initializeVariableConverters();
	}

	protected void initializeVariableConverters() {
		variableConverters.add(new StringRestVariableConverter());
		variableConverters.add(new IntegerRestVariableConverter());
		variableConverters.add(new LongRestVariableConverter());
		variableConverters.add(new ShortRestVariableConverter());
		variableConverters.add(new DoubleRestVariableConverter());
		variableConverters.add(new BooleanRestVariableConverter());
		variableConverters.add(new DateRestVariableConverter());
		variableConverters.add(new ListRestVariableConverter());

	}

	public Object getVariableValue(RestVariable restVariable) {
		Object value = null;

		if (restVariable.getType() != null) {
			RestVariableConverter converter = null;
			for (RestVariableConverter conv : variableConverters) {
				if (conv.getRestTypeName().equals(restVariable.getType())) {
					converter = conv;
					break;
				}
			}
			if (converter == null) {
				throw new FlowableIllegalArgumentException("Variable '" + restVariable.getName() + "' has unsupported type: '" + restVariable.getType() + "'.");
			}
			value = converter.getVariableValue(restVariable);

		} else {
			value = restVariable.getValue();
		}
		return value;
	}

	public List<ProcessInstanceResponse> createProcessInstanceResponseList(List<ProcessInstance> processInstances) {
		List<ProcessInstanceResponse> responseList = new ArrayList<ProcessInstanceResponse>();
		for (ProcessInstance instance : processInstances) {
			responseList.add(createProcessInstanceResponse(instance));
		}
		return responseList;
	}

	public ProcessInstanceResponse createProcessInstanceResponse(ProcessInstance processInstance) {
		ProcessInstanceResponse result = new ProcessInstanceResponse();
		result.setId(processInstance.getId());
		result.setSuspended(processInstance.isSuspended());
		result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
		result.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
		result.setProcessDefinitionName(processInstance.getProcessDefinitionName());
		result.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
		result.setStartTime(processInstance.getStartTime());
		result.setStartUserId(processInstance.getStartUserId());
		result.setCurrentActivityId(processInstance.getActivityId());
		result.setBusinessKey(processInstance.getBusinessKey());
		result.setTenantId(processInstance.getTenantId());
		return result;
	}

	public List<HistoricProcessInstanceResponse> createHistoricProcessInstancResponseList(List<HistoricProcessInstance> processInstances) {
		List<HistoricProcessInstanceResponse> responseList = new ArrayList<HistoricProcessInstanceResponse>();
		for (HistoricProcessInstance instance : processInstances) {
			responseList.add(createHistoricProcessInstanceResponse(instance));
		}
		return responseList;
	}

	public HistoricProcessInstanceResponse createHistoricProcessInstanceResponse(HistoricProcessInstance processInstance) {
		HistoricProcessInstanceResponse result = new HistoricProcessInstanceResponse();
		createHistoricProcessInstanceResponse(result, processInstance);
		return result;
	}

	public ProcessInstanceDetailResponse createProcessInstanceDetailResponse(HistoricProcessInstance hisProcessInstance, ProcessInstance processInstance) {
		ProcessInstanceDetailResponse result = new ProcessInstanceDetailResponse();
		createHistoricProcessInstanceResponse(result, hisProcessInstance);
		if (processInstance != null) {
			result.setSuspended(processInstance.isSuspended());
		}
		return result;
	}

	private void createHistoricProcessInstanceResponse(HistoricProcessInstanceResponse result, HistoricProcessInstance processInstance) {
		result.setId(processInstance.getId());
		result.setBusinessKey(processInstance.getBusinessKey());
		result.setStartTime(processInstance.getStartTime());
		result.setEndTime(processInstance.getEndTime());
		result.setDurationInMillis(processInstance.getDurationInMillis());
		result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
		result.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
		result.setProcessDefinitionName(processInstance.getProcessDefinitionName());
		result.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
		result.setStartActivityId(processInstance.getStartActivityId());
		result.setStartUserId(processInstance.getStartUserId());
		result.setSuperProcessInstanceId(processInstance.getSuperProcessInstanceId());
		result.setTenantId(processInstance.getTenantId());
	}

	public ProcessInstanceStartResponse createProcessInstanceStartResponse(ProcessInstance processInstance, List<Task> tasks) {
		ProcessInstanceStartResponse result = new ProcessInstanceStartResponse();
		result.setId(processInstance.getId());
		result.setBusinessKey(processInstance.getBusinessKey());
		result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
		// 接口有提供获取定义名称和key但是在启动api里面没有设置进去，只能通过获取定义获取
		// result.setProcessDefinitionName(((ExecutionEntity)processInstance).getProcessDefinition().getName());
		// result.setProcessDefinitionKey(((ExecutionEntity)processInstance).getProcessDefinition().getKey());

		result.setCurrentActivityId(processInstance.getActivityId());
		result.setTenantId(processInstance.getTenantId());
		List<Map<String, String>> taskInfo = new ArrayList<Map<String, String>>();
		for (Task task : tasks) {
			Map<String, String> taskMap = new HashMap<String, String>();
			taskMap.put("taskId", task.getId());
			taskMap.put("taskName", task.getName());
			taskMap.put("taskDefinitionKey", task.getTaskDefinitionKey());
			taskInfo.add(taskMap);
		}
		result.setTaskInfo(taskInfo);
		return result;
	}

	public List<HistoricTaskResponse> createHistoricTaskResponseList(List<HistoricTaskInstance> tasks) {
		List<HistoricTaskResponse> responseList = new ArrayList<HistoricTaskResponse>();
		for (HistoricTaskInstance instance : tasks) {
			responseList.add(createHistoricTaskResponse(instance));
		}
		return responseList;
	}

	public TaskDetailResponse createTaskDetailResponse(HistoricTaskInstance historicTaskInstance, Task taskInstance) {
		TaskDetailResponse result = new TaskDetailResponse();
		createHistoricTaskResponse(result, historicTaskInstance);
		if (taskInstance != null) {
			result.setDelegationState(taskInstance.getDelegationState());
			result.setSuspended(taskInstance.isSuspended());
		}
		return result;
	}

	public HistoricTaskResponse createHistoricTaskResponse(HistoricTaskInstance taskInstance) {
		HistoricTaskResponse result = new HistoricTaskResponse();
		createHistoricTaskResponse(result, taskInstance);
		return result;
	}

	private void createHistoricTaskResponse(HistoricTaskResponse result, HistoricTaskInstance taskInstance) {
		result.setId(taskInstance.getId());
		result.setName(taskInstance.getName());
		result.setOwner(taskInstance.getOwner());
		result.setTaskDefinitionKey(taskInstance.getTaskDefinitionKey());
		result.setAssignee(taskInstance.getAssignee());
		result.setDescription(taskInstance.getDescription());
		result.setCategory(taskInstance.getCategory());
		result.setDueDate(taskInstance.getDueDate());
		result.setFormKey(taskInstance.getFormKey());
		result.setParentTaskId(taskInstance.getParentTaskId());
		result.setPriority(taskInstance.getPriority());
		result.setProcessDefinitionId(taskInstance.getProcessDefinitionId());
		result.setTenantId(taskInstance.getTenantId());
		result.setProcessInstanceId(taskInstance.getProcessInstanceId());
		result.setDurationInMillis(taskInstance.getDurationInMillis());
		result.setStartTime(taskInstance.getStartTime());
		result.setEndTime(taskInstance.getEndTime());
		result.setClaimTime(taskInstance.getClaimTime());
		result.setWorkTimeInMillis(taskInstance.getWorkTimeInMillis());
	}

	public List<TaskResponse> createTaskResponseList(List<Task> tasks) {
		List<TaskResponse> responseList = new ArrayList<TaskResponse>();
		for (Task instance : tasks) {
			responseList.add(createTaskResponse(instance));
		}
		return responseList;
	}

	public TaskResponse createTaskResponse(Task taskInstance) {
		TaskResponse result = new TaskResponse();
		createTaskResponse(result, taskInstance);
		return result;
	}

	public TaskCompleteResponse createTaskCompleteResponse(Task taskInstance, List<IdentityLink> identityLinks) {
		TaskCompleteResponse result = new TaskCompleteResponse();
		createTaskResponse(result, taskInstance);
		result.setCandidate(createTaskIdentityResponseList(identityLinks));
		return result;
	}

	private void createTaskResponse(TaskResponse result, Task taskInstance) {
		result.setId(taskInstance.getId());
		result.setName(taskInstance.getName());
		result.setOwner(taskInstance.getOwner());
		result.setTaskDefinitionKey(taskInstance.getTaskDefinitionKey());
		result.setCreateTime(taskInstance.getCreateTime());
		result.setAssignee(taskInstance.getAssignee());
		result.setDescription(taskInstance.getDescription());
		result.setDueDate(taskInstance.getDueDate());
		result.setDelegationState(taskInstance.getDelegationState());
		result.setFormKey(taskInstance.getFormKey());
		result.setParentTaskId(taskInstance.getParentTaskId());
		result.setPriority(taskInstance.getPriority());
		result.setSuspended(taskInstance.isSuspended());
		result.setTenantId(taskInstance.getTenantId());
		result.setCategory(taskInstance.getCategory());
		result.setProcessDefinitionId(taskInstance.getProcessDefinitionId());
		result.setProcessInstanceId(taskInstance.getProcessInstanceId());
	}

	public List<TaskIdentityResponse> createTaskIdentityResponseList(List<IdentityLink> identityLinks) {
		List<TaskIdentityResponse> responseList = new ArrayList<TaskIdentityResponse>();
		for (IdentityLink identityLink : identityLinks) {
			if (identityLink.getType().equals(IdentityLinkType.CANDIDATE)) {
				responseList.add(createTaskIdentityResponse(identityLink));
			}
		}
		return responseList;
	}

	public TaskIdentityResponse createTaskIdentityResponse(IdentityLink identityLink) {
		TaskIdentityResponse result = new TaskIdentityResponse();
		if (identityLink.getGroupId() != null) {
			result.setIdentityId(identityLink.getGroupId());
			result.setType(TaskIdentityResponse.AUTHORIZE_GROUP);
		} else if (identityLink.getUserId() != null) {
			result.setIdentityId(identityLink.getUserId());
			result.setType(TaskIdentityResponse.AUTHORIZE_USER);
		}
		return result;
	}

	public TaskNextActorResponse createTaskNextActorResponse(Task task, List<IdentityLink> identityLinks) {
		TaskNextActorResponse taskNextActor = new TaskNextActorResponse();
		taskNextActor.setProcessDefinitionId(task.getProcessDefinitionId());
		taskNextActor.setTaskDefinitionKey(task.getTaskDefinitionKey());
		taskNextActor.setTaskDefinitionName(task.getName());
		for (IdentityLink identityLink : identityLinks) {
			if (identityLink.getGroupId() != null) {
				taskNextActor.addActorInfo(identityLink.getGroupId(), TaskNextActorResponse.TYPE_GROUP, identityLink.getType());
			} else if (identityLink.getUserId() != null) {
				taskNextActor.addActorInfo(identityLink.getUserId(), TaskNextActorResponse.TYPE_USER, identityLink.getType());
			}
		}
		return taskNextActor;
	}

	public List<ProcessDefinitionResponse> createProcessDefinitionResponseList(List<ProcessDefinition> processDefinitions) {
		List<ProcessDefinitionResponse> responseList = new ArrayList<ProcessDefinitionResponse>();
		for (ProcessDefinition instance : processDefinitions) {
			responseList.add(createProcessDefinitionResponse(instance));
		}
		return responseList;
	}

	public ProcessDefinitionResponse createProcessDefinitionResponse(ProcessDefinition processDefinition) {
		ProcessDefinitionResponse response = new ProcessDefinitionResponse();
		response.setId(processDefinition.getId());
		response.setKey(processDefinition.getKey());
		response.setVersion(processDefinition.getVersion());
		response.setCategory(processDefinition.getCategory());
		response.setName(processDefinition.getName());
		response.setDescription(processDefinition.getDescription());
		response.setSuspended(processDefinition.isSuspended());
		response.setGraphicalNotationDefined(processDefinition.hasGraphicalNotation());
		response.setTenantId(processDefinition.getTenantId());
		return response;
	}

	public List<ModelResponse> createModelResponseList(List<Model> models) {
		List<ModelResponse> responseList = new ArrayList<ModelResponse>();
		for (Model instance : models) {
			responseList.add(createModelResponse(instance));
		}
		return responseList;
	}

	public ModelResponse createModelResponse(Model model) {
		ModelResponse response = new ModelResponse();
		response.setCategory(model.getCategory());
		response.setCreateTime(model.getCreateTime());
		response.setId(model.getId());
		response.setKey(model.getKey());
		response.setLastUpdateTime(model.getLastUpdateTime());
		try {
			JsonNode modelNode = objectMapper.readTree(model.getMetaInfo());
			response.setDescription(modelNode.get("description").asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setName(model.getName());
		response.setVersion(model.getVersion());
		if (model.getDeploymentId() != null) {
			response.setDeployed(true);
		} else {
			response.setDeployed(false);
		}
		response.setTenantId(model.getTenantId());
		return response;
	}

}