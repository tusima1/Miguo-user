package com.miguo.live.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.fanwe.home.views.customerView.XCRoundRectImageView;
import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

/**
 * 拜访照片适配器
 */

public class VisitImgAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<String> datas;

    private AdddMore mAdddMore;

    public void setAddMore(AdddMore mAdddMore) {
        this.mAdddMore = mAdddMore;
    }

    /**
     * 点击加载更多
     */
    public interface AdddMore {
        void add();

        void delete(int position);
    }

    public VisitImgAdapter(Context mContext, LayoutInflater layoutInflater, ArrayList<String> datas) {
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
        Holder mHolder;
        if (null == convertView) {
            mHolder = new Holder();
            convertView = inflater.inflate(
                    R.layout.layout_visit_img_item, null);
            mHolder.ivImg = (XCRoundRectImageView) convertView.findViewById(R.id.iv_visit_img_item);
            mHolder.ivAdd = (ImageView) convertView.findViewById(R.id.iv_add_img_item);
            mHolder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete_img_item);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    private void setData(Holder mHolder, final int position) {
        String path = datas.get(position);
        if ("add".equals(path)) {
            mHolder.ivAdd.setVisibility(View.VISIBLE);
            mHolder.ivImg.setVisibility(View.GONE);
            mHolder.ivDelete.setVisibility(View.GONE);
            mHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdddMore.add();
                }
            });
        } else {
            mHolder.ivAdd.setVisibility(View.GONE);
            mHolder.ivImg.setVisibility(View.VISIBLE);
            mHolder.ivDelete.setVisibility(View.GONE);
            mHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdddMore.delete(position);
                }
            });
            File file = new File(path);
            if (file.exists()) {
                ImageLoader.getInstance().displayImage("file:///" + path, mHolder.ivImg);
            }
        }
    }

    private static class Holder {
        XCRoundRectImageView ivImg;
        ImageView ivAdd, ivDelete;
    }

}
