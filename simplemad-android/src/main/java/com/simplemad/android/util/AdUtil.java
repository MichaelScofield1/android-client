package com.simplemad.android.util;

import com.simplemad.bean.Advertisement;
import com.simplemad.bean.AdvertisementType;

public class AdUtil {

	public static boolean needNetWork(Advertisement ad) {
		if(ad.getAdType().equals(AdvertisementType.HTML)) {
			return true;
		}
		if(ad.getAdType().equals(AdvertisementType.INTERACTION)) {
			return true;
		}
		return false;
	}
	
	public static boolean canNotSubmit(Advertisement advertisement) {
		return advertisement.isSubmited() || DateUtil.isOutOfDate(advertisement.getEndDate());
	}
	
	public static boolean canShareFee(Advertisement advertisement) {
		return advertisement.isSharable() && !advertisement.isShared() && !DateUtil.isOutOfDate(advertisement.getEndDate());
	}
}
