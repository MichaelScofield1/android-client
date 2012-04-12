package com.simplemad.android.setting.base;

import android.graphics.drawable.Drawable;

import com.simplemad.android.setting.UserProtocolSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseUserProtocolSetting implements UserProtocolSetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public TextViewSetting accept() {
		return new BaseRedButtonSetting();
	}

	@Override
	public TextViewSetting refuse() {
		return new BaseBlackButtonSetting();
	}

	@Override
	public Margin margin() {
		return new Margin(20, 5, 20, 5);
	}

}
