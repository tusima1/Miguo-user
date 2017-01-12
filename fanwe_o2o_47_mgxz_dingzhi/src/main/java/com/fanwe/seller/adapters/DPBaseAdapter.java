package com.fanwe.seller.adapters;

/**
 * Created by Administrator on 2017/1/11.
 */
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fanwe.seller.views.customize.DPAdapterViewHolder;

import java.util.List;


/**
 * Created by whiskeyfei on 15-7-9.
 */
public abstract class DPBaseAdapter<T> extends BaseAdapter {

    protected List<T> mDataList;
    protected Context mContext;
    protected int mLayoutId;

//    protected DPOnItemChildLongClickListener mOnItemChildLongClickListener;
//	protected DPOnItemChildClickListener mOnItemChildClickListener;

    public DPBaseAdapter(Context context, List<T> list, int layoutId) {
        mContext = context;
        mDataList = list;
        mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DPAdapterViewHolder holder = DPAdapterViewHolder.get(mContext, convertView, parent, mLayoutId, position);
//        holder.setOnItemChildClickListener(mOnItemChildClickListener);
//        holder.setOnItemChildLongClickListener(mOnItemChildLongClickListener);
        convert(holder,getItem(position));
        return holder.getConvertView();
    }

//    public void setOnItemChildLongClickListener(DPOnItemChildLongClickListener l) {
//        this.mOnItemChildLongClickListener = l;
//    }
//
//	public void setOnItemChildClickListener(DPOnItemChildClickListener l){
//		this.mOnItemChildClickListener = l;
//	}

    public void removeItem(int position) {
        this.mDataList.remove(position);
        this.notifyDataSetChanged();
    }

    private void addItem(int position, T model) {
        this.mDataList.add(position, model);
        this.notifyDataSetChanged();
    }

    public void addFirstItem(T model) {
        this.addItem(0, model);
    }

    public void addLastItem(T model){
        this.addItem(this.mDataList.size(), model);
    }

    public abstract void convert(DPAdapterViewHolder holder,T t);
}

