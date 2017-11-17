package com.hemaapp.hm_FrameWork;

import xtom.frame.XtomAdapter;
import xtom.frame.XtomFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;

public abstract class HemaAdapter extends XtomAdapter {
	protected static final int VIEWTYPE_EMPTY = 0;
	protected static final int VIEWTYPE_NORMAL = 1;

	private String emptyString = "列表为空";
	private TextView emptyTextView;

	public HemaAdapter(Context mContext) {
		super(mContext);
	}

	public HemaAdapter(XtomFragment mFragment) {
		super(mFragment);
	}

	@Override
	public int getItemViewType(int position) {
		if (isEmpty())
			return VIEWTYPE_EMPTY;
		return VIEWTYPE_NORMAL;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	/**
	 * 获取列表为空时的显示View(调用此方法(不重写getItemViewType时)需重写isEmpty()方法)
	 * 
	 * @return a view 传递getView方法中的ViewGroup参数即可
	 */
	public View getEmptyView(ViewGroup parent) {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.listitem_empty, null);
		emptyTextView = (TextView) view.findViewById(R.id.textview);
		emptyTextView.setText(emptyString);
		int width = parent.getWidth();
		int height = parent.getHeight();
		LayoutParams params = new LayoutParams(width, height);
		view.setLayoutParams(params);
		return view;
	}

	/**
	 * 设置空列表提示语
	 * 
	 * @param emptyString
	 */
	public void setEmptyString(String emptyString) {
		if (emptyTextView != null)
			emptyTextView.setText(emptyString);
		this.emptyString = emptyString;
	}

	/**
	 * 设置空列表提示语
	 * 
	 * @param emptyStrID
	 */
	public void setEmptyString(int emptyStrID) {
		emptyString = mContext.getResources().getString(emptyStrID);
		setEmptyString(emptyString);
	}

}
