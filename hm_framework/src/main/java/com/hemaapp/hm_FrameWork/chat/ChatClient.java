package com.hemaapp.hm_FrameWork.chat;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import xtom.frame.XtomActivity;
import xtom.frame.XtomObject;
import xtom.frame.util.XtomTimeUtil;
import android.content.Context;
import android.os.Handler;

public class ChatClient extends XtomObject {
	private String recvJID;
	private Chat chat;
	private ChatSendListener listener;
	private Context context;
	private Handler handler;
	private ChatConnection chatConnection;
	private ChatDBClient chatDBClient;

	private String dxclienttype;// 用户类型(本人)
	private String dxclientname;// 用户名称(本人)
	private String dxclientavatar;// 用户头像(本人)

	public ChatClient(String parmJID, String dxclienttype, String dxclientname,
			String dxclientavatar, ChatSendListener listener, Context context) {
		super();
		this.recvJID = parmJID + "@" + ChatConfig.getSERVER_IP(context) + "/"
				+ ChatConfig.RESOURCE_ID;
		this.listener = listener;
		this.context = context.getApplicationContext();
		this.handler = new ChatHandler(this);
		this.chatDBClient = ChatDBClient.get(context);

		this.dxclienttype = dxclienttype;
		this.dxclientname = dxclientname;
		this.dxclientavatar = dxclientavatar;
	}

	// 判断当前是否有可用网络
	private boolean hasNetWork() {
		if (listener instanceof XtomActivity) {
			XtomActivity ma = (XtomActivity) listener;
			return ma.hasNetWork();
		}
		return true;
	}

	/**
	 * 私聊发送普通text聊天消息
	 * 
	 * @param str
	 *            发送文本内容
	 * 
	 * @param to_client_id
	 *            发送对象的id, 群聊时填groupid
	 * 
	 * @param to_avator
	 *            发送对象的头像地址
	 * 
	 * @param to_nickname
	 *            发送对象的昵称
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 */
	public ChatMessage sendText_siliao(String str, String to_client_id,
			String to_avator, String to_nickname, String dxextend) {
		return sendText_siliao(str, to_client_id, to_avator, to_nickname,
				dxextend, "");
	}

	/**
	 * 私聊发送普通text聊天消息
	 * 
	 * @param content
	 *            发送文本内容
	 * 
	 * @param to_client_id
	 *            发送对象的id, 群聊时填groupid
	 * 
	 * @param to_avator
	 *            发送对象的头像地址
	 * 
	 * @param to_nickname
	 *            发送对象的昵称
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * @param myextend
	 *            我发送的消息在首页需要显示的extend字段内容
	 * 
	 */
	public ChatMessage sendText_siliao(String content, String to_client_id,
			String to_avator, String to_nickname, String dxextend,
			String myextend) {
		return sendMessage(to_client_id, content, "1", "", "", "0", dxextend,
				"", to_avator, to_nickname, "", myextend);
	}

	/**
	 * 群组发送普通text聊天消息
	 * 
	 * @param str
	 *            发送文本内容
	 * 
	 * @param dxgroupname
	 *            群组标题
	 * 
	 * @param dxgroupavatar
	 *            群组图片
	 * 
	 * @param dxgroupid
	 *            群组主键ID
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 */
	public ChatMessage sendText_qun(String str, String dxgroupname,
			String dxgroupavatar, String dxgroupid, String dxextend) {
		return sendText_qun(str, dxgroupname, dxgroupavatar, dxgroupid,
				dxextend, "");
	}

	/**
	 * 群组发送普通text聊天消息
	 * 
	 * @param content
	 *            发送文本内容
	 * 
	 * @param dxgroupname
	 *            群组标题
	 * 
	 * @param dxgroupavatar
	 *            群组图片
	 * 
	 * @param dxgroupid
	 *            群组主键ID
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * @param myextend
	 *            我发送的消息在首页需要显示的extend字段内容
	 * 
	 */
	public ChatMessage sendText_qun(String content, String dxgroupname,
			String dxgroupavatar, String dxgroupid, String dxextend,
			String myextend) {
		return sendMessage(dxgroupid, content, "1", dxgroupname, dxgroupavatar,
				dxgroupid, dxextend, "", "", "", "", myextend);
	}

