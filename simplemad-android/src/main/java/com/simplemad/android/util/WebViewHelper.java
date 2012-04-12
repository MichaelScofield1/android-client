package com.simplemad.android.util;

import android.content.Context;
import android.webkit.WebView;

public class WebViewHelper {

	public static WebView getSystemWebView(Context context) {
		WebView wv = new WebView(context);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setSupportZoom(true);
		wv.getSettings().setBuiltInZoomControls(true);
		return wv;
	}
	
	public static WebView getDefaultWebView(Context context) {
		WebView wv = new WebView(context);
		wv.getSettings().setJavaScriptEnabled(true);
		return wv;
	}
}
