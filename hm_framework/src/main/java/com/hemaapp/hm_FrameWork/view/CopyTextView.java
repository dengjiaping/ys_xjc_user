package com.hemaapp.hm_FrameWork.view;

import xtom.frame.util.XtomToastUtil;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.ClipboardManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class CopyTextView extends TextView implements OnLongClickListener {

	public CopyTextView(Context context) {
		super(context);
		setOnLongClickListener(this);
	}

	public CopyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnLongClickListener(this);
	}

	public CopyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setOnLongClickListener(this);
	}

	public CopyTextView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		setOnLongClickListener(this);
	}

	@Override
	public boolean onLongClick(View v) {
		showCopyDialog();
		return true;
	}

	public void showCopyDialog() {
		Builder builder = new Builder(getContext());
		builder.setTitle("请选择");
		String[] strings = { "复制", "取消" };
		builder.setItems(strings, new CopyItemListener());
		AlertDialog dialog = builder.show();
		dialog.setCanceledOnTouchOutside(true);
	}

	private class CopyItemListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:// 复制
				copy();
				break;
			}
		}

		@SuppressWarnings("deprecation")
		private void copy() {
			ClipboardManager cbm = (ClipboardManager) getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
			cbm.setText(getText().toString());
			XtomToastUtil.showShortToast(getContext(), "已复制到剪贴板");
		}

	}

}
