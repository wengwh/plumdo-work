package com.plumdo.identity.domain;

import javax.persistence.*;

import com.plumdo.common.domain.BaseEntity;


/**
 * The persistent class for the pw_id_menu database table.
 * 
 */
@Entity
@Table(name = "pw_id_menu", catalog="plumdo_identity")
@NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m")
public class Menu extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String code;
	private String icon;
	private String url;
	private String name;
	private int order;
	private int parentId;
	private String remark;
	private byte type;

	public Menu() {
	}

	@Column(name = "code_", nullable = false, length = 64)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "icon_", nullable = false, length = 255)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "url_", length = 255)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "name_", nullable = false, length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Column(name = "remark_", length = 500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "type_", nullable = false)
	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}