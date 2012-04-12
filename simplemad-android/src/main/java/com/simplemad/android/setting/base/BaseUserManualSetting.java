package com.simplemad.android.setting.base;

import android.graphics.drawable.Drawable;

import com.simplemad.android.setting.UserManualSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseUserManualSetting implements UserManualSetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public TextViewSetting title() {
		return new BaseTopTitleSetting();
	}

	@Override
	public Margin contentMargin() {
		return new Margin(0, 20, 0, 0);
	}

	@Override
	public Drawable webViewBG() {
//		GradientDrawable d = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.GRAY, Color.GRAY});
//		d.setCornerRadius(10);
//		d.setStroke(3, Color.WHITE);
//		return d;
		return null;
	}

}
