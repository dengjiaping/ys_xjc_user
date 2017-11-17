package com.hemaapp.hm_FrameWork;

import xtom.frame.XtomConfig;
import xtom.frame.util.XtomLogger;
import android.app.Application;

import com.hemaapp.HemaConfig;

/**
 * 该项目自定义Application
 */
public class HemaApplication extends Application {
	private static final String TAG = "HemaApplication";

	private static HemaApplication application;

	public static HemaApplication getInstance() {
		return application;
	}

	@Override
	public void onCreate() {
		application = this;
		XtomConfig.TIMEOUT_CONNECT_HTTP = HemaConfig.TIMEOUT_HTTP;
		XtomConfig.TRYTIMES_HTTP = HemaConfig.TRYTIMES_HTTP;
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		XtomLogger.i(TAG, "onLowMemory");
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		XtomLogger.i(TAG, "onTerminate");
		super.onTerminate();
	}

}
