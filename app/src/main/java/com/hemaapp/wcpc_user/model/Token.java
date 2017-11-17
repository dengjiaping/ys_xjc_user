package com.hemaapp.wcpc_user.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 
 */
public class Token extends XtomObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String token;//	主键id
	private String temp_token;
	public Token(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				token = get(jsonObject, "token");
				temp_token = get(jsonObject, "temp_token");

				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "Token{" +
				"token='" + token + '\'' +
				", temp_token='" + temp_token + '\'' +
				'}';
	}

	public String getToken() {
		return token;
	}

	public String getTemp_token() {
		return temp_token;
	}
}
