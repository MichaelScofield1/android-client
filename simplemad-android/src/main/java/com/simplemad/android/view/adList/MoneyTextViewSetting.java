package com.simplemad.android.view.adList;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.Gravity;

import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class MoneyTextViewSetting implements TextViewSetting {

	@Override
	public Drawable background() {
		ShapeDrawable sd = new ShapeDrawable(new OvalShape());
		sd.getPaint().setColor(Color.GREEN);
		return sd;
	}

	@Override
	public float fontSize() {
		return 36;
	}

	@Override
	public ColorStateList fontColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int gravity() {
		// TODO Auto-generated method stub
		return Gravity.CENTER;
	}

	@Override
	public Bound bound() {
		// TODO Auto-generated method stub
		return null;
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
