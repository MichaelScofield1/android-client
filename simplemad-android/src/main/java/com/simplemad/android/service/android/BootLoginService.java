package com.simplemad.android.service.android;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;

import com.simplemad.android.service.UserAccountService;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.util.AppUtil;
import com.simplemad.bean.UserAccount;

/**
 * 执行手机开机时默认登录
 * 
 * @author kamen
 * 
 */
public class BootLoginService extends Service {

	private IBinder mBinder = new BootLoginBinder();
	private UserAccountService userService = UserAccountServiceImpl.instance();
	private BroadcastReceiver loginResultReceiver;
	private UserAccount firstUser;

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("ServerNotificationService onBind");
		return mBinder;
	}

	private void startup() {
		try {
			firstUser = userService.loadFirstUserAccount();
			if (firstUser == null) {
				return;
			} else if (!firstUser.isRemPassword()) {
				return;
			}

			ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			
			if (networkInfo == null) {
				stopSelf();
			} else {
				loginResultReceiver = new LoginResultReceiver();
				IntentFilter filter = new IntentFilter(
						AppUtil.RECEIVER_USER_LOGIN_RESULT_MSG);
				
				registerReceiver(loginResultReceiver, filter);
				
				if (userService.loginRemote(firstUser)) {
					userService.loginLocal(firstUser, false);
					unregisterReceiver(loginResultReceiver);
				}
			}
		} catch (Exception e) {
			System.out.println("BootBroadcastReceiver exception");
			System.out.println(e != null ? e.getMessage() : "");
		}
	}

	public class BootLoginBinder extends Binder {
		BootLoginService getService() {
			return BootLoginService.this;
		}
	}

	@Override
	public void onCreate() {
		startup();
	}

	@Override
	public void onDestroy() {
		System.out.println("BootLoginService onDestroy");
		if(loginResultReceiver != null) {
			unregisterReceiver(loginResultReceiver);
		}
		super.onDestroy();
	}

	private class LoginResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("BootLoginService-LoginResultReceiver received...");
			if (firstUser != null
					&& AppUtil.RECEIVER_USER_LOGIN_RESULT_MSG.equals(intent.getAction())) {
				boolean result = intent.getBooleanExtra(
						AppUtil.EXTRA_NAME_LOGIN_RESULT, false);
				if(result) {
					userService.loginLocal(firstUser, result);
				}
				stopSelf();
			}
		}

	}

}
