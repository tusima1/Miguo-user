package com.miguo.live.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.MyItemClickListener;
import com.miguo.live.model.getAuthTag.ModelAuthTag;
import com.miguo.live.viewHolder.ViewHolderAuthTag;

import java.util.List;


/**
 * Created by didik on 2016/7/22.
 */
public class AuthTagAdapter extends RecyclerView.Adapter<ViewHolderAuthTag> {

    private Context mContext;
    private List<ModelAuthTag> datas;

    private MyItemClickListener mItemClickListener;

    public AuthTagAdapter(Context context, List<ModelAuthTag> data) {
        this.mContext = context;
        this.datas = data;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    @Override
    public void onBindViewHolder(ViewHolderAuthTag holder, int position) {
        if (position == (datas.size() - 1)) {
            //换一批
            holder.tvChange.setVisibility(View.VISIBLE);
            holder.tvTag.setVisibility(View.GONE);
        } else {
            //普通标签
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(datas.get(position).getName());
            holder.tvChange.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolderAuthTag onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auth_tag, parent, false);
        ViewHolderAuthTag vh = new ViewHolderAuthTag(itemView, mItemClickListener);
        return vh;
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

}