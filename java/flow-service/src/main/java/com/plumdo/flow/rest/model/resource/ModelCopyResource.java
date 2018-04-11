package com.plumdo.flow.rest.model.resource;

import org.flowable.engine.repository.Model;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.constant.TableConstant;
import com.plumdo.flow.rest.model.ModelRequest;
import com.plumdo.flow.rest.model.ModelResponse;

/**
 * 模型复制接口
 *
 * @author wengwenhui
 * @date 2018年4月11日
 */
@RestController
public class ModelCopyResource extends BaseModelResource {

	@PostMapping(value = "/models/{modelId}/copy", name = "模型复制")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional(propagation = Propagation.REQUIRED)
	public ModelResponse copyModel(@PathVariable String modelId, @RequestBody ModelRequest modelRequest) {
		checkModelKeyExists(modelRequest.getKey());
		Model model = getModelFromRequest(modelId);
		
		Model newModel = repositoryService.newModel();
		newModel.setKey(modelRequest.getKey());
		newModel.setVersion(TableConstant.MODEL_VESION_START);
		
		if (ObjectUtils.isNotEmpty(modelRequest.getName())) {
			newModel.setName(modelRequest.getName());
		} else {
			newModel.setName(model.getName());
		}
		if (ObjectUtils.isNotEmpty(modelRequest.getCategory())) {
			newModel.setCategory(modelRequest.getCategory());
		} else {
			newModel.setCategory(model.getCategory());
		}
		if (ObjectUtils.isNotEmpty(modelRequest.getMetaInfo())) {
			newModel.setMetaInfo(modelRequest.getMetaInfo());
		} else {
			newModel.setMetaInfo(model.getMetaInfo());
		}
		if (ObjectUtils.isNotEmpty(modelRequest.getTenantId())) {
			newModel.setTenantId(modelRequest.getTenantId());
		} else {
			newModel.setTenantId(model.getTenantId());
		}
		repositoryService.saveModel(newModel);
		repositoryService.addModelEditorSource(newModel.getId(), repositoryService.getModelEditorSource(modelId));
		repositoryService.addModelEditorSourceExtra(newModel.getId(), repositoryService.getModelEditorSourceExtra(modelId));
		return restResponseFactory.createModelResponse(newModel);
	}

}
