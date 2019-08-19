package com.plumdo.flow.rest.task;

import lombok.Data;

import java.util.Date;

/**
 * 历史任务结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
@Data
public class HistoricTaskResponse {
    protected String id;
    protected String processDefinitionId;
    protected String processInstanceId;
    protected String name;
    protected String description;
    protected String owner;
    protected String assignee;
    protected Date startTime;
    protected Date endTime;
    protected Long durationInMillis;
    protected Long workTimeInMillis;
    protected Date claimTime;
    protected String taskDefinitionKey;
    protected String formKey;
    protected Integer priority;
    protected Date dueDate;
    protected String parentTaskId;
    protected String tenantId;
    protected String category;

}