	/**
	 * 私聊发送图片
	 * 
	 * @param imgPath
	 *            图片网络路径
	 * @param imgPathBig
	 *            图片网络路径(大图)
	 * 
	 * @param loc_imgPath
	 *            图片本地路径
	 * 
	 * @param to_client_id
	 *            发送对象的id, 群聊时填groupid
	 * 
	 * @param to_avator
	 *            发送对象的头像地址
	 * 
	 * @param to_nickname
	 *            发送对象的昵称
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 */
	public ChatMessage sendImage_siliao(String imgPath, String imgPathBig,
			String loc_imgPath, String to_client_id, String to_avator,
			String to_nickname, String dxextend) {
		return sendImage_siliao(imgPath, imgPathBig, loc_imgPath, to_client_id,
				to_avator, to_nickname, dxextend, "");
	}

	/**
	 * 私聊发送图片
	 * 
	 * @param imgPath
	 *            图片网络路径
	 * @param imgPathBig
	 *            图片网络路径(大图)
	 * 
	 * @param loc_imgPath
	 *            图片本地路径
	 * 
	 * @param to_client_id
	 *            发送对象的id, 群聊时填groupid
	 * 
	 * @param to_avator
	 *            发送对象的头像地址
	 * 
	 * @param to_nickname
	 *            发送对象的昵称
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * 
	 * @param myextend
	 *            我发送的消息在首页需要显示的extend字段内容
	 * 
	 */
	public ChatMessage sendImage_siliao(String imgPath, String imgPathBig,
			String loc_imgPath, String to_client_id, String to_avator,
			String to_nickname, String dxextend, String myextend) {
		return sendMessage(to_client_id, imgPath, "2", "", "", "0", dxextend,
				imgPathBig, to_avator, to_nickname, loc_imgPath, myextend);
	}

	/**
	 * 群组发送图片
	 * 
	 * @param imgPath
	 *            图片网络路径
	 * 
	 * @param imgPathBig
	 *            图片网络路径(大图)
	 * 
	 * @param loc_imgPath
	 *            图片本地路径
	 * 
	 * @param dxgroupname
	 *            群组标题
	 * 
	 * @param dxgroupavatar
	 *            群组图片
	 * 
	 * @param dxgroupid
	 *            群组主键ID
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 */
	public ChatMessage sendImage_qun(String imgPath, String imgPathBig,
			String loc_imgPath, String dxgroupname, String dxgroupavatar,
			String dxgroupid, String dxextend) {
		return sendImage_qun(imgPath, imgPathBig, loc_imgPath, dxgroupname,
				dxgroupavatar, dxgroupid, dxextend, "");
	}

	/**
	 * 群组发送图片
	 * 
	 * @param imgPath
	 *            图片网络路径
	 * 
	 * @param imgPathBig
	 *            图片网络路径(大图)
	 * 
	 * @param loc_imgPath
	 *            图片本地路径
	 * 
	 * @param dxgroupname
	 *            群组标题
	 * 
	 * @param dxgroupavatar
	 *            群组图片
	 * 
	 * @param dxgroupid
	 *            群组主键ID
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * 
	 * @param myextend
	 *            我发送的消息在首页需要显示的extend字段内容
	 */
	public ChatMessage sendImage_qun(String imgPath, String imgPathBig,
			String loc_imgPath, String dxgroupname, String dxgroupavatar,
			String dxgroupid, String dxextend, String myextend) {
		return sendMessage(dxgroupid, imgPath, "2", dxgroupname, dxgroupavatar,
				dxgroupid, dxextend, imgPathBig, "", "", loc_imgPath, myextend);
	}

	/**
	 * 私聊发送语音
	 * 
	 * @param voicePath
	 *            语音网络路径
	 * 
	 * @param loc_recPath
	 *            语音本地路径
	 * 
	 * @param voice_time
	 *            语音时长
	 * 
	 * @param to_client_id
	 *            发送对象的id, 群聊时填groupid
	 * 
	 * @param to_avator
	 *            发送对象的头像地址
	 * 
	 * @param to_nickname
	 *            发送对象的昵称
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 */
	public ChatMessage sendVoice_siliao(String voicePath, String loc_recPath,
			String voice_time, String to_client_id, String to_avator,
			String to_nickname, String dxextend) {
		return sendVoice_siliao(voicePath, loc_recPath, voice_time,
				to_client_id, to_avator, to_nickname, dxextend, "");
	}

