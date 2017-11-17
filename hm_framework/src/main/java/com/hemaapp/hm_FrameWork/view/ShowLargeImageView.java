package com.hemaapp.hm_FrameWork.view;

import java.net.MalformedURLException;
import java.net.URL;

import xtom.frame.XtomObject;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomTimeUtil;
import xtom.frame.util.XtomToastUtil;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;

import com.hemaapp.hm_FrameWork.R;
import com.hemaapp.hm_FrameWork.view.photoview.PhotoView;
import com.hemaapp.hm_FrameWork.view.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class ShowLargeImageView extends XtomObject {
	private Activity mContext;
	private View father;
	private PopupWindow mWindow;
	private ViewGroup mViewGroup;

	private PhotoView mImageView;
	private ProgressBar mProgressBar;
	private XtomImageWorker imageWorker;
	private String localPath;
	private String urlPath;

	public ShowLargeImageView(Activity context, View activityRootView) {
		mContext = context;
		imageWorker = new XtomImageWorker(mContext);
		father = activityRootView;
		mWindow = new PopupWindow(mContext);
		mWindow.setWidth(LayoutParams.MATCH_PARENT);
		mWindow.setHeight(LayoutParams.MATCH_PARENT);
		mWindow.setBackgroundDrawable(new ColorDrawable(0x55000000));
		mWindow.setFocusable(true);
		mWindow.setAnimationStyle(R.style.PopupAnimation);
		mViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(
				R.layout.showlargeimageview, null);
		findView();
		setListener();
		mWindow.setContentView(mViewGroup);

	}

	/**
	 * 设置网络图片地址
	 * 
	 * @param path
	 */
	public void setImageURL(String urlPath) {
		this.urlPath = urlPath;
		this.localPath = null;
		try {
			URL url = new URL(urlPath);
			ImageTask task = new ImageTask(mImageView, url, mContext);

			imageWorker.loadImage(task);

		} catch (MalformedURLException e) {
			//
		}
	}

	/**
	 * 设置本地图片地址
	 * 
	 * @param localPath
	 */
	public void setImagePath(String localPath) {
		this.urlPath = null;
		this.localPath = localPath;
		ImageTask task = new ImageTask(mImageView, localPath, mContext);
		imageWorker.loadImage(task);

	}

	private void findView() {
		mImageView = (PhotoView) mViewGroup.findViewById(R.id.imageview);
		mProgressBar = (ProgressBar) mViewGroup.findViewById(R.id.progressbar);
	}

	private void setListener() {
		mWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});
		mViewGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dimiss();
			}
		});
		mImageView.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View view, float x, float y) {
				dimiss();
			}
		});
		mImageView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Builder builder = new Builder(mContext);
				String[] items = { "保存到手机", "取消" };
				builder.setItems(items, new DialogClickListener());
				builder.show();
				return true;
			}
		});
	}

	private class DialogClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:// 保存到手机
				copy();
				break;
			case 1:// 取消

				break;
			}
		}

		// 复制文件
		private void copy() {
			if (!XtomFileUtil.isExternalMemoryAvailable()) {
				XtomToastUtil.showShortToast(mContext, "没有SD卡,不能复制");
				return;
			}
			String imgPath;
			if (isNull(urlPath)) {
				imgPath = localPath;
			} else {
				imgPath = XtomImageCache.getInstance(mContext).getPathAtLoacal(
						urlPath);
			}
			String saveDir = XtomFileUtil.getExternalMemoryPath();
			String pakage = mContext.getPackageName();
			String folder = "images";
			int dot = pakage.lastIndexOf('.');
			if (dot != -1) {
				folder = pakage.substring(dot + 1);
			}
			saveDir += ("/hemaapp/" + folder + "/");
			String fileName = XtomTimeUtil
					.getCurrentTime("yyyy-MM-dd_HH-mm-ss") + ".jpg";
			String savePath = saveDir + fileName;
			if (XtomFileUtil.copy(imgPath, savePath)) {
				XtomToastUtil.showShortToast(mContext, "图片已保存至" + saveDir);
			} else {
				XtomToastUtil.showShortToast(mContext, "图片保存失败");
			}
		}
	}

	public void show() {
		mWindow.showAtLocation(father, Gravity.BOTTOM, 0, 0);
	}

	public void dimiss() {
		mWindow.dismiss();
	}

	private class ImageTask extends XtomImageTask {

		public ImageTask(ImageView imageView, URL url, Object context) {
			super(imageView, url, context);
		}

		public ImageTask(ImageView imageView, String path, Object context) {
			super(imageView, path, context);
		}

		@Override
		public void success() {
			mImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
			super.success();
		}

		@Override
		public void failed() {
			mImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
			super.failed();
		}

		@Override
		public void beforeload() {
			mImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
			super.beforeload();
		}

	}
}
