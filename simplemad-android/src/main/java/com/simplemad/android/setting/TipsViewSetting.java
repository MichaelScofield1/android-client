package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.TextViewSetting;

public interface TipsViewSetting extends Background {

	TextViewSetting title();
	
	TextViewSetting first();
	
	TextViewSetting second();
}
