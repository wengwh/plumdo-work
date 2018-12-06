package com.plumdo.flow.rest.variable;


/**
 * 流程变量转换接口
 *
 * @author wengwh
 * @date 2018/12/6
 */
public interface RestVariableConverter {

    /**
     * Simple type-name used by this converter.
     */
    String getRestTypeName();

    /**
     * Type of variables this converter is able to convert.
     */
    Class<?> getVariableType();

    /**
     * Extract the variable value to be used in the engine from the given {@link RestVariable}.
     */
    Object getVariableValue(RestVariable result);

    /**
     * Converts the given value and sets the converted value in the given {@link RestVariable}.
     */
    void convertVariableValue(Object variableValue, RestVariable result);
}
