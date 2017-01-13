package com.fanwe.seller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.TypeEntity;
import com.miguo.entity.SearchCateConditionBean;

import java.util.List;


/**
 * 一级分类名称adapter.
 * Created by zhouhy on 2017/1/5.
 */

public class TypeHorizontalScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> mDatas=null;

    public TypeHorizontalScrollViewAdapter(Context context, List<SearchCateConditionBean.ResultBean.BodyBean.CategoryListBean> mDatas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount() {
        return mDatas==null?0:mDatas.size();
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(mDatas==null||position>=mDatas.size()){
            return null;
        }
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.type_item, parent, false);
            viewHolder.mText = (TextView) convertView
                    .findViewById(R.id.textview1);
            viewHolder.divline = (View)convertView.findViewById(R.id.divline);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mText.setText(mDatas.get(position).getName());

        return convertView;
    }

    public class ViewHolder {
        View divline;
        TextView mText;
    }
}

