package com.plumdo.flow.rest;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Component;

import com.plumdo.flow.rest.model.ModelResponse;
import com.plumdo.flow.rest.model.ProcessDefinitionResponse;


/**
 * rest接口返回结果工厂类
 * @author wengwh
 *
 */

@Component
public class RestResponseFactory {


	public RestResponseFactory() {
	}
	
	
	public List<ProcessDefinitionResponse> createProcessDefinitionResponseList(List<ProcessDefinition> processDefinitions) {
		List<ProcessDefinitionResponse> responseList = new ArrayList<ProcessDefinitionResponse>();
		for (ProcessDefinition instance : processDefinitions) {
			responseList.add(createProcessDefinitionResponse(instance));
		}
		return responseList;
	}

	public ProcessDefinitionResponse createProcessDefinitionResponse(ProcessDefinition processDefinition) {
		ProcessDefinitionResponse response = new ProcessDefinitionResponse();
		response.setId(processDefinition.getId());
		response.setKey(processDefinition.getKey());
		response.setVersion(processDefinition.getVersion());
		response.setCategory(processDefinition.getCategory());
		response.setName(processDefinition.getName());
		response.setDescription(processDefinition.getDescription());
		response.setSuspended(processDefinition.isSuspended());
		response.setGraphicalNotationDefined(processDefinition.hasGraphicalNotation());
		response.setTenantId(processDefinition.getTenantId());
		return response;
	}
	

	

	public List<ModelResponse> createModelResponseList(List<Model> models) {
		List<ModelResponse> responseList = new ArrayList<ModelResponse>();
		for (Model instance : models) {
			responseList.add(createModelResponse(instance));
		}
		return responseList;
	}

	public ModelResponse createModelResponse(Model model) {
		ModelResponse response = new ModelResponse();
		response.setCategory(model.getCategory());
		response.setCreateTime(model.getCreateTime());
		response.setId(model.getId());
		response.setKey(model.getKey());
		response.setLastUpdateTime(model.getLastUpdateTime());
		response.setMetaInfo(model.getMetaInfo());
		response.setName(model.getName());
		response.setVersion(model.getVersion());
		if(model.getDeploymentId()!=null){
			response.setDeployed(true);
		}else{
			response.setDeployed(false);
		}
		response.setTenantId(model.getTenantId());
		return response;
	}
		  

}