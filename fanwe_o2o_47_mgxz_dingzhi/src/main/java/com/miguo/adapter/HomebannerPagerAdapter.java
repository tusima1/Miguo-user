package com.miguo.adapter;

import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.common.ImageLoaderManager;
import com.fanwe.customview.MGGradientView;
import com.fanwe.library.utils.SDViewBinder;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiHomeActivity;
import com.miguo.category.fragment.HiHomeFragmentCategory;
import com.miguo.category.fragment.HomeCategoryUtils;
import com.miguo.definition.AdspaceParams;
import com.miguo.entity.AdspaceListBean;
import com.miguo.entity.BannerTypeModel;
import com.miguo.factory.AdspaceTypeFactory;
import com.miguo.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlh on 2016/12/30.
 */

public class HomebannerPagerAdapter extends PagerAdapter {

    List<AdspaceListBean.Result.Body> bodys;
    HiBaseActivity activity;
    HiHomeFragmentCategory category;

    public HomebannerPagerAdapter(HiHomeFragmentCategory category, List<AdspaceListBean.Result.Body> bodys) {
        this.bodys=bodys;
        this.activity = category.getActivity();
        this.category = category;
    }

    @Override
    public int getCount() {
        return null == bodys ? 0 : bodys.size();
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
        LinearLayout group = new LinearLayout(container.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        group.setLayoutParams(params);
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params2);
        group.addView(imageView);
        displayImage(imageView, position);
        container.addView(group);
        setListener(group, position);
        return group;
    }

    private void setListener(View view, final int position){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage(position);
            }
        });
    }

    public void clickImage(int position){
        if(bodys == null){
            return;
        }

        if(bodys.get(position) == null){
            return;
        }
        BannerTypeModel model = HomeCategoryUtils.parseTypeJson(bodys.get(position).getType_id());


        if(bodys.get(position).getType().equals(AdspaceParams.BANNER_LIVE_LIST)){
            category.onActionLiveList();
            return;
        }
        if(bodys.get(position).getType().equals(AdspaceParams.BANNER_SHOP_LIST)){
            category.onActionShopList(model.getCate_id(),model.getTid());
            return;
        }
        String paramValue = model.getId();
        if(TextUtils.isEmpty(paramValue)){
            paramValue = model.getUrl();
        }
        AdspaceTypeFactory.clickWidthType(bodys.get(position).getType(), getActivity(), paramValue);
    }

    private void displayImage(ImageView image, int position){
        String url = bodys.get(position).getIcon();
        if(!TextUtils.isEmpty(url)&&url.startsWith("http://")){
            url = DisplayUtil.qiniuUrlExchange(url,375,210);

        }
        SDViewBinder.setImageView(url, image, ImageLoaderManager.getOptionsNoResetViewBeforeLoading());
    }

    public HiBaseActivity getActivity() {
        return activity;
    }
}
