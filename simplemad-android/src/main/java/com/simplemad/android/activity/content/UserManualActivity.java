package com.simplemad.android.activity.content;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simplemad.android.R;
import com.simplemad.android.activity.NoTitleActivity;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.setting.UserManualSetting;
import com.simplemad.android.util.AppFileHelper;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.util.WebViewHelper;

public class UserManualActivity extends NoTitleActivity {

	private UserManualSetting setting;
	
	private int id = 1;
	private TextView title;
	private WebView wv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		initUI();
	}
	
	private void initUI() {
		ready();
		createUI();
	}
	
	private void ready() {
		setting = AppSetting.userManualSetting();
	}
	
	private void createUI() {
		RelativeLayout layout = new RelativeLayout(this);
		StyleSettingTool.setBackground(layout, setting);
		createTitle(layout);
		createContent(layout);
		setContentView(layout);
	}
	
	private void createTitle(RelativeLayout parent) {
		title = createTitle();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.alignWithParent = true;
		StyleSettingTool.setMargin(setting.title().margin(), params);
		parent.addView(title, params);
	}
	
	private TextView createTitle() {
		TextView title = new TextView(this);
		title.setId(id++);
		title.setText("使用指南");
		StyleSettingTool.setTextStyle(title, setting.title());
		return title;
	}
	
	private void createContent(RelativeLayout parent) {
		wv = createWebView();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, title.getId());
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, parent.getId());
		StyleSettingTool.setMargin(setting.contentMargin(), params);
		parent.addView(wv, params);
	}
	
	private WebView createWebView() {
		WebView wv = WebViewHelper.getDefaultWebView(this);
		wv.setVerticalScrollBarEnabled(false);
		wv.setId(id++);
		StyleSettingTool.setBackground(wv, setting.webViewBG());
		InputStream is = AppUtil.getRawResourceIS(R.raw.user_manual);
		try {
			String data = new String(AppFileHelper.readFile(is), "UTF-8");
			wv.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wv;
	}
}
