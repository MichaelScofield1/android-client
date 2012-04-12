package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.simplemad.android.setting.TextAdViewSetting;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseTextAdViewSetting implements TextAdViewSetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public TextViewSetting title() {
		return new TextAdTitleSetting();
	}

	@Override
	public TextViewSetting content() {
		return new TextAdContentSetting();
	}

	@Override
	public Margin margin() {
		return new Margin(20, 20, 20, 20);
	}
	
	class TextAdTitleSetting implements TextViewSetting {

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
			return Gravity.CENTER;
		}

		@Override
		public Bound bound() {
			return null;
		}

		@Override
		public Padding padding() {
			return null;
		}

		@Override
		public Margin margin() {
			return new Margin(5, 5, 5, 5);
		}
		
	}
	
	class TextAdContentSetting implements TextViewSetting {

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
			return 0;
		}

		@Override
		public Bound bound() {
			return null;
		}

		@Override
		public Padding padding() {
			return null;
		}

		@Override
		public Margin margin() {
			return new Margin(5, 5, 5, 5);
		}
		
	}

}
