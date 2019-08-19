package com.plumdo.flow.rest.task;

import lombok.Data;
import org.flowable.task.api.DelegationState;

import java.util.Date;

/**
 * 任务结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
@Data
public class TaskResponse {
    protected String id;
    protected String name;
    protected String assignee;
    protected String owner;
    protected String delegationState;
    protected boolean suspended;
    protected String description;
    protected String category;
    protected Date createTime;
    protected Date dueDate;
    protected Integer priority;
    protected String taskDefinitionKey;
    protected String parentTaskId;
    protected String formKey;
    protected String tenantId;
    protected String processDefinitionId;
    protected String processDefinitionName;
    protected String processDefinitionKey;
    protected Integer processDefinitionVersion;
    protected String processInstanceId;

    public void setDelegationState(DelegationState delegationState) {
        this.delegationState = getDelegationStateString(delegationState);
    }

    protected String getDelegationStateString(DelegationState state) {
        String result = null;
        if (state != null) {
            result = state.toString().toLowerCase();
        }
        return result;
    }


}
