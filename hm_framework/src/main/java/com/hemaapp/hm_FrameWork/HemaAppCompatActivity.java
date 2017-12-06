package com.hemaapp.hm_FrameWork;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import com.hemaapp.hm_FrameWork.dialog.HemaProgressDialog;
import com.hemaapp.hm_FrameWork.dialog.HemaTextDialog;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.util.HashMap;
import java.util.List;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomIntent;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.image.load.XtomImageWorker;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomLogger;
import xtom.frame.util.XtomToastUtil;

/**
 * * 基本框架.
 * 特别注意setContentView();应在super.onCreate(savedInstanceState);之前调用否则会导致findView
 * ();等初始化方法失效。 该框架内置了网络访问、图片下载、文件下载功能。
 * <p>
 * 1.网络访问使用方法：HemaNetWorker netWorker = {@link #getNetWorker()};
 * netWorker.login();
 * </p>
 * <p>
 * 2.图片下载使用方法：imageWorker.loadImage(task);
 * </p>
 * <p>
 * 3.集成了log_v(msg)等打印方法以及println(Object)。
 * </p>
 */


public abstract class HemaAppCompatActivity extends AppCompatActivity {

    protected static final String NO_NETWORK = "无网络连接，请检查网络设置。";
    protected static final String FAILED_GETDATA_HTTP = "请求异常。";
    protected static final String FAILED_GETDATA_DATAPARSE = "数据异常。";

    /**
     * 是否已被销毁
     */
    protected boolean isDestroyed = false;
    /**
     * 打印TAG，类名
     */
    private String TAG;
    /**
     * 上下文对象，等同于this
     */
    protected Activity mContext;
    /**
     * 下载图片使用
     */
    public XtomImageWorker imageWorker;
    private HemaNetWorker netWorker;
    /**
     * 获取传参使用
     */
    protected Intent mIntent;
    /**
     * 输入法管理器
     */
    protected InputMethodManager mInputMethodManager;
    /**
     * a LayoutInflater
     */
    private LayoutInflater mLayoutInflater;
    /**
     * 任务参数集
     */
    private HashMap<String, String> params;
    /**
     * 任务文件集
     */
    private HashMap<String, String> files;

    protected HemaAppCompatActivity() {
        TAG = getLogTag();
    }

