package com.simplemad.android.util;


public class TextUtil {
	
	public static String translate(String str, boolean isHorizontal) {
		if(!isHorizontal)
			return str;
		if(StringUtil.isEmpty(str))
			return "";
		if(str.length() == 1)
			return str;
		StringBuffer buffer = new StringBuffer();
		for(int index = 0; index < str.length(); index++) {
			buffer.append(str.charAt(index));
			if(index != str.length() - 1)
				buffer.append("\n");
		}
		return buffer.toString();
	}
}