	/**
	 * 私聊发送语音
	 * 
	 * @param voicePath
	 *            语音网络路径
	 * 
	 * @param loc_recPath
	 *            语音本地路径
	 * 
	 * @param voice_time
	 *            语音时长
	 * 
	 * @param to_client_id
	 *            发送对象的id, 群聊时填groupid
	 * 
	 * @param to_avator
	 *            发送对象的头像地址
	 * 
	 * @param to_nickname
	 *            发送对象的昵称
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * 
	 * @param myextend
	 *            我发送的消息在首页需要显示的extend字段内容
	 */
	public ChatMessage sendVoice_siliao(String voicePath, String loc_recPath,
			String voice_time, String to_client_id, String to_avator,
			String to_nickname, String dxextend, String myextend) {
		return sendMessage(to_client_id, voicePath, "3", "", "", "0", dxextend,
				voice_time, to_avator, to_nickname, loc_recPath, myextend);
	}

	/**
	 * 群聊发送语音
	 * 
	 * @param voicePath
	 *            语音网络路径
	 * 
	 * @param loc_recPath
	 *            语音本地路径
	 * 
	 * @param voice_time
	 *            语音时长
	 * 
	 * @param dxgroupname
	 *            群组标题
	 * 
	 * @param dxgroupavatar
	 *            群组图片
	 * 
	 * @param dxgroupid
	 *            群组主键ID
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 */
	public ChatMessage sendVoice_qun(String voicePath, String loc_recPath,
			String voice_time, String dxgroupname, String dxgroupavatar,
			String dxgroupid, String dxextend) {
		return sendVoice_qun(voicePath, loc_recPath, voice_time, dxgroupname,
				dxgroupavatar, dxgroupid, dxextend, "");
	}

	/**
	 * 群聊发送语音
	 * 
	 * @param voicePath
	 *            语音网络路径
	 * 
	 * @param loc_recPath
	 *            语音本地路径
	 * 
	 * @param voice_time
	 *            语音时长
	 * 
	 * @param dxgroupname
	 *            群组标题
	 * 
	 * @param dxgroupavatar
	 *            群组图片
	 * 
	 * @param dxgroupid
	 *            群组主键ID
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * 
	 * @param myextend
	 *            我发送的消息在首页需要显示的extend字段内容
	 */
	public ChatMessage sendVoice_qun(String voicePath, String loc_recPath,
			String voice_time, String dxgroupname, String dxgroupavatar,
			String dxgroupid, String dxextend, String myextend) {
		return sendMessage(dxgroupid, voicePath, "3", dxgroupname,
				dxgroupavatar, dxgroupid, dxextend, voice_time, "", "",
				loc_recPath, myextend);
	}

	/**
	 * 私聊发送视频
	 * 
	 * @param vedioPath
	 *            视频网络路径
	 * 
	 * @param loc_vedioPath
	 *            视频本地路径
	 * 
	 * @param vedio_time
	 *            视频时长
	 * 
	 * @param to_client_id
	 *            发送对象的id, 群聊时填groupid
	 * 
	 * @param to_avator
	 *            发送对象的头像地址
	 * 
	 * @param to_nickname
	 *            发送对象的昵称
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * 
	 */
	public ChatMessage sendVedio_siliao(String vedioPath, String loc_vedioPath,
			String vedio_time, String to_client_id, String to_avator,
			String to_nickname, String dxextend) {
		return sendVedio_siliao(vedioPath, loc_vedioPath, vedio_time,
				to_client_id, to_avator, to_nickname, dxextend, "");
	}

	/**
	 * 私聊发送视频
	 * 
	 * @param vedioPath
	 *            视频网络路径
	 * 
	 * @param loc_vedioPath
	 *            视频本地路径
	 * 
	 * @param vedio_time
	 *            视频时长
	 * 
	 * @param to_client_id
	 *            发送对象的id, 群聊时填groupid
	 * 
	 * @param to_avator
	 *            发送对象的头像地址
	 * 
	 * @param to_nickname
	 *            发送对象的昵称
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * @param myextend
	 *            我发送的消息在首页需要显示的extend字段内容
	 */
	public ChatMessage sendVedio_siliao(String vedioPath, String loc_vedioPath,
			String vedio_time, String to_client_id, String to_avator,
			String to_nickname, String dxextend, String myextend) {
		return sendMessage(to_client_id, vedioPath, "4", "", "", "0", dxextend,
				vedio_time, to_avator, to_nickname, loc_vedioPath, myextend);
	}

	/**
	 * 群聊发送视频
	 * 
	 * @param vedioPath
	 *            视频网络路径
	 * 
	 * @param loc_vedioPath
	 *            视频本地路径
	 * 
	 * @param vedio_time
	 *            视频时长
	 * 
	 * @param dxgroupname
	 *            群组标题
	 * 
	 * @param dxgroupavatar
	 *            群组图片
	 * 
	 * @param dxgroupid
	 *            群组主键ID
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 */
	public ChatMessage sendVedio_qun(String vedioPath, String loc_vedioPath,
			String vedio_time, String dxgroupname, String dxgroupavatar,
			String dxgroupid, String dxextend) {
		return sendVedio_qun(vedioPath, loc_vedioPath, vedio_time, dxgroupname,
				dxgroupavatar, dxgroupid, dxextend, "");
	}

