package com.fanwe.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.common.ImageLoaderManager;
import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;

import java.util.List;

public class DistributionMarketCateAdapter extends SDSimpleBaseAdapter<ModelHomeClassifyList> {

    public DistributionMarketCateAdapter(List<ModelHomeClassifyList> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent) {
        return R.layout.item_home_index;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, ModelHomeClassifyList model) {
        ImageView ivImage = get(R.id.item_home_index_iv_image, convertView);
        TextView tvName = get(R.id.item_home_index_tv_name, convertView);

        SDViewUtil.setViewWidth(ivImage, SDViewUtil.getScreenWidth() / 6);
        SDViewUtil.setViewHeight(ivImage, SDViewUtil.getScreenWidth() / 6);

        SDViewBinder.setTextView(tvName, model.getName());
        SDViewBinder.setImageView(model.getImg(), ivImage, ImageLoaderManager.getOptionsNoResetViewBeforeLoading());

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 根据分类id刷新接口

            }
        });

    }

}
