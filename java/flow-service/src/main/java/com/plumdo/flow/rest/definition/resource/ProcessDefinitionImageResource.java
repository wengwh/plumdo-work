package com.plumdo.flow.rest.definition.resource;

import java.io.InputStream;

import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
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
public class ProcessDefinitionImageResource extends BaseProcessDefinitionResource {

	@RequestMapping(value = "/process-definitions/{processDefinitionId}/image", method = RequestMethod.GET, name="流程定义流程图")
	public ResponseEntity<byte[]> getProcessDefinitionImage(@PathVariable String processDefinitionId) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		InputStream imageStream = repositoryService.getProcessDiagram(processDefinition.getId());

		if (imageStream != null) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.IMAGE_PNG);
			try {
				return new ResponseEntity<byte[]>(IOUtils.toByteArray(imageStream), responseHeaders,HttpStatus.OK);
			} catch (Exception e) {
				throw new FlowableException("Error reading image stream", e);
			}
		} else {
			throw new FlowableIllegalArgumentException("Process definition with id '" + processDefinition.getId()+ "' has no image.");
		}
	}
  
}
