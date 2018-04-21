package com.plumdo.form.rest.model.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import com.plumdo.form.domian.FormModel;
import com.plumdo.form.jpa.Criteria;
import com.plumdo.form.jpa.Restrictions;
import com.plumdo.form.rest.PageResponse;
import com.plumdo.form.rest.model.FormModelRequest;
import com.plumdo.form.rest.model.FormModelResponse;

@Api(tags = "表单模型")
@RestController
public class FormModelCollectionResource extends BaseFormModelResource {

	@ApiOperation(value = "分页查询表单模型", notes = "根据传进来的查询参数，获取表单模型信息")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "id", value = "主键ID", required = false, dataType = "int", paramType = "query"), 
		@ApiImplicitParam(name = "category", value = "模型分类，模糊匹配", required = false, dataType = "string", paramType = "query"), 
		@ApiImplicitParam(name = "key", value = "模型键，模糊匹配", required = false, dataType = "string", paramType = "query"), 
		@ApiImplicitParam(name = "name", value = "模型名称，模糊匹配", required = false, dataType = "string", paramType = "query"), 
		@ApiImplicitParam(name = "tenantId", value = "租户ID，模糊匹配", required = false, dataType = "string", paramType = "query"), 
		@ApiImplicitParam(name = "pageNum", value = "分页查询开始查询的页码", defaultValue = "1", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "pageSize", value = "分页查询每页显示的记录数", defaultValue = "10", required = false, dataType = "int", paramType = "query"), 
		@ApiImplicitParam(name = "sortName", value = "排序的字段，可以多值以逗号分割", required = false, dataType = "string", paramType = "query"), 
		@ApiImplicitParam(name = "sortOrder", value = "排序的方式，可以为asc或desc，可以多值以逗号分割", required = false, dataType = "string", paramType = "query") 
	})
	@RequestMapping(value = "/form-models", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public PageResponse<FormModelResponse> getFormModels(@ApiIgnore @RequestParam Map<String, String> requestParams) {
		Criteria<FormModel> criteria = new Criteria<FormModel>();
		criteria.add(Restrictions.eq("id", requestParams.get("id"), true));
		criteria.add(Restrictions.like("category", requestParams.get("category"), true));
		criteria.add(Restrictions.like("key", requestParams.get("key"), true));
		criteria.add(Restrictions.like("name", requestParams.get("name"), true));
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId"), true));
		return responseFactory.createFormModelPageResponse(formModelRepository.findAll(criteria, getPageable(requestParams)));
	}

	@ApiOperation(value = "创建表单模型", notes = "根据传过来的modelRequest信息来创建表单模型")
	@ApiImplicitParam(name = "modelRequest", value = "表单模型请求实体modelRequest", required = true, dataType = "FormModelRequest")
	@RequestMapping(value = "/form-models", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public FormModelResponse createFormModel(@RequestBody FormModelRequest modelRequest) {
		FormModel formModel = new FormModel();
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

}
