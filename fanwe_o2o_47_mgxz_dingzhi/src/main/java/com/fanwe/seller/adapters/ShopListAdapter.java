package com.fanwe.seller.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.customview.SDWeightLinearLayout;
import com.fanwe.library.customview.SDWeightLinearLayout.CalculateWidthListener;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;

import java.util.List;

public class ShopListAdapter extends SDSimpleBaseAdapter<ModelStoreList> {
    boolean isNotMine;

    public ShopListAdapter(List<ModelStoreList> listModel, Activity activity, boolean isNotMine) {
        super(listModel, activity);
        this.isNotMine = isNotMine;
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_merchant_list;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, final ModelStoreList model) {
        ImageView iv_image = get(R.id.iv_image, convertView);
        RelativeLayout ll_name_bar = get(R.id.ll_name_bar, convertView);
        ImageView iv_tag_tuan = get(R.id.iv_tag_tuan, convertView);
        ImageView iv_tag_quan = get(R.id.iv_tag_quan, convertView);
        final TextView tv_name = get(R.id.tv_name, convertView);
        TextView tv_distance = get(R.id.tv_distance, convertView);
        RatingBar rb_rating = get(R.id.rb_rating, convertView);
        TextView tv_address = get(R.id.tv_address, convertView);
        ImageView iv_tag_hui = get(R.id.iv_tag_hui, convertView);
        RelativeLayout ll_tuan = get(R.id.ll_tuan, convertView);
        TextView tv_tuan_edtail = get(R.id.tv_tuan_edtail, convertView);
        TextView tv_represent = get(R.id.tv_represent, convertView);
        if (isNotMine) {
            tv_represent.setVisibility(View.VISIBLE);
        } else {
            tv_represent.setVisibility(View.GONE);
        }
        SDViewUtil.hide(iv_tag_tuan);
        SDViewUtil.hide(ll_tuan);
        SDViewUtil.hide(iv_tag_quan);
        SDViewBinder.setImageView(model.getPreview(), iv_image);
        SDViewBinder.setTextView(tv_name, model.getShop_name());
        //评分
        if (!TextUtils.isEmpty(model.getAvg_grade())) {
            SDViewBinder.setRatingBar(rb_rating, Float.valueOf(model.getAvg_grade()));
        } else {
            SDViewBinder.setRatingBar(rb_rating, 0);
        }
        SDViewBinder.setTextView(tv_address, model.getAddress());
        SDViewBinder.setTextView(tv_distance, "");

        SDWeightLinearLayout.calculateWidth(ll_name_bar, new CalculateWidthListener() {

            @Override
            public void onResult(int width0, int width1, int widthParent, int widthLeft, ViewGroup parent) {
                if (widthLeft > 0) {
                    tv_name.setMaxWidth(widthLeft);
                }
            }
        });

    }

}