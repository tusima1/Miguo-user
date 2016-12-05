package com.fanwe.user.adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.views.LiveUtil;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.utils.DisplayUtil;
import com.miguo.utils.NetWorkStateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 网红主页直播列表适配器
 */

public class UserHomeLiveImgAdapter extends BaseAdapter {
    private Activity mContext;
    private LayoutInflater inflater;
    private List<ModelRoom> datas;
    private ModelRoom currModelRoom;

    public UserHomeLiveImgAdapter(Activity mContext, LayoutInflater layoutInflater, List<ModelRoom> datas) {
        this.mContext = mContext;
        this.inflater = layoutInflater;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if (null == convertView) {
            mHolder = new Holder();
            convertView = inflater.inflate(
                    R.layout.item_user_home_live, null);
            mHolder.ivBg = (ImageView) convertView.findViewById(R.id.iv_bg_item_user_home);
            mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_shop_item_live_list_user_home);
            mHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status_item_live_list_user_home);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    private void setData(Holder mHolder, final int position) {
        currModelRoom = datas.get(position);
        SDViewBinder.setTextView(mHolder.tvName, currModelRoom.getLbs().getShop_name(), "");
        String url=currModelRoom.getCover();
        if(!TextUtils.isEmpty(url)&&url.startsWith("http://")){
            url = DisplayUtil.qiniuUrlExchange(url,125,125);
        }


        ImageLoader.getInstance().displayImage(url, mHolder.ivBg);
        SDViewBinder.setTextView(mHolder.tvStatus, LiveUtil.getLiveType(currModelRoom));
        mHolder.tvStatus.setBackgroundResource(LiveUtil.getLiveTypeColor(currModelRoom));
        mHolder.ivBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickItem(position);
            }
        });
    }

    private static class Holder {
        ImageView ivBg;
        TextView tvName, tvStatus;
    }

    private final String LIVE = "1";
    private final String LIVE_PLAY_BACK = "1";
    private final String PLAY_BACK = "2";

    private void clickItem(int position) {
        if (TextUtils.isEmpty(App.getInstance().getToken())) {
            Intent intent = new Intent(mContext, ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
            BaseUtils.jumpToNewActivity(mContext, intent);
            return;
        }
        //判断网络环境
        boolean connected = NetWorkStateUtil.isConnected(mContext);
        if (!connected) {
            MGToast.showToast("没有网络,请检测网络环境!");
            return;
        }
        ModelRoom room = (ModelRoom) getItem(position);
        LiveUtil.clickRoom(room, mContext);
    }
}
