package com.hemaapp.hm_FrameWork;

import java.util.HashMap;

import xtom.frame.net.XtomNetTask;

/**
 * 网络请求任务
 */
public abstract class HemaLoginNetTask extends XtomNetTask {
	protected HemaHttpInfomation requestInformation;

	/**
	 * 实例化网络请求任务
	 * 
	 * @param information
	 *            网络请求信息
	 * @param params
	 *            任务参数集(参数名,参数值)
	 */
	public HemaLoginNetTask(HemaHttpInfomation information,
			HashMap<String, String> params) {
		this(information, params, null);
	}

	/**
	 * 实例化网络请求任务
	 * 
	 * @param information
	 *            网络请求信息
	 * @param params
	 *            任务参数集(参数名,参数值)
	 * @param files
	 *            任务文件集(参数名,文件的本地路径)
	 */
	public HemaLoginNetTask(HemaHttpInfomation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information.getId(), information.getUrlPath(), params, files,
				information.getDescription());
		this.requestInformation = information;
	}

}
