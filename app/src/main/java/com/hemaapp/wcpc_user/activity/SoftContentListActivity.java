package com.hemaapp.wcpc_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.EventBusModel;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.RecycleUtils;
import com.hemaapp.wcpc_user.adapter.SoftContentAdapter;
import com.hemaapp.wcpc_user.model.SoftContent;
import com.hemaapp.wcpc_user.model.SysInitInfo;
import com.hemaapp.wcpc_user.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 软件使用说明
 */
public class SoftContentListActivity extends BaseActivity {

    @BindView(R.id.title_btn_left)
    ImageView titleBtnLeft;
    @BindView(R.id.title_btn_right)
    TextView titleBtnRight;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.imageView)
    ImageView imageView;
    private User user;
    private String token = "";
    private SoftContentAdapter adapter;
    private ArrayList<SoftContent> blogs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_softcontent_list);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        titleBtnRight.setVisibility(View.GONE);
        user = BaseApplication.getInstance().getUser();
        if (user == null)
            token = "";
        else
            token = user.getToken();
        adapter = new SoftContentAdapter(mContext, blogs);
        RecycleUtils.initVerticalRecyle(rvList);
        rvList.setAdapter(adapter);
        getList();
        adapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent it = new Intent(mContext, ShowInternetPageActivity.class);
                it.putExtra("name", blogs.get(position).getName());
                SysInitInfo sysInitInfo = BaseApplication.getInstance().getSysInitInfo();
                String path = sysInitInfo.getSys_web_service() + "webview/parm/softexplain/id/"+blogs.get(position).getId();
                it.putExtra("path", path);
                startActivity(it);
            }
        });
    }

    private void getList() {
        getNetWorker().softContentList();
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
    protected void callBeforeDataBack(HemaNetTask netTask) {
        // TODO Auto-generated method stub
        BaseHttpInformation information = (BaseHttpInformation) netTask
                .getHttpInformation();
        switch (information) {
            case SOFT_LIST:
                break;
            case CLIENT_ROUTE_OPEARATE:
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
            case SOFT_LIST:
                break;
            case CLIENT_ROUTE_OPEARATE:
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
            case SOFT_LIST:
                @SuppressWarnings("unchecked")
                HemaArrayParse<SoftContent> gResult = (HemaArrayParse<SoftContent>) baseResult;
                ArrayList<SoftContent> goods = gResult.getObjects();
                this.blogs.clear();
                this.blogs.addAll(goods);
                adapter.notifyDataSetChanged();
                break;
            case CLIENT_ROUTE_OPEARATE:
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
            case SOFT_LIST:
            case CLIENT_ROUTE_OPEARATE:
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
            case SOFT_LIST:
                showTextDialog("加载失败");
                break;
            case CLIENT_ROUTE_OPEARATE:
                showTextDialog("删除失败");
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
        titleText.setText("软件使用说明");
        titleBtnRight.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 1:
                break;
        }
    }

    @OnClick({R.id.title_btn_left, R.id.title_btn_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_btn_left:
                finish();
                break;
            case R.id.title_btn_right:
                break;
        }
    }
}
