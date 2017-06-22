package com.plumdo.flow.rest.instance.resource;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.impl.ProcessInstanceQueryProperty;
import org.flowable.engine.impl.TaskQueryProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.DataResponse;
import com.plumdo.flow.rest.instance.ProcessInstancePaginateList;


@RestController
public class ProcessInstanceRunResource extends BaseProcessInstanceResource {
  
	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

	static {
	    allowedSortProperties.put("processDefinitionId", ProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
	    allowedSortProperties.put("processDefinitionKey", ProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY);
	    allowedSortProperties.put("id", ProcessInstanceQueryProperty.PROCESS_INSTANCE_ID);
	    allowedSortProperties.put("tenantId", ProcessInstanceQueryProperty.TENANT_ID);
	    allowedSortProperties.put("processDefinitionName", new ProcessInstanceQueryProperty("P.NAME_"));
	    allowedSortProperties.put("startUserName", new TaskQueryProperty("U.FIRST_"));
	    allowedSortProperties.put("startTime", new TaskQueryProperty("H.START_TIME_"));
	    
	}
	
	@RequestMapping(value="/process-instance/run", method = RequestMethod.GET, produces="application/json", name="运行的流程实例查询")
  	public DataResponse getRunProcessInstances(@RequestParam Map<String,String> allRequestParams) {
		ProcessInstanceExtQuery query = processExtService.createProcessInstanceExtQuery();
    
		if (allRequestParams.containsKey("processInstanceId")) {
			query.processInstanceId(allRequestParams.get("processInstanceId"));
		}
    
	    if (allRequestParams.containsKey("processDefinitionKey")) {
	    	query.processDefinitionKey(allRequestParams.get("processDefinitionKey"));
	    }
    
	    if (allRequestParams.containsKey("processDefinitionId")) {
	    	query.processDefinitionId(allRequestParams.get("processDefinitionId"));
	    }
	    
	    if (allRequestParams.containsKey("processDefinitionName")) {
	    	query.processDefinitionNameLike(allRequestParams.get("processDefinitionName"));
	    }
	    
	    if (allRequestParams.containsKey("businessKey")) {
	    	query.processInstanceBusinessKey(allRequestParams.get("businessKey"));
	    }
    
	    if (allRequestParams.containsKey("involvedUser")) {
	    	query.involvedUser(allRequestParams.get("involvedUser"));
	    }
    
	    if (allRequestParams.get("suspended") != null) {
	    	boolean isSuspended = Boolean.valueOf(allRequestParams.get("suspended"));
	    	if(isSuspended){
	    		query.suspended();
	    	}else{
	    		query.active();
	    	}
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
	    if (allRequestParams.get("superProcessInstanceId") != null) {
	    	query.superProcessInstanceId(allRequestParams.get("superProcessInstanceId"));
	    }
	      
	    if (allRequestParams.get("excludeSubprocesses") != null) {
	    	query.excludeSubprocesses(Boolean.valueOf(allRequestParams.get("excludeSubprocesses")));
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
		return new ProcessInstancePaginateList(restResponseFactory).paginateList(allRequestParams, query, "id", allowedSortProperties);
	}
  
}
