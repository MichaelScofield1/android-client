package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface TotalMoneySetting extends Background {

	TextViewSetting label();
	
	TextViewSetting money();
	
	TextViewSetting exchange();
	
	Margin contentMargin();
}
