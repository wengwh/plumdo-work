package com.plumdo.flow.rest.task.resource;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.impl.identity.Authentication;
import org.flowable.engine.query.QueryProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.HistoricCcInfoPaginateList;

import cn.starnet.flowable.engine.db.query.HistoricCcInfoQuery;
import cn.starnet.flowable.engine.db.query.impl.HistoricCcInfoQueryProperty;
import cn.starnet.flowable.rest.service.DataResponse;
import cn.starnet.flowable.rest.service.RequestUtil;


@RestController
public class TaskReadedResource extends BaseTaskResource {
	
	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();
	
	static {
	    allowedSortProperties.put("id", HistoricCcInfoQueryProperty.ID);
	    allowedSortProperties.put("processDefinitionId", HistoricCcInfoQueryProperty.PROCESS_DEFINITION_ID);
	    allowedSortProperties.put("processInstanceId", HistoricCcInfoQueryProperty.PROCESS_INSTANCE_ID);
	    allowedSortProperties.put("startTime", HistoricCcInfoQueryProperty.START_TIME);
	    allowedSortProperties.put("endTime", HistoricCcInfoQueryProperty.END_TIME);
	    allowedSortProperties.put("duration", HistoricCcInfoQueryProperty.DURATION);
	    allowedSortProperties.put("taskId", HistoricCcInfoQueryProperty.TASK_ID);
	    allowedSortProperties.put("taskName", HistoricCcInfoQueryProperty.TASK_NAME);
	    allowedSortProperties.put("tenantId", HistoricCcInfoQueryProperty.TENANT_ID);
	}

	@RequestMapping(value="/task/readed", method = RequestMethod.GET, produces="application/json", name="已阅任务查询")
	public DataResponse getReadedTasks(@RequestParam Map<String, String> allRequestParams) {
		HistoricCcInfoQuery query = taskExtService.createHistoricCcInfoQuery();
		
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
    	
	    if (allRequestParams.get("startTimeAfter") != null) {
	    	query.startTimeAfter(RequestUtil.getDate(allRequestParams, "startTimeAfter"));
	    }
	    
	    if (allRequestParams.get("startTimeBefore") != null) {
	    	query.startTimeBefore(RequestUtil.getDate(allRequestParams, "startTimeBefore"));
	    }
	    
	    if (allRequestParams.get("endTimeAfter") != null) {
	    	query.endTimeAfter(RequestUtil.getDate(allRequestParams, "endTimeAfter"));
	    }
	    
	    if (allRequestParams.get("endTimeBefore") != null) {
	    	query.endTimeBefore(RequestUtil.getDate(allRequestParams, "endTimeBefore"));
	    }
	    
	    if (allRequestParams.get("tenantId") != null) {
	    	query.tenantIdLike(allRequestParams.get("tenantId"));
	    }
	    
	    query.finished();
	    
	    return new HistoricCcInfoPaginateList(restResponseFactory).paginateList(allRequestParams, query, "id", allowedSortProperties);
	}
	
}
