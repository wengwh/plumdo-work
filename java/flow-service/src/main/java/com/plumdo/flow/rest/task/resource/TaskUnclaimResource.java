package com.plumdo.flow.rest.task.resource;

import org.flowable.task.api.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务取消认领接口
 *
 * @author wengwh
 * @date 2018年4月23日
 */
@RestController
public class TaskUnclaimResource extends BaseTaskResource {

    @PutMapping(value = "/tasks/{taskId}/unclaim", name = "任务取消认领")
    @ResponseStatus(value = HttpStatus.OK)
    public void claimTask(@PathVariable("taskId") String taskId) {
        Task task = getTaskFromRequest(taskId);
        taskService.unclaim(task.getId());
    }
}
