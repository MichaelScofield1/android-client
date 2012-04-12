package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface UserProtocolSetting extends Background {

	TextViewSetting accept();
	
	TextViewSetting refuse();
	
	Margin margin();
}
