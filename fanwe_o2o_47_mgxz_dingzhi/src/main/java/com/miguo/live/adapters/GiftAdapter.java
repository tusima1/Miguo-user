package com.miguo.live.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.GiftListener;
import com.miguo.live.model.getGiftInfo.GiftListBean;
import com.miguo.utils.DisplayUtil;
import com.miguo.utils.MGLog;

import java.util.List;

/**
 * Created by didik on 2016/9/11.
 */
public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {


    private List<GiftListBean> mData;
    private int selectedPosition = -1;
    private GiftListener mListener;

    public GiftAdapter(List<GiftListBean> mData) {
        this.mData = mData;
    }

    public void setData(List<GiftListBean> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_gift_item,
                null);
        ViewHolder holder=new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /*bind data*/
        GiftListBean info = mData.get(position);

//        String  url = DisplayUtil.qiniuUrlExchange(info.getIcon(),60,60);
        SDViewBinder.setImageView(info.getIcon(),holder.iv_img);
        holder.tv_name.setText(info.getName());
        holder.tv_price.setText(info.getPrice()+"米果钻");

        boolean isSelected = selectedPosition == position;
        holder.itemView.setSelected(isSelected);
        if (mListener!=null && isSelected){
            mListener.onItemSelected(position,info);
            MGLog.e("onItemSelected:"+position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPosition);
                selectedPosition=position;
                notifyItemChanged(selectedPosition);
                if (mListener!=null){
                    mListener.onItemClickListener(v,position);
                    MGLog.e("onItemClickListener:"+position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData==null ? 0 :mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_img;
        private TextView tv_name;
        private TextView tv_price;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_img= (ImageView) itemView.findViewById(R.id.iv_img);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            tv_price= (TextView) itemView.findViewById(R.id.tv_price);
        }
    }

    public void setGiftListener(GiftListener listener){
        this.mListener=listener;
    }
}
