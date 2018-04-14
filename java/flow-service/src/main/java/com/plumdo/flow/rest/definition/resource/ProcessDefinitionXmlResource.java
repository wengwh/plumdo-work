package com.plumdo.flow.rest.definition.resource;

import java.io.InputStream;
import java.util.List;

import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessDefinitionXmlResource extends BaseProcessDefinitionResource {

	@RequestMapping(value = "/process-definition/{processDefinitionId}/xml", method = RequestMethod.GET, name="流程定义XML")
	public ResponseEntity<byte[]> getProcessDefinitionXml(@PathVariable String processDefinitionId) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		String deploymentId = processDefinition.getDeploymentId();
		String resourceId = processDefinition.getResourceName();
		if (deploymentId == null) {
			throw new FlowableIllegalArgumentException("No deployment id provided");
    	}
	    if (resourceId == null) {
	      throw new FlowableIllegalArgumentException("No resource id provided");
	    }

	    Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
	    if (deployment == null) {
	      throw new FlowableObjectNotFoundException("Could not find a deployment with id '" + deploymentId + "'.", Deployment.class);
	    }

	    List<String> resourceList = repositoryService.getDeploymentResourceNames(deploymentId);

	    if (resourceList.contains(resourceId)) {
	    	final InputStream resourceStream = repositoryService.getResourceAsStream(deploymentId, resourceId);
	    	HttpHeaders responseHeaders = new HttpHeaders();
	      	responseHeaders.setContentType(MediaType.TEXT_XML);
	    	try {
	    		return new ResponseEntity<byte[]>(IOUtils.toByteArray(resourceStream), responseHeaders,HttpStatus.OK);
	    	} catch (Exception e) {
	    		throw new FlowableException("Error converting resource stream", e);
	    	}
	    } else {
	    	throw new FlowableObjectNotFoundException("Could not find a resource with id '" + resourceId + "' in deployment '" + deploymentId + "'.", String.class);
	    }
	}
  
}
