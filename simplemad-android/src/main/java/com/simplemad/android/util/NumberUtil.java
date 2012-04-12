package com.simplemad.android.util;


public class NumberUtil {

	public static String transform(double value, int scale) {
		double result = Math.round(value*Math.pow(10, scale))/(Math.pow(10, scale));
		String str = String.valueOf(result);
		int emptyLength = scale - (str.length() - (1 + str.indexOf(".")));
		while(emptyLength > 0) {
			str += "0";
			emptyLength--;
		}
		return str;
	}
}
