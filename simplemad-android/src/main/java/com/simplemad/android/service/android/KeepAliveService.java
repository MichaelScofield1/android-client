package com.simplemad.android.service.android;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.client.ClientUtil;
import com.simplemad.android.service.UserAccountServiceImpl;

/**
 * 定时发送心跳包的service
 * 
 * @author kamen
 * 
 */
public class KeepAliveService extends Service {

	private IBinder mBinder = new KeepAliveBinder();
	private Handler handler;
	protected static final int WHAT = 199;
	protected static final long timeMillis = 3 * 60 * 1000;
	
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("KeepAliveService onBind");
		return mBinder;
	}

	public class KeepAliveBinder extends Binder {
		KeepAliveService getService() {
			return KeepAliveService.this;
		}
	}

	@Override
	public void onCreate() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == WHAT) {
					sendKeepAlivePack();
					sendEmptyMessageDelayed(WHAT, timeMillis);
				} else {
					removeMessages(msg.what);
				}
			};
		};
	}
	
	protected void sendKeepAlivePack() {
		if(UserAccountServiceImpl.instance().getCurrentUserAccount() == null || !SimpleMadApp.instance().isInNetwork()) {
			return;
		}
		ClientUtil.doSendEmpty();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handler.sendEmptyMessageDelayed(WHAT, timeMillis);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if(handler != null) {
			handler.removeMessages(WHAT);
		}
		super.onDestroy();
	}

}
