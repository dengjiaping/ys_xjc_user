package com.hemaapp.hm_FrameWork.chat;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomTimeUtil;
import android.content.Context;

import com.hemaapp.HemaConfig;
import com.hemaapp.hm_FrameWork.HemaApplication;
import com.hemaapp.hm_FrameWork.HemaUtil;

/**
 * 聊天消息接收监听
 * 
 */
public class ChatReceiver extends XtomObject implements ChatManagerListener {

	private static Context appContext;
	private ChatDBClient mChatDbClient;
	private String cacheDir;
	private ChatNotice mChatNotice;
	private ChatFresh mChatFresh;
	private static OnNewMessageListener onNewMessageListener;
	private static boolean mediaNoticeEnable = true;

	private static String shieldIds;// 屏蔽的私聊ID串
	private static String shieldGroupIds;// 屏蔽的群聊ID串
	private static String noNoticeGroupIds;// 接收但不提醒的群聊ID串
	private static String noNoticeIds;  //接收但不提醒的私聊ID串

	public ChatReceiver(Context appContext) {
		super();
		ChatReceiver.appContext = appContext;
		mChatDbClient = ChatDBClient.get(appContext);

		cacheDir = XtomFileUtil.getCacheDir(appContext) + "chat/" // 保存聊天记录的地址
				+ ChatConfig.getLOGIN_CID(appContext) + "/"; // todo
		mChatNotice = new ChatNotice(appContext);
		mChatFresh = new ChatFresh(appContext);
		File file = new File(cacheDir);
		if (!file.exists())
			file.mkdirs();
	}

	@Override
	public void chatCreated(Chat chat, boolean create) {
		chat.addMessageListener(new MesListener());
	}

	// 处理接收到的消息
	public void dealMessage(Message message, boolean isOffline) {
		log_d(message.toXML());
		boolean chatdeal = dealChatMess(message, isOffline);

		if (chatdeal) {
			log_w("dealMessage -- pakid= " + message.getPacketID()); // mlp
			String dxgroupid = get(message, "dxgroupid");
			if (!HemaUtil.isAppOnForeground(appContext)) {// 如果程序不在前台运行的话,发送通知并响铃

				if ("0".equals(dxgroupid)) { // 单聊消息
					if (onNewMessageListener != null
							&& !isNoNoticeMessage(message))
						onNewMessageListener.notice(message);
					if (mediaNoticeEnable && !isNoNoticeMessage(message))
						mChatNotice.mediaNotice();
				} else {  //群聊消息
					if (onNewMessageListener != null
							&& !isNoNoticeGroupMessage(message))
						onNewMessageListener.notice(message);
					if (mediaNoticeEnable && !isNoNoticeGroupMessage(message))
						mChatNotice.mediaNotice();
				}

			} else {// 如果程序在前台运行的话,只响铃,并刷新界面
				if("0".equals(dxgroupid)){  //私聊消息
					if (mediaNoticeEnable && !isNoNoticeMessage(message))
						mChatNotice.mediaNotice();
					mChatFresh.fresh();
				}else{  //群聊消息
					if (mediaNoticeEnable && !isNoNoticeGroupMessage(message))
						mChatNotice.mediaNotice();
					mChatFresh.fresh();
				}
			}
		}
	}

