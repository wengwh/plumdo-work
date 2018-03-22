package com.plumdo.identity.domain;

import javax.persistence.*;

import com.plumdo.common.domain.BaseEntity;


/**
 * The persistent class for the pw_id_role database table.
 * 
 */
@Entity
@Table(name="pw_id_role", catalog="plumdo_identity")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String name;
	private String remark;

	public Role() {
	}

	@Column(name="name_", nullable=false, length=64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(name="remark_", length=500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}