package com.simplemad.android.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.simplemad.android.util.DialogUtil;

public abstract class BackgroundProgressDialog extends ProgressDialog {

	protected Handler _progressHandler;
	private final int COMPLETE = 1;
	private final int ERROR = 2;
	private final String ERROR_MSG = "error";
	protected long _waitingTime = 100;
	protected boolean _isCancel = false;

	public BackgroundProgressDialog(Context context, String title) {
		super(context);
		setMessage(title);
		setCancelable(false);
		_progressHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what==COMPLETE) {
					try {
						complete();
					} catch (Exception e) {
						DialogUtil.sayError(getContext(), e);
					}
					BackgroundProgressDialog.this.dismiss();
                } else if(msg.what == ERROR){
                	DialogUtil.sayError(getContext(), msg.getData().getString(ERROR_MSG));
                } else if(BackgroundProgressDialog.this.isShowing()) {
                	BackgroundProgressDialog.this.dismiss();
                }
				if(BackgroundProgressDialog.this.isShowing()) 
					BackgroundProgressDialog.this.dismiss();
			}
		};

	}

	@Override
	public void show() {
		super.show();
		new Thread(){
			@Override
			public void run() {
				try {
					work();
					_progressHandler.sendEmptyMessage(COMPLETE);
				} catch (Exception e) {
					Message message=new Message();
					message.what = ERROR;
					message.getData().putString(ERROR_MSG, e.getMessage());					
					_progressHandler.sendMessage(message);
				}
			};
		}.start();
	}

	/**
	 * run Task
	 * 
	 * @throws DasError
	 */
	protected abstract void work() throws Exception;
	
	protected abstract void complete() throws Exception;

}
