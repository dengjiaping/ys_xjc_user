package com.hemaapp.wcpc_user.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 
 */
public class ID extends XtomObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;//	主键id
	private String comment_id;
	private String custom_id;
	private String count;
	private String about_id;
	private String phone;
	private String advice_id;
	private String keytype;
	private String complain_id;
	private String fee;
	private String driver_mobile;
	public ID(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				driver_mobile = get(jsonObject, "driver_mobile");
				fee = get(jsonObject, "fee");
				id = get(jsonObject, "id");
				custom_id = get(jsonObject, "custom_id");
				comment_id = get(jsonObject, "comment_id");
				about_id = get(jsonObject, "about_id");
				count = get(jsonObject, "count");
				phone = get(jsonObject, "phone");
				advice_id = get(jsonObject, "advice_id");
				keytype = get(jsonObject, "keytype");
				complain_id = get(jsonObject, "complain_id");
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "ID{" +
				"id='" + id + '\'' +
				", comment_id='" + comment_id + '\'' +
				", custom_id='" + custom_id + '\'' +
				", count='" + count + '\'' +
				", about_id='" + about_id + '\'' +
				", phone='" + phone + '\'' +
				", advice_id='" + advice_id + '\'' +
				", keytype='" + keytype + '\'' +
				", complain_id='" + complain_id + '\'' +
				", fee='" + fee + '\'' +
				", driver_mobile='" + driver_mobile + '\'' +
				'}';
	}

	public String getId() {
		return id;
	}

	public String getComment_id() {
		return comment_id;
	}

	public String getPhone() {
		return phone;
	}

	public String getAbout_id() {
		return about_id;
	}

	public String getCustom_id() {
		return custom_id;
	}

	public String getCount() {
		return count;
	}

	public String getAdvice_id() {
		return advice_id;
	}

	public String getComplain_id() {
		return complain_id;
	}

	public String getKeytype() {
		return keytype;
	}

	public String getFee() {
		return fee;
	}

	public String getDriver_mobile() {
		return driver_mobile;
	}

	public void setCount(String count) {
		this.count = count;
	}
}
