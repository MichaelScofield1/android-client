package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface TextAdViewSetting extends Background {

	TextViewSetting title();
	
	TextViewSetting content();
	
	Margin margin();
}
