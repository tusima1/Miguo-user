package com.miguo.live.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

/**
 * Created by didik on 2016/8/6.
 */
public class RedNumGridAdapter extends RecyclerView.Adapter<RedNumGridAdapter.ViewHolder> {

    private List<String> mData;

    public RedNumGridAdapter(List<String> datas) {
        this.mData=datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .act_live_grid_red_num, null);
        ViewHolder holder=new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String num = mData.get(position);
        holder.tv_num.setText(""+num);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MGToast.showToast(""+num);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mData==null?0:mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_num;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_num= (TextView) itemView.findViewById(R.id.tv_num);
        }
    }
}
