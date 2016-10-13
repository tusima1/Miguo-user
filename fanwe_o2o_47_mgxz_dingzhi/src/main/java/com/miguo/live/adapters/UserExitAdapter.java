package com.miguo.live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fanwe.home.model.Room;
import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 用户直播退出页面适配器
 */

public class UserExitAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Room> datas;

    public UserExitAdapter(Context mContext, LayoutInflater layoutInflater, List<Room> datas) {
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
                    R.layout.item_pop_live_exit, null);
            mHolder.ivImg = (ImageView) convertView.findViewById(R.id.iv_ren);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    private void setData(Holder mHolder, final int position) {
        Room room = datas.get(position);
        ImageLoader.getInstance().displayImage(room.getCover(), mHolder.ivImg);
    }

    private static class Holder {
        ImageView ivImg;
    }

}
