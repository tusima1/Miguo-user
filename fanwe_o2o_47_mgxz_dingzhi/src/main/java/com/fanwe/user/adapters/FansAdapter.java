package com.fanwe.user.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;

/**
 * Created by didik on 2016/8/25.
 */
public class FansAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_fans,null);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        return convertView;
    }

//    public void setData(List<ModelShopInfo2> data){
//        mData=data;
//        notifyDataSetChanged();
//    }

    private class ViewHolder{
        public TextView tv_shopName;
        public TextView tv_shopTel;
        public TextView tv_shopAddress;
    }
}
