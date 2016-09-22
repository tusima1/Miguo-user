package com.fanwe.home.viewHolder;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.fanwe.customview.SnapUpCountDownTimerView;
import com.fanwe.home.views.customerView.XCRoundRectImageView;
import com.fanwe.o2o.miguo.R;

public class ViewHolderTimeLimit extends ViewHolder {

    public SnapUpCountDownTimerView tvTime;
    public XCRoundRectImageView ivImage;

    public ViewHolderTimeLimit(View view) {
        super(view);
        tvTime = (SnapUpCountDownTimerView) view.findViewById(R.id.item_home_recommend_event_tv_time);
        ivImage = (XCRoundRectImageView) view.findViewById(R.id.item_home_recommend_event_iv_image);
    }


}
