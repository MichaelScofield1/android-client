package com.simplemad.android.service.android;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;

import com.simplemad.android.receiver.NetWorkReceiver;
import com.simplemad.android.server.SimplemadServer;

/**
 * 监听服务器所有消息
 * @author kamen
 *
 */
public class ServerNotificationService extends Service {
	
	private IBinder mBinder = new ServerNotificationBinder();
	private BroadcastReceiver networkReceiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("ServerNotificationService onBind");
		return mBinder;
	}
	
	private void startup() {
		SimplemadServer.instance();
	}
	
	private void shutdown() {
		SimplemadServer.instance().shutdown();
	}
	
	public class ServerNotificationBinder extends Binder {
		ServerNotificationService getService() {
			return ServerNotificationService.this;
		}
	}
	
	@Override
	public void onCreate() {
		startup();
		registerNetworkReceiver();
		startBootLoginService();
	}
	
	private void startBootLoginService() {
		System.out.println("bootLoginServiceIntent starting...");
		Intent bootLoginServiceIntent = new Intent(this, BootLoginService.class);
		this.startService(bootLoginServiceIntent);
		System.out.println("bootLoginServiceIntent started...");
	}
	
	private void registerNetworkReceiver() {
		System.out.println("ServerNotificationService register network-receiver");
		networkReceiver = new NetWorkReceiver();
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(networkReceiver, filter);
	}
	
	private void stopReceiver() {
		if(networkReceiver != null) {
			unregisterReceiver(networkReceiver);
		}
	}
	
	@Override
	public void onDestroy() {
		System.out.println("ServerNotificationService onDestroy");
		shutdown();
		stopReceiver();
		super.onDestroy();
	}

}
