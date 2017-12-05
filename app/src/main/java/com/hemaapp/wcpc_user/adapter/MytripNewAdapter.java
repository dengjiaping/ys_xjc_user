package com.hemaapp.wcpc_user.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.dialog.HemaButtonDialog;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.wcpc_user.BaseActivity;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseNetWorker;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.BaseUtil;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.ToLogin;
import com.hemaapp.wcpc_user.activity.CancelOrderActivity;
import com.hemaapp.wcpc_user.activity.MainNewMapActivity;
import com.hemaapp.wcpc_user.activity.PingJiaActivity;
import com.hemaapp.wcpc_user.activity.ToPayActivity;
import com.hemaapp.wcpc_user.model.CurrentTripsInfor;
import com.hemaapp.wcpc_user.model.Often;
import com.hemaapp.wcpc_user.model.User;
import com.hemaapp.wcpc_user.view.FlowLayout.TagFlowLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import xtom.frame.util.XtomBaseUtil;
import xtom.frame.util.XtomTimeUtil;

/**
 * 我的行程-2017-12-05  1.0.7
 */
public class MytripNewAdapter extends BaseRecycleAdapter<CurrentTripsInfor> {
    private Context mContext;
    private BaseNetWorker netWorker;
    public CurrentTripsInfor blog;
    private String keytype;
    User user;
    private PopupWindow mWindow;
    private ViewGroup mViewGroup;

