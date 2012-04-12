package com.simplemad.android.util;

public class StringUtil {

	public static boolean isEmpty(String str) {
		if(str == null || str.length() == 0)
			return true;
		return false;
	}
	
	public static String trim(String str) {
		if(str == null)
			return null;
		return str.trim();
	}
}
