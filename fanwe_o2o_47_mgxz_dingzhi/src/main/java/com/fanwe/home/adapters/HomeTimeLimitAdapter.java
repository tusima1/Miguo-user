package com.fanwe.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.EventDetailActivity;
import com.fanwe.app.App;
import com.fanwe.customview.SnapUpCountDownTimerView;
import com.fanwe.home.viewHolder.ViewHolderTimeLimit;
import com.fanwe.model.EventModel_List;
import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * 限时特惠适配器
 */
public class HomeTimeLimitAdapter extends RecyclerView.Adapter<ViewHolderTimeLimit> {

    private Context mContext;
    private List<EventModel_List> datas;

    public HomeTimeLimitAdapter(Context mContext, List<EventModel_List> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    @Override
    public void onBindViewHolder(ViewHolderTimeLimit holder, int position) {
        EventModel_List model = datas.get(position);
        ImageLoader.getInstance().displayImage(model.getSpecial_icon(), holder.ivImage);
        setTime(holder.tvTime);
    }

    @Override
    public ViewHolderTimeLimit onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_recommend_event, parent, false);
        ViewHolderTimeLimit vh = new ViewHolderTimeLimit(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.EXTRA_EVENT_ID, 1);
                mContext.startActivity(intent);
            }
        });
        return vh;
    }

    private void setTime(SnapUpCountDownTimerView tv_time) {
        tv_time.setTime(1, 55, 3);//设置小时，分钟，秒。注意不能大于正常值，否则会抛出异常
        tv_time.start();//开始倒计时
    }
}