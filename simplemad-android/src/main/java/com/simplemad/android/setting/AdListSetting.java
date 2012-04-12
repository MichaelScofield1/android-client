package com.simplemad.android.setting;

import com.simplemad.android.view.adList.AdListItemRelativeViewSetting;
import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Divider;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface AdListSetting extends Background {

	public TextViewSetting title();
	
	public Margin listMargin();
	
	public AdListItemRelativeViewSetting adListItemSetting();
	
	public Divider adListDivider();
}
