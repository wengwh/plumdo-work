package com.plumdo.form.domain;

import javax.persistence.*;

import com.plumdo.common.domain.BaseEntity;


/**
 * The persistent class for the pw_fo_bytearray database table.
 * 
 */
@Entity
@Table(name = "pw_fo_bytearray", catalog = "plumdo_form")
@NamedQuery(name = "ByteArray.findAll", query = "SELECT b FROM ByteArray b")
public class ByteArray extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private byte[] contentByte;
	private String name;

	public ByteArray() {
	}

	
	@Lob
	@Column(name = "content_byte_")
	public byte[] getContentByte() {
		return this.contentByte;
	}

	public void setContentByte(byte[] contentByte) {
		this.contentByte = contentByte;
	}

	@Column(name = "name_")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}