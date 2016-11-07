package com.miguo.category.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.fanwe.common.ImageLoaderManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.definition.AdspaceParams;
import com.miguo.factory.AdspaceTypeFactory;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.fragment.HomeBannerFragmet;
import com.miguo.listener.fragment.HomeBannerFragmentListener;
import com.miguo.ui.view.HomeBannerImageView;
import com.miguo.ui.view.HomeBannerViewPager;

/**
 * Created by zlh/狗蛋哥/Barry on 2016/10/17.
 * 首页banner
 */
public class HomeBannerFragmentCategory extends FragmentCategory{

    @ViewInject(R.id.image)
    HomeBannerImageView image;

    public HomeBannerFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {
        listener = new HomeBannerFragmentListener(this);
    }

    @Override
    protected void setFragmentListener() {
        image.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        setBannerParams();
    }

    private void setBannerParams(){
        int width = getScreenWidth();
        int height = width * 420 / 750;
        LinearLayout.LayoutParams params = getLineaLayoutParams(width, height);
        image.setLayoutParams(params);
        SDViewBinder.setImageView(getFragment().getBanner().getIcon(), image, ImageLoaderManager.getOptionsNoResetViewBeforeLoading());
    }

    public void clickImage(){
        if(getFragment() == null){
            return;
        }

        if(getFragment().getBanner() == null){
            return;
        }

        if(getFragment().getBanner().getType() == null){
            return;
        }

        if(getFragment().getBanner().getType().equals(AdspaceParams.BANNER_LIVE_LIST)){
            getHomeBannerViewPager().handlerActionLiveList();
            return;
        }
        if(getFragment().getBanner().getType().equals(AdspaceParams.BANNER_SHOP_LIST)){
            getHomeBannerViewPager().handlerActionShopList(getFragment().getBanner().getType_id());
            return;
        }
        AdspaceTypeFactory.clickWidthType(getFragment().getBanner().getType(), getActivity(), getFragment().getBanner().getType_id());
    }

    public HomeBannerViewPager getHomeBannerViewPager(){
        return (HomeBannerViewPager)view.getParent();
    }

    public HomeBannerFragmet getFragment(){
        return (HomeBannerFragmet)fragment;
    }

}
