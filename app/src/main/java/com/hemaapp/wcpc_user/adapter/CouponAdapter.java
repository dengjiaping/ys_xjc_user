package com.hemaapp.wcpc_user.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.activity.CouponListActivity;
import com.hemaapp.wcpc_user.model.CouponListInfor;
import com.hemaapp.wcpc_user.model.User;

import java.util.List;

import xtom.frame.util.XtomBaseUtil;

import static android.app.Activity.RESULT_OK;

/**
 * 代金券
 */
public class CouponAdapter extends BaseRecycleAdapter<CouponListInfor> {
    private Context mContext;
    public CouponListInfor blog;
    private String keytype;
    User user;

    public CouponAdapter(Context mContext, List<CouponListInfor> datas) {
        super(datas);
        this.mContext = mContext;
        user = BaseApplication.getInstance().getUser();
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {
        final CouponListInfor infor = datas.get(position);
        if ("1".equals(infor.getUseflag())) {
            holder.getView(R.id.lv_bg).setBackgroundResource(R.mipmap.bg_coupon_used);
            ((TextView) holder.getView(R.id.tv_regdate)).setTextColor(0xffffffff);
            ((TextView) holder.getView(R.id.tv_price)).setTextColor(0xffffffff);
            ((TextView) holder.getView(R.id.tv_danwei)).setTextColor(0xffffffff);
            ((TextView) holder.getView(R.id.tv_free)).setTextColor(0xffffffff);
            ((TextView) holder.getView(R.id.tv_share)).setBackgroundResource(R.drawable.bg_coupon_share_n);

        } else if ("1".equals(infor.getDateflag())) {
            holder.getView(R.id.lv_bg).setBackgroundResource(R.mipmap.bg_coupon_outdate);
            ((TextView) holder.getView(R.id.tv_regdate)).setTextColor(0xffffffff);
            ((TextView) holder.getView(R.id.tv_price)).setTextColor(0xffffffff);
            ((TextView) holder.getView(R.id.tv_danwei)).setTextColor(0xffffffff);
            ((TextView) holder.getView(R.id.tv_free)).setTextColor(0xffffffff);
            ((TextView) holder.getView(R.id.tv_share)).setBackgroundResource(R.drawable.bg_coupon_share_n);
            ((TextView) holder.getView(R.id.tv_right)).setTextColor(0xffffffff);
        } else {
            holder.getView(R.id.lv_bg).setBackgroundResource(R.mipmap.bg_coupon_using);
            ((TextView) holder.getView(R.id.tv_price)).setTextColor(0xfff49400);
            ((TextView) holder.getView(R.id.tv_regdate)).setTextColor(0xfff49400);
            ((TextView) holder.getView(R.id.tv_danwei)).setTextColor(0xfff49400);
            ((TextView) holder.getView(R.id.tv_free)).setTextColor(0xfff49400);
            ((TextView) holder.getView(R.id.tv_share)).setBackgroundResource(R.drawable.bg_coupon_share);
            ((TextView) holder.getView(R.id.tv_right)).setTextColor(0xfff49400);
        }
        ((TextView) holder.getView(R.id.tv_price)).setText(infor.getValue());
        ((TextView) holder.getView(R.id.tv_regdate)).setText("有效期至 " + infor.getDateline());
        if (infor.getKeytype().equals("1")) {//抵扣
            holder.getView(R.id.tv_notice).setVisibility(View.GONE);
            holder.getView(R.id.lv_price).setVisibility(View.VISIBLE);
            holder.getView(R.id.tv_free).setVisibility(View.GONE);
            holder.getView(R.id.lv_active).setVisibility(View.GONE);
        } else if (infor.getKeytype().equals("2")) {//免单一人
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
                if (XtomBaseUtil.isNull(infor.getDateflag()) || "0".equals(infor.getDateflag())) {
                    Log.e("454545", "onClick: -------------------------------------------------------------------");
                    ((CouponListActivity) mContext).share(infor.getId());
                }
            }
        });
        holder.getView(R.id.lv_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2".equals(keytype)) {
                    if (infor.getKeytype().equals("2") && !infor.getIs_active().equals("1")) {//免单一人
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("money", infor.getValue());
                    intent.putExtra("id", infor.getId());
                    intent.putExtra("keytype", infor.getKeytype());
                    ((CouponListActivity) mContext).setResult(RESULT_OK, intent);
                    ((CouponListActivity) mContext).finish();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.listitem_coupon;
    }

}
