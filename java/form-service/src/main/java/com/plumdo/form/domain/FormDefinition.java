package com.plumdo.form.domain;

import javax.persistence.*;

import com.plumdo.common.domain.BaseEntity;


/**
 * The persistent class for the pw_fo_definition database table.
 * 
 */
@Entity
@Table(name = "pw_fo_definition", catalog = "plumdo_form")
@NamedQuery(name = "FormDefinition.findAll", query = "SELECT f FROM FormDefinition f")
public class FormDefinition extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String category;
	private int deploySourceId;
	private String key;
	private String name;
	private int tableId;
	private int version;

	public FormDefinition() {
	}

	@Column(name = "category_")
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "deploy_source_id_")
	public int getDeploySourceId() {
		return this.deploySourceId;
	}

	public void setDeploySourceId(int deploySourceId) {
		this.deploySourceId = deploySourceId;
	}

	@Column(name = "key_")
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "name_")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "table_id_")
	public int getTableId() {
		return this.tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	
	@Column(name = "version_")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}