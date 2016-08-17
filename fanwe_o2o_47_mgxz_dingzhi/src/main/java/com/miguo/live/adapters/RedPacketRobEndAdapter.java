package com.miguo.live.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by didik on 2016/8/17.
 */
public class RedPacketRobEndAdapter extends RecyclerView.Adapter<RedPacketRobEndAdapter.ViewHolder> {


    public RedPacketRobEndAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder=new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_re_red_rob_end,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_name.setText("item:"+position);
        holder.tv_right.setText(position+"折");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView userImage;
        public TextView tv_name;
        public TextView tv_right;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_right=(TextView) itemView.findViewById(R.id.tv_right);
            userImage= (CircleImageView) itemView.findViewById(R.id.civ_user_image);
        }
    }
}
