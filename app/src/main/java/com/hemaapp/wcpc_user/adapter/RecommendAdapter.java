package com.hemaapp.wcpc_user.adapter;

import android.content.Context;
import android.widget.TextView;

import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.Recomm;
import com.hemaapp.wcpc_user.model.User;

import java.util.List;

/**
 * 推荐地址
 */
public class RecommendAdapter extends BaseRecycleAdapter<Recomm> {
    private Context mContext;
    public Recomm blog;
    User user;

    public RecommendAdapter(Context mContext, List<Recomm> datas) {
        super(datas);
        this.mContext = mContext;
        user = BaseApplication.getInstance().getUser();
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {
        final Recomm infor = datas.get(position);
        ((TextView) holder.getView(R.id.textview)).setText(infor.getAddress());
    }

    @Override
    public int getLayoutId() {
        return R.layout.listitem_recom;
    }


}
