package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;

import com.simplemad.android.setting.DetailInfoSetting;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.LabelFieldSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseDetailInfoSetting implements DetailInfoSetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public LabelFieldSetting labelField() {
		return new BaseLabelFieldSetting();
	}

	@Override
	public TextViewSetting modifyBtn() {
		return new BaseModifyButonSetting();
	}

	@Override
	public Margin contentMargin() {
		return new Margin(20, 0, 20, 0);
	}
	
	class BaseModifyButonSetting implements TextViewSetting {

		@Override
		public Drawable background() {
			StateListDrawable sld = new StateListDrawable();
			
			GradientDrawable pressed = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(104, 104, 104), Color.rgb(104, 104, 104)});
			pressed.setCornerRadius(10);
			
			GradientDrawable normal = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(65, 65, 65), Color.rgb(65, 65, 65)});
			normal.setCornerRadius(10);
			
			sld.addState(new int[]{android.R.attr.state_pressed}, pressed);
			sld.addState(new int[]{android.R.attr.state_enabled}, normal);
			
			return sld;
		}

		@Override
		public float fontSize() {
			return 18.0f;
		}

		@Override
		public ColorStateList fontColor() {
			return new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, 
						new int[]{android.R.attr.state_enabled}}, new int[]{Color.WHITE, Color.WHITE});
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
			return new Margin(20, 0, 20, 10);
		}
		
	}
	

}
