package com.plumdo.form.rest.data.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import com.plumdo.form.entity.FormData;
import com.plumdo.form.exception.ObjectNotFoundException;
import com.plumdo.form.jpa.Criteria;
import com.plumdo.form.jpa.Restrictions;
import com.plumdo.form.repository.FormDataRepository;
import com.plumdo.form.rest.BaseResource;
import com.plumdo.form.rest.data.FormDataRequest;

@Api(tags="表单内容")
@RestController
public class FormDataResource extends BaseResource{

	@Autowired
	private FormDataRepository formDataRepository;
	
	private FormData getFormDataFromRequest(Long id){
		FormData formData = formDataRepository.findOne(id);
		if(formData == null){
			throw new ObjectNotFoundException("Could not find a FormData with id '" + id + "'.",FormData.class);
		}
		return formData;
	}
	

	@ApiOperation(value="分页查询表单内容", notes="根据传进来的查询参数，获取表单内容信息")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "id", value = "主键ID", required = false, dataType = "int", paramType = "query"),
	    @ApiImplicitParam(name = "businessKey", value = "业务主键KEY，模糊匹配", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "key", value = "内容键，模糊匹配", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "value", value = "内容值，模糊匹配", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "tenantId", value = "租户ID，模糊匹配", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "page", value = "分页查询，开始查询的页码", defaultValue="0", required = false, dataType = "int", paramType = "query"),
	    @ApiImplicitParam(name = "size", value = "分页查询，每页显示的记录数", defaultValue="10", required = false, dataType = "int", paramType = "query"),
	    @ApiImplicitParam(name = "sort", value = "排序的字段，可以多值以逗号分割", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "order", value = "排序的方式，可以为asc或desc，可以多值以逗号分割", required = false, dataType = "string", paramType = "query")
	})
	@RequestMapping(value = "/form-datas", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public Page<FormData> getFormDatas(@ApiIgnore @RequestParam Map<String, String> requestParams) {
		Criteria<FormData> criteria = new Criteria<FormData>();  
		criteria.add(Restrictions.eq("id", requestParams.get("id"), true)); 
		criteria.add(Restrictions.like("businessKey", requestParams.get("businessKey"), true)); 
		criteria.add(Restrictions.like("key", requestParams.get("key"), true)); 
		criteria.add(Restrictions.like("value", requestParams.get("value"), true)); 
		criteria.add(Restrictions.like("tenantId", requestParams.get("tenantId"), true)); 
		return formDataRepository.findAll(criteria, getPageable(requestParams));
	}
	
	
	@ApiOperation(value="查询表单内容详情", notes="根据表单内容的id来获取指定对象")
    @ApiImplicitParam(name = "id", value = "表单内容ID", required = true, dataType = "Long",paramType="path")
	@RequestMapping(value = "/form-datas/{id}", method = RequestMethod.GET, produces = "application/json", name="根据ID模型查询")
	@ResponseStatus(value = HttpStatus.OK)
	public FormData getFormData(@PathVariable Long id) {
		return getFormDataFromRequest(id);
	}
	
	@ApiOperation(value="创建表单内容", notes="根据传过来的formDataRequest信息来创建表单内容")
    @ApiImplicitParam(name = "formDataRequest", value = "表单内容请求实体formDataRequest", required = true, dataType = "FormDataRequest")
	@RequestMapping(value = "/form-datas", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public FormData createFormData(@RequestBody FormDataRequest formDataRequest){
		FormData formData = new FormData();
		if(formDataRequest.getKey() != null){
			formData.setKey(formDataRequest.getKey());
		}
		if(formDataRequest.getValue() != null){
			formData.setValue(formDataRequest.getValue());
		}
		if(formDataRequest.getBusinessKey() != null){
//			formData.setBusinessKey(formDataRequest.getBusinessKey());
		}
		if(formDataRequest.getTenantId() != null){
			formData.setTenantId(formDataRequest.getTenantId());
		}
		return formDataRepository.save(formData);
	}
    
    @ApiOperation(value="更新表单内容", notes="根据表单内容的id来指定更新对象，并根据传过来的formDataRequest信息来更新表单内容")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "表单内容ID", required = true, dataType = "Long",paramType="path"),
        @ApiImplicitParam(name = "formDataRequest", value = "表单内容请求实体formDataRequest", required = true, dataType = "FormDataRequest")
    })
	@RequestMapping(value = "/form-datas/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
	public FormData updateFormData(@PathVariable Long id,@RequestBody FormDataRequest formDataRequest) throws InterruptedException {
    	FormData formData = getFormDataFromRequest(id);
    	if(formDataRequest.getKey() != null){
			formData.setKey(formDataRequest.getKey());
		}
		if(formDataRequest.getValue() != null){
			formData.setValue(formDataRequest.getValue());
		}
		if(formDataRequest.getBusinessKey() != null){
//			formData.setBusinessKey(formDataRequest.getBusinessKey());
		}
		if(formDataRequest.getTenantId() != null){
			formData.setTenantId(formDataRequest.getTenantId());
		}
		return formDataRepository.save(formData);
	}

    @ApiOperation(value="删除表单内容", notes="根据表单内容的id来删除指定对象")
    @ApiImplicitParam(name = "id", value = "表单内容ID", required = true, dataType = "Long",paramType="path")
	@RequestMapping(value = "/form-datas/{id}", method = RequestMethod.DELETE, name="模型删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteFormData(@PathVariable Long id) {
    	FormData formData = getFormDataFromRequest(id);
		formDataRepository.delete(formData);
	}
}
