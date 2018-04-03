package com.plumdo.form.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * FormData entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "form_data")
public class FormData extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private FormInstance formInstance;
	private String key;
	private String value;

	// Constructors

	/** default constructor */
	public FormData() {
	}

	/** full constructor */
	public FormData(FormInstance formInstance, Timestamp createTime, Timestamp lastUpdateTime, Integer rev, String tenantId, String key, String value) {
		this.formInstance = formInstance;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
		this.rev = rev;
		this.tenantId = tenantId;
		this.key = key;
		this.value = value;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_instance_id_")
	public FormInstance getFormInstance() {
		return this.formInstance;
	}

	public void setFormInstance(FormInstance formInstance) {
		this.formInstance = formInstance;
	}

	@Column(name = "KEY_")
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "VALUE_")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}