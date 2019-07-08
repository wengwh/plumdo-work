package com.plumdo.form.request;

import lombok.Data;

import java.util.Map;

/**
 * 表达实例请求类
 *
 * @author wengwh
 * @date 2018/12/20
 */
@Data
public class FormInstanceCreateRequest {
    private Integer formDefinitionId;
    private String formDefinitionKey;
    private String tenantId;
    private Map<String, String> formData;

}
