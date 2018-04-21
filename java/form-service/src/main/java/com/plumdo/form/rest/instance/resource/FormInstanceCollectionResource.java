package com.plumdo.form.rest.instance.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

import com.plumdo.form.domian.FormData;
import com.plumdo.form.domian.FormInstance;
import com.plumdo.form.jpa.Criteria;
import com.plumdo.form.jpa.Restrictions;
import com.plumdo.form.rest.PageResponse;
import com.plumdo.form.rest.instance.FormInstanceData;
import com.plumdo.form.rest.instance.FormInstanceRequest;
import com.plumdo.form.rest.instance.FormInstanceResponse;

@Api(tags="表单定义")
@RestController
public class FormInstanceCollectionResource extends BaseFormInstanceResource{

	
	@ApiOperation(value = "创建表单模型", notes = "根据传过来的modelRequest信息来创建表单模型")
	@ApiImplicitParam(name = "modelRequest", value = "表单模型请求实体modelRequest", required = true, dataType = "FormInstanceRequest")
	@RequestMapping(value = "/form-instances", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public FormInstanceResponse createFormInstance(@RequestBody FormInstanceRequest formInstanceRequest) {
		FormInstance formInstance = new FormInstance();
		if (formInstanceRequest.getBusinessKey() != null) {
			formInstance.setBusinessKey(formInstanceRequest.getBusinessKey());
		}
		if (formInstanceRequest.getTenantId() != null) {
			formInstance.setTenantId(formInstanceRequest.getTenantId());
		}
		
		if (formInstanceRequest.getFormDatas() != null) {
			for(FormInstanceData formInstanceData : formInstanceRequest.getFormDatas()){
				FormData formData = new FormData();
				formData.setFormInstance(formInstance);
				formData.setKey(formInstanceData.getKey());
				formData.setValue(formInstanceData.getValue());
				formInstance.addFormData(formData);
			}
		}
		
		formInstanceRepository.save(formInstance);

		return responseFactory.createFormInstanceResponse(formInstance);
	}
	
}
