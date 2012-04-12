package com.simplemad.android.view.tree;

import android.view.View;

import com.simplemad.android.util.DialogUtil;

public abstract class DasOnGroupExpandListener{
	
	protected abstract void perform(View target, int level) throws Exception;
	
	/**
	 * default error handle method, it could be override
	 * @param v
	 * @param e
	 */
	protected void handleError(View target, Exception e) {
		DialogUtil.sayError(target.getContext(), e.getMessage());
	}
	
	public void onGroupExpand(View target, int level) {
		try {
			perform(target, level);
		} catch (Exception e) {
			handleError(target, e);
		}
	}

}
