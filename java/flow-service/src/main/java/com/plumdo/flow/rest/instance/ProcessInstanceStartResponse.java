package com.plumdo.flow.rest.instance;

import java.util.List;
import java.util.Map;

/**
 * 流程实例启动方结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class ProcessInstanceStartResponse {
    protected String id;
    protected String businessKey;
    protected String processDefinitionId;
    protected String currentActivityId;
    protected String tenantId;
    protected List<Map<String, String>> taskInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getCurrentActivityId() {
        return currentActivityId;
    }

    public void setCurrentActivityId(String currentActivityId) {
        this.currentActivityId = currentActivityId;
    }

    public List<Map<String, String>> getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(List<Map<String, String>> taskInfo) {
        this.taskInfo = taskInfo;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }

}
