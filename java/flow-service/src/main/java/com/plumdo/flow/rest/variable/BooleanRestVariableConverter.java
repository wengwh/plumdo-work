package com.plumdo.flow.rest.variable;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;


public class BooleanRestVariableConverter implements RestVariableConverter {

  @Override
  public String getRestTypeName() {
    return "boolean";
  }

  @Override
  public Class< ? > getVariableType() {
    return Boolean.class;
  }
 
  @Override
  public Object getVariableValue(RestVariable result) {
    if(result.getValue() != null) {
    	try{
    		return Boolean.valueOf(String.valueOf(result.getValue()));
    	}catch (Exception e) {
            throw new FlowableIllegalArgumentException("Converter can only convert booleans");
		}
    }
    return null;
  }

  @Override
  public void convertVariableValue(Object variableValue, RestVariable result) {
    if(variableValue != null) {
      if(!(variableValue instanceof Boolean)) {
        throw new FlowableIllegalArgumentException("Converter can only convert booleans");
      }
      result.setValue(variableValue);
    } else {
      result.setValue(null);
    }
  }

}
