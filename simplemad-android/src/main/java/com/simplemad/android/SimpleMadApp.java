package com.simplemad.android;

import com.simplemad.android.util.StringUtil;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;

public class SimpleMadApp extends Application {

	private static SimpleMadApp instance;
	
	public static boolean DEBUG_MODE = false;
	
	private String localIp;
	
	
	
	public static synchronized SimpleMadApp instance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	public int getVersion() {
		try {
			return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	public boolean isInNetwork() {
		return !StringUtil.isEmpty(localIp);
	}
	
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	
	public String getLocalIp() {
		return this.localIp;
	}
	
	
}
