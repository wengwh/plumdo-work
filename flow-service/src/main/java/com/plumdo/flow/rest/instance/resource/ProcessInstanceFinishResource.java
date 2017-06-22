package com.plumdo.flow.rest.instance.resource;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.HistoricProcessInstanceQueryProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.DataResponse;
import com.plumdo.flow.rest.RequestUtil;
import com.plumdo.flow.rest.instance.HistoricProcessInstancePaginateList;


@RestController
public class ProcessInstanceFinishResource extends BaseProcessInstanceResource {
  
	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();
	  
	static {
		allowedSortProperties.put("id", HistoricProcessInstanceQueryProperty.PROCESS_INSTANCE_ID_);
	    allowedSortProperties.put("processDefinitionId", HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
	    allowedSortProperties.put("businessKey", HistoricProcessInstanceQueryProperty.BUSINESS_KEY);
	    allowedSortProperties.put("startTime", HistoricProcessInstanceQueryProperty.START_TIME);
	    allowedSortProperties.put("endTime", HistoricProcessInstanceQueryProperty.END_TIME);
	    allowedSortProperties.put("duration", HistoricProcessInstanceQueryProperty.DURATION);
	    allowedSortProperties.put("tenantId", HistoricProcessInstanceQueryProperty.TENANT_ID);
	    allowedSortProperties.put("processDefinitionName", new HistoricProcessInstanceQueryProperty("P.NAME_"));
	    allowedSortProperties.put("startUserName", new HistoricProcessInstanceQueryProperty("U.FIRST_"));
	}
	
	@RequestMapping(value="/process-instance/finish", method = RequestMethod.GET, produces="application/json", name="结束的流程实例查询")
  	public DataResponse getFinishProcessInstances(@RequestParam Map<String,String> allRequestParams) {
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
	    	if(isFinished){
	    		query.finished();
	    	}else{
	    		query.unfinished();
	    	}
	    }else{
		    query.finished();
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
}
