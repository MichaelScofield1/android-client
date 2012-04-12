package com.simplemad.android.util;

import java.util.List;

public class CollectionUtil {

	public static boolean isEmpty(List<?> list) {
		if(list == null || list.isEmpty())
			return true;
		return false;
	}
}
