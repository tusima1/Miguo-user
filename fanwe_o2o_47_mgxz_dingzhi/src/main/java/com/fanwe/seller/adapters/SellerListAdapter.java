package com.fanwe.seller.adapters;

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

import com.fanwe.app.App;
import com.fanwe.library.customview.SDWeightLinearLayout;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getBusinessListings.ModelBusinessListings;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.SDDistanceUtil;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.utils.DisplayUtil;

import java.util.List;

/**
 * 商家列表适配器
 * Created by qiang.chen on 2017/1/5.
 */
public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.MViewHolder> {

    private Activity activity;
    private List<ModelBusinessListings> listData;

    public SellerListAdapter(Activity activity, List<ModelBusinessListings> mList) {
        super();
        this.activity = activity;
        this.listData = mList;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {

        View view = View.inflate(viewGroup.getContext(),
                R.layout.item_merchant_list, null);
        // 创建一个ViewHolder
        MViewHolder holder = new MViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(MViewHolder mViewHolder, int position) {
        final ModelBusinessListings model = listData.get(position);
        mViewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemintent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(HiShopDetailActivity.EXTRA_MERCHANT_ID, model.getId());
                bundle.putInt("type", 0);
                itemintent.putExtras(bundle);
                itemintent.setClass(App.getApplication(), HiShopDetailActivity.class);
                activity.startActivity(itemintent);
            }
        });
        setView(model, mViewHolder, position);
    }

    private void setView(ModelBusinessListings model, final MViewHolder mViewHolder, int position) {
        //代言按钮
        if ("1".equals(model.getIs_endorsement())) {
            SDViewUtil.show(mViewHolder.iv_represent);
        } else {
            SDViewUtil.hide(mViewHolder.iv_represent);
        }
        if (DataFormat.toInt(model.getTuan_num()) > 0) {
            SDViewUtil.show(mViewHolder.iv_tag_tuan);
            SDViewUtil.show(mViewHolder.ll_tuan);
            SDViewUtil.show(mViewHolder.viewHolderImage);
        } else {
            SDViewUtil.hide(mViewHolder.iv_tag_tuan);
            SDViewUtil.hide(mViewHolder.ll_tuan);
            SDViewUtil.hide(mViewHolder.viewHolderImage);
        }
        SDViewBinder.setTextView(mViewHolder.tv_tuan_edtail, model.getTuan_name());

        String url = model.getIndex_img();
        if (!TextUtils.isEmpty(url) && url.startsWith("http://")) {
            url = DisplayUtil.qiniuUrlExchange(url, 100, 100);
        }
        SDViewBinder.setImageView(url, mViewHolder.iv_image);
        SDViewBinder.setTextView(mViewHolder.tv_name, model.getShop_name());
        SDViewBinder.setRatingBar(mViewHolder.rb_rating, DataFormat.toFloat(model.getAvg_grade()));
        if (!TextUtils.isEmpty(model.getAvg_grade())) {
            SDViewBinder.setTextView(mViewHolder.tv_rating, model.getAvg_grade() + "分");
        } else {
            SDViewBinder.setTextView(mViewHolder.tv_rating, "");
        }

        SDViewBinder.setTextView(mViewHolder.tv_address, model.getAddress());
        SDViewBinder.setTextView(mViewHolder.tv_distance, SDDistanceUtil.getMGDistance(DataFormat.toDouble(model.getDistance())));

        SDWeightLinearLayout.calculateWidth(mViewHolder.ll_name_bar, new SDWeightLinearLayout.CalculateWidthListener() {

            @Override
            public void onResult(int width0, int width1, int widthParent, int widthLeft, ViewGroup parent) {
                if (widthLeft > 0) {
                    mViewHolder.tv_name.setMaxWidth(widthLeft);
                }
            }
        });

    }

    public class MViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image, iv_tag_tuan, iv_represent;
        public View viewHolderImage;
        public TextView tv_name, tv_distance, tv_rating, tv_address, tv_tuan_edtail;
        public RelativeLayout ll_name_bar, ll_tuan;
        public RatingBar rb_rating;
        public LinearLayout layoutItem;

        public MViewHolder(View view) {
            super(view);
            this.iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            this.iv_tag_tuan = (ImageView) itemView.findViewById(R.id.iv_tag_tuan);
            this.iv_represent = (ImageView) itemView.findViewById(R.id.iv_represent);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            this.tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            this.tv_rating = (TextView) itemView.findViewById(R.id.tv_rating);
            this.tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            this.tv_tuan_edtail = (TextView) itemView.findViewById(R.id.tv_tuan_edtail);
            this.ll_name_bar = (RelativeLayout) itemView.findViewById(R.id.ll_name_bar);
            this.ll_tuan = (RelativeLayout) itemView.findViewById(R.id.ll_tuan);
            this.layoutItem = (LinearLayout) itemView.findViewById(R.id.layout_item_merchant_list);
            this.viewHolderImage = (View) itemView.findViewById(R.id.view_image_holder);
            this.rb_rating = (RatingBar) itemView.findViewById(R.id.rb_rating);
        }
    }

}