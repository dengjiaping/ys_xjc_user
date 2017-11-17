package com.hemaapp.hm_FrameWork.result;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xtom.frame.exception.DataParseException;

/**
 * 对BaseResult的拓展，适用返回数据中有数组(并且数组分页)的情况
 */
public abstract class HemaPageArrayResult<T> extends HemaBaseResult {
	private ArrayList<T> objects = new ArrayList<T>();
	private int totalCount;

	public HemaPageArrayResult(JSONObject jsonObject) throws DataParseException {
		super(jsonObject);
		if (jsonObject != null) {
			try {
				if (!jsonObject.isNull("infor")
						&& !isNull(jsonObject.getString("infor"))) {
					JSONObject object = jsonObject.getJSONObject("infor");
					if (!object.isNull("totalCount")) {
						totalCount = object.getInt("totalCount");
					}
					if (!object.isNull("listItems")
							&& !isNull(object.getString("listItems"))) {
						JSONArray jsonList = object.getJSONArray("listItems");
						int size = jsonList.length();
						for (int i = 0; i < size; i++) {
							objects.add(parse(jsonList.getJSONObject(i)));
						}
					}
				}
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	/**
	 * @return the totalCount 表示所有符合查询条件的总记录的个数（totalCount=0 表示暂无数据）
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 获取服务器返回的实例集合
	 * 
	 * @return 服务器返回的实例集合
	 */
	public ArrayList<T> getObjects() {
		return objects;
	}

	/**
	 * 该方法将JSONObject解析为具体的数据实例
	 */
	public abstract T parse(JSONObject jsonObject) throws DataParseException;
}
