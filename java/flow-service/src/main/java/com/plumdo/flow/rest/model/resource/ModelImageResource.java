package com.plumdo.flow.rest.model.resource;

import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.repository.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ModelImageResource extends BaseModelResource{
	
	@RequestMapping(value = "/models/{modelId}/image", method = RequestMethod.GET, name="获取模型流程图")
	public ResponseEntity<byte[]> getModelImage(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		byte[] imageBytes = repositoryService.getModelEditorSourceExtra(model.getId());
		if (imageBytes != null) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.IMAGE_PNG);
			try {
				return new ResponseEntity<byte[]>(imageBytes, responseHeaders,HttpStatus.OK);
			} catch (Exception e) {
				throw new FlowableException("Error reading image stream", e);
			}
		} else {
			throw new FlowableIllegalArgumentException("model with id '" + model.getId()+ "' has no image.");
		}
	}
}
