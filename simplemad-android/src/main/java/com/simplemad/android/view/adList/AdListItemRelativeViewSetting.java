package com.simplemad.android.view.adList;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.ImageSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface AdListItemRelativeViewSetting extends Background {

	Margin margin();
	
	TextViewSetting name();
	
	TextViewSetting adType();
	
	TextViewSetting leftTime();
	
	TextViewSetting money();
	
	TextViewSetting status();
	
	ImageSetting image();
	
	Margin processBarMargin();
}
