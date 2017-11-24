package com.hemaapp.wcpc_user.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseRecycleAdapter;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.Client;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *同行
 */
public class TogetherAdapter extends BaseRecycleAdapter<Client> {
    private Context mContext;
    public Client blog;
    View rootView;
    public TogetherAdapter(Context mContext, List<Client> datas,View rootView) {
        super(datas);
        this.mContext=mContext;
        this.rootView=rootView;
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {
        final Client infor=datas.get(position);
        RoundedImageView imageView=(RoundedImageView)holder.getView(R.id.iv_image);
        ImageLoader.getInstance().displayImage(infor.getAvatar(),imageView, BaseApplication.getInstance()
                .getOptions(R.mipmap.default_user));
        imageView.setCornerRadius(100);
        ((TextView)holder.getView(R.id.tv_num)).setText(" "+infor.getNumbers()+"人 ");
        (holder.getView(R.id.iv_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView = new ShowLargeImageView((Activity) mContext, rootView);
                mView.show();
                mView.setImageURL(infor.getAvatar());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private ShowLargeImageView mView;
    @Override
    public int getLayoutId() {
        return R.layout.listitem_together;
    }
}
