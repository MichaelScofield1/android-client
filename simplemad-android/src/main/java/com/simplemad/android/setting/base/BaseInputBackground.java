package com.simplemad.android.setting.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

import com.simplemad.android.view.setting.Background;

public class BaseInputBackground implements Background {

	@Override
	public Drawable background() {
		GradientDrawable drawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(128, 128, 128), Color.rgb(128, 128, 128)});
		drawable.setCornerRadius(10);
		drawable.setStroke(2, Color.BLACK);
		return drawable;
	}

}
