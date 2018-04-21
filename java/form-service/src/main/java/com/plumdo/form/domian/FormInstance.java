package com.plumdo.form.domian;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * FormInstance entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "form_instance", catalog = "form")
public class FormInstance extends BaseEntity {
	private static final long serialVersionUID = 1L;
	// Fields
	private FormDefinition formDefinition;
	private String businessKey;
	private Set<FormData> formDatas = new HashSet<FormData>(0);

	// Constructors

	/** default constructor */
	public FormInstance() {
	}

	/** full constructor */
	public FormInstance(FormDefinition formDefinition, Integer rev, String businessKey, Timestamp createTime, String tenantId, Timestamp lastUpdateTime, Set<FormData> formDatas) {
		this.formDefinition = formDefinition;
		this.rev = rev;
		this.businessKey = businessKey;
		this.createTime = createTime;
		this.tenantId = tenantId;
		this.lastUpdateTime = lastUpdateTime;
		this.formDatas = formDatas;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORM_DEFINTION_ID_")
	public FormDefinition getFormDefinition() {
		return this.formDefinition;
	}

	public void setFormDefinition(FormDefinition formDefinition) {
		this.formDefinition = formDefinition;
	}

	@Column(name = "BUSINESS_KEY_")
	public String getBusinessKey() {
		return this.businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "formInstance")
	public Set<FormData> getFormDatas() {
		return this.formDatas;
	}

	public void setFormDatas(Set<FormData> formDatas) {
		this.formDatas = formDatas;
	}

	public void addFormData(FormData formData) {
		formDatas.add(formData);
	}

}