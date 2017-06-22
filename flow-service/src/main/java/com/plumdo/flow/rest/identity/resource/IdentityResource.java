package com.plumdo.flow.rest.identity.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.starnet.flowable.rest.service.RestResponseFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class IdentityResource {
	@Autowired
	private IdentityService identityService;	
	
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	protected RestResponseFactory restResponseFactory;
	
	protected static HashMap<String, QueryProperty> properties = new HashMap<String, QueryProperty>();
	  
	
	@RequestMapping(value = "/identity/group", method = RequestMethod.GET, name = "获取群组信息")
	public ArrayNode getGroupByParent(@RequestParam Map<String,String> allRequestParams) {
		String parentId = allRequestParams.get("parentId");
		String tenantId = allRequestParams.get("tenantId");
		List<Group> groups = identityService.createNativeGroupQuery()
			.sql("select g.* from ACT_ID_GROUP g  where REV_=#{parentId} and TYPE_ =#{type}")
			.parameter("parentId", parentId).parameter("type", tenantId).list();
		ArrayNode result = objectMapper.createArrayNode();
		for(Group entity : groups){
			ObjectNode objectNode = objectMapper.createObjectNode();
			objectNode.put("id", entity.getId());
			objectNode.put("name", entity.getName());
			objectNode.put("type", entity.getType());
			result.add(objectNode);
		}
		return result;
	}
	
	@RequestMapping(value = "/identity/user/{groupId}", method = RequestMethod.GET, name = "根据部门ID获取用户")
	public ArrayNode getUsersByGroup(@PathVariable("groupId") String groupId) {
		List<User> users = identityService.createNativeUserQuery()
			.sql("select * from act_id_user u where exists (select i.id_ from act_id_info i where u.id_ = i.user_id_ and i.value_=#{value})")
			.parameter("value", groupId).list();
		ArrayNode result = objectMapper.createArrayNode();
		for(User entity : users){
			ObjectNode objectNode = objectMapper.createObjectNode();
			objectNode.put("id", entity.getId());
			objectNode.put("name", entity.getFirstName());
			result.add(objectNode);
		}
		return result;
	}
	
	@RequestMapping(value = "/identity/user", method = RequestMethod.GET, name = "获取人员信息")
	public ArrayNode getUsers(@RequestParam Map<String,String> allRequestParams) {
		UserQuery query = identityService.createUserQuery();
		if (allRequestParams.containsKey("id")) {
	      query.userId(allRequestParams.get("id"));
	    }
	    if (allRequestParams.containsKey("firstName")) {
	      query.userFirstNameLike(allRequestParams.get("firstName"));
	    }
	    if (allRequestParams.containsKey("lastName")) {
	      query.userLastNameLike(allRequestParams.get("lastName"));
	    }
	    if (allRequestParams.containsKey("email")) {
	      query.userEmailLike(allRequestParams.get("email"));
	    }
	    if (allRequestParams.containsKey("memberOfGroup")) {
	      query.memberOfGroup(allRequestParams.get("memberOfGroup"));
	    }
	    if (allRequestParams.containsKey("potentialStarter")) {
	      query.potentialStarter(allRequestParams.get("potentialStarter"));
	    }
	    List<User> users = query.list();
	    ArrayNode result = objectMapper.createArrayNode();
		for(User entity : users){
			ObjectNode objectNode = objectMapper.createObjectNode();
			objectNode.put("id", entity.getId());
			objectNode.put("firstName", entity.getFirstName());
			objectNode.put("lastName", entity.getLastName());
			result.add(objectNode);
		}
		return result;

	}
}
