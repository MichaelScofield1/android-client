package com.simplemad.android.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public abstract class ConfirmDialog {

	private Context context;
	private String message;
	
	public ConfirmDialog(Context context, String message) {
		this.context = context;
		this.message = message;
	}
	
	public void show() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setPositiveButton("是", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ok();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("否", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
//		builder.setView(DialogUtil.createTitleView(_activity, _message));
		builder.setTitle(message);
		AlertDialog dialog = builder.create();
		dialog.show();	
	}
	
	public abstract void ok();
	
}
