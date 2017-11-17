package com.hemaapp.hm_FrameWork.view;

import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class HemaWebView extends WebView {

	public HemaWebView(Context context) {
		this(context, null);
	}

	public HemaWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		set();
	}

	public HemaWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		set();
	}

	private void set() {
		getSettings().setJavaScriptEnabled(true);
		getSettings().setDomStorageEnabled(true);
		getSettings().setPluginState(PluginState.ON);
		setWebViewClient(new HemaWebViewClient());
		setWebChromeClient(new WebChromeClient());
	}

	private class HemaWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			try {
				new URL(url);// 检查是否是合法的URL
				view.loadUrl(url);
				return true;
			} catch (MalformedURLException e) {
				return false;
			}

		}
	}

}
