package com.simplemad.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.simplemad.android.service.android.ServerNotificationService;

public class BootBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			System.out.println("BootBroadcastReceiver received:" + intent.getAction());
			if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
				Intent serverNotificationServiceIntent = new Intent(context, ServerNotificationService.class);
				context.startService(serverNotificationServiceIntent);
				System.out.println("serverNotificationService started...");
			}
		} catch(Exception e) {
			System.out.println("BootBroadcastReceiver exception");
			System.out.println(e != null ? e.getMessage() : "");
		}
	}

}
