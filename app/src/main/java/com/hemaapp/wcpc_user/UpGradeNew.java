/*
 * Copyright (C) 2014 The Android Client Of Demo Project
 * 
 *     The BeiJing PingChuanJiaHeng Technology Co., Ltd.
 * 
 * Author:Yang ZiTian
 * You Can Contact QQ:646172820 Or Email:mail_yzt@163.com
 */
package com.hemaapp.wcpc_user;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wcpc_user.model.SysInitInfo;

import java.io.File;

import xtom.frame.XtomObject;
import xtom.frame.fileload.FileInfo;
import xtom.frame.fileload.XtomFileDownLoader;
import xtom.frame.fileload.XtomFileDownLoader.XtomDownLoadListener;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomToastUtil;

/**
 * 软件升级
 */
public abstract class UpGradeNew extends XtomObject {
    private long checkTime = 0;
    private Context mContext;
    private String savePath;
    private BaseNetWorker netWorker;
    private SysInitInfo sysInitInfo;

    public UpGradeNew(Context mContext) {
        this.mContext = mContext;
        this.netWorker = new BaseNetWorker(mContext);
        this.netWorker.setOnTaskExecuteListener(new TaskExecuteListener(
                mContext));
    }

    /**
     * 检查升级
     */
    public void check() {
        long currentTime = System.currentTimeMillis();
        boolean isCanCheck = checkTime == 0
                || currentTime - checkTime > 1000 * 60 * 60 * 24;
        if (isCanCheck) {
            netWorker.init();
        }
    }

    // 是否强制升级
    private boolean isMust() {
        SysInitInfo sysInfo = BaseApplication.getInstance().getSysInitInfo();
        boolean must = "1".equals(sysInfo.getAndroid_must_update());
        return must;
    }

    private class TaskExecuteListener extends BaseNetTaskExecuteListener {

        /**
         * @param context
         */
        public TaskExecuteListener(Context context) {
            super(context);
        }

        @Override
        public void onPreExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
        }

        @Override
        public void onPostExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onServerSuccess(HemaNetWorker netWorker,
                                    HemaNetTask netTask, HemaBaseResult baseResult) {
            checkTime = System.currentTimeMillis();
            HemaArrayResult<SysInitInfo> sResult = (HemaArrayResult<SysInitInfo>) baseResult;
            sysInitInfo = sResult.getObjects().get(0);
            String sysVersion = sysInitInfo.getAndroid_last_version();
            String version = HemaUtil.getAppVersionForSever(mContext);
            if (HemaUtil.isNeedUpDate(version, sysVersion)) {
                alert();
            }
        }

        @Override
        public void onServerFailed(HemaNetWorker netWorker,
                                   HemaNetTask netTask, HemaBaseResult baseResult) {
        }

