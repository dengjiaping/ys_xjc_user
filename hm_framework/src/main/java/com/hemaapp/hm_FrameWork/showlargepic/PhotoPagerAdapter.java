package com.hemaapp.hm_FrameWork.showlargepic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.XtomActivity;
import xtom.frame.image.cache.XtomImageCache;
import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageTask.Size;
import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomTimeUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.util.XtomWindowSize;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hemaapp.hm_FrameWork.R;
import com.hemaapp.hm_FrameWork.view.photoview.PhotoView;
import com.hemaapp.hm_FrameWork.view.photoview.PhotoViewAttacher.OnViewTapListener;

/**
 *
 */
public class PhotoPagerAdapter extends PagerAdapter {
	private XtomActivity context;
	private ArrayList<String> urllist;

	public PhotoPagerAdapter(XtomActivity context, ArrayList<String> urllist) {
		this.context = context;
		this.urllist = urllist;
	}

	@Override
	public int getCount() {
		return urllist == null ? 0 : urllist.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.griditem_showlargepic, null);

		PhotoView photoView = (PhotoView) view.findViewById(R.id.imageview);
		ProgressBar progressBar = (ProgressBar) view
				.findViewById(R.id.progressbar);
		photoView.setOnViewTapListener(new OnViewTapListener() {

			@Override
			public void onViewTap(View view, float x, float y) {
				ShowLargePicActivity a = (ShowLargePicActivity) context;
				a.toogleInfo();
			}
		});
		// Now just add PhotoView to ViewPager and return it
		container.addView(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		String imgPath = urllist.get(position);
		try {
			URL url = new URL(imgPath);
			context.imageWorker.loadImage(new ImageTask(photoView, url,
					context, photoView, progressBar));
			photoView.setOnLongClickListener(new LongClickListener(null,
					imgPath));
		} catch (MalformedURLException e) {
			int width = XtomWindowSize.getWidth(context);
			context.imageWorker.loadImage(new ImageTask(photoView, imgPath,
					context, new Size(width, width), photoView, progressBar));
			photoView.setOnLongClickListener(new LongClickListener(imgPath,
					null));
		}
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	private class LongClickListener implements OnLongClickListener {
		private String localPath;
		private String urlPath;

		public LongClickListener(String localPath, String urlPath) {
			this.localPath = localPath;
			this.urlPath = urlPath;
		}

		@Override
		public boolean onLongClick(View v) {
			Builder builder = new Builder(context);
			String[] items = { "保存到手机", "取消" };
			builder.setItems(items, new DialogClickListener(localPath, urlPath));
			builder.show();
			return true;
		}

	}

	private class DialogClickListener implements
			DialogInterface.OnClickListener {
		private String localPath;
		private String urlPath;

		public DialogClickListener(String localPath, String urlPath) {
			this.localPath = localPath;
			this.urlPath = urlPath;
		}

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
			try {
				if (!XtomFileUtil.isExternalMemoryAvailable()) {
					XtomToastUtil.showShortToast(context, "没有SD卡,不能复制");
					return;
				}
				String imgPath;
				if (XtomBaseUtil.isNull(urlPath)) {
					imgPath = localPath;
				} else {
					imgPath = XtomImageCache.getInstance(context)
							.getPathAtLoacal(urlPath);
				}
				String saveDir = XtomFileUtil.getExternalMemoryPath();
				String pakage = context.getPackageName();
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
					XtomToastUtil.showShortToast(context, "图片已保存至" + saveDir);
					Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					Uri uri = Uri.fromFile(new File(savePath));
					intent.setData(uri);
					context.sendBroadcast(intent);//
				} else {
					XtomToastUtil.showShortToast(context, "图片保存失败");
				}
			} catch (Exception e) {
				XtomToastUtil.showShortToast(context, "图片保存失败");
			}
		}
	}

	private class ImageTask extends XtomImageTask {
		PhotoView photoView;
		ProgressBar progressBar;

		public ImageTask(ImageView imageView, URL url, Object context,
				PhotoView photoView, ProgressBar progressBar) {
			super(imageView, url, context);
			this.photoView = photoView;
			this.progressBar = progressBar;
		}

		public ImageTask(ImageView imageView, String path, Object context,
				Size size, PhotoView photoView, ProgressBar progressBar) {
			super(imageView, path, context, size);
			this.photoView = photoView;
			this.progressBar = progressBar;
		}

		@Override
		public void success() {
			photoView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			super.success();
		}

		@Override
		public void failed() {
			photoView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			super.failed();
		}

		@Override
		public void beforeload() {
			photoView.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			super.beforeload();
		}

	}

}
