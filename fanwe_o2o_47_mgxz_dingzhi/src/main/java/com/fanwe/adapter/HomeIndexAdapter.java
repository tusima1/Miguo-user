package com.fanwe.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunday.eventbus.SDEventManager;

import java.util.List;

public class HomeIndexAdapter extends SDBaseAdapter<ModelHomeClassifyList> {

    public HomeIndexAdapter(List<ModelHomeClassifyList> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, final ModelHomeClassifyList model) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_home_index, null);
        }
        ImageView ivImg = (ImageView) convertView.findViewById(R.id.item_home_index_iv_image);
        TextView tvName = ViewHolder.get(convertView, R.id.item_home_index_tv_name);

        SDViewUtil.setViewWidth(ivImg, SDViewUtil.getScreenWidth() / 6);
        SDViewUtil.setViewHeight(ivImg, SDViewUtil.getScreenWidth() / 6);

        SDViewBinder.setTextView(tvName, model.getName());
        ImageLoader.getInstance().displayImage(model.getImg(), ivImg);

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SDEventManager.post(model, EnumEventTag.HOME_TYPE_CHANGE.ordinal());
            }
        });
        return convertView;
    }

}