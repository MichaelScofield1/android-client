package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Margin;

public interface UserStatisticsDetailSetting extends Background {

	UserInfoSetting userInfo();
	
	DetailInfoSetting detailInfo();
	
	Margin contentMargin();
}
