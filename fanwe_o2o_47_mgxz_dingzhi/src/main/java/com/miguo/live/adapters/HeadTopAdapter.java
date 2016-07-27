package com.miguo.live.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by didik on 2016/7/22.
 */
public class HeadTopAdapter extends RecyclerView.Adapter<HeadTopAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mData;

    public HeadTopAdapter(List<String> data, Context context){
        this.mContext=context;
        this.mData=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_act_live_headtop, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((CircleImageView) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCIV.setBackground(mContext.getResources().getDrawable(R.drawable.app_icon));
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mCIV;

        public ViewHolder(CircleImageView itemView) {
            super(itemView);
            mCIV = itemView;
        }
    }
}
