package com.hemaapp.wcpc_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.EventBusConfig;
import com.hemaapp.wcpc_user.EventBusModel;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.DistrictInfor;
import com.hemaapp.wcpc_user.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 常用行程增加
 */
public class OftenAddActivity extends BaseActivity {


    @BindView(R.id.title_btn_left)
    ImageView titleBtnLeft;
    @BindView(R.id.title_btn_right)
    TextView titleBtnRight;
    @BindView(R.id.title_text)
    TextView titleText;
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
    @BindView(R.id.tv_button)
    TextView tvButton;
    private User user;
    private String token = "";
    private DistrictInfor startCity, endCity, temCity, myCity;
    private String start_address = "", end_address, start_lng, start_lat, end_lat, end_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_often_add);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        titleBtnRight.setVisibility(View.GONE);
        user = BaseApplication.getInstance().getUser();
        if (user == null)
            token = "";
        else
            token = user.getToken();
    }

    private void getList(int page) {
        getNetWorker().oftenList(token, page);
    }

    public void onEventMainThread(EventBusModel event) {
        switch (event.getType()) {
            case REFRESH_OFTEN_LIST:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_ROUTE_SAVE:
                showProgressDialog("请稍后");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_ROUTE_SAVE:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask netTask,
                                            HemaBaseResult baseResult) {
        // TODO Auto-generated method stub
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_ROUTE_SAVE:
                showTextDialog("添加成功");
                EventBus.getDefault().post(new EventBusModel(EventBusConfig.REFRESH_OFTEN_LIST));
                titleText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask,
                                           HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_ROUTE_SAVE:
                showTextDialog(baseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
        // TODO Auto-generated method stub
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case CLIENT_ROUTE_SAVE:
                showTextDialog("添加失败");
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
    }

    @Override
    protected void setListener() {
        titleText.setText("新增常用行程");
        titleBtnRight.setVisibility(View.GONE);
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
                break;
            case 2:
                endCity = (DistrictInfor) data.getSerializableExtra("infor");
                myCity = (DistrictInfor) data.getSerializableExtra("infor");
                tvEndCity.setText(endCity.getName());
                start_address = "";
                end_address = "";
                tvStart.setText("");
                tvEnd.setText("");
                break;
            case 3:
                start_lat = data.getStringExtra("lat");
                start_lng = data.getStringExtra("lng");
                start_address = data.getStringExtra("address");
                tvStart.setText(start_address);
                break;
            case 4:
                end_lat = data.getStringExtra("lat");
                end_lng = data.getStringExtra("lng");
                end_address = data.getStringExtra("address");
                tvEnd.setText(end_address);
                break;
        }
    }


    @OnClick({R.id.title_btn_left, R.id.tv_start_city, R.id.iv_change, R.id.tv_end_city, R.id.tv_start, R.id.tv_end, R.id.tv_button})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.title_btn_left:
                finish();
                break;
            case R.id.tv_start_city:
                it = new Intent(mContext, SelectCityActivity.class);
                it.putExtra("start_cityid", "0");
                startActivityForResult(it, 1);
                break;
            case R.id.iv_change:
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
                if (myCity != null) {
                    it = new Intent(mContext, MapStartActivity.class);
                    if (myCity.getCity_id().equals(startCity.getCity_id())) {//正常myCity就是endCity，但是点击交换之后，要特殊处理
                        it.putExtra("areas", myCity.getAreas2());
                        it.putExtra("center_city", myCity.getCenter_lnglat2());
                    } else {
                        it.putExtra("areas", myCity.getAreas1());
                        it.putExtra("center_city", myCity.getCenter_lnglat1());
                    }
                    it.putExtra("city", startCity.getName());
                    it.putExtra("title", "选择出发地");
                    startActivityForResult(it, 3);
                } else {
                    showTextDialog("请先选择城市");
                }
                break;
            case R.id.tv_end:
                if (myCity != null) {
                    it = new Intent(mContext, MapStartActivity.class);
                    if (myCity.getCity_id().equals(startCity.getCity_id())) {//正常myCity就是endCity，但是点击交换之后，要特殊处理
                        it.putExtra("areas", myCity.getAreas1());
                        it.putExtra("center_city", myCity.getCenter_lnglat1());
                    } else {
                        it.putExtra("areas", myCity.getAreas2());
                        it.putExtra("center_city", myCity.getCenter_lnglat2());
                    }
                    it.putExtra("city", endCity.getName());
                    it.putExtra("title", "选择目的地");
                    startActivityForResult(it, 4);
                } else {
                    showTextDialog("请先选择城市");
                }
                break;
            case R.id.tv_button:
                if (startCity == null) {
                    showTextDialog("请选择出发城市");
                    return;
                }
                if (endCity == null) {
                    showTextDialog("请选择出发城市");
                    return;
                }
                if (isNull(start_address)) {
                    showTextDialog("请选择出发地点");
                    return;
                }
                if (isNull(end_address)) {
                    showTextDialog("请选择目的地");
                    return;
                }
                getNetWorker().oftenAdd(user.getToken(), "0", start_address, startCity.getCity_id(), startCity.getName(), end_address, endCity.getCity_id(), endCity.getName(),
                        start_lng, start_lat, end_lng, end_lat);
                break;
        }
    }


}
