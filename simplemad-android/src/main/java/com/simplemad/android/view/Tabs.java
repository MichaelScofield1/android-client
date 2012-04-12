package com.simplemad.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.simplemad.android.setting.TabWidgetSetting;
import com.simplemad.android.util.StyleSettingTool;

public class Tabs extends TabHost {

	public static final int TOP_POSITION = 1;
	public static final int BOTTOM_POSITION = 2;
	
	public Tabs(Context context, TabWidgetSetting tabWidgetSetting, int position) {
		super(context);
		initUI(tabWidgetSetting, position);
	}
	
	public Tabs(Context context, TabWidgetSetting tabWidgetSetting, AttributeSet attrs, int position) {
		super(context, attrs);
		initUI(tabWidgetSetting, position);
	}
	
	private void initUI(TabWidgetSetting tabWidgetSetting, int position) {
		this.setId(android.R.id.tabhost);
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		TabWidget widget = new TabWidget(getContext());
		widget.setId(android.R.id.tabs);
		StyleSettingTool.setPadding(widget, tabWidgetSetting.padding());
		StyleSettingTool.setBackground(widget, tabWidgetSetting);
		FrameLayout content = new FrameLayout(getContext());
		content.setId(android.R.id.tabcontent);
		if(position == TOP_POSITION) {
			layout.addView(widget, tabWidgetParams());
			layout.addView(content, tabContentParams());
		} else {
			layout.addView(content, tabContentParams());
			layout.addView(widget, tabWidgetParams());
		}
		addView(layout);
	}
	
	private LinearLayout.LayoutParams tabWidgetParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		return params;
	}
	
	private LinearLayout.LayoutParams tabContentParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		return params;
	}

}
