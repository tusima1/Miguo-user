package com.fanwe.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 网红主页直播列表适配器
 */

public class UserHomeLiveImgAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<String> datas;


    public UserHomeLiveImgAdapter(Context mContext, LayoutInflater layoutInflater, List<String> datas) {
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
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    private void setData(Holder mHolder, final int position) {
        ImageLoader.getInstance().displayImage(datas.get(position), mHolder.ivBg);
    }

    private static class Holder {
        ImageView ivBg;
    }

}
