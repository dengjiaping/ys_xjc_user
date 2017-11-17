package com.hemaapp.wcpc_user;

import android.content.Context;

import xtom.frame.XtomObject;

public class ShareUtil extends XtomObject {
	private Context context;
	private String id;
	private String content;
	private String imgurl;
	private String title;
	private String type;// 分享类型 1：软件 3：商品 2：工匠

	public ShareUtil(Context context, String id, String title, String content,
			String imgurl,String type) {
		this.id = id;
		this.content = content;
		this.imgurl = imgurl;
		this.context = context;
		this.type = type;
		this.title = title;
	}

//	/* 分享相关 */
//	private OnekeyShare oks;
//
//	public void showShare() {
//		if (oks == null) {
//			oks = new OnekeyShare();
//			// 关闭sso授权
//			oks.disableSSOWhenAuthorize();
//			// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//			oks.setTitle(title);
//			// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//			SysInitInfo initInfo = ((BaseActivity) context).getApplicationContext()
//					.getSysInitInfo();
//			String sys_plugins = initInfo.getSys_plugins();
//			String pathWebView="";
//			String pathWX="";
//			if(type.equals("1")){
//				 pathWebView = sys_plugins + "share/sdk.php?id="+0;
//				 pathWX = sys_plugins + "share/sdk.php?id="+0;
//				}
//			if(type.equals("3")){
//			 pathWebView = sys_plugins + "share/sdk.php?id="+id;
//			 pathWX = sys_plugins + "share/sdk.php?id="+id;
//			}
//			if(type.equals("2")){
//				 pathWebView = sys_plugins + "share/sdk2.php?id="+id;
//				 pathWX = sys_plugins + "share/sdk2.php?id="+id;
//				}
//			oks.setTitleUrl(pathWX);
//			// text是分享文本，所有平台都需要这个字段
//			oks.setText(content);
//			// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//			if(isNull(imgurl))
//				oks.setImagePath(initImagePath());
//			else
//				oks.setImageUrl(imgurl);
//			oks.setFilePath(initImagePath());
//			// url仅在微信（包括好友和朋友圈）中使用
//			oks.setUrl(pathWX);
//			// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//			oks.setComment("");
//			// site是分享此内容的网站名称，仅在QQ空间使用
//			oks.setSite(context.getResources().getString(R.string.app_name));
//			// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//			oks.setSiteUrl(pathWebView);
//
//			Bitmap enableLogo = BitmapFactory.decodeResource(
//					context.getResources(), R.drawable.logo_other);
//			// Bitmap disableLogo = BitmapFactory.decodeResource(getResources(),
//			// R.drawable.sharesdk_unchecked);
//			OnClickListener listener = new OnClickListener() {
//				public void onClick(View v) {
////					Intent it = new Intent(context, ShareActivity.class);
////					it.putExtra("id", id);
////					context.startActivity(it);
//				}
//			};
//			//oks.setCustomerLogo(enableLogo, null, "其他", listener);
//
//		}
//		// 启动分享GUI
//		oks.show(context);
//	}
//
//	/* 分享相关end */
//
//	private String initImagePath() {
//		String imagePath;
//		try {
//
//			String cachePath_internal = XtomFileUtil.getCacheDir(context)
//					+ "images/";// 获取缓存路径
//			File dirFile = new File(cachePath_internal);
//			if (!dirFile.exists()) {
//				dirFile.mkdirs();
//			}
//			imagePath = cachePath_internal + "share.png";
//			File file = new File(imagePath);
//			if (!file.exists()) {
//				file.createNewFile();
//				Bitmap pic;
//
//				pic = BitmapFactory.decodeResource(context.getResources(),
//						R.drawable.ic_launcher);
//
//				FileOutputStream fos = new FileOutputStream(file);
//				pic.compress(CompressFormat.PNG, 100, fos);
//				fos.flush();
//				fos.close();
//			}
//		} catch (Throwable t) {
//			t.printStackTrace();
//			imagePath = null;
//		}
//		log_i("imagePath:" + imagePath);
//		return imagePath;
//	}
}