    public MytripNewAdapter(Context mContext, List<CurrentTripsInfor> datas, BaseNetWorker netWorker) {
        super(datas);
        this.mContext = mContext;
        this.netWorker = netWorker;
        user = BaseApplication.getInstance().getUser();
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {
        final CurrentTripsInfor infor = datas.get(position);

        if (infor.getTimetype().equals("1")) {
            ((TextView) holder.getView(R.id.tv_time_type)).setText("预约");
            ((TextView) holder.getView(R.id.tv_flag)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) holder.getView(R.id.tv_time_type)).setText("实时");
            ((TextView) holder.getView(R.id.tv_flag)).setVisibility(View.GONE);
        }
        ((TextView) holder.getView(R.id.tv_start)).setText(infor.getStartaddress());
        ((TextView) holder.getView(R.id.tv_end)).setText(infor.getEndaddress());
        String today = XtomTimeUtil.getCurrentTime("yyyy-MM-dd");
        String day = XtomTimeUtil.TransTime(infor.getBegintime(), "yyyy-MM-dd");
        String time = XtomTimeUtil.TransTime(infor.getBegintime(), "HH:mm");
        if (day.equals(today))
            day = "今天";
        ((TextView) holder.getView(R.id.tv_time)).setText(day + " " + time);
        if (infor.getCarpoolflag().equals("1")) {
            ((TextView) holder.getView(R.id.tv_flag)).setText(" 拼车 ");
            ((TextView) holder.getView(R.id.tv_flag)).setBackgroundResource(R.drawable.bg_flag_pin);
            ((TextView) holder.getView(R.id.tv_flag)).setTextColor(0xfff49400);
        } else {
            ((TextView) holder.getView(R.id.tv_flag)).setText(" 包车 ");
            ((TextView) holder.getView(R.id.tv_flag)).setBackgroundResource(R.drawable.bg_flag_bao);
            ((TextView) holder.getView(R.id.tv_flag)).setTextColor(0xff65C7AB);
        }
        if (infor.getStatus().equals("0")) {
            holder.getView(R.id.iv_back).setVisibility(View.INVISIBLE);
            ((TextView) holder.getView(R.id.status)).setText("待派单");
            ((TextView) holder.getView(R.id.tv_button0)).setText("取消订单");
            ((TextView) holder.getView(R.id.tv_button0)).setVisibility(View.VISIBLE);
            ((TextView) holder.getView(R.id.tv_button1)).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_time_type).setVisibility(View.VISIBLE);
            if (infor.getStatus().equals("1")) {
                holder.getView(R.id.iv_back).setVisibility(View.INVISIBLE);
                ((TextView) holder.getView(R.id.status)).setText("待上车");
                ((TextView) holder.getView(R.id.tv_button0)).setText("取消订单");
                ((TextView) holder.getView(R.id.tv_button1)).setText("确认上车");
                ((TextView) holder.getView(R.id.tv_button0)).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.tv_button1)).setVisibility(View.VISIBLE);
            } else if (infor.getStatus().equals("3")) {
                holder.getView(R.id.iv_back).setVisibility(View.INVISIBLE);
                ((TextView) holder.getView(R.id.status)).setText("进行中");
                ((TextView) holder.getView(R.id.tv_button1)).setText("到达目的地");
                ((TextView) holder.getView(R.id.tv_button0)).setVisibility(View.GONE);
                ((TextView) holder.getView(R.id.tv_button1)).setVisibility(View.VISIBLE);
            } else if (infor.getStatus().equals("5")) {
                holder.getView(R.id.iv_back).setVisibility(View.INVISIBLE);
                ((TextView) holder.getView(R.id.status)).setText("待支付");
                ((TextView) holder.getView(R.id.tv_button1)).setText("  去支付  ");
                ((TextView) holder.getView(R.id.tv_button0)).setVisibility(View.GONE);
                ((TextView) holder.getView(R.id.tv_button1)).setVisibility(View.VISIBLE);
            } else if (infor.getStatus().equals("6")) {
                holder.getView(R.id.iv_back).setVisibility(View.VISIBLE);
                if (infor.getReplyflag1().equals("0")) {
                    ((TextView) holder.getView(R.id.status)).setText("待评价");
                    ((TextView) holder.getView(R.id.tv_button0)).setText("删除订单");
                    ((TextView) holder.getView(R.id.tv_button1)).setText("  去评价  ");
                    ((TextView) holder.getView(R.id.tv_button0)).setVisibility(View.VISIBLE);
                    ((TextView) holder.getView(R.id.tv_button1)).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) holder.getView(R.id.status)).setText("已完成");
                    ((TextView) holder.getView(R.id.tv_button0)).setText("删除订单");
                    ((TextView) holder.getView(R.id.tv_button0)).setVisibility(View.VISIBLE);
                    ((TextView) holder.getView(R.id.tv_button1)).setVisibility(View.GONE);
                }

            } else if (infor.getStatus().equals("10") || infor.getStatus().equals("11")) {
                holder.getView(R.id.iv_back).setVisibility(View.GONE);
                ((TextView) holder.getView(R.id.status)).setText("已取消");
                ((TextView) holder.getView(R.id.tv_button0)).setText("删除订单");
                ((TextView) holder.getView(R.id.tv_button0)).setVisibility(View.VISIBLE);
                ((TextView) holder.getView(R.id.tv_button1)).setVisibility(View.GONE);
            }
        }
        if (infor.getIs_helpcall().equals("1")) {
            holder.getView(R.id.tv_bang).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.tv_bang).setVisibility(View.GONE);
        }
        holder.getView(R.id.tv_button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blog = infor;
                User user = BaseApplication.getInstance().getUser();
                if (user == null) {
                    ToLogin.showLogin((BaseActivity) mContext);
                    return;
                }
                if (blog.getStatus().equals("0") || blog.getStatus().equals("1")) {
                    CancelTip();
//                    Intent it = new Intent(mContext, CancelOrderActivity.class);
//                    it.putExtra("id", infor.getId());
//                    mContext.startActivity(it);
                } else if (blog.getStatus().equals("6") || blog.getStatus().equals("10") || blog.getStatus().equals("11")) {
                    keytype = "6";
                    dialog();
                }
            }
        });
        holder.getView(R.id.tv_button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blog = infor;
                User user = BaseApplication.getInstance().getUser();
                if (user == null) {
                    ToLogin.showLogin((BaseActivity) mContext);
                    return;
                }
                if (blog.getStatus().equals("1")) {//确认上车
                    keytype = "3";
                    dialog();
                } else if (blog.getStatus().equals("3")) {//到达目的地
                    keytype = "5";
                    dialog();
                } else if (blog.getStatus().equals("5")) {//去支付
                    Intent it = new Intent(mContext, ToPayActivity.class);
                    it.putExtra("id", infor.getId());
                    it.putExtra("total_fee", infor.getTotal_fee());
                    it.putExtra("driver_id", infor.getDriver_id());
                    mContext.startActivity(it);
                } else if (blog.getStatus().equals("6")) {//去评价
                    Intent it = new Intent(mContext, PingJiaActivity.class);
                    it.putExtra("id", infor.getId());
                    it.putExtra("driver_id", infor.getDriver_id());
                    mContext.startActivity(it);
                }
            }
        });
        holder.getView(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blog = infor;
                Intent it = new Intent(mContext, MainNewMapActivity.class);
                Often often = new Often("0", blog.getEndaddress(),
                        blog.getEndcity_id(), blog.getEndcity(), blog.getStartaddress(), blog.getStartcity_id(), blog.getStartcity(),
                        blog.getLng_end(),
                        blog.getLat_end(), blog.getLng_start(), blog.getLat_start());
                it.putExtra("often", often);
                mContext.startActivity(it);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blog = infor;
                TripInfor();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.listitem_mytripslist_new2;
    }

    private HemaButtonDialog mDialog;

    public void dialog() {
        if (mDialog == null) {
            mDialog = new HemaButtonDialog(mContext);
        }
        if (keytype.equals("3")) {
            mDialog.setLeftButtonText("取消");
            mDialog.setRightButtonText("确定");
            mDialog.setText("是否确认上车？");
        } else if (keytype.equals("5")) {
            mDialog.setLeftButtonText("取消");
            mDialog.setRightButtonText("确定");
            mDialog.setText("为了保障您的出行，请谨慎操作。\n确定到达目的地吗？");
        } else if (keytype.equals("6")) {
            mDialog.setLeftButtonText("取消");
            mDialog.setRightButtonText("确定");
            mDialog.setText("您确定要删除该订单?一旦删除无法找回");
        }
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
            netWorker.tripsOperate(user.getToken(), keytype, blog.getId(), "");
        }
    }

    private void CancelTip() {
        User user = BaseApplication.getInstance().getUser();
        if (mWindow != null) {
            mWindow.dismiss();
        }
        mWindow = new PopupWindow(mContext);
        mWindow.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setBackgroundDrawable(new BitmapDrawable());
        mWindow.setFocusable(true);
        mWindow.setAnimationStyle(R.style.PopupAnimation);
        mViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.pop_first_tip, null);
        TextView cancel = (TextView) mViewGroup.findViewById(R.id.textview_1);
        TextView ok = (TextView) mViewGroup.findViewById(R.id.textview_2);
        TextView title1 = (TextView) mViewGroup.findViewById(R.id.textview);
        TextView title2 = (TextView) mViewGroup.findViewById(R.id.textview_0);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        if (user.getToday_cancel_count().equals("3")) {
            title1.setText("您今天已取消3次订单！");
        } else
            title1.setText("确定要取消吗？");
        title2.setText("一天内订单取消不能超过3次,您已取消" + user.getToday_cancel_count() + "次");
        cancel.setText("取消");
        ok.setText("确定");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
                User user = BaseApplication.getInstance().getUser();
                if (user.getToday_cancel_count().equals("3")) {
                    return;
                }
                Intent it = new Intent(mContext, CancelOrderActivity.class);
                it.putExtra("id", blog.getId());
                if (blog.getStatus().equals("0"))
                    it.putExtra("keytype", "1");
                else
                    it.putExtra("keytype", "6");
                ((BaseActivity) mContext).startActivityForResult(it, 1);
            }
        });
    }

    private void TripInfor() {
        User user = BaseApplication.getInstance().getUser();
        if (mWindow != null) {
            mWindow.dismiss();
        }
        mWindow = new PopupWindow(mContext);
        mWindow.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
        mWindow.setBackgroundDrawable(new BitmapDrawable());
        mWindow.setFocusable(true);
        mWindow.setAnimationStyle(R.style.PopupAnimation);
        mViewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.pop_tripinfor, null);
        LinearLayout lv_bang = (LinearLayout) mViewGroup.findViewById(R.id.lv_bang);
        LinearLayout lv_top = (LinearLayout) mViewGroup.findViewById(R.id.lv_top);
        LinearLayout lv_bottom = (LinearLayout) mViewGroup.findViewById(R.id.lv_bottom);
        TextView tv_driver_name = (TextView) mViewGroup.findViewById(R.id.tv_driver_name);
        TextView tv_car_name = (TextView) mViewGroup.findViewById(R.id.tv_car_name);
        TextView tv_car_number = (TextView) mViewGroup.findViewById(R.id.tv_car_number);
        RoundedImageView iv_driver_avatar = (RoundedImageView) mViewGroup.findViewById(R.id.iv_driver_avatar);
        ImageView iv_driver_sex = (ImageView) mViewGroup.findViewById(R.id.iv_driver_sex);
        ImageView iv_cancel = (ImageView) mViewGroup.findViewById(R.id.iv_cancel);
        ImageView iv_bao = (ImageView) mViewGroup.findViewById(R.id.iv_bao);
        TextView tv_count = (TextView) mViewGroup.findViewById(R.id.tv_count);
        TextView tv_price = (TextView) mViewGroup.findViewById(R.id.tv_price);
        TextView tv_bang_name = (TextView) mViewGroup.findViewById(R.id.tv_bang_name);
        TextView tv_bang_tel = (TextView) mViewGroup.findViewById(R.id.tv_bang_tel);
        TextView tv_content = (TextView) mViewGroup.findViewById(R.id.tv_content);
        ImageLoader.getInstance().displayImage(blog.getDriver_avatar(), iv_driver_avatar, BaseApplication.getInstance()
                .getOptions(R.mipmap.default_driver));
        iv_driver_avatar.setCornerRadius(100);
        tv_driver_name.setText(blog.getRealname());
        tv_car_name.setText(blog.getCarbrand());
        tv_car_number.setText(blog.getCarnumber());
        if (XtomBaseUtil.isNull(blog.getRemarks()))
            tv_content.setText("无");
        else
            tv_content.setText(blog.getRemarks());
        tv_bang_name.setText(blog.getHelpcallname());
        tv_bang_tel.setText(blog.getHelpcallmobile());
        tv_count.setText("乘车人数: " + blog.getNumbers() + "人");
        if (XtomBaseUtil.isNull(blog.getCoupon_fee()) || blog.getCoupon_fee().equals("0") || blog.getCoupon_fee().equals("0.0") || blog.getCoupon_fee().equals("0.00")) {
            tv_price.setText("乘车费用: " + blog.getTotal_fee() + "元");
        } else {
            tv_price.setText("乘车费用: " + blog.getTotal_fee() + "元(代金券抵扣" + blog.getCoupon_fee() + "元) ");
        }
        if ("男".equals(blog.getDriver_sex()))
            iv_driver_sex.setImageResource(R.mipmap.img_sex_boy);
        else
            iv_driver_sex.setImageResource(R.mipmap.img_sex_girl);
        if (blog.getIs_helpcall().equals("1")) {
            lv_bang.setVisibility(View.VISIBLE);
        } else {
            lv_bang.setVisibility(View.GONE);
        }
        if (blog.getTimetype().equals("1")) {//预约
            if (blog.getCarpoolflag().equals("1")) {//拼车
                tv_count.setVisibility(View.VISIBLE);
                iv_bao.setVisibility(View.VISIBLE);
                iv_bao.setImageResource(R.mipmap.iv_trip_pin);
            } else {//包车
                tv_count.setVisibility(View.GONE);
                iv_bao.setVisibility(View.VISIBLE);
                iv_bao.setImageResource(R.mipmap.iv_trip_bao);
            }
        } else {//实时
            tv_count.setVisibility(View.GONE);
            iv_bao.setVisibility(View.GONE);
        }
        if (XtomBaseUtil.isNull(blog.getDriver_id())||blog.getDriver_id().equals("0")){//无司机信息
            lv_top.setVisibility(View.GONE);
            lv_bottom.setBackgroundResource(R.drawable.bg_trip_white);
        }else {
            lv_top.setVisibility(View.VISIBLE);
            lv_bottom.setBackgroundResource(R.drawable.bg_driver_bottom);
        }
        BaseUtil.fitPopupWindowOverStatusBar(mWindow, true);
        mWindow.setContentView(mViewGroup);
        mWindow.showAtLocation(mViewGroup, Gravity.CENTER, 0, 0);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindow.dismiss();
            }
        });

    }
}
