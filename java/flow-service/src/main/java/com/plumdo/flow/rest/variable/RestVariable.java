package com.plumdo.flow.rest.variable;

/**
 * 流程变量
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class RestVariable {

    private String name;
    private String type;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
