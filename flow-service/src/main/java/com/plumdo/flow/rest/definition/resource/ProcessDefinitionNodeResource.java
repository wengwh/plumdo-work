package com.plumdo.flow.rest.definition.resource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.flowable.engine.ManagementService;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plumdo.flow.rest.definition.ProcessDefinitionNodeResponse;

@RestController
public class ProcessDefinitionNodeResource extends BaseProcessDefinitionResource {

	@Autowired
	private ManagementService managementService;

	@Autowired
	protected ObjectMapper objectMapper;
	
	@RequestMapping(value = "/process-definition/node", method = RequestMethod.GET, name="获取流程定义的节点信息")
	public ProcessDefinitionNodeResponse getProcessDefinitionNode(@RequestParam Map<String, String> allRequestParams) throws SQLException {
		
		if (!allRequestParams.containsKey("processDefinitionId")&&!allRequestParams.containsKey("processDefinitionKey") ) {
			throw new FlowableIllegalArgumentException("Either processDefinitionId, processDefinitionKey is required.");
		}
		if (allRequestParams.containsKey("processDefinitionId")&&allRequestParams.containsKey("processDefinitionKey")) {
			throw new FlowableIllegalArgumentException("Only one of processDefinitionId, processDefinitionKey should be set.");
		}
		
		ProcessDefinitionEntity processDefinition = null;
		if(allRequestParams.containsKey("processDefinitionId")){
			processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(allRequestParams.get("processDefinitionId"));
		}else if(allRequestParams.containsKey("processDefinitionKey")){
			if(allRequestParams.containsKey("tenantId")){
				processDefinition = managementService.executeCommand(new GetProcessDefinitionLatestByKeyCmd(allRequestParams.get("processDefinitionKey"),allRequestParams.get("tenantId")));
			}else{
				processDefinition = managementService.executeCommand(new GetProcessDefinitionLatestByKeyCmd(allRequestParams.get("processDefinitionKey")));
			}
		}

		if(processDefinition == null){
			throw new FlowableObjectNotFoundException("Could not find a process definition ",ProcessDefinition.class);
		}
		
		return restResponseFactory.createProcessDefinitionNodeResponse(processDefinition);
	}
	
	
	@RequestMapping(value = "/process-definition/{processDefinitionId}/{activityId}/next", method = RequestMethod.GET, name="获取下一节点信息")
	public ArrayNode getNextActivity(@PathVariable("processDefinitionId") String processDefinitionId,@PathVariable("activityId")String activityId) throws SQLException {
		ArrayNode responseJSON = objectMapper.createArrayNode();
		List<PvmActivity> activityImpls =  managementService.executeCommand(new FindNextFlowableesCmd(processDefinitionId, activityId));
		for(PvmActivity pvmActivity : activityImpls){
			ObjectNode objectNode = objectMapper.createObjectNode();
			objectNode.put("id", pvmActivity.getId());
			objectNode.put("name", (String) pvmActivity.getProperty("name"));
			responseJSON.add(objectNode);
		}
		return responseJSON;
	}
	
}
