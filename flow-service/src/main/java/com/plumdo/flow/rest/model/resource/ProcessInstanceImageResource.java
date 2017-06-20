package com.plumdo.flow.rest.model.resource;

import java.io.InputStream;
import java.util.Collections;



import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProcessInstanceImageResource {

	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected ProcessEngineConfiguration processEngineConfiguration;

	@RequestMapping(value = "/process-instance/{processInstanceId}/image", method = RequestMethod.GET, name="流程实例流程图")
	public ResponseEntity<byte[]> getProcessInstanceImage(@PathVariable String processInstanceId) {
					
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			throw new FlowableObjectNotFoundException("Could not find a run process instance with id '"+  processInstanceId + "'.",ProcessInstance.class);
		}
		ProcessDefinition pde = repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

		if (pde != null && pde.hasGraphicalNotation()) {
			BpmnModel bpmnModel = repositoryService.getBpmnModel(pde.getId());
			ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
			InputStream resource = diagramGenerator.generateDiagram(bpmnModel,"png", runtimeService.getActiveActivityIds(processInstance.getId()), 
					Collections.<String> emptyList(),
					"微软雅黑",
					"微软雅黑",
					"微软雅黑",
					processEngineConfiguration.getClassLoader(), 1.0);

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.IMAGE_PNG);
			try {
				return new ResponseEntity<byte[]>(IOUtils.toByteArray(resource), responseHeaders,HttpStatus.OK);
			} catch (Exception e) {
				throw new FlowableIllegalArgumentException("Error exporting diagram", e);
			}

		} else {
			throw new FlowableIllegalArgumentException("Process instance with id '" + processInstance.getId()+ "' has no graphical notation defined.");
		}
	}
}
