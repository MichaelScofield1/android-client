package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;

public interface TabsSetting extends Background {

	public TabSetting tab(int index);
	
	public TabWidgetSetting tabWidget();
}
