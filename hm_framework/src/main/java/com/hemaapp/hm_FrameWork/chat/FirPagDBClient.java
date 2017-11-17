package com.hemaapp.hm_FrameWork.chat;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import xtom.frame.util.XtomBaseUtil;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * 首页计数
 */
public class FirPagDBClient extends DBHelper {
	private static FirPagDBClient mClient;

	private SQLiteDatabase mDatabase;

	private AtomicInteger mOpenCounter = new AtomicInteger();

	private String columns = "content,time,count,dxclientid,dxpacktype,dxclientavatar,dxclientname,"
			+ "dxclientype,dxgroupname,dxgroupavatar,dxgroupid,dxextend,dxdetail";

	private String updateColums = "content=?,time=?,count=?,dxclientid=?,dxpacktype=?,dxclientavatar=?,dxclientname=?,"
			+ "dxclientype=?,dxgroupname=?,dxgroupavatar=?,dxgroupid=?,dxextend=?,dxdetail=?";

	private Object obj = new Object();

	private FirPagDBClient(Context context) {
		super(context);
	}

	public static FirPagDBClient get(Context context) {
		return mClient == null ? mClient = new FirPagDBClient(context)
				: mClient;
	}

	public static void realse() {
		mClient = null;
	}

	public boolean insertOrUpdate(FirPagCount count) {
		if (isExist(count)) {
			return update(count);
		} else {
			return insert(count);
		}
	}

	/**
	 * 插入一条记录
	 * 
	 * @param count
	 * @return
	 */
	public boolean insert(FirPagCount count) {
		synchronized (obj) {
			SQLiteDatabase db = openDatabase();
			boolean success = true;
			try {
				String sql = "insert into " + FIRST_PAGE_COUNT + " (" + columns
						+ ") values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

				Object[] bindArgs = new Object[] { count.getcontent(),
						count.gettime(), count.getcount(),
						count.getdxclientid(), count.getdxpacktype(),
						count.getdxclientavatar(), count.getdxclientname(),
						count.getdxclientype(), count.getdxgroupname(),
						count.getdxgroupavatar(), count.getdxgroupid(),
						count.getdxextend(), count.getdxdetail() };

				db.execSQL(sql, bindArgs);
			} catch (SQLException e) {
				success = false;
			}
			closeDatabase();
			return success;
		}
	}

	/**
	 * 更新一条记录
	 * 
	 * @param count
	 * @return
	 */
	public boolean update(FirPagCount count) {
		synchronized (obj) {
			SQLiteDatabase db = openDatabase();
			boolean success = true;

			Object[] bindArgs = new Object[] { count.getcontent(),
					count.gettime(), count.getcount(), count.getdxclientid(),
					count.getdxpacktype(), count.getdxclientavatar(),
					count.getdxclientname(), count.getdxclientype(),
					count.getdxgroupname(), count.getdxgroupavatar(),
					count.getdxgroupid(), count.getdxextend(),
					count.getdxdetail() };

			/* 根据发送者的id 或者群组id 来查找更新 */
			String groupid = count.getdxgroupid();
			if (!isGroup(groupid)) {
				try {
					String conditions = " where dxclientid='"
							+ count.getdxclientid() + "' and dxgroupid='0'";
					String sql = "update " + FIRST_PAGE_COUNT + " set "
							+ updateColums + conditions;

					db.execSQL(sql, bindArgs);
				} catch (SQLException e) {
					success = false;
				}
			} else {
				try {
					String conditions = " where dxgroupid='" + groupid + "'";
					String sql = "update " + FIRST_PAGE_COUNT + " set "
							+ updateColums + conditions;

					db.execSQL(sql, bindArgs);
				} catch (SQLException e) {
					success = false;
				}
			}

			closeDatabase();
			return success;
		}
	}

	private boolean isGroup(String groupid) {
		return !XtomBaseUtil.isNull(groupid) && !"0".equals(groupid);
	}

	/**
	 * 更新所有count值为0
	 * 
	 * @param count
	 * @return
	 */
	public boolean updateCount0() {
		synchronized (obj) {
			SQLiteDatabase db = openDatabase();
			boolean success = true;

			try {
				String sqlstr = "update " + FIRST_PAGE_COUNT + " set count='0'";
				db.execSQL(sqlstr);
			} catch (SQLException e) {
				success = false;
			}
			closeDatabase();
			return success;
		}
	}