	/**
	 * 群聊发送视频
	 * 
	 * @param vedioPath
	 *            视频网络路径
	 * 
	 * @param loc_vedioPath
	 *            视频本地路径
	 * 
	 * @param vedio_time
	 *            视频时长
	 * 
	 * @param dxgroupname
	 *            群组标题
	 * 
	 * @param dxgroupavatar
	 *            群组图片
	 * 
	 * @param dxgroupid
	 *            群组主键ID
	 * 
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * 
	 * @param myextend
	 *            我发送的消息在首页需要显示的extend字段内容
	 */
	public ChatMessage sendVedio_qun(String vedioPath, String loc_vedioPath,
			String vedio_time, String dxgroupname, String dxgroupavatar,
			String dxgroupid, String dxextend, String myextend) {
		return sendMessage(dxgroupid, vedioPath, "4", dxgroupname,
				dxgroupavatar, dxgroupid, dxextend, vedio_time, "", "",
				loc_vedioPath, myextend);
	}

	/**
	 * 
	 * @param tojid
	 *            接收者id(群聊时为群组id)
	 * @param stanza
	 *            发送内容
	 * @param dxpacktype
	 *            数据包类型1：文本 2：图片 3：音频 4：视频(其余待拓展)
	 * @param dxgroupname
	 *            群组名称
	 * @param dxgroupavatar
	 *            群组头像
	 * @param dxgroupid
	 *            群组id(单聊时固定为0)
	 * @param dxextend
	 *            业务耦合扩展属性存储区，多个以英文逗号分隔
	 * @param dxdetail
	 *            多媒体资源详情
	 * @param toavatar
	 *            接收者头像(群聊时传空字符串)
	 * @param tonickname
	 *            接收者名称(群聊时传空字符串)
	 * @param dxlocalpath
	 *            文件本地地址
	 * @param myextend
	 *            我发送的消息在首页需要显示的extend字段内容
	 * @return
	 */
	public ChatMessage sendMessage(String tojid, String stanza,
			String dxpacktype, String dxgroupname, String dxgroupavatar,
			String dxgroupid, String dxextend, String dxdetail,
			String toavatar, String tonickname, String dxlocalpath,
			String myextend) {
		ChatMessage cMessage = null;
		String fromjid = ChatConfig.getLOGIN_CID(context);
		if (isNull(tojid))
			tojid = dxgroupid;
		String regdatedatetime = XtomTimeUtil
				.getCurrentTime("yyyy-MM-dd HH:mm:ss");
		String dxpackid = ChatFunction.getPackIdStr();

		if (isNull(dxgroupname))
			dxgroupname = "";
		if (isNull(dxgroupavatar))
			dxgroupavatar = "";
		if (isNull(dxgroupname))
			dxgroupname = "";
		if (isNull(dxgroupid))
			dxgroupid = "";
		if (isNull(dxextend))
			dxextend = "";
		if (isNull(dxdetail))
			dxdetail = "";
		if (isNull(toavatar))
			toavatar = "";
		if (isNull(tonickname))
			tonickname = "";
		if (isNull(dxlocalpath))
			dxlocalpath = "";
		if (isNull(myextend))
			myextend = "";

		String isread = "1";
		String islisten = "0";
		String issend = "2";
		cMessage = new ChatMessage(fromjid, tojid, stanza, regdatedatetime,
				dxpacktype, dxclientavatar, dxclientname, dxclienttype,
				dxgroupname, dxgroupavatar, dxgroupid, dxextend, dxdetail,
				isread, islisten, issend, toavatar, tonickname, dxpackid,
				dxlocalpath);
		cMessage.setMyextend(myextend);
		sendMsg(cMessage);

		return cMessage;
	}

	public void sendMsg(ChatMessage cMessage) {
		new SendThread(cMessage).start();
	}

	private void send_msg(ChatMessage cMessage) throws Exception {
		Message tempMsg = new Message();
		tempMsg.setBody(cMessage.getStanza());
		set_msg_core(tempMsg, cMessage);

		chat.sendMessage(tempMsg);
	}

