package com.hemaapp.wcpc_user.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.SoftContent;
import com.hemaapp.wcpc_user.model.User;

import java.util.List;

/**
 * 软件使用说明
 */
public class SoftContentAdapter extends BaseRecycleAdapter<SoftContent> {
    private Context mContext;
    public SoftContent blog;
    User user;

    public SoftContentAdapter(Context mContext, List<SoftContent> datas) {
        super(datas);
        this.mContext = mContext;
        user = BaseApplication.getInstance().getUser();
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {
        final SoftContent infor = datas.get(position);
        ((TextView) holder.getView(R.id.tv_name)).setText(infor.getName());
    }

    @Override
    public int getLayoutId() {
        return R.layout.listitem_softcontent;
    }

}
