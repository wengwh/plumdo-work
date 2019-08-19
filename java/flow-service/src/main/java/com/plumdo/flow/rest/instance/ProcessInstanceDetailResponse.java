package com.plumdo.flow.rest.instance;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程实例明细结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessInstanceDetailResponse extends HistoricProcessInstanceResponse {
    private boolean suspended;
    private String deleteReason;
    private String startUserName;

}
