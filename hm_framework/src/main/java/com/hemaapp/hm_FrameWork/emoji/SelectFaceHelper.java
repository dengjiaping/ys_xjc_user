package com.hemaapp.hm_FrameWork.emoji;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.hemaapp.hm_FrameWork.R;

public class SelectFaceHelper implements OnItemClickListener {
	private static final String TAG = SelectFaceHelper.class.getSimpleName();
	private Context context;
	private View mFaceView;
	private ViewPager mViewPager;
	private LinearLayout mIndexContainer;
	private LayoutInflater mInflater;
	private int pageSize = 23;
	private List<MsgEmojiModle> mMsgEmojiData = new ArrayList<MsgEmojiModle>();
	public List<List<MsgEmojiModle>> mPageEmojiDatas = new ArrayList<List<MsgEmojiModle>>();

	private ArrayList<View> pageViews;

	private List<FaceAdapter> faceAdapters;

	private int current = 0;
	private ArrayList<ImageView> pointViews;

	private OnFaceOprateListener mOnFaceOprateListener;

	public SelectFaceHelper(Context context, View toolView) {
		this.context = context;
		mInflater = LayoutInflater.from(this.context);
		mFaceView = toolView;
		mViewPager = (ViewPager) mFaceView.findViewById(R.id.face_viewpager);
		mIndexContainer = (LinearLayout) mFaceView
				.findViewById(R.id.msg_face_index_view);
		ParseData();
		initView();

	}

	private void initView() {
		Init_viewPager();
		Init_Point();
		Init_Data();
	}

	private void Init_viewPager() {
		pageViews = new ArrayList<View>();
		View nullView1 = new View(context);
		nullView1.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView1);

		faceAdapters = new ArrayList<FaceAdapter>();
		for (int i = 0; i < mPageEmojiDatas.size(); i++) {
			GridView view = (GridView) mInflater.inflate(
					R.layout.msg_face_gridview, null);
			FaceAdapter adapter = new FaceAdapter(context,
					mPageEmojiDatas.get(i));
			view.setSelector(R.drawable.transparent_background);
			view.setAdapter(adapter);
			faceAdapters.add(adapter);
			view.setOnItemClickListener(this);
			pageViews.add(view);
		}

		View nullView2 = new View(context);
		nullView2.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView2);
	}

	private void Init_Point() {

		pointViews = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.icon_jw_face_index_nor);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			mIndexContainer.addView(imageView, layoutParams);
			if (i == 0 || i == pageViews.size() - 1) {
				imageView.setVisibility(View.GONE);
			}
			if (i == 1) {
				imageView
						.setBackgroundResource(R.drawable.icon_jw_face_index_prs);
			}
			pointViews.add(imageView);

		}
	}

	private void Init_Data() {
		mViewPager.setAdapter(new ViewPagerAdapter(pageViews));
		mViewPager.setCurrentItem(1);
		current = 0;
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				current = arg0 - 1;
				draw_Point(arg0);
				if (arg0 == pointViews.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						mViewPager.setCurrentItem(arg0 + 1);
						pointViews.get(1).setBackgroundResource(
								R.drawable.icon_jw_face_index_prs);
					} else {
						mViewPager.setCurrentItem(arg0 - 1);
						pointViews.get(arg0 - 1).setBackgroundResource(
								R.drawable.icon_jw_face_index_prs);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	public void draw_Point(int index) {
		for (int i = 1; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(
						R.drawable.icon_jw_face_index_prs);
			} else {
				pointViews.get(i).setBackgroundResource(
						R.drawable.icon_jw_face_index_nor);
			}
		}
	}

	private void ParseData() {
		MsgEmojiModle emojEentry;
		try {
			int len = MsgFaceUtils.faceImgs.length;
			for (int i = 0; i < len; i++) {
				int resID = MsgFaceUtils.faceImgs[i];
				if (resID != 0) {
					emojEentry = new MsgEmojiModle();
					emojEentry.setId(resID);
					emojEentry.setCharacter(MsgFaceUtils.faceImgNames[i]);
					mMsgEmojiData.add(emojEentry);
				}
			}
			int pageCount = (int) Math.ceil(mMsgEmojiData.size() / pageSize
					+ 0.1);
			for (int i = 0; i < pageCount; i++) {
				mPageEmojiDatas.add(getData(i));
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	private List<MsgEmojiModle> getData(int page) {
		int startIndex = page * pageSize;
		int endIndex = startIndex + pageSize;
		if (endIndex > mMsgEmojiData.size()) {
			endIndex = mMsgEmojiData.size();
		}
		List<MsgEmojiModle> list = new ArrayList<MsgEmojiModle>();
		list.addAll(mMsgEmojiData.subList(startIndex, endIndex));
		if (list.size() < pageSize) {
			for (int i = list.size(); i < pageSize; i++) {
				MsgEmojiModle object = new MsgEmojiModle();
				list.add(object);
			}
		}
		if (list.size() == pageSize) {
			MsgEmojiModle object = new MsgEmojiModle();
			object.setId(R.drawable.face_delete_select);
			list.add(object);
		}
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MsgEmojiModle msgEmoji = (MsgEmojiModle) faceAdapters.get(current)
				.getItem(position);
		if (msgEmoji.getId() == R.drawable.face_delete_select) {
			if (null != mOnFaceOprateListener) {
				mOnFaceOprateListener.onFaceDeleted();
			}
		}
		if (msgEmoji.getCharacter() != null) {
			String emojiStr = EmojiParser.getInstance(context).convertEmoji(
					msgEmoji.getCharacter());
			SpannableString spannableString = EmojiParser.getInstance(context)
					.addFace(context, msgEmoji.getId(), emojiStr);
			Log.d(TAG, spannableString.toString());
			if (null != mOnFaceOprateListener) {
				mOnFaceOprateListener.onFaceSelected(spannableString);
			}
		}
	}

	public void setFaceOpreateListener(
			OnFaceOprateListener mOnFaceOprateListener) {
		this.mOnFaceOprateListener = mOnFaceOprateListener;
	}

	public interface OnFaceOprateListener {

		void onFaceSelected(SpannableString spanEmojiStr);

		void onFaceDeleted();
	}
}
