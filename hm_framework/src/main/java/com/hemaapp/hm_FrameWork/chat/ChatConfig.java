package com.hemaapp.hm_FrameWork.chat;

import xtom.frame.util.XtomSharedPreferencesUtil;
import android.content.Context;

/**
 *
 */
public class ChatConfig {
	// 定义服务器连接信息
	public static String SERVER_IP;
	public static Integer SERVER_PORT;// 此处端口号会由王海滨统一分配（各项目不会相同）
	public static final String RESOURCE_ID = "DXResource";// 定义统一resource后缀标识，实现单点登陆（后登陆点挤掉前登陆点）

	// 定义客户端连接信息
	public static String LOGIN_CID;
	public static String LOGIN_PWD;
	public static final Integer CLIENT_TYPE = 1; // 1:A类用户 2：B类用户

	// 通知铃音的id
	public static Integer noticeringid;

	/*
	 * 定义一个测试接收方的JID ] whbmemo：此处可以加Smack或Spark 2.6.3或XtomResource自定义后缀：
	 * (1)多点登录可以通过resource后缀区分发往哪个客户端(2)如果各种设备统一resource后缀标识则可以实现单点登陆（3）如果不加后缀，
	 * Spark优先级最高
	 */
	public static final String TEST_RECV_JID = "3@" + ChatConfig.SERVER_IP
			+ "/" + RESOURCE_ID;

	// 是否启用离线消息接收模式，默认启用
	public static final Boolean OFFLINE_MODE = true;

	/**
	 * 分页获取聊天记录时,一页记录数
	 */
	public static final int CHATPAGE_COUNT = 10;

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_IMG = 2;
	public static final int TYPE_RECORD = 3;
	public static final int TYPE_VIDEO = 4;

	/**
	 * @return the sERVER_IP
	 */
	public static String getSERVER_IP(Context context) {
		if (SERVER_IP == null) {
			SERVER_IP = XtomSharedPreferencesUtil
					.get(context, "chat_server_ip");
		}
		return SERVER_IP;
	}

	/**
	 * @param sERVER_IP
	 *            the sERVER_IP to set
	 */
	public static void setSERVER_IP(Context context, String sERVER_IP) {
		SERVER_IP = sERVER_IP;
		XtomSharedPreferencesUtil.save(context, "chat_server_ip", sERVER_IP);
	}

	/**
	 * @return the sERVER_PORT
	 */
	public static Integer getSERVER_PORT(Context context) {
		if (SERVER_PORT == null) {
			String port = XtomSharedPreferencesUtil.get(context,
					"chat_server_port");
			SERVER_PORT = Integer.parseInt(port);
		}
		return SERVER_PORT;
	}

	/**
	 * @param sERVER_PORT
	 *            the sERVER_PORT to set
	 */
	public static void setSERVER_PORT(Context context, Integer sERVER_PORT) {
		if(sERVER_PORT==null) return;
		XtomSharedPreferencesUtil.save(context, "chat_server_port",
				sERVER_PORT.toString());
		SERVER_PORT = sERVER_PORT;
	}

	/**
	 * @return the lOGIN_CID
	 */
	public static String getLOGIN_CID(Context context) {
		if (LOGIN_CID == null) {
			LOGIN_CID = XtomSharedPreferencesUtil
					.get(context, "chat_login_cid");
		}
		return LOGIN_CID;
	}

	/**
	 * @param lOGIN_CID
	 *            the lOGIN_CID to set
	 */
	public static void setLOGIN_CID(Context context, String lOGIN_CID) {
		XtomSharedPreferencesUtil.save(context, "chat_login_cid", lOGIN_CID);
		LOGIN_CID = lOGIN_CID;
	}

	/**
	 * @return the lOGIN_PWD
	 */
	public static String getLOGIN_PWD(Context context) {
		if (LOGIN_PWD == null) {
			LOGIN_PWD = XtomSharedPreferencesUtil
					.get(context, "chat_login_pwd");
		}
		return LOGIN_PWD;
	}

	/**
	 * @param lOGIN_PWD
	 *            the lOGIN_PWD to set
	 */
	public static void setLOGIN_PWD(Context context, String lOGIN_PWD) {
		XtomSharedPreferencesUtil.save(context, "chat_login_pwd", lOGIN_PWD);
		LOGIN_PWD = lOGIN_PWD;
	}

	/**
	 * @return the noticeringid
	 */
	public static Integer getNoticeringid(Context context) {
		if (noticeringid == null) {
			String id = XtomSharedPreferencesUtil.get(context,
					"chat_noticeringid");
			noticeringid = Integer.parseInt(id);
		}
		return noticeringid;
	}

	/**
	 * @param noticeringid
	 *            the noticeringid to set
	 */
	public static void setNoticeringid(Context context, Integer noticeringid) {
		if(noticeringid==null) return;
		XtomSharedPreferencesUtil.save(context, "chat_noticeringid",
				noticeringid.toString());
		ChatConfig.noticeringid = noticeringid;
	}

}
