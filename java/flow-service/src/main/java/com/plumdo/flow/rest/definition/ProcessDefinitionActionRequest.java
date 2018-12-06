package com.plumdo.flow.rest.definition;

import java.util.Date;

/**
 * 流程定义操作请求类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class ProcessDefinitionActionRequest {

    public static final String ACTION_SUSPEND = "suspend";
    public static final String ACTION_ACTIVATE = "activate";

    private boolean includeProcessInstances = false;
    private Date date;

    public void setIncludeProcessInstances(boolean includeProcessInstances) {
        this.includeProcessInstances = includeProcessInstances;
    }

    public boolean isIncludeProcessInstances() {
        return includeProcessInstances;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

}
