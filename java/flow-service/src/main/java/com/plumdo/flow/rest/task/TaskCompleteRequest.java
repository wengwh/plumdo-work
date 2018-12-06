package com.plumdo.flow.rest.task;

import java.util.List;

import com.plumdo.flow.rest.variable.RestVariable;

/**
 * 任务完成请求类
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class TaskCompleteRequest {

    private List<RestVariable> variables;

    public List<RestVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<RestVariable> variables) {
        this.variables = variables;
    }

}
