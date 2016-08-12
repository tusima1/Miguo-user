package com.miguo.live.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.model.UserRedPacketInfo;

import java.util.List;


/**
 * Created by didik on 2016/7/29.
 * 直播底部的viewpager的红包页面
 */
public class PagerRedPacketAdapter extends RecyclerView.Adapter<PagerRedPacketAdapter.ViewHolder> {


    public List<UserRedPacketInfo> mdatas;
    /**
     * 填充数据
     */
    public PagerRedPacketAdapter() {

    }

    @Override
    public PagerRedPacketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_pager_red_packet_recycler, null);
        PagerRedPacketAdapter.ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PagerRedPacketAdapter.ViewHolder holder, int position) {
        UserRedPacketInfo userRedPacketInfo = mdatas.get(position);
        //红包类型 1 折扣券。2 优惠券。"red_packet_type":"1",
        if(userRedPacketInfo!=null) {
            String str = "";
            holder.mTv_BigNum.setText(userRedPacketInfo.getRed_packet_amount());
            switch (userRedPacketInfo.getRed_packet_type()){
                case "1":
                    str="折";
                    break;
                case "2":
                    str="优惠券";
                    break;
                default:
                    break;
            }
            holder.packet_type.setText(str);
            holder.mTv_Title.setText(userRedPacketInfo.getRed_packet_name()==null?"":userRedPacketInfo.getRed_packet_name());
            holder.mTv_Time.setText(userRedPacketInfo.getAvailable_time_start()==null?"":userRedPacketInfo.getAvailable_time_start()+"-"+userRedPacketInfo.getAvailable_time_end()==null?"":userRedPacketInfo.getAvailable_time_end());
        }
    }

    @Override
    public int getItemCount() {
        return this.mdatas==null?0:mdatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTv_Time;//有效期
        public TextView mTv_TitleTag;//类型(专属优惠)
        public TextView mTv_Title;//标题
        public TextView mTv_Content;//内容
        /**
         * 红包类型。
         */
        public TextView packet_type;//
        /**
         * 金额 内容。
         */
        public TextView mTv_BigNum;//打几折?例如8折,就是8,只能是数字

        public ViewHolder(View itemView) {
            super(itemView);
            mTv_Time = ((TextView) itemView.findViewById(R.id.tv_time));
            mTv_TitleTag = ((TextView) itemView.findViewById(R.id.tv_tag));
            mTv_BigNum = ((TextView) itemView.findViewById(R.id.tv_big_num));
            packet_type = ((TextView) itemView.findViewById(R.id.packet_type));
            mTv_Title = ((TextView) itemView.findViewById(R.id.tv_title));
            mTv_Content = ((TextView) itemView.findViewById(R.id.tv_content));
        }
    }

    public List<UserRedPacketInfo> getMdatas() {
        return mdatas;
    }

    public void setMdatas(List<UserRedPacketInfo> mdatas) {
        this.mdatas = mdatas;
        notifyItemRangeChanged(0,getItemCount());
    }
}
