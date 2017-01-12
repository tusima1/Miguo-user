package com.miguo.ui.view.floatdropdown.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.floatdropdown.interf.OnRvItemClickListener;

import java.util.List;

/**
 * Created by didik 
 * Created time 2017/1/9
 * Description: 
 */

public class SearchGuideAdapter extends RecyclerView.Adapter<SearchGuideAdapter.ViewHolder> {
    private List<String> words;
    private OnRvItemClickListener<String> onRvItemClickListener;

    public SearchGuideAdapter(List<String> words) {
        this.words = words;
    }

    public void setWords(List<String> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rv_search,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String name = words.get(position);
        holder.tv_name.setText(name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRvItemClickListener!=null){
                    onRvItemClickListener.onRvItemClick(v,holder.getAdapterPosition(),name);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return words ==null ? 0 :words.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public void setOnRvItemClickListener(OnRvItemClickListener<String> onRvItemClickListener) {
        this.onRvItemClickListener = onRvItemClickListener;
    }
}
