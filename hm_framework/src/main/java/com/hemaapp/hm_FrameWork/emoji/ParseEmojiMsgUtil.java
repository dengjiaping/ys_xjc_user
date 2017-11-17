package com.hemaapp.hm_FrameWork.emoji;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;

import com.hemaapp.hm_FrameWork.R;

public class ParseEmojiMsgUtil {
	private static final String TAG = ParseEmojiMsgUtil.class.getSimpleName();
	private static final String REGEX_STR = "\\[e\\](.*?)\\[/e\\]";

	private static int density = 0;

	public static void dealExpression(Context context,
			SpannableString spannableString, Pattern patten, int start)
			throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			if (matcher.start() < start) {
				continue;
			}
			String name = key.substring(key.indexOf("]") + 1,
					key.lastIndexOf("["));
			Field field = R.drawable.class.getDeclaredField("emoji_" + name);
			String source = "[e]" + name + "[/e]";
			int resId = Integer.parseInt(field.get(null).toString());
			if (resId != 0) {
				if (density == 0) {
					float dsy = context.getResources().getDisplayMetrics().density;
					float m5 = 5 * dsy;
					if(m5 > 15){
						density = 520;
					}else if (m5 > 8) {
						density = 420;
					} else if (m5 > 5) {
						density = 320;
					} else if (m5 > 3) {
						density = 240;
					} else {
						density = 160;
					}
				}
				BitmapDrawable drawable = (BitmapDrawable) context
						.getResources().getDrawable(resId);
				if(density == 520){// 增大高分辨率
					drawable.setTargetDensity(420);
				}else if (density == 420) {
					drawable.setTargetDensity(240);
				} else if (density == 320) {
					drawable.setTargetDensity(160);
				} else if (density == 240) {
					drawable.setTargetDensity(120);
				} else {
					drawable.setTargetDensity(80);
				}

				// int w = drawable.getIntrinsicWidth();
				// int h = drawable.getIntrinsicHeight();
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight());
				ImageSpan imageSpan = new ImageSpan(drawable, source);
				int end = matcher.start() + key.length();
				spannableString.setSpan(imageSpan, matcher.start(), end,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				if (end < spannableString.length()) {
					dealExpression(context, spannableString, patten, end);
				}
				break;
			}
		}
	}

	public static SpannableString getExpressionString(Context context,
			String str) {
		SpannableString spannableString = new SpannableString(str);
		Pattern sinaPatten = Pattern.compile(REGEX_STR,
				Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return spannableString;
	}

	public static String convertToMsg(CharSequence cs, Context mContext) {
		SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
		ImageSpan[] spans = ssb.getSpans(0, cs.length(), ImageSpan.class);
		for (int i = 0; i < spans.length; i++) {
			ImageSpan span = spans[i];
			String c = span.getSource();
			int a = ssb.getSpanStart(span);
			int b = ssb.getSpanEnd(span);
			if (c.contains("[e]") || c.contains("[")) {
				ssb.replace(a, b, convertUnicode(c));
			}
		}
		ssb.clearSpans();
		return ssb.toString();
	}

	private static String convertUnicode(String emo) {
		if (emo.contains("[e]")) {
			emo = emo.substring(3, emo.length() - 4);
		} else if (emo.contains("[")) {
			emo = emo.substring(1, emo.length() - 1);
		}

		if (emo.length() < 6) {
			return new String(Character.toChars(Integer.parseInt(emo, 16)));
		}
		String[] emos = emo.split("_");
		char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
		char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
		char[] emoji = new char[char0.length + char1.length];
		for (int i = 0; i < char0.length; i++) {
			emoji[i] = char0[i];
		}
		for (int i = char0.length; i < emoji.length; i++) {
			emoji[i] = char1[i - char0.length];
		}
		return new String(emoji);
	}
}
