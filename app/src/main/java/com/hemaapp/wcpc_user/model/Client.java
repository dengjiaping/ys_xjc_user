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

    private String avatar;//	头像
    private String numbers;//		乘车人数
    private String realname;//		姓名
    private String takecount;//		乘车次数
    private String startaddress;//		出发地
    private String endaddress;//		目的地
    private String status;//		状态	1：待上车 3：待送达 5：待支付6：已支付
    private String sex;//
    private String charindex;// 首拼字母
    private boolean showCharIndex;
    private String id;//用户id
    private String username;//	登录名	手机号
    private String is_reg;//	是否注册	1：是，0：否
    private String name;//	姓名	手机号
    private String mobile;//	手机号
    private boolean isSelect=false;
    private boolean isEidit=false;
    public Client(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                is_reg = get(jsonObject, "is_reg");
                name = get(jsonObject, "name");
                mobile = get(jsonObject, "mobile");
                id = get(jsonObject, "id");
                username = get(jsonObject, "username");
                charindex = get(jsonObject, "charindex");
                sex = get(jsonObject, "sex");
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
                ", charindex='" + charindex + '\'' +
                ", showCharIndex=" + showCharIndex +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", is_reg='" + is_reg + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
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

    public String getCharindex() {
        return charindex;
    }

    public void setCharindex(String charindex) {
        this.charindex = charindex;
    }

    public boolean isShowCharIndex() {
        return showCharIndex;
    }

    public void setShowCharIndex(boolean showCharIndex) {
        this.showCharIndex = showCharIndex;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getIs_reg() {
        return is_reg;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isEidit() {
        return isEidit;
    }

    public void setEidit(boolean eidit) {
        isEidit = eidit;
    }

    public String getNumbers() {
        return numbers;
    }
}

