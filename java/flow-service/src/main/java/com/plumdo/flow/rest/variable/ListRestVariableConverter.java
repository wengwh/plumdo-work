package com.plumdo.flow.rest.variable;

import java.util.List;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 集合类型变量
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class ListRestVariableConverter implements RestVariableConverter {

    @Override
    public String getRestTypeName() {
        return "list";
    }

    @Override
    public Class<?> getVariableType() {
        return List.class;
    }

    @SuppressWarnings({"rawtypes"})
    @Override
    public Object getVariableValue(RestVariable result) {
        if (result.getValue() != null) {
            ObjectMapper mapper = new ObjectMapper();
            List list = null;
            try {
                list = mapper.readValue(String.valueOf(result.getValue()), List.class);
            } catch (Exception e) {
                throw new FlowableIllegalArgumentException("Converter to list error", e);
            }
            return list;
        }
        return null;
    }

    @Override
    public void convertVariableValue(Object variableValue, RestVariable result) {
        if (variableValue != null) {
            if (!(variableValue instanceof List)) {
                throw new FlowableIllegalArgumentException("Converter can only convert List");
            }
            result.setValue(variableValue);
        } else {
            result.setValue(null);
        }
    }

}
