package com.fanwe.seller.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.utils.SDViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/10/14.
 */

public class GoodsDetailPagerAdapter extends PagerAdapter {

    List<String> mData=new ArrayList<>();
//    List<ImageView> mData=new ArrayList<>();
    public GoodsDetailPagerAdapter(List<String> mData) {
        this.mData=mData;
    }

    @Override
    public int getCount() {
        return mData==null ? 0:mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        SDViewBinder.setImageView(mData.get(position),imageView);
        container.addView(imageView);
        return imageView;
    }
}
