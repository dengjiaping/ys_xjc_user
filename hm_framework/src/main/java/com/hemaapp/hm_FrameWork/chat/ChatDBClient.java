package com.hemaapp.hm_FrameWork.chat;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomLogger;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 聊天
 */
public class ChatDBClient extends DBHelper {
	private static ChatDBClient mClient;
	private static Context mContext;

	private SQLiteDatabase mDatabase;

	private AtomicInteger mOpenCounter = new AtomicInteger();

	protected static final String QUN_CHAT_HISTORY = "qun_chat_history_";

	private String columns = "fromjid,tojid,stanza,regdatedatetime,dxpacktype,dxclientavatar,dxclientname,dxclientype,"
			+ "dxgroupname,dxgroupavatar,dxgroupid,dxextend,dxdetail,isread,islisten,issend,"
			+ "toavatar,tonickname,dxpackid,dxlocalpath";

	private String updateColums = "fromjid=?,tojid=?,stanza=?,regdatedatetime=?,dxpacktype=?,dxclientavatar=?,dxclientname=?,dxclientype=?,"
			+ "dxgroupname=?,dxgroupavatar=?,dxgroupid=?,dxextend=?,dxdetail=?,isread=?,islisten=?,issend=?,"
			+ "toavatar=?,tonickname=?";

	private ChatDBClient(Context context) {
		super(context);
	}

	public synchronized static ChatDBClient get(Context context) {
		mContext = context;
		return mClient == null ? mClient = new ChatDBClient(context) : mClient;
	}

	public static void realse() {
		mClient = null;
	}

	public synchronized boolean insertOrUpdate(ChatMessage chat) { // 根据ChatMessage中的XtomPackid来判断是插入还是更新
		if (isExist(chat)) {
			String dxgroupid = chat.getdxgroupid();
			if (dxgroupid == null || "".equals(dxgroupid)
					|| "0".equals(dxgroupid)) {
				return update(chat);
			} else {
				return updateQun(chat);
			}

		} else {
			String dxgroupid = chat.getdxgroupid();
			if (dxgroupid == null || "".equals(dxgroupid)
					|| "0".equals(dxgroupid)) {
				return insert(chat);
			} else {
				return inserttoQun(chat);
			}

		}

	}

	private boolean isExist(ChatMessage chat) {
		String dxpackid = chat.getdxpackid();
		String dxgroupid = chat.getdxgroupid();
		String name;
		if (dxgroupid == null || "".equals(dxgroupid) || "0".equals(dxgroupid)) {
			name = CHAT_HISTORY;
		} else {
			name = QUN_CHAT_HISTORY + chat.getdxgroupid();
		}

		String sql = ("select * from " + name + " where dxpackid='" + dxpackid + "'");
		SQLiteDatabase db = openDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
		} catch (SQLException e) {
			if (cursor != null)
				cursor.close();
			closeDatabase();
			return false;
		}

