package com.hemaapp.wcpc_user.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 *
 */
public class Client extends XtomObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String   avatar	;//	头像
    private String   numbers;//		乘车人数
    private String   realname;//		姓名
    private String  takecount;//		乘车次数
    private String  startaddress;//		出发地
    private String  endaddress;//		目的地
    private String   status;//		状态	1：待上车 3：待送达 5：待支付6：已支付
    private String   sex;//


    public Client(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                sex= get(jsonObject, "sex");
                avatar = get(jsonObject, "avatar");
                numbers = get(jsonObject, "numbers");
                realname = get(jsonObject, "realname");
                takecount = get(jsonObject, "takecount");
                startaddress = get(jsonObject, "startaddress");
                endaddress = get(jsonObject, "endaddress");
                status = get(jsonObject, "status");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public Client(String avatar, String numbers) {
        this.avatar = avatar;
        this.numbers = numbers;
    }

    @Override
    public String toString() {
        return "Client{" +
                "avatar='" + avatar + '\'' +
                ", numbers='" + numbers + '\'' +
                ", realname='" + realname + '\'' +
                ", takecount='" + takecount + '\'' +
                ", startaddress='" + startaddress + '\'' +
                ", endaddress='" + endaddress + '\'' +
                ", status='" + status + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }

    public String getAvatar() {
        return avatar;
    }

    public String getRealname() {
        return realname;
    }

    public String getTakecount() {
        return takecount;
    }

    public String getStartaddress() {
        return startaddress;
    }

    public String getEndaddress() {
        return endaddress;
    }

    public String getStatus() {
        return status;
    }

    public String getSex() {
        return sex;
    }

    public String getNumbers() {
        return numbers;
    }
}

