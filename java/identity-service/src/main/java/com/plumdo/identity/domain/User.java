package com.plumdo.identity.domain;

import javax.persistence.*;

import com.plumdo.common.domain.BaseEntity;

/**
 * The persistent class for the pw_id_user database table.
 * 
 */
@Entity
@Table(name = "pw_id_user", catalog="plumdo_identity")
@NamedQuery(name = "User.findAll", query = "SELECT s FROM User s")
public class User extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String account;
	private String avatar;
	private String email;
	private String name;
	private String phone;
	private String pwd;
	private String remark;
	private byte sex;
	private byte status;

	public User() {
	}

	@Column(name = "account_", nullable = false, length = 64)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "avatar_", nullable = false, length = 255)
	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Column(name = "email_", length = 64)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "name_", nullable = false, length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "phone_", length = 64)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "pwd_", nullable = false, length = 64)
	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Column(name = "remark_", length = 500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "sex_", nullable = false)
	public byte getSex() {
		return this.sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	@Column(name = "status_", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}