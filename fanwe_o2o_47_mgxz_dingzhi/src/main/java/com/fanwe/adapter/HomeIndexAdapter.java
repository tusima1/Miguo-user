package com.fanwe.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.constant.EnumEventTag;
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
        if (model.is_checked()) {
            ImageLoader.getInstance().displayImage(model.getImg(), ivImg);
        } else {
            String imageUrl = model.getImg();
            if(!TextUtils.isEmpty(model.getUncheck_img())){
                imageUrl = model.getUncheck_img();
            }
            ImageLoader.getInstance().displayImage(imageUrl, ivImg);
        }

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCheckedItem(model);
                SDEventManager.post(model, EnumEventTag.HOME_TYPE_CHANGE.ordinal());
            }
        });
        return convertView;
    }
    /**
     * 修改选中的状态。
     *
     * @param model
     */

    public void changeCheckedItem(ModelHomeClassifyList model) {
        String id = model.getId();
        for (int i = 0; i < mListModel.size(); i++) {
            ModelHomeClassifyList entity = mListModel.get(i);
            if (entity != null && entity.getId().equals(id)) {
                entity.setIs_checked(true);
            } else {
                entity.setIs_checked(false);
            }
        }
    }

}