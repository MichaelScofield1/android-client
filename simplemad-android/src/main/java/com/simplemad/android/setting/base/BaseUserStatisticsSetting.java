package com.simplemad.android.setting.base;

import com.simplemad.android.setting.TotalMoneySetting;
import com.simplemad.android.setting.UserStatisticsDetailSetting;
import com.simplemad.android.setting.UserStatisticsSetting;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseUserStatisticsSetting extends BaseActivityBackground implements UserStatisticsSetting {

	@Override
	public TextViewSetting title() {
		return new BaseTopTitleSetting();
	}

	@Override
	public TotalMoneySetting totalMoney() {
		return new BaseTotalMoneySetting();
	}

	@Override
	public UserStatisticsDetailSetting detail() {
		return new BaseUserStatisticsDetailSetting();
	}

}
