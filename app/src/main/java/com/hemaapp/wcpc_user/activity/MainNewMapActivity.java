package com.hemaapp.wcpc_user.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.canyinghao.caneffect.CanShadowDrawable;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.wcpc_user.AmapTTSController;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.BaseUtil;
import com.hemaapp.wcpc_user.CircularAnim;
import com.hemaapp.wcpc_user.EventBusConfig;
import com.hemaapp.wcpc_user.EventBusModel;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.RecycleUtils;
import com.hemaapp.wcpc_user.ToLogin;
import com.hemaapp.wcpc_user.UpGrade;
import com.hemaapp.wcpc_user.adapter.PersonCountAdapter;
import com.hemaapp.wcpc_user.adapter.PopTimeAdapter;
import com.hemaapp.wcpc_user.adapter.TogetherAdapter;
import com.hemaapp.wcpc_user.keepappalive.ScreenManager;
import com.hemaapp.wcpc_user.keepappalive.ScreenReceiverUtil;
import com.hemaapp.wcpc_user.model.Area;
import com.hemaapp.wcpc_user.model.Client;
import com.hemaapp.wcpc_user.model.CouponListInfor;
import com.hemaapp.wcpc_user.model.CurrentTripsInfor;
import com.hemaapp.wcpc_user.model.DistrictInfor;
import com.hemaapp.wcpc_user.model.DriverPosition;
import com.hemaapp.wcpc_user.model.ID;
import com.hemaapp.wcpc_user.model.Often;
import com.hemaapp.wcpc_user.model.PersonCountInfor;
import com.hemaapp.wcpc_user.model.SysInitInfo;
import com.hemaapp.wcpc_user.model.User;
import com.hemaapp.wcpc_user.newgetui.GeTuiIntentService;
import com.hemaapp.wcpc_user.newgetui.PushUtils;
import com.hemaapp.wcpc_user.util.AndroidBug5497Workaround;
import com.hemaapp.wcpc_user.util.HiddenAnimUtils;
import com.hemaapp.wcpc_user.view.wheelview.OnWheelScrollListener;
import com.hemaapp.wcpc_user.view.wheelview.WheelView;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.PushService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.greenrobot.event.EventBus;
import xtom.frame.XtomActivityManager;
import xtom.frame.util.XtomDeviceUuidFactory;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomTimeUtil;
import xtom.frame.util.XtomToastUtil;

/**
 * 首页
 */
public class MainNewMapActivity extends BaseActivity implements
        GeocodeSearch.OnGeocodeSearchListener, AMap.OnCameraChangeListener, AMap.OnMyLocationChangeListener, AMap.InfoWindowAdapter, INaviInfoCallback,
        PlatformActionListener {
    @BindView(R.id.title_btn_left)
    ImageView titleBtnLeft;
    @BindView(R.id.title_btn_right_image)
    ImageView titleBtnRightImage;
    @BindView(R.id.title_point)
    ImageView titlePoint;
    @BindView(R.id.bmapView)
    MapView mapView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.lv_search)
    LinearLayout lvSearch;
    @BindView(R.id.tv_now)
    TextView tvNow;
    @BindView(R.id.tv_appointment)
    TextView tvAppointment;
    @BindView(R.id.tv_often)
    TextView tvOften;
    @BindView(R.id.tv_start_city)
    TextView tvStartCity;
    @BindView(R.id.iv_change)
    ImageView ivChange;
    @BindView(R.id.tv_end_city)
    TextView tvEndCity;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.lv_bang)
    LinearLayout lvBang;
    @BindView(R.id.lv_one_next)
    LinearLayout lvOneNext;
    @BindView(R.id.lv_send0)
    LinearLayout lvSend0;//下单第一步
    @BindView(R.id.tv_sendtwo_cancel)
    TextView tvSendtwoCancel;
    @BindView(R.id.ev_send_bang_name)
    EditText evSendBangName;
    @BindView(R.id.ev_send_bang_tel)
    EditText evSendBangTel;
    @BindView(R.id.lv_bang_infor)
    LinearLayout lvBangInfor;
    @BindView(R.id.tv_send_pin)
    TextView tvSendPin;
    @BindView(R.id.tv_send_bao)
    TextView tvSendBao;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.tv_send_count)
    TextView tvSendCount;
    @BindView(R.id.lv_send_otherinfor)
    LinearLayout lvSendOtherinfor;
    @BindView(R.id.tv_send_coupon)
    TextView tvSendCoupon;
    @BindView(R.id.tv_send_content)
    TextView tvSendContent;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_send_feeinfor)
    TextView tvSendFeeinfor;
    @BindView(R.id.tv_send_button)
    TextView tvSendButton;
    @BindView(R.id.lv_send1)
    LinearLayout lvSend1;//发单第二步
//    @BindView(R.id.tv_cur_tip)
//    TextView tvCurTip;

    @BindView(R.id.iv_daohang)
    ImageView ivDaohang;
    @BindView(R.id.fv_current_top)
    FrameLayout fvCurrentTop;
    @BindView(R.id.iv_cur_avatar)
    RoundedImageView ivCurAvatar;
    @BindView(R.id.tv_cur_start)
    TextView tvCurStart;
    @BindView(R.id.tv_cur_end)
    TextView tvCurEnd;
    @BindView(R.id.lv_cur_rout)
    LinearLayout lvCurRout;
    @BindView(R.id.tv_cur_name)
    TextView tvCurName;
    @BindView(R.id.iv_cur_sex)
    ImageView ivCurSex;
    @BindView(R.id.tv_cur_distance)
    TextView tvCurDistance;
    @BindView(R.id.tv_cur_car)
    TextView tvCurCar;
    @BindView(R.id.lv_cur_driver)
    LinearLayout lvCurDriver;
    @BindView(R.id.iv_cur_tel)
    ImageView ivCurTel;
    @BindView(R.id.tv_cur_bang_name)
    TextView tvCurBangName;
    @BindView(R.id.tv_cur_bang_tel)
    TextView tvCurBangTel;
    @BindView(R.id.lv_cur_bang)
    LinearLayout lvCurBang;
    @BindView(R.id.tv_cur_num)
    TextView tvCurNum;
    @BindView(R.id.tv_cur_time)
    TextView tvCurTime;
    @BindView(R.id.tv_cur_price)
    TextView tvCurPrice;
    @BindView(R.id.tv_cur_couple)
    TextView tvCurCouple;
    @BindView(R.id.tv_cur_together)
    TextView tvCurTogether;
    @BindView(R.id.rv_cur_list)
    RecyclerView rvCurList;
    @BindView(R.id.lv_cur_together)
    LinearLayout lvCurTogether;
    @BindView(R.id.tv_cur_button0)
    TextView tvCurButton0;
    @BindView(R.id.tv_cur_button1)
    TextView tvCurButton1;
    @BindView(R.id.lv_current_bottom)
    LinearLayout lvCurrentBottom;
    @BindView(R.id.lv_current0)
    LinearLayout lvCurrent0;
    @BindView(R.id.iv_current_top)
    ImageView ivCurrentTop;
    @BindView(R.id.tv_cur_price2)
    TextView tvCurPrice2;
    @BindView(R.id.tv_cur_couple2)
    TextView tvCurCouple2;
    @BindView(R.id.lv_price2)
    LinearLayout lvPrice2;
    @BindView(R.id.lv_cur_price1)
    LinearLayout lvCurPrice1;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.lv_main)
    FrameLayout lvMain;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;
    @BindView(R.id.lv_send0_bottom)
    LinearLayout lvSend0Bottom;
    @BindView(R.id.lv_send1_bottom)
    LinearLayout lvSend1Bottom;
    @BindView(R.id.lv_cur_tip)
    LinearLayout lvCurTip;

    private User user;
    private int msgcount;
    private long time;// 用于判断二次点击返回键的时间间隔
    private static MainNewMapActivity activity;
    public ArrayList<DistrictInfor> allDistricts = new ArrayList<>();

    public static MainNewMapActivity getInstance() {
        return activity;
    }

    private PopupWindow mWindow;
    private ViewGroup mViewGroup;
    private String phone;
    private UpGrade upGrade;
    private AMap aMap;
    Marker screenMarker = null;
    private Marker sendStartMarker = null, sendEndMarker = null, driverMarker = null;
    private ArrayList<Polygon> polygons = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private boolean inArea = false, isFirstLoc = true, hasCircle = false, isSend2 = false, moveDriver = true, isSearch = false;
    private GeocodeSearch geocoderSearch;
    private LatLng latlng, loclatlng, driver_latlng;
    private ArrayList<Area> areas = new ArrayList<>();//开通区域
    private String move_lat, move_lng, myAddress, lng, lat, loc_lng, loc_lat, selectAddress = "0";
    MyLocationStyle myLocationStyle;
    private CurrentTripsInfor infor;//当前行程信息
    AlphaAnimation appearAnimation, disappearAnimation;
    private DistrictInfor startCity, endCity, myCity;
    private String start_address = "", end_address, start_lng, start_lat, end_lat, end_lng, begintime, coupon_vavle, coupon_id, bangFlag, pinFlag = "1",
            timetype = "1";
    private String begin;//和可拼车时间去比较的出发时间
    private int count = 1, coupon = 0;
    private float totleFee = 0, price = 0, addstart = 0, addend = 0;
    private PopupWindow mWindow_exit;
    private ViewGroup mViewGroup_exit;
    private ArrayList<PersonCountInfor> counts = new ArrayList<>();//人数
    private int timeflag = 0;
    private PopupWindow timePop;
    private ViewGroup timeViewGroup;
    private WheelView dayListView;
    private WheelView timeListView;
    private WheelView secondListView;
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<String> days1 = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private ArrayList<String> seconds = new ArrayList<>();
    private PopTimeAdapter time_adapter;
    private int bottomHeight;
    private TogetherAdapter togetherAdapter;
    private ArrayList<Client> clients = new ArrayList<>();
    private AmapTTSController amapTTSController;
    View infoWindow;
    Often often;//常用行程
    private boolean isDrawer = false;//侧滑
    private OnekeyShare oks;
    private ScreenManager manager;
    private ScreenReceiverUtil screenReceiverUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        setContentView(R.layout.activity_main_newmap2);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        AndroidBug5497Workaround.assistActivity(this);
        ShareSDK.initSDK(this);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        EventBus.getDefault().register(this);

        upGrade = new UpGrade(mContext) {
            @Override
            public void NoNeedUpdate() {
            }
        };
        user = BaseApplication.getInstance().getUser();
        phone = BaseApplication.getInstance().getSysInitInfo().getSys_service_phone();
        if (user == null) {
            titlePoint.setVisibility(View.INVISIBLE);
        } else {
            getNetWorker().noticeUnread(user.getToken(), "2", "1");
        }
        amapTTSController = AmapTTSController.getInstance(mContext);
        amapTTSController.init();

        getNetWorker().cityList("0");//获取已开通城市
        appearAnimation = new AlphaAnimation(0, 1);
        appearAnimation.setDuration(500);
        disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(500);
        titleBtnLeft.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!BaseUtil.isOPen(mContext)) {
                    GpsTip();
                }
