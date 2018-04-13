package com.plumdo.flow.rest.definition.resource;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import com.plumdo.common.resource.BaseResource;
import com.plumdo.flow.rest.RestResponseFactory;

public class BaseProcessDefinitionResource extends BaseResource {

	@Autowired
	protected RestResponseFactory restResponseFactory;
	@Autowired
	protected RepositoryService repositoryService;

	protected ProcessDefinition getProcessDefinitionFromRequest(String processDefinitionId) {
		// 直接查询数据库，不查询缓存，防止出现挂起激活验证不一致
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

		if (processDefinition == null) {
			throw new FlowableObjectNotFoundException("Could not find a process definition with id '" + processDefinitionId + "'.", ProcessDefinition.class);
		}

		return processDefinition;
	}
}
