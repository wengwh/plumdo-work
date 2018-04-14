package com.plumdo.form.rest.instance.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;









import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;









import com.plumdo.form.entity.FormData;
import com.plumdo.form.entity.FormInstance;
import com.plumdo.form.entity.FormModel;
import com.plumdo.form.jpa.Criteria;
import com.plumdo.form.jpa.Restrictions;
import com.plumdo.form.repository.FormDataRepository;
import com.plumdo.form.repository.FormInstanceRepository;
import com.plumdo.form.rest.instance.FormInstanceData;
import com.plumdo.form.rest.instance.FormInstanceRequest;
import com.plumdo.form.rest.instance.FormInstanceResponse;

@Api(tags="表单实例")
@RestController
public class FormInstanceResource extends BaseFormInstanceResource{
	@Autowired
	private FormDataRepository formDataRepository;
	
	@ApiOperation(value="查询表单实例详情", notes="根据表单实例的id来获取指定对象")
    @ApiImplicitParam(name = "id", value = "表单实例ID", required = true, dataType = "Long",paramType="path")
	@RequestMapping(value = "/form-instances/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(value = HttpStatus.OK)
	public FormInstanceResponse getFormInstance(@PathVariable Long id) {
		FormInstance formInstance = getFormInstanceFromRequest(id);
		return responseFactory.createFormInstanceResponse(formInstance);
	}
	
	
	@ApiOperation(value = "创建表单模型", notes = "根据传过来的modelRequest信息来创建表单模型")
	@ApiImplicitParam(name = "modelRequest", value = "表单模型请求实体modelRequest", required = true, dataType = "FormInstanceRequest")
	@RequestMapping(value = "/form-instances/{id}", method = RequestMethod.PUT, produces = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public FormInstanceResponse updateFormInstance(@PathVariable Long id,@RequestBody FormInstanceRequest formInstanceRequest) {
		FormInstance formInstance = getFormInstanceFromRequest(id);
		if (formInstanceRequest.getBusinessKey() != null) {
			formInstance.setBusinessKey(formInstanceRequest.getBusinessKey());
		}
		if (formInstanceRequest.getTenantId() != null) {
			formInstance.setTenantId(formInstanceRequest.getTenantId());
		}
		
		if (formInstanceRequest.getFormDatas() != null) {
			for(FormInstanceData formInstanceData : formInstanceRequest.getFormDatas()){
				Criteria<FormData> criteria = new Criteria<FormData>();
				criteria.add(Restrictions.eq("key", formInstanceData.getKey(),false));
				criteria.add(Restrictions.eq("formInstance.id", id,false));
				FormData oldFormData = formDataRepository.findOne(criteria);
				if(oldFormData == null){
					FormData formData = new FormData();
					formData.setFormInstance(formInstance);
					formData.setKey(formInstanceData.getKey());
					formData.setValue(formInstanceData.getValue());
					formInstance.addFormData(formData);
				}else{
					oldFormData.setValue(formInstanceData.getValue());
					formInstance.addFormData(oldFormData);
				}
			}
		}
		
		formInstanceRepository.save(formInstance);

		return responseFactory.createFormInstanceResponse(formInstance);
	}
	
}
