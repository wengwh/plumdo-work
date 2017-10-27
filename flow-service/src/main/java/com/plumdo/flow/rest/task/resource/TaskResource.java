package com.plumdo.flow.rest.task.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.impl.HistoricTaskInstanceQueryProperty;
import org.flowable.engine.task.Task;
import org.flowable.engine.task.TaskQuery;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.DataResponse;
import com.plumdo.flow.rest.RequestUtil;
import com.plumdo.flow.rest.task.TaskPaginateList;
import com.plumdo.flow.rest.task.TaskResponse;


@RestController
public class TaskResource extends BaseTaskResource {
	
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

	 
	@RequestMapping(value="/task", method = RequestMethod.GET, produces="application/json", name="任务查询")
	public DataResponse getTasks(@RequestParam Map<String, String> allRequestParams) {
		TaskQuery query = taskService.createTaskQuery();
		
		if (allRequestParams.get("taskId") != null) {
			query.taskId(allRequestParams.get("taskId"));
	    }
	    
	    if (allRequestParams.get("processInstanceId") != null) {
	    	query.processInstanceId(allRequestParams.get("processInstanceId"));
	    }
	    
	    if (allRequestParams.get("processBusinessKey") != null) {
	    	query.processInstanceBusinessKeyLike(allRequestParams.get("processBusinessKey"));
	    }
	    
	    if (allRequestParams.get("processDefinitionKey") != null) {
	    	query.processDefinitionKeyLike(allRequestParams.get("processDefinitionKey"));
	    }
	    
	    if (allRequestParams.get("processDefinitionId") != null) {
	    	query.processDefinitionId(allRequestParams.get("processDefinitionId"));
	    }
	    
	    if (allRequestParams.get("processDefinitionName") != null) {
	    	query.processDefinitionNameLike(allRequestParams.get("processDefinitionName"));
	    }
	    
	    if (allRequestParams.get("executionId") != null) {
	    	query.executionId(allRequestParams.get("executionId"));
	    }
	    
	    if (allRequestParams.get("taskName") != null) {
	    	query.taskNameLike(allRequestParams.get("taskName"));
	    }
	    
	    if (allRequestParams.get("taskDescription") != null) {
	    	query.taskDescriptionLike(allRequestParams.get("taskDescription"));
	    }
	    
	    if (allRequestParams.get("taskDefinitionKey") != null) {
	    	query.taskDefinitionKeyLike(allRequestParams.get("taskDefinitionKey"));
	    }
	    
	    if (allRequestParams.get("taskAssignee") != null) {
	    	query.taskAssigneeLike(allRequestParams.get("taskAssignee"));
	    }
	    
	    if (allRequestParams.get("taskOwner") != null) {
	    	query.taskOwnerLike(allRequestParams.get("taskOwner"));
	    }
	    
	    if (allRequestParams.get("taskInvolvedUser") != null) {
	    	query.taskInvolvedUser(allRequestParams.get("taskInvolvedUser"));
	    }
	    
	    if (allRequestParams.get("taskPriority") != null) {
	    	query.taskPriority(Integer.valueOf(allRequestParams.get("taskPriority")));
	    }
	    
	   
	    
	    if (allRequestParams.get("dueDateAfter") != null) {
	    	query.taskDueAfter(RequestUtil.getDate(allRequestParams, "dueDateAfter"));
	    }
	    
	    if (allRequestParams.get("dueDateBefore") != null) {
	    	query.taskDueBefore(RequestUtil.getDate(allRequestParams, "dueDateBefore"));
	    }
	    
	    if (allRequestParams.get("taskCreatedBefore") != null) {
	    	query.taskCreatedBefore(RequestUtil.getDate(allRequestParams, "taskCreatedBefore"));
	    }
	    
	    if (allRequestParams.get("taskCreatedAfter") != null) {
	    	query.taskCreatedAfter(RequestUtil.getDate(allRequestParams, "taskCreatedAfter"));
	    }
	    
	    if (allRequestParams.get("tenantId") != null) {
	    	query.taskTenantIdLike(allRequestParams.get("tenantId"));
	    }
	    
	    if (allRequestParams.containsKey("taskCandidateUser")) {
	    	query.taskCandidateUser(allRequestParams.get("taskCandidateUser"));
	    }
	    if (allRequestParams.containsKey("taskCandidateGroup")) {
	    	query.taskCandidateGroup(allRequestParams.get("taskCandidateGroup"));
	    }
	    if (allRequestParams.get("taskCandidateGroups") != null) {
	    	 String[] candidateGroups = allRequestParams.get("taskCandidateGroups").split(",");
	         List<String> groups = new ArrayList<String>(candidateGroups.length);
	         for (String candidateGroup : candidateGroups) {
	           groups.add(candidateGroup);
	         }
	         query.taskCandidateGroupIn(groups);
	    }
	    return new TaskPaginateList(restResponseFactory).paginateList(allRequestParams, query, "taskInstanceId", allowedSortProperties);
	}
	
	/*@RequestMapping(value="/task/{taskId}", method = RequestMethod.GET, produces="application/json", name="根据ID任务查询")
	public TaskResponse getTaskById(@PathVariable("taskId") String taskId) {
		Task task =  taskExtService.createTaskExtQuery().taskId(taskId).singleResult();
		
		if (task == null) {
			throw new FlowableObjectNotFoundException("Could not find a task with id '" + taskId + "'.",Task.class);
		}
		return restResponseFactory.createTaskResponse((TaskExt) task);
	}*/
}
