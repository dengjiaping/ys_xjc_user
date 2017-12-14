package com.hemaapp.wcpc_user.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.ClientAddCoupon;
import com.hemaapp.wcpc_user.model.User;

import java.util.List;

/**
 * 注册送代金券
 */
public class ClientAddCouponAdapter extends BaseRecycleAdapter<ClientAddCoupon> {
    private Context mContext;
    public ClientAddCoupon blog;
    User user;

    public ClientAddCouponAdapter(Context mContext, List<ClientAddCoupon> datas) {
        super(datas);
        this.mContext = mContext;
        user = BaseApplication.getInstance().getUser();
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {
        final ClientAddCoupon infor = datas.get(position);
        holder.getView(R.id.lv_bg).setBackgroundResource(R.mipmap.bg_coupon_using);
        ((TextView) holder.getView(R.id.tv_price)).setTextColor(0xfff49400);
        ((TextView) holder.getView(R.id.tv_regdate)).setTextColor(0xfff49400);
        ((TextView) holder.getView(R.id.tv_danwei)).setTextColor(0xfff49400);
        ((TextView) holder.getView(R.id.tv_price)).setText(infor.getCoupon_value());
        ((TextView) holder.getView(R.id.tv_regdate)).setText("有效期至 " + infor.getCoupon_dateline());
        if (infor.getCoupon_type().equals("1")) {//抵扣
            holder.getView(R.id.tv_notice).setVisibility(View.GONE);
            holder.getView(R.id.lv_price).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_free).setVisibility(View.GONE);
            holder.getView(R.id.lv_active).setVisibility(View.GONE);
        } else if (infor.getCoupon_type().equals("2")) {//免单一人
            holder.getView(R.id.tv_notice).setVisibility(View.VISIBLE);
            holder.getView(R.id.lv_price).setVisibility(View.GONE);
            holder.getView(R.id.tv_free).setVisibility(View.VISIBLE);
            holder.getView(R.id.lv_active).setVisibility(View.VISIBLE);
            if (infor.getIs_active().equals("1")) {//已激活
                holder.getView(R.id.tv_right).setVisibility(View.GONE);
                holder.getView(R.id.tv_share).setVisibility(View.GONE);
                ((TextView) holder.getView(R.id.tv_status)).setText("已激活");
            } else {
                holder.getView(R.id.tv_right).setVisibility(View.VISIBLE);
                holder.getView(R.id.tv_share).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.tv_status)).setText("未激活");
            }
        }
        holder.getView(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.listitem_coupon_clientadd;
    }

}
