package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.LabelFieldSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface DetailInfoSetting extends Background {

	public LabelFieldSetting labelField();
	
	public TextViewSetting modifyBtn();
	
	public Margin contentMargin();
}
