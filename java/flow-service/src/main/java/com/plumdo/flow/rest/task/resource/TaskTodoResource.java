package com.plumdo.flow.rest.task.resource;

import com.plumdo.common.model.Authentication;
import com.plumdo.common.resource.PageResponse;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.task.*;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.impl.TaskQueryProperty;
import org.flowable.task.service.impl.TaskQueryProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务基础接口
 *
 * @author wengwh
 * @date 2018/12/6
 */
@RestController
public class TaskTodoResource extends BaseTaskResource {

    private static Map<String, QueryProperty> allowedSortProperties = new HashMap<>();

    static {
        allowedSortProperties.put("processDefinitionId", TaskQueryProperty.PROCESS_DEFINITION_ID);
        allowedSortProperties.put("processInstanceId", TaskQueryProperty.PROCESS_INSTANCE_ID);
        allowedSortProperties.put("taskDefinitionKey", TaskQueryProperty.TASK_DEFINITION_KEY);
        allowedSortProperties.put("dueDate", TaskQueryProperty.DUE_DATE);
        allowedSortProperties.put("name", TaskQueryProperty.NAME);
        allowedSortProperties.put("priority", TaskQueryProperty.PRIORITY);
        allowedSortProperties.put("tenantId", TaskQueryProperty.TENANT_ID);
        allowedSortProperties.put("createTime", TaskQueryProperty.CREATE_TIME);
    }

    @GetMapping(value = "/tasks/todo", name = "待办任务查询")
    public PageResponse getTasks(@RequestParam Map<String, String> requestParams) {
        TaskQuery query = taskService.createTaskQuery();

        if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceId"))) {
            query.processInstanceId(requestParams.get("processInstanceId"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processInstanceBusinessKey"))) {
            query.processInstanceBusinessKeyLike(ObjectUtils.convertToLike(requestParams.get("processInstanceBusinessKey")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionKey"))) {
            query.processDefinitionKeyLike(ObjectUtils.convertToLike(requestParams.get("processDefinitionKey")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionId"))) {
            query.processDefinitionId(requestParams.get("processDefinitionId"));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("processDefinitionName"))) {
            query.processDefinitionNameLike(ObjectUtils.convertToLike(requestParams.get("processDefinitionName")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("dueDateAfter"))) {
            query.taskDueAfter(ObjectUtils.convertToDatetime(requestParams.get("dueDateAfter")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("dueDateBefore"))) {
            query.taskDueBefore(ObjectUtils.convertToDatetime(requestParams.get("dueDateBefore")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskCreatedBefore"))) {
            query.taskCreatedBefore(ObjectUtils.convertToDatetime(requestParams.get("taskCreatedBefore")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("taskCreatedAfter"))) {
            query.taskCreatedAfter(ObjectUtils.convertToDatetime(requestParams.get("taskCreatedAfter")));
        }
        if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
            query.taskTenantId(requestParams.get("tenantId"));
        }

        if (ObjectUtils.isNotEmpty(requestParams.get("suspended"))) {
            boolean isSuspended = ObjectUtils.convertToBoolean(requestParams.get("suspended"));
            if (isSuspended) {
                query.suspended();
            } else {
                query.active();
            }
        }

        query.or().taskCandidateOrAssigned(Authentication.getUserId()).taskOwner(Authentication.getUserId()).endOr();

        return new TaskTodoPaginateList(restResponseFactory).paginateList(getPageable(requestParams), query, allowedSortProperties);
    }

}
