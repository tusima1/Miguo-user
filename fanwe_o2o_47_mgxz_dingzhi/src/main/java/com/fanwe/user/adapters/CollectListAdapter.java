package com.fanwe.user.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.StoreDetailActivity;
import com.fanwe.app.App;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.model.getShopAndUserCollect.Image;
import com.fanwe.user.model.getShopAndUserCollect.ModelShopAndUserCollect;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder mHolder;
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelShopAndUserCollect temp = datas.get(position);
                if ("1".equals(temp.getCollect_type())) {
                    //门店
                    Intent itemintent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(StoreDetailActivity.EXTRA_MERCHANT_ID, temp.getId());
                    bundle.putInt("type", 0);
                    itemintent.putExtras(bundle);
                    itemintent.setClass(App.getApplication(), StoreDetailActivity.class);
                    mContext.startActivity(itemintent);
                } else {
                    //网红小店
                }
            }
        });
        return convertView;
    }

    ModelShopAndUserCollect currModle;

    private void setData(Holder mHolder, final int position) {
        currModle = datas.get(position);
        SDViewBinder.setTextView(mHolder.tvName, currModle.getShop_name(), "");
        SDViewBinder.setTextView(mHolder.tvNum, "销售量" + currModle.getCons_count() + "  收藏数" + currModle.getColl_count());
        ImageLoader.getInstance().displayImage(currModle.getIndex_img(), mHolder.ivIcon);
        List<String> listData = new ArrayList<String>();
        if (!SDCollectionUtil.isEmpty(currModle.getImg_arr())) {
            for (Image img : currModle.getImg_arr()) {
                if (!TextUtils.isEmpty(img.getImg_url()))
                    listData.add(img.getImg_url());
            }
        }
        if (SDCollectionUtil.isEmpty(listData)) {
            mHolder.recyclerView.setVisibility(View.GONE);
        } else {
            mHolder.recyclerView.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mHolder.recyclerView.setLayoutManager(layoutManager);
            ImageAdapter adapter = new ImageAdapter(mContext, listData);
            mHolder.recyclerView.setAdapter(adapter);
        }
    }


    private static class Holder {
        View viewLine;
        TextView tvName, tvNum;
        CircleImageView ivIcon;
        RecyclerView recyclerView;
    }

}
