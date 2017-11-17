package com.hemaapp.hm_FrameWork;

import java.io.File;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomObject;
import xtom.frame.fileload.FileInfo;
import xtom.frame.fileload.XtomFileDownLoader;
import xtom.frame.fileload.XtomFileDownLoader.XtomDownLoadListener;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomToastUtil;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * 软件升级
 */
public class HemaUpGrade extends XtomObject {
	private Context mContext;
	private HemaUser mUser;
	private String savePath;

	public HemaUpGrade(HemaUser user) {
		this.mContext = XtomActivityManager.getLastActivity();
		this.mUser = user;
	}

	// 是否强制升级
	private boolean isMust() {
		boolean must = "1".equals(mUser.getAndroid_must_update());
		return must;
	}

	public void alert(String curr, String server) {
		Builder ab = new Builder(mContext);
		ab.setTitle("软件更新");
		String message = "当前客户端版本是" + curr + ",服务器最新版本是" + server + ",确定要升级吗？";
		ab.setMessage(message);
		ab.setPositiveButton("升级", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				upGrade();
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				if (isMust())
					XtomActivityManager.finishAll();
			}
		});
		ab.setCancelable(false);
		ab.show();
	}

	public void upGrade() {
		String downPath = mUser.getAndroid_update_url();
		savePath = XtomFileUtil.getFileDir(mContext) + "/apps/hemaapp_"
				+ mUser.getAndroid_last_version() + ".apk";
		XtomFileDownLoader downLoader = new XtomFileDownLoader(mContext,
				downPath, savePath);
		downLoader.setThreadCount(3);
		downLoader.setXtomDownLoadListener(new DownLoadListener());
		downLoader.start();
	}

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
			pBar.setTitle("正在下载");
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
			if (pBar != null) {
				pBar.setProgress(per);
			}
		}

		@Override
		public void onStop(XtomFileDownLoader loader) {
			if (pBar != null) {
				pBar.cancel();
			}
			XtomToastUtil.showShortToast(mContext, "下载停止");
			if (isMust())
				XtomActivityManager.finishAll();
		}
	}

}
