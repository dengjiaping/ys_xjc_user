/*
 * Copyright (C) 2014 The Android Client Of QK Project
 * 
 *     The BeiJing PingChuanJiaHeng Technology Co., Ltd.
 * 
 * Author:Yang ZiTian
 * You Can Contact QQ:646172820 Or Email:mail_yzt@163.com
 */
package com.hemaapp.hm_FrameWork.chat;

import xtom.frame.XtomObject;
import android.content.Context;

/**
 * 
 */
public class HemaChat extends XtomObject implements Runnable {
	private Context context;
	private boolean isRuning;
	private ChatConnection chatConnection;

	private static HemaChat hemaChat;

	private HemaChat(Context context) {
		this.context = context.getApplicationContext();
	}

	public static HemaChat getInstance(Context context) {
		if (hemaChat == null) {
			return hemaChat = new HemaChat(context);
		}
		return hemaChat;
	}

	public void init() {
		// 初始化ChatConnection，并将Application作为参数传入
		chatConnection = ChatConnection.getInstance(context);
		if (!isRuning) {
			isRuning = true;
			Thread t = new Thread(this);
			t.start();
		}
	}

	@Override
	public void run() {
		while (isRuning) {
			if (isConnectServer()) {
				isRuning = false;
			}
			try {
				Thread.sleep(1000 * 30);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

	public boolean isConnectServer() {
		if (!chatConnection.isConnected())
			return chatConnection.connectServer();
		return true;
	}

	public void release() {
		isRuning = false;
		if (chatConnection != null) {
			chatConnection.disconnect();
			chatConnection.release();
		}
		ChatDBClient.realse();
		FirPagDBClient.realse();
	}
}
