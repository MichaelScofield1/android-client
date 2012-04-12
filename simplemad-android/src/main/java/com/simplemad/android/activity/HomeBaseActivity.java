package com.simplemad.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.simplemad.android.service.android.AppDisplayService;
import com.simplemad.android.util.AppUtil;

@SuppressWarnings("deprecation")
public class HomeBaseActivity extends Activity {

	@Override
	public void onAttachedToWindow() {  
		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);         
		super.onAttachedToWindow();    
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_HOME) {
			AppUtil.homePresse(this);
			startService(new Intent(this, AppDisplayService.class));
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}
	
	@Override
	protected void onResume() {
		stopService(new Intent(this, AppDisplayService.class));
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(new Intent(this, AppDisplayService.class));
	}
}