        @Override
        public void onExecuteFailed(HemaNetWorker netWorker,
                                    HemaNetTask netTask, int failedType) {
        }

    }
    private void init() {
        DOWNLOAD_URL = sysInitInfo.getAndroid_update_url();
//        Aria.download(mContext).register();
//        DownloadEntity entity = Aria.download(this).getDownloadEntity(DOWNLOAD_URL);
//        if (entity != null) {
//           //mSize.setText(CommonUtil.formatFileSize(entity.getFileSize()));
//            int p = (int) (entity.getCurrentProgress() * 100 / entity.getFileSize());
////            mPb.setProgress(p);
////            int state = entity.getState();
////            setBtState(state != DownloadEntity.STATE_RUNNING);
//        } else {
////            setBtState(true);
//        }
    }
    public void alert() {
        init();
        boolean wifi = BaseUtil.isWifiAvailable(mContext);
        savePath = XtomFileUtil.getFileDir(mContext) + "/apps/user_"
                + sysInitInfo.getAndroid_last_version() + ".apk";
//        VersionParams.Builder builder = new VersionParams.Builder();
//        //如果仅使用下载功能，downloadUrl是必须的
//        builder.setOnlyDownload(true)
//                .setDownloadUrl(DOWNLOAD_URL)
//                .setTitle("检测到新版本")
//                .setUpdateMsg("检测到新版本");
//        AllenChecker.startVersionCheck(mContext, builder.build());
//        Builder ab = new Builder(mContext);
//        ab.setTitle("软件更新");
//        if (wifi)
//            ab.setMessage("有最新的软件版本，是否升级？\n(Wifi已连接)");
//        else
//            ab.setMessage("有最新的软件版本，是否升级？\n(Wifi未连接)");
//        ab.setPositiveButton("升级", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                if (sysInitInfo == null)
//                    sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
//            }
//        });
//        ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                if (isMust()) {
//                    BaseUtil.clientLoginout(mContext);
//                } else {
//                    NoNeedUpdate();
//                }
//            }
//        });
//        ab.setCancelable(false);
       // ab.show();
    }
    private  String DOWNLOAD_URL;
    public void upGrade(SysInitInfo sysInitInfo) {
        DOWNLOAD_URL = sysInitInfo.getAndroid_update_url();
        savePath = XtomFileUtil.getFileDir(mContext) + "/apps/user_"
                + sysInitInfo.getAndroid_last_version() + ".apk";
//        Aria.download(mContext)
//                .load(DOWNLOAD_URL)
//                .setDownloadPath(savePath)
//                .start();
//        XtomFileDownLoader downLoader = new XtomFileDownLoader(mContext,
//                downPath, savePath);
//        downLoader.setThreadCount(3);
//       downLoader.setXtomDownLoadListener(new DownLoadListener());
//        downLoader.start();
    }
    private ProgressDialog pBar;
//    @Download.onTaskPre public void onTaskPre(DownloadTask task) {
//        log_e("开始下载---------------------------------------------------------------");
//        pBar = new ProgressDialog(mContext) {
//            @Override
//            public void onBackPressed() {
//                Aria.download(this).load(DOWNLOAD_URL).pause();;
//            }
//        };
////        int s=task.getFileInfo().getContentLength()/1024/1024;
//        pBar.setTitle("正在下载(" + CommonUtil.formatFileSize(task.getFileSize())+")");
//        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pBar.setMax(100);
//        pBar.setCancelable(false);
//        pBar.show();
//    }


    private class DownLoadListener implements XtomDownLoadListener {
        private ProgressDialog pBar;

        @Override
        public void onStart(final XtomFileDownLoader loader) {
            pBar = new ProgressDialog(mContext) {
                @Override
                public void onBackPressed() {
                    loader.stop();
                }
            };
            int s=loader.getFileInfo().getContentLength()/1024/1024;
            pBar.setTitle("正在下载(" + s+"M)");
            pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pBar.setMax(100);
            pBar.setCancelable(false);
            pBar.show();

        }

        @Override
        public void onSuccess(XtomFileDownLoader loader) {
            if (pBar != null) {
                pBar.cancel();
            }
            install();
        }

        void install() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(savePath)),
                    "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }

        @Override
        public void onFailed(XtomFileDownLoader loader) {
            if (pBar != null) {
                pBar.cancel();
            }
            XtomToastUtil.showShortToast(mContext, "下载失败了");
        }

        @Override
        public void onLoading(XtomFileDownLoader loader) {
            FileInfo fileInfo = loader.getFileInfo();
            int curr = fileInfo.getCurrentLength();
            int cont = fileInfo.getContentLength();
            int per = (int) ((float) curr / (float) cont * 100);
            int s=loader.getFileInfo().getContentLength()/1024/1024;
            if (pBar != null) {
                pBar.setProgress(per);
                pBar.setTitle("正在下载(" + s+"M)");
            }
        }

        @Override
        public void onStop(XtomFileDownLoader loader) {
            if (pBar != null) {
                pBar.cancel();
            }
            XtomToastUtil.showShortToast(mContext, "下载停止");
            if (isMust())
                BaseUtil.clientLoginout(mContext);
        }
    }

    public abstract void NoNeedUpdate();
}
