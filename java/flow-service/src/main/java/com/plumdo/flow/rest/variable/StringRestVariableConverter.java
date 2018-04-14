package com.plumdo.flow.rest.variable;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;


/**
 * @author Frederik Heremans
 */
public class StringRestVariableConverter implements RestVariableConverter {

  @Override
  public String getRestTypeName() {
    return "string";
  }

  @Override
  public Class< ? > getVariableType() {
    return String.class;
  }

  @Override
  public Object getVariableValue(RestVariable result) {
    if(result.getValue() != null) {
    	try {
			 return String.valueOf(result.getValue());
		} catch (Exception e) {
	        throw new FlowableIllegalArgumentException("Converter can only convert strings");
		}
     
    }
    return null;
  }

  @Override
  public void convertVariableValue(Object variableValue, RestVariable result) {
    if(variableValue != null) {
      if(!(variableValue instanceof String)) {
        throw new FlowableIllegalArgumentException("Converter can only convert strings");
      }
      result.setValue(variableValue);
    } else {
      result.setValue(null);
    }
  }

}
