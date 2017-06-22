package com.plumdo.flow.rest.task.resource;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.impl.TaskQueryProperty;
import org.flowable.engine.impl.identity.Authentication;
import org.flowable.engine.query.QueryProperty;
import org.flowable.engine.task.DelegationState;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.TaskPaginateList;

import cn.starnet.flowable.engine.db.query.TaskExtQuery;
import cn.starnet.flowable.rest.service.DataResponse;
import cn.starnet.flowable.rest.service.RequestUtil;

@RestController
public class TaskToClaimResource extends BaseTaskResource {
	
	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();
	
	static {
	    allowedSortProperties.put("id", TaskQueryProperty.TASK_ID);
	    allowedSortProperties.put("name", TaskQueryProperty.NAME);
	    allowedSortProperties.put("description", TaskQueryProperty.DESCRIPTION);
	    allowedSortProperties.put("dueDate", TaskQueryProperty.DUE_DATE);
	    allowedSortProperties.put("createTime", TaskQueryProperty.CREATE_TIME);
	    allowedSortProperties.put("priority", TaskQueryProperty.PRIORITY);
	    allowedSortProperties.put("executionId", TaskQueryProperty.EXECUTION_ID);
	    allowedSortProperties.put("processInstanceId", TaskQueryProperty.PROCESS_INSTANCE_ID);
	    allowedSortProperties.put("tenantId", TaskQueryProperty.TENANT_ID);
	}
	
	@RequestMapping(value="/task/toclaim", method = RequestMethod.GET, produces="application/json", name="待领任务查询")
	public DataResponse getToClaimTasks(@RequestParam Map<String, String> allRequestParams) {
		
		TaskExtQuery query = taskExtService.createTaskExtQuery();
		
		if (allRequestParams.get("taskId") != null) {
			query.taskId(allRequestParams.get("taskId"));
	    }  
	    if (allRequestParams.get("taskName") != null) {
	    	query.taskNameLike(allRequestParams.get("taskName"));
	    }
	    if (allRequestParams.get("taskDefinitionKey") != null) {
	    	query.taskDefinitionKeyLike(allRequestParams.get("taskDefinitionKey"));
	    }
	    if (allRequestParams.get("delegationState") != null) {
	    	boolean delegationState = Boolean.valueOf(allRequestParams.get("delegationState"));
	    	if(delegationState){
		    	query.taskDelegationState(DelegationState.PENDING);
	    	}else{
	    		query.taskDelegationState(null);
	    	}
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
	    if (allRequestParams.get("taskDueAfter") != null) {
	    	query.taskDueAfter(RequestUtil.getDate(allRequestParams, "taskDueAfter"));
	    }
	    
	    if (allRequestParams.get("taskDueBefore") != null) {
	    	query.taskDueBefore(RequestUtil.getDate(allRequestParams, "taskDueBefore"));
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
    	
	    boolean isAuthorize = false;
	    
    	if(allRequestParams.get("isAuthorize") != null){
	    	isAuthorize = Boolean.valueOf(allRequestParams.get("isAuthorize"));
	    }
    	
    	if(isAuthorize){
    		query.taskCandidateUser(Authentication.getAuthenticatedUserId());
 	    }else{
 	    	 if (allRequestParams.get("assignee") != null) {
 	    		query.taskCandidateUser(allRequestParams.get("assignee"));
 	    	 }
 	    }

	    query.taskUnassigned();
	    
	    return new TaskPaginateList(restResponseFactory).paginateList(allRequestParams, query, "id", allowedSortProperties);
	}
}
