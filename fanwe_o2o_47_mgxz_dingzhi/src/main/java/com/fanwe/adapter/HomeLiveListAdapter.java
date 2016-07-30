/**
 * @版权所有 聚光科技（杭州）股份有限公司
 */
package com.fanwe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fanwe.home.model.Room;
import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 直播列表适配器
 */

public class HomeLiveListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Room> datas;

    public HomeLiveListAdapter(Context mContext,
                               LayoutInflater layoutInflater, ArrayList<Room> datas) {
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
                    R.layout.item_live_view_home_list, null);
            mHolder.ivBg = (ImageView) convertView.findViewById(R.id.iv_bg_item_live);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    private void setData(Holder mHolder, int position) {
        Room room = datas.get(position);
        ImageLoader.getInstance().displayImage(room.getCover(), mHolder.ivBg, null, null, null);
    }

    private static class Holder {
        private ImageView ivBg;
    }

}
