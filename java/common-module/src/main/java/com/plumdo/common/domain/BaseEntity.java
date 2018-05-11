package com.plumdo.common.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;


@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public abstract class BaseEntity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected Integer id;
	protected Integer rev;
	protected Timestamp createTime;
	protected Timestamp lastUpdateTime;
	protected String tenantId;

	@PrePersist
	public void prePersist() {
		this.createTime = new Timestamp(System.currentTimeMillis());
		this.lastUpdateTime = this.createTime;
	}

	@PreUpdate
	public void preUpdate() {
		this.lastUpdateTime = new Timestamp(System.currentTimeMillis());
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID_", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Version
	@Column(name = "REV_")
	public Integer getRev() {
		return rev;
	}

	public void setRev(Integer rev) {
		this.rev = rev;
	}

	@Column(name = "CREATE_TIME_", length = 19)
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "LAST_UPDATE_TIME_", length = 19)
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Column(name = "TENANT_ID_")
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}