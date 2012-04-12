package com.simplemad.android.setting.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.simplemad.android.setting.AverageTextViewListSetting;
import com.simplemad.android.setting.ExchangeSetting;
import com.simplemad.android.view.setting.Bound;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseExchangeSetting extends BaseActivityBackground implements
		ExchangeSetting {

	@Override
	public TextViewSetting title() {
		return new BaseTopTitleSetting();
	}

	@Override
	public TextViewSetting phone() {
		return new LabelTextViewSetting() {
			@Override
			public int gravity() {
				return Gravity.CENTER;
			}
		};
	}

	@Override
	public TextViewSetting exchange() {
		return new BaseRedButtonSetting();
	}

	@Override
	public AverageTextViewListSetting list() {
		return new BaseAverageTextViewListSetting();
	}

	@Override
	public TextViewSetting recordLable() {
		return new LabelTextViewSetting();
	}

	@Override
	public TextViewSetting exchangeDesc() {
		return new LabelTextViewSetting();
	}

	@Override
	public Margin margin() {
		return new Margin(20, 20, 20, 0);
	}
	
	class LabelTextViewSetting implements TextViewSetting {
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
			return Gravity.CENTER_VERTICAL;
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
	}

}
