package com.simplemad.android.view.tree;

import com.simplemad.android.view.setting.Background;
import com.simplemad.android.view.setting.Divider;
import com.simplemad.android.view.setting.Padding;

public interface TreeSetting extends Background {
	
	int leftMargin();
	
	Padding padding();
	
	Divider divider();
	
	NodeSetting node();
}
