package com.hemaapp.wcpc_user.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 *
 */
public class Recomm extends XtomObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;//	主键id
    private String city_id;//	城市主键id
    private String address;//地点名称
    private String lng;//经度
    private String lat;//纬度

    public Recomm(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                lat = get(jsonObject, "lat");
                lng = get(jsonObject, "lng");
                city_id = get(jsonObject, "city_id");
                address = get(jsonObject, "address");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Recomm{" +
                "id='" + id + '\'' +
                ", city_id='" + city_id + '\'' +
                ", address='" + address + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getAddress() {
        return address;
    }

    public String getLng() {
        return lng;
    }

    public String getLat() {
        return lat;
    }
}
