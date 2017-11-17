package com.hemaapp.wcpc_user.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的钱包
 */
public class MyPurseNewActivity extends BaseActivity {

    @BindView(R.id.title_btn_left)
    ImageView titleBtnLeft;
    @BindView(R.id.title_btn_right)
    TextView titleBtnRight;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title)
    LinearLayout title;
    @BindView(R.id.textview_1)
    TextView textview1;
    @BindView(R.id.button)
    TextView button;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mywallet);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        user = BaseApplication.getInstance().getUser();
    }

    @Override
    protected void onResume() {
        getNetWorker().clientGet(user.getToken(), user.getId());
        super.onResume();
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
                textview1.setText((isNull(user.getFeeaccount()) ? "0" : user.getFeeaccount()));
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
        titleText.setText("我的钱包");
        titleBtnRight.setText("账户明细");
    }

    private void setListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it;
                switch (v.getId()) {
                    case R.id.textview_0: //优惠券列表
                        it = new Intent(mContext, MyCouponListActivity.class);
                        it.putExtra("keytype", "1");
                        startActivity(it);
                        break;
                    case R.id.layout: //账户明细

                        break;
                    case R.id.textview_2: //充值
                        it = new Intent(mContext, ChargeMoneyActivity.class);
                        startActivity(it);
                        break;
                    case R.id.textview_3: //提现
                        showSelectPopWindow();
                        break;
                    case R.id.textview_4: //设置支付密码
                        it = new Intent(mContext, ResetPayPasswordActivity.class);
                        startActivity(it);
                        break;
                }
            }
        });
    }

    private PopupWindow mWindow;
    private ViewGroup mViewGroup;
    private TextView boy;
    private TextView girl;
    private TextView cancel;

    private void showSelectPopWindow() {
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
                R.layout.pop_sex, null);
        boy = (TextView) mViewGroup.findViewById(R.id.textview);
        girl = (TextView) mViewGroup.findViewById(R.id.textview_0);
        cancel = (TextView) mViewGroup.findViewById(R.id.textview_2);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        boy.setText("支付宝");
        girl.setText("银行卡");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
            }
        });

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
                Intent it = new Intent(mContext, CashAddByAlipayActivity.class);
                startActivity(it);
            }
        });

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
                Intent it = new Intent(mContext, CashAddByUnionpayActivity.class);
                startActivity(it);
            }
        });
    }

    @OnClick({R.id.title_btn_left, R.id.title_btn_right, R.id.button})
    public void onViewClicked(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.title_btn_left:
                finish();
                break;
            case R.id.title_btn_right:
                it = new Intent(mContext, MyFeeAccountActivity.class);
                startActivity(it);
                break;
            case R.id.button:
                it = new Intent(mContext, ChargeMoneyActivity.class);
                startActivity(it);
                break;
        }
    }
}
