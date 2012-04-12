package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface UserPasswordSetting extends Background {

	public InputSetting originalPsw();
	
	public InputSetting newPsw();
	
	public InputSetting confirmPsw();
	
	public TextViewSetting modifyBtn();
	
	public TextViewSetting cancelBtn();
	
	public Margin contentMargin();
	
	public TextViewSetting title();
}
