package com.hemaapp.hm_FrameWork.chat;

import xtom.frame.util.XtomLogger;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 */
public class DBHelper extends SQLiteOpenHelper {
	/**
	 * 聊天缓存，表名
	 */
	protected static final String CHAT_HISTORY = "pro_chat_history";

	/**
	 * 首页计数，表名
	 */
	protected static final String FIRST_PAGE_COUNT = "first_page_count";

	public DBHelper(Context context) {
		super(context, getDbname(context), null, 1);
	}

	private static String getDbname(Context context) {
		String dbName = "chat_" + ChatConfig.getLOGIN_CID(context) + ".db";
		XtomLogger.i("dbName", "dbName=" + dbName);
		return dbName;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String fir = "content text,time text,count text,dxclientid text,dxpacktype text,dxclientavatar text,dxclientname text,dxclientype text,dxgroupname text,"
				+ "dxgroupavatar,dxgroupid,dxextend,dxdetail";
		String firSQL = "create table " + FIRST_PAGE_COUNT + " (" + fir + ")";
		db.execSQL(firSQL);

		String his = "fromjid text,tojid text,stanza text,regdatedatetime text,dxpacktype text, dxclientavatar text, dxclientname text,"
				+ "dxclientype text,dxgroupname text,dxgroupavatar text,dxgroupid text,dxextend text,dxdetail text,"
				+ "isread int default 0,islisten int default 0,issend int default 0,toavatar text,tonickname text ,dxpackid text,dxlocalpath text";
		String hisSQL = "create table " + CHAT_HISTORY
				+ " (id integer primary key," + his + ")";
		db.execSQL(hisSQL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {

	}

}
