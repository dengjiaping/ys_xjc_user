package com.hemaapp.wcpc_user.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * 
 */
public class Often extends XtomObject implements Serializable {

	private String 	id;
	private String startaddress;//	出发点
	private String startcity_id	;//出发城市id
	private String startcity;//	出发城市名称
	private String endaddress;//	目的地
	private String endcity_id;//	目的地城市id
	private String endcity	;//目的地城市名称
	private String lng_start;//	出发点经度
	private String lat_start;//	出发点纬度
	private String lng_end;//	目的地经度
	private String lat_end;//	目的地经度
	public Often(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				id = get(jsonObject, "id");
				startaddress = get(jsonObject, "startaddress");
				startcity_id = get(jsonObject, "startcity_id");
				startcity = get(jsonObject, "startcity");
				endaddress = get(jsonObject, "endaddress");
				endcity_id = get(jsonObject, "endcity_id");
				endcity = get(jsonObject, "endcity");
				lng_start = get(jsonObject, "lng_start");
				lat_start = get(jsonObject, "lat_start");
				lng_end = get(jsonObject, "lng_end");
				lat_end = get(jsonObject, "lat_end");
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		return "Often{" +
				"id='" + id + '\'' +
				", startaddress='" + startaddress + '\'' +
				", startcity_id='" + startcity_id + '\'' +
				", startcity='" + startcity + '\'' +
				", endaddress='" + endaddress + '\'' +
				", endcity_id='" + endcity_id + '\'' +
				", endcity='" + endcity + '\'' +
				", lng_start='" + lng_start + '\'' +
				", lat_start='" + lat_start + '\'' +
				", lng_end='" + lng_end + '\'' +
				", lat_end='" + lat_end + '\'' +
				'}';
	}

	public String getId() {
		return id;
	}

	public String getStartaddress() {
		return startaddress;
	}

	public String getStartcity_id() {
		return startcity_id;
	}

	public String getStartcity() {
		return startcity;
	}

	public String getEndaddress() {
		return endaddress;
	}

	public String getEndcity_id() {
		return endcity_id;
	}

	public String getEndcity() {
		return endcity;
	}

	public String getLng_start() {
		return lng_start;
	}

	public String getLat_start() {
		return lat_start;
	}

	public String getLng_end() {
		return lng_end;
	}

	public String getLat_end() {
		return lat_end;
	}
}
