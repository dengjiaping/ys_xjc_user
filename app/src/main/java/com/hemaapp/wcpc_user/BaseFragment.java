package com.hemaapp.wcpc_user;

import android.content.Intent;

import com.hemaapp.wcpc_user.activity.LoginActivity;
import com.hemaapp.hm_FrameWork.HemaFragment;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import xtom.frame.XtomActivityManager;
import xtom.frame.net.XtomNetWorker;
import xtom.frame.util.XtomSharedPreferencesUtil;

public abstract class BaseFragment extends HemaFragment {

	@Override
	protected HemaNetWorker initNetWorker() {
		return new BaseNetWorker(getActivity());
	}

	@Override
	public BaseNetWorker getNetWorker() {
		return (BaseNetWorker) super.getNetWorker();
	}
	
	@Override
	public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                     HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
		switch (failedType) {
		case 0:// 服务器处理失败
			int error_code = baseResult.getError_code();
			switch (error_code) {
			case 102:// 密码错误
				XtomActivityManager.finishAll();
				Intent it = new Intent(getActivity(), LoginActivity.class);
				startActivity(it);
				return true;
			default:
				break;
			}
		case XtomNetWorker.FAILED_HTTP:// 网络异常
		case XtomNetWorker.FAILED_DATAPARSE:// 数据异常
		case XtomNetWorker.FAILED_NONETWORK:// 无网络
			break;
		}
		return false;
	}

	// ------------------------下面填充项目自定义方法---------------------------
	
	/**
	 * 保存城市名称
	 * 
	 * @param cityName
	 */
	public void saveCityName(String cityName) {
		XtomSharedPreferencesUtil.save(getActivity(), "city_name", cityName);
	}

	/**
	 * @return 获取城市名称
	 */
	public String getCityName() {
		if (isNull(XtomSharedPreferencesUtil.get(getActivity(), "city_name"))) {
			return "济南市";
		}else {
			return XtomSharedPreferencesUtil.get(getActivity(), "city_name");
		}
	}

	/**
	 * 保存城市id
	 * 
	 * @param cityId
	 */
	public void saveCityId(String cityId) {
		XtomSharedPreferencesUtil.save(getActivity(), "city_id", cityId);
	}

	/**
	 * @return 获取城市id
	 */
	public String getCityId() {
		return XtomSharedPreferencesUtil.get(getActivity(), "city_id");
	}

	/**
	 * 保存地区名称(此地区名称为用户最终获取数据的地区)
	 * 
	 * @param districtName
	 */
	public void saveDistrictName(String districtName) {
		XtomSharedPreferencesUtil.save(getActivity(), "district_name",
				districtName);
	}

	/**
	 * @return 获取地区名称(此地区名称为用户最终获取数据的地区)
	 */
	public String getDistrictName() {
		return XtomSharedPreferencesUtil.get(getActivity(), "district_name");
	}
	
	

}
