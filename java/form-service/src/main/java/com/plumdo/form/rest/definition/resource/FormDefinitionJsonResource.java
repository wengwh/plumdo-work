package com.plumdo.form.rest.definition.resource;

import java.io.UnsupportedEncodingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.form.domian.FormDefinition;

@Api(tags="表单定义")
@RestController
public class FormDefinitionJsonResource extends BaseFormDefinitionResource{
	
	@ApiOperation(value = "获取表单定义设计内容", notes = "根据表单定义的id来获取表单定义设计内容")
	@ApiImplicitParam(name = "id", value = "表单定义ID", required = true, dataType = "Long", paramType = "path")
	@RequestMapping(value = "/form-definitions/{id}/json", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public String getEditorJson(@PathVariable Long id) throws UnsupportedEncodingException {
		FormDefinition formDefinition = getFormDefinitionFromRequest(id);
		String editorJson = null;
		if (formDefinition.getEditorSourceBytes() == null) {
			editorJson = objectMapper.createArrayNode().toString();
		} else {
			editorJson = new String(formDefinition.getEditorSourceBytes(), "utf-8");
		}
		return editorJson;
	}
	
    

}
