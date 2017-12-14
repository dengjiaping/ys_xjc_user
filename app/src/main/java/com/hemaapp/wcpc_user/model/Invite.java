package com.hemaapp.wcpc_user.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 *
 */
public class Invite extends XtomObject implements Serializable {

    /**
     *
     */
    private String  keytype	;//记录类型	7：邀请注册 8：邀请的用户完成首单
    private String  amount;//	金额
    private String  regdate	;//时间
    private String  username;//	用户手机
    private String  realname;//	用户姓名
    public Invite(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                keytype = get(jsonObject, "keytype");
                amount = get(jsonObject, "amount");
                regdate = get(jsonObject, "regdate");
                username = get(jsonObject, "username");
                realname = get(jsonObject, "realname");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }


    @Override
    public String toString() {
        return "Invite{" +
                "keytype='" + keytype + '\'' +
                ", amount='" + amount + '\'' +
                ", regdate='" + regdate + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                '}';
    }

    public String getKeytype() {
        return keytype;
    }

    public String getAmount() {
        return amount;
    }

    public String getRegdate() {
        return regdate;
    }

    public String getUsername() {
        return username;
    }

    public String getRealname() {
        return realname;
    }
}

