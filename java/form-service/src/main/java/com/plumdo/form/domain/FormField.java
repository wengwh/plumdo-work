package com.plumdo.form.domain;

import java.util.UUID;

import javax.persistence.*;

import com.plumdo.common.domain.BaseEntity;


/**
 * The persistent class for the pw_fo_field database table.
 * 
 */
@Entity
@Table(name = "pw_fo_field", catalog = "plumdo_form")
@NamedQuery(name = "FormField.findAll", query = "SELECT f FROM FormField f")
public class FormField extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String key;
	private String name;
	private String remark;
	private byte status;
	private int tableId;
	private byte type;

	public FormField() {
	}

	@PrePersist
	public void prePersist() {
		super.prePersist();
		if(key == null || key.isEmpty()) {
			key = UUID.randomUUID().toString();
		}
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

	@Column(name = "remark_")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "status_")
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Column(name = "table_id_")
	public int getTableId() {
		return this.tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	@Column(name = "type_")
	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}