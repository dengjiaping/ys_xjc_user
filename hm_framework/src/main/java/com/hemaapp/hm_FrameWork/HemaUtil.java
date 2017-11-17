package com.hemaapp.hm_FrameWork;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomTimeUtil;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.SpannableString;
import android.widget.EditText;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.emoji.EmojiParser;
import com.hemaapp.hm_FrameWork.emoji.ParseEmojiMsgUtil;

/**
 * 工具类
 */
public class HemaUtil {

	/**
	 * 转换时间InMillis
	 * 
	 * @param time
	 *            时间字符串
	 * @return long(如果时间字符串无法转换返回0)
	 */
	public static long timeInMillis(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		Date date = null;
		try {
			date = sdf.parse(time);
			return date.getTime();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 获取当前版本号(注意此处获取的为3位服务器版本号)
	 * 
	 * @param context
	 * @return 当前版本号
	 */
	public static final String getAppVersionForSever(Context context) {
		String version = null;
		try {
			version = XtomBaseUtil.getAppVersionName(context);
			String[] vs = version.split("\\.");
			if (vs.length >= 4) {
				version = vs[0] + "." + vs[1] + "." + vs[2];
			}
		} catch (NameNotFoundException e) {
			version = "1.0.0";
		}
		return version;
	}

	/**
	 * 获取当前版本号(注意此处获取的为当前4位版本号的全部字符串)
	 * 
	 * @param context
	 * @return 当前版本号
	 */
	public static final String getAppVersionForTest(Context context) {
		String version = null;
		try {
			version = XtomBaseUtil.getAppVersionName(context);
		} catch (NameNotFoundException e) {
			version = "1.0.0.0";
		}
		return version;
	}

	/**
	 * 比较app版本是否需要升级
	 * 
	 * @param current
	 * @param service
	 * @return
	 */
	public static boolean isNeedUpDate(String current, String service) {
		if (XtomBaseUtil.isNull(current) || XtomBaseUtil.isNull(service))
			return false;

		String[] c = current.split("\\."); // 2.2.3
		String[] s = service.split("\\."); // 2.4.0
		long fc = Long.valueOf(c[0]); // 2
		long fs = Long.valueOf(s[0]); // 2
		if (fc > fs)
			return false;
		else if (fc < fs) {
			return true;
		} else {
			long sc = Long.valueOf(c[1]); // 2
			long ss = Long.valueOf(s[1]); // 4
			if (sc > ss)
				return false;
			else if (sc < ss) {
				return true;
			} else {
				long tc = Long.valueOf(c[2]); // 3
				long ts = Long.valueOf(s[2]); // 0
				if (tc >= ts)
					return false;
				else
					return true;
			}
		}
	}

	/**
	 * 转换时间显示形式(与当前系统时间比较),在发表话题、帖子和评论时使用
	 * 
	 * @param time
	 *            时间字符串
	 * @return String
	 */
	public static String transTime(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault());
			String current = XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
			String dian24 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
					+ " 24:00:00";
			String dian00 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
					+ " 00:00:00";
			Date now = null;
			Date date = null;
			Date d24 = null;
			Date d00 = null;

			now = sdf.parse(current); // 将当前时间转化为日期
			date = sdf.parse(time); // 将传入的时间参数转化为日期
			d24 = sdf.parse(dian24);
			d00 = sdf.parse(dian00);

			long diff = now.getTime() - date.getTime(); // 获取二者之间的时间差值
			long min = diff / (60 * 1000);
			if (min <= 5)
				return "刚刚";
			if (min < 60)
				return min + "分钟前";

			if (now.getTime() <= d24.getTime()
					&& date.getTime() >= d00.getTime())
				return "今天" + XtomTimeUtil.TransTime(time, "HH:mm");

			int sendYear = Integer
					.valueOf(XtomTimeUtil.TransTime(time, "yyyy"));
			int nowYear = Integer.valueOf(XtomTimeUtil.TransTime(current,
					"yyyy"));
			if (sendYear < nowYear)
				return XtomTimeUtil.TransTime(time, "yyyy-MM-dd HH:mm");
			else
				return XtomTimeUtil.TransTime(time, "MM-dd HH:mm");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 转换时间显示形式(与当前系统时间比较),在显示即时聊天的时间时使用
	 * 
	 * @param time
	 *            时间字符串
	 * @return String
	 */
	public static String transTimeChat(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault());
			String current = XtomTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss");
			String dian24 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
					+ " 24:00:00";
			String dian00 = XtomTimeUtil.TransTime(current, "yyyy-MM-dd")
					+ " 00:00:00";
			Date now = null;
			Date date = null;
			Date d24 = null;
			Date d00 = null;
			try {
				now = sdf.parse(current); // 将当前时间转化为日期
				date = sdf.parse(time); // 将传入的时间参数转化为日期
				d24 = sdf.parse(dian24);
				d00 = sdf.parse(dian00);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long diff = now.getTime() - date.getTime(); // 获取二者之间的时间差值
			long min = diff / (60 * 1000);
			if (min <= 5)
				return "刚刚";
			if (min < 60)
				return min + "分钟前";

			if (now.getTime() <= d24.getTime()
					&& date.getTime() >= d00.getTime())
				return "今天" + XtomTimeUtil.TransTime(time, "HH:mm");

			int sendYear = Integer
					.valueOf(XtomTimeUtil.TransTime(time, "yyyy"));
			int nowYear = Integer.valueOf(XtomTimeUtil.TransTime(current,
					"yyyy"));
			if (sendYear < nowYear)
				return XtomTimeUtil.TransTime(time, "yyyy-MM-dd HH:mm");
			else
				return XtomTimeUtil.TransTime(time, "MM-dd HH:mm");
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 隐藏手机号和邮箱显示
	 * 
	 * @param old
	 *            需要隐藏的手机号或邮箱
	 * @param keytype
	 *            1手机2邮箱
	 * @return
	 */
	public static String hide(String old, String keytype) {
		try {
			if ("1".equals(keytype))
				return old.substring(0, 3) + "****" + old.substring(7, 11);
			else {
				StringBuilder sb = new StringBuilder();
				String[] s = old.split("@");
				int l = s[0].length();
				int z = l / 3;
				sb.append(s[0].substring(0, z));
				int y = l % 3;
				for (int i = 0; i < z + y; i++)
					sb.append("*");
				sb.append(s[0].substring(z * 2 + y, l));
				sb.append("@");
				if (s[1] == null) {

				}
				sb.append(s[1]);
				return sb.toString();
			}
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 聊天时，转换表情显示
	 * 
	 * @param mContext
	 * @param textView
	 * @param content
	 */
	public static void SetMessageTextView(Context mContext, TextView textView,
			String content) {
		if (content == null || "".equals(content)) {
			textView.setText("");
			return;
		}
		String unicode = EmojiParser.getInstance(mContext).parseEmoji(content);
		SpannableString spannableString = ParseEmojiMsgUtil
				.getExpressionString(mContext, unicode);
		textView.setText(spannableString);
		if (textView instanceof EditText && spannableString != null) {
			((EditText) textView).setSelection(spannableString.length());
		}
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device
		ActivityManager activityManager = (ActivityManager) context
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		String packageName = context.getApplicationContext().getPackageName();
		System.out.println("packageName=" + packageName);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断当前用户是否是第三方登录
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isThirdSave(Context context) {
		String thirdsave = XtomSharedPreferencesUtil.get(context, "thirdsave");
		return "true".equals(thirdsave);
	}

	/**
	 * 设置当前用户是否是第三方登录
	 * 
	 * @param context
	 * @param thirdsave
	 */
	public static void setThirdSave(Context context, boolean thirdsave) {
		XtomSharedPreferencesUtil.save(context, "thirdsave", thirdsave ? "true"
				: "false");
	}
}