	// 是否是接收但不提醒的群聊消息
	private boolean isNoNoticeGroupMessage(Message message) {
		String dxgroupid = get(message, "dxgroupid");
		if (noNoticeGroupIds != null) {
			noNoticeGroupIds = getNoNoticeGroupIds();
			String[] ids = noNoticeGroupIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals(dxgroupid)) {
					log_w("该消息为接收但不提醒的群聊消息");
					return true;
				}
			}
		}
		return false;
	}

	private boolean isNoNoticeMessage(Message message) {
		String fromjid = message.getFrom().split("@")[0];
		if (noNoticeIds != null) {
			noNoticeIds = getNoNoticeIds();
			String[] ids = noNoticeIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals(fromjid)) {
					log_w("该消息为接收但不提醒的私聊消息");
					return true;
				}
			}
		}
		return false;
	}
	
	// 转换离线时间戳
	private String transUTCTime(String utcTime) {
		utcTime = utcTime.replace("\"", "");
		log_i("离线时间戳=" + utcTime);
		String time;
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
			java.util.Date UTCDate = null;
			format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			UTCDate = format.parse(utcTime);
			format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			format.applyPattern("yyyy-MM-dd HH:mm:ss");
			time = format.format(UTCDate);
		} catch (ParseException e) {
			e.printStackTrace();
			log_d("转换离线时间戳失败,使用当前时间");
			time = XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
		}
		return time;
	}

	// 获取离线时间戳
	private String getUTCTime(String xml) {
		String startString = "<item node=";
		String endString = "/></offline>";
		int start = xml.indexOf(startString) + 11;
		int end = xml.indexOf(endString);
		String utc = xml.substring(start, end);
		return utc;
	}

	// 处理普通聊天消息
	private boolean dealChatMess(Message message, boolean isOffline) {
		String fromjid = message.getFrom().split("@")[0];

		// 处理屏蔽私聊消息
		shieldIds = getShieldIds();
		if (shieldIds != null) {
			String[] ids = shieldIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals(fromjid)) {
					log_w("该消息已被屏蔽");
					return false;
				}
			}
		}
		// 群组主键ID (单聊时固定为0，群聊时即为群组主键；客户端可以根据此值是否为0来判断是单聊还是群聊)
		String dxgroupid = get(message, "dxgroupid");
		// 处理屏蔽群聊消息
		shieldGroupIds = getShieldGroupIds();
		if (shieldGroupIds != null) {
			String[] ids = shieldGroupIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals(dxgroupid)) {
					log_w("该消息已被屏蔽");
					return false;
				}
			}
		}

		// 自己发的不处理
		if (ChatConfig.getLOGIN_CID(appContext).equals(fromjid)) {
			log_w("this is my sent");
			return false;
		}

		// 自定义数据包类型，取值范围：1：文本 2：图片 3：音频 4：视频
		String dxpacktype = get(message, "dxpacktype");
		// 发送用户类型，赋值规则: 1：A型用户 2：B型用户（随业务而异，否则固定传1）
		String dxclientype = get(message, "dxclientype");
		// 发送用户昵称，赋值规则: 名称字符串
		String dxclientname = get(message, "dxclientname");
		// 发送用户头像，赋值规则: http://图片绝对地址
		String dxclientavatar = get(message, "dxclientavatar");
		// 当dxpacktype=3或4时，封装相关属性串，多个以英文逗号分隔
		String dxdetail = get(message, "dxdetail");
		// 业务耦合扩展属性存储区，多个以英文逗号分隔
		String dxextend = get(message, "dxextend");
		// 群组标题
		String dxgroupname = get(message, "dxgroupname");
		// 群组图片
		String dxgroupavatar = get(message, "dxgroupavatar");

		String dxpackid = message.getPacketID();
		if (isNull(dxpackid)) {
			dxpackid = ChatFunction.getPackIdStr();
		}

		// String fromjid = message.getFrom().split("@")[0];
		String tojid = message.getTo().split("@")[0];
		String stanza = message.getBody();
		String isread = "0";
		String islisten = "0";
		String issend = "1";
		String regdatedatetime = isOffline ? transUTCTime(getUTCTime(message
				.toXML())) : XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
		
		if((!"0".equals(dxgroupid)) && HemaConfig.DXPACKTYPE.equals(dxpacktype)){   //群红包
			if(stanza.contains(HemaConfig.CHAT_SEPARTOR)){
				String data[] = stanza.split(HemaConfig.CHAT_SEPARTOR);
				if(data.length == 2){
					if(!ChatConfig.getLOGIN_CID(appContext).equals(data[1])){  //自己收的群红包
						return false;
					}else{
						ChatMessage chatMessage = new ChatMessage(fromjid, tojid, stanza,
								regdatedatetime, dxpacktype, dxclientavatar, dxclientname,
								dxclientype, dxgroupname, dxgroupavatar, dxgroupid, dxextend,
								dxdetail, isread, islisten, issend, null, null, dxpackid, "");
						mChatDbClient.insertOrUpdate(chatMessage);
						return true;
					}
				}else{
					return false;
				}
			}
			return false;
						
		}
		
		ChatMessage chatMessage = new ChatMessage(fromjid, tojid, stanza,
				regdatedatetime, dxpacktype, dxclientavatar, dxclientname,
				dxclientype, dxgroupname, dxgroupavatar, dxgroupid, dxextend,
				dxdetail, isread, islisten, issend, null, null, dxpackid, "");
		mChatDbClient.insertOrUpdate(chatMessage);
		return true;

		
	}

	private String get(Message message, String name) {
		Object obj = message.getProperty(name); // 根据name，获取对应的属性值
		return obj == null ? null : obj.toString();
	}

	private class MesListener implements MessageListener {

		@Override
		public void processMessage(Chat chat, Message message) {
			log_d("接收到来自" + message.getFrom() + "的消息");
			// 过滤非chat信息
			if (!"chat".equals(message.getType().toString()))
				return;
			// 重复收到同一条消息时不做处理
			// if (mChatDbClient.isExist(get(message, "xtompackid")))
			// return;
			if (message.getBody() != null) {
				dealMessage(message, false);
			}
		}
	}

	/**
	 * @return 屏蔽私聊消息的id串
	 */
	public static String getShieldIds() {
		if (appContext == null)
			appContext = HemaApplication.getInstance();
		if (shieldIds == null) {
			shieldIds = XtomSharedPreferencesUtil.get(appContext,
					"chat_shieldIds");
		}
		return shieldIds;
	}

	/**
	 * @param shieldIds
	 *            屏蔽私聊消息的id串
	 */
	public static void setShieldIds(String shieldIds) {
		if (appContext == null)
			appContext = HemaApplication.getInstance();
		XtomSharedPreferencesUtil.save(appContext, "chat_shieldIds", shieldIds);
		ChatReceiver.shieldIds = shieldIds;
	}

	/**
	 * @return 屏蔽群聊消息的id串
	 */
	public static String getShieldGroupIds() {
		if (appContext == null)
			appContext = HemaApplication.getInstance();
		if (shieldGroupIds == null) {
			shieldGroupIds = XtomSharedPreferencesUtil.get(appContext,
					"chat_shieldGroupIds");
		}
		return shieldGroupIds;
	}

	/**
	 * @param shieldGroupIds
	 *            屏蔽群聊消息的id串
	 */
	public static void setShieldGroupIds(String shieldGroupIds) {
		if (appContext == null)
			appContext = HemaApplication.getInstance();
		XtomSharedPreferencesUtil.save(appContext, "chat_shieldGroupIds",
				shieldGroupIds);
		ChatReceiver.shieldGroupIds = shieldGroupIds;
	}

	/**
	 * @return 接收但不提醒的群聊id串
	 */
	public static String getNoNoticeGroupIds() {
		if (appContext == null)
			appContext = HemaApplication.getInstance();
		if (noNoticeGroupIds == null) {
			noNoticeGroupIds = XtomSharedPreferencesUtil.get(appContext,
					"chat_noNoticeGroupIds");
		}
		return noNoticeGroupIds;
	}
	/**
	 * 
	 * @return  接收但不提醒的私聊id串
	 */
	public static String getNoNoticeIds() {
		if (appContext == null) {
			appContext = HemaApplication.getInstance();
		}

		if (noNoticeIds == null) {
			noNoticeIds = XtomSharedPreferencesUtil.get(appContext,
					"chat_noNoticeIds");
		}
		return noNoticeIds;
	}

	/**
	 * @param noNoticeGroupIds
	 *            接收但不提醒的群聊id串
	 */
	public static void setNoNoticeGroupIds(String noNoticeGroupIds) {
		if (appContext == null)
			appContext = HemaApplication.getInstance();
		XtomSharedPreferencesUtil.save(appContext, "chat_noNoticeGroupIds",
				noNoticeGroupIds);
		ChatReceiver.noNoticeGroupIds = noNoticeGroupIds;
	}
	
	/**
	 * 
	 * @param noNoticeIds 接收但不提醒的私聊id串
	 */
	public static void setNoNoticeIds(String noNoticeIds){
		if(appContext == null){
			appContext = HemaApplication.getInstance();
		}
		XtomSharedPreferencesUtil.save(appContext,"chat_noNoticeIds",noNoticeIds);
		ChatReceiver.noNoticeIds = noNoticeIds;
	}

	/**
	 * @param mediaNoticeEnable
	 *            收到新消息时是否响铃(默认为true)
	 */
	public static void setMediaNoticeEnable(boolean mediaNoticeEnable) {
		ChatReceiver.mediaNoticeEnable = mediaNoticeEnable;
	}

	/**
	 * @param onNewMessageListener
	 *            the onNewMessageListener to set
	 */
	public static void setOnNewMessageListener(
			OnNewMessageListener onNewMessageListener) {
		ChatReceiver.onNewMessageListener = onNewMessageListener;
	}

	/**
	 * 新消息监听
	 */
	public interface OnNewMessageListener {
		/**
		 * 发送顶部通知
		 * 
		 * @param message
		 */
		public void notice(Message message);
	}
}
