package com.hemaapp.wcpc_user.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.adapter.InviteBookAdapter;
import com.hemaapp.wcpc_user.model.Client;
import com.hemaapp.wcpc_user.model.User;
import com.hemaapp.wcpc_user.util.PinyinComparatorClient;
import com.hemaapp.wcpc_user.view.LetterListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 邀请-通讯录
 */
public class InviteBookActivity extends BaseActivity {
    private TextView titleText;
    private ImageView titleLeft;
    private TextView titleRight;

    private XtomRefreshLoadmoreLayout layout;
    private XtomListView listView;
    private ProgressBar progressBar;

    private InviteBookAdapter mAdapter;
    private ArrayList<Client> allUsers = new ArrayList<Client>();// 通讯录中的全部用户
    private ArrayList<Client> invitedUsers = new ArrayList<Client>();// 已经邀请的用户
    private ArrayList<Client> noinvitUsers = new ArrayList<Client>();// 未邀请的用户

    private LetterListView letterList;
    private TextView overlay;
    private PinyinComparatorClient pinyinComparator = new PinyinComparatorClient();
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private Handler handler;
    private OverlayThread overlayThread;
    private WindowManager windowManager;
    private ArrayList<String> strings = new ArrayList<>();
    boolean isEdit = false;
    private ImageView iv_all;
    private TextView tv_button;
    private TextView tv_all;
    private LinearLayout lv_bottom;
    boolean isSelectAll = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invitebook);
        super.onCreate(savedInstanceState);
        initOverlay();
        UserList();
    }

    private void UserList() {
        User user = BaseApplication.getInstance().getUser();
        String token = user.getToken();
        String keyid = getPhoneContacts();
        if (!isNull(keyid)) {
            for (String s : strings) {
                getNetWorker().mobileList(token, s);
            }
        } else {
            layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            freshData("通讯录为空");
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case MOBILE_LIST:
                // nothing
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case MOBILE_LIST:
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                letterList.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case MOBILE_LIST:
                @SuppressWarnings("unchecked")
                HemaArrayResult<Client> cResult = (HemaArrayResult<Client>) baseResult;
                allUsers = cResult.getObjects();
                for (Client User : allUsers) {
                    noinvitUsers.add(User);
                    Collections.sort(noinvitUsers, pinyinComparator);
                }
                freshData("");
                break;
            default:
                break;
        }
    }

    private void freshData(String emptyMsg) {
        if (mAdapter == null) {
            mAdapter = new InviteBookAdapter(this, noinvitUsers,
                    getNetWorker());
            mAdapter.setEmptyString(emptyMsg);
            listView.setAdapter(mAdapter);
        } else {
            mAdapter.setEmptyString(emptyMsg);
            mAdapter.notifyDataSetChanged();
        }
        alphaIndexer = mAdapter.getAlphaIndexer();
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case MOBILE_LIST:
                layout.refreshFailed();
                freshData("");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case MOBILE_LIST:
                layout.refreshFailed();
                freshData("");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        titleText = (TextView) findViewById(R.id.title_text);
        titleLeft = (ImageView) findViewById(R.id.title_btn_left);
        titleRight = (TextView) findViewById(R.id.title_btn_right);

        layout = (XtomRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        listView = (XtomListView) findViewById(R.id.listview);
        letterList = (LetterListView) findViewById(R.id.letterListView);

        iv_all = (ImageView) findViewById(R.id.iv_all);
        tv_button = (TextView) findViewById(R.id.tv_button);
        tv_all = (TextView) findViewById(R.id.tv_all);
        lv_bottom = (LinearLayout) findViewById(R.id.lv_bottom);
    }

    @Override
    protected void setListener() {
        titleText.setText("通讯录好友");
        titleLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleRight.setText("编辑");
        titleRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isEdit) {
                    titleRight.setText("编辑");
                    lv_bottom.setVisibility(View.GONE);
                    isEdit = false;
                    for (Client User : noinvitUsers) {
                        User.setSelect(false);
                        User.setEidit(false);
                    }
                } else {
                    titleRight.setText("取消");
                    lv_bottom.setVisibility(View.VISIBLE);
                    for (Client User : noinvitUsers) {
                        User.setSelect(false);
                        User.setEidit(true);
                    }
                    isEdit = true;
                }
                isAll();
                freshData("");
            }
        });
        layout.setLoadmoreable(false);
        layout.setRefreshable(false);
        letterList
                .setOnTouchingLetterChangedListener((LetterListView.OnTouchingLetterChangedListener) new LetterListViewListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit) {
                    noinvitUsers.get(position).setSelect(!noinvitUsers.get(position).isSelect());
                    freshData("");
                    isAll();
                }
            }
        });
        tv_all.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                for (Client User : noinvitUsers) {
                    if (isSelectAll)
                        User.setSelect(false);
                    else
                        User.setSelect(true);
                }
                freshData("");
                isAll();
            }
        });
        iv_all.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                for (Client User : noinvitUsers) {
                    if (isSelectAll)
                        User.setSelect(false);
                    else
                        User.setSelect(true);
                }
                freshData("");
                isAll();
            }
        });
        tv_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String mob = "";
                for (Client User : noinvitUsers) {
                    if (User.getIs_reg().equals("0")) {
                        if (User.isSelect()) {
                            if (isNull(mob))
                                mob = User.getMobile();
                            else {
                                mob = mob + ";" + User.getMobile();
                            }
                        }
                    }
                }
                if (isNull(mob)) {
                    XtomToastUtil.showShortToast(mContext, "请选择邀请好友！");
                    return;
                }
                String msg = BaseApplication.getInstance()
                        .getSysInitInfo().getMsg_invite() + "下载链接" + BaseApplication.getInstance().getSysInitInfo().getSys_plugins() +
                        "share/sdk.php?invitecode=" + BaseApplication.getInstance().getUser().getInvitecode() + "&keyid=0" + "&type=1" + "【小叫车】跨城用车，就用小叫车！";
                Uri uri = Uri.parse("smsto:" + mob);
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", msg);
                startActivity(it);
            }
        });

    }

    private void isAll() {
        boolean isAll = true;
        for (Client User : noinvitUsers) {
            if (User.getIs_reg().equals("0") && !User.isSelect()) {
                isAll = false;
            }
        }
        if (isAll) {
            iv_all.setImageResource(R.mipmap.img_selectall_p);
        } else {
            iv_all.setImageResource(R.mipmap.img_selectall_n);
        }
        isSelectAll = isAll;
    }

    // 初始化汉语拼音首字母弹出提示框
    @SuppressLint("InflateParams")
    private void initOverlay() {
        handler = new Handler();
        overlayThread = new OverlayThread();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) mContext.getSystemService(
                Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private class LetterListViewListener implements
            LetterListView.OnTouchingLetterChangedListener {

        @SuppressLint("NewApi")
        @Override
        public void onTouchingLetterChanged(final String s) {
            Integer up = alphaIndexer.get(s.toUpperCase(Locale.getDefault()));
            Integer low = alphaIndexer.get(s.toLowerCase(Locale.getDefault()));
            if (alphaIndexer != null && (up != null || low != null)) {
                final int position = up == null ? low : up;
                listView.setSelection(position + invitedUsers.size());
            } else if ("↑".equals(s)) {
                listView.setSelection(0);
            }
            overlay.setText(s);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1500);
        }
    }

    /**
     * 得到手机通讯录联系人信息
     **/
    private String getPhoneContacts() {
        int i = 0;
        String mobile_list = null;
        // 获取库Phon表字段
        String[] PHONES_PROJECTION = new String[]{Phone.DISPLAY_NAME,
                Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID};
        // 联系人显示名称
        int PHONES_DISPLAY_NAME_INDEX = 0;
        // 电话号码
        int PHONES_NUMBER_INDEX = 1;
        ContentResolver resolver = getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX)
                        .replace(" ", "");
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber) || phoneNumber.equals("未填写"))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor
                        .getString(PHONES_DISPLAY_NAME_INDEX);

                User user = BaseApplication.getInstance().getUser();
                String mobile = user.getMobile();
                if (!mobile.equals(phoneNumber)) {
                    if (mobile_list == null)
                        mobile_list = contactName + "|" + phoneNumber;
                    else
                        mobile_list = contactName + "|" + phoneNumber + "," + mobile_list;
                }
                i++;
                if (i % 500 == 0) {
                    strings.add(mobile_list);
                }
            }
            phoneCursor.close();
        }
        if (i < 500) {
            strings.add(mobile_list);
        }
        return mobile_list;
    }

    @Override
    protected void getExras() {
        // TODO Auto-generated method stub

    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {

        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }

    }
}
