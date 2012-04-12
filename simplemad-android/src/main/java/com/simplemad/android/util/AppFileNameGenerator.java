package com.simplemad.android.util;

import com.simplemad.bean.AdvertisementType;

public class AppFileNameGenerator {

	private static final String UNKNOW_FILE = "unknow";
	private static final String DOT = ".";
	
	public static String generateFileName(AdvertisementType adType, String extendedName) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(generatePrefix(adType));
		buffer.append(generateMiddle(adType));
		buffer.append(generateTail(extendedName));
		
		return buffer.toString();
	}
	
	private static String generatePrefix(AdvertisementType adType) {
		if(adType == null) {
			return UNKNOW_FILE;
		} else {
			return adType.getEnglishName();
		}
	}
	
	private static String generateMiddle(AdvertisementType adType) {
		return String.valueOf(System.currentTimeMillis());
	}
	
	private static String generateTail(String extendedName) {
		if(StringUtil.isEmpty(extendedName)) {
			return "";
		} else if(extendedName.startsWith(DOT)) {
			return extendedName;
		} else {
			return DOT + extendedName;
		}
	}
}
