package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.Gravity;

import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseMoneySetting implements TextViewSetting {

	@Override
	public Drawable background() {
		StateListDrawable sld = new StateListDrawable();
		
		GradientDrawable enabled = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(232, 133, 36), Color.rgb(232, 133, 36)});
		enabled.setCornerRadius(10);
		
		GradientDrawable disabled = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.GRAY, Color.GRAY});
		disabled.setCornerRadius(10);
		
		GradientDrawable pressed = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.RED, Color.RED});
		pressed.setCornerRadius(10);
		
		sld.addState(new int[]{-android.R.attr.state_enabled}, disabled);
		sld.addState(new int[]{android.R.attr.state_pressed}, pressed);
		sld.addState(new int[]{android.R.attr.state_enabled}, enabled);
		return sld;
	}

	@Override
	public float fontSize() {
		return 18.0f;
	}

	@Override
	public ColorStateList fontColor() {
		return new ColorStateList(new int[][]{new int[]{-android.R.attr.state_enabled},
					new int[]{android.R.attr.state_pressed}, new int[]{android.R.attr.state_enabled}}, 
					new int[]{Color.WHITE, Color.WHITE, Color.WHITE});
	}

	@Override
	public int gravity() {
		return Gravity.CENTER;
	}

	@Override
	public Bound bound() {
		return new Bound(80, 30, 1, 80, 30, true);
	}

	@Override
	public Padding padding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Margin margin() {
		return new Margin(0, 15, 5, 0);
	}

}
