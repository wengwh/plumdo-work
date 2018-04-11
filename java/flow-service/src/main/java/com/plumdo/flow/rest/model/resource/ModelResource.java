package com.plumdo.flow.rest.model.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.impl.ModelQueryProperty;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
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
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.cmd.SaveModelEditorCmd;
import com.plumdo.flow.cmd.UpdateModelKeyCmd;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.constant.TableConstant;
import com.plumdo.flow.rest.model.ModelRequest;
import com.plumdo.flow.rest.model.ModelResponse;
import com.plumdo.flow.rest.model.ModelsPaginateList;

/**
 * 模型资源类
 *
 * @author wengwenhui
 * @date 2018年4月11日
 */
@RestController
public class ModelResource extends BaseModelResource {
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
	public PageResponse getModels(@RequestParam Map<String, String> requestParams) {
		ModelQuery modelQuery = repositoryService.createModelQuery();

		if (ObjectUtils.isNotEmpty(requestParams.get("id"))) {
			modelQuery.modelId(requestParams.get("id"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("category"))) {
			modelQuery.modelCategoryLike(requestParams.get("category"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("name"))) {
			modelQuery.modelNameLike(requestParams.get("name"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("key"))) {
			modelQuery.modelKey(requestParams.get("key"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("version"))) {
			modelQuery.modelVersion(ObjectUtils.convertToInteger(requestParams.get("version")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("latestVersion"))) {
			boolean isLatestVersion = ObjectUtils.convertToBoolean(requestParams.get("latestVersion"));
			if (isLatestVersion) {
				modelQuery.latestVersion();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("deploymentId"))) {
			modelQuery.deploymentId(requestParams.get("deploymentId"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("deployed"))) {
			boolean isDeployed = ObjectUtils.convertToBoolean(requestParams.get("deployed"));
			if (isDeployed) {
				modelQuery.deployed();
			} else {
				modelQuery.notDeployed();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
			modelQuery.modelTenantIdLike(requestParams.get("tenantId"));
		}

		if (ObjectUtils.isNotEmpty(requestParams.get("withoutTenantId"))) {
			boolean withoutTenantId = ObjectUtils.convertToBoolean(requestParams.get("withoutTenantId"));
			if (withoutTenantId) {
				modelQuery.modelWithoutTenantId();
			}
		}
		return new ModelsPaginateList(restResponseFactory).paginateList(getPageable(requestParams), modelQuery, allowedSortProperties);
	}

	@GetMapping(value = "/models/{modelId}", name = "根据ID模型查询")
	public ModelResponse getModel(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		return restResponseFactory.createModelResponse(model);
	}

	@GetMapping(value = "/models/{modelkey}/{modelVersion}", name = "根据标识,版本模型查询")
	public ModelResponse getModel(@PathVariable String modelkey, @PathVariable Integer modelVersion) {
		Model model = repositoryService.createModelQuery().modelKey(modelkey).modelVersion(modelVersion).singleResult();
		if (model == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.MODEL_KEY_NOT_FOUND, modelkey, modelVersion);
		}
		return restResponseFactory.createModelResponse(model);
	}

	@PostMapping(value = "/models", name = "模型创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public ModelResponse createModel(@RequestBody ModelRequest modelRequest) {
		checkModelKeyExists(modelRequest.getKey());

		Model model = repositoryService.newModel();
		model.setCategory(modelRequest.getCategory());
		model.setKey(modelRequest.getKey());
		model.setName(modelRequest.getName());
		model.setVersion(TableConstant.MODEL_VESION_START);
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
	@Transactional(propagation = Propagation.REQUIRED)
	public ModelResponse updateModel(@PathVariable String modelId, @RequestBody ModelRequest modelRequest) {
		Model model = getModelFromRequest(modelId);
		model.setCategory(modelRequest.getCategory());
		if (!modelRequest.getKey().equals(model.getKey())) {
			checkModelKeyExists(modelRequest.getKey());
			managementService.executeCommand(new UpdateModelKeyCmd(modelId, modelRequest.getKey()));
		}
		model.setKey(modelRequest.getKey());
		model.setName(modelRequest.getName());
		model.setMetaInfo(modelRequest.getMetaInfo());
		model.setTenantId(modelRequest.getTenantId());
		if (modelRequest.getClearDeployId()) {
			model.setDeploymentId(null);
		}
		repositoryService.saveModel(model);
		
		
		return restResponseFactory.createModelResponse(model);
	}

	@DeleteMapping(value = "/models/{modelId}", name = "模型删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteModel(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		List<Model> models = repositoryService.createModelQuery().modelKey(model.getKey()).list();
		for(Model deleteModel : models) {
			repositoryService.deleteModel(deleteModel.getId());
		}
	}
}
