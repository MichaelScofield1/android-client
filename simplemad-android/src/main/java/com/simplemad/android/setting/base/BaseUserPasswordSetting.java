package com.simplemad.android.setting.base;

import com.simplemad.android.setting.InputSetting;
import com.simplemad.android.setting.UserPasswordSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseUserPasswordSetting extends BaseActivityBackground implements UserPasswordSetting {

	@Override
	public InputSetting originalPsw() {
		return new BaseInputSetting();
	}

	@Override
	public InputSetting newPsw() {
		return new BaseInputSetting();
	}

	@Override
	public InputSetting confirmPsw() {
		return new BaseInputSetting();
	}

	@Override
	public TextViewSetting modifyBtn() {
		return new BaseRedButtonSetting();
	}

	@Override
	public TextViewSetting cancelBtn() {
		return new BaseBlackButtonSetting();
	}

	@Override
	public Margin contentMargin() {
		return new Margin(20, 20, 20, 0);
	}

	@Override
	public TextViewSetting title() {
		return new BaseTopTitleSetting();
	}

}
