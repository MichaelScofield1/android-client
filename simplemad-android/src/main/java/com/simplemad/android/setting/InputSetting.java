package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.TextViewSetting;

public interface InputSetting extends Background {

	public TextViewSetting label();
	
	public TextViewSetting edit();
}
