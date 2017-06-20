package com.plumdo.form.rest.instance;

import java.util.HashSet;
import java.util.Set;

import com.plumdo.form.rest.BaseRequest;

import io.swagger.annotations.ApiModelProperty;


public class FormInstanceRequest extends BaseRequest{
	@ApiModelProperty(value = "业务Key")
	private String businessKey;
	@ApiModelProperty(value = "表单数据集合")
	private Set<FormInstanceData> formDatas = new HashSet<FormInstanceData>(0);
	
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public Set<FormInstanceData> getFormDatas() {
		return formDatas;
	}
	public void setFormDatas(Set<FormInstanceData> formDatas) {
		this.formDatas = formDatas;
	}

}