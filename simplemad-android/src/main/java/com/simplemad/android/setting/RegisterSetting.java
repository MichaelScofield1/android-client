package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface RegisterSetting extends Background {

	public TextViewSetting title();
	
	public InputSetting user();
	
	public InputSetting password();
	
	public InputSetting confirmPassword();
	
	public TextViewSetting register();
	
	public TextViewSetting cancel();
	
	public TipsViewSetting tips();
	
	public Margin contentMargin();
}
