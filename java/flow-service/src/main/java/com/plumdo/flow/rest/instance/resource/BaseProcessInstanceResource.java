package com.plumdo.flow.rest.instance.resource;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.plumdo.common.resource.BaseResource;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.RestResponseFactory;

/**
 * 流程实例基础接口
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class BaseProcessInstanceResource extends BaseResource {
    @Autowired
    protected RestResponseFactory restResponseFactory;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;

    ProcessInstance getProcessInstanceFromRequest(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.INSTANCE_NOT_FOUND, processInstanceId);
        }
        return processInstance;
    }

    HistoricProcessInstance getHistoricProcessInstanceFromRequest(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.INSTANCE_NOT_FOUND, processInstanceId);
        }
        return historicProcessInstance;
    }
}
