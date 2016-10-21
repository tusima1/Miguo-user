package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.StoreDetailActivity;
import com.fanwe.app.App;
import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.customview.SDWeightLinearLayout;
import com.fanwe.library.customview.SDWeightLinearLayout.CalculateWidthListener;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getBusinessListings.ModelBusinessListings;
import com.fanwe.utils.DataFormat;

import java.util.List;

public class MerchantListAdapter extends SDSimpleBaseAdapter<ModelBusinessListings> {

    public MerchantListAdapter(List<ModelBusinessListings> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_merchant_list;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, final ModelBusinessListings model) {
        ImageView iv_image = get(R.id.iv_image, convertView);
        View viewHolderImage = get(R.id.view_image_holder, convertView);
        RelativeLayout ll_name_bar = get(R.id.ll_name_bar, convertView);
        ImageView iv_tag_tuan = get(R.id.iv_tag_tuan, convertView);
        final TextView tv_name = get(R.id.tv_name, convertView);
        TextView tv_distance = get(R.id.tv_distance, convertView);
        RatingBar rb_rating = get(R.id.rb_rating, convertView);
        TextView tv_rating = get(R.id.tv_rating, convertView);
        TextView tv_address = get(R.id.tv_address, convertView);
        RelativeLayout ll_tuan = get(R.id.ll_tuan, convertView);
        TextView tv_tuan_edtail = get(R.id.tv_tuan_edtail, convertView);
        ImageView iv_represent = get(R.id.iv_represent, convertView);
        //代言按钮
        if ("1".equals(model.getIs_endorsement())) {
            SDViewUtil.show(iv_represent);
        } else {
            SDViewUtil.hide(iv_represent);
        }
        if (DataFormat.toInt(model.getTuan_num()) > 0) {
            SDViewUtil.show(iv_tag_tuan);
            SDViewUtil.show(ll_tuan);
            SDViewUtil.show(viewHolderImage);
        } else {
            SDViewUtil.hide(iv_tag_tuan);
            SDViewUtil.hide(ll_tuan);
            SDViewUtil.hide(viewHolderImage);
        }
        SDViewBinder.setTextView(tv_tuan_edtail, model.getTuan_name());
        SDViewBinder.setImageView(model.getIndex_img(), iv_image);
        SDViewBinder.setTextView(tv_name, model.getShop_name());
        SDViewBinder.setRatingBar(rb_rating, DataFormat.toFloat(model.getAvg_grade()));
        if (!TextUtils.isEmpty(model.getAvg_grade())) {
            SDViewBinder.setTextView(tv_rating, model.getAvg_grade() + "分");
        } else {
            SDViewBinder.setTextView(tv_rating, "");
        }

        SDViewBinder.setTextView(tv_address, model.getAddress());
        SDViewBinder.setTextView(tv_distance, model.calculateDistance());

        SDWeightLinearLayout.calculateWidth(ll_name_bar, new CalculateWidthListener() {

            @Override
            public void onResult(int width0, int width1, int widthParent, int widthLeft, ViewGroup parent) {
                if (widthLeft > 0) {
                    tv_name.setMaxWidth(widthLeft);
                }
            }
        });

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemintent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(StoreDetailActivity.EXTRA_MERCHANT_ID, model.getId());
                bundle.putInt("type", 0);
                itemintent.putExtras(bundle);
                itemintent.setClass(App.getApplication(), StoreDetailActivity.class);
                mActivity.startActivity(itemintent);
            }
        });
    }
}