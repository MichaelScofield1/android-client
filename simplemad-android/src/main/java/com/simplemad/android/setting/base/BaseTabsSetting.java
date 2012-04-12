package com.simplemad.android.setting.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;

import com.simplemad.android.R;
import com.simplemad.android.setting.TabSetting;
import com.simplemad.android.setting.TabWidgetSetting;
import com.simplemad.android.setting.TabsSetting;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.view.setting.ImageSetting;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.Padding;
import com.simplemad.android.view.setting.TextViewSetting;

public class BaseTabsSetting extends BaseActivityBackground implements TabsSetting {
	
	@Override
	public TabWidgetSetting tabWidget() {
		return new BaseTabWidgetSetting();
	}
	
	@Override
	public TabSetting tab(int index) {
		switch (index) {
		case 0:
			return new BaseTabSettingRemote();
		case 1:
			return new BaseTabSettingLocal();
		case 2:
			return new BaseTabSettingUserAccount();
		case 3:
			return new BaseTabSettingUserManual();
		default:
			return null;
		}
	}
	
	abstract class  BaseTabSettingAbstract implements TabSetting {
		@Override
		public Drawable background() {
			StateListDrawable sld = new StateListDrawable();
			
			GradientDrawable selected = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(48, 47, 50), Color.rgb(48, 47, 50)});
			selected.setCornerRadius(10);
			
			GradientDrawable pressed = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.rgb(48, 47, 50), Color.rgb(48, 47, 50)});
			pressed.setCornerRadius(10);
			
//			GradientDrawable normal = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.RED, Color.RED});
//			selected.setCornerRadius(10);
			
			sld.addState(new int[]{android.R.attr.state_selected}, selected);
			sld.addState(new int[]{android.R.attr.state_pressed}, pressed);
			
			return sld;
		}
		
		@Override
		public TextViewSetting name() {
			return new BaseTopTitleSetting() {
				@Override
				public float fontSize() {
					return 12.0f;
				}
				
				@Override
				public Margin margin() {
					return new Margin(0, 5, 0, 5);
				}
			};
		}
	}
	
	class BaseTabSettingRemote extends BaseTabSettingAbstract {
		
		@Override
		public ImageSetting image() {
			return new BaseImageSettingAbstract() {
				
				@Override
				public Drawable image() {
					return AppUtil.getResourceDrawable(R.drawable.star);
				}
			};
			
		}
		
	}
	
	class BaseTabSettingLocal extends BaseTabSettingAbstract {
		@Override
		public ImageSetting image() {
			return new BaseImageSettingAbstract() {
				
				@Override
				public Drawable image() {
					return AppUtil.getResourceDrawable(R.drawable.app_store_2);
				}
			};
		}
	}

	class BaseTabSettingUserAccount extends BaseTabSettingAbstract {
		@Override
		public ImageSetting image() {
			return new BaseImageSettingAbstract() {
				
				@Override
				public Drawable image() {
					return AppUtil.getResourceDrawable(R.drawable.gowalla);
				}
			};
		}
	}
	
	class BaseTabSettingUserManual extends BaseTabSettingAbstract {
		@Override
		public ImageSetting image() {
			return new BaseImageSettingAbstract() {
				
				@Override
				public Drawable image() {
					return AppUtil.getResourceDrawable(R.drawable.newsvine);
				}
			};
		}
	}
	
	abstract class BaseImageSettingAbstract implements ImageSetting {
		
		@Override
		public Padding padding() {
			return new Padding(0, 5, 0, 5);
		}
	}

}
