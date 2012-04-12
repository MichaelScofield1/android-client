package com.simplemad.android.setting.base;

import com.simplemad.android.setting.InputSetting;
import com.simplemad.android.setting.RegisterSetting;
import com.simplemad.android.setting.TipsViewSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseRegisterSetting extends BaseActivityBackground implements RegisterSetting {

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
	public InputSetting confirmPassword() {
		return new BaseInputSetting();
	}

	@Override
	public TextViewSetting register() {
		return new BaseRedButtonSetting();
	}

	@Override
	public TextViewSetting cancel() {
		return new BaseBlackButtonSetting();
	}

	@Override
	public Margin contentMargin() {
		return new Margin(20, 20, 20, 0);
	}

	@Override
	public TipsViewSetting tips() {
		return new BaseTipsViewSetting();
	}

}
