package com.hemaapp.wcpc_user.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 *
 */
public class ClientAddCoupon extends XtomObject implements Serializable {

    /**
     *
     */
    private String  is_reg;//	是否为新注册记录	1：是，0：否
    private String  password;//	密码
    private String   coupon_type;//		代金券类型	1：抵扣金额，2：免单券
    private String   coupon_count;//		该种代金券张数
    private String   coupon_value;//		金额	coupon_type=1抵扣金额时有效，默认0
    private String   coupon_dateline;//		有效期
    private String   is_active;//		是否激活	1：是，0：否coupon_type=2有效

    private String  keytype	;//代金券类型	1：抵扣金额，2：免单
    private String value;//	面额	keytype=1有效
    private String dateline	;//有效日期
    public ClientAddCoupon(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                coupon_count = get(jsonObject, "coupon_count");
                coupon_value = get(jsonObject, "coupon_value");
                coupon_dateline = get(jsonObject, "coupon_dateline");
                is_reg = get(jsonObject, "is_reg");
                password = get(jsonObject, "password");
                coupon_type = get(jsonObject, "coupon_type");
                is_active = get(jsonObject, "is_active");

                keytype = get(jsonObject, "keytype");
                value = get(jsonObject, "value");
                dateline = get(jsonObject, "dateline");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }


    @Override
    public String toString() {
        return "ClientAddCoupon{" +
                "is_reg='" + is_reg + '\'' +
                ", password='" + password + '\'' +
                ", coupon_type='" + coupon_type + '\'' +
                ", coupon_count='" + coupon_count + '\'' +
                ", coupon_value='" + coupon_value + '\'' +
                ", coupon_dateline='" + coupon_dateline + '\'' +
                ", is_active='" + is_active + '\'' +
                ", keytype='" + keytype + '\'' +
                ", value='" + value + '\'' +
                ", dateline='" + dateline + '\'' +
                '}';
    }

    public String getCoupon_count() {
        return coupon_count;
    }

    public String getCoupon_value() {
        if (isNull(coupon_value))
            return value;
        return coupon_value;
    }

    public String getIs_reg() {
        return is_reg;
    }

    public String getCoupon_type() {
        if (isNull(coupon_type))
            return keytype;
        return coupon_type;
    }

    public String getIs_active() {
        if (isNull(is_active))
            return "0";
        return is_active;
    }

    public String getPassword() {
        return password;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getValue() {
        return value;
    }

    public String getDateline() {
        return dateline;
    }

    public String getCoupon_dateline() {
        if (isNull(coupon_dateline))
            return dateline;
        return coupon_dateline;
    }
}

