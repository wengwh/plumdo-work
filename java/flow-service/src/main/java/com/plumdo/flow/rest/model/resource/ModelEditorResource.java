package com.plumdo.flow.rest.model.resource;

import org.apache.commons.lang3.StringUtils;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.engine.ManagementService;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plumdo.flow.cmd.SaveModelEditorCmd;
import com.plumdo.flow.rest.model.ModelEditorJsonRequest;

@RestController
public class ModelEditorResource extends BaseModelResource implements ModelDataJsonConstants {

	@Autowired
	private ManagementService managementService;

	@GetMapping(value = "/models/{modelId}/editor", name = "设计器获取模型信息")
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
				editorJsonNode.put("modelType", "model");
				modelNode.set("model", editorJsonNode);
			} catch (Exception e) {
				logger.error("Error creating model JSON", e);
				throw new FlowableException("Error creating model JSON", e);
			}
		}
		return modelNode;
	}

	@PostMapping(value = "/models/{modelId}/editor", name = "模型设计器保存模型")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveModel(@PathVariable String modelId, @RequestBody ModelEditorJsonRequest values) {
		try {
			Model model = getModelFromRequest(modelId);

			ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
			modelJson.put(MODEL_NAME, values.getName());
			modelJson.put(MODEL_DESCRIPTION, values.getDescription());
			model.setMetaInfo(modelJson.toString());
			model.setName(values.getName());
			if (model.getDeploymentId() != null) {
				model.setDeploymentId(null);
			}
			if (values.isAddVersion()) {
				model.setVersion(model.getVersion() + 1);
			}

			repositoryService.saveModel(model);
			managementService.executeCommand(new SaveModelEditorCmd(model.getId(), values.getJson_xml()));
		} catch (Exception e) {
			logger.error("Error saving model", e);
			throw new FlowableException("Error saving model", e);
		}

	}
}