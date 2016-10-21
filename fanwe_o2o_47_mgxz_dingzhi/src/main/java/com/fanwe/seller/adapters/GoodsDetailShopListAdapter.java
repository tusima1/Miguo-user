package com.fanwe.seller.adapters;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.StoreLocationActivity;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.fragment.StoreLocationFragment;
import com.fanwe.library.utils.SDActivityUtil;
import com.fanwe.library.utils.SDIntentUtil;
import com.fanwe.model.Store_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getGroupDeatilNew.ShopListBean;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDistanceUtil;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

/**
 * Created by didik on 2016/10/17.
 */

public class GoodsDetailShopListAdapter extends BaseAdapter {

    private GoodsDetailActivity mGoodsDetailActivity;
    private List<ShopListBean> mData;
    private int mCount=1;

    public GoodsDetailShopListAdapter(List<ShopListBean> data, GoodsDetailActivity
            goodsDetailActivity) {
        this.mData = data;
        this.mGoodsDetailActivity=goodsDetailActivity;
    }

    public void setCount(int count){
        this.mCount=count;
    }

    @Override
    public int getCount() {
        return mCount;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        GDViewHolder holder;
        if (convertView==null){
            holder=new GDViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_detail_shop_list,null);
            holder.tv_name_address= (TextView) convertView.findViewById(R.id.tv_name_or_address);
            holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_distance= (TextView) convertView.findViewById(R.id.tv_distance);
            holder.iv_phone= (ImageView) convertView.findViewById(R.id.iv_phone);
            holder.iv_map= (ImageView) convertView.findViewById(R.id.iv_map);
            holder.iv_shop= (ImageView) convertView.findViewById(R.id.iv_shop);
            convertView.setTag(holder);
        }
        holder= (GDViewHolder) convertView.getTag();
        final ShopListBean shopListBean = mData.get(position);
        if (shopListBean!=null){
            if (position==0){
                holder.tv_name_address.setText(shopListBean.getAddress());
            }else {
                holder.tv_name_address.setText(shopListBean.getShop_name());
            }
            String geo_x = shopListBean.getGeo_x();
            String geo_y = shopListBean.getGeo_y();
            if (TextUtils.isEmpty(geo_x) || TextUtils.isEmpty(geo_y)){
                holder.iv_map.setImageResource(R.drawable.ic_map_disable);
                holder.tv_distance.setText("--km");
            }else {
                holder.iv_map.setImageResource(R.drawable.ic_map_enable);
                int distanceFromMyLocation = (int) BaiduMapManager.getInstance()
                        .getDistanceFromMyLocation(geo_x,geo_y);
                holder.tv_distance.setText(SDDistanceUtil.getKmDistanceString(distanceFromMyLocation));
                holder.iv_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(parent.getContext(), StoreLocationActivity.class);
                        Store_infoModel store_infoModel=new Store_infoModel();
                        store_infoModel.setAddress(shopListBean.getAddress());
                        store_infoModel.setXpoint(DataFormat.toDouble(shopListBean.getGeo_x()));
                        store_infoModel.setYpoint(DataFormat.toDouble(shopListBean.getGeo_y()));
                        store_infoModel.setId(shopListBean.getId());
                        store_infoModel.setName(shopListBean.getShop_name());
                        store_infoModel.setTel(shopListBean.getTel());

                        intent.putExtra(StoreLocationFragment.EXTRA_MODEL_MERCHANTITEMACTMODEL, store_infoModel);
                        SDActivityUtil.startActivity(mGoodsDetailActivity,intent);
                    }
                });
            }

            holder.tv_time.setText(shopListBean.getTrade_day());

            final String tel = shopListBean.getTel();
            if (TextUtils.isEmpty(tel)){
                holder.iv_phone.setImageResource(R.drawable.ic_phone_disable);
            }else {
                holder.iv_phone.setImageResource(R.drawable.ic_phone_enable);
                holder.iv_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = SDIntentUtil.getIntentCallPhone(tel);
                        SDActivityUtil.startActivity(mGoodsDetailActivity, intent);
                    }
                });
            }
            String shop_id = shopListBean.getId();
            if (TextUtils.isEmpty(shop_id)){
                holder.iv_shop.setImageResource(R.drawable.ic_shop_disable);
            }else {
                holder.iv_shop.setImageResource(R.drawable.ic_shop_enable);
                holder.iv_shop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MGToast.showToast("item:"+position+"去小店");
                    }
                });
            }


        }

        return convertView;
    }

    private class GDViewHolder{
        public TextView tv_name_address;
        public TextView tv_time;
        public TextView tv_distance;

        public ImageView iv_phone;
        public ImageView iv_map;
        public ImageView iv_shop;
    }
}
