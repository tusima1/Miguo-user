package com.fanwe.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getSpokePlay.ModelSpokePlay;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 网红主页直播列表适配器
 */

public class UserHomeLiveImgAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelSpokePlay> datas;
    private ModelSpokePlay currModelSpokePlay;

    public UserHomeLiveImgAdapter(Context mContext, LayoutInflater layoutInflater, List<ModelSpokePlay> datas) {
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
        Holder mHolder = null;
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
        currModelSpokePlay = datas.get(position);
        SDViewBinder.setTextView(mHolder.tvName, currModelSpokePlay.getShop_name(), "");
        ImageLoader.getInstance().displayImage(currModelSpokePlay.getCover(), mHolder.ivBg);
        // 开始状态,0:未开始，1:直播中，2:已结束
        if ("0".equals(currModelSpokePlay.getStart_status())) {
            SDViewBinder.setTextView(mHolder.tvStatus, "未开始");
        } else if ("1".equals(currModelSpokePlay.getStart_status())) {
            SDViewBinder.setTextView(mHolder.tvStatus, "直播中");
        } else if ("2".equals(currModelSpokePlay.getStart_status())) {
            SDViewBinder.setTextView(mHolder.tvStatus, "已结束");
        }
    }

    private static class Holder {
        ImageView ivBg;
        TextView tvName, tvStatus;
    }

}
