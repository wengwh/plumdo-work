package com.plumdo.flow.rest.task.resource;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.impl.HistoricTaskInstanceQueryProperty;
import org.flowable.engine.impl.identity.Authentication;
import org.flowable.engine.query.QueryProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.HistoricTaskPaginateList;

import cn.starnet.flowable.engine.db.query.HistoricTaskExtQuery;
import cn.starnet.flowable.engine.db.query.impl.BusinessQueryProperty;
import cn.starnet.flowable.rest.service.DataResponse;
import cn.starnet.flowable.rest.service.RequestUtil;

@RestController
public class TaskDoneResource extends BaseTaskResource {
	
	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();
	
	static {
	    allowedSortProperties.put("id", HistoricTaskInstanceQueryProperty.HISTORIC_TASK_INSTANCE_ID);
	    allowedSortProperties.put("name", HistoricTaskInstanceQueryProperty.TASK_NAME);
	    allowedSortProperties.put("processDefinitionId", HistoricTaskInstanceQueryProperty.PROCESS_DEFINITION_ID);
	    allowedSortProperties.put("processInstanceId", HistoricTaskInstanceQueryProperty.PROCESS_INSTANCE_ID);
	    allowedSortProperties.put("startTime", HistoricTaskInstanceQueryProperty.START);
	    allowedSortProperties.put("endTime", HistoricTaskInstanceQueryProperty.END);
	    allowedSortProperties.put("assignee", HistoricTaskInstanceQueryProperty.TASK_ASSIGNEE);
	    allowedSortProperties.put("taskDefinitionKey", HistoricTaskInstanceQueryProperty.TASK_DEFINITION_KEY);
	    allowedSortProperties.put("description", HistoricTaskInstanceQueryProperty.TASK_DESCRIPTION);
	    allowedSortProperties.put("dueDate", HistoricTaskInstanceQueryProperty.TASK_DUE_DATE);
	    allowedSortProperties.put("owner", HistoricTaskInstanceQueryProperty.TASK_OWNER);
	    allowedSortProperties.put("priority", HistoricTaskInstanceQueryProperty.TASK_PRIORITY);
	    allowedSortProperties.put("deleteReason", HistoricTaskInstanceQueryProperty.DELETE_REASON);
	    allowedSortProperties.put("duration", HistoricTaskInstanceQueryProperty.DURATION);
	    allowedSortProperties.put("executionId", HistoricTaskInstanceQueryProperty.EXECUTION_ID);
	    allowedSortProperties.put("tenantId", HistoricTaskInstanceQueryProperty.TENANT_ID_);
	    
	    allowedSortProperties.put("attrDate2", BusinessQueryProperty.ATTR_DATE2);
	    allowedSortProperties.put("attrDate3", BusinessQueryProperty.ATTR_DATE3);
	}
	
	@RequestMapping(value="/task/done", method = RequestMethod.GET, produces="application/json", name="已办任务查询")
	public DataResponse getDoneTasks(@RequestParam Map<String, String> allRequestParams) {
		
		HistoricTaskExtQuery query = taskExtService.createHistoricTaskExtQuery();
		
		if (allRequestParams.get("taskId") != null) {
			query.taskId(allRequestParams.get("taskId"));
	    }
		if (allRequestParams.get("taskName") != null) {
	    	query.taskNameLike(allRequestParams.get("taskName"));
	    }
		if (allRequestParams.get("taskDefinitionKey") != null) {
	    	query.taskDefinitionKeyLike(allRequestParams.get("taskDefinitionKey"));
	    }
		
		if (allRequestParams.get("taskDeleteReason") != null) {
	    	query.taskDeleteReasonLike(allRequestParams.get("taskDeleteReason"));
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
	    
	    if (allRequestParams.get("taskCompletedBefore") != null) {
	    	query.taskCompletedBefore(RequestUtil.getDate(allRequestParams, "taskCompletedBefore"));
	    }
	    if (allRequestParams.get("taskCompletedAfter") != null) {
	    	query.taskCompletedAfter(RequestUtil.getDate(allRequestParams, "taskCompletedAfter"));
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
	    
	    if (allRequestParams.get("taskDefinitionKey") != null) {
	    	query.taskDefinitionKeyLike(allRequestParams.get("taskDefinitionKey"));
	    }
	    

	    if (allRequestParams.get("tenantId") != null) {
	    	query.taskTenantIdLike(allRequestParams.get("tenantId"));
	    }
	    
	    boolean isAuthorize = false;
	    
    	if(allRequestParams.get("isAuthorize") != null){
	    	isAuthorize = Boolean.valueOf(allRequestParams.get("isAuthorize"));
	    }

    	if(isAuthorize){
    		query.taskAssignee(Authentication.getAuthenticatedUserId());
 	    }else{
 	    	if (allRequestParams.get("assignee") != null) {
 	    		query.taskAssignee(allRequestParams.get("assignee"));
 	    	}
 	    }
    	
	    query.finished();
	    
	    return new HistoricTaskPaginateList(restResponseFactory).paginateList(allRequestParams, query, "id", allowedSortProperties);
	}
}
