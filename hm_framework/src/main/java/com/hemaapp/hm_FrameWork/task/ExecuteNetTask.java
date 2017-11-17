package com.hemaapp.hm_FrameWork.task;

import com.hemaapp.hm_FrameWork.HemaHttpInfomation;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 网络任务，分装无返回值，返回单个对象多个对象及分页
 * create 2016-12-02
 */
public class ExecuteNetTask<T> extends HemaNetTask {

    private Class<T> classType;

    public ExecuteNetTask(HemaHttpInfomation information,
                          HashMap<String, String> params, Class<T> classType) {
        super(information, params);
        this.classType = classType;
    }

    public ExecuteNetTask(HemaHttpInfomation information,
                          HashMap<String, String> params, HashMap<String, String> files, Class<T> classType) {
        super(information, params, files);
        this.classType = classType;
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new HemaArrayParse(jsonObject, classType);
    }

}