package com.plumdo.flow.rest.model.resource;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.flowable.editor.constants.EditorJsonConstants;
import org.flowable.editor.constants.StencilConstants;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.impl.ModelQueryProperty;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.flow.exception.FlowableConflictException;
import com.plumdo.flow.rest.model.ModelRequest;
import com.plumdo.flow.rest.model.ModelResponse;
import com.plumdo.flow.rest.model.ModelsPaginateList;


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

	@RequestMapping(value = "/models", method = RequestMethod.GET, produces = "application/json", name="模型查询")
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
		return new ModelsPaginateList(restResponseFactory).paginateList(getPageable(allRequestParams), modelQuery,  allowedSortProperties);
	}
	
	@RequestMapping(value = "/models/{modelId}", method = RequestMethod.GET, produces = "application/json", name="根据ID模型查询")
	public ModelResponse getModel(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		return restResponseFactory.createModelResponse(model);
	}
	
	@RequestMapping(value = "/models", method = RequestMethod.POST, produces = "application/json", name="模型创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public ModelResponse createModel(@RequestBody ModelRequest modelRequest){
		
		Model model = repositoryService.newModel();
		model.setCategory(modelRequest.getCategory());
		model.setKey(modelRequest.getKey());
		model.setMetaInfo(modelRequest.getMetaInfo());
		model.setName(modelRequest.getName());
		model.setVersion(modelRequest.getVersion());
		model.setTenantId(modelRequest.getTenantId());

		repositoryService.saveModel(model);
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put(EditorJsonConstants.EDITOR_STENCIL_ID, "canvas");
		
		editorNode.put(EditorJsonConstants.EDITOR_SHAPE_ID, "canvas");
		//设置流程定义初始化的key和name
		ObjectNode propertieNode = objectMapper.createObjectNode();
		if(StringUtils.isNotEmpty(model.getKey())){
			propertieNode.put(StencilConstants.PROPERTY_PROCESS_ID, model.getKey());
		}else{
			propertieNode.put(StencilConstants.PROPERTY_PROCESS_ID, "model_"+model.getId());
		}
		propertieNode.put(StencilConstants.PROPERTY_NAME, model.getName());
		editorNode.set(EditorJsonConstants.EDITOR_SHAPE_PROPERTIES, propertieNode);
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.set("stencilset", stencilSetNode);
		try {
			repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new FlowableConflictException("create model exception :"+e.getMessage());
		}
	      
		return restResponseFactory.createModelResponse(model);
	}

	@RequestMapping(value = "/models/{modelId}", method = RequestMethod.PUT, produces = "application/json", name="模型修改")
	public ModelResponse updateModel(@PathVariable String modelId,@RequestBody ModelRequest modelRequest) {
		Model model = getModelFromRequest(modelId);

		if (modelRequest.isCategoryChanged()) {
			model.setCategory(modelRequest.getCategory());
		}
		if (modelRequest.isKeyChanged()) {
			model.setKey(modelRequest.getKey());
		}
		if (modelRequest.isMetaInfoChanged()) {
			model.setMetaInfo(modelRequest.getMetaInfo());
		}
		if (modelRequest.isNameChanged()) {
			model.setName(modelRequest.getName());
		}
		if (modelRequest.isVersionChanged()) {
			model.setVersion(modelRequest.getVersion());
		}
		if (modelRequest.isTenantIdChanged()) {
			model.setTenantId(modelRequest.getTenantId());
		}
		
		if (modelRequest.isClearDeployChanged()) {
			if(modelRequest.getClearDeployId()){
				model.setDeploymentId(null);
			}
		}
	
		repositoryService.saveModel(model);
		return restResponseFactory.createModelResponse(model);
	}

	@RequestMapping(value = "/models/{modelId}", method = RequestMethod.DELETE, name="模型删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteModel(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		repositoryService.deleteModel(model.getId());
	}
}
