package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.Gravity;

import com.simplemad.android.setting.AdvertisementPlaySetting;
import com.simplemad.android.setting.TextAdViewSetting;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseAdvertisementPlaySetting implements AdvertisementPlaySetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public TextViewSetting earnAdMoney() {
		return new BaseRedButtonSetting();
	}

	@Override
	public TextViewSetting earnSharingMoney() {
		return new BaseBlackButtonSetting();
	}

	@Override
	public TextViewSetting timer() {
		return new TimerViewSetting();
	}
	
	class TimerViewSetting implements TextViewSetting {

		@Override
		public Drawable background() {
			ShapeDrawable sd = new ShapeDrawable(new TimerShaper());
			sd.setAlpha(50);
			return sd;
		}
		
		class TimerShaper extends OvalShape {
			@Override
			public void draw(Canvas canvas, Paint paint) {
				paint.setColor(Color.GRAY);
				paint.setAlpha(50);
				super.draw(canvas, paint);
			}
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
			return new Padding(5, 5, 5, 5);
		}

		@Override
		public Margin margin() {
			return null;
		}
		
	}

	@Override
	public TextAdViewSetting text() {
		return new BaseTextAdViewSetting();
	}

}
