package com.hemaapp.wcpc_user.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.dialog.HemaButtonDialog;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseNetWorker;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.ToLogin;
import com.hemaapp.wcpc_user.model.Often;
import com.hemaapp.wcpc_user.model.User;

import java.util.List;

/**
 * 常用行程
 */
public class OftenAdapter extends BaseRecycleAdapter<Often> {
    private Context mContext;
    private BaseNetWorker netWorker;
    public Often blog;
    User user;

    public OftenAdapter(Context mContext, List<Often> datas, BaseNetWorker netWorker) {
        super(datas);
        this.mContext = mContext;
        this.netWorker = netWorker;
        user = BaseApplication.getInstance().getUser();
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {
        final Often infor = datas.get(position);
        ((TextView) holder.getView(R.id.tv_start)).setText(infor.getStartaddress());
        ((TextView) holder.getView(R.id.tv_end)).setText(infor.getEndaddress());
        holder.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blog = infor;
                User user = BaseApplication.getInstance().getUser();
                if (user == null) {
                    ToLogin.showLogin((BaseActivity) mContext);
                    return;
                }
                dialog();
            }
        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.listitem_often;
    }

    private HemaButtonDialog mDialog;

    public void dialog() {
        if (mDialog == null) {
            mDialog = new HemaButtonDialog(mContext);
        }


        mDialog.setLeftButtonText("取消");
        mDialog.setRightButtonText("确定");
        mDialog.setText("您确定要删除此常用行程吗?");

        mDialog.setButtonListener(new ButtonListener());
        mDialog.setRightButtonTextColor(mContext.getResources().getColor(R.color.yellow));

        mDialog.show();
    }

    private class ButtonListener implements HemaButtonDialog.OnButtonListener {

        @Override
        public void onLeftButtonClick(HemaButtonDialog dialog) {
            dialog.cancel();
        }

        @Override
        public void onRightButtonClick(HemaButtonDialog dialog) {
            dialog.cancel();
            User user = BaseApplication.getInstance().getUser();
            netWorker.oftenOperate(user.getToken(), "1", blog.getId());
        }
    }

}
