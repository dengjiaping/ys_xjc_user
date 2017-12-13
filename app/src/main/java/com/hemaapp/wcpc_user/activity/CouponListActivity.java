package com.hemaapp.wcpc_user.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.BaseUtil;
import com.hemaapp.wcpc_user.EventBusConfig;
import com.hemaapp.wcpc_user.EventBusModel;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.RecycleUtils;
import com.hemaapp.wcpc_user.adapter.ClientAddCouponAdapter;
import com.hemaapp.wcpc_user.adapter.CouponAdapter;
import com.hemaapp.wcpc_user.model.ClientAddCoupon;
import com.hemaapp.wcpc_user.model.CouponListInfor;
import com.hemaapp.wcpc_user.model.User;
import com.hemaapp.wcpc_user.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.greenrobot.event.EventBus;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * keytype = "1"：我的优惠券
 * keytype = "2"：可用优惠券
 */
public class CouponListActivity extends BaseActivity implements PlatformActionListener {

    @BindView(R.id.title_btn_left)
    ImageView titleBtnLeft;
    @BindView(R.id.title_btn_right)
    TextView titleBtnRight;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.refreshLoadmoreLayout)
    RefreshLoadmoreLayout refreshLoadmoreLayout;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.empty)
    LinearLayout empty;
    @BindView(R.id.ev_code)
    ClearEditText evCode;
    @BindView(R.id.tv_button)
    TextView tvButton;
    private int page = 0;
    private ArrayList<CouponListInfor> infors = new ArrayList<>();
    private CouponAdapter adapter;
    private User user;
    private String keytype, coupon_id;
    private PopupWindow mWindow_exit;//分享
    private ViewGroup mViewGroup_exit;
    private OnekeyShare oks;
    private ArrayList<ClientAddCoupon> coupons = new ArrayList<>();//兑换
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_couponlist);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        user = BaseApplication.getInstance().getUser();
        adapter = new CouponAdapter(mContext, infors);
        RecycleUtils.initVerticalRecyle(rvList);
        rvList.setAdapter(adapter);
        adapter.setKeytype(keytype);
        getTripsList();
    }

    private void getTripsList() {
        getNetWorker().couponsList(user.getToken(), keytype, page);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case COUPONS_LIST:
                break;
            case GET_COUPON_BYCODE:
                showProgressDialog("请稍后...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case COUPONS_LIST:
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case GET_COUPON_BYCODE:
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
            case COUPONS_LIST:
                String page = netTask.getParams().get("page");
                HemaArrayParse<CouponListInfor> cResult = (HemaArrayParse<CouponListInfor>) baseResult;
                ArrayList<CouponListInfor> cashs = cResult.getObjects();
                if ("0".equals(page)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    infors.clear();
                    infors.addAll(cashs);

                    BaseApplication application = BaseApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (cashs.size() < sysPagesize)
                        refreshLoadmoreLayout.setLoadmoreable(false);
                    else
                        refreshLoadmoreLayout.setLoadmoreable(true);
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (cashs.size() > 0)
                        infors.addAll(cashs);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        XtomToastUtil.showShortToast(mContext, "已经到最后啦");
                    }
                }
                if (infors.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    empty.setVisibility(View.INVISIBLE);
                }
                adapter.notifyDataSetChanged();
                break;
            case SHARE_CALLBACK:
                this.page = 0;
                getTripsList();
                break;
            case GET_COUPON_BYCODE:
                HemaArrayParse<ClientAddCoupon> gResult = (HemaArrayParse<ClientAddCoupon>) baseResult;
                ClientAddCoupon couponListInfor = gResult.getObjects().get(0);
                coupons.clear();
                coupons.add(couponListInfor);
                showCouponWindow();
                this.page = 0;
                getTripsList();
                EventBus.getDefault().post(new EventBusModel(EventBusConfig.REFRESH_CUSTOMER_INFO));
                break;
            default:
                break;
        }
    }
    private PopupWindow mWindow;
    private ViewGroup mViewGroup;
    private void showCouponWindow() {
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
                R.layout.pop_couple2, null);
        RecyclerView recyclerView = (RecyclerView) mViewGroup.findViewById(R.id.rv_list);
        TextView count = (TextView) mViewGroup.findViewById(R.id.tv_count);
        TextView tv_button = (TextView) mViewGroup.findViewById(R.id.tv_button);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        String c = "恭喜您获得";
        for (ClientAddCoupon clientAddCoupon : coupons) {
            if (clientAddCoupon.getKeytype().equals("1")){
                c=c+"1张"+clientAddCoupon.getValue()+"元代金券";
                tv_button.setText("前去拼车");
            }else {
                c=c+"1张免费乘车券";
                tv_button.setText("分享激活");
            }
        }
        count.setText(c);
        ClientAddCouponAdapter adapter = new ClientAddCouponAdapter(mContext, coupons);
        RecycleUtils.initVerticalRecyle(recyclerView);
        recyclerView.setAdapter(adapter);
        tv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();

            }
        });
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case COUPONS_LIST:
                String page = netTask.getParams().get("page");
                if ("0".equals(page)) {// 刷新
                    refreshLoadmoreLayout.refreshFailed();
                    adapter.notifyDataSetChanged();
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreFailed();
                }
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
            case COUPONS_LIST:
                String page = netTask.getParams().get("page");
                if ("0".equals(page)) {// 刷新
                    refreshLoadmoreLayout.refreshFailed();
                    adapter.notifyDataSetChanged();
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        titleText.setText("我的代金券");
        titleBtnRight.setText("使用规则");

        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getTripsList();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                getTripsList();
            }
        });
        evCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0)
                    tvButton.setBackgroundResource(R.drawable.bg_coupon_button_n);
                else
                    tvButton.setBackgroundResource(R.drawable.bg_coupon_button_p);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @OnClick({R.id.title_btn_left, R.id.title_btn_right, R.id.tv_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_btn_left:
                finish();
                break;
            case R.id.title_btn_right:
                Intent it = new Intent(mContext, ShowInternetPageActivity.class);
                it.putExtra("name", "代金券使用说明");
                String sysInitInfo = BaseApplication.getInstance().getSysInitInfo().getSys_web_service();
                String path = sysInitInfo + "webview/parm/coupon_rule";
                it.putExtra("path", path);
                startActivity(it);
                break;
            case R.id.tv_button:
                String code = evCode.getText().toString();
                if (!isNull(code)) {
                    getNetWorker().getCoupon(user.getToken(), code);
                }
                break;
        }
    }

    @SuppressWarnings("deprecation")
    public void share(String coupon_id) {
        this.coupon_id = coupon_id;
        if (mWindow_exit != null) {
            mWindow_exit.dismiss();
        }
        mWindow_exit = new PopupWindow(mContext);
        mWindow_exit.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow_exit.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow_exit.setBackgroundDrawable(new BitmapDrawable());
        mWindow_exit.setFocusable(true);
        mWindow_exit.setAnimationStyle(R.style.PopupAnimation);
        mViewGroup_exit = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.pop_share, null);
        TextView wechat = (TextView) mViewGroup_exit.findViewById(R.id.wechat);
        TextView moment = (TextView) mViewGroup_exit.findViewById(R.id.moment);
        TextView qqshare = (TextView) mViewGroup_exit.findViewById(R.id.qq);
        TextView qzone = (TextView) mViewGroup_exit.findViewById(R.id.zone);
        TextView cancel = (TextView) mViewGroup_exit.findViewById(R.id.tv_cancel);
        wechat.setVisibility(View.GONE);
        qqshare.setVisibility(View.GONE);
        View all = mViewGroup_exit.findViewById(R.id.allitem);
        BaseUtil.fitPopupWindowOverStatusBar(mWindow_exit, true);
        mWindow_exit.setContentView(mViewGroup_exit);
        mWindow_exit.showAtLocation(mViewGroup_exit, Gravity.CENTER, 0, 0);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mWindow_exit.dismiss();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mWindow_exit.dismiss();
            }
        });
        qqshare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showShare(QQ.NAME);
                mWindow_exit.dismiss();
            }
        });
        wechat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showShare(Wechat.NAME);
                mWindow_exit.dismiss();
            }
        });
        moment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showShare(WechatMoments.NAME);
                mWindow_exit.dismiss();
            }
        });
        qzone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showShare(QZone.NAME);
                mWindow_exit.dismiss();
            }
        });
    }

    private void showShare(String platform) {
        String pathWX = BaseApplication.getInstance().getSysInitInfo().getSys_plugins() + "share/sdk.php?invitecode=" + user.getInvitecode() + "&keyid=0" + "&type=1";
        String imageurl = BaseUtil.initImagePath(mContext);
        String sharecontent = BaseApplication.getInstance().getSysInitInfo().getSharecontent();
        sharecontent = sharecontent.replace("\\n", "\n");
        String sharetitle = BaseApplication.getInstance().getSysInitInfo().getSharetitle();
        sharetitle = sharetitle.replace("\\n", "\n");
        if (oks == null) {
            oks = new OnekeyShare();
            oks.setTitle(sharetitle);
            oks.setTitleUrl(pathWX); // 标题的超链接
            oks.setText(sharecontent);
            oks.setImagePath(imageurl);
            oks.setUrl(pathWX);
            oks.setSiteUrl(pathWX);
            oks.setCallback(this);
        }
        oks.setPlatform(platform);
        oks.show(mContext);
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> hashMap) {
        if (arg0.getName().equals(Wechat.NAME)) {// 判断成功的平台是不是微信
            handler.sendEmptyMessage(1);
        }
        if (arg0.getName().equals(WechatMoments.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(2);
        }
        if (arg0.getName().equals(QQ.NAME)) {// 判断成功的平台是不是QQ
            handler.sendEmptyMessage(3);
        }
        if (arg0.getName().equals(QZone.NAME)) {// 判断成功的平台是不是空间
            handler.sendEmptyMessage(4);
        }
        if (arg0.getName().equals(WechatFavorite.NAME)) {// 判断成功的平台是不是微信收藏
            handler.sendEmptyMessage(5);
        }
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 6;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);

    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(7);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getNetWorker().shareCallback(user.getToken(), "3", coupon_id, "1");
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    getNetWorker().shareCallback(user.getToken(), "3", coupon_id, "2");
                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    getNetWorker().shareCallback(user.getToken(), "3", coupon_id, "3");
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    getNetWorker().shareCallback(user.getToken(), "3", coupon_id, "4");
                    Toast.makeText(getApplicationContext(), "QQ空间分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "微信收藏分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };
}
