package com.simplemad.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static final String SIMPLE_PATTERN = "yyyy-MM-dd";
	
	public static final String MINUTE_PATTERN = "yyyy-MM-dd hh:mm";
	
	public static final String FULL_PATTERN = "yyyy-MM-dd hh:mm:ss";
	
	public static final long ONE_SECOND = 1000;
	
	public static final long ONE_MINUTE = 60 * ONE_SECOND;
	
	public static final long ONE_HOUR = 60 * ONE_MINUTE;
	
	public static final long ONE_DAY = 24 * ONE_HOUR;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();

	public static String date2String(Date date) {
		if(date == null) {
			return "";
		}
		sdf.applyPattern(FULL_PATTERN);
		return sdf.format(date);
	}
	
	public static String date2String(Date date, String pattern) {
		if(date == null) {
			return "";
		}
		sdf.applyPattern(pattern);
		return sdf.format(date);
	}
	
	public static Date string2Date(String string) {
		if(StringUtil.isEmpty(string)) {
			return null;
		}
		sdf.applyPattern(FULL_PATTERN);
		try {
			return sdf.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date string2Date(String string, String pattern) {
		if(StringUtil.isEmpty(string)) {
			return null;
		}
		sdf.applyPattern(pattern);
		try {
			return sdf.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isOutOfDate(Date date) {
		if(date == null) {
			return true;
		} else {
			return date.before(new Date());
		}
	}
	
	public static Date addDay(Date first, int days) {
		if(first == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(first);
		c.add(Calendar.DAY_OF_YEAR, days);
		return c.getTime();
	}
	
	public static Date addHour(Date first, int hours) {
		if(first == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(first);
		c.add(Calendar.HOUR, hours);
		return c.getTime();
	}
	
	public static Date addMinute(Date first, int minutes) {
		if(first == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(first);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}
	
	public static long timeMills(Date date) {
		if(date == null) {
			return 0;
		}
		return date.getTime();
	}
	
	public static long timeMinus(Date startDate, Date endDate) {
		return timeMills(endDate) - timeMills(startDate);
	}
	
	public static String calculateLeftTimeString(long mills) {
		StringBuffer buffer = new StringBuffer();
		
		long days = mills / ONE_DAY;
		buffer.append(days).append("天");

		long hours = (mills - days * ONE_DAY) / ONE_HOUR;
		buffer.append(hours).append("时");
		
		long minutes = (mills - days * ONE_DAY - hours * ONE_HOUR) / ONE_MINUTE;
		buffer.append(minutes).append("分");
		
		return buffer.toString();
	}
	
	public static Date initializeByCurrentDay(long timing) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.setTimeInMillis(c.getTimeInMillis() + timing);
		return c.getTime();
	}
	
	public static Date currentDate() {
		return new Date();
	}
}
