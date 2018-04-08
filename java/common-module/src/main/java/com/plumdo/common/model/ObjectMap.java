package com.plumdo.common.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plumdo.common.utils.ObjectUtils;


/**
 * 转换map
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
public class ObjectMap extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public static ObjectMap of(String k1, Object v1) {
		return new ObjectMap(k1, v1);
	}
	
	public static ObjectMap of(String k1, Object v1, String k2, Object v2) {
		ObjectMap objectMap = new ObjectMap();
		objectMap.put(k1, v1);
		objectMap.put(k2, v2);
	    return objectMap;
	}
	
	public static ObjectMap of(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
		ObjectMap objectMap = new ObjectMap();
		objectMap.put(k1, v1);
		objectMap.put(k2, v2);
		objectMap.put(k3, v3);
	    return objectMap;
	}
	
	public ObjectMap() {
	}

	public ObjectMap(String key, Object obj) {
		put(key, obj);
	}

	public ObjectMap(Map<String, Object> map) {
		putAll(map);
	}

	public ObjectMap(Map<String, Object> map, boolean lowerCase) {
		putAll(map);
	}

	private Object getKey(Object key) {
		return key;
	}

	@Override
	public Object get(Object key) {
		return super.get(getKey(key));
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(getKey(key));
	}

	@Override
	public Object remove(Object key) {
		return super.remove(getKey(key));
	}

	@Override
	public Object put(String key, Object value) {
		return super.put((String) getKey(key), value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		for (Map.Entry<? extends String, ? extends Object> e : m.entrySet())
			put(e.getKey(), e.getValue());
	}

	public List<Object> getAsObjectList(String pStr) {
		return ObjectUtils.convertToList(get(pStr));
	}

	public List<ObjectMap> getAsList(String pStr) {
		return ObjectUtils.convertToObjectMapList(get(pStr));
	}

	public ObjectMap getAsObjectMap(String pStr) {
		return ObjectUtils.convertToObjectMap(get(pStr));
	}
	
	public String getAsString(String pStr, String defaultVal) {
		return ObjectUtils.convertToString(get(pStr), defaultVal);
	}

	public String getAsString(String pStr) {
		return ObjectUtils.convertToString(get(pStr));
	}

	public Boolean getAsBoolean(String pStr, Boolean defaultVal) {
		return ObjectUtils.convertToBoolean(get(pStr), defaultVal);
	}

	public Boolean getAsBoolean(String pStr) {
		return ObjectUtils.convertToBoolean(get(pStr));
	}
	
	public Integer getAsInteger(String pStr, Integer defaultVal) {
		return ObjectUtils.convertToInteger(get(pStr), defaultVal);
	}

	public Integer getAsInteger(String pStr) {
		return ObjectUtils.convertToInteger(get(pStr));
	}

	public Byte getAsByte(String pStr, Byte defaultVal) {
		return ObjectUtils.convertToByte(get(pStr), defaultVal);
	}

	public Byte getAsByte(String pStr) {
		return ObjectUtils.convertToByte(get(pStr));
	}
	
	public Short getAsShort(String pStr, Short defaultVal) {
		return ObjectUtils.convertToShort(get(pStr), defaultVal);
	}

	public Short getAsShort(String pStr) {
		return ObjectUtils.convertToShort(get(pStr));
	}

	public Float getAsFloat(String pStr, Float defaultVal) {
		return ObjectUtils.convertToFloat(get(pStr), defaultVal);
	}

	public Float getAsFloat(String pStr) {
		return ObjectUtils.convertToFloat(get(pStr));
	}

	public Long getAsLong(String pStr, Long defaultVal) {
		return ObjectUtils.convertToLong(get(pStr), defaultVal);
	}

	public Long getAsLong(String pStr) {
		return ObjectUtils.convertToLong(get(pStr));
	}

	public Double getAsDouble(String pStr, Double defaultVal) {
		return ObjectUtils.convertToDouble(get(pStr), defaultVal);
	}

	public Double getAsDouble(String pStr) {
		return ObjectUtils.convertToDouble(get(pStr));
	}

	public BigDecimal getAsBigDecimal(String pStr, BigDecimal defaultVal) {
		return ObjectUtils.convertToBigDecimal(get(pStr), defaultVal);
	}

	public BigDecimal getAsBigDecimal(String pStr) {
		return ObjectUtils.convertToBigDecimal(get(pStr));
	}

	public Timestamp getAsTimestamp(String pStr, Timestamp defaultVal) {
		return ObjectUtils.convertToTimestamp(get(pStr), defaultVal);
	}

	public Timestamp getAsTimestamp(String pStr) {
		return ObjectUtils.convertToTimestamp(get(pStr));
	}
	
	public Date getAsDate(String pStr, Date defaultVal) {
		return ObjectUtils.convertToDate(get(pStr), defaultVal);
	}

	public Date getAsDate(String pStr) {
		return ObjectUtils.convertToDate(get(pStr));
	}

}
