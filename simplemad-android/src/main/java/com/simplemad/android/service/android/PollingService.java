package com.simplemad.android.service.android;

import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.client.TimerManager;
import com.simplemad.android.service.UserAccountService;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.util.DateUtil;

/**
 * 定时轮询的service
 * 
 * @author kamen
 * 
 */
public class PollingService extends Service {

	private IBinder mBinder = new PollingBinder();
	private Handler handler;
	protected static final int WHAT = 299;
	
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("PollingService onBind");
		return mBinder;
	}

	public class PollingBinder extends Binder {
		PollingService getService() {
			return PollingService.this;
		}
	}

	@Override
	public void onCreate() {
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == WHAT) {
					sendPollingPack();
					sendEmptyMessageDelayed(WHAT, TimerManager.getTimer().getInterval());
				} else {
					removeMessages(msg.what);
				}
			};
		};
	}
	
	protected void sendPollingPack() {
		if(UserAccountServiceImpl.instance().getCurrentUserAccount() == null || !SimpleMadApp.instance().isInNetwork()) {
			return;
		}
		if(isOutOfTiming()) {
			return;
		}
		UserAccountService userAccountService = UserAccountServiceImpl.instance();
		userAccountService.reloginRemote();
	}
	
	private boolean isOutOfTiming() {
		Date start = DateUtil.initializeByCurrentDay(TimerManager.getTimer().getStart());
		Date end = DateUtil.initializeByCurrentDay(TimerManager.getTimer().getEnd());
		Date current = DateUtil.currentDate();
		if(current.after(start) && current.before(end)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handler.sendEmptyMessageDelayed(WHAT, TimerManager.getTimer().getInterval());
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
