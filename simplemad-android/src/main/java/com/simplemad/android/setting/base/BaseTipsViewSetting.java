package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.simplemad.android.setting.TipsViewSetting;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.SettingConstants;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseTipsViewSetting implements TipsViewSetting {

	@Override
	public Drawable background() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextViewSetting title() {
		return new TipsItemSetting();
	}

	@Override
	public TextViewSetting first() {
		return new TipsItemSetting();
	}

	@Override
	public TextViewSetting second() {
		return new TipsItemSetting();
	}
	
	class TipsItemSetting implements TextViewSetting {

		@Override
		public Drawable background() {
			return null;
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
		public int gravity() {
			return Gravity.CENTER_VERTICAL;
		}

		@Override
		public Bound bound() {
			return new Bound(SettingConstants.NO_WIDTH, SettingConstants.NO_HEIGHT, 1, SettingConstants.NO_WIDTH, SettingConstants.NO_HEIGHT, false);
		}

		@Override
		public Padding padding() {
			return null;
		}

		@Override
		public Margin margin() {
			return new Margin(10, 10, 10, 10);
		}
		
	}

}
