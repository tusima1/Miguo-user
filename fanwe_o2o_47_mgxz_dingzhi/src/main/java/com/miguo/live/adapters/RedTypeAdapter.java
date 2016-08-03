package com.miguo.live.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.model.RedPacketInfo;

import java.util.List;


    /**
     * Created by didik on 2016/7/22.
     */
    public class RedTypeAdapter extends RecyclerView.Adapter<RedTypeAdapter.ViewHolder> {

        private Context mContext;
        private List<RedPacketInfo> mData;

        private View.OnClickListener  mOnclickListener;

        public RedTypeAdapter(List<RedPacketInfo> data, Context context){
            this.mContext=context;
            this.mData=data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.red_packet_item, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if(mData.get(position)!=null) {
                String typeTextValue = "";
                String countTextValue="";
                int count = mData.get(position).getCount();
                if (mData.get(position).getType() > 0) {
                    typeTextValue= mData.get(position).getType()+"折";
                }
                if(count >999){
                    countTextValue ="余(999+)";
                }else{
                    countTextValue ="余(" +count +"+)";
                }
                holder.typeText.setText(typeTextValue);
                holder.countText.setText(countTextValue);
                if(count<=0){
                    mData.get(position).setClickable(false);
                    holder.itemView.setBackgroundColor(Color.GRAY);
                }
                holder.setClickListener(mOnclickListener);
            }


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public FrameLayout red_line;
            public TextView typeText;
            public TextView countText;
            public View.OnClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);
                red_line = (FrameLayout)itemView.findViewById(R.id.red_line);
                typeText = (TextView)itemView.findViewById(R.id.type_text);
                countText = (TextView) itemView.findViewById(R.id.count_text);
                red_line.setOnClickListener(clickListener);

            }

            public void setClickListener(View.OnClickListener clickListener) {
                this.clickListener = clickListener;
            }
        }
    }