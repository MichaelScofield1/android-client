package com.simplemad.android.activity.content;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.simplemad.android.activity.NoTitleActivity;
import com.simplemad.android.service.UserAccountServiceImpl;
import com.simplemad.android.util.WebViewHelper;
import com.simplemad.parameter.ClientParameter;

public class UserStatisticsModifyActivity extends NoTitleActivity {

	private WebView wv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		wv = WebViewHelper.getSystemWebView(this);
		wv.loadUrl(ClientParameter.USER_STATISTICS_MODIFY + "?mobile=" + UserAccountServiceImpl.instance().getCurrentUserAccount().getMobile());
		wv.setVerticalScrollBarEnabled(false);
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		wv.setWebChromeClient(new WebChromeClient());
		setContentView(wv);
	}
	
	@Override
	public void onBackPressed() {
		if(wv.getUrl().startsWith(ClientParameter.COMPLETION)) {
			super.onBackPressed();
		} else if(wv.canGoBack()) {
			wv.goBack();
		} else {
			super.onBackPressed();
		}
		
	}
}
