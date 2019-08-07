package com.plumdo.flow.rest.form.resource;

import com.google.common.collect.ImmutableMap;
import org.flowable.engine.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 流程表单的接口
 *
 * @author wengwh
 * @date 2019/8/7
 */
@RestController
public class FormResource {

    private final FormService formService;

    @Autowired
    public FormResource(FormService formService) {
        this.formService = formService;
    }


    @GetMapping(value = "/process-forms/{processDefinitionId}/key", name = "获取流程启动的表单key")
    public Map<String, String> getStartFormKey(@PathVariable String processDefinitionId) {
        String key = formService.getStartFormKey(processDefinitionId);
        return ImmutableMap.of("key", key);
    }


    @GetMapping(value = "/process-forms/{processDefinitionId}/{taskDefinitionKey}/key", name = "获取流程任务的表单key")
    public Map<String, String> getTaskFormKey(@PathVariable String processDefinitionId, @PathVariable String taskDefinitionKey) {
        String key = formService.getTaskFormKey(processDefinitionId, taskDefinitionKey);
        return ImmutableMap.of("key", key);
    }

}
