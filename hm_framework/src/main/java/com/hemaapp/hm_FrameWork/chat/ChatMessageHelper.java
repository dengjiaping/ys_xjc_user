package com.hemaapp.hm_FrameWork.chat;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 该类主要负责从数据库取聊天记录
 */
public class ChatMessageHelper extends XtomObject {
	private static final int CHAT_TYPE_SINGLE = 0;// 私聊
	private static final int CHAT_TYPE_GROUP = 1;// 群聊

	private int chatType;
	private Params params;
	private ChatDBClient chatDBClient;
	private FirPagDBClient firDBClient;

	private ChatMessageHelper(Context context, int chatType) {
		this.chatType = chatType;
		this.params = new Params();
		this.chatDBClient = ChatDBClient.get(context);
		this.firDBClient = FirPagDBClient.get(context);
	}

	private ChatMessageHelper(Context context, int chatType, String oneJid,
			String anotherJid) {
		this(context, chatType);
		params.oneJid = oneJid;
		params.anotherJid = anotherJid;
	}

	private ChatMessageHelper(Context context, int chatType, String oneJid,
			String anotherJid, String packdetail_id) {
		this(context, chatType, oneJid, anotherJid);
	}

	private ChatMessageHelper(Context context, int chatType, String group_id) {
		this(context, chatType);
		params.group_id = group_id;
	}

	/**
	 * 私聊
	 * 
	 */
	public static ChatMessageHelper getSINInstance(Context context,
			String oneJid, String anotherJid) {
		return new ChatMessageHelper(context, CHAT_TYPE_SINGLE, oneJid,
				anotherJid);
	}

	/**
	 * 群聊
	 */
	public static ChatMessageHelper getGroupInstance(Context context,
			String group_id) {
		return new ChatMessageHelper(context, CHAT_TYPE_GROUP, group_id);
	}

	/**
	 * 获取下一页记录
	 */
	public void getNextPage() {
		new HelperTask(HelperTask.TASK_TYPE_NEXT).execute();
	}

	/**
	 * 获取新收到的消息
	 */
	public void getNewMessages() {
		new HelperTask(HelperTask.TASK_TYPE_NEW).execute();
	}

	/**
	 * 清空未读消息
	 */
	public void clearcount() {
		new HelperTask(HelperTask.TASK_TYPE_CLEAR_COUNT).execute();
	}

	/**
	 * 更新某条记录的状态
	 * 
	 * @param message
	 */
	public void updateMessage(ChatMessage message) {
		new HelperTask(HelperTask.TASK_TYPE_UPDATE).execute(message);
	}

	private class HelperTask extends AsyncTask<Object, Object, Object> {
		private static final int TASK_TYPE_NEXT = 0;
		private static final int TASK_TYPE_NEW = 1;
		private static final int TASK_TYPE_UPDATE = 2;
		private static final int TASK_TYPE_CLEAR_COUNT = 3;
		private int taskType;

		public HelperTask(int taskType) {
			this.taskType = taskType;
		}

		@Override
		protected Object doInBackground(Object... params) {
			Object result = null;
			synchronized (ChatMessageHelper.this) {
				switch (chatType) {
				case CHAT_TYPE_SINGLE:
					result = doInBgSingle(params);
					break;

				case CHAT_TYPE_GROUP:
					result = doInBgGroup(params);
					break;
				}
				return result;
			}
		}

		private ArrayList<ChatMessage> checkId(ArrayList<ChatMessage> messages) {
			if (messages != null)
				for (ChatMessage cm : messages) {
					int id = cm.getId();
					if (id > params.maxid)
						params.maxid = id;
					if (params.minid == 0 || id < params.minid)
						params.minid = id;
				}
			return messages;
		}

		private Object doInBgSingle(Object... param) {
			Object result = null;
			switch (taskType) {
			case TASK_TYPE_NEXT:
				result = checkId(chatDBClient.selectPaging(params.oneJid,
						params.anotherJid, params.minid));
				break;
			case TASK_TYPE_NEW:
				result = checkId(chatDBClient.selectNew(params.oneJid,
						params.anotherJid, params.maxid));
				break;
			case TASK_TYPE_UPDATE:
				result = param[0];
				chatDBClient.insertOrUpdate((ChatMessage) result);
				break;
			case TASK_TYPE_CLEAR_COUNT:
				firDBClient.updateCount0(params.anotherJid, "0");
				break;
			}
			return result;
		}

		private Object doInBgGroup(Object... param) {
			Object result = null;
			switch (taskType) {
			case TASK_TYPE_NEXT:
				result = checkId(chatDBClient.selectGroupPaging(
						params.group_id, params.minid));
				break;
			case TASK_TYPE_NEW:
				result = checkId(chatDBClient.selectGroupNew(params.group_id,
						params.maxid));
				break;
			case TASK_TYPE_UPDATE:
				result = param[0];
				chatDBClient.insertOrUpdate((ChatMessage) result);
				break;
			case TASK_TYPE_CLEAR_COUNT:
				firDBClient.updateCount0(params.anotherJid, params.group_id);
				break;
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			synchronized (ChatMessageHelper.this) {
				if (helperListener != null) {
					switch (taskType) {
					case TASK_TYPE_NEXT:
						helperListener.onNextPage(result == null ? null
								: (ArrayList<ChatMessage>) result);
						break;
					case TASK_TYPE_NEW:
						helperListener.onNewMessages(result == null ? null
								: (ArrayList<ChatMessage>) result);
						break;
					case TASK_TYPE_UPDATE:
						helperListener.onUpdate((ChatMessage) result);
					}
				}
			}
		}
	}

	private class Params {
		String oneJid;
		String anotherJid;
		String group_id;

		int maxid = 0;
		int minid = 0;
	}

	private HelperListener helperListener;

	public HelperListener getHelperListener() {
		return helperListener;
	}

	public void setHelperListener(HelperListener helperListener) {
		this.helperListener = helperListener;
	}

	public interface HelperListener {
		/**
		 * 
		 * @param messages
		 *            所取得的消息记录
		 */
		public void onNextPage(ArrayList<ChatMessage> messages);

		/**
		 * 
		 * @param messages
		 *            所取得的消息记录
		 */
		public void onNewMessages(ArrayList<ChatMessage> messages);

		/**
		 * 
		 * @param message
		 * 
		 */
		public void onUpdate(ChatMessage message);
	}
}
