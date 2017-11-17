package com.hemaapp.hm_FrameWork.result;

/**
 * Created by 赵京伟 on 2017/3/9.
 */

import android.annotation.TargetApi;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import xtom.frame.exception.DataParseException;

public class HemaArrayParse<T> extends HemaArrayResult<T> {
    public HemaArrayParse(JSONObject jsonObject, Class<T> tClass) throws DataParseException {
        super(jsonObject, tClass);
    }

    @Override
    @TargetApi(19)
    public T parse(JSONObject jsonObject, Class<T> classType) throws DataParseException {
        Object classT = null;

        try {
            Constructor e = classType.getConstructor(new Class[]{JSONObject.class});
            e.setAccessible(true);
            classT = e.newInstance(new Object[]{jsonObject});
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException var5) {
            var5.printStackTrace();
        }

        return (T) classT;
    }
}
