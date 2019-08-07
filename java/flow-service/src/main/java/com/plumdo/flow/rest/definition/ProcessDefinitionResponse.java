package com.plumdo.flow.rest.definition;

import lombok.Data;

/**
 * 流程定义结果类
 *
 * @author wengwh
 * @date 2018/12/6
 */
@Data
public class ProcessDefinitionResponse {
    private String id;
    private String key;
    private int version;
    private String name;
    private String description;
    private String tenantId;
    private String category;
    private String formKey;
    private boolean graphicalNotationDefined = false;
    private boolean suspended = false;

}
