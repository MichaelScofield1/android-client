package com.simplemad.android.view.adList;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;

import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.SettingConstants;
import com.simplemad.android.view.setting.TextViewSetting;

public class DeleteTextViewSetting implements TextViewSetting {

	@Override
	public Drawable background() {
		StateListDrawable drawable = new StateListDrawable();
		ShapeDrawable sd_pressed = new ShapeDrawable(new DeleteShape());
		sd_pressed.getPaint().setColor(Color.GREEN);
		ShapeDrawable sd_enabled = new ShapeDrawable(new DeleteShape());
		sd_enabled.getPaint().setColor(Color.RED);
		ShapeDrawable sd_disabled = new ShapeDrawable(new DeleteShape());
		sd_disabled.getPaint().setColor(Color.GRAY);
		drawable.addState(new int[]{android.R.attr.state_pressed}, sd_pressed);
		drawable.addState(new int[]{android.R.attr.state_enabled}, sd_enabled);
		drawable.addState(new int[]{-android.R.attr.state_enabled}, sd_disabled);
		return drawable;
	}

	@Override
	public float fontSize() {
		return 18.0f;
	}

	@Override
	public ColorStateList fontColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int gravity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Bound bound() {
		return new Bound(30, 30, SettingConstants.NO_LINES, SettingConstants.NO_WIDTH, SettingConstants.NO_HEIGHT, true);
	}

	@Override
	public Padding padding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Margin margin() {
		// TODO Auto-generated method stub
		return null;
	}

}