//                if (!NotificationsUtils.isNotificationEnabled(mContext)){
//                   // NotifiTip();
//                }
            }
        }, 800);
        init();
        for (int i = 0; i < 4; i++) {//
            counts.add(i, new PersonCountInfor(String.valueOf(i + 1), false));
        }
        handler();
        findCenter();
        setCenter();
        CanShadowDrawable.Builder.on(lvSend0Bottom)//发布加阴影效果
                .radius(dip2px(mContext, 2))
                .shadowColor(Color.parseColor("#d9d9d9"))
                .shadowRange(dip2px(mContext, 5))
                .offsetTop(dip2px(mContext, 5))
                .offsetBottom(dip2px(mContext, 5))
                .offsetLeft(dip2px(mContext, 5))
                .offsetRight(dip2px(mContext, 5))
                .create();
        CanShadowDrawable.Builder.on(lvSend1Bottom)//发布加阴影效果
                .radius(dip2px(mContext, 2))
                .shadowColor(Color.parseColor("#d9d9d9"))
                .shadowRange(dip2px(mContext, 5))
                .offsetTop(dip2px(mContext, 5))
                .offsetBottom(dip2px(mContext, 5))
                .offsetLeft(dip2px(mContext, 5))
                .offsetRight(dip2px(mContext, 5))
                .create();
        initPXActivity();
    }

    /**
     * 初始化1px像素activity
     */
    private void initPXActivity() {
        screenReceiverUtil = new ScreenReceiverUtil(this);
        manager = ScreenManager.getScreenManager(this);
        screenReceiverUtil.setScreenListener(new ScreenReceiverUtil.ScreenListener() {
            @Override
            public void screenOn() {
                manager.finishActivity();
            }

            @Override
            public void screenOff() {
                manager.startSingleActivity();
            }

            @Override
            public void onUserPresent() {
                //锁屏按钮操作
            }
        });
    }

    public void onEventMainThread(EventBusModel event) {
        switch (event.getType()) {
            case NEW_MESSAGE:
                getNetWorker().noticeUnread(user.getToken(), "2", "1");
                break;
            case CLIENT_ID:
                saveDevice(event.getContent());
                break;
            case REFRESH_CUSTOMER_INFO:
                getNetWorker().clientGet(user.getToken(), user.getId());
                break;
            case REFRESH_BLOG_LIST:
                getNetWorker().currentTrips(user.getToken());
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (infor == null) {
            lvSend0.setVisibility(View.VISIBLE);
            lvSend1.setVisibility(View.GONE);
            often = (Often) intent.getSerializableExtra("often");
            getNetWorker().cityList(often.getStartcity_id(), often.getEndcity_id());
            getNetWorker().canTrips(user.getToken());
        } else {
            XtomToastUtil.showShortToast(mContext, "当前行程未结束！");
        }
        super.onNewIntent(intent);
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    progressBar2.setVisibility(View.GONE);
                    addMarkerInScreenCenter();
                    aMap.setOnCameraChangeListener(activity);
                    aMap.setOnMyLocationChangeListener(activity);
                    geocoderSearch = new GeocodeSearch(activity);
                    geocoderSearch.setOnGeocodeSearchListener(activity);

                }
            });
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.getUiSettings().setRotateGesturesEnabled(false);
            aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            setupLocationStyle();
        }
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(3000);
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.loc_my));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(180, 3, 145, 255));
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(0x000000);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    /**
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        screenMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.purple_pin)));
        //设置Marker在屏幕上,不跟随地图移动
        screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
        if (infor != null) {
            screenMarker.setVisible(false);
        }
    }

    /**
     * 地图画圈
     */
    private void setUpMap() {
        for (Polygon p : polygons) {
            p.remove();
        }
        mapView.invalidate();//刷新地图
        polygons.clear();
        prices.clear();
        for (Area area : areas) {
            PolygonOptions pOption = new PolygonOptions();
            String[] str = area.getLnglat().split(";");
            for (int i = 0; i < str.length; i++) {
                String lat = str[i].split(",")[1];
                String lng = str[i].split(",")[0];
                pOption.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            }
            Polygon polygon = aMap.addPolygon(pOption.strokeWidth(4)
                    .strokeColor(Color.argb(50, 1, 1, 1))
                    .fillColor(0x70e5e5e5));
            polygons.add(polygon);
            prices.add(area.getAddprice());
            if (isNull(move_lat)) {
                move_lat = str[0].split(",")[1];
                move_lng = str[0].split(",")[0];
            }
        }
        hasCircle = true;//已经画圈
        selectAddress = "1";//选择出发地
        tvStart.setBackgroundColor(0x40e5e5e5);
        tvEnd.setBackgroundColor(0xffffffff);
        if (!isNull(myCity.getCenter_lnglat1())) {
            String locCity = XtomSharedPreferencesUtil.get(mContext, "city");
            if (locCity.equals(startCity.getName())) {
                move_lat = loc_lat;
                move_lng = loc_lng;
                CameraPosition cameraPosition = new CameraPosition(loclatlng, 0, 0, 0);//如果当前位置没有变化，是不会走onCameraChangeFinish方法的,所以强行调一下
                onCameraChangeFinish(cameraPosition);
            } else {
                move_lat = myCity.getCenter_lnglat1().split(",")[1];
                move_lng = myCity.getCenter_lnglat1().split(",")[0];
            }
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(move_lat),
                    Double.parseDouble(move_lng)), 15));
        }
    }

    @Override
    protected boolean onKeyBack() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (lvSend1.getVisibility() == View.VISIBLE) {
            isSend2 = false;
            lvSearch.setVisibility(View.VISIBLE);
            sendEndMarker.setVisible(false);
            sendStartMarker.setVisible(false);
            screenMarker.setVisible(true);
            lvSend1.setVisibility(View.GONE);
            lvSend0.startAnimation(appearAnimation);
            lvSend0.setVisibility(View.VISIBLE);
        } else {
            if ((System.currentTimeMillis() - time) >= 2000) {
                XtomToastUtil.showShortToast(mContext, "再按一次返回键退出程序");
                time = System.currentTimeMillis();
            } else {
                XtomActivityManager.finishAll();
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mapView.onDestroy();
        amapTTSController.destroy();
        realseTimeTask();
        screenReceiverUtil.unRegisterScreenListenr();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = BaseApplication.getInstance().getUser();
        checkPermission();
        upGrade.check();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    private void realseTimeTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CURRENT_TRIPS:
                showProgressDialog("请稍后...");
                break;
            case NOTICE_UNREAD:
                break;
            case TRIPS_ADD:
                showProgressDialog("请稍后...");
                break;
            case ORDER_OPERATE:
                showProgressDialog("请稍后...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CURRENT_TRIPS:
            case CAN_TRIPS:
            case TRIPS_ADD:
            case ORDER_OPERATE:
                cancelProgressDialog();
                break;
            case NOTICE_UNREAD:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_UNREAD:
                HemaArrayParse<ID> cResult = (HemaArrayParse<ID>) baseResult;
                msgcount = Integer.parseInt(isNull(cResult.getObjects().get(0).getCount()) ? "0" : cResult.getObjects().get(0).getCount());
                if (msgcount == 0)
                    titlePoint.setVisibility(View.INVISIBLE);
                else
                    titlePoint.setVisibility(View.VISIBLE);
                break;
            case CAN_TRIPS:
                cancelProgressDialog();
                HemaArrayParse<ID> sResult = (HemaArrayParse<ID>) baseResult;
                String keytype = sResult.getObjects().get(0).getKeytype();
                if ("1".equals(keytype)) {
                    getNetWorker().couponsList(user.getToken(), "2", 0);
                } else if ("2".equals(keytype)) {
                    CanNotTip();
                } else {
                    String start = BaseUtil.TransTimeHour(user.getOrder_start(), "HH:mm");
                    String end = BaseUtil.TransTimeHour(user.getOrder_end(), "HH:mm");
                    if (isNull(start)) {
                        start = "5:00";
                        end = "20:30";
                    }
                    lvSend0.setVisibility(View.GONE);
                    TimeTip(start, end);
                }
                break;
            case CITY_LIST:
                String paramid = hemaNetTask.getParams().get("paramid");
                HemaArrayParse<DistrictInfor> CResult = (HemaArrayParse<DistrictInfor>) baseResult;
                if (isNull(paramid)) {
                    allDistricts = CResult.getObjects();
                    String locCity = XtomSharedPreferencesUtil.get(mContext, "city");
                    for (DistrictInfor infor : allDistricts) {
                        if (infor.getName().equals(locCity)) {
                            startCity = infor;
                            break;
                        }
                    }
                    if (startCity != null) {
                        tvStartCity.setText(startCity.getName());
                    }
                } else {//常用行程
                    endCity = CResult.getObjects().get(0);
                    myCity = endCity;
                    startCity = new DistrictInfor(often.getStartcity(), often.getStartcity_id());
                    start_lat = often.getLat_start();
                    start_lng = often.getLng_start();
                    end_lng = often.getLng_end();
                    end_lat = often.getLat_end();
                    start_address = often.getStartaddress();
                    end_address = often.getEndaddress();
                    tvStart.setText(start_address);
                    tvEnd.setText(end_address);
                    tvStartCity.setText(startCity.getName());
                    tvEndCity.setText(endCity.getName());
                    //如果之前画过圈，清一下
                    hasCircle = false;
                    price = Float.parseFloat(myCity.getPrice());
                    areas.clear();
                    areas.addAll(myCity.getAreas());
                    setOftenMap();
                }
                break;
            case CLIENT_GET:
                HemaArrayParse<User> uResult = (HemaArrayParse<User>) baseResult;
                user = uResult.getObjects().get(0);
                BaseApplication.getInstance().setUser(user);
                setCenter();
                break;
            case CURRENT_TRIPS://当前行程
                HemaArrayParse<CurrentTripsInfor> llResult = (HemaArrayParse<CurrentTripsInfor>) baseResult;
                if (llResult.getObjects() != null && llResult.getObjects().size() > 0) {
                    infor = llResult.getObjects().get(0);
                    togetherAdapter = new TogetherAdapter(mContext, clients, navView);
                    RecycleUtils.initHorizontalRecyle(rvCurList);
                    rvCurList.setAdapter(togetherAdapter);
                } else {
                    infor = null;
                }
                if (infor == null) {
                    isSearch = false;
                    myLocationStyle.showMyLocation(true);
                    lvCurTip.setVisibility(View.GONE);
                    lvCurrent0.setVisibility(View.GONE);
                    lvSend0.startAnimation(appearAnimation);
                    lvSend0.setVisibility(View.VISIBLE);
                    lvSearch.setVisibility(View.VISIBLE);
                    if (driverMarker != null) {
                        driverMarker.setVisible(false);
                    }
                    if (screenMarker != null) {
                        screenMarker.setVisible(true);
                    }
                    if (sendEndMarker != null) {
                        sendEndMarker.setVisible(false);
                    }
                    realseTimeTask();
                    if (sendStartMarker != null) {
                        sendStartMarker.setVisible(false);
                        sendStartMarker.hideInfoWindow();
                        aMap.setInfoWindowAdapter(null);
                    }
                    getNetWorker().canTrips(user.getToken());

                    endCity = null;
                    start_address = "";
                    end_address = "";
                    tvEndCity.setText("");
                    tvStart.setText("");
                    tvEnd.setText("");
                    end_lat = "";
                    end_lng = "";
                    start_lng = "";
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(loc_lat),
                            Double.parseDouble(loc_lng)), 15));
                } else {
                    myLocationStyle.showMyLocation(true);
                    isSend2 = false;
                    if (driverMarker != null) {
                        driverMarker.setVisible(false);
                    }
                    if (sendStartMarker != null)
                        sendStartMarker.setVisible(false);
                    if (sendEndMarker != null)
                        sendEndMarker.setVisible(false);
                    if (screenMarker != null)
                        screenMarker.setVisible(false);
                    lvSearch.setVisibility(View.GONE);
                    lvSend0.setVisibility(View.GONE);
                    lvSend1.setVisibility(View.GONE);
                    lvCurrent0.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lvCurrentBottom.setLayoutParams(layoutParams);
                    lvCurrentBottom.setVisibility(View.VISIBLE);
                    setData();
                    setMarker();
                    bottomHeight = lvCurrentBottom.getLayoutParams().height;
                    log_e("bottomHeight=====" + bottomHeight);

                }
                break;
            case COUPONS_LIST:
                HemaArrayParse<CouponListInfor> couResult = (HemaArrayParse<CouponListInfor>) baseResult;
                ArrayList<CouponListInfor> cs = couResult.getObjects();
                if (cs != null && cs.size() > 0) {
                    coupon_id = cs.get(0).getId();
                    coupon_vavle = cs.get(0).getValue();
                    tvSendCoupon.setText(coupon_vavle + "元");
                    coupon = Integer.parseInt(coupon_vavle);
                }
                break;
            case TRIPS_ADD:
                EventBus.getDefault().post(new EventBusModel(EventBusConfig.REFRESH_CUSTOMER_INFO));
                showTextDialog("发布成功");
                tvSearch.setText("");
                //如果之前画过圈，清一下
                hasCircle = false;
                for (Polygon p : polygons) {
                    p.remove();
                }
                mapView.invalidate();//刷新地图
                polygons.clear();
                prices.clear();
                getNetWorker().currentTrips(user.getToken());
                break;
            case ORDER_OPERATE:
                getNetWorker().currentTrips(user.getToken());
                break;
            case DRIVER_POSITION_GET:
                HemaArrayParse<DriverPosition> DResult = (HemaArrayParse<DriverPosition>) baseResult;
                DriverPosition position = DResult.getObjects().get(0);
                driver_latlng = new LatLng(Double.parseDouble(position.getLat()), Double.parseDouble(position.getLng()));
                if (driverMarker == null) {
                    driverMarker = aMap.addMarker(new MarkerOptions()
                            .position(driver_latlng)
                            .icon(BitmapDescriptorFactory
                                    .fromBitmap(BitmapFactory.
                                            decodeResource(getResources(), R.mipmap.img_marker_car))));
                }
                driverMarker.setPosition(driver_latlng);
                if (infor.getStatus().equals("3") && infor.getIs_helpcall().equals("1") && moveDriver) {//帮人叫车行驶中，移动到车的位置
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driver_latlng, 15));
                    moveDriver = false;
                }
                if (infor.getStatus().equals("1")) {
                    float distance = AMapUtils.calculateLineDistance(loclatlng, driver_latlng);
                    String dis = "0";
                    if (distance > 1000) {
                        dis = BaseUtil.divide(distance + "", "1000", 2) + "km";
                    } else {
                        dis = BaseUtil.multiply(distance + "", "100");
                        dis = BaseUtil.divide(dis, "100", 1) + "m";
                    }
                    tvCurDistance.setText("距您" + dis);
                }
                break;
        }
    }

    private void setOftenMap() {
        for (Polygon p : polygons) {
            p.remove();
        }
        mapView.invalidate();//刷新地图
        polygons.clear();
        prices.clear();
        for (Area area : areas) {
            PolygonOptions pOption = new PolygonOptions();
            String[] str = area.getLnglat().split(";");
            for (int i = 0; i < str.length; i++) {
                String lat = str[i].split(",")[1];
                String lng = str[i].split(",")[0];
                pOption.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
            }
            Polygon polygon = aMap.addPolygon(pOption.strokeWidth(4)
                    .strokeColor(Color.argb(50, 1, 1, 1))
                    .fillColor(0x70e5e5e5));
            polygons.add(polygon);
            prices.add(area.getAddprice());
            if (isNull(move_lat)) {
                move_lat = str[0].split(",")[1];
                move_lng = str[0].split(",")[0];
            }
        }
        hasCircle = true;//已经画圈
        for (int i = 0; i < polygons.size(); i++) {
            if (polygons.get(i).contains(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lng))) && startCity.getCity_id().equals(areas.get(i).getCity_id())) {//选择地点是出发城市
                addstart = Float.parseFloat(prices.get(i));
            }
            if (polygons.get(i).contains(new LatLng(Double.parseDouble(end_lat), Double.parseDouble(end_lng))) && endCity.getCity_id().equals(areas.get(i).getCity_id())) {//选择地点是到达城市
                addend = Float.parseFloat(prices.get(i));
            }
        }
        tvStart.setBackgroundColor(0xffffffff);
        tvEnd.setBackgroundColor(0x40e5e5e5);
        selectAddress = "1";//选择出发地
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(start_lat),
                Double.parseDouble(start_lng)), 15));
        selectAddress = "2";//选择出发地
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(end_lat),
                Double.parseDouble(end_lng)), 15));
    }

    private void setData() {
        start_lng = infor.getLng_start();
        start_lat = infor.getLat_start();
        end_lng = infor.getLng_end();
        end_lat = infor.getLat_end();
        if (infor.getStatus().equals("0")) {//待派单
            ivDaohang.setVisibility(View.VISIBLE);
            lvCurRout.setVisibility(View.VISIBLE);
            lvCurDriver.setVisibility(View.GONE);
            ivCurAvatar.setVisibility(View.GONE);
            tvCurDistance.setVisibility(View.GONE);
            ivCurTel.setImageResource(R.mipmap.img_order_kefu);
            lvCurTogether.setVisibility(View.INVISIBLE);
            tvCurButton0.setVisibility(View.VISIBLE);
            tvCurButton1.setVisibility(View.GONE);
            tvCurButton0.setTextColor(0xff5e5e5e);
            tvCurButton0.setBackgroundResource(R.drawable.bg_operate);
            tvCurButton0.setText("取消订单");
            tvCurStart.setText(infor.getStartaddress());
            tvCurEnd.setText(infor.getEndaddress());
            tvCurNum.setText("我的乘车人数:" + infor.getNumbers() + "人");
            tvCurTime.setText("出发时间:" + XtomTimeUtil.TransTime(infor.getBegintime(), "MM-dd HH:mm"));
            tvCurPrice.setText(infor.getTotal_fee() + "元");
            tvCurPrice2.setText(infor.getTotal_fee() + "元");
            if (isNull(infor.getCoupon_fee()) || infor.getCoupon_fee().equals("0.00")) {
                tvCurCouple.setVisibility(View.GONE);
                tvCurCouple2.setVisibility(View.GONE);
            } else {
                tvCurCouple.setText("(代金券抵扣" + infor.getCoupon_fee() + "元)");
                tvCurCouple2.setText("(代金券抵扣" + infor.getCoupon_fee() + "元)");
            }
            if (infor.getIs_helpcall().equals("1")) {//帮人叫车
                lvCurBang.setVisibility(View.VISIBLE);
                tvCurBangName.setText(infor.getHelpcallname());
                tvCurBangTel.setText(infor.getHelpcallmobile());
            } else {
                lvCurBang.setVisibility(View.GONE);

            }
            if (infor.getTimetype().equals("1")) {//预约
                tvCurTime.setVisibility(View.VISIBLE);
                tvCurNum.setVisibility(View.VISIBLE);
            } else {//实时
                tvCurTime.setVisibility(View.GONE);
                tvCurNum.setVisibility(View.GONE);
            }
        } else {
            if (infor.getStatus().equals("1")) {//待上车
                ivDaohang.setVisibility(View.VISIBLE);
                tvCurButton0.setVisibility(View.VISIBLE);
                tvCurButton1.setVisibility(View.VISIBLE);
                tvCurDistance.setVisibility(View.VISIBLE);
                tvCurButton0.setText("取消订单");
                tvCurButton1.setText("确认上车");
                tvCurButton0.setTextColor(0xff5e5e5e);
                tvCurButton1.setTextColor(0xffffffff);
                tvCurTime.setVisibility(View.VISIBLE);
                tvCurTime.setText("出发时间:" + XtomTimeUtil.TransTime(infor.getBegintime(), "MM-dd HH:mm"));
                tvCurButton0.setBackgroundResource(R.drawable.bg_operate);
                tvCurButton1.setBackgroundResource(R.drawable.bt_qiangdan);
            } else if (infor.getStatus().equals("3")) {//待送达
                ivDaohang.setVisibility(View.VISIBLE);
                tvCurTime.setVisibility(View.GONE);
                tvCurButton0.setVisibility(View.GONE);
                tvCurButton1.setVisibility(View.VISIBLE);
                tvCurDistance.setVisibility(View.GONE);
                tvCurButton1.setText("到达目的地");
                tvCurButton1.setTextColor(0xffffffff);
                tvCurButton1.setBackgroundResource(R.drawable.bt_qiangdan);
            } else if (infor.getStatus().equals("5")) {//待支付
                ivDaohang.setVisibility(View.GONE);
                tvCurTime.setVisibility(View.GONE);
                tvCurButton0.setVisibility(View.VISIBLE);
                tvCurButton1.setVisibility(View.VISIBLE);
                tvCurDistance.setVisibility(View.GONE);
                tvCurButton0.setText("到达目的地");
                tvCurButton1.setText("去支付");
                tvCurButton0.setTextColor(0xffff9900);
                tvCurButton1.setTextColor(0xffffffff);
                tvCurButton1.setBackgroundResource(R.drawable.bt_qiangdan);
                tvCurButton0.setBackgroundColor(0xffffffff);
            }
            if (infor.getIs_helpcall().equals("1")) {//帮人叫车
                lvCurBang.setVisibility(View.VISIBLE);
                tvCurBangName.setText(infor.getHelpcallname());
                tvCurBangTel.setText(infor.getHelpcallmobile());
            } else {
                lvCurBang.setVisibility(View.GONE);
            }
            ivCurAvatar.setVisibility(View.VISIBLE);
            lvCurTip.setVisibility(View.GONE);
            lvCurRout.setVisibility(View.GONE);
            lvCurDriver.setVisibility(View.VISIBLE);
            ivCurTel.setImageResource(R.mipmap.img_order_tel);
            lvCurTogether.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(infor.getDriver_avatar(), ivCurAvatar, BaseApplication.getInstance()
                    .getOptions(R.mipmap.default_driver));
            ivCurAvatar.setCornerRadius(100);
            tvCurName.setText(infor.getRealname());
            tvCurCar.setText(infor.getCarbrand() + " " + infor.getCarnumber());
            tvCurNum.setText("我的乘车人数:" + infor.getNumbers() + "人");
            if (isNull(infor.getCoupon_fee()) || infor.getCoupon_fee().equals("0.00")) {
                tvCurCouple.setVisibility(View.GONE);
                tvCurCouple2.setVisibility(View.GONE);
            } else {
                tvCurCouple.setText("(代金券抵扣" + infor.getCoupon_fee() + "元)");
                tvCurCouple2.setText("(代金券抵扣" + infor.getCoupon_fee() + "元)");
            }
            tvCurPrice.setText(infor.getTotal_fee() + "元");
            tvCurPrice2.setText(infor.getTotal_fee() + "元");
            clients.clear();
            clients.addAll(infor.getClients());
            if (clients.size() == 0)
                tvCurTogether.setVisibility(View.GONE);
            else
                tvCurTogether.setVisibility(View.VISIBLE);
            togetherAdapter.notifyDataSetChanged();
        }
        if ("男".equals(infor.getDriver_sex()))
            ivCurSex.setImageResource(R.mipmap.img_sex_boy);
        else
            ivCurSex.setImageResource(R.mipmap.img_sex_girl);
        if (infor.getTimetype().equals("1")) {//预约

            tvCurCouple.setVisibility(View.VISIBLE);
            tvCurPrice.setVisibility(View.VISIBLE);
            lvPrice2.setVisibility(View.GONE);
            lvCurPrice1.setVisibility(View.VISIBLE);
        } else {//实时
            if (infor.getIs_helpcall().equals("1")) {
                tvCurCouple.setVisibility(View.VISIBLE);
                tvCurPrice.setVisibility(View.VISIBLE);
                lvPrice2.setVisibility(View.GONE);
                lvCurPrice1.setVisibility(View.VISIBLE);
            } else {
                lvPrice2.setVisibility(View.VISIBLE);
                lvCurPrice1.setVisibility(View.GONE);
            }
        }
        if (isNull(infor.getCoupon_fee()) || infor.getCoupon_fee().equals("0.00")) {
            tvCurCouple.setVisibility(View.GONE);
            tvCurCouple2.setVisibility(View.GONE);
        }
    }

    private void setMarker() {
        realseTimeTask();
        lvCurTip.setVisibility(View.GONE);
        if (screenMarker != null)
            screenMarker.setVisible(false);
        if (sendEndMarker != null)
            sendEndMarker.setVisible(false);
        if (sendStartMarker != null) {
            sendStartMarker.setVisible(false);
            sendStartMarker.hideInfoWindow();
            aMap.setInfoWindowAdapter(null);
        }
        if (driverMarker != null)
            driverMarker.setVisible(false);
        if (infor.getStatus().equals("0")) {//待派单
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(loc_lat),
                    Double.parseDouble(loc_lng)), 15));
            if (infor.getTimetype().equals("1")) {//预约,显示自己位置和出发点
                lvCurTip.setVisibility(View.VISIBLE);
                sendStartMarker = aMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lng)))
                        .title("出发地")
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(BitmapFactory.
                                        decodeResource(getResources(), R.mipmap.marker_send))));
                sendStartMarker.setVisible(true);
            } else {//实时
                //倒计时
                myLocationStyle.showMyLocation(false);
                aMap.setInfoWindowAdapter(this);
                sendStartMarker = aMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lng)))
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(BitmapFactory.
                                        decodeResource(getResources(), R.mipmap.marker_send))));
                sendStartMarker.setVisible(true);
                sendStartMarker.showInfoWindow();
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(start_lat),
                        Double.parseDouble(start_lng)), 15));
                long tt = BaseUtil.time(infor.getRegdate(), infor.getCurrent_time());
                int min = (int) (tt / (60 * 1000));
                int sec = (int) (tt / 1000 - (min * 60));
                log_e("min========" + min);
                log_e("sec========" + sec);
                setOriginTime(min, sec, 0);
                startTimeTask();
            }
        } else if (infor.getStatus().equals("1")) {//待上车 ，显示自己位置+出发点+车
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(loc_lat),
                    Double.parseDouble(loc_lng)), 15));
            if (sendStartMarker == null) {
                sendStartMarker = aMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lng)))
                        .title("出发地")
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(BitmapFactory.
                                        decodeResource(getResources(), R.mipmap.marker_send))));
            }
            sendStartMarker.setVisible(true);
        } else if (infor.getStatus().equals("3")) {//待送达。显示自己+目的地（帮人叫车要显示车）
            if (infor.getIs_helpcall().equals("0"))
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(loc_lat),
                        Double.parseDouble(loc_lng)), 15));
            if (sendEndMarker == null) {
                sendEndMarker = aMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(end_lat), Double.parseDouble(end_lng)))
                        .title("目的地")
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(BitmapFactory.
                                        decodeResource(getResources(), R.mipmap.marker_send))));
            }
            sendEndMarker.setVisible(true);
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case TRIPS_ADD:
                showTextDialog("发布失败");
                break;
            case CAN_TRIPS:
                showTextDialog("检查失败，请稍后重试");
                break;
            case ORDER_OPERATE:
                showTextDialog("抱歉，操作失败");
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case TRIPS_ADD:
            case CAN_TRIPS:
            case ORDER_OPERATE:
                showTextDialog(baseResult.getMsg());
                break;
        }
    }


    @Override
    protected void findView() {
        infoWindow = getLayoutInflater().inflate(
                R.layout.layout_inforwindow, null);
        tvMin = (TextView) infoWindow.findViewById(R.id.tv_infowindow_min);
        tvSec = (TextView) infoWindow.findViewById(R.id.tv_infowindow_sec);

    }

    @Override
    protected void getExras() {
    }

    @Override
    protected void setListener() {
        lvMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isDrawer) {
                    return navView.dispatchTouchEvent(motionEvent);
                } else {
                    return false;
                }
            }
        });
        drawerLayout.setScrimColor(0x50000000);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer = true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                // lvMain.layout(navView.getRight(), 0, navView.getRight() + display.getWidth(), display.getHeight());
                // lvMain.layout(0, 0, navView.getRight() + display.getWidth(), display.getHeight());
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }


    /*个推相关*/
    // DemoPushService.class 自定义服务名称, 核心服务
    private Class userPushService = PushService.class;
    private static final int REQUEST_PERMISSION = 0;

    private void checkPermission() {
        PackageManager pkgManager = getPackageManager();

        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PERMISSION);
        } else {
            PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        }

        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 DemoIntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 DemoIntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().initialize(mContext.getApplicationContext(), PushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            } else {
                Log.e("MainActivity", "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void saveDevice(String channelId) {
        User user = getApplicationContext().getUser();
        if (user == null) {
            return;
        }
        String deviceId = PushUtils.getUserId(mContext);
        if (isNull(deviceId)) {// 如果deviceId为空时，保存为手机串号
            deviceId = XtomDeviceUuidFactory.get(mContext);
        }
        getNetWorker().deviceSave(user.getToken(),
                deviceId, "2", channelId);
    }

    /*个推相关结束*/

    private void TimeTip(String start, String end) {
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
                R.layout.pop_first_tip, null);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView ok = (TextView) mViewGroup.findViewById(R.id.textview_2);
        TextView title1 = (TextView) mViewGroup.findViewById(R.id.textview);
        TextView title2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        title1.setText("当前时间段不支持手机下单，\n如有需要请联系客服" + phone);
        title2.setText("可下单时间段为" + start + "-" + end);
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
                //Intent.ACTION_CALL 直接拨打电话，就是进入拨打电话界面，电话已经被拨打出去了。
                //Intent.ACTION_DIAL 是进入拨打电话界面，电话号码已经输入了，但是需要人为的按拨打电话键，才能播出电话。
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + phone));
                startActivity(intent);
            }
        });
    }

    private void CanNotTip() {
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
                R.layout.pop_first_tip, null);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView ok = (TextView) mViewGroup.findViewById(R.id.textview_2);
        TextView title1 = (TextView) mViewGroup.findViewById(R.id.textview);
        TextView title2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        title1.setText(" 您有未完成的行程，\n暂不能发布新的行程");
        title2.setText("前去我的行程查看正在进行的行程？");
        cancel.setText("取消");
        ok.setText("确定");
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
                Intent it = new Intent(mContext, MyCurrentTrip2Activity.class);
                startActivity(it);
            }
        });
    }

    private void GpsTip() {
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
                R.layout.pop_first_tip, null);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView ok = (TextView) mViewGroup.findViewById(R.id.textview_2);
        TextView title1 = (TextView) mViewGroup.findViewById(R.id.textview);
        TextView title2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        title1.setText("请打开GPS定位");
        title2.setText("开启GPS定位功能才能正常使用地图功能");
        cancel.setText("取消");
        ok.setText("去打开");
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
                // 让用户打开GPS
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }
        });
    }

    @OnClick({R.id.title_btn_left, R.id.title_btn_right_image, R.id.title_point, R.id.lv_search,
            R.id.tv_now, R.id.tv_appointment, R.id.tv_often, R.id.tv_start_city, R.id.iv_change,
            R.id.tv_end_city, R.id.tv_start, R.id.tv_end, R.id.lv_bang, R.id.lv_one_next,
            R.id.tv_sendtwo_cancel, R.id.tv_send_pin, R.id.tv_send_bao, R.id.tv_send_time,
            R.id.tv_send_count, R.id.tv_send_coupon, R.id.tv_send_content, R.id.tv_send_feeinfor, R.id.tv_send_button,
            R.id.iv_daohang, R.id.fv_current_top, R.id.iv_cur_avatar, R.id.iv_cur_tel, R.id.tv_cur_button0, R.id.tv_cur_button1, R.id.lv_current0})
    public void onClick(View view) {
        user = BaseApplication.getInstance().getUser();
        Intent it;
        switch (view.getId()) {
            case R.id.title_btn_left:
//                if (user == null) {
//                    ToLogin.showLogin(mContext);
//                } else {
//                    it = new Intent(mContext, PersonCenterNewActivity.class);
//                    startActivity(it);
//                }
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.title_btn_right_image:
                if (user == null) {
                    ToLogin.showLogin(mContext);
                } else {
                    it = new Intent(mContext, NoticeListActivity.class);
                    startActivity(it);
                }
                break;
            case R.id.lv_search:
                if (startCity == null) {
                    showTextDialog("请先选择出发城市");
                    return;
                }
                if (endCity == null) {
                    showTextDialog("请先选择到达城市");
                    return;
                }
                it = new Intent(mContext, SearchActivity.class);
                if (selectAddress.equals("1")) {
                    it.putExtra("citycode", startCity.getName());
                    it.putExtra("city_id", startCity.getCity_id());
                    it.putExtra("hint", "从哪出发");
                    startActivityForResult(it, 10);
                }
                if (selectAddress.equals("2")) {
                    it.putExtra("citycode", endCity.getName());
                    it.putExtra("city_id", endCity.getCity_id());
                    it.putExtra("hint", "你要去哪");
                    startActivityForResult(it, 11);
                }
                break;
            case R.id.tv_now://实时
                timetype = "2";
                tvNow.setTextColor(getResources().getColor(R.color.yellow));
                tvAppointment.setTextColor(getResources().getColor(R.color.word_black));
                break;
            case R.id.tv_appointment://预约
                timetype = "1";
                tvAppointment.setTextColor(getResources().getColor(R.color.yellow));
                tvNow.setTextColor(getResources().getColor(R.color.word_black));
                break;
            case R.id.tv_often:
                it = new Intent(mContext, OftenListActivity.class);
                startActivityForResult(it, 12);
                break;
            case R.id.tv_start_city:
                it = new Intent(mContext, SelectCityActivity.class);
                it.putExtra("start_cityid", "0");
                startActivityForResult(it, 1);
                break;
            case R.id.iv_change:
                if (startCity == null) {
                    showTextDialog("请选择出发城市");
                    break;
                }
                if (endCity == null) {
                    showTextDialog("请选择到达城市");
                    break;
                }
                DistrictInfor temCity = startCity;
                startCity = endCity;
                endCity = temCity;
                String tem_address = start_address;
                start_address = end_address;
                end_address = tem_address;
                String tem_lat = start_lat;
                start_lat = end_lat;
                end_lat = tem_lat;
                String tem_lng = start_lng;
                start_lng = end_lng;
                end_lng = tem_lng;
                if (startCity != null)
                    tvStartCity.setText(startCity.getName());
                else
                    tvStartCity.setText("");
                if (endCity != null)
                    tvEndCity.setText(endCity.getName());
                else
                    tvEndCity.setText("");
                tvStart.setText(start_address);
                tvEnd.setText(end_address);
                if (hasCircle) {
                    if (selectAddress.equals("1")) {
                        selectAddress = "2";
                        tvStart.setBackgroundColor(0xffffffff);
                        tvEnd.setBackgroundColor(0x40e5e5e5);
                    } else if (selectAddress.equals("2")) {
                        selectAddress = "1";
                        tvStart.setBackgroundColor(0x40e5e5e5);
                        tvEnd.setBackgroundColor(0xffffffff);
                    }
                }
                break;
            case R.id.tv_end_city:
                if (startCity == null) {
                    showTextDialog("请先选择出发城市");
                    break;
                }
                it = new Intent(mContext, SelectCityActivity.class);
                it.putExtra("start_cityid", startCity.getCity_id());
                startActivityForResult(it, 2);
                break;
            case R.id.tv_start:
                if (startCity == null) {
                    showTextDialog("请先选择出发城市");
                    return;
                }
                if (endCity == null) {
                    showTextDialog("请先选择到达城市");
                    return;
                }
                if (hasCircle && !selectAddress.equals("1")) {
                    selectAddress = "1";
                    tvStart.setBackgroundColor(0x40e5e5e5);
                    tvEnd.setBackgroundColor(0xffffffff);
                    String centerLatlng = "";
                    if (myCity.getCity_id().equals(startCity.getCity_id())) {//正常myCity就是endCity，但是点击交换之后，要特殊处理
                        centerLatlng = myCity.getCenter_lnglat2();
                    } else {
                        centerLatlng = myCity.getCenter_lnglat1();
                    }
                    if (!isNull(centerLatlng)) {
                        if (isNull(start_lat)) {
                            move_lat = centerLatlng.split(",")[1];
                            move_lng = centerLatlng.split(",")[0];
                        } else {
                            move_lat = start_lat;
                            move_lng = start_lng;
                        }

                        CameraPosition cameraPosition = new CameraPosition(new LatLng(Double.parseDouble(move_lat), Double.parseDouble(move_lng)), 0, 0, 0);//如果当前位置没有变化，是不会走onCameraChangeFinish方法的,所以强行调一下
                        onCameraChangeFinish(cameraPosition);
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(move_lat),
                                Double.parseDouble(move_lng)), 15));
                    }
                }
                break;
            case R.id.tv_end:
                if (startCity == null) {
                    showTextDialog("请先选择出发城市");
                    return;
                }
                if (endCity == null) {
                    showTextDialog("请先选择到达城市");
                    return;
                }
                if (hasCircle && !selectAddress.equals("2")) {
                    selectAddress = "2";
                    tvStart.setBackgroundColor(0xffffffff);
                    tvEnd.setBackgroundColor(0x40e5e5e5);
                    String centerLatlng = "";
                    if (myCity.getCity_id().equals(startCity.getCity_id())) {//正常myCity就是endCity，但是点击交换之后，要特殊处理
                        centerLatlng = myCity.getCenter_lnglat1();
                    } else {
                        centerLatlng = myCity.getCenter_lnglat2();
                    }
                    if (!isNull(centerLatlng)) {
                        if (isNull(end_lat)) {
                            log_e("isnull-----------------------------------------------");
                            move_lat = centerLatlng.split(",")[1];
                            move_lng = centerLatlng.split(",")[0];
                        } else {
                            log_e("isnull2222-----------------------------------------------");
                            move_lat = end_lat;
                            move_lng = end_lng;
                        }
                        CameraPosition cameraPosition = new CameraPosition(new LatLng(Double.parseDouble(move_lat), Double.parseDouble(move_lng)), 0, 0, 0);//如果当前位置没有变化，是不会走onCameraChangeFinish方法的,所以强行调一下
                        onCameraChangeFinish(cameraPosition);
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(move_lat),
                                Double.parseDouble(move_lng)), 15));
                    }
                }
                it = new Intent(mContext, SearchActivity.class);
                it.putExtra("citycode", endCity.getName());
                it.putExtra("city_id", endCity.getCity_id());
                it.putExtra("hint", "你要去哪");
                startActivityForResult(it, 11);
                break;
            case R.id.lv_bang://帮人下单
                bangFlag = "1";
                sendNext();
                break;
            case R.id.lv_one_next:
                bangFlag = "2";
                sendNext();
                break;
            case R.id.tv_sendtwo_cancel://取消，回上一步
                lvSearch.setVisibility(View.VISIBLE);
                isSend2 = false;
                sendEndMarker.setVisible(false);
                sendStartMarker.setVisible(false);
                screenMarker.setVisible(true);
                lvSend1.setVisibility(View.GONE);
                lvSend0.startAnimation(appearAnimation);
                lvSend0.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_send_pin://拼车
                if (count == 4) {
                    return;
                }
                if (!isNull(begin) && (BaseUtil.compareTime(begin, user.getPin_start()) == -1 || BaseUtil.compareTime(user.getPin_end(), begin) == -1)) {//选择的出发时间不在可拼单时间内
                    String start = BaseUtil.TransTimeHour(user.getPin_start(), "HH:mm");
                    String end = BaseUtil.TransTimeHour(user.getPin_end(), "HH:mm");
                    showTextDialog("出发时间在" + start + "至" + end + "期间提供拼车服务");
                } else {
                    pinFlag = "1";
                    tvSendPin.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.img_agree_s, 0);
                    tvSendBao.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.img_agree_n, 0);
                    resetPrice();
                }
                break;
            case R.id.tv_send_bao://包车
                pinFlag = "0";
                tvSendPin.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.mipmap.img_agree_n, 0);
                tvSendBao.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.mipmap.img_agree_s, 0);
                //count = 4;//包车默认4人
