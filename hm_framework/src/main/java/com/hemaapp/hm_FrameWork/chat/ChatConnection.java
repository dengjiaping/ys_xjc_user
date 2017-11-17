package com.hemaapp.hm_FrameWork.chat;

import java.util.Iterator;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import xtom.frame.XtomObject;
import android.content.Context;

/**
 * 聊天连接
 */
public class ChatConnection extends XtomObject {
	public static final String RESOURCE_ID = "DXResource";// 定义统一resource后缀标识，实现单点登陆（后登陆点挤掉前登陆点）
	private Context appContext;
	private XMPPConnection xmppConnection = null;
	private ChatManager mChatManager = null;
	private ChatReceiver mReceiver = null;
	private ChatConnectionListener mConnListener = null;
	private String ip;// 聊天服务器ip
	private Integer port;// 聊天服务器端口
	private String loginName;// 登录名
	private String loginPwd;// 登录密码

	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e) {
			// ignore
		}
	}

	private static ChatConnection chatConnection;

	// 单例模式,确保连接只有一个
	// 在构造函数中，初始化聊天接收器，同时获取聊天服务器的IP地址、端口号、登录名、密码
	private ChatConnection(Context appContext) {
		log_w("ChatConnection getInstance --");

		this.appContext = appContext;
		mReceiver = new ChatReceiver(appContext);
		mConnListener = new ChatConnectionListener(this);
		initParams();
	}

	private void initParams() {
		ip = ChatConfig.SERVER_IP;
		if (!isNull(ip)) {
			ChatConfig.setSERVER_IP(appContext, ip);
		} else {
			ip = ChatConfig.getSERVER_IP(appContext);
		}
		port = ChatConfig.SERVER_PORT;
		if (port!=null&&port!=0) {
			ChatConfig.setSERVER_PORT(appContext, port);
		} else {
			port = ChatConfig.getSERVER_PORT(appContext);
		}
		loginName = ChatConfig.LOGIN_CID;
		if (!isNull(loginName)) {
			ChatConfig.setLOGIN_CID(appContext, loginName);
		} else {
			loginName = ChatConfig.getLOGIN_CID(appContext);
		}
		loginPwd = ChatConfig.LOGIN_PWD;
		if (!isNull(loginPwd)) {
			ChatConfig.setLOGIN_PWD(appContext, loginPwd);
		} else {
			loginPwd = ChatConfig.getLOGIN_PWD(appContext);
		}
		Integer	noticeringid= ChatConfig.noticeringid;
		if (noticeringid!=null&&noticeringid!=0) {
			ChatConfig.setNoticeringid(appContext, noticeringid);
		} 
	}

	public static ChatConnection getInstance(Context appContext) {
		return chatConnection == null ? chatConnection = new ChatConnection(
				appContext) : chatConnection;
	}

	/**
	 * 连接服务器
	 * 
	 * @return 成功与否
	 */
	public boolean connectServer() {
		disconnect();// 如果当前有连接，断开
		try {
			log_d(loginName + " 正在登录聊天服务器[" + ip + "] " + "端口号[" + port
					+ "],请稍侯...");
			ConnectionConfiguration config = new ConnectionConfiguration(ip,
					port);
			config.setReconnectionAllowed(true); // config.setReconnectionAllowed(true);
			config.setSendPresence(false); // 将状态设置为离线

			// 如果开启离线模式，则先获取离线消息
			xmppConnection = new XMPPConnection(config);
			xmppConnection.connect();// 先连接
			if (xmppConnection.isConnected()) {
				addPing();// 添加网络心跳包响应
				configureConnection(ProviderManager.getInstance());
				xmppConnection.addConnectionListener(mConnListener); // 添加网络状态监听器
				String loginJID = loginName + "@" + ip;
				xmppConnection.login(loginJID, loginPwd, RESOURCE_ID);// 后登录
				getOfflineMsg();// 获取离线消息
				// 添加消息监听器对象
				mChatManager = xmppConnection.getChatManager();
				mChatManager.addChatListener(mReceiver);
			}

			if (xmppConnection.isAuthenticated()) {
				log_d("登录聊天服务器成功");
				return true;
			} else {
				log_d("登录聊天服务器失败");
				disconnect();
			}
		} catch (Exception e) {
			log_d("无法连接聊天服务器，具体信息：" + e.toString());
			disconnect();
		}
		return false;
	}

	// 添加网络心跳包响应
	private void addPing() {
		// openfire服务器主动跟客户端发送心跳包，如果客户端有IQ响应则表示网路连接正常，连续3次无响应链路会被关闭
		ProviderManager.getInstance().addIQProvider("ping", "urn:xmpp:ping",
				new HemaPingIQProvider());
		xmppConnection.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet p) {
				HemaPingIQ pingIQ = (HemaPingIQ) p;
				IQ pongIQ = HemaPingIQ.createResultIQ(pingIQ);
				xmppConnection.sendPacket(pongIQ);
				log_d("发送心跳包" + pongIQ.toXML());
			}
		}, new PacketTypeFilter(HemaPingIQ.class));
	}

	// 获取离线消息
	public void getOfflineMsg() {
		log_d("开始获取离线消息");
		try {
			OfflineMessageManager offlineManager = new OfflineMessageManager(
					xmppConnection);
			Iterator<Message> msgIt = offlineManager.getMessages();
			// int offlineMsgCount = offlineManager.getMessageCount();
			// log_d("离线消息总数量: " + offlineMsgCount);
			if (msgIt.hasNext()) {
				while (msgIt.hasNext()) {
					Message message = msgIt.next();
					mReceiver.dealMessage(message, true);
				}
				// Map<String, ArrayList<Message>> offlineMsgs = new
				// HashMap<String, ArrayList<Message>>();
				// 实现对离线消息，按照fromJID进行归类_________________begin
				// while (msgIt.hasNext()) {
				// Message message = msgIt.next();
				// String fromJID = message.getFrom().split("/")[0]; //
				// 去除/esource资源符后的JID
				//
				// if (!offlineMsgs.containsKey(fromJID)) {
				// ArrayList<Message> tempList = new ArrayList<Message>();
				// tempList.add(message);
				// offlineMsgs.put(fromJID, tempList);// 插入HashMap
				// } else {
				// offlineMsgs.get(fromJID).add(message);// 归类到早期创建的
				// // tempList
				// }
				// }
				// 实现对离线消息，按照fromJID进行归类_________________end

				// 编写离线处理方式(此处通过keySet方式遍历offlineMsgs,依次打印各JID的离线消息）
				// Set<String> keySet = offlineMsgs.keySet();
				// Iterator<String> offIt = keySet.iterator();
				// while (offIt.hasNext()) {
				// String keyJID = offIt.next();
				// ArrayList<Message> msgList = offlineMsgs.get(keyJID);
				// log_d("收到 来自【" + keyJID + "】的离线消息,内容如下:");
				// for (Message msgItem : msgList) {
				// mReceiver.dealMessage(msgItem, true);
				// }
				// }
			}// if结束

			// 记得通知服务器把离线消息删除，否则下次上线消息仍存在
			offlineManager.deleteMessages();
			// 处理完离线消息，则更改状态为上线
			Presence presence = new Presence(Presence.Type.available);
			xmppConnection.sendPacket(presence);
			log_d("获取离线消息完成");
		} catch (Exception e) {
			e.printStackTrace();
			log_d("get_offline_msg获取离线消息时出现错误，具体信息：" + e.toString());
		}
	}

	/**
	 * xmpp配置
	 */
	private void configureConnection(ProviderManager pm) {
		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Roster Exchange
		pm.addExtensionProvider("x", "jabberroster",
				new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabberevent", new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabberconference",
				new GroupChatInvitation.Provider());
		// Service Discovery # Items //解析房间列表
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info //某一个房间的信息
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabberdata", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabberdelay",
				new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}

	// 当前连接状态
	public Boolean isConnected() {
		return xmppConnection == null ? false : xmppConnection.isConnected();
	}

	/**
	 * 断开连接
	 */
	public synchronized void disconnect() {
		if (xmppConnection != null) {
			if (mChatManager != null)
				mChatManager.removeChatListener(mReceiver);
			xmppConnection.removeConnectionListener(mConnListener);
			xmppConnection.disconnect();
			xmppConnection = null;
		}
	}

	public void release() {
		chatConnection = null;
	}

	public XMPPConnection getXMPPConnection() {
		return xmppConnection;
	}

	public ChatManager getChatManager() {
		return mChatManager == null ? mChatManager = xmppConnection
				.getChatManager() : mChatManager;
	}

	/**
	 * @return the appContext
	 */
	public Context getAppContext() {
		return appContext;
	}

}
