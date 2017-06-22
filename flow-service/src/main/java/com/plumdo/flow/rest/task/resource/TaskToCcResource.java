package com.plumdo.flow.rest.task.resource;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.impl.identity.Authentication;
import org.flowable.engine.query.QueryProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.CcInfoPaginateList;

import cn.starnet.flowable.engine.db.query.CcInfoQuery;
import cn.starnet.flowable.engine.db.query.impl.CcInfoQueryProperty;
import cn.starnet.flowable.rest.service.DataResponse;
import cn.starnet.flowable.rest.service.RequestUtil;

@RestController
public class TaskToCcResource extends BaseTaskResource {
	
	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();
	
	static {
	    allowedSortProperties.put("id", CcInfoQueryProperty.ID);
	    allowedSortProperties.put("processDefinitionId", CcInfoQueryProperty.PROCESS_DEFINITION_ID);
	    allowedSortProperties.put("processInstanceId", CcInfoQueryProperty.PROCESS_INSTANCE_ID);
	    allowedSortProperties.put("createTime", CcInfoQueryProperty.CREATE_TIME);
	    allowedSortProperties.put("tenantId", CcInfoQueryProperty.TENANT_ID);
	    allowedSortProperties.put("taskId", CcInfoQueryProperty.TASK_ID);
	    allowedSortProperties.put("taskName", CcInfoQueryProperty.TASK_NAME);
	}

	@RequestMapping(value="/task/tocc", method = RequestMethod.GET, produces="application/json", name="待查阅任务查询")
	public DataResponse getToCcTasks(@RequestParam Map<String, String> allRequestParams) {
		CcInfoQuery query = taskExtService.createCcInfoQuery();
		
	    if (allRequestParams.get("executionId") != null) {
	    	query.executionId(allRequestParams.get("executionId"));
	    }
	    
	    if (allRequestParams.get("processInstanceId") != null) {
	    	query.processInstanceId(allRequestParams.get("processInstanceId"));
	    }
	    
	    if (allRequestParams.get("processDefinitionId") != null) {
	    	query.processDefinitionId(allRequestParams.get("processDefinitionId"));
	    }
	    if (allRequestParams.get("processDefinitionKey") != null) {
	    	query.processDefinitionKeyLike(allRequestParams.get("processDefinitionKey"));
	    }
	    if (allRequestParams.get("processDefinitionName") != null) {
	    	query.processDefinitionNameLike(allRequestParams.get("processDefinitionName"));
	    }
	    
	    if (allRequestParams.get("taskId") != null) {
			query.taskId(allRequestParams.get("taskId"));
	    }  
	    if (allRequestParams.get("taskName") != null) {
	    	query.taskNameLike(allRequestParams.get("taskName"));
	    }

	    if (allRequestParams.get("taskDefinitionKey") != null) {
	    	query.taskDefinitionKey(allRequestParams.get("taskDefinitionKey"));
	    }
	    if (allRequestParams.get("assigner") != null) {
	    	query.assigner(allRequestParams.get("assigner"));
	    }
	    boolean isAuthorize = false;
	    
    	if(allRequestParams.get("isAuthorize") != null){
	    	isAuthorize = Boolean.valueOf(allRequestParams.get("isAuthorize"));
	    }
    	
    	if(isAuthorize){
    		query.assignee(Authentication.getAuthenticatedUserId());
 	    }else{
 	    	 if (allRequestParams.get("assignee") != null) {
 	    		query.assignee(allRequestParams.get("assignee"));
 	    	 }
 	    }
    	
	    if (allRequestParams.get("createTimeBefore") != null) {
	    	query.createTimeBefore(RequestUtil.getDate(allRequestParams, "createTimeBefore"));
	    }
	    
	    if (allRequestParams.get("createTimeAfter") != null) {
	    	query.createTimeAfter(RequestUtil.getDate(allRequestParams, "createTimeAfter"));
	    }
	    
	    if (allRequestParams.get("tenantId") != null) {
	    	query.tenantIdLike(allRequestParams.get("tenantId"));
	    }
	    
	    return new CcInfoPaginateList(restResponseFactory).paginateList(allRequestParams, query, "id", allowedSortProperties);
	}
}
