package com.plumdo.flow.rest.instance.resource;

import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.constant.ErrorConstant;

/**
 * 流程实例挂起接口
 *
 * @author wengwh
 * @date 2018/12/6
 */
@RestController
public class ProcessInstanceSuspendResource extends BaseProcessInstanceResource {

    @PutMapping(value = "/process-instances/{processInstanceId}/suspend", name = "流程实例挂起")
    @ResponseStatus(value = HttpStatus.OK)
    public void suspendProcessInstance(@PathVariable String processInstanceId) {
        ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);

        if (processInstance.isSuspended()) {
            exceptionFactory.throwConflict(ErrorConstant.INSTANCE_ALREADY_SUSPEND, processInstance.getId());
        }
        runtimeService.suspendProcessInstanceById(processInstance.getId());
    }
}
