package com.plumdo.flow.rest.task.resource;

import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.plumdo.common.resource.BaseResource;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.RestResponseFactory;

/**
 * 任务接口基类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class BaseTaskResource extends BaseResource {
    @Autowired
    protected RestResponseFactory restResponseFactory;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected HistoryService historyService;

    Task getTaskFromRequest(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.TASK_NOT_FOUND, taskId);
        }
        return task;
    }

    HistoricTaskInstance getHistoricTaskFromRequest(String taskId) {
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if (task == null) {
            exceptionFactory.throwObjectNotFound(ErrorConstant.TASK_NOT_FOUND, taskId);
        }
        return task;
    }

}
