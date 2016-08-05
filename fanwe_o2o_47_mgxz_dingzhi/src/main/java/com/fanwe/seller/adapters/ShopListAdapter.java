package com.fanwe.seller.adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.customview.SDWeightLinearLayout;
import com.fanwe.library.customview.SDWeightLinearLayout.CalculateWidthListener;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getShopList.ModelShopList;

import java.util.List;

public class ShopListAdapter extends SDSimpleBaseAdapter<ModelShopList> {

    public ShopListAdapter(List<ModelShopList> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_merchant_list;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, final ModelShopList model) {
        ImageView iv_image = get(R.id.iv_image, convertView);
        LinearLayout ll_name_bar = get(R.id.ll_name_bar, convertView);
        ImageView iv_tag_tuan = get(R.id.iv_tag_tuan, convertView);
        ImageView iv_tag_quan = get(R.id.iv_tag_quan, convertView);
        final TextView tv_name = get(R.id.tv_name, convertView);
        TextView tv_distance = get(R.id.tv_distance, convertView);
        RatingBar rb_rating = get(R.id.rb_rating, convertView);
        TextView tv_address = get(R.id.tv_address, convertView);
        ImageView iv_tag_hui = get(R.id.iv_tag_hui, convertView);
        LinearLayout ll_tuan = get(R.id.ll_tuan, convertView);
        LinearLayout ll_hui = get(R.id.ll_hui, convertView);
        TextView tv_hui_edtail = get(R.id.tv_hui_edtail, convertView);
        TextView tv_tuan_edtail = get(R.id.tv_tuan_edtail, convertView);


//        if (model.getDeal_count() > 0) {
//            SDViewUtil.show(iv_tag_tuan);
//            SDViewUtil.show(ll_tuan);
//        } else {
        SDViewUtil.hide(iv_tag_tuan);
        SDViewUtil.hide(ll_tuan);
//        }

//        if (model.getYouhui_count() > 0) {
//            SDViewUtil.show(iv_tag_quan);
//        } else {
        SDViewUtil.hide(iv_tag_quan);
//        }
        if ("0".equals(model.getOffline())) {
            SDViewUtil.hide(iv_tag_hui);
            SDViewUtil.hide(ll_hui);
        } else {
            SDViewUtil.show(iv_tag_hui);
            SDViewUtil.show(ll_hui);
        }
        /*if(model.getDiscount_pay() >0 && model.getSalary_money() >= 1)
        {
			SDViewUtil.show(iv_tag_hui);
			SDViewUtil.show(ll_hui);
		}
		else if(model.getSalary_money() >= 1 && model.getDiscount_pay() <= 0){
			SDViewUtil.show(iv_tag_hui);
			SDViewUtil.hide(ll_hui);
		}
		else if(model.getDiscount_pay() >0 && model.getSalary_money() <= 1){
			SDViewUtil.show(iv_tag_hui);
			SDViewUtil.show(ll_hui);
		}
		else if(model.getDiscount_pay() <= 0 && model.getSalary_money() <= 1 ){
			SDViewUtil.hide(iv_tag_hui);
			SDViewUtil.hide(ll_hui);
		}*/
//        if (model.getDiscount_pay() <= 0) {
//            SDViewBinder.setTextView(tv_hui_edtail, "买单立减");
//        } else {
//            BigDecimal bd1 = new BigDecimal((100 - model.getDiscount_pay()) / 10.0);
//            bd1 = bd1.setScale(1, BigDecimal.ROUND_HALF_UP);
//            SDViewBinder.setTextView(tv_hui_edtail, bd1 + "折 （买单立减）");
//        }

//        SDViewBinder.setTextView(tv_tuan_edtail, model.getDeal_name());
        SDViewBinder.setImageView(model.getPreview(), iv_image);
        SDViewBinder.setTextView(tv_name, model.getShop_name());
        //评分
        if (!TextUtils.isEmpty(model.getAvg_grade())) {
            SDViewBinder.setRatingBar(rb_rating, Float.valueOf(model.getAvg_grade()));
        } else {
            SDViewBinder.setRatingBar(rb_rating, 0);
        }
        SDViewBinder.setTextView(tv_address, model.getAddress());
//        SDViewBinder.setTextView(tv_distance, model.getDistanceFormat());
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