//                tvSendCount.setText("4人");
                resetPrice();
                break;
            case R.id.tv_send_time:
                initHour(0);
                initMinute(0);
                initDay();
                showTimePopWindow();
                break;
            case R.id.tv_send_count:
                countDialog();
                break;
            case R.id.tv_send_coupon:
                it = new Intent(mContext, MyCouponListActivity.class);
                it.putExtra("keytype", "2");
                startActivityForResult(it, 5);
                break;
            case R.id.tv_send_content:
                it = new Intent(mContext, EditContentActivity.class);
                it.putExtra("content", tvSendContent.getText().toString());
                startActivityForResult(it, 6);
                break;
            case R.id.tv_send_feeinfor:
                it = new Intent(mContext, FeeInforActivity.class);
                it.putExtra("start", startCity.getName());
                it.putExtra("end", endCity.getName());
                it.putExtra("price", myCity.getPrice());
                if (pinFlag.equals("0")) {
                    it.putExtra("count", "4");
                } else {
                    it.putExtra("count", count + "");
                }
                it.putExtra("addstart", addstart + "");
                it.putExtra("addend", addend + "");
                it.putExtra("couple", coupon_vavle);
                it.putExtra("all", totleFee + "");
                startActivity(it);
                break;
            case R.id.tv_send_button://发布
                String helpcallname = "", helpcallmobile = "", content;
                content = tvSendContent.getText().toString();
                float allfee = totleFee + coupon;
                if (bangFlag.equals("1")) {//代人叫车
                    helpcallname = evSendBangName.getText().toString();
                    helpcallmobile = evSendBangTel.getText().toString();
                    if (isNull(helpcallname)) {
                        showTextDialog("请填写乘车人姓名");
                        return;
                    }
                    if (isNull(helpcallmobile)) {
                        showTextDialog("请填写乘车人电话");
                        return;
                    }
                } else {//自己叫车
                    helpcallname = "";
                    helpcallmobile = "";
                }
                if (isNull(coupon_id))
                    coupon_id = "0";
                if (timetype.equals("1")) {//预约
                    if (isNull(begintime)) {
                        showTextDialog("请选择出发时间");
                        return;
                    }
                    getNetWorker().tripsAddNew(user.getToken(), timetype, helpcallname, helpcallmobile, start_address, startCity.getCity_id(), startCity.getName(),
                            end_address, endCity.getCity_id(), endCity.getName(), begintime, count + "", pinFlag, content, start_lng, start_lat,
                            end_lng, end_lat, coupon_id, allfee + "");
                } else {//实时
                    getNetWorker().tripsAddNew(user.getToken(), timetype, helpcallname, helpcallmobile, start_address, startCity.getCity_id(), startCity.getName(),
                            end_address, endCity.getCity_id(), endCity.getName(), "", "4", "0", content, start_lng, start_lat,
                            end_lng, end_lat, coupon_id, allfee + "");
                }
                break;
            case R.id.iv_daohang://导航按钮
                if (infor != null) {
                    LatLng p0 = new LatLng(Double.parseDouble(infor.getLat_start()), Double.parseDouble(infor.getLng_start()));
                    LatLng p = new LatLng(Double.parseDouble(infor.getLat_end()), Double.parseDouble(infor.getLng_end()));
                    if (infor.getStatus().equals("0")) {//待派单
                        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, new Poi(infor.getStartaddress(), p0, ""), AmapNaviType.DRIVER), MainNewMapActivity.this);
                    } else if (infor.getStatus().equals("1")) {//待上车
                        if (driver_latlng == null)
                            AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, new Poi(infor.getStartaddress(), p0, ""), AmapNaviType.DRIVER), MainNewMapActivity.this);
                        else
                            AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(new Poi(infor.getStartaddress(), p0, ""), null, new Poi("司机位置", driver_latlng, ""), AmapNaviType.DRIVER), MainNewMapActivity.this);
                    } else {
                        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, new Poi(infor.getEndaddress(), p, ""), AmapNaviType.DRIVER), MainNewMapActivity.this);
                    }
                }
                break;
            case R.id.fv_current_top://收起、显示当前行程信息
                HiddenAnimUtils.newInstance(mContext, lvCurrentBottom, ivCurrentTop, bottomHeight).toggle();
                break;
            case R.id.iv_cur_avatar://司机头像
                mView = new ShowLargeImageView(mContext, navView);
                mView.show();
                mView.setImageURL(infor.getDriver_avatar());
                break;
            case R.id.iv_cur_tel://电话
                if (infor != null) {
                    if (infor.getStatus().equals("0")) {//待派单
                        phone = BaseApplication.getInstance().getSysInitInfo().getSys_service_phone();
                        toMakePhone();
                    } else {
                        phone = infor.getDriver_username();
                        toMakePhone();
                    }
                }
                break;
            case R.id.tv_cur_button0:
                if (infor != null) {
                    if (infor.getStatus().equals("0") || infor.getStatus().equals("1")) {//取消订单
                        CancelTip();
                    }
                }
                break;
            case R.id.tv_cur_button1:
                if (infor != null) {
                    if (infor.getStatus().equals("1")) {//确认上车
                        shangcheDialog();
                    } else if (infor.getStatus().equals("3")) {//确认送达
                        showarrivedDialog();
                    } else if (infor.getStatus().equals("5")) {//支付
                        it = new Intent(mContext, ToPayActivity.class);
                        it.putExtra("id", infor.getId());
                        it.putExtra("total_fee", infor.getTotal_fee());
                        it.putExtra("driver_id", infor.getDriver_id());
                        startActivityForResult(it, R.id.layout);
                    }
                }
                break;
        }
    }

    private void sendNext() {//发布第二步
        if (startCity == null) {
            showTextDialog("请选择出发城市");
            return;
        }
        if (endCity == null) {
            showTextDialog("请选择到达城市");
            return;
        }
        String startAdd = tvStart.getText().toString();
        String endAdd = tvEnd.getText().toString();
        if (isNull(startAdd)) {
            showTextDialog("请选择出发地点");
            return;
        }
        if (isNull(endAdd)) {
            showTextDialog("请选择目的地点");
            return;
        }
        isSend2 = true;
        lvSearch.setVisibility(View.GONE);
        screenMarker.setVisible(false);
        sendStartMarker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lng)))
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.
                                decodeResource(getResources(), R.mipmap.marker_send))));
        sendStartMarker.setVisible(true);
        sendEndMarker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(end_lat), Double.parseDouble(end_lng)))
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.
                                decodeResource(getResources(), R.mipmap.marker_send))));
        sendEndMarker.setVisible(true);
        lvSend0.setVisibility(View.GONE);
        lvSend1.startAnimation(appearAnimation);
        lvSend1.setVisibility(View.VISIBLE);
        if (bangFlag.equals("1")) {
            lvBangInfor.setVisibility(View.VISIBLE);
        } else {
            lvBangInfor.setVisibility(View.GONE);
        }
        if (timetype.equals("1")) {//预约
            lvSendOtherinfor.setVisibility(View.VISIBLE);
        } else {//实时（默认包车）
            lvSendOtherinfor.setVisibility(View.GONE);
            pinFlag = "0";
            count = 4;
            begintime = "";
        }

        resetPrice();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 1:
                startCity = (DistrictInfor) data.getSerializableExtra("infor");
                tvStartCity.setText(startCity.getName());
                endCity = null;
                myCity = null;
                tvEndCity.setText("");
                start_address = "";
                end_address = "";
                tvStart.setText("");
                tvEnd.setText("");
                price = 0;
                addend = 0;
                addstart = 0;
                resetPrice();
                //如果之前画过圈，清一下
                hasCircle = false;
                for (Polygon p : polygons) {
                    p.remove();
                }
                mapView.invalidate();//刷新地图
                polygons.clear();
                prices.clear();
                break;
            case 2:
                endCity = (DistrictInfor) data.getSerializableExtra("infor");
                myCity = (DistrictInfor) data.getSerializableExtra("infor");
                price = Float.parseFloat(myCity.getPrice());
                tvEndCity.setText(endCity.getName());
                start_address = "";
                end_address = "";
                tvStart.setText("");
                tvEnd.setText("");
                end_lat = "";
                end_lng = "";
                areas.clear();
                areas.addAll(myCity.getAreas());
                setUpMap();
                resetPrice();
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                coupon_id = data.getStringExtra("id");
                coupon_vavle = data.getStringExtra("money");
                tvSendCoupon.setText(coupon_vavle + "元");
                coupon = Integer.parseInt(coupon_vavle);
                resetPrice();
                break;
            case 6:
                String content = data.getStringExtra("content");
                tvSendContent.setText(content);
                break;
            case 7://取消订单
            case R.id.layout:
            case R.id.layout_1:
                getNetWorker().currentTrips(user.getToken());
                break;
            case 10://出发地搜索
                isSearch = true;
                Double la = data.getDoubleExtra("lat", 0);
                Double ln = data.getDoubleExtra("lng", 0);
                lng = ln + "";
                lat = la + "";
                myAddress = data.getStringExtra("name");
                latlng = new LatLng(la, ln);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                break;
            case 11://目的地搜索
                isSearch = true;
                Double la1 = data.getDoubleExtra("lat", 0);
                Double ln1 = data.getDoubleExtra("lng", 0);
                lng = ln1 + "";
                lat = la1 + "";
                myAddress = data.getStringExtra("name");
                latlng = new LatLng(la1, ln1);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                break;
            case 12://常用行程
                often = (Often) data.getSerializableExtra("often");
                getNetWorker().cityList(often.getStartcity_id(), often.getEndcity_id());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (infor != null) {//有当前行程
            return;
        }
        if (isSend2) {//发布第二步，地图滑动选址功能屏蔽
            return;
        }
        log_e("onCameraChange-------------------------------------------------------------");
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (infor != null) {//有当前行程
            return;
        }
        if (isSend2) {//发布第二步，地图滑动选址功能屏蔽
            return;
        }
        log_e("onCameraChangeFinish-------------------------------------------------------------");
        if (!isSearch) {
            tvSearch.setText("");
            myAddress = "";
        }
        CircularAnim.hide(lvSearch).go();
        progressBar.setVisibility(View.VISIBLE);
        //屏幕中心的Marker跳动
        startJumpAnimation();
        LatLng latLng = cameraPosition.target;
        if (hasCircle) {//已经画圈
            boolean b1 = false;
            for (int i = 0; i < polygons.size(); i++) {
                if (selectAddress.equals("1")) {//出发地
                    if (polygons.get(i).contains(latLng) && startCity.getCity_id().equals(areas.get(i).getCity_id())) {//选择地点是出发城市
                        b1 = true;
                        addstart = Float.parseFloat(prices.get(i));
                    }
                }
                if (selectAddress.equals("2")) {//目的地
                    if (polygons.get(i).contains(latLng) && endCity.getCity_id().equals(areas.get(i).getCity_id())) {//选择地点是到达城市
                        b1 = true;
                        addend = Float.parseFloat(prices.get(i));
                    }
                }
            }
            inArea = b1;
            if (inArea) {
                latlng = latLng;
                lat = latLng.latitude + "";
                lng = latLng.longitude + "";
                LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                        GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
            } else {
                showTextDialog("该区域暂未开通");
                if (selectAddress.equals("1"))
                    tvStart.setText("");
                if (selectAddress.equals("2"))
                    tvEnd.setText("");
            }
        } else {//还没画圈
            latlng = latLng;
            lat = latLng.latitude + "";
            lng = latLng.longitude + "";
            loclatlng = latLng;
            loc_lat = latLng.latitude + "";
            loc_lng = latLng.longitude + "";
            LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                    GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                RegeocodeAddress address = result.getRegeocodeAddress();
                if (!isSearch) {
                    if (address.getAois() != null && address.getAois().size() > 0)
                        myAddress = address.getCity() + address.getAois().get(0).getAoiName();
                    else {
                        if (address.getFormatAddress().contains("自治区")) {
                            myAddress = address.getFormatAddress().split("自治区")[1];
                        } else
                            myAddress = address.getFormatAddress().substring(3);
                    }
                }
                isSearch = false;
                tvSearch.setText(myAddress);
                if (infor == null) {
                    tvSearch.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            if (infor == null)
                                CircularAnim.show(lvSearch).go();
                        }
                    }, 400);
                } else {
                    lvSearch.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
                if (hasCircle) {
                    if (selectAddress.equals("1")) {//出发地
                        tvStart.setText(myAddress);
                        start_address = myAddress;
                        start_lat = lat;
                        start_lng = lng;
                    } else if (selectAddress.equals("2")) {//目的地
                        tvEnd.setText(myAddress);
                        end_address = myAddress;
                        end_lat = lat;
                        end_lng = lng;
                    }
                }
            } else {
                XtomToastUtil.showShortToast(mContext, "抱歉，没有找到符合的结果");
            }
        } else if (rCode == 27) {
            XtomToastUtil.showShortToast(mContext, "网络出现问题,请重新检查");
        } else if (rCode == 32) {
            XtomToastUtil.showShortToast(mContext, "应用key值,请重新检查");
        } else {
            XtomToastUtil.showShortToast(mContext, "出现其他类型的问题,请重新检查");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {

        if (screenMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = screenMarker.getPosition();
            Point point = aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(this, 50);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(500);
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();

        } else {
            Log.e("amap", "screenMarker is null");
        }
    }

    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (infor != null && infor.getStatus().equals("1")) {//待上车，调取司机位置
            getNetWorker().driverPositionGet(user.getToken(), infor.getId(), infor.getDriver_id());
        }
        if (infor != null && infor.getStatus().equals("3") && infor.getIs_helpcall().equals("1")) {//待送达，帮人叫车，调取司机位置
            getNetWorker().driverPositionGet(user.getToken(), infor.getId(), infor.getDriver_id());
        }
        if (location != null) {
            log_e("执行定位------------------------------" + location.getExtras().getString("desc"));
            String c = location.getExtras().getString("desc").split(" ")[1];
            log_e("执行定位city=" + c);
            XtomSharedPreferencesUtil.save(mContext, "city", c);
            mapView.setVisibility(View.VISIBLE);
            LatLng latlng0 = new LatLng(location.getLatitude(), location.getLongitude());
            loclatlng = latlng0;
            loc_lng = String.valueOf(location.getLongitude());
            loc_lat = String.valueOf(location.getLatitude());
            if (isFirstLoc) {
                latlng = latlng0;
                lng = String.valueOf(location.getLongitude());
                lat = String.valueOf(location.getLatitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                getNetWorker().currentTrips(user.getToken());
            }
            isFirstLoc = false;
        }
    }

    private void countDialog() {
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
                R.layout.pop_count, null);
        TextView bt_ok = (TextView) mViewGroup_exit.findViewById(R.id.tv_button);
        TextView bt_cancel = (TextView) mViewGroup_exit.findViewById(R.id.tv_cancel);
        RecyclerView recyclerView = (RecyclerView) mViewGroup_exit.findViewById(R.id.recyclerView);
        mWindow_exit.setContentView(mViewGroup_exit);
        mWindow_exit.showAtLocation(mViewGroup_exit, Gravity.CENTER, 0, 0);
        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mWindow_exit.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mWindow_exit.dismiss();
                for (PersonCountInfor infor : counts) {
                    if (infor.isChecked()) {
                        count = Integer.parseInt(infor.getCount());
                        resetPrice();
                        tvSendCount.setText(count + "人");
                        if (count == 4) {
                            pinFlag = "0";
                            tvSendPin.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                    R.mipmap.img_agree_n, 0);
                            tvSendBao.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                    R.mipmap.img_agree_s, 0);
                        }
                        break;
                    }
                }
            }
        });
        final PersonCountAdapter adapter = new PersonCountAdapter(mContext, counts);
        RecycleUtils.initHorizontalRecyle(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void resetPrice() {
        if (pinFlag.equals("0")) {//包车
            totleFee = price * 4 - coupon + addend + addstart;
        } else
            totleFee = price * count - coupon + addend + addstart;
        tvPrice.setText(totleFee + "元");
    }

    private void showTimePopWindow() {
        if (timePop != null) {
            timePop.dismiss();
        }
        timePop = new PopupWindow(mContext);
        timePop.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        timePop.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        timePop.setBackgroundDrawable(new BitmapDrawable());
        timePop.setFocusable(true);
        timePop.setAnimationStyle(R.style.PopupAnimation);
        timeViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.pop_time3, null);
        dayListView = (WheelView) timeViewGroup
                .findViewById(R.id.listview0);
        timeListView = (WheelView) timeViewGroup
                .findViewById(R.id.listview1);
        secondListView = (WheelView) timeViewGroup
                .findViewById(R.id.listview2);
        TextView time_clear = (TextView) timeViewGroup
                .findViewById(R.id.clear);
        TextView time_ok = (TextView) timeViewGroup.findViewById(R.id.ok);
        timePop.setContentView(timeViewGroup);
        timePop.showAtLocation(timeViewGroup, Gravity.CENTER, 0, 0);

        dayListView.setVisibleItems(4);
        dayListView.setViewAdapter(new PopTimeAdapter(mContext, days));
        dayListView.setCurrentItem(0);
        dayListView.addScrollingListener(scrollListener);

        timeListView.setVisibleItems(4);
        timeListView.setViewAdapter(new PopTimeAdapter(mContext, times));
        timeListView.setCurrentItem(0);
        timeListView.addScrollingListener(scrollListener);

        secondListView.setVisibleItems(4);
        secondListView.setViewAdapter(new PopTimeAdapter(mContext, seconds));
        secondListView.setCurrentItem(0);
        secondListView.addScrollingListener(scrollListener);

        time_clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timePop.dismiss();
            }
        });
        time_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timePop.dismiss();
                String t1;
                int d = dayListView.getCurrentItem();
                int t = timeListView.getCurrentItem();
                int s = secondListView.getCurrentItem();

                String time = times.get(t).substring(0, times.get(t).length() - 1);
                if (time.length() == 1)
                    time = "0" + time;
                String second = seconds.get(s).substring(0, seconds.get(s).length() - 1);
                if (second.length() == 1)
                    second = "0" + second;
                if (timeflag == 0) {
                    if (d == 0) {
                        t1 = "今天 " + time + ":" + second + "出发";
                    } else if (d == 1) {
                        t1 = "明天" + time + ":" + second + "出发";
                    } else if (d == 2) {
                        t1 = "后天" + time + ":" + second + "出发";
                    } else {
                        t1 = days.get(d) + time + ":" + second + "出发";
                    }
                } else {
                    if (d == 0) {
                        t1 = "明天 " + time + ":" + second + "出发";
                    } else if (d == 1) {
                        t1 = "后天" + time + ":" + second + "出发";
                    } else if (d == 2) {
                        t1 = "大后天" + time + ":" + second + "出发";
                    } else {
                        t1 = days.get(d) + time + ":" + second + "出发";
                    }
                }

                SpannableString str = new SpannableString(t1);
                str.setSpan(new ForegroundColorSpan(0xff414141), 0,
                        t1.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                begintime = days1.get(d) + " " + time + ":"
                        + second + ":00";
                tvSendTime.setText(str);
                begin = time + ":" + second + ":00";
                if (BaseUtil.compareTime(begin, user.getPin_start()) == 1
                        && BaseUtil.compareTime(user.getPin_end(), begin) == 1) {//在可拼单时间内
                } else {
                    pinFlag = "0";
                    tvSendPin.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.img_agree_n, 0);
                    tvSendBao.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.img_agree_s, 0);
                    resetPrice();
                }
            }
        });
    }

    private OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int current = wheel.getCurrentItem();
            if (wheel == dayListView) {
                if (current == 0) {
                    initHour(0);
                    initMinute(0);
                } else {
                    initHour(1);
                    initMinute(1);
                }
            } else if (wheel == timeListView) {
                if (dayListView.getCurrentItem() == 0) {
                    if (current == 0)
                        initMinute(0);
                    else if (current == times.size() - 1) {
                        initMinute(2);
                    } else
                        initMinute(1);
                } else {
                    if (current == 0)
                        initMinute(1);
                    else if (current == times.size() - 1) {
                        initMinute(2);
                    } else
                        initMinute(1);
                }
            }
        }
    };

    private void initHour(int type) {
        times.clear();
        Calendar calendar = Calendar.getInstance();
        int min = calendar.get(Calendar.MINUTE);
        if (min > 30)
            calendar.add(Calendar.MINUTE, 180);
        else
            calendar.add(Calendar.MINUTE, 120); //将当前时间向后移动
        if (type == 0) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY); //新的小时
            for (int i = hour; i < 24; i++) {
                times.add(i - hour, i + "点");
            }
        } else {
            int hour = calendar.get(Calendar.HOUR_OF_DAY); //新的小时
            if (hour < 3)
                hour = 2;
            for (int i = 0; i < hour - 1; i++) {
                times.add(i, i + "点");
            }
        }

        if (timeListView != null) {
            timeListView.setVisibleItems(4);
            time_adapter = new PopTimeAdapter(mContext, times);
            timeListView.setViewAdapter(time_adapter);
            timeListView.setCurrentItem(0);
            timeListView.addScrollingListener(scrollListener);
        }
    }

    private void initMinute(int type) {
        seconds.clear();
        Calendar calendar = Calendar.getInstance();
        if (type == 0) {
            int min = calendar.get(Calendar.MINUTE); //
            if (min > 0 && min <= 30)
                seconds.add(0, "30分");
            else {
                seconds.add(0, "00分");
                seconds.add(1, "30分");
            }
        } else if (type == 1) {
            seconds.add(0, "00分");
            seconds.add(1, "30分");
        } else {
            int min = calendar.get(Calendar.MINUTE); //
            seconds.add(0, "00分");
            seconds.add(1, "30分");
        }
        if (secondListView != null) {
            secondListView.setVisibleItems(4);
            secondListView.setViewAdapter(new PopTimeAdapter(mContext, seconds));
            secondListView.setCurrentItem(0);
            secondListView.addScrollingListener(scrollListener);
        }
    }

    //初始化当前日期范围
    private void initDay() {
        days.clear();
        days1.clear();
        Calendar calendar = Calendar.getInstance();
        int min = calendar.get(Calendar.MINUTE);
        if (min > 30)
            calendar.add(Calendar.MINUTE, 180);
        else
            calendar.add(Calendar.MINUTE, 120); //将当前时间向后移动
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //新的小时
        if (hour < 3) {
            timeflag = 1;
            for (int i = 1; i < 3; i++) {
                getNextDay2(i);
            }
        } else {
            timeflag = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
            String day1 = sdf.format(new Date());
            days.add(0, day1);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            days1.add(0, sdf1.format(new Date()));
            for (int i = 1; i < 2; i++) {
                getNextDay(i);
            }
        }
    }

    private void getNextDay(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        Date dt = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DAY_OF_YEAR, day);// 日期加day天
        Date dt1 = calendar.getTime();
        String day1 = sdf.format(dt1);
        days.add(day, day1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        days1.add(day, sdf1.format(dt1));
    }

    private void getNextDay2(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        Date dt = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DAY_OF_YEAR, day);// 日期加day天
        Date dt1 = calendar.getTime();
        String day1 = sdf.format(dt1);
        days.add(day - 1, day1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        days1.add(day - 1, sdf1.format(dt1));
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
        TextView content1 = (TextView) mViewGroup.findViewById(R.id.textview);
        TextView content2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView ok = (TextView) mViewGroup.findViewById(R.id.textview_2);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        if (infor.getStatus().equals("0"))
            content1.setText("拨打客服电话");
        else
            content1.setText("拨打司机电话");
        content2.setText(phone);
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
                //Intent.ACTION_CALL 直接拨打电话，就是进入拨打电话界面，电话已经被拨打出去了。
                //Intent.ACTION_DIAL 是进入拨打电话界面，电话号码已经输入了，但是需要人为的按拨打电话键，才能播出电话。
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + phone));
                startActivity(intent);
            }
        });
    }

    private void showarrivedDialog() {
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
                R.layout.pop_exit, null);
        TextView exit = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_0);
        TextView pop_content = (TextView) mViewGroup.findViewById(R.id.textview);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        pop_content.setText("为了保障您的出行，请谨慎操作。\n确定到达目的地吗？");
        cancel.setText("取消");
        exit.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
                getNetWorker().orderOperate(user.getToken(), "5", infor.getId(), "", "", "");
            }
        });
    }

    private void shangcheDialog() {
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
                R.layout.pop_exit, null);
        TextView exit = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_0);
        TextView pop_content = (TextView) mViewGroup.findViewById(R.id.textview);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        pop_content.setText("为保障您的出行，请谨慎操作。\n确定已上车吗？");
        cancel.setText("取消");
        exit.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
                getNetWorker().orderOperate(user.getToken(), "3", infor.getId(), "", "", "");
            }
        });
    }

    private void CancelTip() {
        user = BaseApplication.getInstance().getUser();
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
                R.layout.pop_first_tip, null);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView ok = (TextView) mViewGroup.findViewById(R.id.textview_2);
        TextView title1 = (TextView) mViewGroup.findViewById(R.id.textview);
        TextView title2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        if (user.getToday_cancel_count().equals("3")) {
            title1.setText("您今天已取消3次订单！");
        } else
            title1.setText("确定要取消吗？");
        title2.setText("一天内订单取消不能超过3次,您已取消" + user.getToday_cancel_count() + "次");
        cancel.setText("我再想想");
        ok.setText("确定");
        ok.setTextColor(0xffa2a2a2);
        cancel.setTextColor(0xfff49400);
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
                if (user.getToday_cancel_count().equals("3")) {
                    return;
                }
                Intent it = new Intent(mContext, CancelOrderActivity.class);
                it.putExtra("id", infor.getId());
                if (infor.getStatus().equals("0"))
                    it.putExtra("keytype", "1");
                else
                    it.putExtra("keytype", "6");
                startActivityForResult(it, 7);
            }
        });
    }

    private void NotifiTip() {
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
                R.layout.pop_first_tip, null);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView ok = (TextView) mViewGroup.findViewById(R.id.textview_2);
        TextView title1 = (TextView) mViewGroup.findViewById(R.id.textview);
        TextView title2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        title1.setText("请打开通知状态");
        title2.setText("打开通知状态才能正常接收系统消息");
        cancel.setText("取消");
        ok.setText("去打开");
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
                getAppDetailSettingIntent(mContext);
            }
        });
    }

    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onGetNavigationText(String s) {
        amapTTSController.onGetNavigationText(s);
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onStopSpeaking() {
        amapTTSController.stopSpeaking();
    }

    private Handler handler;
    private Timer timer;
    private TimerTask timerTask;
    static int minute = -1;
    static int second = -1;
    static int millisecond = -1;
    final int TimeGapMilliSecond = 1;
    //final int TimeGapSecond = 1000;
    // final int TimeGapMinute = 60 * 1000;
    //boolean isTimer = true; //是否在计时
    private TextView tvMin, tvSec;

    @Override
    public View getInfoWindow(Marker marker) {

        return infoWindow;
    }

    private void handler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) { // msg.what == -1 正计时
                    if (millisecond == 999) {
                        if (second == 59) {
                            minute++;
                            second = 0;
                            millisecond = 0;
                        } else {
                            second++;
                            millisecond = 0;
                        }
                    } else {
                        millisecond++;
                    }
                }
                tvShow();
            }
        };
    }

    public void tvShow() {
        tvSec.setText(BaseUtil.alignmentString(second) + "秒");
        tvMin.setText(BaseUtil.alignmentString(minute) + "分");
    }

    public void setOriginTime(int setMinute, int setSecond, int setMilliSecond) {
        minute = setMinute;
        second = setSecond;
        millisecond = setMilliSecond;
    }

    public void startTimeTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, TimeGapMilliSecond);

    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }

    //----------------------------个人中心------------------------------------------------------------------------------------------
    RoundedImageView ivAvatar;
    TextView tvCenterName;
    ImageView ivSex;
    TextView tvCarCount;
    TextView tvFee;
    LinearLayout lvMywallet;
    TextView tvCouple;
    LinearLayout lvMycouple;
    LinearLayout lvMyorder;
    TextView tvPassword;
    TextView set;
    TextView share;
    TextView tvPhone;
    LinearLayout lvPhone;

    public void findCenter() {
        View centerView = navView.inflateHeaderView(R.layout.layout_personcenter);
        ivAvatar = (RoundedImageView) centerView.findViewById(R.id.iv_avatar);
        tvCenterName = (TextView) centerView.findViewById(R.id.tv_center_name);
        ivSex = (ImageView) centerView.findViewById(R.id.iv_sex);
        tvCarCount = (TextView) centerView.findViewById(R.id.tv_car_count);
        tvFee = (TextView) centerView.findViewById(R.id.tv_fee);
        lvMywallet = (LinearLayout) centerView.findViewById(R.id.lv_mywallet);
        tvCouple = (TextView) centerView.findViewById(R.id.tv_couple);
        lvMycouple = (LinearLayout) centerView.findViewById(R.id.lv_mycouple);
        lvMyorder = (LinearLayout) centerView.findViewById(R.id.lv_myorder);
        tvPassword = (TextView) centerView.findViewById(R.id.tv_password);
        set = (TextView) centerView.findViewById(R.id.set);
        share = (TextView) centerView.findViewById(R.id.share);
        tvPhone = (TextView) centerView.findViewById(R.id.tv_phone);
        lvPhone = (LinearLayout) centerView.findViewById(R.id.lv_phone);
    }

    private ShowLargeImageView mView;

    public void setCenter() {
        SysInitInfo sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
        ImageLoader.getInstance().displayImage(user.getAvatar(), ivAvatar, BaseApplication.getInstance()
                .getOptions(R.mipmap.default_user));
        ivAvatar.setCornerRadius(100);
        tvCenterName.setText(user.getRealname());
        tvPhone.setText(sysInitInfo.getSys_service_phone());
        if ("男".equals(user.getSex()))
            ivSex.setImageResource(R.mipmap.img_sex_boy);
        else
            ivSex.setImageResource(R.mipmap.img_sex_girl);
        tvCarCount.setText("乘车次数 " + (isNull(user.getTakecount()) ? "0" : user.getTakecount()));
        tvFee.setText(user.getFeeaccount() + "元");
        tvCouple.setText(user.getCouponcount() + "张");
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, PersonInforActivity.class);
                startActivityForResult(it, 20);
            }
        });
        lvMywallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, MyPurseNewActivity.class);
                startActivity(it);
            }
        });
        lvMycouple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, MyCouponListActivity.class);
                it.putExtra("keytype", "1");
                startActivity(it);
            }
        });
        lvMyorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, MListActivity.class);
                startActivity(it);
            }
        });
        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, PassWord0Activity.class);
                startActivity(it);
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SetActivity.class);
                startActivity(it);
            }
        });
        lvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCenterPhone();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    private void toCenterPhone() {
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
        TextView content1 = (TextView) mViewGroup.findViewById(R.id.textview);
        TextView content2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView ok = (TextView) mViewGroup.findViewById(R.id.textview_2);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        content1.setText("拨打客服电话");
        content2.setText(BaseApplication.getInstance().getSysInitInfo().getSys_service_phone());
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
                //Intent.ACTION_CALL 直接拨打电话，就是进入拨打电话界面，电话已经被拨打出去了。
                //Intent.ACTION_DIAL 是进入拨打电话界面，电话号码已经输入了，但是需要人为的按拨打电话键，才能播出电话。
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + BaseApplication.getInstance().getSysInitInfo().getSys_service_phone()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> hashMap) {
        if (arg0.getName().equals(Wechat.NAME)) {// 判断成功的平台是不是微信
            handlerShaer.sendEmptyMessage(1);
        }
        if (arg0.getName().equals(WechatMoments.NAME)) {// 判断成功的平台是不是微信朋友圈
            handlerShaer.sendEmptyMessage(2);
        }
        if (arg0.getName().equals(QQ.NAME)) {// 判断成功的平台是不是QQ
            handlerShaer.sendEmptyMessage(3);
        }
        if (arg0.getName().equals(QZone.NAME)) {// 判断成功的平台是不是空间
            handlerShaer.sendEmptyMessage(4);
        }
        if (arg0.getName().equals(WechatFavorite.NAME)) {// 判断成功的平台是不是微信收藏
            handlerShaer.sendEmptyMessage(5);
        }
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 6;
        msg.obj = arg2.getMessage();
        handlerShaer.sendMessage(msg);

    }

    @Override
    public void onCancel(Platform platform, int i) {
        handlerShaer.sendEmptyMessage(7);
    }

    Handler handlerShaer = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getNetWorker().shareCallback(user.getToken(), "1", "0", "1");
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    getNetWorker().shareCallback(user.getToken(), "1", "0", "2");
                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    getNetWorker().shareCallback(user.getToken(), "1", "0", "3");
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    getNetWorker().shareCallback(user.getToken(), "1", "0", "4");
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

    @SuppressWarnings("deprecation")
    private void share() {
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
        View all = mViewGroup_exit.findViewById(R.id.allitem);
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
            //oks.setTitle("跨城出行就用小叫车，注册即得50元代金券！");
            oks.setTitle(sharetitle);
            oks.setTitleUrl(pathWX); // 标题的超链接
//            oks.setText("莱芜 ⇌ 济南25元；\n" +
//                    "莱芜 ⇌ 泰安15元。");
            oks.setText(sharecontent);
            oks.setImagePath(imageurl);
            oks.setUrl(pathWX);
            oks.setSiteUrl(pathWX);
            oks.setCallback(this);

        }
        oks.setPlatform(platform);
        oks.show(mContext);
    }
}
