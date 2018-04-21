package com.plumdo.form.rest.definition.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import com.plumdo.form.domian.FormDefinition;
import com.plumdo.form.jpa.Criteria;
import com.plumdo.form.jpa.Restrictions;
import com.plumdo.form.rest.PageResponse;
import com.plumdo.form.rest.definition.FormDefinitionResponse;

@Api(tags="表单定义")
@RestController
public class FormDefinitionCollectionResource extends BaseFormDefinitionResource{

	@ApiOperation(value="分页查询表单定义", notes="根据传进来的查询参数，获取表单定义信息")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "主键ID", required = false, dataType = "int", paramType = "query"),
	    @ApiImplicitParam(name = "category", value = "定义分类，模糊匹配", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "key", value = "定义键，模糊匹配", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "name", value = "定义名称，模糊匹配", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "tenantId", value = "租户ID，模糊匹配", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "pageNum", value = "分页查询开始查询的页码", defaultValue="1", required = false, dataType = "int", paramType = "query"),
	    @ApiImplicitParam(name = "pageSize", value = "分页查询每页显示的记录数", defaultValue="10", required = false, dataType = "int", paramType = "query"),
	    @ApiImplicitParam(name = "sortName", value = "排序的字段，可以多值以逗号分割", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "sortOrder", value = "排序的方式，可以为asc或desc，可以多值以逗号分割", required = false, dataType = "string", paramType = "query")
	})
	@RequestMapping(value = "/form-definitions", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse<FormDefinitionResponse> getFormDefinitions(@ApiIgnore @RequestParam Map<String, String> requestParams) {
		Criteria<FormDefinition> criteria = new Criteria<FormDefinition>();  
		criteria.add(Restrictions.eq("id", requestParams.get("id"), true)); 
		criteria.add(Restrictions.like("category", requestParams.get("category"), true)); 
		criteria.add(Restrictions.like("key", requestParams.get("key"), true)); 
		criteria.add(Restrictions.like("name", requestParams.get("name"), true)); 
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId"), true)); 
		return responseFactory.createFormDefinitionPageResponse(formDefinitionRepository.findAll(criteria, getPageable(requestParams)));
	}
	
}
