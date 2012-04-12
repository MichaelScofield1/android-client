package com.simplemad.android.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;

import com.simplemad.android.SimpleMadApp;

public class AppUtil {

	public static final String RECEIVER_DOWNLOAD_COMPLETE = "com.simplemad.android.service.complete";
	public static final String RECEIVER_NEW_ADVERTISEMENT_MSG = "com.simplemad.android.service.newad";
	public static final String RECEIVER_USER_LOGIN_RESULT_MSG = "com.simplemad.android.broadcast.login";
	public static final String RECEIVER_USER_REGISTER_RESULT_MSG = "com.simplemad.android.broadcast.register";
	
	public static final String EXTRA_NAME_AD = "advertisement";
	public static final String EXTRA_NAME_LOGIN_RESULT = "login_result";
	public static final String EXTRA_NAME_REGISTER_RESULT = "register_result";
	public static final String EXTRA_NAME_DOWNLOAD_AD_RESULT = "download_ad_result";
	
	public static final long TIME_OUT = 30000;
	
	public static void homePresse(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
	}
	
	public static Intent createMainLauncherIntent(Context context, Class<?> clazz) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
	    intent.addCategory(Intent.CATEGORY_LAUNCHER);
	    intent.setClass(context, clazz);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	    return intent;
	}
	
	public static boolean isServiceExisted(String className) {
		Context context = SimpleMadApp.instance().getBaseContext();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if(!(serviceList.size() > 0)) {
            return false;
        }

        for(int i = 0; i < serviceList.size(); i++) {
            RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if(serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
	
	public static Drawable getResourceDrawable(int resourceId) {
		return getResources().getDrawable(resourceId);
	}
	
	public static Resources getResources() {
		return SimpleMadApp.instance().getBaseContext().getResources();
	}
	
	public static InputStream getRawResourceIS(int rawId) {
		return getResources().openRawResource(rawId);
	}
	
	public static FileInputStream getRawResourceFIS(int rawId) {
		try {
			return getResources().openRawResourceFd(rawId).createInputStream();
		} catch (NotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			return null;
		}
	}
}
