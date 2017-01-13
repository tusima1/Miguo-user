package com.miguo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;
import com.fanwe.library.customview.SDScaleImageView;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
        return null;
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {

    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {

    }

    class ViewHolder extends RecyclerView.ViewHolder{
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


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
