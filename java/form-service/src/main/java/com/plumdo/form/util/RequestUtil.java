package com.plumdo.form.util;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;

public class RequestUtil {

	private static final FastDateFormat longDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

	public static boolean getBoolean(Map<String, String> requestParams, String name, boolean defaultValue) {
		boolean value = defaultValue;
		if (requestParams.get(name) != null) {
			value = Boolean.valueOf(requestParams.get(name));
		}
		return value;
	}

	public static int getInteger(Map<String, String> requestParams, String name, int defaultValue) {
		int value = defaultValue;
		if (requestParams.get(name) != null) {
			value = Integer.valueOf(requestParams.get(name));
		}
		return value;
	}

	public static String[] getArray(Map<String, String> requestParams, String name) {
		String[] value = new String[] {};
		if (requestParams.get(name) != null) {
			value = requestParams.get(name).split(",");
		}
		return value;
	}

	public static Date getDate(Map<String, String> requestParams, String name) {
		Date value = null;

		if (requestParams.get(name) != null) {

			String input = requestParams.get(name).trim();
			try {
				value = longDateFormat.parse(input);
			} catch (Exception e) {
				throw new RuntimeException("Failed to parse date " + input);
			}
		}
		return value;
	}

	public static String dateToString(Date date) {
		String dateString = null;
		if (date != null) {
			dateString = longDateFormat.format(date);
		}

		return dateString;
	}

	public static Integer parseToInteger(String integer) {
		Integer parsedInteger = null;
		try {
			parsedInteger = Integer.parseInt(integer);
		} catch (Exception e) {
		}
		return parsedInteger;
	}

	public static Date parseToDate(String date) {
		Date parsedDate = null;
		try {
			parsedDate = longDateFormat.parse(date);
		} catch (Exception e) {
		}
		return parsedDate;
	}
}
