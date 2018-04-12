package com.plumdo.flow.rest.model.resource;

import org.flowable.engine.repository.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.annotation.NotAuth;
import com.plumdo.flow.constant.ErrorConstant;

@RestController
public class ModelImageResource extends BaseModelResource {

	@GetMapping(value = "/models/{modelId}/image", name = "获取模型流程图")
	@NotAuth
	public ResponseEntity<byte[]> getModelImage(@PathVariable String modelId) {
		Model model = getModelFromRequest(modelId);
		byte[] imageBytes = repositoryService.getModelEditorSourceExtra(model.getId());
		if (imageBytes == null) {
			exceptionFactory.throwObjectNotFound(ErrorConstant.MODEL_IMAGE_NOT_FOUND, model.getId());
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.IMAGE_PNG);
		try {
			return new ResponseEntity<byte[]>(imageBytes, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			exceptionFactory.throwDefinedException(ErrorConstant.MODEL_IMAGE_READ_ERROR, e.getMessage());
		}
		return null;
	}
}
