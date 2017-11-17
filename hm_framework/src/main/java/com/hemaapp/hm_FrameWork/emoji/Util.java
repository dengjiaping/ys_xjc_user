package com.hemaapp.hm_FrameWork.emoji;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

	public static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());
		String strDate = format.format(date);
		return strDate;

	}
}
