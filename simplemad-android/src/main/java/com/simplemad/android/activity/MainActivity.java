package com.simplemad.android.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.simplemad.android.activity.content.LocalAdListActivity;
import com.simplemad.android.activity.content.RemoteAdListActivity;
import com.simplemad.android.activity.content.UserManualActivity;
import com.simplemad.android.activity.content.UserPasswordManagerActivity;
import com.simplemad.android.activity.content.UserStatisticsViewActivity;
import com.simplemad.android.client.KeepAliveManager;
import com.simplemad.android.service.DownloadServiceManager;
import com.simplemad.android.service.UpgradeService;
import com.simplemad.android.service.UpgradeServiceImpl;
import com.simplemad.android.service.UserAccountService;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.service.UserService;
import com.simplemad.android.service.UserServiceImpl;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.setting.TabSetting;
import com.simplemad.android.setting.TabsSetting;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.view.Tabs;
import com.simplemad.parameter.ClientParameter;

public class MainActivity extends NoTitleActivityGroup {

	public static final String TAB_INDEX = "tab_index";
	public static final int REMOTE_AD_INDEX = 1;
	
	private UserAccountService uaService;
	private UserService userService;
	
	private TabHost host;
	
	private UpgradeService upgradeService;
	private Handler handler;
	private static final int WHAT_UPGRADE = 1;
	
	private TabsSetting setting;
	
	@Override
	public void onAttachedToWindow() {
		//disable the home key
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);         
		super.onAttachedToWindow();    
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_HOME) {
			//enable home key to execute app's action
			KeepAliveManager.startPolling();
			finish();
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}
	
	@Override
	public void onBackPressed() {
		KeepAliveManager.startPolling();
		super.onBackPressed();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		initUI();
		
		initEvent();
	}
	
	private void initUI() {
		ready();
		
		host = new Tabs(this, setting.tabWidget(), Tabs.BOTTOM_POSITION);
		host.setup(getLocalActivityManager());
		StyleSettingTool.setBackground(host, setting);
		createRemote();
		createLocal();
		createUserAccount();
		createUserManual();
		
		setContentView(host);
	}
	
	private void initEvent() {
		handler = createHandler();
		handler.sendEmptyMessageDelayed(WHAT_UPGRADE, 100);
	}
	
	private void ready() {
		setting = AppSetting.tabsSetting();
		uaService = UserAccountServiceImpl.instance();
		userService = UserServiceImpl.instance();
		userService.setMobile(uaService.getCurrentUserAccount().getMobile());
	}
	
	private void createRemote() {
		TabSpec spec = host.newTabSpec("Remote");
		spec.setIndicator(createIndicator(setting.tab(0), "新广告"));
		spec.setContent(createContent(RemoteAdListActivity.class));
		host.addTab(spec);
	}
	
	private void createLocal() {
		TabSpec spec = host.newTabSpec("Local");
		spec.setIndicator(createIndicator(setting.tab(1), "已下载广告"));
		spec.setContent(createContent(LocalAdListActivity.class));
		host.addTab(spec);
	}
	
	private void createUserAccount() {
		TabSpec spec = host.newTabSpec("UserStatistics");
		spec.setIndicator(createIndicator(setting.tab(2), "账户管理"));
		spec.setContent(createContent(UserStatisticsViewActivity.class));
		host.addTab(spec);
	}
	
	private void createUserManual() {
		TabSpec spec = host.newTabSpec("UserManual");
		spec.setIndicator(createIndicator(setting.tab(3), "使用指南"));
		spec.setContent(createContent(UserManualActivity.class));
		host.addTab(spec);
	}
	
	private View createIndicator(TabSetting tabSetting, String name) {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		StyleSettingTool.setBackground(layout, tabSetting);
		
		ImageView image = new ImageView(this);
		StyleSettingTool.setImageViewStyle(tabSetting.image(), image);
		layout.addView(image, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		TextView text = new TextView(this);
		text.setText(name);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setTextStyle(text, tabSetting.name());
		StyleSettingTool.setMargin(tabSetting.name().margin(), params);
		layout.addView(text, params);
		
		return layout;
	}
	
	private Handler createHandler() {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == WHAT_UPGRADE) {
					upgradeService = new UpgradeServiceImpl(MainActivity.this);
					upgradeService.upgrade(ClientParameter.SOFTWARE_UPGRADE);
				}
			}
		};
	}
	
	@Override
	protected void onStart() {
		KeepAliveManager.startService();
		super.onStart();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int tab_index = intent.getIntExtra(TAB_INDEX, -1);
		System.out.println("MainActivity newIntent... : " + tab_index);
		if(tab_index == REMOTE_AD_INDEX) {
			host.setCurrentTab(0);
			sendIntentMessage();
		}
	}
	
	protected void sendIntentMessage() {
		Intent intent = new Intent(AppUtil.RECEIVER_NEW_ADVERTISEMENT_MSG);
		sendBroadcast(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 0, 0, "修改密码").setIcon(android.R.drawable.ic_menu_edit);
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "更改账号").setIcon(android.R.drawable.ic_menu_edit);
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "退出").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 0: {
			Intent loginIntent = createContent(UserPasswordManagerActivity.class);
			startActivity(loginIntent);
			break;
		}
		case Menu.FIRST + 1: {
			uaService.logout();
			KeepAliveManager.stopService();
			KeepAliveManager.stopPolling();
			Intent loginIntent = createContent(LoginActivity.class);
			startActivity(loginIntent);
			finish();
			break;
		}
		case Menu.FIRST + 2: {
			uaService.logout();
			KeepAliveManager.stopService();
			KeepAliveManager.stopPolling();
			DownloadServiceManager.instance().stopAllServices();
			finish();
			break;
		}
		default:
			break;
		}
		return true;
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}
	
	private Intent createContent(Class<?> activityClass) {
		return new Intent(this, activityClass);
	}
	
	@Override
	protected void onDestroy() {
		if(handler != null) {
			handler.removeMessages(WHAT_UPGRADE);
		}
		if(upgradeService != null) {
			upgradeService.destroy();
		}
		super.onDestroy();
	}
	
}
