package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.ImageSetting;
import com.simplemad.android.view.setting.TextViewSetting;

public interface TabSetting extends Background {

	public ImageSetting image();
	
	public TextViewSetting name();
}
