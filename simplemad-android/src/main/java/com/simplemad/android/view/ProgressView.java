package com.simplemad.android.view;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProgressView extends RelativeLayout {

	private ProgressBar progress;
	private TextView percent;
	
	public ProgressView(Context context) {
		super(context);
		initView();
	}
	
	public void setProgress(int current, int max) {
		if(progress.getVisibility() != VISIBLE) {
			progress.setVisibility(VISIBLE);
		}
		if(percent.getVisibility() != VISIBLE) {
			percent.setVisibility(VISIBLE);
		}
		progress.setMax(max);
		progress.setProgress(current);
		percent.setText(getPercent(current, max));
	}
	
	private String getPercent(int current, int max) {
		return (current * 100 / max) + "%";
	}
	
	private void initView() {
		addView(createProgress(), progressParams());
		addView(createPercent(), percentParams());
	}
	
	private View createProgress() {
		progress = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
		progress.setVisibility(GONE);
		return progress;
	}
	
	private View createPercent() {
		percent = new TextView(getContext());
		percent.setText("0%");
		percent.setVisibility(GONE);
		return percent;
	}
	
	private LayoutParams progressParams() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		return params;
	}
	
	private LayoutParams percentParams() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(CENTER_IN_PARENT, getId());
		return params;
	}

}
