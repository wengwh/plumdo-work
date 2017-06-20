package com.plumdo.flow.rest.model.resource;

import org.apache.commons.lang3.StringUtils;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class ModelEditorJsonRestResource extends BaseModelResource implements ModelDataJsonConstants {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping(value = "/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json", name="设计器获取模型信息")
	public ObjectNode getEditorJson(@PathVariable String modelId) {
		ObjectNode modelNode = null;
		 
		Model model = getModelFromRequest(modelId);

		if (model != null) {
			try {
				if (StringUtils.isNotEmpty(model.getMetaInfo())) {
					modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
				} else {
					modelNode = objectMapper.createObjectNode();
					modelNode.put(MODEL_NAME, model.getName());
				}
				modelNode.put("key", model.getKey());
				modelNode.put("category", model.getCategory());
				modelNode.put("tenantId", model.getTenantId());
				modelNode.put(MODEL_ID, model.getId());
				ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
				modelNode.set("model", editorJsonNode);
			} catch (Exception e) {
				LOGGER.error("Error creating model JSON", e);
				throw new FlowableException("Error creating model JSON", e);
			}
		}
		return modelNode;
	}
}