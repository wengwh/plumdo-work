package com.plumdo.flow.rest.model.resource;

import java.io.ByteArrayInputStream;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ModelXmlResource extends BaseModelResource{
	
	@RequestMapping(value = "/model/{modelId}/xml", method = RequestMethod.GET, name="获取模型XML")
	public ResponseEntity<byte[]> getModelXml(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		try {
	    	BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
	    	JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
	    	BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
	      	BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
	      	byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
	      	ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
	      	HttpHeaders responseHeaders = new HttpHeaders();
	      	responseHeaders.setContentType(MediaType.TEXT_XML);
	      	return new ResponseEntity<byte[]>(IOUtils.toByteArray(in), responseHeaders,HttpStatus.OK);
	    } catch (Exception e) {
	    	throw new FlowableException("Error converting resource stream", e);
	    }
	}
}
