package com.simplemad.android.client;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.service.android.KeepAliveService;
import com.simplemad.android.service.android.PollingService;

import android.content.Context;
import android.content.Intent;


public class KeepAliveManager {
	
	private KeepAliveManager() {
	}
	
	public static void startService() {
		stopPolling();
		
		Context context = getContext();
		Intent keepAliveService = new Intent(context, KeepAliveService.class);
		context.startService(keepAliveService);
	}
	
	public static void stopService() {
		Context context = getContext();
		Intent keepAliveService = new Intent(context, KeepAliveService.class);
		context.stopService(keepAliveService);
	}
	
	public static void startPolling() {
		stopService();
		
		Context context = getContext();
		Intent pollingService = new Intent(context, PollingService.class);
		context.startService(pollingService);
	}
	
	public static void stopPolling() {
		Context context = getContext();
		Intent pollingService = new Intent(context, PollingService.class);
		context.stopService(pollingService);
	}
	
	private static Context getContext() {
		return SimpleMadApp.instance().getBaseContext();
	}
}
