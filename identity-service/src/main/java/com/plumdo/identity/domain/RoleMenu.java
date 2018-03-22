package com.plumdo.identity.domain;

import javax.persistence.*;

import com.plumdo.common.domain.BaseEntity;


/**
 * The persistent class for the pw_id_role_menu database table.
 * 
 */
@Entity
@Table(name="pw_id_role_menu", catalog="plumdo_identity")
@NamedQuery(name="RoleMenu.findAll", query="SELECT r FROM RoleMenu r")
public class RoleMenu extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private int menuId;
	private int roleId;

	public RoleMenu() {
	}

	@Column(name="menu_id_", nullable=false)
	public int getMenuId() {
		return this.menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}


	@Column(name="role_id_", nullable=false)
	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}



}