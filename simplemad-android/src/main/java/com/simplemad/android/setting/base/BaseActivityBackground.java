package com.simplemad.android.setting.base;

import android.graphics.drawable.Drawable;

import com.simplemad.android.R;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.view.setting.Background;

public class BaseActivityBackground implements Background {

	@Override
	public Drawable background() {
		return AppUtil.getResourceDrawable(R.drawable.bg);
	}

}
