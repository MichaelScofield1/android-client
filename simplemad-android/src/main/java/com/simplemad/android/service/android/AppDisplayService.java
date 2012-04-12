package com.simplemad.android.service.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.simplemad.android.R;
import com.simplemad.android.activity.MainActivity;
import com.simplemad.android.util.AppUtil;

@Deprecated
public class AppDisplayService extends Service {

	private int id = R.id.tv;
	private NotificationManager nNM;
	private Notification notification;
	
	@Override
	public IBinder onBind(Intent intent) {
		return new Binder();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		showNotification();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void showNotification() {
		nNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = createNotification();
		nNM.notify(id, notification);
	}

	private Notification createNotification() {
		Intent mainIntent = AppUtil.createMainLauncherIntent(this, MainActivity.class);
		Notification notification = new Notification(com.simplemad.android.R.drawable.ic_launcher, "",
                System.currentTimeMillis());
		notification.flags = Notification.FLAG_AUTO_CANCEL|Notification.FLAG_ONGOING_EVENT;
		 // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, "用户名xxx",
        		"简单广告正在后台支行", contentIntent);
        return notification;
	}

	@Override
	public void onDestroy() {
		nNM.cancel(id);
		super.onDestroy();
	}

}
