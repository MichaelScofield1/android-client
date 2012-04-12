package com.simplemad.android.setting.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.simplemad.android.setting.TabWidgetSetting;
import com.simplemad.android.view.setting.Padding;

public class BaseTabWidgetSetting implements TabWidgetSetting {

	@Override
	public Drawable background() {
		return new ColorDrawable(Color.BLACK);
	}

	@Override
	public Padding padding() {
		return new Padding(20, 10, 20, 10);
	}

}
