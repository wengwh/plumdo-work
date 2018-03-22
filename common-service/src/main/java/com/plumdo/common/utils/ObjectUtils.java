package com.plumdo.common.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Object转换类，都给予默认值
 * 
 * @author wengwenhui
 *
 */
public class ObjectUtils {

	public static boolean isGtZero(Object pObj) {
		return !isLeZeroOrNull(pObj);
	}
	
	public static boolean isGeZero(Object pObj) {
		if (pObj == null)
			return false;
		if (pObj instanceof Number) {
			if (convertToInteger(pObj) >= 0) {
				return true;
			}
		}
		return false;
	}

	public static boolean isLeZeroOrNull(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj instanceof Number) {
			if (convertToInteger(pObj) <= 0) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNotEmpty(Object pObj) {
		return !isEmpty(pObj);
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj == "")
			return true;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}

	public static String convertToString(Object obj, String defaultVal) {
		try {
			return (obj != null) ? String.valueOf(obj) : defaultVal;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static String convertToString(Object obj) {
		return convertToString(obj, "");
	}

	public static Boolean convertToBoolean(Object obj, boolean defaultVal) {
		try {
			return (obj != null) ? Boolean.parseBoolean(convertToString(obj)) : defaultVal;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static Boolean convertToBoolean(Object obj) {
		return convertToBoolean(obj, false);
	}

	public static Integer convertToInteger(Object obj, Integer defaultVal) {
		try {
			return (obj != null) ? Integer.parseInt(convertToString(obj)) : defaultVal;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static Integer convertToInteger(Object obj) {
		return convertToInteger(obj, 0);
	}

	public static Short convertToShort(Object obj, Short defaultVal) {
		try {
			return (obj != null) ? Short.parseShort(convertToString(obj)) : defaultVal;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static Short convertToShort(Object obj) {
		return convertToShort(obj, (short) 0);
	}

	public static Float convertToFloat(Object obj, Float defaultVal) {
		try {
			return (obj != null) ? Float.parseFloat(convertToString(obj)) : defaultVal;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static Float convertToFloat(Object obj) {
		return convertToFloat(obj, 0F);
	}

	public static Long convertToLong(Object obj, Long defaultVal) {
		try {
			return (obj != null) ? Long.parseLong(convertToString(obj)) : defaultVal;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static Long convertToLong(Object obj) {
		return convertToLong(obj, 0L);
	}

	public static Double convertToDouble(Object obj, Double defaultVal) {
		try {
			return (obj != null) ? Double.parseDouble(convertToString(obj)) : defaultVal;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static Double convertToDouble(Object obj) {
		return convertToDouble(obj, 0D);
	}

	public static BigDecimal convertToBigDecimal(Object obj, BigDecimal defaultVal) {
		try {
			return (obj != null) ? BigDecimal.valueOf(convertToDouble(obj)) : defaultVal;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static BigDecimal convertToBigDecimal(Object obj) {
		return convertToBigDecimal(obj, BigDecimal.ZERO);
	}

	public static Date convertToDate(Object obj, Date defaultVal) {
		return DateUtils.parseDate(convertToString(obj), defaultVal);
	}

	public static Date convertToDate(Object obj) {
		return convertToDate(obj, null);
	}
	
	public static Timestamp convertToTimestap(Object obj, Timestamp defaultVal) {
		try {
			return (obj != null) ? Timestamp.valueOf(convertToString(obj)) : defaultVal;
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static Timestamp convertToTimestap(Object obj) {
		return convertToTimestap(obj, null);
	}
	
	

	@SuppressWarnings("rawtypes")
	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)).append(separator);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}


}