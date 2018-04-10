package com.plumdo.flow.rest.model.resource;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.ManagementService;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.impl.ModelQueryProperty;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.flow.cmd.SaveModelEditorCmd;
import com.plumdo.flow.rest.model.ModelRequest;
import com.plumdo.flow.rest.model.ModelResponse;
import com.plumdo.flow.rest.model.ModelsPaginateList;

@RestController
public class ModelResource extends BaseModelResource {
	@Autowired
	private ManagementService managementService;

	private static Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

	static {
		allowedSortProperties.put("id", ModelQueryProperty.MODEL_ID);
		allowedSortProperties.put("category", ModelQueryProperty.MODEL_CATEGORY);
		allowedSortProperties.put("createTime", ModelQueryProperty.MODEL_CREATE_TIME);
		allowedSortProperties.put("key", ModelQueryProperty.MODEL_KEY);
		allowedSortProperties.put("lastUpdateTime", ModelQueryProperty.MODEL_LAST_UPDATE_TIME);
		allowedSortProperties.put("name", ModelQueryProperty.MODEL_NAME);
		allowedSortProperties.put("version", ModelQueryProperty.MODEL_VERSION);
		allowedSortProperties.put("tenantId", ModelQueryProperty.MODEL_TENANT_ID);
	}

	@GetMapping(value = "/models", name = "模型查询")
	public PageResponse getModels(@RequestParam Map<String, String> allRequestParams) {
		ModelQuery modelQuery = repositoryService.createModelQuery();

		if (allRequestParams.containsKey("id")) {
			modelQuery.modelId(allRequestParams.get("id"));
		}
		if (allRequestParams.containsKey("category")) {
			modelQuery.modelCategoryLike(allRequestParams.get("category"));
		}
		if (allRequestParams.containsKey("name")) {
			modelQuery.modelNameLike(allRequestParams.get("name"));
		}
		if (allRequestParams.containsKey("key")) {
			modelQuery.modelKey(allRequestParams.get("key"));
		}
		if (allRequestParams.containsKey("version")) {
			modelQuery.modelVersion(Integer.valueOf(allRequestParams.get("version")));
		}
		if (allRequestParams.containsKey("latestVersion")) {
			boolean isLatestVersion = Boolean.valueOf(allRequestParams.get("latestVersion"));
			if (isLatestVersion) {
				modelQuery.latestVersion();
			}
		}
		if (allRequestParams.containsKey("deploymentId")) {
			modelQuery.deploymentId(allRequestParams.get("deploymentId"));
		}
		if (allRequestParams.containsKey("deployed")) {
			boolean isDeployed = Boolean.valueOf(allRequestParams.get("deployed"));
			if (isDeployed) {
				modelQuery.deployed();
			} else {
				modelQuery.notDeployed();
			}
		}
		if (allRequestParams.containsKey("tenantId")) {
			modelQuery.modelTenantIdLike(allRequestParams.get("tenantId"));
		}

		if (allRequestParams.containsKey("withoutTenantId")) {
			boolean withoutTenantId = Boolean.valueOf(allRequestParams.get("withoutTenantId"));
			if (withoutTenantId) {
				modelQuery.modelWithoutTenantId();
			}
		}
		return new ModelsPaginateList(restResponseFactory).paginateList(getPageable(allRequestParams), modelQuery, allowedSortProperties);
	}

	@GetMapping(value = "/models/{modelId}", name = "根据ID模型查询")
	public ModelResponse getModel(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		return restResponseFactory.createModelResponse(model);
	}

	@PostMapping(value = "/models", name = "模型创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public ModelResponse createModel(@RequestBody ModelRequest modelRequest) {
		Model model = repositoryService.newModel();
		model.setCategory(modelRequest.getCategory());
		model.setKey(modelRequest.getKey());
		model.setName(modelRequest.getName());
		model.setVersion(modelRequest.getVersion());
		model.setMetaInfo(modelRequest.getMetaInfo());
		model.setTenantId(modelRequest.getTenantId());
		repositoryService.saveModel(model);

		managementService.executeCommand(new SaveModelEditorCmd(model.getId(), createModelJson(model)));

		return restResponseFactory.createModelResponse(model);
	}

	private String createModelJson(Model model) {
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.set("stencilset", stencilSetNode);
		ObjectNode propertiesNode = objectMapper.createObjectNode();
		propertiesNode.put("process_id", model.getKey());
		propertiesNode.put("name", model.getName());
		editorNode.set("properties", propertiesNode);

		ArrayNode childShapeArray = objectMapper.createArrayNode();
		editorNode.set("childShapes", childShapeArray);
		ObjectNode childNode = objectMapper.createObjectNode();
		childShapeArray.add(childNode);
		ObjectNode boundsNode = objectMapper.createObjectNode();
		childNode.set("bounds", boundsNode);
		ObjectNode lowerRightNode = objectMapper.createObjectNode();
		boundsNode.set("lowerRight", lowerRightNode);
		lowerRightNode.put("x", 130);
		lowerRightNode.put("y", 193);
		ObjectNode upperLeftNode = objectMapper.createObjectNode();
		boundsNode.set("upperLeft", upperLeftNode);
		upperLeftNode.put("x", 100);
		upperLeftNode.put("y", 163);
		childNode.set("childShapes", objectMapper.createArrayNode());
		childNode.set("dockers", objectMapper.createArrayNode());
		childNode.set("outgoing", objectMapper.createArrayNode());
		childNode.put("resourceId", "startEvent1");
		ObjectNode stencilNode = objectMapper.createObjectNode();
		childNode.set("stencil", stencilNode);
		stencilNode.put("id", "StartNoneEvent");
		return editorNode.toString();
	}

	@PutMapping(value = "/models/{modelId}", name = "模型修改")
	public ModelResponse updateModel(@PathVariable String modelId, @RequestBody ModelRequest modelRequest) {
		Model model = getModelFromRequest(modelId);

		if (modelRequest.isCategoryChanged()) {
			model.setCategory(modelRequest.getCategory());
		}
		if (modelRequest.isKeyChanged()) {
			model.setKey(modelRequest.getKey());
		}

		if (modelRequest.isNameChanged()) {
			model.setName(modelRequest.getName());
		}
		if (modelRequest.isVersionChanged()) {
			model.setVersion(modelRequest.getVersion());
		}

		if (modelRequest.isMetaInfoChanged()) {
			model.setMetaInfo(modelRequest.getMetaInfo());
		}

		if (modelRequest.isTenantIdChanged()) {
			model.setTenantId(modelRequest.getTenantId());
		}

		if (modelRequest.isClearDeployChanged()) {
			if (modelRequest.getClearDeployId()) {
				model.setDeploymentId(null);
			}
		}

		repositoryService.saveModel(model);
		return restResponseFactory.createModelResponse(model);
	}

	@DeleteMapping(value = "/models/{modelId}", name = "模型删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteModel(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		repositoryService.deleteModel(model.getId());
	}
}
