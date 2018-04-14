package com.plumdo.identity.domain;

import javax.persistence.*;

import com.plumdo.common.domain.BaseEntity;

/**
 * The persistent class for the pw_id_group database table.
 * 
 */
@Entity
@Table(name = "pw_id_group", catalog = "plumdo_identity")
@NamedQuery(name = "Group.findAll", query = "SELECT d FROM Group d")
public class Group extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String name;
	private byte type;
	private int order;
	private int parentId;
	private byte status;
	private String remark;

	public Group() {
	}

	@Column(name = "name_", nullable = false, length = 255)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "type_", nullable = false)
	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}
	
	@Column(name = "order_", nullable = false)
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Column(name = "parent_id_", nullable = false)
	public int getParentId() {
		return this.parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Column(name = "status_", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Column(name = "remark_", length = 500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}