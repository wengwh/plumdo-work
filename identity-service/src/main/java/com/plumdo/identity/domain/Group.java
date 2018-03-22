package com.plumdo.identity.domain;

import javax.persistence.*;

import com.plumdo.common.domain.BaseEntity;

/**
 * The persistent class for the pw_id_group database table.
 * 
 */
@Entity
@Table(name="pw_id_group", catalog="plumdo_identity")
@NamedQuery(name="Group.findAll", query="SELECT d FROM Group d")
public class Group extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String name;
	private int order;
	private int parentId;
	private int userId;
	private byte status;

	public Group() {
	}

	@Column(name="name_", nullable=false, length=255)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(name="order_", nullable=false)
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}


	@Column(name="parent_id_", nullable=false)
	public int getParentId() {
		return this.parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Column(name="user_id_", nullable=false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	@Column(name="status_", nullable=false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}