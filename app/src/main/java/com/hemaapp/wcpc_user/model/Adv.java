package com.hemaapp.wcpc_user.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 *
 */
public class Adv extends XtomObject implements Serializable {

    /**
     *
     */
    private String id	;//主键id;//
    private String alertimgurl;//	弹框图片地址
    private String imgurl	;//详情图片地址

    public Adv(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                alertimgurl = get(jsonObject, "alertimgurl");
                imgurl = get(jsonObject, "imgurl");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }


    @Override
    public String toString() {
        return "Adv{" +
                "id='" + id + '\'' +
                ", alertimgurl='" + alertimgurl + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getAlertimgurl() {
        return alertimgurl;
    }

    public String getImgurl() {
        return imgurl;
    }
}