    private HemaTextDialog textDialog;
    private HemaProgressDialog progressDialog;
    protected boolean isStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XtomActivityManager.add(this);
        mContext = this;
        imageWorker = new XtomImageWorker(mContext);
        mIntent = getIntent();
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        init(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        isStop = true;
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (onKeyBack())
                    return true;
                else
                    return super.onKeyDown(keyCode, event);
            case KeyEvent.KEYCODE_MENU:
                if (onKeyMenu())
                    return true;
                else
                    return super.onKeyDown(keyCode, event);
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 初始化三部曲
    private void init(Bundle savedInstanceState) {
        if (savedInstanceState != null && (savedInstanceState.getSerializable("intent") != null)) {
            mIntent = (XtomIntent) savedInstanceState.getSerializable("intent");
        }
        getExras();
        findView();
        setListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("intent", new XtomIntent(mIntent));
        super.onSaveInstanceState(outState);
    }

    /**
     * 显示或更换Fragment
     *
     * @param fragmentClass   Fragment.class
     * @param containerViewId Fragment显示的空间ID
     * @param replace         是否替换
     */
    public void toogleFragment(Class<? extends Fragment> fragmentClass,
                               int containerViewId, boolean replace) {
        if (isStop)
            return;
        FragmentManager manager = getSupportFragmentManager();
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
                if (fm != null && !fm.equals(fragment))
                    transaction.hide(fm);

        transaction.show(fragment);
        transaction.commit();
    }

    /**
     * 关闭Activity
     *
     * @param enterAnim 进入Activity的动画,若没有传0即可
     * @param exitAnim  退出Activity的动画,若没有传0即可
     */
    public void finish(int enterAnim, int exitAnim) {
        finish();
        overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * @param enterAnim 进入Activity的动画,若没有传0即可
     * @param exitAnim  退出Activity的动画,若没有传0即可
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
     * @param enterAnim 进入Activity的动画,若没有传0即可
     * @param exitAnim  退出Activity的动画,若没有传0即可
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
     * @param text 弹窗提示语
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
     * @param text       弹窗提示语id
     * @param cancelable 是否可以点击弹窗外侧取消
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
     * @param text 弹窗提示语
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
     * @param text       弹窗提示语
     * @param cancelable 是否可以点击弹窗外侧取消
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
     * @param text 弹窗提示语
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
     * @param text 弹窗提示语id
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
        destroy();
        super.onDestroy();
        recyclePics();// 回收图片
    }

    private void destroy() {
        isDestroyed = true;
        XtomActivityManager.remove(this);
        stopNetThread();// 杀掉网络线程
        if (imageWorker != null)
            imageWorker.clearTasks();// 取消图片下载任务
        XtomToastUtil.cancelAllToast();
    }

    @Override
    public void finish() {
        cancelTextDialog();
        if (progressDialog != null)
            progressDialog.cancelImmediately();
        destroy();
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
     * @param failedType 失败原因
     *                   <p>
     *                   See {@link HemaNetWorker#FAILED_DATAPARSE
     *                   HemaNetWorker.FAILED_DATAPARSE},
     *                   {@link HemaNetWorker#FAILED_HTTP HemaNetWorker.FAILED_HTTP},
     *                   {@link HemaNetWorker#FAILED_NONETWORK
     *                   HemaNetWorker.FAILED_HTTP}
     *                   </p>
     */
    protected abstract void callBackForGetDataFailed(HemaNetTask netTask,
                                                     int failedType);

    /**
     * 自动登录失败
     *
     * @param netWorker
     * @param netTask
     * @param failedType 如果failedType为0表示服务器处理失败,其余参照
     *                   {@link HemaNetWorker#FAILED_DATAPARSE
     *                   HemaNetWorker.FAILED_DATAPARSE},
     *                   {@link HemaNetWorker#FAILED_HTTP HemaNetWorker.FAILED_HTTP},
     *                   {@link HemaNetWorker#FAILED_NONETWORK
     *                   HemaNetWorker.FAILED_NONETWORK}
     * @param baseResult 执行结果(仅当failedType为0时有值,其余为null)
     * @return true表示拦截该任务执行流程,
     * 不会继续调用callBackForServerFailed或者callBackForGetDataFailed方法;
     * false反之
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
        public void onExecuteFailed(HemaNetWorker netWorker, HemaNetTask netTask, int failedType) {
            callBackForGetDataFailed(netTask, failedType);
        }

        @Override
        public boolean onAutoLoginFailed(HemaNetWorker netWorker,
                                         HemaNetTask netTask, int failedType, HemaBaseResult baseResult) {
            return HemaAppCompatActivity.this.onAutoLoginFailed(netWorker,
                    netTask, failedType, baseResult);
        }
    }

    protected boolean onKeyBack() {
        finish();
        return true;
    }

    protected boolean onKeyMenu() {
        // TODO Auto-generated method stub
        return false;
    }

    protected abstract void callBeforeDataBack(HemaNetTask netTask);

    protected abstract void callAfterDataBack(HemaNetTask netTask);

    // 友盟相关
    @Override
    protected void onResume() {
        isStop = false;
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // 友盟相关end

    @Override
    protected void onNewIntent(Intent intent) {
        isStop = false;
        super.onNewIntent(intent);
    }


    /**
     * 无网络提示
     *
     * @param task
     */
    protected void noNetWork(HemaNetTask task) {
        noNetWork(task.getId());
    }

    /**
     * 无网络提示
     *
     * @param taskID
     */
    protected void noNetWork(int taskID) {
        noNetWork();
    }

    /**
     * 无网络提示
     */
    protected void noNetWork() {
        XtomToastUtil.showLongToast(mContext, NO_NETWORK);
    }

    /**
     * 初始化三部曲之：查找控件
     */
    protected abstract void findView();

    /**
     * 初始化三部曲之：获取传参
     */
    protected abstract void getExras();

    /**
     * 初始化三部曲之：设置监听
     */
    protected abstract void setListener();


    /**
     * 打印v级别信息
     *
     * @param msg
     */
    protected void log_v(String msg) {
        XtomLogger.v(TAG, msg);
    }

    /**
     * 打印d级别信息
     *
     * @param msg
     */
    protected void log_d(String msg) {
        XtomLogger.d(TAG, msg);
    }

    /**
     * 打印i级别信息
     *
     * @param msg
     */
    protected void log_i(String msg) {
        XtomLogger.i(TAG, msg);
    }

    /**
     * 打印w级别信息
     *
     * @param msg
     */
    protected void log_w(String msg) {
        XtomLogger.w(TAG, msg);
    }

    /**
     * 打印e级别信息
     *
     * @param msg
     */
    protected void log_e(String msg) {
        XtomLogger.e(TAG, msg);
    }

    /**
     * 打印
     *
     * @param msg
     */
    protected void println(Object msg) {
        XtomLogger.println(msg);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true如果该字符串为null或者"",否则false
     */
    protected boolean isNull(String str) {
        return XtomBaseUtil.isNull(str);
    }

    /**
     * 判断网络任务是否都已完成
     *
     * @return
     */
    protected boolean isNetTasksFinished() {
        return netWorker == null || netWorker.isNetTasksFinished();
    }

    /**
     * 获取任务参数集容器
     *
     * @return an empty HashMap
     */
    public HashMap<String, String> getHashParams() {
        if (params == null)
            params = new HashMap<String, String>();
        else
            params.clear();
        return params;
    }

    /**
     * 是否已被销毁
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * 获取任务文件集容器
     *
     * @return an empty HashMap
     */
    public HashMap<String, String> getHashFiles() {
        if (files == null)
            files = new HashMap<String, String>();
        else
            files.clear();
        return files;
    }

    /**
     * get a LayoutInflater
     */
    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater == null ? mLayoutInflater = LayoutInflater
                .from(this) : mLayoutInflater;
    }

    // 回收图片
    private void recyclePics() {
        XtomImageCache.getInstance(this).reMoveCacheInMemByObj(this);
        XtomImageCache.getInstance(this).recyclePics();
    }

    // 杀掉网络线程
    private void stopNetThread() {
        if (netWorker != null) {
            netWorker.cancelTasks();
        }

    }

    // 获取打印TAG，即类名
    private String getLogTag() {
        return getClass().getSimpleName();
    }

    /**
     * 判断当前是否有可用网络
     *
     * @return 如果有true否则false
     */
    public boolean hasNetWork() {
        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = con.getActiveNetworkInfo();// 获取可用的网络服务
        return info != null && info.isAvailable();
    }
}
