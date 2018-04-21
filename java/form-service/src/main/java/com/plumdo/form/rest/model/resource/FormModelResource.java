package com.plumdo.form.rest.model.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.form.domian.FormModel;
import com.plumdo.form.rest.model.FormModelRequest;
import com.plumdo.form.rest.model.FormModelResponse;

@Api(tags = "表单模型")
@RestController
public class FormModelResource extends BaseFormModelResource {

	@ApiOperation(value = "查询表单模型详情", notes = "根据表单模型的id来获取指定对象")
	@ApiImplicitParam(name = "id", value = "表单模型ID", required = true, dataType = "Long", paramType = "path")
	@RequestMapping(value = "/form-models/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public FormModelResponse getFormModel(@PathVariable Long id) {
		FormModel formModel = getFormModelFromRequest(id);
		return responseFactory.createFormModelResponse(formModel);
	}

	@ApiOperation(value = "更新表单模型", notes = "根据表单模型的id来指定更新对象，并根据传过来的modelRequest信息来更新表单模型")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "id", value = "表单模型ID", required = true, dataType = "Long", paramType = "path"), 
		@ApiImplicitParam(name = "modelRequest", value = "表单模型请求实体modelRequest", required = true, dataType = "FormModelRequest") 
	})
	@RequestMapping(value = "/form-models/{id}", method = RequestMethod.PUT, produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public FormModelResponse updateFormModel(@PathVariable Long id, @RequestBody FormModelRequest modelRequest) {
		FormModel formModel = getFormModelFromRequest(id);
		if (modelRequest.getName() != null) {
			formModel.setName(modelRequest.getName());
		}
		if (modelRequest.getKey() != null) {
			formModel.setKey(modelRequest.getKey());
		}
		if (modelRequest.getCategory() != null) {
			formModel.setCategory(modelRequest.getCategory());
		}
		if (modelRequest.getTenantId() != null) {
			formModel.setTenantId(modelRequest.getTenantId());
		}
		formModelRepository.save(formModel);
		return responseFactory.createFormModelResponse(formModel);
	}

	@ApiOperation(value = "删除表单模型", notes = "根据表单模型的id来删除指定对象")
	@ApiImplicitParam(name = "id", value = "表单模型ID", required = true, dataType = "Long", paramType = "path")
	@RequestMapping(value = "/form-models/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteFormModel(@PathVariable Long id) {
		FormModel formModel = getFormModelFromRequest(id);
		formModelRepository.delete(formModel);
	}

}
