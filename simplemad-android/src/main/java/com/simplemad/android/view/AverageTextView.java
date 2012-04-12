package com.simplemad.android.view;

import java.util.List;

import com.simplemad.android.setting.AverageTextViewSetting;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.setting.Margin;
import com.simplemad.android.view.setting.TextViewSetting;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AverageTextView extends LinearLayout {

	private List<String> data;
	private AverageTextViewSetting setting;
	
	public AverageTextView(Context context, AverageTextViewSetting setting, List<String> data) {
		super(context);
		this.data = data;
		this.setting = setting;
		initUI();
	}
	
	private void initUI() {
		StyleSettingTool.setBackground(this, setting);
		for(int index = 0; index < data.size(); index++) {
			addText(data.get(index), index);
		}
	}
	
	private void addText(String value, int index) {
		TextViewSetting tvSetting = setting.text(index);
		TextView tv = new TextView(getContext());
		tv.setText(value);
		StyleSettingTool.setTextStyle(tv, tvSetting);
		addView(tv, params(tvSetting.margin()));
	}
	
	private LayoutParams params(Margin margin) {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		StyleSettingTool.setMargin(margin, params);
		return params;
	}

}