	/**
	 * 更新count值为0
	 * 
	 * @param count
	 * @return
	 */
	public boolean updateCount0(String dxclientid, String dxgroupid) {
		synchronized (obj) {
			SQLiteDatabase db = openDatabase();
			boolean success = true;

			if (dxgroupid != null && false == "0".equals(dxgroupid)) {
				try {
					String sqlstr = "update " + FIRST_PAGE_COUNT
							+ " set count='0'" + " where dxgroupid='"
							+ dxgroupid + "'";
					db.execSQL(sqlstr);
				} catch (SQLException e) {
					success = false;
				}
			} else {
				try {
					String sqlstr = "update " + FIRST_PAGE_COUNT
							+ " set count='0'" + " where dxclientid='"
							+ dxclientid + "' and dxgroupid='0'";
					db.execSQL(sqlstr);
				} catch (SQLException e) {
					success = false;
				}
			}
			closeDatabase();
			return success;
		}
	}

	public boolean isExist(FirPagCount count) {
		synchronized (obj) {
			String groupid = count.getdxgroupid();
			String sql;
			if (!isGroup(groupid)) {
				sql = ("select * from " + FIRST_PAGE_COUNT
						+ " where dxclientid='" + count.getdxclientid() + "' and dxgroupid='0'");
			} else {
				sql = ("select * from " + FIRST_PAGE_COUNT
						+ " where dxgroupid='" + count.getdxgroupid() + "'");
			}

			SQLiteDatabase db = openDatabase();
			Cursor cursor = db.rawQuery(sql, null);
			boolean exist = cursor != null && cursor.getCount() > 0;
			cursor.close();
			closeDatabase();
			return exist;
		}
	}

	/**
	 * 删除一条记录
	 */
	public boolean delete(FirPagCount count) {
		synchronized (obj) {
			SQLiteDatabase db = openDatabase();
			boolean success = true;
			String groupid = count.getdxgroupid();
			if (!isGroup(groupid)) {
				try {
					db.execSQL("delete from " + FIRST_PAGE_COUNT
							+ " where dxclientid='" + count.getdxclientid()
							+ "' and dxgroupid='0'");
				} catch (SQLException e) {
					success = false;
				}
			} else {
				try {
					db.execSQL("delete from " + FIRST_PAGE_COUNT
							+ " where dxgroupid='" + count.getdxgroupid() + "'");
				} catch (SQLException e) {
					success = false;
				}
			}

			closeDatabase();
			return success;
		}
	}

	/**
	 * 清空
	 */
	public void clearTalk() {
		synchronized (obj) {
			SQLiteDatabase db = openDatabase();
			db.execSQL("delete from " + FIRST_PAGE_COUNT);
			closeDatabase();
		}
	}

