package com.fanwe.user.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getGroupBuyCoupon.ModelShopInfo2;

import java.util.List;

/**
 * Created by didik on 2016/8/25.
 */
public class ShopListAdapter extends BaseAdapter {

    List<ModelShopInfo2> mData;
    public ShopListAdapter(List<ModelShopInfo2> data) {
        mData=data;
    }

    @Override
    public int getCount() {
        return mData==null? 0:mData.size();
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_coupon_detail,null);
            holder.tv_shopName= (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_shopTel= (TextView) convertView.findViewById(R.id.tv_tel);
            holder.tv_shopAddress= (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        if (mData!=null){
            ModelShopInfo2 item = mData.get(position);
            holder.tv_shopName.setText(item.getShop_name());
            holder.tv_shopTel.setText(item.getTel());
            holder.tv_shopAddress.setText(item.getAddress());
        }
        return convertView;
    }

    public void setData(List<ModelShopInfo2> data){
        mData=data;
        notifyDataSetChanged();
    }

    private class ViewHolder{
        public TextView tv_shopName;
        public TextView tv_shopTel;
        public TextView tv_shopAddress;
    }
}
