package com.simplemad.android.util;

public class BooleanUtil {

	public static final String YES_NO_TYPE = "YES_NO";
	public static final String TRUE_FALSE_TYPE = "TRUE_FALSE";
	public static final String ONE_ZERO_TYPE = "ONE_ZERO";
	
	public static boolean toBoolean(String s) {
		if(StringUtil.isEmpty(s)) {
			return false;
		}
		if(s.equalsIgnoreCase("Y") || s.equalsIgnoreCase("TRUE") || s.equalsIgnoreCase("1")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean toBoolean(Integer integer) {
		if(integer == null) {
			return false;
		}
		if(integer.intValue() == 1 ) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Integer toInteger(boolean bool) {
		if(bool) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public static String toString(boolean bool, String booleanType) {
		if(bool) {
			if(YES_NO_TYPE.equals(booleanType)) {
				return "Y";
			} else if(TRUE_FALSE_TYPE.equals(booleanType)) {
				return "TRUE";
			} else {
				return "1";
			}
		} else {
			if(YES_NO_TYPE.equals(booleanType)) {
				return "N";
			} else if(TRUE_FALSE_TYPE.equals(booleanType)) {
				return "FALSE";
			} else {
				return "0";
			}
		}
	}
}
