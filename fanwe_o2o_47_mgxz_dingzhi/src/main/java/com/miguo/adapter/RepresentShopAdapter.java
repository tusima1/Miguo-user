package com.miguo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;
import com.fanwe.app.App;
import com.fanwe.library.customview.SDScaleImageView;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getBusinessListings.ModelBusinessListings;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDistanceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.utils.BaseUtils;
import com.miguo.utils.DisplayUtil;

import java.util.List;

/**
 * Created by zlh on 2017/1/13.
 */

public class RepresentShopAdapter extends BarryBaseRecyclerAdapter {

    public RepresentShopAdapter(Activity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.item_merchant_list, parent, false);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return new RepresentShopAdapterListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).itemLayout.setOnClickListener(listener);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        /**
         * 设置代言标志
         */
        getHolder(holder).represent.setVisibility(isRepresent(position) ? View.VISIBLE : View.GONE);
        /**
         * 设置团购标志
         */
        getHolder(holder).tagTuan.setVisibility(isGroup(position) ? View.VISIBLE : View.GONE);
        getHolder(holder).tuanLayout.setVisibility(isGroup(position) ? View.VISIBLE : View.GONE);
        getHolder(holder).viewHolderImage.setVisibility(isGroup(position) ? View.VISIBLE : View.GONE);
        /**
         * 设置团购详情
         */
        getHolder(holder).detail.setText(getItem(position).getTuan_name());
        /**
         * 设置团购图片
         */
        SDViewBinder.setImageView(getImageUrl(position), getHolder(holder).image);
        /**
         * 设置商家名字
         */
        getHolder(holder).name.setText(getItem(position).getShop_name());
        /**
         * 设置团购评分
         */
        getHolder(holder).ratingBar.setRating(getRating(position));
        /**
         * 设置评分内容
         */
        getHolder(holder).rating.setText(getRatingName(position));
        /**
         * 设置地址
         */
        getHolder(holder).address.setText(getItem(position).getAddress());
        /**
         * 设置距离
         */
        getHolder(holder).distance.setText(getDistance(position));
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {

    }

    private String getImageUrl(int position){
        String url = getItem(position).getIndex_img();
        if (!TextUtils.isEmpty(url) && url.startsWith("http://")) {
            url = DisplayUtil.qiniuUrlExchange(url, 100, 100);
        }
        return url;
    }

    private String getDistance(int position){
        return SDDistanceUtil.getMGDistance(DataFormat.toDouble(getItem(position).getDistance()));
    }

    private String getRatingName(int position){
        return !TextUtils.isEmpty(getItem(position).getAvg_grade()) ? getItem(position).getAvg_grade() + "分" : "";
    }

    private float getRating(int position){
        return DataFormat.toFloat(getItem(position).getAvg_grade());
    }

    /**
     * 是否是团购
     * @param position
     * @return
     */
    private boolean isGroup(int position){
        return DataFormat.toInt(getItem(position).getTuan_num()) > 0;
    }

    /**
     * 是否已代言
     * @param position
     * @return
     */
    private boolean isRepresent(int position){
        return "1".equals(getItem(position).getIs_endorsement());
    }

    @Override
    public ModelBusinessListings getItem(int position) {
        return (ModelBusinessListings)super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder)super.getHolder(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.layout_item_merchant_list)
        LinearLayout itemLayout;
        /**
         * 图片
         */
        @ViewInject(R.id.iv_image)
        SDScaleImageView image;
        /**
         * 商家名字
         */
        @ViewInject(R.id.tv_name)
        TextView name;
        /**
         * 代言图标
         */
        @ViewInject(R.id.iv_represent)
        ImageView represent;
        /**
         * 距离
         */
        @ViewInject(R.id.tv_distance)
        TextView distance;

        /**
         * 地址
         */
        @ViewInject(R.id.tv_address)
        TextView address;

        /**
         * 分
         */
        @ViewInject(R.id.tv_rating)
        TextView rating;
        /**
         * 团购详情描述
         */
        @ViewInject(R.id.tv_tuan_edtail)
        TextView detail;

        /**
         * 计分图标
         */
        @ViewInject(R.id.rb_rating)
        RatingBar ratingBar;

        /**
         *
         */
        @ViewInject(R.id.view_image_holder)
        View viewHolderImage;

        /**
         * 团购标签
         */
        @ViewInject(R.id.iv_tag_tuan)
        ImageView tagTuan;

        @ViewInject(R.id.ll_tuan)
        RelativeLayout tuanLayout;


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class RepresentShopAdapterListener extends BarryListener{

        public RepresentShopAdapterListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_item_merchant_list:
                    clickItem();
                    break;
            }
            super.onClick(v);
        }

        private void clickItem(){
            Intent itemintent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(HiShopDetailActivity.EXTRA_MERCHANT_ID, getItem(position).getId());
            bundle.putInt("type", 0);
            itemintent.putExtras(bundle);
            itemintent.setClass(App.getApplication(), HiShopDetailActivity.class);
            BaseUtils.jumpToNewActivity(getActivity(), itemintent);
        }

    }

}
