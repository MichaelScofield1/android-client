package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.simplemad.android.setting.AverageTextViewSetting;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseAverageTextViewSetting implements AverageTextViewSetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public TextViewSetting text(int index) {
		return new TextViewSetting() {
			
			@Override
			public Drawable background() {
				return null;
			}
			
			@Override
			public Padding padding() {
				return null;
			}
			
			@Override
			public Margin margin() {
				return null;
			}
			
			@Override
			public int gravity() {
				return Gravity.CENTER;
			}
			
			@Override
			public float fontSize() {
				return 18;
			}
			
			@Override
			public ColorStateList fontColor() {
				return new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{Color.WHITE});
			}
			
			@Override
			public Bound bound() {
				return null;
			}
		};
	}

}
