package com.plumdo.form.domian;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * FormDefinition entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "form_definition")
public class FormDefinition extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String category;
	private String name;
	private String key;
	private Integer version;
	private String description;
	private byte[] editorSourceBytes;

	// Constructors

	/** default constructor */
	public FormDefinition() {
	}

	/** minimal constructor */
	public FormDefinition(String key, Integer version) {
		this.key = key;
		this.version = version;
	}

	/** full constructor */
	public FormDefinition(Integer rev, String category, String name, String key, Integer version, Timestamp createTime, String description, byte[] editorSourceBytes, String tenantId) {
		this.rev = rev;
		this.category = category;
		this.name = name;
		this.key = key;
		this.version = version;
		this.createTime = createTime;
		this.description = description;
		this.editorSourceBytes = editorSourceBytes;
		this.tenantId = tenantId;
	}

	
	@Column(name = "CATEGORY_")
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "NAME_")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "KEY_", nullable = false)
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "VERSION_", nullable = false)
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}


	@Column(name = "DESCRIPTION_", length = 4000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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