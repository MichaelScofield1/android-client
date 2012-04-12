package com.simplemad.android.setting;

import android.graphics.drawable.Drawable;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface UserManualSetting extends Background {

	TextViewSetting title();
	
	Margin contentMargin();
	
	Drawable webViewBG();
}
