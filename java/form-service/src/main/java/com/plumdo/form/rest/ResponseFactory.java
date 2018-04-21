package com.plumdo.form.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.plumdo.form.domian.FormData;
import com.plumdo.form.domian.FormDefinition;
import com.plumdo.form.domian.FormInstance;
import com.plumdo.form.domian.FormModel;
import com.plumdo.form.rest.definition.FormDefinitionResponse;
import com.plumdo.form.rest.instance.FormInstanceResponse;
import com.plumdo.form.rest.model.FormModelResponse;

/**
 * rest接口返回结果工厂类
 * 
 * @author wengwh
 * 
 */
@Component
public class ResponseFactory {

	public ResponseFactory() {
	}

	private <T, I> PageResponse<I> createPageResponse(Page<T> page) {
		PageResponse<I> pageResponse = new PageResponse<I>();
		pageResponse.setPageTotal(page.getTotalPages());
		pageResponse.setPageNum(page.getNumber() + 1);
		pageResponse.setPageSize(page.getSize());
		pageResponse.setDataTotal(page.getTotalElements());
		pageResponse.setStartNum(page.getNumber() * page.getSize() + 1);
		pageResponse.setEndNum(page.isLast() ? page.getTotalElements() : (page.getNumber() + 1) * page.getSize());
		return pageResponse;
	}

	public PageResponse<FormModelResponse> createFormModelPageResponse(Page<FormModel> formModels) {
		PageResponse<FormModelResponse> pageResponse = createPageResponse(formModels);
		pageResponse.setData(createFormModelResponseList(formModels.getContent()));
		return pageResponse;
	}

	public List<FormModelResponse> createFormModelResponseList(List<FormModel> formModels) {
		List<FormModelResponse> responseList = new ArrayList<FormModelResponse>();
		for (FormModel instance : formModels) {
			responseList.add(createFormModelResponse(instance));
		}
		return responseList;
	}

	public FormModelResponse createFormModelResponse(FormModel formModel) {
		FormModelResponse response = new FormModelResponse();
		response.setBaseEntity(formModel);
		response.setCategory(formModel.getCategory());
		response.setKey(formModel.getKey());
		response.setName(formModel.getName());
		return response;
	}

	public PageResponse<FormDefinitionResponse> createFormDefinitionPageResponse(Page<FormDefinition> formDefinitions) {
		PageResponse<FormDefinitionResponse> pageResponse = createPageResponse(formDefinitions);
		pageResponse.setData(createFormDefinitionResponseList(formDefinitions.getContent()));
		return pageResponse;
	}

	public List<FormDefinitionResponse> createFormDefinitionResponseList(List<FormDefinition> formDefinitions) {
		List<FormDefinitionResponse> responseList = new ArrayList<FormDefinitionResponse>();
		for (FormDefinition instance : formDefinitions) {
			responseList.add(createFormDefinitionResponse(instance));
		}
		return responseList;
	}

	public FormDefinitionResponse createFormDefinitionResponse(FormDefinition formDefinition) {
		FormDefinitionResponse response = new FormDefinitionResponse();
		response.setBaseEntity(formDefinition);
		response.setCategory(formDefinition.getCategory());
		response.setKey(formDefinition.getKey());
		response.setName(formDefinition.getName());
		response.setVersion(formDefinition.getVersion());
		response.setDescription(formDefinition.getDescription());
		return response;
	}
	
	
	public FormInstanceResponse createFormInstanceResponse(FormInstance formInstance) {
		FormInstanceResponse response = new FormInstanceResponse();
		response.setBaseEntity(formInstance);
		response.setBusinessKey(formInstance.getBusinessKey());
		for(FormData formData : formInstance.getFormDatas()){
			response.addFormData(formData);
		}
		return response;
	}

}