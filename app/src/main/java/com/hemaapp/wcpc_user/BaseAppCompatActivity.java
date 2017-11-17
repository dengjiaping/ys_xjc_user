package com.hemaapp.wcpc_user;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.hemaapp.wcpc_user.activity.LoginActivity;
import com.hemaapp.wcpc_user.model.User;
import com.hemaapp.hm_FrameWork.HemaAppCompatActivity;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.util.ArrayList;
import java.util.List;

import xtom.frame.XtomActivityManager;
import xtom.frame.net.XtomNetWorker;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 */
public abstract class BaseAppCompatActivity extends HemaAppCompatActivity {
	public	List<View> noHideInput=new ArrayList<>();//点击这些view不会收回键盘
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		noHideInput.clear();
		// setStatusBar();
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		BaseUtil.hideInputWhenTouchOtherViewBase(this, ev, noHideInput);//点击屏幕收回输入键盘（除去noHideInput和Eidittext）
		return super.dispatchTouchEvent(ev);
	}
	@Override
	protected HemaNetWorker initNetWorker() {
		return new BaseNetWorker(mContext);
	}

	@Override
	public BaseNetWorker getNetWorker() {
		return (BaseNetWorker) super.getNetWorker();
	}

	@Override
	public BaseApplication getApplicationContext() {
		return (BaseApplication) super.getApplicationContext();
	}

	@Override
	protected void callBackForServerFailed(HemaNetTask netTask,
			HemaBaseResult baseResult) {
		if(baseResult.getError_code() == 404){
			showTextDialog("您即将访问的页面不存在");
			finish();
		}
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
				Intent it = new Intent(mContext, LoginActivity.class);
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
		XtomSharedPreferencesUtil.save(this, "city_name", cityName);
	}

	/**
	 * @return 获取城市名称
	 */
	public String getCityName() {
		if (isNull(XtomSharedPreferencesUtil.get(this, "city_name"))) {
			return "济南市";
		}else {
			return XtomSharedPreferencesUtil.get(this, "city_name");
		}

	}

	/**
	 * 保存城市id
	 * 
	 * @param cityId
	 */
	public void saveCityId(String cityId) {
		XtomSharedPreferencesUtil.save(this, "city_id", cityId);
	}

	/**
	 * @return 获取城市id
	 */
	public String getCityId() {
		return XtomSharedPreferencesUtil.get(this, "city_id");
	}

	/**
	 * 保存地区名称(此地区名称为用户最终获取数据的地区)
	 * 
	 * @param districtName
	 */
	public void saveDistrictName(String districtName) {
		XtomSharedPreferencesUtil.save(this, "district_name", districtName);
	}

	/**
	 * @return 获取地区名称(此地区名称为用户最终获取数据的地区)
	 */
	public String getDistrictName() {
		return XtomSharedPreferencesUtil.get(this, "district_name");
	}

	/**
	 * 获取用户
	 * */
	public User getUser(){
		User user = BaseApplication.getInstance().getUser();
		if(user == null)
			return null;
		else {
			return user;
		}
	}
}
