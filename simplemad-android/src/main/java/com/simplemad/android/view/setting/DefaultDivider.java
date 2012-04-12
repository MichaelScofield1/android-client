package com.simplemad.android.view.setting;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;

public class DefaultDivider implements Divider {

	@Override
	public Drawable background() {
		return new ColorDrawable(Color.GRAY);
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth() {
		return LayoutParams.MATCH_PARENT;
	}

	@Override
	public Margin margin() {
		return null;
	}

}
