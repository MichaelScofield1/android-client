package com.simplemad.android.setting.base;

import com.simplemad.android.setting.InputSetting;
import com.simplemad.android.setting.LoginSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseLoginSetting extends BaseActivityBackground implements LoginSetting {

	@Override
	public TextViewSetting title() {
		return new BaseTopTitleSetting();
	}

	@Override
	public InputSetting user() {
		return new BaseInputSetting();
	}

	@Override
	public InputSetting password() {
		return new BaseInputSetting();
	}

	@Override
	public TextViewSetting login() {
		return new BaseRedButtonSetting();
	}

	@Override
	public TextViewSetting register() {
		return new BaseBlackButtonSetting();
	}

	@Override
	public Margin contentMargin() {
		return new Margin(20, 20, 20, 0);
	}

}
