package com.plumdo.flow.rest.instance.resource;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.ProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.constant.ErrorConstant;


@RestController
public class ProcessInstanceImageResource extends BaseProcessInstanceResource {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ProcessEngineConfiguration processEngineConfiguration;

	@GetMapping(value = "/process-instances/{processInstanceId}/image", name="流程实例流程图")
	public ResponseEntity<byte[]> getProcessInstanceImage(@PathVariable String processInstanceId) {
		HistoricProcessInstance processInstance = getHistoricProcessInstanceFromRequest(processInstanceId);
		ProcessDefinition pde = repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
		if (pde == null || !pde.hasGraphicalNotation()) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.INSTANCE_IMAGE_NOT_FOUND, processInstance.getId());
		}
		
		List<String> highLightedActivities = null;
		if (processInstance.getEndTime() == null) {
			highLightedActivities = runtimeService.getActiveActivityIds(processInstance.getId());
		} else {
			highLightedActivities = Collections.<String>emptyList();
		}
		
		BpmnModel bpmnModel = repositoryService.getBpmnModel(pde.getId());
		ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
		
		InputStream resource = diagramGenerator.generateDiagram(bpmnModel,"png", highLightedActivities, 
				Collections.<String> emptyList(),
				processEngineConfiguration.getActivityFontName(),
				processEngineConfiguration.getLabelFontName(),
				processEngineConfiguration.getAnnotationFontName(),
				processEngineConfiguration.getClassLoader(), 1.0);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.IMAGE_PNG);
		try {
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(resource), responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			exceptionFactory.throwDefinedException(ErrorConstant.INSTANCE_IMAGE_READ_ERROR, e.getMessage());
		}

		return null;
	}
}
