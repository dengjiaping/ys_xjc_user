package com.hemaapp.hm_FrameWork.chat;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class ChatFresh extends XtomObject {
	private Handler mHandler;
	private FreshRunable freshRunable;
	private static ArrayList<ChatFreshListener> chatFreshListeners;

	public ChatFresh(Context context) {
		super();
		Looper looper;
		if ((looper = Looper.myLooper()) != null) {
			mHandler = new Handler(looper);
		} else if ((looper = Looper.getMainLooper()) != null) {
			mHandler = new Handler(looper);
		} else {
			mHandler = null;
		}
	}

	public void fresh() {
		if (freshRunable == null)
			freshRunable = new FreshRunable();
		else
			mHandler.removeCallbacks(freshRunable);
		mHandler.postDelayed(new FreshRunable(), 500);
	}

	private class FreshRunable implements Runnable {

		@Override
		public void run() {
			if (chatFreshListeners != null)
				for (ChatFreshListener chatFreshListener : chatFreshListeners) {
					chatFreshListener.chatFresh();
				}
		}
	}

	public static void addChatFreshListener(ChatFreshListener listener) {
		if (chatFreshListeners == null)
			chatFreshListeners = new ArrayList<ChatFreshListener>();
		if (!chatFreshListeners.contains(listener))
			chatFreshListeners.add(listener);
	}

	public static void removeChatFreshListener(ChatFreshListener listener) {
		if (chatFreshListeners != null)
			chatFreshListeners.remove(listener);
	}

	public static void removeAllChatFreshListener() {
		if (chatFreshListeners != null)
			chatFreshListeners.clear();
	}

	public interface ChatFreshListener {
		public void chatFresh();
	}
}
