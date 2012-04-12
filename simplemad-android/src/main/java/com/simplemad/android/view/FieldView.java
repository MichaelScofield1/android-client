package com.simplemad.android.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class FieldView extends LinearLayout {

	private TextView labelView;
	
	public FieldView(Context context) {
		super(context);
		initUI();
	}
	
	public void setLabel(String label) {
		labelView.setText(label + " : ");
	}
	
	private void initUI() {
		labelView = new TextView(getContext());
		addView(labelView, createLabelParams());
		addView(createEditView(), createEditParams());
	}
	
	private LayoutParams createLabelParams() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		return params;
	}
	
	private LayoutParams createEditParams() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.weight = 1;
		return params;
	}
	
	protected abstract View createEditView();

}
