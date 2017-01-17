package com.fanwe.search.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.fragment.FragmentShopList;
import com.miguo.definition.FilterIndexParams;
import com.miguo.entity.SingleMode;
import com.miguo.groupon.listener.IDataInterface;
import com.miguo.groupon.views.FragmentFeaturedGroupon;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.view.floatdropdown.helper.DropDownPopHelper;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownListener;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;

import java.util.List;

/**
 * 搜索商家
 * Created by qiang.chen on 2017/1/6.
 */

public class FragmentSearchShop extends Fragment implements IDataInterface ,OnDropDownListener{
    private LinearLayout layoutEmpty;
    private FakeDropDownMenu ddm;
    private DropDownPopHelper popHelper;
    String keywords;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search_goods, container, false);
        preWidget(view);
        initFragment();
        setListener();
        return view;
    }

    private void preWidget(View view) {
        layoutEmpty = (LinearLayout) view.findViewById(R.id.layout_empty_frag_search);
        ddm = ((FakeDropDownMenu) view.findViewById(R.id.ddm_act_search));
        popHelper = new DropDownPopHelper(getActivity(), ddm);
    }

    public void search(String keyword) {
        this.keywords = keyword;
        if (fragmentShop != null) {
            fragmentShop.keyword = keyword;
            fragmentShop.refresh();
        }
    }

    private void setListener() {
        popHelper.setOnDropDownListener(this);
        layoutEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentShop != null) {
                    fragmentShop.refresh();
                }
            }
        });
    }



    /**
     *
     * @param index 1/2/3/4
     * @param pair 附近、分类、排序的左右数据id
     * @param items 筛选的数据只有在index为4的时候
     */
    @Override
    public void onItemSelected(int index, Pair<SingleMode, SingleMode> pair, List<SingleMode> items) {
        switch (index){
            case FilterIndexParams.NEAR_BY:
                handleItemSelectNearBy(pair);
                break;
            case FilterIndexParams.CATEGORY:
                handleItemSelectCategory(pair);
                break;
            case FilterIndexParams.INTEL:
                handleItemSelectsIntel(pair);
                break;
            case FilterIndexParams.FILTER:
                handleItemSelectFilter(items);
                break;
        }
        onRefreshShopList();
        popHelper.dismiss();
    }

    private void onRefreshShopList(){
        if (fragmentShop != null) {
            fragmentShop.refresh();
        }
    }

    private void handleItemSelectNearBy(Pair<SingleMode, SingleMode> pair){
        fragmentShop.area_one = isFirstEmpty(pair) ? "" : pair.first.getSingleId();
        fragmentShop.area_two = isSecondEmpty(pair) ? "" : pair.second.getSingleId();
    }

    private void handleItemSelectCategory(Pair<SingleMode, SingleMode> pair){
        fragmentShop.category_one = isFirstEmpty(pair) ? "" : pair.first.getSingleId();
        fragmentShop.category_two = isSecondEmpty(pair) ? "" : pair.second.getSingleId();
    }

    private void handleItemSelectsIntel(Pair<SingleMode, SingleMode> pair){
        fragmentShop.sort_type = isFirstEmpty(pair) ? "" : pair.first.getSingleId();
    }

    private void handleItemSelectFilter(List<SingleMode> items){
        String ids = "";
        if(!SDCollectionUtil.isEmpty(items)){
            for(int i = 0; i < items.size(); i++){
                ids = ids + (i == 0 ? "" : ",") + items.get(i).getSingleId();
            }
        }
        fragmentShop.filter = ids;
    }

    private boolean isSecondEmpty(Pair<SingleMode, SingleMode> pair){
        return null == pair.second;
    }

    private boolean isFirstEmpty(Pair<SingleMode, SingleMode> pair){
        return null == pair.first;
    }

    private FragmentShopList fragmentShop;
    private FragmentFeaturedGroupon fragmentFeatured;
    private android.support.v4.app.FragmentManager fm;

    /**
     * Fragment初始化
     */
    private void initFragment() {
        fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentShop == null) {
            fragmentShop = new FragmentShopList();
            fragmentShop.setIDataInterface(this);
            fragmentShop.keyword = keywords;
            fragmentShop.refresh();
            ft.add(R.id.content_frag_search, fragmentShop);
        } else {
            ft.show(fragmentShop);
        }
        ft.commit();
    }

    @Override
    public void verifyData(boolean isValid) {
        if (isValid) {
            layoutEmpty.setVisibility(View.GONE);
            addShopFragment();
        } else {
            layoutEmpty.setVisibility(View.VISIBLE);
            addFeaturedFragment();
        }
    }


    /**
     * 商家列表
     */
    private void addShopFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentFeatured != null && !fragmentFeatured.isHidden()) {
            ft.hide(fragmentFeatured);
        }
        if (fragmentShop.isHidden()) {
            ft.show(fragmentShop);
        }
        ft.commit();
    }

    /**
     * 为你推荐
     */
    private void addFeaturedFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentShop != null && !fragmentShop.isHidden()) {
            ft.hide(fragmentShop);
        }
        fragmentFeatured = new FragmentFeaturedGroupon();
        fragmentFeatured.refresh();
        ft.add(R.id.content_frag_search, fragmentFeatured);
        ft.commit();
    }

}
