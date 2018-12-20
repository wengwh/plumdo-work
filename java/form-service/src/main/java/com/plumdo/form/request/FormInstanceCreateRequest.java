package com.plumdo.form.request;

import lombok.Data;

import java.util.Map;

/**
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
