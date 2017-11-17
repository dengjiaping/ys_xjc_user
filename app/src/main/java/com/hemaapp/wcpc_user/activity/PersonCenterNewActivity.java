package com.hemaapp.wcpc_user.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.SysInitInfo;
import com.hemaapp.wcpc_user.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心
 */
public class PersonCenterNewActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.view)
    ImageView view;
    @BindView(R.id.title_btn_left)
    ImageView titleBtnLeft;
    @BindView(R.id.title_btn_right2)
    ImageView titleBtnRight2;
    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.lv_mywallet)
    RelativeLayout lvMywallet;
    @BindView(R.id.tv_couple)
    TextView tvCouple;
    @BindView(R.id.lv_mycouple)
    RelativeLayout lvMycouple;
    @BindView(R.id.lv_myorder)
    RelativeLayout lvMyorder;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.set)
    TextView set;
    private PopupWindow mWindow;
    private ViewGroup mViewGroup;
    private TextView content1;
    private TextView content2;
    private TextView ok;
    private TextView cancel;

    private SysInitInfo sysInitInfo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personcenter_new);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.reset().init();
        sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
        user = BaseApplication.getInstance().getUser();
        getNetWorker().clientGet(user.getToken(), user.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = BaseApplication.getInstance().getUser();
        initUserData();
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initUserData() {
        ImageLoader.getInstance().displayImage(user.getAvatar(), ivAvatar, BaseApplication.getInstance()
                .getOptions(R.mipmap.default_user));
        ivAvatar.setCornerRadius(100);
        tvName.setText(user.getRealname());
        if ("男".equals(user.getSex()))
            ivSex.setImageResource(R.mipmap.img_sex_boy);
        else
            ivSex.setImageResource(R.mipmap.img_sex_girl);
        tvCount.setText("乘车次数 " + (isNull(user.getTakecount()) ? "0" : user.getTakecount()));
        tvFee.setText(user.getFeeaccount()+"元");
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showProgressDialog("请稍后...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                HemaArrayParse<User> uResult = (HemaArrayParse<User>) baseResult;
                user = uResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);
                initUserData();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showTextDialog(baseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showTextDialog("抱歉，获取数据失败");
                break;
        }
    }

    @Override
    protected void findView() {
    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {

    }

    private PopupWindow mWindow1;
    private ViewGroup mViewGroup1;
    private TextView boy;
    private TextView girl;
    private TextView cancel1;

    private void showSelectPopWindow() {
        if (mWindow1 != null) {
            mWindow1.dismiss();
        }
        mWindow1 = new PopupWindow(mContext);
        mWindow1.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow1.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow1.setBackgroundDrawable(new BitmapDrawable());
        mWindow1.setFocusable(true);
        mWindow1.setAnimationStyle(R.style.PopupAnimation);
        mViewGroup1 = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.pop_sex, null);
        boy = (TextView) mViewGroup1.findViewById(R.id.textview);
        girl = (TextView) mViewGroup1.findViewById(R.id.textview_0);
        cancel1 = (TextView) mViewGroup1.findViewById(R.id.textview_2);
        mWindow1.setContentView(mViewGroup1);
        mWindow1.showAtLocation(mViewGroup1, Gravity.CENTER, 0, 0);
        boy.setText("支付宝");
        girl.setText("银行卡");
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow1.dismiss();
            }
        });

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow1.dismiss();
                Intent it = new Intent(mContext, CashAddByAlipayActivity.class);
                startActivity(it);
            }
        });

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow1.dismiss();
                Intent it = new Intent(mContext, CashAddByUnionpayActivity.class);
                startActivity(it);
            }
        });
    }

    private void toMakePhone() {
        if (mWindow != null) {
            mWindow.dismiss();
        }
        mWindow = new PopupWindow(mContext);
        mWindow.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setBackgroundDrawable(new BitmapDrawable());
        mWindow.setFocusable(true);
        mWindow.setAnimationStyle(R.style.PopupAnimation);
        mViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.pop_phone, null);
        content1 = (TextView) mViewGroup.findViewById(R.id.textview);
        content2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
        cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
        ok = (TextView) mViewGroup.findViewById(R.id.textview_2);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        content1.setText("拨打客服电话");
        content2.setText(sysInitInfo.getSys_service_phone());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
                String phone = sysInitInfo.getSys_service_phone();
                //Intent.ACTION_CALL 直接拨打电话，就是进入拨打电话界面，电话已经被拨打出去了。
                //Intent.ACTION_DIAL 是进入拨打电话界面，电话号码已经输入了，但是需要人为的按拨打电话键，才能播出电话。
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + phone));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case R.id.layout:
                getNetWorker().clientGet(user.getToken(), user.getId());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
        }
    }

    @OnClick({R.id.title_btn_left, R.id.title_btn_right2, R.id.iv_avatar, R.id.lv_mywallet, R.id.lv_mycouple, R.id.lv_myorder, R.id.tv_password, R.id.set})
    public void onViewClicked(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.title_btn_left:
                finish();
                break;
            case R.id.title_btn_right2:
                toMakePhone();
                break;
            case R.id.iv_avatar:
                it = new Intent(mContext, PersonInforActivity.class);
                startActivityForResult(it, R.id.layout);
                break;
            case R.id.lv_mywallet:
                it = new Intent(mContext, MyPurseNewActivity.class);
                startActivity(it);
                break;
            case R.id.lv_mycouple:
                it = new Intent(mContext, MyCouponListActivity.class);
                it.putExtra("keytype", "1");
                startActivity(it);
                break;
            case R.id.lv_myorder:
                it = new Intent(mContext, MListActivity.class);
                startActivity(it);
                break;
            case R.id.tv_password:
                it = new Intent(mContext, PassWord0Activity.class);
                startActivity(it);
                break;
            case R.id.set:
                it = new Intent(mContext, SetActivity.class);
                startActivity(it);
                break;
        }
    }
}