	private void sendMsgByGroup(ChatMessage cMessage) throws Exception {
		Message tempMsg = new Message();
		tempMsg.setType(Message.Type.chat);
		tempMsg.setTo(cMessage.getdxgroupid() + "@broadcast."
				+ ChatConfig.getSERVER_IP(context));
		tempMsg.setBody(cMessage.getStanza());
		set_msg_core(tempMsg, cMessage);

		// chat.sendMessage(tempMsg);
		chatConnection.getXMPPConnection().sendPacket(tempMsg);

	}

	// 封装协议扩展的通用属性
	private void set_msg_core(Message tempMsg, ChatMessage cMessage)
			throws XMPPException {
		String dxpacktype = cMessage.getdxpacktype();
		tempMsg.setProperty("dxpacktype", dxpacktype == null ? "" : dxpacktype);

		String dxclientype = cMessage.getdxclienttype();
		tempMsg.setProperty("dxclientype", dxclientype == null ? ""
				: dxclientype);

		String dxclientname = cMessage.getdxnickname();
		tempMsg.setProperty("dxclientname", dxclientname == null ? ""
				: dxclientname);

		String dxclientavatar = cMessage.getdxavatar();
		tempMsg.setProperty("dxclientavatar", dxclientavatar == null ? ""
				: dxclientavatar);

		String dxgroupid = cMessage.getdxgroupid();
		tempMsg.setProperty("dxgroupid", dxgroupid == null ? "" : dxgroupid);

		String dxgroupname = cMessage.getdxgroupname();
		tempMsg.setProperty("dxgroupname", dxgroupname == null ? ""
				: dxgroupname);

		String dxgroupavatar = cMessage.getdxgroupavatar();
		tempMsg.setProperty("dxgroupavatar", dxgroupavatar == null ? ""
				: dxgroupavatar);

		String dxdetail = cMessage.getdxdetail();
		tempMsg.setProperty("dxdetail", dxdetail == null ? "" : dxdetail);

		String dxextend = cMessage.getdxextend();
		tempMsg.setProperty("dxextend", dxextend == null ? "" : dxextend);

	}

	private class SendThread extends Thread {
		private ChatMessage cMessage;

		public SendThread(ChatMessage cMessage) {
			this.cMessage = cMessage;
		}

		@Override
		public void run() {
			cMessage.setIssend("2");
			chatDBClient.insertOrUpdate(cMessage);
			android.os.Message startMessage = new android.os.Message();
			startMessage.obj = cMessage;
			startMessage.what = 0;
			handler.sendMessage(startMessage);
			String groupid = cMessage.getdxgroupid();
			boolean groupmsg = false;

			if (groupid == null || "".equals(groupid)) {
				groupmsg = false;
			} else {
				if ("0".equals(groupid) == false) {
					groupmsg = true;
				}
			}

			chatConnection = ChatConnection.getInstance(context);
			if (!hasNetWork()
					|| (!chatConnection.isConnected() && !chatConnection
							.connectServer())) {
				cMessage.setIssend("0");
				chatDBClient.insertOrUpdate(cMessage);
				android.os.Message noNetWorkMessage = new android.os.Message();
				noNetWorkMessage.obj = cMessage;
				noNetWorkMessage.what = 1;
				handler.sendMessage(noNetWorkMessage);
				return;
			}
			if (chat == null) {
				chat = chatConnection.getChatManager()
						.createChat(recvJID, null);
			}

			try {
				if (groupmsg) {
					sendMsgByGroup(cMessage);
				} else {
					send_msg(cMessage);
				}
				cMessage.setIssend("1");
				chatDBClient.insertOrUpdate(cMessage);
				android.os.Message successMessage = new android.os.Message();
				successMessage.obj = cMessage;
				successMessage.what = 2;
				handler.sendMessage(successMessage);
			} catch (Exception e) {
				chat = null;// chat异常已不可用,再次发送时需重新建立chat
				cMessage.setIssend("0");
				chatDBClient.insertOrUpdate(cMessage);
				android.os.Message failedMessage = new android.os.Message();
				failedMessage.obj = cMessage;
				failedMessage.what = 3;
				handler.sendMessage(failedMessage);
			}
		}
	}

	private static class ChatHandler extends Handler {
		ChatClient client;

		public ChatHandler(ChatClient client) {
			this.client = client;
		}

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				client.listener.noStart((ChatMessage) msg.obj);
				break;
			case 1:
				client.listener.noConnect((ChatMessage) msg.obj);
				break;
			case 2:
				client.listener.sucess((ChatMessage) msg.obj);
				break;
			case 3:
				client.listener.failed((ChatMessage) msg.obj);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

}
