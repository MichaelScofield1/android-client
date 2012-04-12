package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.ImageSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface UserInfoSetting extends Background {

	public ImageSetting header();
	
	public TextViewSetting name();
	
	public TextViewSetting registerDate();
	
	public Margin contentMargin();
}
