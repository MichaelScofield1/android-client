package com.simplemad.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.simplemad.android.service.UserAccountService;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.service.android.ServerNotificationService;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.bean.UserAccount;

public class CheckingActivity extends NoTitleActivity {

	private static final int INIT_DATA = 1;
	private BroadcastReceiver loginResultReceiver;
	private UserAccount userAccount;
	private UserAccountService userAccountService = UserAccountServiceImpl.instance();

	private Handler loadingHandler;
	private static final int INIT_DATA_WHAT = 1;
	
	private Handler timeoutHandler;
	private static final int TIME_OUT_WHAT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO create ui....
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		LinearLayout layout = new LinearLayout(this);
		StyleSettingTool.setBackground(layout, AppSetting.getAppBackground());
		setContentView(layout);
		
		Intent serverNotificationServiceIntent = new Intent(this, ServerNotificationService.class);
		startService(serverNotificationServiceIntent);
		
		loginResultReceiver = new LoginResultReceiver();
		IntentFilter filter = new IntentFilter(AppUtil.RECEIVER_USER_LOGIN_RESULT_MSG);
		registerReceiver(loginResultReceiver, filter);
		
		loadingHandler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what == INIT_DATA_WHAT) {
					/*check loginstatus and password, get the user history*/
					initData();
				}
			};
		};
		
		loadingHandler.sendEmptyMessageDelayed(INIT_DATA_WHAT, 3000);
		
		
	}
	
	private void initData() {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == INIT_DATA) {
					login();
					removeMessages(INIT_DATA);
				}
			}
		};
		handler.sendEmptyMessageDelayed(INIT_DATA, 100);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(loginResultReceiver);
		userAccount = null;
	}
	
	protected void login() {
		userAccount = userAccountService.loadFirstUserAccount();
		if(userAccount == null) {
			/*go to login activity*/
			startLoginActivity();
		} else {
			if(userAccount.isRemPassword()) {
				/*login automatically*/
				boolean msgNotSend = userAccountService.loginRemote(userAccount);
				if(msgNotSend) {
					boolean isLoginLocal = userAccountService.loginLocal(userAccount, false);
					if(isLoginLocal) {
						startMainActivity();
					} else {
						startLoginActivity();
					}
				} else {
					timeoutHandler = new Handler() {
						public void handleMessage(Message msg) {
							if(msg.what == TIME_OUT_WHAT && !isFinishing()) {
								startLoginActivity();
							}
						};
					};
					timeoutHandler.sendEmptyMessageDelayed(TIME_OUT_WHAT, AppUtil.TIME_OUT);
					/*login fail, going to the LoginActivity*/
//					startLoginActivity();
				}
			} else {
				startLoginActivity();
			}
		}
	}
	
	private void startLoginActivity() {
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivity(loginIntent);
		finish();
	}
	
	private void startMainActivity() {
		Intent loginIntent = new Intent(this, MainActivity.class);
		startActivity(loginIntent);
		finish();
	}
	
	private class LoginResultReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(userAccount != null && AppUtil.RECEIVER_USER_LOGIN_RESULT_MSG.equals(intent.getAction())) {
				boolean result = intent.getBooleanExtra(AppUtil.EXTRA_NAME_LOGIN_RESULT, false);
				if(!result) {
					startLoginActivity();
				} else {
					boolean isLoginLocal = userAccountService.loginLocal(userAccount, result);
					if(isLoginLocal) {
						startMainActivity();
					} else {
						startLoginActivity();
					}
				}
			}
		}
		
	}
}
