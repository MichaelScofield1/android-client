package com.simplemad.android.setting.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.simplemad.android.setting.AverageTextViewListSetting;
import com.simplemad.android.setting.AverageTextViewSetting;
import com.simplemad.android.view.setting.Divider;
import com.simplemad.android.view.setting.Margin;

public class BaseAverageTextViewListSetting implements
		AverageTextViewListSetting {

	@Override
	public Drawable background() {
		return new ColorDrawable(Color.GRAY);
	}

	@Override
	public AverageTextViewSetting text() {
		return new BaseAverageTextViewSetting();
	}

	@Override
	public Divider divider() {
		return new Divider() {
			
			@Override
			public Drawable background() {
				return null;
			}
			
			@Override
			public Margin margin() {
				return null;
			}
			
			@Override
			public int getWidth() {
				return 0;
			}
			
			@Override
			public int getHeight() {
				return 0;
			}
		};
	}

}
