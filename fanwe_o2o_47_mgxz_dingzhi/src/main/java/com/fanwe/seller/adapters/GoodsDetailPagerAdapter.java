package com.fanwe.seller.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fanwe.customview.MGGradientView;
import com.fanwe.library.utils.SDViewBinder;
import com.miguo.utils.DisplayUtil;

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
        FrameLayout frameLayout=new FrameLayout(container.getContext());

        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        SDViewBinder.setImageView(mData.get(position),imageView);
        frameLayout.addView(imageView);

        MGGradientView gradient=new MGGradientView(container.getContext());
        int height = DisplayUtil.dp2px(container.getContext(), 100);
//        int margin = DisplayUtil.dp2px(container.getContext(), 24);
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,height);
        params.gravity= Gravity.TOP;
//        params.setMargins(0,margin,0,0);
        gradient.setGravity(MGGradientView.TOP);
        gradient.setLayoutParams(params);
        frameLayout.addView(gradient);

        container.addView(frameLayout);

        return frameLayout;
    }
}
