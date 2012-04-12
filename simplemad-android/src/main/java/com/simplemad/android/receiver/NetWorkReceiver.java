package com.simplemad.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.simplemad.android.SimpleMadApp;
import com.simplemad.android.server.SimplemadServer;
import com.simplemad.android.service.UserAccountService;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.IPAddressHelper;

public class NetWorkReceiver extends BroadcastReceiver{

	private NetworkInfo networkInfo;
	
	private UserAccountService accountService;
	
	public NetWorkReceiver() {
		accountService = UserAccountServiceImpl.instance();
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		//ip地址的改变需要通知后台服务器,把ip发送至后台,并在MainActivity里根据接收到的状态改变界面的值
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo = manager.getActiveNetworkInfo();
		if(networkInfo == null) {
			System.out.println("Network is InVisible");
			SimpleMadApp.instance().setLocalIp(null);
			if(accountService.getCurrentUserAccount() != null) {
				Intent loginIntent = new Intent(AppUtil.RECEIVER_USER_LOGIN_RESULT_MSG);
				loginIntent.putExtra(AppUtil.EXTRA_NAME_LOGIN_RESULT, false);
				context.sendBroadcast(loginIntent);
			}
		} else {
			String localIp = IPAddressHelper.getLocalIpAddress();
			System.out.println("Network is Visible:" + networkInfo.getTypeName() + " /IP:" + localIp);
			String previousLocalIp = SimpleMadApp.instance().getLocalIp();
			if(localIp.equals(previousLocalIp)) {
				return;
			}
			SimpleMadApp.instance().setLocalIp(localIp);
			SimplemadServer.instance().restart();
			accountService.reloginRemote();
		}
	}

}
