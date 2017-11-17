package com.hemaapp.hm_FrameWork.chat;

import xtom.frame.XtomObject;
import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;

public class ChatNotice extends XtomObject {
	private Context mContext;
	private Handler mHandler;
	private MediaPlayer mPlayer;
	private Vibrator mVibrator;
	private MediaNoticeRunable mediaNoticeRunable;

	public ChatNotice(Context context) {
		super();
		mContext = context;
		Looper looper;
		if ((looper = Looper.myLooper()) != null) {
			mHandler = new Handler(looper);
		} else if ((looper = Looper.getMainLooper()) != null) {
			mHandler = new Handler(looper);
		} else {
			mHandler = null;
		}

		try {
			AssetFileDescriptor afd = context.getResources().openRawResourceFd(
					ChatConfig.getNoticeringid(mContext));
			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());
			mPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);// 使用通知音量
			afd.close();
			mPlayer.prepare();
		} catch (Exception e) {
			// ignore
		}

		mVibrator = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
	}

	public void mediaNotice() {
		if (mediaNoticeRunable == null)
			mediaNoticeRunable = new MediaNoticeRunable();
		else
			mHandler.removeCallbacks(mediaNoticeRunable);
		mHandler.postDelayed(new MediaNoticeRunable(), 1000);
	}

	private class MediaNoticeRunable implements Runnable {

		@Override
		public void run() {
			if (isMute()) {
				mVibrator.vibrate(new long[] { 50, 300, 100, 300 }, -1);
			} else {
				mPlayer.start();
			}
		}
	}

	// 判断手机是否静音
	private boolean isMute() {
		AudioManager am = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		final int ringerMode = am.getRingerMode();
		switch (ringerMode) {
		case AudioManager.RINGER_MODE_VIBRATE:
		case AudioManager.RINGER_MODE_SILENT:
			return true;
		}
		return false;
	}

}
