package com.simplemad.android.setting.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

import com.simplemad.android.setting.DetailInfoSetting;
import com.simplemad.android.setting.UserInfoSetting;
import com.simplemad.android.setting.UserStatisticsDetailSetting;
import com.simplemad.android.view.setting.Margin;

public class BaseUserStatisticsDetailSetting implements UserStatisticsDetailSetting {

	@Override
	public Drawable background() {
		GradientDrawable d = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(63, 61, 65), Color.rgb(63, 61, 65)});
		d.setCornerRadius(10);
		d.setAlpha(200);
		return d;
	}

	@Override
	public UserInfoSetting userInfo() {
		return new BaseUserInfoSetting();
	}

	@Override
	public DetailInfoSetting detailInfo() {
		return new BaseDetailInfoSetting();
	}

	@Override
	public Margin contentMargin() {
		return new Margin(20, 0, 20, 0);
	}

}
