package com.hemaapp.wcpc_user.adapter;

import android.content.Context;
import android.widget.TextView;

import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.Invite;
import com.hemaapp.wcpc_user.model.User;

import java.util.List;

import xtom.frame.util.XtomTimeUtil;

/**
 * 我的邀请
 */
public class InviteAdapter extends BaseRecycleAdapter<Invite> {
    private Context mContext;
    public Invite blog;
    private String keytype;
    User user;

    public InviteAdapter(Context mContext, List<Invite> datas) {
        super(datas);
        this.mContext = mContext;
        user = BaseApplication.getInstance().getUser();
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {
        final Invite infor = datas.get(position);

        ((TextView) holder.getView(R.id.tv_name)).setText(infor.getRealname() + "(" + infor.getUsername() + ")");
        ((TextView) holder.getView(R.id.tv_time)).setText(XtomTimeUtil.TransTime(infor.getRegdate(), "yyyy-MM-dd HH:mm"));
        if (infor.getKeytype().equals("7"))
            ((TextView) holder.getView(R.id.tv_keytype)).setText("注册");
        if (infor.getKeytype().equals("8"))
            ((TextView) holder.getView(R.id.tv_keytype)).setText("首单");
        ((TextView) holder.getView(R.id.tv_amount)).setText(infor.getAmount()+"元");
    }

    @Override
    public int getLayoutId() {
        return R.layout.listitem_invite;
    }

}
