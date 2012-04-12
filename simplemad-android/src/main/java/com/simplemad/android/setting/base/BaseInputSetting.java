package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.simplemad.android.setting.InputSetting;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseInputSetting extends BaseInputBackground implements InputSetting {

	@Override
	public TextViewSetting label() {
		return new TextViewSetting() {
			
			@Override
			public Drawable background() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Padding padding() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Margin margin() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int gravity() {
				return Gravity.CENTER_VERTICAL|Gravity.RIGHT;
			}
			
			@Override
			public float fontSize() {
				return 18.0f;
			}
			
			@Override
			public ColorStateList fontColor() {
				return new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{Color.BLACK});
			}
			
			@Override
			public Bound bound() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Override
	public TextViewSetting edit() {
		return new TextViewSetting() {
			
			@Override
			public Drawable background() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Padding padding() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Margin margin() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int gravity() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public float fontSize() {
				return 18.0f;
			}
			
			@Override
			public ColorStateList fontColor() {
				return new ColorStateList(new int[][]{new int[]{android.R.attr.state_enabled}}, new int[]{Color.BLACK});
			}
			
			@Override
			public Bound bound() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
