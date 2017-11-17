package com.hemaapp.hm_FrameWork;

import java.util.List;

import xtom.frame.XtomFragment;
import xtom.frame.net.XtomNetTask;
import xtom.frame.net.XtomNetWorker;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hemaapp.HemaConfig;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.umeng.analytics.MobclickAgent;

/**
 * 本项目中对XtomFragment的一些拓展
 * <p>
 * 1.本项目中发送网络请求不建议使用{@link #getDataFromServer(XtomNetTask)} 建议使用如下
 * 
 * <pre>
 * HemaNetWorker netWorker = {@link #getNetWorker()};
 * netWorker.login();
 * </pre>
 * 
 */
public abstract class HemaFragment extends XtomFragment {
	private HemaNetWorker netWorker;

	public View findViewById(int id) {
		View view = null;
		if (rootView != null)
			view = rootView.findViewById(id);
		return view;
	}

	/**
	 * 显示或更换Fragment
	 * 
	 * @param fragmentClass
	 *            Fragment.class
	 * @param containerViewId
	 *            Fragment显示的空间ID
	 * @param replace
	 *            是否替换
	 */
	public void toogleFragment(Class<? extends Fragment> fragmentClass,
			int containerViewId, boolean replace) {
		FragmentManager manager = getChildFragmentManager();
		String tag = fragmentClass.getName();
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment fragment = manager.findFragmentByTag(tag);

		if (fragment == null) {
			try {
				fragment = fragmentClass.newInstance();
				if (replace)
					transaction.replace(containerViewId, fragment, tag);
				else
					// 替换时保留Fragment,以便复用
					transaction.add(containerViewId, fragment, tag);
			} catch (Exception e) {
				// ignore
			}
		} else {
			// nothing
		}
		// 遍历存在的Fragment,隐藏其他Fragment
		List<Fragment> fragments = manager.getFragments();
		if (fragments != null)
			for (Fragment fm : fragments)
				if (!fm.equals(fragment))
					transaction.hide(fm);

		transaction.show(fragment);
		transaction.commit();
	}

	public void showProgressDialog(String text) {
		HemaAppCompatActivity activity = (HemaAppCompatActivity) getActivity();
		activity.showProgressDialog(text);
	}

	public void showProgressDialog(int text) {
		HemaAppCompatActivity activity = (HemaAppCompatActivity) getActivity();
		activity.showProgressDialog(text);
	}

	public void cancelProgressDialog() {
		HemaAppCompatActivity activity = (HemaAppCompatActivity) getActivity();
		activity.cancelProgressDialog();
	}

	public void showTextDialog(String text) {
		HemaAppCompatActivity activity = (HemaAppCompatActivity) getActivity();
		activity.showTextDialog(text);
	}

	public void showTextDialog(int text) {
		HemaAppCompatActivity activity = (HemaAppCompatActivity) getActivity();
		activity.showTextDialog(text);
	}

	public void cancelTextDialog() {
		HemaAppCompatActivity activity = (HemaAppCompatActivity) getActivity();
		activity.cancelTextDialog();
	}

	@Override
	public void onDestroy() {
		if (netWorker != null)
			netWorker.setOnTaskExecuteListener(null);
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return rootView;
	}

	/**
	 * 获取网络请求工具类
	 */
	public HemaNetWorker getNetWorker() {
		if (netWorker == null) {
			netWorker = initNetWorker();
			netWorker.setOnTaskExecuteListener(new NetTaskExecuteListener(
					getActivity()));
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
	 *            XtomNetWorker.FAILED_HTTP}
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
			return HemaFragment.this.onAutoLoginFailed(netWorker, netTask,
					failedType, baseResult);
		}
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
	public void onResume() {
		super.onResume();
		if (HemaConfig.UMENG_ENABLE)
			MobclickAgent.onPageStart(getClass().getSimpleName());
	}

	@Override
	public void onPause() {
		super.onPause();
		if (HemaConfig.UMENG_ENABLE)
			MobclickAgent.onPageEnd(getClass().getSimpleName());
	}
	// 友盟相关end
}
