package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface LoginSetting extends Background {

	public TextViewSetting title();
	
	public InputSetting user();
	
	public InputSetting password();
	
	public TextViewSetting login();
	
	public TextViewSetting register();
	
	public Margin contentMargin();
}
