package com.hemaapp.hm_FrameWork.chat;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomLogger;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.hemaapp.hm_FrameWork.dialog.HemaButtonDialog;
import com.hemaapp.hm_FrameWork.dialog.HemaButtonDialog.OnButtonListener;

/***
 * 功能描述：[网络连接监听器类，专用于处理网络连接异常情况]
 */
public class ChatConnectionListener implements ConnectionListener {
	private static final String TAG = "XtomConnListener";
	private ChatConnection chatConnection;
	private ExitHandler mHandler;
	private static boolean showOtherLoginDialog = true;// 是否弹出多点登录提醒
	private NetReceiver netReceiver = new NetReceiver();

	public ChatConnectionListener(ChatConnection chatConnection) {
		Looper looper;
		if ((looper = Looper.myLooper()) != null) {
			mHandler = new ExitHandler(looper);
		} else if ((looper = Looper.getMainLooper()) != null) {
			mHandler = new ExitHandler(looper);
		} else {
			mHandler = null;
		}
		this.chatConnection = chatConnection;
	}

	@Override
	public void connectionClosed() {
		XtomLogger.d(TAG, "客户端连接正常关闭");
		chatConnection.disconnect();
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		XtomLogger.d(TAG, "客户端连接被关闭，具体信息：" + e.getMessage());
		// 判断是否为在异地登陆
		boolean isLoginConflict = e.getMessage().equals(
				"stream:error (conflict)");
		if (isLoginConflict) {
			XtomLogger.d(TAG, "客户端连接被关闭，原因：当前账号已在其他地方登陆！");
			if (showOtherLoginDialog) {
				XtomLogger.d(TAG, "弹出多点登录弹窗");
				chatConnection.disconnect();
				mHandler.sendEmptyMessage(0);// 弹出多点登录提示
			} else {
				XtomLogger.d(TAG, "无需弹窗,将尝试自动重新连接服务器...");
				reconnect();
			}
		} else {
			XtomLogger.d(TAG, "客户端连接异常，将尝试自动重新连接服务器...");
			reconnect();
		}
	}

	// 重新登录(由于采用smack的自动登录,老是出现未知错误,所以此处采用断开连接重新登录模式)
	private void reconnect() {
		if (chatConnection != null)
			chatConnection.disconnect();
		Context context = chatConnection.getAppContext();
		if (hasNetWork(context)) {
			XtomLogger.d(TAG, "有网络连接，重新连接服务器...");
			HemaChat chat = HemaChat
					.getInstance(chatConnection.getAppContext());
			chat.init();
		} else {
			XtomLogger.d(TAG, "无网络连接，注册网络监听...");
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			context.registerReceiver(netReceiver, filter);
		}
	}

	@Override
	public void reconnectingIn(int arg0) {
		// XtomLogger.d(TAG, "reconnectingIn "+arg0);
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		XtomLogger.d(TAG, "reconnectionFailed");
	}

	@Override
	public void reconnectionSuccessful() {
		XtomLogger.d(TAG, "reconnectionSuccessful");
		XMPPConnection xmppConnection = chatConnection.getXMPPConnection();
		chatConnection.getOfflineMsg();// 重新连接之后先获取离线消息
		if (xmppConnection != null)
			xmppConnection.sendPacket(new Presence(Presence.Type.available)); // 第一时间告知服务器自己上线，非常重要
	}

	// 多点登录提示
	private static class ExitHandler extends Handler {
		private HemaButtonDialog buttonDialog;

		public ExitHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			final Activity mContext = XtomActivityManager.getLastActivity();
			if (mContext == null)
				return;

			buttonDialog = new HemaButtonDialog(mContext);
			buttonDialog.setText("您的账号已在其他设备登录\n是否重新登录？");
			buttonDialog.setLeftButtonText("退出账号");
			buttonDialog.setRightButtonText("重新登录");
			buttonDialog.setButtonListener(new OnButtonListener() {

				@Override
				public void onRightButtonClick(HemaButtonDialog dialog) {
					dialog.cancel();
					if (onOtherLoginListener != null)
						onOtherLoginListener.onRelogin();
				}

				@Override
				public void onLeftButtonClick(HemaButtonDialog dialog) {
					dialog.cancel();
					if (onOtherLoginListener != null)
						onOtherLoginListener.onCancel();
				}
			});

			super.handleMessage(msg);
		}
	}

	private static OnOtherLoginListener onOtherLoginListener;

	/**
	 * @param onOtherLoginListener
	 *            the onOtherLoginListener to set
	 */
	public static void setOnOtherLoginListener(
			OnOtherLoginListener onOtherLoginListener) {
		ChatConnectionListener.onOtherLoginListener = onOtherLoginListener;
	}

	public interface OnOtherLoginListener {
		/**
		 * 取消
		 */
		public void onCancel();

		/**
		 * 重新登录
		 */
		public void onRelogin();
	}

	/**
	 * @return the showOtherLoginDialog
	 */
	public static boolean isShowOtherLoginDialog() {
		return showOtherLoginDialog;
	}

	/**
	 * @param showOtherLoginDialog
	 *            the showOtherLoginDialog to set
	 */
	public static void setShowOtherLoginDialog(boolean showOtherLoginDialog) {
		ChatConnectionListener.showOtherLoginDialog = showOtherLoginDialog;
	}

	public class NetReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (hasNetWork(context)) {
				XtomLogger.d(TAG, "网络变化-有网，重新连接聊天服务器...");
				HemaChat chat = HemaChat.getInstance(chatConnection
						.getAppContext());
				chat.init();
				chatConnection.getAppContext().unregisterReceiver(netReceiver);
			} else {
				XtomLogger.d(TAG, "网络变化-无网，断开连接聊天服务器...");
				if (chatConnection != null)
					chatConnection.disconnect();
			}
		}

	}

	/**
	 * 判断当前是否有可用网络
	 * 
	 * @return 如果有true否则false
	 */
	public boolean hasNetWork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = con.getActiveNetworkInfo();// 获取可用的网络服务
		return info != null && info.isAvailable();
	}
}
