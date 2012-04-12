package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.TextViewSetting;

public interface UserStatisticsSetting extends Background {

	public TextViewSetting title();
	
	public TotalMoneySetting totalMoney();
	
	public UserStatisticsDetailSetting detail();
}
