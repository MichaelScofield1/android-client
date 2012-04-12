package com.simplemad.android.view;

import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.setting.LabelFieldSetting;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LabelField extends LinearLayout {

	private TextView labelView;
	private TextView valueView;
	
	private LabelFieldSetting setting;
	
	public LabelField(Context context, LabelFieldSetting setting) {
		super(context);
		this.setting = setting;
		initUI();
	}
	
	public void setValue(String value) {
		valueView.setText(value);
	}
	
	public void setLabel(String label) {
		labelView.setText(label + " : ");
	}
	
	private void initUI() {
		StyleSettingTool.setBackground(this, setting);
		addView(createLabelView(), createLabelParams());
		addView(createEditView(), createEditParams());
	}
	
	private View createLabelView() {
		labelView = new TextView(getContext());
		StyleSettingTool.setTextStyle(labelView, setting.label());
		return labelView;
	}
	
	private LayoutParams createLabelParams() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		StyleSettingTool.setMargin(setting.label().margin(), params);
		return params;
	}
	
	private LayoutParams createEditParams() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		StyleSettingTool.setMargin(setting.field().margin(), params);
		params.weight = 1;
		return params;
	}

	protected View createEditView() {
		valueView = new TextView(getContext());
		StyleSettingTool.setTextStyle(valueView, setting.field());
		return valueView;
	}

}
