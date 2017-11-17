package com.hemaapp.wcpc_user.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.view.RoundedImageView;
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
    public TogetherAdapter(Context mContext, List<Client> datas) {
        super(datas);
        this.mContext=mContext;
    }

    @Override
    protected void bindData(BaseViewHolder holder, final int position) {
        final Client infor=datas.get(position);
        RoundedImageView imageView=(RoundedImageView)holder.getView(R.id.iv_image);
        ImageLoader.getInstance().displayImage(infor.getAvatar(),imageView, BaseApplication.getInstance()
                .getOptions(R.mipmap.default_user));
        imageView.setCornerRadius(100);
        ((TextView)holder.getView(R.id.tv_num)).setText(" "+infor.getNumbers()+"人 ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.listitem_together;
    }
}
