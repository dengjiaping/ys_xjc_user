package com.hemaapp.wcpc_user.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.wcpc_user.BaseApplication;
import com.hemaapp.wcpc_user.BaseNetWorker;
import com.hemaapp.wcpc_user.R;
import com.hemaapp.wcpc_user.model.Client;
import com.hemaapp.wcpc_user.model.SysInitInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class InviteBookAdapter extends HemaAdapter implements OnClickListener {
    private static final int INSTALL = 0;
    private static final int NOINSTALL = 1;

    //    private ArrayList<Client> installUsers;
    private ArrayList<Client> noUsers;
    private BaseNetWorker netWorker;
    public Client mUser;
    private HashMap<String, Integer> alphaIndexer;

    public InviteBookAdapter(Context context,
                             ArrayList<Client> noUsers, BaseNetWorker netWorker) {
        super(context);
        this.noUsers = noUsers;
        this.netWorker = netWorker;
        init();
    }

    @Override
    public void notifyDataSetChanged() {
        init();
        super.notifyDataSetChanged();
    }

    private void init() {
        if (alphaIndexer == null)
            alphaIndexer = new HashMap<String, Integer>();
        else
            alphaIndexer.clear();

        for (int i = 0; i < noUsers.size(); i++) {
            Client friend = noUsers.get(i);
            // 当前汉语拼音首字母
            String currentStr = friend.getCharindex();
            // 上一个汉语拼音首字母，如果不存在为“ ”
            String lastStr = (i - 1) >= 0 ? noUsers.get(i - 1).getCharindex()
                    : " ";
            if (!lastStr.equals(currentStr)) {
                String name = friend.getCharindex();
                alphaIndexer.put(name, i);
                friend.setShowCharIndex(true);
            } else {
                friend.setShowCharIndex(false);
            }
        }
    }

    @Override
    public int getCount() {
//        int i = installUsers == null ? 0 : installUsers.size();
        int n = noUsers == null ? 0 : noUsers.size();
        return n;
    }

    @Override
    public int getItemViewType(int position) {
//        int i = installUsers == null ? 0 : installUsers.size();
//        int n = noUsers == null ? 0 : noUsers.size();
//        if (i == 0) {
//            return NOINSTALL;
//        } else if (n == 0) {
//            return INSTALL;
//        } else {
//            if (position < i)
//                return INSTALL;
//            return NOINSTALL;
//        }
        return NOINSTALL;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = get(position);
        }
        setData(convertView, position);
        return convertView;
    }

    private void setData(View view, int position) {
        int type = getItemViewType(position);
        ViewHolder holder;
        Client User = getUser(position);
        switch (type) {
            case INSTALL:
                break;
            case NOINSTALL:
                holder = (ViewHolder) view.getTag(R.id.TAG);
                if (User.isEidit()) {
                    holder.select.setVisibility(View.VISIBLE);
                    holder.buton.setVisibility(View.INVISIBLE);
                    if (User.getIs_reg().equals("1")) {
                        holder.select.setImageResource(R.mipmap.img_select_no);
                    } else {
                        if (User.isSelect()) {
                            holder.select.setImageResource(R.mipmap.img_select_p);
                        } else {
                            holder.select.setImageResource(R.mipmap.img_select_n);
                        }
                    }
                }else {
                    holder.buton.setVisibility(View.VISIBLE);
                    holder.select.setVisibility(View.GONE);
                }
                if (isNull(User.getName()))
                    holder.nickname.setText(User.getMobile());
                else
                    holder.nickname.setText(User.getName());
                holder.buton.setTag(User);
//                holder.buton.setTag(R.id.TAG, type);
                if (User.getIs_reg().equals("0")) {
                    holder.buton.setText("邀请");
                    holder.buton.setTextColor(0xff414141);
                    holder.buton.setBackgroundResource(R.drawable.bg_invite_button);
                } else {
                    holder.buton.setText("已邀请");
                    holder.buton.setTextColor(0xffAFAFAF);
                    holder.buton.setBackgroundColor(0xffffffff);
                }
                holder.buton.setOnClickListener(this);
                if (User.isShowCharIndex()) {
                    holder.charindex.setVisibility(View.VISIBLE);
                    holder.charindex.setText(User.getCharindex().toUpperCase(
                            Locale.getDefault()));
                } else {
                    holder.charindex.setVisibility(View.GONE);
                }
                break;
        }
    }

    private Client getUser(int position) {
        return noUsers.get(position);
    }

    private View get(int position) {
        int type = getItemViewType(position);
        View view = null;
        ViewHolder holder;
        switch (type) {
            case INSTALL:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.listitem_bookinvite_install, null);
                holder = new ViewHolder();
                findView0(holder, view);
                view.setTag(R.id.TAG, holder);
                break;
            case NOINSTALL:
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.listitem_bookinvite_noinstall, null);
                holder = new ViewHolder();
                findView(holder, view);
                view.setTag(R.id.TAG, holder);
                break;
        }
        return view;
    }

    private void findView(ViewHolder holder, View view) {
        holder.nickname = (TextView) view.findViewById(R.id.nickname);
        holder.buton = (TextView) view.findViewById(R.id.button);
        holder.charindex = (TextView) view.findViewById(R.id.charindex);
        holder.select = (ImageView) view.findViewById(R.id.iv_select);
    }

    private void findView0(ViewHolder holder, View view) {
        holder.nickname = (TextView) view.findViewById(R.id.nickname);
        holder.buton = (TextView) view.findViewById(R.id.button);
    }

    private static class ViewHolder {
        TextView charindex;
        TextView nickname;
        TextView buton;
        ImageView select;
    }

    @Override
    public void onClick(View v) {
        mUser = (Client) v.getTag();
        if (mUser.getIs_reg().equals("0")) {
            String msg = "下载链接"+BaseApplication.getInstance().getSysInitInfo().getSys_plugins() +
                    "share/sdk.php?invitecode=" + BaseApplication.getInstance().getUser().getInvitecode() + "&keyid=0" + "&type=1"+"【小叫车】跨城用车，就用小叫车！";
            Uri smsToUri = Uri.fromParts("smsto", mUser.getMobile(), null);
            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            SysInitInfo initInfo = BaseApplication.getInstance()
                    .getSysInitInfo();
            intent.putExtra("sms_body", initInfo.getMsg_invite()+msg);
            mContext.startActivity(intent);

        }
    }

    /**
     * @return the alphaIndexer
     */
    public HashMap<String, Integer> getAlphaIndexer() {
        return alphaIndexer;
    }

}
