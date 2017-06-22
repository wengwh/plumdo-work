package com.plumdo.flow.rest.diagram.resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class ProcessDefinitionDiagramLayoutResource extends BaseDiagramLayoutResource {

	@RequestMapping(value="/process-definition/{processDefinitionId}/diagram-layout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8", name="获取流程定义监控图信息")
	public ObjectNode getDiagram(@PathVariable String processDefinitionId) {
		return getDiagramNode(null, processDefinitionId,null);
	}
  
	@RequestMapping(value="/process-definition/{processDefinitionKey}/latest/diagram-layout", method = RequestMethod.GET, produces = "application/json;charset=UTF-8", name="根据KEY获取最新的流程定义")
	public ObjectNode getLatestDiagram(@PathVariable String processDefinitionKey,@RequestParam(value = "tenantId", required = false) String tenantId) {
		return getDiagramNode(null, null,processDefinitionKey,tenantId);
	}

}
