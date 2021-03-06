package com.hemaapp.wcpc_user;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.dialog.HemaButtonDialog;
import com.hemaapp.wcpc_user.activity.LoginActivity;


public class ToLogin {

	private static Activity mActivity;
	private static HemaButtonDialog exitDialog;

	public static void showLogin(final Activity activity)  {
		if (exitDialog == null || activity != mActivity) {
			exitDialog = new HemaButtonDialog(activity);
			exitDialog.setText("此操作需要登录，是否去登录？");
			exitDialog.setLeftButtonText("取消");
			exitDialog.setRightButtonText("登录");
			exitDialog.setRightButtonTextColor(activity.getResources().getColor(R.color.black));
			exitDialog.setButtonListener(new ButtonListener(activity));
		}
		exitDialog.show();
	}

	private static class ButtonListener implements HemaButtonDialog.OnButtonListener {

		private Activity activity;

		public ButtonListener(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onLeftButtonClick(HemaButtonDialog dialog) {
			dialog.cancel();
		}

		@Override
		public void onRightButtonClick(HemaButtonDialog dialog) {
			dialog.cancel();
			Intent it = new Intent(activity, LoginActivity.class);
			activity.startActivity(it);
		}
	}
}
