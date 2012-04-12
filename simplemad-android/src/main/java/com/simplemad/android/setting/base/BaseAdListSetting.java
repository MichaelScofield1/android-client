package com.simplemad.android.setting.base;

import android.graphics.drawable.Drawable;

import com.simplemad.android.setting.AdListSetting;
import com.simplemad.android.view.adList.AdListItemRelativeViewSetting;
import com.simplemad.android.view.setting.Divider;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseAdListSetting implements AdListSetting {

	@Override
	public Drawable background() {
		return null;
	}

	@Override
	public TextViewSetting title() {
		return new BaseTopTitleSetting();
	}

	@Override
	public Margin listMargin() {
		return new Margin(20, 20, 20, 0);
	}

	@Override
	public AdListItemRelativeViewSetting adListItemSetting() {
		return new BaseAdListItemRelativeViewSetting();
	}

	@Override
	public Divider adListDivider() {
		return new BaseAdListDivider();
	}
	
	class BaseAdListDivider implements Divider {

		@Override
		public Drawable background() {
			return null;
		}

		@Override
		public int getHeight() {
			return 10;
		}

		@Override
		public int getWidth() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Margin margin() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
