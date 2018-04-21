package com.plumdo.form.domian;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * FormModel entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "form_model")
public class FormModel extends BaseEntity {

	private static final long serialVersionUID = 1L;
	// Fields
	private String name;
	private String key;
	private String category;
	private byte[] editorSourceBytes;

	// Constructors

	/** default constructor */
	public FormModel() {
	}

	/** full constructor */
	public FormModel(String name, String key, String category, byte[] editorSourceBytes, String tenantId) {
		this.name = name;
		this.key = key;
		this.category = category;
		this.editorSourceBytes = editorSourceBytes;
		this.tenantId = tenantId;
	}

	@Column(name = "NAME_")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "KEY_")
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "CATEGORY_")
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name = "EDITOR_SOURCE_BYTES_")
	public byte[] getEditorSourceBytes() {
		return this.editorSourceBytes;
	}

	public void setEditorSourceBytes(byte[] editorSourceBytes) {
		this.editorSourceBytes = editorSourceBytes;
	}

}