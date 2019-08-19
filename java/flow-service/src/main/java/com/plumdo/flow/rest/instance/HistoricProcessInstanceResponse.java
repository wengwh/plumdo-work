package com.plumdo.flow.rest.instance;

import lombok.Data;

import java.util.Date;

/**
 * 历史流程实例结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
@Data
public class HistoricProcessInstanceResponse {
    protected String id;
    private String businessKey;
    protected String processDefinitionId;
    private String processDefinitionName;
    private String processDefinitionKey;
    private Integer processDefinitionVersion;
    private Date startTime;
    private Date endTime;
    private Long durationInMillis;
    private String startUserId;
    private String startActivityId;
    private String superProcessInstanceId;
    protected String tenantId;

}
