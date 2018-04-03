package com.plumdo.form.rest.model.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.io.UnsupportedEncodingException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.form.entity.FormModel;
import com.plumdo.form.rest.model.FormModelResponse;

@Api(tags = "表单模型")
@RestController
public class FormModelEditorResource extends BaseFormModelResource {

	@ApiOperation(value = "获取表单模型设计内容", notes = "根据表单模型的id来获取表单模型设计内容")
	@ApiImplicitParam(name = "id", value = "表单模型ID", required = true, dataType = "Long", paramType = "path")
	@RequestMapping(value = "/form-models/{id}/json", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public String getEditorJson(@PathVariable Long id) throws UnsupportedEncodingException {
		FormModel formModel = getFormModelFromRequest(id);
		String editorJson = null;
		if (formModel.getEditorSourceBytes() == null) {
			editorJson = objectMapper.createArrayNode().toString();
		} else {
			editorJson = new String(formModel.getEditorSourceBytes(), "utf-8");
		}
		return editorJson;
	}

	@ApiOperation(value = "保存表单模型设计内容", notes = "根据传入的editorJson来保存表单模型设计内容")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "id", value = "表单模型ID", required = true, dataType = "Long", paramType = "path"),
		@ApiImplicitParam(name = "editorJson", value = "表单模型设计内容", required = true, dataType = "String") 
	})
	@RequestMapping(value = "/form-models/{id}/json", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public FormModelResponse saveEditorJson(@PathVariable Long id, @RequestBody String editorJson) throws UnsupportedEncodingException {
		FormModel formModel = getFormModelFromRequest(id);
		formModel.setEditorSourceBytes(editorJson.getBytes("utf-8"));
		formModelRepository.save(formModel);
		return responseFactory.createFormModelResponse(formModel);
	}
}
