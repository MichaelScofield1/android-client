package com.simplemad.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.util.IPAddressHelper;

public class ShutdownBroadcastReceiver extends BroadcastReceiver {
   
    public void onReceive(Context context, Intent intent) {
       
        if(intent.getAction().equals(Intent.ACTION_SHUTDOWN)){
            System.out.println("Shut down this system!!!");
            UserAccountServiceImpl.instance().logout();
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo != null) {
	            System.out.println("Shut down when network enabled:" + networkInfo.isConnected());
	            System.out.println(IPAddressHelper.getLocalIpAddress());
            }
        }
    }

}
