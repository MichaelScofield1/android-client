package com.simplemad.android.setting;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

public interface ExchangeSetting extends Background {

	public TextViewSetting title();
	
	public TextViewSetting phone();
	
	public TextViewSetting exchange();
	
	public AverageTextViewListSetting list();
	
	public TextViewSetting recordLable();
	
	public TextViewSetting exchangeDesc();
	
	public Margin margin();
}
