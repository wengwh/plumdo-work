package com.plumdo.flow.rest.definition.resource;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.IdentityService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.service.IdentityLinkType;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.model.ObjectMap;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.constant.TableConstant;
import com.plumdo.flow.rest.definition.ProcessDefinitionIdentityRequest;

/**
 * 流程定义授权信息接口
 * 
 * @author wengwh
 * @date 2018年4月21日
 */
@RestController
public class ProcessDefinitionIdentityResource extends BaseProcessDefinitionResource {

	@Autowired
	private IdentityService identityService;

	@GetMapping(value = "/process-definitions/{processDefinitionId}/identity-links", name = "流程定义授权查询")
	public List<ObjectMap> getIdentitys(@PathVariable String processDefinitionId) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
		List<ObjectMap> arrayNode = new ArrayList<>();
		for (IdentityLink identityLink : identityLinks) {
			ObjectMap objectNode = new ObjectMap();
			if (identityLink.getGroupId() != null) {
				objectNode.put("type", TableConstant.IDENTITY_GROUP);
				objectNode.put("identityId", identityLink.getGroupId());
				Group group = identityService.createGroupQuery().groupId(identityLink.getGroupId()).singleResult();
				if (group != null) {
					objectNode.put("identityName", group.getName());
				}
			} else if (identityLink.getUserId() != null) {
				objectNode.put("type", TableConstant.IDENTITY_USER);
				objectNode.put("identityId", identityLink.getUserId());
				User user = identityService.createUserQuery().userId(identityLink.getUserId()).singleResult();
				if (user != null) {
					objectNode.put("identityName", user.getFirstName());
				}
			}
			arrayNode.add(objectNode);
		}

		return arrayNode;
	}

	@PostMapping(value = "/process-definitions/{processDefinitionId}/identity-links", name = "流程定义授权创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createIdentity(@PathVariable String processDefinitionId, @RequestBody ProcessDefinitionIdentityRequest authorizeRequest) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

		validateIdentityArguments(authorizeRequest.getIdentityId(), authorizeRequest.getType());

		if (TableConstant.IDENTITY_GROUP.equals(authorizeRequest.getType())) {
			repositoryService.addCandidateStarterGroup(processDefinition.getId(), authorizeRequest.getIdentityId());
		} else if (TableConstant.IDENTITY_USER.equals(authorizeRequest.getType())) {
			repositoryService.addCandidateStarterUser(processDefinition.getId(), authorizeRequest.getIdentityId());
		}

	}

	@DeleteMapping(value = "/process-definitions/{processDefinitionId}/identity-links/{type}/{id}", name = "流程定义授权删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteIdentity(@PathVariable("processDefinitionId") String processDefinitionId, @PathVariable("id") String id, @PathVariable("type") String type) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

		validateIdentityArguments(id, type);

		validateIdentityExists(processDefinitionId, id, type);

		if (TableConstant.IDENTITY_GROUP.equals(type)) {
			repositoryService.deleteCandidateStarterGroup(processDefinition.getId(), id);
		} else if (TableConstant.IDENTITY_USER.equals(type)) {
			repositoryService.deleteCandidateStarterUser(processDefinition.getId(), id);
		}
	}

	private void validateIdentityArguments(String id, String type) {
		if (id == null) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.DEFINITION_IDENTITY_ID_NOT_FOUND);
		}
		if (type == null) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.DEFINITION_IDENTITY_TYPE_NOT_FOUND);
		}

		if (!TableConstant.IDENTITY_GROUP.equals(type) && !TableConstant.IDENTITY_USER.equals(type)) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.DEFINITION_IDENTITY_TYPE_ERROR);
		}
	}

	private void validateIdentityExists(String processDefinitionId, String identityId, String type) {
		List<IdentityLink> allLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinitionId);
		for (IdentityLink link : allLinks) {
			boolean rightIdentity = false;
			if (TableConstant.IDENTITY_USER.equals(type)) {
				rightIdentity = identityId.equals(link.getUserId());
			} else {
				rightIdentity = identityId.equals(link.getGroupId());
			}

			if (rightIdentity && link.getType().equals(IdentityLinkType.CANDIDATE)) {
				return;
			}
		}
		exceptionFactory.throwObjectNotFound(ErrorConstant.DEFINITION_IDENTITY_NOT_FOUND);

	}
}
