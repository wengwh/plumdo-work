package com.plumdo.flow.rest.definition.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.impl.ProcessDefinitionQueryProperty;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.job.api.Job;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.common.resource.PageResponse;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.definition.ProcessDefinitionResponse;
import com.plumdo.flow.rest.definition.ProcessDefinitionsPaginateList;

/**
 * 流程定义基础接口类
 *
 * @author wengwenhui
 * @date 2018年4月17日
 */
@RestController
public class ProcessDefinitionResource extends BaseProcessDefinitionResource {

    private static final Map<String, QueryProperty> ALLOWED_SORT_PROPERTIES = new HashMap<>();

    static {
        ALLOWED_SORT_PROPERTIES.put("id", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_ID);
        ALLOWED_SORT_PROPERTIES.put("key", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_KEY);
        ALLOWED_SORT_PROPERTIES.put("category", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_CATEGORY);
        ALLOWED_SORT_PROPERTIES.put("name", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_NAME);
        ALLOWED_SORT_PROPERTIES.put("version", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION);
        ALLOWED_SORT_PROPERTIES.put("tenantId", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_TENANT_ID);
    }

    @GetMapping(value = "/process-definitions", name = "流程定义查询")
    public PageResponse getProcessDefinitions(@RequestParam Map<String, String> requestParams) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (ObjectUtils.isNotEmpty(requestParams.get("id"))) {
            processDefinitionQuery.processDefinitionId(requestParams.get("id"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("category"))) {
            processDefinitionQuery.processDefinitionCategoryLike(ObjectUtils.convertToLike(requestParams.get("category")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("key"))) {
            processDefinitionQuery.processDefinitionKeyLike(ObjectUtils.convertToLike(requestParams.get("key")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("name"))) {
            processDefinitionQuery.processDefinitionNameLike(ObjectUtils.convertToLike(requestParams.get("name")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("version"))) {
            processDefinitionQuery.processDefinitionVersion(ObjectUtils.convertToInteger(requestParams.get("version")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("suspended"))) {
            boolean suspended = ObjectUtils.convertToBoolean(requestParams.get("suspended"));
            if (suspended) {
                processDefinitionQuery.suspended();
            } else {
                processDefinitionQuery.active();
            }
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("latestVersion"))) {
            boolean latest = ObjectUtils.convertToBoolean(requestParams.get("latestVersion"));
            if (latest) {
                processDefinitionQuery.latestVersion();
            }
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("startableByUser"))) {
            processDefinitionQuery.startableByUser(requestParams.get("startableByUser"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
            processDefinitionQuery.processDefinitionTenantId(requestParams.get("tenantId"));
        }

        return new ProcessDefinitionsPaginateList(restResponseFactory).paginateList(getPageable(requestParams), processDefinitionQuery, ALLOWED_SORT_PROPERTIES);
    }

    @GetMapping(value = "/process-definitions/{processDefinitionId}", name = "根据ID获取流程定义")
    public ProcessDefinitionResponse getProcessDefinition(@PathVariable String processDefinitionId) {
        ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
        return restResponseFactory.createProcessDefinitionResponse(processDefinition);
    }

    @DeleteMapping(value = "/process-definitions/{processDefinitionId}", name = "流程定义删除")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteProcessDefinition(@PathVariable String processDefinitionId, @RequestParam(value = "cascade", required = false, defaultValue = "false") Boolean cascade) {
        ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

        if (processDefinition.getDeploymentId() == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.DEFINITION_DEPLOY_NOT_FOUND, processDefinitionId);
        }

        if (cascade) {
            List<Job> jobs = managementService.createTimerJobQuery().processDefinitionId(processDefinitionId).list();
            for (Job job : jobs) {
                managementService.deleteTimerJob(job.getId());
            }
            repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
        } else {
            long processCount = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).count();
            if (processCount > 0) {
                exceptionFactory.throwForbidden(ErrorConstant.DEFINITION_HAVE_INSTANCE, processDefinitionId);
            }

            long jobCount = managementService.createTimerJobQuery().processDefinitionId(processDefinitionId).count();
            if (jobCount > 0) {
                exceptionFactory.throwForbidden(ErrorConstant.DEFINITION_HAVE_TIME_JOB, processDefinitionId);
            }
            repositoryService.deleteDeployment(processDefinition.getDeploymentId());
        }
    }
}
