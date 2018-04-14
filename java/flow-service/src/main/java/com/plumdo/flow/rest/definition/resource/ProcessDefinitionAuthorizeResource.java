package com.plumdo.flow.rest.definition.resource;

import java.util.List;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.service.IdentityLinkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plumdo.flow.rest.definition.ProcessDefinitionAuthorizeRequest;

@RestController
public class ProcessDefinitionAuthorizeResource extends BaseProcessDefinitionResource {

	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(value = "/process-definition/{processDefinitionId}/authorize", method = RequestMethod.GET, produces = "application/json", name = "流程定义授权查询")
	public ArrayNode getAuthorizes(@PathVariable String processDefinitionId) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
		ArrayNode arrayNode = objectMapper.createArrayNode();
		for(IdentityLink identityLink :identityLinks){
			ObjectNode objectNode = objectMapper.createObjectNode();
			if(identityLink.getGroupId()!=null){
				objectNode.put("type", ProcessDefinitionAuthorizeRequest.AUTHORIZE_GROUP);
				objectNode.put("identityId", identityLink.getGroupId());
			}else if(identityLink.getUserId()!=null){
				objectNode.put("type", ProcessDefinitionAuthorizeRequest.AUTHORIZE_USER);
				objectNode.put("identityId", identityLink.getUserId());
			}
			arrayNode.add(objectNode);
		}
		
		return arrayNode;
	}
	
	@RequestMapping(value = "/process-definition/{processDefinitionId}/authorize", method = RequestMethod.POST, name="流程定义授权创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createAuthorize(@PathVariable String processDefinitionId,@RequestBody ProcessDefinitionAuthorizeRequest authorizeRequest) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

		validateAuthorizeArguments(authorizeRequest.getIdentityId(), authorizeRequest.getType());

		if (ProcessDefinitionAuthorizeRequest.AUTHORIZE_GROUP.equals(authorizeRequest.getType())) {
			repositoryService.addCandidateStarterGroup(processDefinition.getId(), authorizeRequest.getIdentityId());
		} else if (ProcessDefinitionAuthorizeRequest.AUTHORIZE_USER.equals(authorizeRequest.getType())){
			repositoryService.addCandidateStarterUser(processDefinition.getId(), authorizeRequest.getIdentityId());
		}
	
	}

	@RequestMapping(value="/process-definition/{processDefinitionId}/authorize/{type}/{id}", method = RequestMethod.DELETE, name="流程定义授权删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteAuthorize(@PathVariable("processDefinitionId") String processDefinitionId, @PathVariable("id") String id, @PathVariable("type") String type) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
  
		validateAuthorizeArguments(id, type);
		
		getAuthorize(processDefinitionId, id, type);
	
		if (ProcessDefinitionAuthorizeRequest.AUTHORIZE_GROUP.equals(type)) {
			repositoryService.deleteCandidateStarterGroup(processDefinition.getId(),id);
		} else if (ProcessDefinitionAuthorizeRequest.AUTHORIZE_USER.equals(type)){
			repositoryService.deleteCandidateStarterUser(processDefinition.getId(), id);
		}
	}
	
	protected void validateAuthorizeArguments(String id, String type) {
		if (id == null) {
			throw new FlowableIllegalArgumentException("id is required.");
		}
		if (type == null) {
			throw new FlowableIllegalArgumentException("Type is required.");
		}
		 
		if(!ProcessDefinitionAuthorizeRequest.AUTHORIZE_GROUP.equals(type)&&!ProcessDefinitionAuthorizeRequest.AUTHORIZE_USER.equals(type)){
			throw new FlowableIllegalArgumentException("Type is must be group or user.");
		}
	}
	 
	 protected IdentityLink getAuthorize(String processDefinitionId,String identityId, String type) {
		List<IdentityLink> allLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinitionId);
		 for (IdentityLink link : allLinks) {
			 boolean rightIdentity = false;
			 if (ProcessDefinitionAuthorizeRequest.AUTHORIZE_USER.equals(type)) {
				 rightIdentity = identityId.equals(link.getUserId());
			 } else {
				 rightIdentity = identityId.equals(link.getGroupId());
			 }
		      
			 if (rightIdentity && link.getType().equals(IdentityLinkType.CANDIDATE)) {
		        return link;
			 }
		 }
		 throw new FlowableObjectNotFoundException("Could not find the requested authorize.", IdentityLink.class);
	 }
}
