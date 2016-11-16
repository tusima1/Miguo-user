package com.miguo.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/11/11.
 */

public abstract class BaseRVAdapter<T, H extends BaseRVViewHolder> extends RecyclerView.Adapter<BaseRVViewHolder> implements View.OnClickListener {
    protected final Context context;
    private final int layoutResId;
    protected List<T> data;
    private OnItemClickListener mOnItemClickListener;
    private View emptyLayout;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public BaseRVAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseRVAdapter(Context context, int layoutResId, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : data;
        this.context = context;
        this.layoutResId = layoutResId;
    }
    public BaseRVAdapter(Context context, int layoutResId, List<T> data,View emptyLayout) {
        this.data = data == null ? new ArrayList<T>() : data;
        this.context = context;
        this.layoutResId = layoutResId;
        this.emptyLayout=emptyLayout;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }
    @Override
    public BaseRVViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        view.setOnClickListener(this);
        return new BaseRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseRVViewHolder helper, int position) {
        helper.itemView.setTag(position);
        T item = getItem(position);
        bindView((H)helper, item);
    }
    public void setEmptyLayout(View emptyView){
        this.emptyLayout=emptyView;
    }
    protected void toggleEmptyLayout(){
        if (emptyLayout==null)return;
        if (data.isEmpty() && emptyLayout.getVisibility()==View.GONE){
            emptyLayout.setVisibility(View.VISIBLE);
        }else if (!data.isEmpty() && emptyLayout.getVisibility()==View.VISIBLE){
            emptyLayout.setVisibility(View.GONE);
        }
    }
    protected abstract void bindView(H helper, T item);

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
