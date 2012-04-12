package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;

import com.simplemad.android.view.adList.AdListItemRelativeViewSetting;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.ImageSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseAdListItemRelativeViewSetting implements
		AdListItemRelativeViewSetting {

	@Override
	public Drawable background() {
		StateListDrawable sld = new StateListDrawable();
		
		GradientDrawable selected = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(125, 125, 125), Color.rgb(125, 125, 125)});
		selected.setCornerRadius(10);
		
		GradientDrawable pressed = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(125, 125, 125), Color.rgb(125, 125, 125)});
		pressed.setCornerRadius(10);
		
		GradientDrawable normal = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(63, 61, 65), Color.rgb(63, 61, 65)});
		normal.setAlpha(200);
		normal.setCornerRadius(10);
		
		sld.addState(new int[]{android.R.attr.state_selected}, selected);
		sld.addState(new int[]{android.R.attr.state_pressed}, pressed);
		sld.addState(new int[]{android.R.attr.state_enabled}, normal);
		
		return sld;
	}

	@Override
	public Margin margin() {
		return null;
	}

	@Override
	public TextViewSetting name() {
		return new ContentTextViewSetting();
	}

	@Override
	public TextViewSetting adType() {
		return new ContentTextViewSetting();
	}

	@Override
	public TextViewSetting money() {
		return new BaseMoneySetting();
	}

	@Override
	public TextViewSetting status() {
		return new BaseStatusSetting();
	}

	@Override
	public ImageSetting image() {
		return new ImageSetting() {
			
			@Override
			public Padding padding() {
				return new Padding(5, 5, 5, 5);
			}
			
			@Override
			public Drawable image() {
				return null;
			}
		};
	}

	@Override
	public Margin processBarMargin() {
		return new Margin(0, 0, 0, 5);
	}

	@Override
	public TextViewSetting leftTime() {
		return new ContentTextViewSetting() {
			
			@Override
			public Margin margin() {
				return new Margin(0, 0, 0, 5);
			}
		};
	}
	
	class ContentTextViewSetting implements TextViewSetting {

		@Override
		public Drawable background() {
			return null;
		}

		@Override
		public float fontSize() {
			return 0;
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
		
	}

}
