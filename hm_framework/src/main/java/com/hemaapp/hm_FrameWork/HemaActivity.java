package com.hemaapp.hm_FrameWork;

import xtom.frame.XtomActivity;
import xtom.frame.net.XtomNetTask;
import xtom.frame.net.XtomNetWorker;
import android.content.Context;
import android.content.Intent;

import com.hemaapp.HemaConfig;
import com.hemaapp.hm_FrameWork.dialog.HemaProgressDialog;
import com.hemaapp.hm_FrameWork.dialog.HemaTextDialog;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.umeng.analytics.MobclickAgent;

/**
 * 本项目中对XtomActivity的一些拓展
 * <p>
 * 1.本项目中发送网络请求不建议使用{@link #getDataFromServer(XtomNetTask)} 建议使用如下
 * 
 * <pre>
 * HemaNetWorker netWorker = {@link #getNetWorker()};
 * netWorker.login();
 * </pre>
 * 
 * </p>
 * 
 */
public abstract class HemaActivity extends XtomActivity {
	private HemaNetWorker netWorker;
	private HemaTextDialog textDialog;
	private HemaProgressDialog progressDialog;

	/**
	 * 关闭Activity
	 * 
	 * @param enterAnim
	 *            进入Activity的动画,若没有传0即可
	 * @param exitAnim
	 *            退出Activity的动画,若没有传0即可
	 */
	public void finish(int enterAnim, int exitAnim) {
		finish();
		overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * 
	 * @param enterAnim
	 *            进入Activity的动画,若没有传0即可
	 * @param exitAnim
	 *            退出Activity的动画,若没有传0即可
	 */
	public void startActivityForResult(Intent intent, int requestCode,
			int enterAnim, int exitAnim) {
		startActivityForResult(intent, requestCode);
		if (getParent() != null)
			getParent().overridePendingTransition(enterAnim, exitAnim);
		else
			overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * 
	 * @param enterAnim
	 *            进入Activity的动画,若没有传0即可
	 * @param exitAnim
	 *            退出Activity的动画,若没有传0即可
	 */
	public void startActivity(Intent intent, int enterAnim, int exitAnim) {
		startActivity(intent);
		if (getParent() != null)
			getParent().overridePendingTransition(enterAnim, exitAnim);
		else
			overridePendingTransition(enterAnim, exitAnim);
	}

	/**
	 * 显示交互弹窗(默认不可以点击弹窗外侧取消)
	 * 
	 * @param text
	 *            弹窗提示语
	 */
	public void showProgressDialog(String text) {
		if (progressDialog == null)
			progressDialog = new HemaProgressDialog(this);
		progressDialog.setText(text);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	/**
	 * 显示交互弹窗
	 * 
	 * @param text
	 *            弹窗提示语id
	 * @param cancelable
	 *            是否可以点击弹窗外侧取消
	 */
	public void showProgressDialog(String text, boolean cancelable) {
		if (progressDialog == null)
			progressDialog = new HemaProgressDialog(this);
		progressDialog.setText(text);
		progressDialog.setCancelable(cancelable);
		progressDialog.show();
	}

	/**
	 * 显示交互弹窗(默认不可以点击弹窗外侧取消)
	 * 
	 * @param text
	 *            弹窗提示语
	 */
	public void showProgressDialog(int text) {
		if (progressDialog == null)
			progressDialog = new HemaProgressDialog(this);
		progressDialog.setText(text);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	/**
	 * 显示交互弹窗
	 * 
	 * @param text
	 *            弹窗提示语
	 * @param cancelable
	 *            是否可以点击弹窗外侧取消
	 */
	public void showProgressDialog(int text, boolean cancelable) {
		if (progressDialog == null)
			progressDialog = new HemaProgressDialog(this);
		progressDialog.setText(text);
		progressDialog.setCancelable(cancelable);
		progressDialog.show();
	}

	/**
	 * 取消交互弹窗(同时setCancelable(false))
	 */
	public void cancelProgressDialog() {
		if (progressDialog != null) {
			progressDialog.setCancelable(false);
			progressDialog.cancel();
		}
	}

	/**
	 * 显示提示弹窗
	 * 
	 * @param text
	 *            弹窗提示语
	 */
	public void showTextDialog(String text) {
		if (textDialog == null)
			textDialog = new HemaTextDialog(this);
		textDialog.setText(text);
		textDialog.show();
	}

	/**
	 * 显示提示弹窗
	 * 
	 * @param text
	 *            弹窗提示语id
	 */
	public void showTextDialog(int text) {
		if (textDialog == null)
			textDialog = new HemaTextDialog(this);
		textDialog.setText(text);
		textDialog.show();
	}

	/**
	 * 取消提示弹窗
	 */
	public void cancelTextDialog() {
		if (textDialog != null)
			textDialog.cancel();
	}

	@Override
	protected void onDestroy() {
		if (netWorker != null) {
			netWorker.cancelTasks();
			netWorker.setOnTaskExecuteListener(null);
		}
		super.onDestroy();
	}

	@Override
	public void finish() {
		cancelTextDialog();
		if (progressDialog != null)
			progressDialog.cancelImmediately();
		super.finish();
	}

	/**
	 * 获取网络请求工具类
	 */
	public HemaNetWorker getNetWorker() {
		if (netWorker == null) {
			netWorker = initNetWorker();
			netWorker
					.setOnTaskExecuteListener(new NetTaskExecuteListener(this));
		}
		return netWorker;
	}

	/**
	 * 初始化NetWorker
	 */
	protected abstract HemaNetWorker initNetWorker();

	/**
	 * 返回数据前的操作，如显示进度条
	 * 
	 * @param netTask
	 */
	protected abstract void callBeforeDataBack(HemaNetTask netTask);

	/**
	 * 返回数据后的操作，如关闭进度条
	 * 
	 * @param netTask
	 */
	protected abstract void callAfterDataBack(HemaNetTask netTask);

	/**
	 * 服务器处理成功
	 * 
	 * @param netTask
	 * @param baseResult
	 */
	protected abstract void callBackForServerSuccess(HemaNetTask netTask,
			HemaBaseResult baseResult);

	/**
	 * 服务器处理失败
	 * 
	 * @param netTask
	 * @param baseResult
	 */
	protected abstract void callBackForServerFailed(HemaNetTask netTask,
			HemaBaseResult baseResult);

	/**
	 * 获取数据失败
	 * 
	 * @param netTask
	 * @param failedType
	 *            失败原因
	 *            <p>
	 *            See {@link XtomNetWorker#FAILED_DATAPARSE
	 *            XtomNetWorker.FAILED_DATAPARSE},
	 *            {@link XtomNetWorker#FAILED_HTTP XtomNetWorker.FAILED_HTTP},
	 *            {@link XtomNetWorker#FAILED_NONETWORK
	 *            XtomNetWorker.FAILED_NONETWORK}
	 *            </p>
	 */
	protected abstract void callBackForGetDataFailed(HemaNetTask netTask,
			int failedType);

	/**
	 * 自动登录失败
	 * 
	 * @param netWorker
	 * @param netTask
	 * @param failedType
	 *            如果failedType为0表示服务器处理失败,其余参照
	 *            {@link XtomNetWorker#FAILED_DATAPARSE
	 *            XtomNetWorker.FAILED_DATAPARSE},
	 *            {@link XtomNetWorker#FAILED_HTTP XtomNetWorker.FAILED_HTTP},
	 *            {@link XtomNetWorker#FAILED_NONETWORK
	 *            XtomNetWorker.FAILED_NONETWORK}
	 * @param baseResult
	 *            执行结果(仅当failedType为0时有值,其余为null)
	 * @return true表示拦截该任务执行流程,
	 *         不会继续调用callBackForServerFailed或者callBackForGetDataFailed方法;
	 *         false反之
	 */
	public abstract boolean onAutoLoginFailed(HemaNetWorker netWorker,
			HemaNetTask netTask, int failedType, HemaBaseResult baseResult);

	private class NetTaskExecuteListener extends HemaNetTaskExecuteListener {

		public NetTaskExecuteListener(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
			callBeforeDataBack(netTask);
		}

		@Override
		public void onPostExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
			callAfterDataBack(netTask);
		}

		@Override
		public void onServerSuccess(HemaNetWorker netWorker,
				HemaNetTask netTask, HemaBaseResult baseResult) {
			callBackForServerSuccess(netTask, baseResult);
		}

		@Override
		public void onServerFailed(HemaNetWorker netWorker,
				HemaNetTask netTask, HemaBaseResult baseResult) {
			callBackForServerFailed(netTask, baseResult);
		}

		@Override
		public void onExecuteFailed(HemaNetWorker netWorker,
				HemaNetTask netTask, int failedType) {
			callBackForGetDataFailed(netTask, failedType);
		}

		@Override
		public boolean onAutoLoginFailed(HemaNetWorker netWorker,
				HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
			return HemaActivity.this.onAutoLoginFailed(netWorker, netTask,
					failedType, baseResult);
		}
	}

	@Override
	protected boolean onKeyBack() {
		finish();
		return true;
	}

	@Override
	protected boolean onKeyMenu() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void callBeforeDataBack(XtomNetTask netTask) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void callAfterDataBack(XtomNetTask netTask) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void callBackForGetDataSuccess(XtomNetTask netTask, Object result) {
		// TODO Auto-generated method stub
	}

	@Override
	@Deprecated
	public void getDataFromServer(XtomNetTask netTask) {
		log_e("本项目中不建议使用此方法");
	}

	// 友盟相关
	@Override
	protected void onResume() {
		super.onResume();
		if (HemaConfig.UMENG_ENABLE)
			MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (HemaConfig.UMENG_ENABLE)
			MobclickAgent.onPause(this);
	}
	// 友盟相关end
}
