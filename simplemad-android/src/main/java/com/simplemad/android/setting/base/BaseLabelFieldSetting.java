package com.simplemad.android.setting.base;

import android.graphics.drawable.Drawable;

import com.simplemad.android.view.setting.LabelFieldSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseLabelFieldSetting implements LabelFieldSetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public TextViewSetting label() {
		return new BaseLabelSmallSetting();
	}

	@Override
	public TextViewSetting field() {
		return new BaseLabelSmallSetting();
	}

	@Override
	public Margin contentMargin() {
		return null;
	}

}
