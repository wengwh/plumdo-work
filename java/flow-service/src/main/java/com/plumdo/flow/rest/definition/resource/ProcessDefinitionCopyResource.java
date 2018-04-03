package com.plumdo.flow.rest.definition.resource;

import java.io.ByteArrayInputStream;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.definition.ProcessDefinitionCopyRequest;
import com.plumdo.flow.rest.definition.ProcessDefinitionResponse;

@RestController
public class ProcessDefinitionCopyResource extends BaseProcessDefinitionResource {

	@RequestMapping(value = "/process-definition/{processDefinitionId}/copy", method = RequestMethod.POST, produces = "application/json", name="复制流程定义")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ProcessDefinitionResponse copyProcessDefinition(@PathVariable String processDefinitionId,@RequestBody(required=false) ProcessDefinitionCopyRequest processDefinitionCopyRequest) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		try {
			String name = null;
			if(processDefinitionCopyRequest != null && processDefinitionCopyRequest.getName() != null){
				name = processDefinitionCopyRequest.getName();
			}else{
				name = "CopyOf"+processDefinition.getName();
			}
			
			String key = null;
			if(processDefinitionCopyRequest != null && processDefinitionCopyRequest.getKey() != null){
				key = processDefinitionCopyRequest.getKey();
			}else{
				//保证key不重复使用时间戳
				key = "CopyOf"+System.currentTimeMillis();
			}
			
			DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
			BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
			bpmnModel.getMainProcess().setName(name);
			bpmnModel.getMainProcess().setId(key);
      	  	byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
      	  
      	  	String fileName = processDefinition.getResourceName();
      	  	ByteArrayInputStream bis = new ByteArrayInputStream(bpmnBytes);
      	  	deploymentBuilder.addInputStream(fileName, bis);
      	  	deploymentBuilder.name(fileName);
      	  	if(processDefinition.getTenantId() != null) {
      	  		deploymentBuilder.tenantId(processDefinition.getTenantId());
      	  	}
      		
			String deploymentId = deploymentBuilder.deploy().getId();

			ProcessDefinition processDefinitionNew = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
			
			return restResponseFactory.createProcessDefinitionResponse(processDefinitionNew);
		
		} catch (Exception e) {
			throw new FlowableException("Error copy process-definition", e);
		}
		
	}
}