	/**
	 * 判断表是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		synchronized (obj) {
			SQLiteDatabase db = openDatabase();
			Cursor cursor = db.rawQuery("select * from " + FIRST_PAGE_COUNT,
					null);
			boolean empty = 0 == cursor.getCount();
			cursor.close();
			closeDatabase();
			return empty;
		}
	}

	/**
	 * 获取单个
	 * 
	 * @return
	 */
	public FirPagCount select(String dxclientid, String dxgroupid) {
		synchronized (obj) {
			String conditions;
			if (dxgroupid != null && false == "0".equals(dxgroupid)) {
				conditions = "dxgroupid='" + dxgroupid + "'";
			} else {
				conditions = "dxclientid='" + dxclientid
						+ "' and dxgroupid='0'";
			}

			String sql = "select " + columns + " from " + FIRST_PAGE_COUNT
					+ " where " + conditions;

			SQLiteDatabase db = openDatabase();
			FirPagCount count = null;
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				count = new FirPagCount(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10),
						cursor.getString(11), cursor.getString(12));

			}
			cursor.close();
			closeDatabase();
			return count;
		}
	}

	/**
	 * 获取所有
	 * 
	 * @return
	 */
	public ArrayList<FirPagCount> select() {
		synchronized (obj) {
			String sql = "select " + columns + " from " + FIRST_PAGE_COUNT
					+ " order by time desc";
			SQLiteDatabase db = openDatabase();
			ArrayList<FirPagCount> counts = null;
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0) {
				counts = new ArrayList<FirPagCount>();
				cursor.moveToFirst();
				FirPagCount count;
				for (int i = 0; i < cursor.getCount(); i++) {
					count = new FirPagCount(cursor.getString(0),
							cursor.getString(1), cursor.getString(2),
							cursor.getString(3), cursor.getString(4),
							cursor.getString(5), cursor.getString(6),
							cursor.getString(7), cursor.getString(8),
							cursor.getString(9), cursor.getString(10),
							cursor.getString(11), cursor.getString(12));
					counts.add(count);
					cursor.moveToNext();
				}
			}
			cursor.close();
			closeDatabase();
			return counts;
		}
	}

	/**
	 * 获取所有(模糊搜索)
	 * 
	 * @return
	 */
	public ArrayList<FirPagCount> selectLike(String str) {
		synchronized (obj) {
			String condtions = " where dxclientname like '%" + str
					+ "%' or dxgroupname like '%" + str
					+ "%' order by time desc";
			String sql = "select " + columns + " from " + FIRST_PAGE_COUNT
					+ condtions;

			SQLiteDatabase db = openDatabase();
			ArrayList<FirPagCount> counts = null;
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0) {
				counts = new ArrayList<FirPagCount>();
				cursor.moveToFirst();
				FirPagCount count;
				for (int i = 0; i < cursor.getCount(); i++) {
					count = new FirPagCount(cursor.getString(0),
							cursor.getString(1), cursor.getString(2),
							cursor.getString(3), cursor.getString(4),
							cursor.getString(5), cursor.getString(6),
							cursor.getString(7), cursor.getString(8),
							cursor.getString(9), cursor.getString(10),
							cursor.getString(11), cursor.getString(12));
					counts.add(count);
					cursor.moveToNext();
				}
			}
			cursor.close();
			closeDatabase();
			return counts;
		}
	}

	/**
	 * 获取所有(以拓展字段匹配区分)
	 * 
	 * @return
	 */
	public ArrayList<FirPagCount> select(String str) {
		synchronized (obj) {
			String condtions = " where dxextend like '" + str
					+ "%' order by time desc";
			String sql = "select " + columns + " from " + FIRST_PAGE_COUNT
					+ condtions;

			SQLiteDatabase db = openDatabase();
			ArrayList<FirPagCount> counts = null;
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor != null && cursor.getCount() > 0) {
				counts = new ArrayList<FirPagCount>();
				cursor.moveToFirst();
				FirPagCount count;
				for (int i = 0; i < cursor.getCount(); i++) {
					count = new FirPagCount(cursor.getString(0),
							cursor.getString(1), cursor.getString(2),
							cursor.getString(3), cursor.getString(4),
							cursor.getString(5), cursor.getString(6),
							cursor.getString(7), cursor.getString(8),
							cursor.getString(9), cursor.getString(10),
							cursor.getString(11), cursor.getString(12));
					counts.add(count);
					cursor.moveToNext();
				}
			}
			cursor.close();
			closeDatabase();
			return counts;
		}
	}

	/**
	 * 更新聊天记录中ID为id的用户的昵称
	 * 
	 * @param id
	 *            用户id
	 * @param nickname
	 *            新的用户昵称
	 */
	protected void updateNickname(String id, String nickname) {
		// 更新单聊记录
		String conditions = "dxclientid='" + id + "'";
		String sql = "update " + FIRST_PAGE_COUNT + " set dxclientname='"
				+ nickname + "' where " + conditions;
		SQLiteDatabase db = openDatabase();
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			// nothing
		}
		closeDatabase();
	}
	
	/**
	 * 更新聊天记录中ID为id的用户的头像
	 * @param id 用户id
	 * @param avatar 新的用户头像
	 */
	protected void updateAvatar(String id, String avatar) {
		// 更新单聊记录
		String conditions = "dxclientid='" + id + "'";
		String sql = "update " + FIRST_PAGE_COUNT + " set dxclientavatar='"
				+ avatar + "' where " + conditions;
		SQLiteDatabase db = openDatabase();
		try {
			db.execSQL(sql);
		} catch (SQLException e) {
			// nothing
		}
		closeDatabase();
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
