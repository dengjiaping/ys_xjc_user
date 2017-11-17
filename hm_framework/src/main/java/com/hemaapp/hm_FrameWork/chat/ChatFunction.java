package com.hemaapp.hm_FrameWork.chat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/***
 * author: WHB time: 2013-4-15下午3:21:05 memo: 定义系统级别全局函数(全部为public static
 * final修饰)
 */
public class ChatFunction {
	private static final String TAG = "ChatFunction";

	/***
	 * 
	 * author: WHB time: 2013-6-19上午9:35:41 memo: 得到格式化时间串
	 */
	public static final String getTimeStr() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 注意必须是这个，不能随便换形式
		return dateFormat.format(new Date());
	}

	/***
	 * author: WHB time: 2013-4-15下午3:17:58 memo: @param parmLength memo:
	 * 返回固定长度的随机数，在前面补0
	 */
	public static final String getRandStr(int parmLength) {
		Random rm = new Random();
		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, parmLength);
		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);
		// 返回固定的长度的随机数
		return fixLenthString.substring(1, parmLength + 1);
	}

	/***
	 * 
	 * author: WHB time: 2013-6-19下午2:07:41 memo: 得到包标识（时间串+随机串共20位）
	 */
	public static final String getPackIdStr() {
		return getTimeStr() + getRandStr(6);
	}

	/***
	 * 
	 * author: WHB time: 2013-6-21上午10:11:33 memo: @param parmStr memo:
	 * 判断字符串是否为空
	 */
	public static final boolean IsEmptyStr(String parmStr) {
		if (parmStr == null || parmStr.length() == 0)
			return true;
		return false;
	}
}
