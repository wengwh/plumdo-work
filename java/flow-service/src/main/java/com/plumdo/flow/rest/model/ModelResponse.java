package com.plumdo.flow.rest.model;

import java.util.Date;

/**
 * 模型结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class ModelResponse extends ModelRequest {
    protected String id;
    protected Date createTime;
    protected Date lastUpdateTime;
    protected Boolean deployed;
    protected Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getDeployed() {
        return deployed;
    }

    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
