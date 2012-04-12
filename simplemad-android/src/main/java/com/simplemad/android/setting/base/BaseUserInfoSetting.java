package com.simplemad.android.setting.base;

import android.graphics.drawable.Drawable;

import com.simplemad.android.R;
import com.simplemad.android.setting.UserInfoSetting;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.view.setting.ImageSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseUserInfoSetting implements UserInfoSetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public ImageSetting header() {
		return new ImageSetting() {
			
			@Override
			public Padding padding() {
				return new Padding(5, 5, 5, 5);
			}
			
			@Override
			public Drawable image() {
				return AppUtil.getResourceDrawable(R.drawable.myspace);
			}
		};
	}

	@Override
	public TextViewSetting name() {
		return new BaseLabelSetting();
	}

	@Override
	public TextViewSetting registerDate() {
		return new BaseLabelSmallSetting();
	}

	@Override
	public Margin contentMargin() {
		return new Margin(20, 5, 20, 5);
	}

}
