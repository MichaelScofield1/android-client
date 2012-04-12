package com.simplemad.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManagerUtil {

	public static boolean hasNetwork(Context context) {
		return getManager(context).getActiveNetworkInfo() != null;
	}
	
	public static NetworkInfo getActivityNetworkInfo(Context context) {
		return getManager(context).getActiveNetworkInfo();
	}
	
	public static NetworkInfo getActivityNetworkInfo(ConnectivityManager manager) {
		return manager.getActiveNetworkInfo();
	}
	
	public static boolean isRoaming(Context context) {
		ConnectivityManager manager = getManager(context);
		return getActivityNetworkInfo(manager) != null && getActivityNetworkInfo(manager).isRoaming();
	}
	
	public static boolean isAvailable(Context context) {
		ConnectivityManager manager = getManager(context);
		return getActivityNetworkInfo(manager) != null && getActivityNetworkInfo(manager).isAvailable();
	}
	
	public static boolean isConnected(Context context) {
		ConnectivityManager manager = getManager(context);
		return getActivityNetworkInfo(manager) != null && getActivityNetworkInfo(manager).isConnected();
	}
	
	public static boolean isConnectedOrConnecting(Context context) {
		ConnectivityManager manager = getManager(context);
		return getActivityNetworkInfo(manager) != null && getActivityNetworkInfo(manager).isConnectedOrConnecting();
	}
	
	public static boolean isFailover(Context context) {
		ConnectivityManager manager = getManager(context);
		return getActivityNetworkInfo(manager) != null && getActivityNetworkInfo(manager).isFailover();
	}
	
	public static ConnectivityManager getManager(Context context) {
		return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
}
