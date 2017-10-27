package com.plumdo.form.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * SystemMenu entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "system_menu")
public class SystemMenu  extends BaseEntity {

	
	private static final long serialVersionUID = 1L;
	
	private SystemMenu systemMenu;
	private String menuTitle;
	private Integer menuSerial;
	private String menuUrl;
	private String menuIcon;
	private Set<SystemMenu> systemMenus = new HashSet<SystemMenu>(0);


	/** default constructor */
	public SystemMenu() {
	}

	/** full constructor */
	public SystemMenu(SystemMenu systemMenu, Integer rev, String menuTitle, Integer menuSerial, String menuUrl, String menuIcon, String tenantId, Timestamp createTime, Timestamp lastUpdateTime, Set<SystemMenu> systemMenus) {
		this.systemMenu = systemMenu;
		this.rev = rev;
		this.menuTitle = menuTitle;
		this.menuSerial = menuSerial;
		this.menuUrl = menuUrl;
		this.menuIcon = menuIcon;
		this.tenantId = tenantId;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
		this.systemMenus = systemMenus;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id_")
	public SystemMenu getSystemMenu() {
		return this.systemMenu;
	}

	public void setSystemMenu(SystemMenu systemMenu) {
		this.systemMenu = systemMenu;
	}


	@Column(name = "menu_title_", length = 50)
	public String getMenuTitle() {
		return this.menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	@Column(name = "menu_serial_")
	public Integer getMenuSerial() {
		return this.menuSerial;
	}

	public void setMenuSerial(Integer menuSerial) {
		this.menuSerial = menuSerial;
	}

	@Column(name = "menu_url_", length = 100)
	public String getMenuUrl() {
		return this.menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	@Column(name = "menu_icon_", length = 100)
	public String getMenuIcon() {
		return this.menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "systemMenu")
	public Set<SystemMenu> getSystemMenus() {
		return this.systemMenus;
	}

	public void setSystemMenus(Set<SystemMenu> systemMenus) {
		this.systemMenus = systemMenus;
	}

}