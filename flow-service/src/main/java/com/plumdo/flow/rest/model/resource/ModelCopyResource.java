package com.plumdo.flow.rest.model.resource;

import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.repository.Model;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.model.ModelRequest;
import com.plumdo.flow.rest.model.ModelResponse;


@RestController
public class ModelCopyResource extends BaseModelResource {

	@RequestMapping(value = "/model/{modelId}/copy", method = RequestMethod.POST, produces = "application/json", name="模型复制")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional(propagation = Propagation.REQUIRED)
	public ModelResponse copyModel(@PathVariable String modelId,@RequestBody(required=false) ModelRequest modelRequest) {
		Model model = getModelFromRequest(modelId);
		try {
			Model modelData = repositoryService.newModel();
		
			if(modelRequest!=null && modelRequest.isKeyChanged()){
				modelData.setKey(modelRequest.getKey());
			}else{
				modelData.setKey(model.getKey());
			}
			if(modelRequest!=null && modelRequest.isNameChanged()){
				modelData.setName(modelRequest.getName());
			}else{
				modelData.setName(model.getName());
			}
			if(modelRequest!=null && modelRequest.isCategoryChanged()){
				modelData.setCategory(modelRequest.getCategory());
			}else{
				modelData.setCategory(model.getCategory());
			}
			if (modelRequest!=null && modelRequest.isMetaInfoChanged()) {
				modelData.setMetaInfo(modelRequest.getMetaInfo());
			}else{
				modelData.setMetaInfo(model.getMetaInfo());
			}
			if (modelRequest!=null && modelRequest.isVersionChanged()) {
				modelData.setVersion(modelRequest.getVersion());
			}else{
				modelData.setVersion(model.getVersion());
			}
			if (modelRequest!=null && modelRequest.isTenantIdChanged()) {
				modelData.setTenantId(modelRequest.getTenantId());
			}else{
				modelData.setTenantId(model.getTenantId());
			}

			repositoryService.saveModel(modelData);

			repositoryService.addModelEditorSource(modelData.getId(),repositoryService.getModelEditorSource(modelId));
			repositoryService.addModelEditorSourceExtra(modelData.getId(),repositoryService.getModelEditorSourceExtra(modelId));
			return restResponseFactory.createModelResponse(modelData);
		} catch (Exception e) {
			throw new FlowableException("Error copy model", e);
		}
	}
}
