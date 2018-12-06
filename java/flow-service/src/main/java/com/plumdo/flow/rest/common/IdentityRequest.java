package com.plumdo.flow.rest.common;


/**
 * 用户信息请求类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class IdentityRequest {
    private String identityId;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

}
