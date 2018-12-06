package com.plumdo.flow.rest.definition.resource;

import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import com.plumdo.common.resource.BaseResource;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.RestResponseFactory;

/**
 * 流程定义接口基类
 *
 * @author wengwenhui
 * @date 2018年4月17日
 */
public class BaseProcessDefinitionResource extends BaseResource {
    @Autowired
    protected RestResponseFactory restResponseFactory;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected ManagementService managementService;
    @Autowired
    protected RuntimeService runtimeService;

    ProcessDefinition getProcessDefinitionFromRequest(String processDefinitionId) {
        // 直接查询数据库，不查询缓存，防止出现挂起激活验证不一致
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

        if (processDefinition == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.DEFINITION_NOT_FOUND, processDefinitionId);
        }

        return processDefinition;
    }
}