		boolean exist = cursor != null && cursor.getCount() > 0;
		if (cursor != null)
			cursor.close();
		closeDatabase();
		return exist;
	}

	/**
	 * 插入一条聊天记录
	 * 
	 * @param chat
	 * @return
	 */
	public synchronized boolean insert(ChatMessage chat) {
		String sql = "insert into " + CHAT_HISTORY + " (" + columns
				+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] bindArgs = new Object[] { chat.getFromjid(), chat.getTojid(),
				chat.getStanza(), chat.getRegdatedatetime(),
				chat.getdxpacktype(), chat.getdxavatar(), chat.getdxnickname(),
				chat.getdxclienttype(), chat.getdxgroupname(),
				chat.getdxgroupavatar(), chat.getdxgroupid(),
				chat.getdxextend(), chat.getdxdetail(), chat.getIsread(),
				chat.getIslisten(), chat.getIssend(), chat.getToavatar(),
				chat.getTonickname(), chat.getdxpackid(), chat.getdxlocalpath() };

		SQLiteDatabase db = openDatabase();
		boolean success = true;
		try {
			db.execSQL(sql, bindArgs);
		} catch (SQLException e) {
			success = false;
		}
		closeDatabase();

		if (success) {
			// 自己发的，计数=0
			if (chat.getFromjid().equals(ChatConfig.getLOGIN_CID(mContext))) {
				freshFirstDB(chat, true);
			} else {
				freshFirstDB(chat, false);
			}
		}
		return success;
	}

	/**
	 * 插入一条聊天记录到群记录
	 * 
	 * @param chat
	 * @return
	 */
	public synchronized boolean inserttoQun(ChatMessage chat) {
		String name = QUN_CHAT_HISTORY + chat.getdxgroupid();

		String sql = "insert into " + name + " (" + columns
				+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] bindArgs = new Object[] { chat.getFromjid(), chat.getTojid(),
				chat.getStanza(), chat.getRegdatedatetime(),
				chat.getdxpacktype(), chat.getdxavatar(), chat.getdxnickname(),
				chat.getdxclienttype(), chat.getdxgroupname(),
				chat.getdxgroupavatar(), chat.getdxgroupid(),
				chat.getdxextend(), chat.getdxdetail(), chat.getIsread(),
				chat.getIslisten(), chat.getIssend(), chat.getToavatar(),
				chat.getTonickname(), chat.getdxpackid(), chat.getdxlocalpath() };

		SQLiteDatabase db = openDatabase();
		boolean success = true;
		try {
			db.execSQL("create table if not exists "
					+ name
					+ "(id integer primary key,fromjid text,tojid text,stanza text,regdatedatetime text,dxpacktype text,dxclientavatar text,dxclientname text,"
					+ "dxclientype text,dxgroupname text,dxgroupavatar text,dxgroupid text,dxextend text,dxdetail text,"
					+ "isread int default 0,islisten int default 0,issend int default 0,toavatar text,tonickname text,dxpackid text,dxlocalpath text) ");

			db.execSQL(sql, bindArgs);
		} catch (SQLException e) {
			success = false;
		}
		closeDatabase();

		// 自己发的，计数=0
		if (chat.getFromjid().equals(ChatConfig.getLOGIN_CID(mContext))) {
			freshFirstDB(chat, true);
		} else {
			freshFirstDB(chat, false);
		}
		return success;
	}

	// 更新首页计数
	private void freshFirstDB(ChatMessage chat, boolean count0) {
		FirPagDBClient client = FirPagDBClient.get(mContext);
		String content = null;
		String time = null;
		String count = null;
		String dxclientid = null;
		String dxpacktype = null;
		String dxclientavatar = null;
		String dxclientname = null;
		String dxclientype = null;
		String dxgroupname = null;
		String dxgroupavatar = null;
		String dxgroupid = null;
		String dxextend = null;
		String dxdetail = null;

		content = chat.getStanza();
		time = chat.getRegdatedatetime();
		dxclientid = chat.getFromjid();
		dxpacktype = chat.getdxpacktype();
		dxclientavatar = chat.getdxavatar();
		dxclientname = chat.getdxnickname();
		dxclientype = chat.getdxclienttype();
		dxgroupname = chat.getdxgroupname();
		dxgroupavatar = chat.getdxgroupavatar();
		dxgroupid = chat.getdxgroupid();
		dxextend = chat.getdxextend();
		dxdetail = chat.getdxdetail();

		String myextend = chat.getMyextend();
		if (!XtomBaseUtil.isNull(myextend)) {
			dxextend = myextend;
		}

		if (dxclientid.equals(ChatConfig.getLOGIN_CID(mContext))) {
			if ("0".equals(dxgroupid)) {
				dxclientid = chat.getTojid();
				dxclientname = chat.getTonickname();
				dxclientavatar = chat.getToavatar();
			}
		}

		FirPagCount fpc = client.select(dxclientid, dxgroupid);

		if (fpc == null) {
			if (count0)
				count = "0";
			else
				count = "1";
			fpc = new FirPagCount(content, time, count, dxclientid, dxpacktype,
					dxclientavatar, dxclientname, dxclientype, dxgroupname,
					dxgroupavatar, dxgroupid, dxextend, dxdetail);
		} else {
			fpc.setTime(time);

			if (count0) {
				count = "0";
			} else {
				int c = Integer.valueOf(fpc.getcount());
				c++;
				fpc.setCount(String.valueOf(c));
			}

			fpc.setDxpacktype(dxpacktype);
			fpc.setdxclientname(dxclientname);
			fpc.setDxclientavatar(dxclientavatar);
			fpc.setContent(content);
		}

		client.insertOrUpdate(fpc);
	}

	/**
	 * 更新
	 * 
	 * @param chat
	 * @return
	 */
	public synchronized boolean update(ChatMessage chat) {
		String conditions = "dxpackid='" + chat.getdxpackid() + "'";
		String sql = "update " + CHAT_HISTORY + " set " + updateColums
				+ " where " + conditions;

		Object[] bindArgs = new Object[] { chat.getFromjid(), chat.getTojid(),
				chat.getStanza(), chat.getRegdatedatetime(),
				chat.getdxpacktype(), chat.getdxavatar(), chat.getdxnickname(),
				chat.getdxclienttype(), chat.getdxgroupname(),
				chat.getdxgroupavatar(), chat.getdxgroupid(),
				chat.getdxextend(), chat.getdxdetail(), chat.getIsread(),
				chat.getIslisten(), chat.getIssend(), chat.getToavatar(),
				chat.getTonickname() };

		SQLiteDatabase db = openDatabase();
		boolean success = true;
		try {
			db.execSQL(sql, bindArgs);
		} catch (SQLException e) {
			success = false;
		}
		closeDatabase();
		return success;

	}

	/**
	 * 更新群消息
	 * 
	 * @param chat
	 * @return
	 */
	public synchronized boolean updateQun(ChatMessage chat) {
		String name = QUN_CHAT_HISTORY + chat.getdxgroupid();

		String conditions = "dxpackid='" + chat.getdxpackid() + "'";
		String sql = "update " + name + " set " + updateColums + " where "
				+ conditions;

		Object[] bindArgs = new Object[] { chat.getFromjid(), chat.getTojid(),
				chat.getStanza(), chat.getRegdatedatetime(),
				chat.getdxpacktype(), chat.getdxavatar(), chat.getdxnickname(),
				chat.getdxclienttype(), chat.getdxgroupname(),
				chat.getdxgroupavatar(), chat.getdxgroupid(),
				chat.getdxextend(), chat.getdxdetail(), chat.getIsread(),
				chat.getIslisten(), chat.getIssend(), chat.getToavatar(),
				chat.getTonickname() };

		SQLiteDatabase db = openDatabase();
		boolean success = true;
		try {
			db.execSQL(sql, bindArgs);
		} catch (SQLException e) {
			success = false;
		}
		closeDatabase();
		return success;

	}

	/**
	 * 获取新收到的群聊消息
	 * 
	 * @param groupid
	 *            专题id
	 * @param maxid
	 *            当前已有记录中的最大id
	 * @return
	 */
	public ArrayList<ChatMessage> selectGroupNew(String groupid, int maxid) {
		String name = QUN_CHAT_HISTORY + groupid;

		String columns = this.columns + ",id";
		String conditions;

		if (maxid == 0) {
			conditions = " ORDER BY id DESC LIMIT 0,"
					+ ChatConfig.CHATPAGE_COUNT;
		} else {
			conditions = " where id>" + maxid + " ORDER BY id DESC";
		}

		String sql = "select " + columns + " from " + name + conditions;

		SQLiteDatabase db = openDatabase();
		ArrayList<ChatMessage> chats = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
		} catch (SQLException e) {

		}

		if (cursor != null && cursor.getCount() > 0) {
			chats = new ArrayList<ChatMessage>();
			cursor.moveToFirst();
			ChatMessage message;
			for (int i = 0; i < cursor.getCount(); i++) {
				message = new ChatMessage(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10),
						cursor.getString(11), cursor.getString(12),
						cursor.getString(13), cursor.getString(14),
						cursor.getString(15), cursor.getString(16),
						cursor.getString(17), cursor.getString(18),
						cursor.getString(19));
				message.setId(cursor.getInt(20));
				chats.add(0, message);
				cursor.moveToNext();
			}

		}
		if (cursor != null)
			cursor.close();
		closeDatabase();
		return chats;

	}

	/**
	 * 分页获取群聊消息
	 * 
	 * @param groupid
	 *            专题id
	 * @return
	 */
	public ArrayList<ChatMessage> selectGroupPaging(String groupid, int minid) {
		String name = QUN_CHAT_HISTORY + groupid;

		String columns = this.columns + ",id";
		String minidStr = minid == 0 ? "" : " where id<" + minid;

		String conditions = minidStr + " ORDER BY id DESC LIMIT 0,"
				+ ChatConfig.CHATPAGE_COUNT;

		String sql = "select " + columns + " from " + name + conditions;

		SQLiteDatabase db = openDatabase();
		ArrayList<ChatMessage> chats = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
		} catch (SQLException e) {

		}

		if (cursor != null && cursor.getCount() > 0) {
			chats = new ArrayList<ChatMessage>();
			cursor.moveToFirst();
			ChatMessage message;
			for (int i = 0; i < cursor.getCount(); i++) {
				message = new ChatMessage(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10),
						cursor.getString(11), cursor.getString(12),
						cursor.getString(13), cursor.getString(14),
						cursor.getString(15), cursor.getString(16),
						cursor.getString(17), cursor.getString(18),
						cursor.getString(19));
				message.setId(cursor.getInt(20));
				chats.add(0, message);
				cursor.moveToNext();
			}

		}
		if (cursor != null)
			cursor.close();
		closeDatabase();
		return chats;
	}

	/**
	 * 获取新接收到的oneJid和anotherJid的聊天记录
	 * 
	 * @param oneJid
	 * @param anotherJid
	 * @param maxid
	 *            当前已有记录中的最大id
	 * @return
	 */
	public ArrayList<ChatMessage> selectNew(String oneJid, String anotherJid,
			int maxid) {
		String columns = this.columns + ",id";
		String conditions;

		if (maxid == 0) {
			conditions = "((fromjid='" + oneJid + "' and tojid='" + anotherJid
					+ "') or (fromjid='" + anotherJid + "' and tojid='"
					+ oneJid + "')) ORDER BY id DESC LIMIT 0,"
					+ ChatConfig.CHATPAGE_COUNT;
		} else {
			conditions = "((fromjid='" + oneJid + "' and tojid='" + anotherJid
					+ "') or (fromjid='" + anotherJid + "' and tojid='"
					+ oneJid + "')) and id>" + maxid + " ORDER BY id DESC";
		}

		String sql = "select " + columns + " from " + CHAT_HISTORY + " where "
				+ conditions;

		SQLiteDatabase db = openDatabase();
		XtomLogger.e("ChagDBClient", "db.isopen" + db.isOpen());
		if (!db.isOpen()) {
			mClient.onOpen(db);
		}

		XtomLogger.e("ChagDBClient", "db" + db.isOpen());
		ArrayList<ChatMessage> chats = null;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			chats = new ArrayList<ChatMessage>();
			cursor.moveToFirst();
			ChatMessage message;
			for (int i = 0; i < cursor.getCount(); i++) {
				message = new ChatMessage(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10),
						cursor.getString(11), cursor.getString(12),
						cursor.getString(13), cursor.getString(14),
						cursor.getString(15), cursor.getString(16),
						cursor.getString(17), cursor.getString(18),
						cursor.getString(19));
				message.setId(cursor.getInt(20));
				chats.add(0, message);
				cursor.moveToNext();
			}
		}
		if (cursor != null)
			cursor.close();
		if (db.isOpen()) {
			closeDatabase();
		}
		return chats;

	}

	/**
	 * 分页获取oneJid和anotherJid的聊天记录
	 * 
	 * @param oneJid
	 * @param anotherJid
	 * @param minid
	 *            当前已有记录中的最小id(若没有可传0)
	 * @return
	 */
	public ArrayList<ChatMessage> selectPaging(String oneJid,
			String anotherJid, int minid) {
		String columns = this.columns + ",id";
		String minidStr = minid == 0 ? "" : " and id<" + minid;
		String conditions = "((fromjid='" + oneJid + "' and tojid='"
				+ anotherJid + "') or (fromjid='" + anotherJid
				+ "' and tojid='" + oneJid + "')) " + minidStr
				+ " ORDER BY id DESC LIMIT 0," + ChatConfig.CHATPAGE_COUNT;

		String sql = "select " + columns + " from " + CHAT_HISTORY + " where "
				+ conditions;
		SQLiteDatabase db = openDatabase();
		ArrayList<ChatMessage> chats = null;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			chats = new ArrayList<ChatMessage>();
			cursor.moveToFirst();
			ChatMessage message;
			for (int i = 0; i < cursor.getCount(); i++) {
				message = new ChatMessage(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10),
						cursor.getString(11), cursor.getString(12),
						cursor.getString(13), cursor.getString(14),
						cursor.getString(15), cursor.getString(16),
						cursor.getString(17), cursor.getString(18),
						cursor.getString(19));
				message.setId(cursor.getInt(20));
				chats.add(0, message);
				cursor.moveToNext();
			}

		}
		if (cursor != null)
			cursor.close();
		closeDatabase();

		return chats;

	}

	/**
	 * 判断聊天表是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		SQLiteDatabase db = openDatabase();
		Cursor cursor = db.rawQuery("select * from " + CHAT_HISTORY, null);
		boolean empty = 0 == cursor.getCount();
		closeDatabase();
		return empty;
	}

	/**
	 * 删除群组的聊天记录
	 * 
	 * @param groupid
	 * @return
	 */
	public boolean deleteGroup(String groupid) {
		boolean success = true;
		String tableName = QUN_CHAT_HISTORY + groupid;
		String sql = "delete from " + tableName;
		SQLiteDatabase db = openDatabase();
		try {
			db.execSQL(sql);
		} catch (Exception e) {
			success = false;
		}
		return success;
	}

	/**
	 * 删除某条聊天记录
	 * 
	 * @param chat
	 * @return
	 */
	public boolean delete(ChatMessage chat) {
		boolean success = true;
		String dxpackid = chat.getdxpackid();
		String dxgroupid = chat.getdxgroupid();
		String tableName;
		if (dxgroupid == null || "".equals(dxgroupid) || "0".equals(dxgroupid)) {
			tableName = CHAT_HISTORY;
		} else {
			tableName = QUN_CHAT_HISTORY + chat.getdxgroupid();
		}
		String conditions = " where dxpackid='" + dxpackid + "'";
		String sql = "delete from " + tableName + conditions;
		SQLiteDatabase db = openDatabase();
		try {
			db.execSQL(sql);
		} catch (Exception e) {
			success = false;
		}
		return success;

	}

	/**
	 * 删除自己与别人的聊天记录
	 * 
	 * @param anotherJid
	 *            别人的Jid
	 */
	public boolean delete(String anotherJid) {
		boolean success = true;
		String myid = ChatConfig.getLOGIN_CID(mContext);
		String conditions = "((fromjid='" + anotherJid + "' and tojid='" + myid
				+ "') or (fromjid='" + myid + "' and tojid='" + anotherJid
				+ "'))";
		String sql = "delete from " + CHAT_HISTORY + " where " + conditions;
		SQLiteDatabase db = openDatabase();
		try {
			db.execSQL(sql);
		} catch (Exception e) {
			success = false;
		}
		return success;
	}

	/**
	 * 清空私聊聊天记录
	 */
	public boolean clearPrivate() {
		boolean success = true;
		String sql = "delete from " + CHAT_HISTORY;
		SQLiteDatabase db = openDatabase();
		try {
			db.execSQL(sql);
		} catch (Exception e) {
			success = false;
		}
		return success;
	}

	/**
	 * 清空群聊聊天记录
	 */
	public boolean clearQun() {
		boolean success = true;
		// 获取聊天数据库中的所有表
		String sql = "select name from sqlite_master where type='table' order by name";
		SQLiteDatabase db = openDatabase();
		ArrayList<String> tables = null;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			tables = new ArrayList<String>();
			cursor.moveToFirst();
			String table;
			for (int i = 0; i < cursor.getCount(); i++) {
				table = cursor.getString(0);
				tables.add(table);
				cursor.moveToNext();
			}
		}
		if (cursor != null)
			cursor.close();

		// 遍历筛选清空群聊聊天表
		for (String table : tables) {
			if (table.contains(QUN_CHAT_HISTORY)) {
				XtomLogger.i("clearQun", "清空群聊表" + table);
				String delSql = "delete from " + table;
				try {
					db.execSQL(delSql);
				} catch (Exception e) {
					success = false;
				}
			}
		}

		return success;
	}

	/**
	 * 清空所有聊天消息
	 */
	public boolean clear() {
		return clearPrivate() && clearQun();
	}

	/**
	 * 更新聊天记录中ID为id的用户的昵称
	 * 
	 * @param id
	 *            用户id
	 * @param nickname
	 *            新的用户昵称
	 */
	public synchronized void updateNickname(final String id,
			final String nickname) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateDxclientname(id, nickname);
			}
		}).start();

	}

	private void updateDxclientname(String id, String nickname) {
		// 更新单聊记录
		String conditions = "fromjid='" + id + "' or tojid='" + id + "'";
		String sql = "update " + CHAT_HISTORY + " set dxclientname='"
				+ nickname + "' where " + conditions;
		SQLiteDatabase db = openDatabase();
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			// nothing
		}

		// 获取聊天数据库中的所有表
		sql = "select name from sqlite_master where type='table' order by name";
		ArrayList<String> tables = null;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			tables = new ArrayList<String>();
			cursor.moveToFirst();
			String table;
			for (int i = 0; i < cursor.getCount(); i++) {
				table = cursor.getString(0);
				tables.add(table);
				cursor.moveToNext();
			}
		}
		if (cursor != null)
			cursor.close();

		// 遍历更新群聊记录
		for (String table : tables) {
			if (table.contains(QUN_CHAT_HISTORY)) {
				conditions = "fromjid='" + id + "' or tojid='" + id + "'";
				sql = "update " + table + " set dxclientname='" + nickname
						+ "' where " + conditions;
				try {
					db.execSQL(sql);
				} catch (Exception e) {
					// nothing
				}
			}
		}
		closeDatabase();

		// 更新首页计数记录
		FirPagDBClient client = FirPagDBClient.get(mContext);
		client.updateNickname(id, nickname);
	}
	
	/**
	 * 更新聊天记录中ID为id的用户的头像
	 * @param id 
	 * @param avatar 新的用户头像
	 */
	public synchronized void updateAvatar(final String id,
			final String avatar) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateDxclientavatar(id,avatar);
			}
		}).start();

	}

	private void updateDxclientavatar(String id, String avatar) {
		// 更新单聊记录
		String conditions = "fromjid='" + id + "' or tojid='" + id + "'";
		String sql = "update " + CHAT_HISTORY + " set dxclientavatar='"
				+ avatar + "' where " + conditions;
		SQLiteDatabase db = openDatabase();
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			// nothing
		}

		// 获取聊天数据库中的所有表
		sql = "select name from sqlite_master where type='table' order by name";
		ArrayList<String> tables = null;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			tables = new ArrayList<String>();
			cursor.moveToFirst();
			String table;
			for (int i = 0; i < cursor.getCount(); i++) {
				table = cursor.getString(0);
				tables.add(table);
				cursor.moveToNext();
			}
		}
		if (cursor != null)
			cursor.close();

		// 遍历更新群聊记录
		for (String table : tables) {
			if (table.contains(QUN_CHAT_HISTORY)) {
				conditions = "fromjid='" + id + "' or tojid='" + id + "'";
				sql = "update " + table + " set dxclientavatar='" + avatar
						+ "' where " + conditions;
				try {
					db.execSQL(sql);
				} catch (Exception e) {
					// nothing
				}
			}
		}
		closeDatabase();

		// 更新首页计数记录
		FirPagDBClient client = FirPagDBClient.get(mContext);
		client.updateAvatar(id, avatar);
	}

	public synchronized SQLiteDatabase openDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			// Opening new database
			mDatabase = getWritableDatabase();
		}
		return mDatabase;
	}

	public synchronized void closeDatabase() {
		if (mOpenCounter.decrementAndGet() == 0) {
			// Closing database
			mDatabase.close();

		}
	}
}
