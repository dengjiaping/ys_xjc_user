package com.hemaapp.wcpc_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayParse;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.BaseUtil;
import com.hemaapp.wcpc_user.EventBusModel;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.Often;
import com.hemaapp.wcpc_user.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 邀请好友
 */
public class InviteActivity extends BaseActivity {

    @BindView(R.id.title_btn_left)
    ImageView titleBtnLeft;
    @BindView(R.id.title_btn_right)
    TextView titleBtnRight;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.iv_avatar)
    RoundedImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.lv_save_img)
    LinearLayout lvSaveImg;
    @BindView(R.id.tv_button)
    TextView tvButton;
    private User user;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        user = BaseApplication.getInstance().getUser();
        tvName.setText("我是"+user.getRealname()+"(邀请码"+user.getInvitecode()+")");
        ImageLoader.getInstance().displayImage(user.getAvatar(), ivAvatar, BaseApplication.getInstance()
                .getOptions(R.mipmap.default_user));
        ivAvatar.setCornerRadius(100);
        ImageLoader.getInstance().displayImage(user.getQrcodeurl(), ivCode, BaseApplication.getInstance()
                .getOptions(R.color.white));
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
            case CLIENT_ROUTE_LIST:
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
            case CLIENT_ROUTE_LIST:
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
            case CLIENT_ROUTE_LIST:
                String page = netTask.getParams().get("page");
                @SuppressWarnings("unchecked")
                HemaArrayParse<Often> gResult = (HemaArrayParse<Often>) baseResult;
                ArrayList<Often> goods = gResult.getObjects();
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
            case CLIENT_ROUTE_LIST:
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
            case CLIENT_ROUTE_LIST:
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
        titleText.setText("邀请好友");
        titleBtnRight.setText("我的邀请");
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
    @OnClick({R.id.title_btn_left, R.id.title_btn_right, R.id.tv_button})
    public void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.title_btn_left:
                finish();
                break;
            case R.id.title_btn_right:
                it=new Intent(mContext,InviteListActivity.class);
                startActivity(it);
                break;
            case R.id.tv_button:
                BaseUtil.saveImage(mContext,lvSaveImg);
                break;
        }
    }
}
