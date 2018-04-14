package com.plumdo.flow.rest.variable;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;


/**
 * @author Frederik Heremans
 */
public class ShortRestVariableConverter implements RestVariableConverter {

	@Override
	public String getRestTypeName() {
		return "short";
	}

	@Override
	public Class<?> getVariableType() {
		return Short.class;
	}

	@Override
	public Object getVariableValue(RestVariable result) {
		if (result.getValue() != null) {
			try {
				return Short.valueOf(String.valueOf(result.getValue()));
			} catch (Exception e) {
				throw new FlowableIllegalArgumentException("Converter can only convert shorts");
			}
		}
		return null;
	}

	@Override
	public void convertVariableValue(Object variableValue, RestVariable result) {
		if (variableValue != null) {
			if (!(variableValue instanceof Short)) {
				throw new FlowableIllegalArgumentException(
						"Converter can only convert shorts");
			}
			result.setValue(variableValue);
		} else {
			result.setValue(null);
		}
	}

}
