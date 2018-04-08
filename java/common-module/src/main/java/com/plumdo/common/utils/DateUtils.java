package com.plumdo.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
public class DateUtils {
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_TIME = "HH:mm:ss";
	public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATETIME_CHINA = "yyyy年MM月dd日 HH:mm:ss";

	public static SimpleDateFormat getDateFmt() {
		return new SimpleDateFormat(FORMAT_DATE);
	}

	public static SimpleDateFormat getTimeFmt() {
		return new SimpleDateFormat(FORMAT_TIME);
	}

	public static SimpleDateFormat getDateTimeFmt() {
		return new SimpleDateFormat(FORMAT_DATETIME);
	}

	public static SimpleDateFormat getDateTimeCHFmt() {
		return new SimpleDateFormat(FORMAT_DATETIME_CHINA);
	}

	public static SimpleDateFormat getTimestampFmt() {
		return new SimpleDateFormat(FORMAT_TIMESTAMP);
	}

	public static Date newDateByDay(int day) {
		return newDateByDay(newDate(), day);
	}

	public static Date newDateByDay(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	public static Date newDateBySecond(int second) {
		return newDateBySecond(newDate(), second);
	}

	public static Date newDateBySecond(Date d, int second) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.SECOND, now.get(Calendar.SECOND) + second);
		return now.getTime();
	}

	public static Date newDate() {
		return new Date();
	}

	public static Long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static Long currentTimeSecond() {
		return System.currentTimeMillis() / 1000;
	}

	public static Timestamp currentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String currentTimestampStr() {
		return getTimestampFmt().format(currentTimestamp());
	}

	public static String currentDateTimeCHStr() {
		return getDateTimeCHFmt().format(currentTimestamp());
	}

	public static String currentDateTimeStr() {
		return getDateTimeFmt().format(currentTimestamp());
	}

	public static String currentDateStr() {
		return getDateFmt().format(currentTimestamp());
	}

	public static String currentTimeStr() {
		return getTimeFmt().format(currentTimestamp());
	}

	public static String formatTimestamp(Date date) {
		return (date != null) ? getTimestampFmt().format(date) : null;
	}

	public static String formatDateTime(Date date) {
		return (date != null) ? getDateTimeFmt().format(date) : null;
	}

	public static String formatCHDateTime(Date date) {
		return (date != null) ? getDateTimeCHFmt().format(date) : null;
	}

	public static String formatDate(Date date) {
		return (date != null) ? getDateFmt().format(date) : null;
	}

	public static String formatTime(Date date) {
		return (date != null) ? getTimeFmt().format(date) : null;
	}

	public static Date parseTime(String date) {
		return parseTime(date, null);
	}

	public static Date parseTime(String date, Date defaultDate) {
		try {
			return (date != null) ? getTimeFmt().parse(date) : defaultDate;
		} catch (Exception e) {
			return defaultDate;
		}
	}

	public static Date parseDate(String date) {
		return parseDate(date, null);
	}

	public static Date parseDate(String date, Date defaultDate) {
		try {
			return (date != null) ? getDateFmt().parse(date) : defaultDate;
		} catch (Exception e) {
			return defaultDate;
		}
	}

	public static Date parseDateTime(String date) {
		return parseDateTime(date, null);
	}

	public static Date parseDateTime(String date, Date defaultDate) {
		try {
			return (date != null) ? getDateTimeFmt().parse(date) : defaultDate;
		} catch (Exception e) {
			return defaultDate;
		}
	}

	public static Timestamp parseTimestamp(String date) {
		return parseTimestamp(date, null);
	}

	public static Timestamp parseTimestamp(String date, Timestamp defaultDate) {
		try {
			return (date != null) ? Timestamp.valueOf(date) : defaultDate;
		} catch (Exception e) {
			return defaultDate;
		}
	}

	public static String hourToTime(int hour) {
		return String.format("%02d:00:00", hour);
	}

	public static long getTimeMillisConsume(long begintime) {
		return System.currentTimeMillis() - begintime;
	}

	public static long getTimeMillisConsume(Date begintime) {
		return getTimeMillisConsume(begintime.getTime());
	}

	public static long getTimeSecondConsume(Date begintime) {
		return getTimeMillisConsume(begintime) / 1000;
	}

	public static long getTimeMinuteConsume(Date begintime) {
		return getTimeSecondConsume(begintime) / 60;
	}

	public static Date getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static int getYear(Date date) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		return now.get(Calendar.YEAR);
	}

	public static Date getYesterdayOutWeek() {
		if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != 2) {
			return newDateByDay(getCurrentDay(), -1);
		} else {
			return newDateByDay(getCurrentDay(), -3);
		}
	}

	public static boolean betweenTimes(String compareTime, String beginTimeStr, String endTimeStr) {
		try {
			Date nowTime = DateUtils.parseTime(compareTime);
			Date beginTime = null;
			Date endTime = null;
			if (ObjectUtils.isNotEmpty(beginTimeStr)) {
				beginTime = DateUtils.parseTime(beginTimeStr);
			}
			if (ObjectUtils.isNotEmpty(endTimeStr)) {
				endTime = DateUtils.parseTime(endTimeStr);
				// 结束时间和开始时间做个比较（19.00.00 —— 2.00.00，结束时间需要加上1天 ）
				if (beginTime != null && endTime != null && endTime.before(beginTime)) {
					endTime = DateUtils.newDateByDay(endTime, 1);
				}
			}

			if (beginTime != null && nowTime.before(beginTime)) {
				return false;
			}
			if (endTime != null && nowTime.after(endTime)) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
