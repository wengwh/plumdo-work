package com.plumdo.flow.rest.diagram.resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class ProcessInstanceDiagramLayoutResource extends BaseDiagramLayoutResource {
	
	@RequestMapping(value="/process-instance/{processInstanceId}/diagram-layout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8", name="获取流程实例监控图信息")
	public ObjectNode getDiagram(@PathVariable String processInstanceId) {
		return getDiagramNode(processInstanceId, null, null);
	}
}
