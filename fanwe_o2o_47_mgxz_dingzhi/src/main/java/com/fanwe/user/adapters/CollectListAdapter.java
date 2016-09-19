package com.fanwe.user.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getShopAndUserCollect.ModelShopAndUserCollect;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的收藏适配器
 */

public class CollectListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ModelShopAndUserCollect> datas;

    public CollectListAdapter(Context mContext, LayoutInflater layoutInflater, List<ModelShopAndUserCollect> datas) {
        this.mContext = mContext;
        this.inflater = layoutInflater;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder = null;
        if (null == convertView) {
            mHolder = new Holder();
            convertView = inflater.inflate(R.layout.item_collect_list, null);
            mHolder.ivIcon = (CircleImageView) convertView.findViewById(R.id.iv_icon_item_collect);
            mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name_item_collect);
            mHolder.tvNum = (TextView) convertView.findViewById(R.id.tv_num_item_collect);
            mHolder.viewLine = (View) convertView.findViewById(R.id.view_item_collect);
            mHolder.recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerview_item_collect);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    ModelShopAndUserCollect currModle;

    private void setData(Holder mHolder, final int position) {
        currModle = datas.get(position);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHolder.recyclerView.setLayoutManager(layoutManager);
        List<String> listData = new ArrayList<String>();
        for (int i = 0; i < 5; ++i) {
            String uBean = "http://img15.3lian.com/2015/f1/119/d/47.jpg";
            listData.add(uBean);
        }
        ImageAdapter adapter = new ImageAdapter(mContext, listData);
        mHolder.recyclerView.setAdapter(adapter);
    }


    private static class Holder {
        View viewLine;
        TextView tvName, tvNum;
        CircleImageView ivIcon;
        RecyclerView recyclerView;
    }

}
