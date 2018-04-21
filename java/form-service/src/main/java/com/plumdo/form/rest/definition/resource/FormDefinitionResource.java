package com.plumdo.form.rest.definition.resource;

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

import com.plumdo.form.domian.FormDefinition;
import com.plumdo.form.rest.definition.FormDefinitionRequest;

@Api(tags="表单定义")
@RestController
public class FormDefinitionResource extends BaseFormDefinitionResource{
	
	@ApiOperation(value="查询表单定义详情", notes="根据表单定义的id来获取指定对象")
    @ApiImplicitParam(name = "id", value = "表单定义ID", required = true, dataType = "Long",paramType="path")
	@RequestMapping(value = "/form-definitions/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public FormDefinition getFormDefinition(@PathVariable Long id) {
		return getFormDefinitionFromRequest(id);
	}
    
    @ApiOperation(value="更新表单定义", notes="根据表单定义的id来指定更新对象，并根据传过来的definitionRequest信息来更新表单定义")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "表单定义ID", required = true, dataType = "Long",paramType="path"),
        @ApiImplicitParam(name = "definitionRequest", value = "表单定义请求实体definitionRequest", required = true, dataType = "FormDefinitionRequest")
    })
	@RequestMapping(value = "/form-definitions/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK,reason="修改成功")
	public FormDefinition updateFormDefinition(@PathVariable Long id,@RequestBody FormDefinitionRequest definitionRequest) {
    	FormDefinition formDefinition = getFormDefinitionFromRequest(id);
    	if(definitionRequest.getName() != null){
			formDefinition.setName(definitionRequest.getName());
		}
		if(definitionRequest.getKey() != null){
			formDefinition.setKey(definitionRequest.getKey());
		}
		if(definitionRequest.getVersion() != null){
			formDefinition.setVersion(definitionRequest.getVersion());
		}
		if(definitionRequest.getDescription() != null){
			formDefinition.setDescription(definitionRequest.getDescription());
		}
		if(definitionRequest.getCategory() != null){
			formDefinition.setCategory(definitionRequest.getCategory());
		}
		if(definitionRequest.getTenantId() != null){
			formDefinition.setTenantId(definitionRequest.getTenantId());
		}
		
		return formDefinitionRepository.save(formDefinition);
	}

    @ApiOperation(value="删除表单定义", notes="根据表单定义的id来删除指定对象")
    @ApiImplicitParam(name = "id", value = "表单定义ID", required = true, dataType = "Long",paramType="path")
	@RequestMapping(value = "/form-definitions/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteFormDefinition(@PathVariable Long id) {
    	FormDefinition formDefinition = getFormDefinitionFromRequest(id);
		formDefinitionRepository.delete(formDefinition);
	}
}
