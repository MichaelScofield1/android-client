package com.simplemad.android.service.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.simplemad.android.R;
import com.simplemad.android.activity.MainActivity;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.VibratorUtil;
import com.simplemad.bean.Advertisement;

/**
 * 服务器有新广告的提示服务
 * @author kamen
 *
 */
public class AdNotificationService extends Service {

	private IBinder mBinder = new AdNotificationBinder();
	private NotificationManager mNM;
	private int id = R.string.login;
	
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("AdNotificationService onBind");
		return mBinder;
	}
	
	public class AdNotificationBinder extends Binder {
		AdNotificationService getService() {
			return AdNotificationService.this;
		}
	}
	
	@Override
	public void onCreate() {
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}

	protected void receiveMsg(final Advertisement advertisement) {
		VibratorUtil.vibrator(AdNotificationService.this, 1000);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Intent i = new Intent(AppUtil.RECEIVER_NEW_ADVERTISEMENT_MSG);
				sendBroadcast(i);
			}
		}).start();
		showNotification(advertisement);
	}
	
	
	private void showNotification(Advertisement advertisement) {
		mNM.cancel(id);
		mNM.notify(id, createNotification(advertisement));
	}
	
	private Notification createNotification(Advertisement advertisement) {
		Notification notification = new Notification(com.simplemad.android.R.drawable.ic_launcher, "新广告",
                System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_ALL;
		Intent intent = new Intent(Intent.ACTION_MAIN);
	    intent.addCategory(Intent.CATEGORY_LAUNCHER);
	    intent.setClass(this, MainActivity.class);
		intent.putExtra(MainActivity.TAB_INDEX, MainActivity.REMOTE_AD_INDEX);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, "新广告",
        		advertisement.getName(), contentIntent);
        return notification;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("AdNotificationService onStartCommand");
		Advertisement advertisement = (Advertisement) intent.getSerializableExtra(AppUtil.EXTRA_NAME_AD);
		if(advertisement != null) {
			receiveMsg(advertisement);
		}
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		System.out.println("AdNotificationService onDestroy");
		super.onDestroy();
		mNM.cancel(id);
		
	}

}
