package com.miguo.live.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.MyItemClickListenerRedNum;
import com.miguo.live.model.getHandOutRedPacket.ModelRedNum;
import com.miguo.live.viewHolder.ViewHolderRedNum;

import java.util.List;

/**
 * Created by didik on 2016/8/6.
 */
public class RedNumGridAdapter extends RecyclerView.Adapter<ViewHolderRedNum> {
    private Context mContext;
    private List<ModelRedNum> datas;

    private MyItemClickListenerRedNum mItemClickListener;

    public RedNumGridAdapter(Context context, List<ModelRedNum> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    @Override
    public ViewHolderRedNum onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.act_live_grid_red_num, parent, false);
        ViewHolderRedNum vh = new ViewHolderRedNum(itemView, mItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolderRedNum holder, int position) {
        ModelRedNum modelRedNum = datas.get(position);
        String num = modelRedNum.getName();
        holder.tv_num.setText(num);

        if (modelRedNum.isChecked()) {
            //橙色
            holder.itemView.setBackgroundResource(R.drawable.bg_orange_small);
            holder.tv_num.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            //白色
            holder.itemView.setBackgroundResource(R.drawable.shape_cricle_bg_white_gray);
            holder.tv_num.setTextColor(mContext.getResources().getColor(R.color.text_line));
        }

    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListenerRedNum listener) {
        this.mItemClickListener = listener;
    }
}