package com.simplemad.android.view.tree;

import android.content.Context;
import android.view.View;

import com.simplemad.android.util.DialogUtil;

public abstract class LeafClickListener {

	public void click(Context context, View view) {
		try {
			perform(view);
		} catch (Exception e) {
			error(e, context);
		}
	}
	
	protected void error(Exception e, Context context) {
		DialogUtil.sayError(context, e.getMessage());
	}
	
	protected abstract void perform(View view) throws Exception;
}
