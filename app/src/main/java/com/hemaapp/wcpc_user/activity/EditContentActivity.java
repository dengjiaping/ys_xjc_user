package com.hemaapp.wcpc_user.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseHttpInformation;
import com.hemaapp.wcpc_user.BaseUtil;
import com.hemaapp.wcpc_user.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 备注
 */
public class EditContentActivity extends BaseActivity {


    @BindView(R.id.title_btn_left)
    ImageView titleBtnLeft;
    @BindView(R.id.title_btn_right)
    TextView titleBtnRight;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.ev_content)
    EditText evContent;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addcontent);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        if (!isNull(content)) {
            evContent.setText(content);
            evContent.setSelection(content.length());
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY_SAVE:
                showProgressDialog("请稍后...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY_SAVE:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        BaseHttpInformation information = (BaseHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ALIPAY_SAVE:
                showTextDialog("提交失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask netTask, HemaBaseResult baseResult) {
        BaseHttpInformation information = (BaseHttpInformation) netTask.getHttpInformation();
        switch (information) {
            case ALIPAY_SAVE:
                showTextDialog(baseResult.getMsg());
                break;
        }
    }

    @Override
    protected void findView() {
    }

    @Override
    protected void getExras() {
        content = mIntent.getStringExtra("content");
    }

    @Override
    protected void setListener() {
        titleText.setText("备注");
        titleBtnRight.setText("确定");
        evContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});

    }

    @OnClick({R.id.title_btn_left, R.id.title_btn_right})
    public void onClick(View view) {
        BaseUtil.hideInput(mContext, titleBtnLeft);
        switch (view.getId()) {
            case R.id.title_btn_left:
                finish();
                break;
            case R.id.title_btn_right:
                content = evContent.getText().toString();
                if (isNull(content))
                    content = "";
                mIntent.putExtra("content", content);
                setResult(RESULT_OK, mIntent);
                finish();
                break;
        }
    }
}
