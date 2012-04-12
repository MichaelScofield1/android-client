package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseTopTitleSetting implements TextViewSetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public float fontSize() {
		return 24.0f;
	}

	@Override
	public ColorStateList fontColor() {
		return new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{Color.WHITE});
	}

	@Override
	public int gravity() {
		return Gravity.CENTER;
	}

	@Override
	public Bound bound() {
		return null;
	}

	@Override
	public Padding padding() {
		return null;
	}

	@Override
	public Margin margin() {
		return new Margin(0, 10, 0, 0);
	}

}
