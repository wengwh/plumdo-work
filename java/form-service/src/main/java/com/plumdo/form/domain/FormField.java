package com.plumdo.form.domain;

import com.plumdo.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the pw_fo_field database table.
 */
@Entity
@Table(name = "pw_fo_field", catalog = "plumdo_form")
@NamedQuery(name = "FormField.findAll", query = "SELECT f FROM FormField f")
public class FormField extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String key;
    private String name;
    private String remark;
    private int tableId;

    public FormField() {
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

    @Column(name = "table_id_")
    public int getTableId() {
        return this.tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

}