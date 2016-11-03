package com.miguo.category.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.common.ImageLoaderManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.ShopDetailPagerItemFragmet;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/17.
 */
public class ShopDetailItemFragmentCategory extends FragmentCategory{

    @ViewInject(R.id.image)
    ImageView image;

    public ShopDetailItemFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {

    }

    @Override
    protected void setFragmentListener() {

    }

    @Override
    protected void init() {
        setBannerParams();
    }

    private void setBannerParams(){
        try{
            int width = getScreenWidth();
            int height = width * 420 / 750;
            LinearLayout.LayoutParams params = getLineaLayoutParams(width, height);
            image.setLayoutParams(params);
            SDViewBinder.setImageView(getFragment().getBanner().getImage_url(), image, ImageLoaderManager.getOptionsNoResetViewBeforeLoading());
        }catch (Exception e){

        }

    }

    public ShopDetailPagerItemFragmet getFragment(){
        return (ShopDetailPagerItemFragmet)fragment;
    }

}
