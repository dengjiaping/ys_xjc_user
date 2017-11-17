package com.hemaapp.hm_FrameWork;

import xtom.frame.net.XtomNetWorker;
import android.content.Context;

/**
 * 网络请求工具类
 */
public abstract class HemaNetWorker extends XtomNetWorker {

	public HemaNetWorker(Context mContext) {
		super(mContext);
	}

	/**
	 * token失效时自动登录方法
	 */
	public abstract void clientLogin();

	/**
	 * token失效时自动登录方法(第三方登录)
	 * 
	 * @return 如果当前用户是第三方登录的请返回true否则将自动调用{@link #clientLogin()}
	 */
	public abstract boolean thirdSave();

}
