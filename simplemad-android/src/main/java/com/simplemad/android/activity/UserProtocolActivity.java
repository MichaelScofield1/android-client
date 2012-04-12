package com.simplemad.android.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.simplemad.android.R;
import com.simplemad.android.setting.AppSetting;
import com.simplemad.android.setting.UserProtocolSetting;
import com.simplemad.android.util.AppFileHelper;
import com.simplemad.android.util.AppUtil;
import com.simplemad.android.util.StyleSettingTool;
import com.simplemad.android.util.WebViewHelper;
import com.simplemad.android.view.setting.Margin;

public class UserProtocolActivity extends NoTitleActivity {

	private UserProtocolSetting setting;
	
	private Button accept;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
	}
	
	private void initUI() {
		ready();
		createUI();
	}
	
	private void ready() {
		setting = AppSetting.protocolSetting();
	}
	
	private void createUI() {
		ScrollView sv = new ScrollView(this);
		
		LinearLayout layout = new LinearLayout(this);
		StyleSettingTool.setBackground(layout, setting);
		layout.setOrientation(LinearLayout.VERTICAL);
		addWebView(layout);
		addHaveRead(layout);
		addToolView(layout);
		sv.addView(layout);
		
		setContentView(sv);
	}
	
	private void addToolView(LinearLayout parent) {
		LinearLayout tool = new LinearLayout(this);
		
		accept = new Button(this);
		StyleSettingTool.setTextStyle(accept, setting.accept());
		accept.setText("同   意");
		accept.setEnabled(false);
		accept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RequestAndResultCode.ResultCode.RESULT_USER_PROTOCOL_OK);
				finish();
			}
		});
		tool.addView(accept, buttonParams(setting.accept().margin()));
		
		Button refuse = new Button(this);
		StyleSettingTool.setTextStyle(refuse, setting.refuse());
		refuse.setText("拒   绝");
		refuse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		tool.addView(refuse, buttonParams(setting.refuse().margin()));
		
		parent.addView(tool, contentParams());
	}
	
	private void addHaveRead(LinearLayout parent) {
		CheckBox cb = new CheckBox(this);
		cb.setText("我已仔细阅读并同意《手机广告最终用户协议》");
		cb.setChecked(false);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				accept.setEnabled(isChecked);
			}
		});
		parent.addView(cb, contentParams());
	}
	
	private LinearLayout.LayoutParams contentParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(setting.margin(), params);
		return params;
	}
	
	private LinearLayout.LayoutParams buttonParams(Margin margin) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		StyleSettingTool.setMargin(margin, params);
		params.weight = 1;
		return params;
	}
	
	private void addWebView(LinearLayout parent) {
		WebView wv = createWebView();
		parent.addView(wv, contentParams());
	}
	
	private WebView createWebView() {
		WebView wv = WebViewHelper.getDefaultWebView(this);
		InputStream is = AppUtil.getRawResourceIS(R.raw.user_protocol);
		try {
			String data = new String(AppFileHelper.readFile(is), "UTF-8");
			wv.loadDataWithBaseURL(null, data, "text/plain", "UTF-8", null);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wv;
	}
}
