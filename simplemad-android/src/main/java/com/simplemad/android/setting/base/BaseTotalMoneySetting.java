package com.simplemad.android.setting.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;

import com.simplemad.android.setting.TotalMoneySetting;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseTotalMoneySetting implements TotalMoneySetting {

	@Override
	public Drawable background() {
		GradientDrawable d = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(63, 61, 65), Color.rgb(63, 61, 65)});
		d.setCornerRadius(10);
		d.setAlpha(200);
		return d;
	}

	@Override
	public TextViewSetting label() {
		return new BaseLabelSmallSetting() {
			@Override
			public Margin margin() {
				return new Margin(25, 0, 5, 0);
			}
		};
	}

	@Override
	public TextViewSetting money() {
		return new BaseMoneySetting() {
			@Override
			public Margin margin() {
				return new Margin(5, 0, 15, 0);
			}
			
			@Override
			public Bound bound() {
				return new Bound(100, 30, 1, 100, 30, true);
			}
		};
	}

	@Override
	public TextViewSetting exchange() {
		return new BaseLabelSetting() {
			@Override
			public Margin margin() {
				return new Margin(5, 0, 20, 0);
			}
		};
	}

	@Override
	public Margin contentMargin() {
		return new Margin(20, 20, 20, 20);
	}

}
