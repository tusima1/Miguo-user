package com.miguo.live.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.MyItemClickListenerRedType;
import com.miguo.live.model.getHandOutRedPacket.ModelHandOutRedPacket;
import com.miguo.live.viewHolder.ViewHolderRedType;

import java.util.List;


/**
 * Created by didik on 2016/7/22.
 */
public class RedTypeAdapter extends RecyclerView.Adapter<ViewHolderRedType> {

    private Context mContext;
    private List<ModelHandOutRedPacket> mData;

    private MyItemClickListenerRedType mItemClickListener;

    public RedTypeAdapter(List<ModelHandOutRedPacket> data, Context context) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public void onBindViewHolder(ViewHolderRedType holder, int position) {
        ModelHandOutRedPacket modelHandOutRedPacket = mData.get(position);
        String countTextValue = "";
        String count = modelHandOutRedPacket.getRed_packets();
        String type = modelHandOutRedPacket.getRed_packet_type();
        String amount = modelHandOutRedPacket.getRed_packet_amount();
        if ("1".equals(type)) {
            holder.typeText.setText(amount + "折");
        } else if ("2".equals(type)) {
            holder.typeText.setText(amount + "元");
        } else {
            return;
        }
        if (Integer.valueOf(count) > 999) {
            countTextValue = "余(999+)";
        } else {
            countTextValue = "余(" + count + "+)";
        }
        holder.countText.setText(countTextValue);
        if (modelHandOutRedPacket.isChecked()) {
            //橙色
            holder.itemView.setBackgroundResource(R.drawable.bg_orange_small);
            holder.typeText.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.countText.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            //白色
            holder.itemView.setBackgroundResource(R.drawable.shape_cricle_bg_white_gray);
            holder.typeText.setTextColor(mContext.getResources().getColor(R.color.text_line));
            holder.countText.setTextColor(mContext.getResources().getColor(R.color.text_line));
        }
        if (Integer.valueOf(count) <= 0) {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public ViewHolderRedType onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.red_packet_item, parent, false);
        ViewHolderRedType vh = new ViewHolderRedType(itemView, mItemClickListener);
        return vh;
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListenerRedType listener) {
        this.mItemClickListener = listener;
    }

}