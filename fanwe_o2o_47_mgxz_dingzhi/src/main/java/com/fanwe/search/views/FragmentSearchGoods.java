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
import com.miguo.definition.FilterIndexParams;
import com.miguo.definition.PageSize;
import com.miguo.entity.SingleMode;
import com.miguo.groupon.listener.IDataInterface;
import com.miguo.groupon.views.FragmentFeaturedGroupon;
import com.miguo.groupon.views.FragmentGoodsList;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.ui.view.floatdropdown.helper.DropDownPopHelper;
import com.miguo.ui.view.floatdropdown.interf.OnDropDownListener;
import com.miguo.ui.view.floatdropdown.view.FakeDropDownMenu;

import java.util.List;

/**
 * 搜索商品
 * Created by qiang.chen on 2017/1/6.
 */

public class FragmentSearchGoods extends Fragment implements IDataInterface ,OnDropDownListener{
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
        if (fragmentGoods != null) {
            fragmentGoods.keyword = keyword;
            fragmentGoods.refresh();
        }
    }

    public void initSearchKeyword(String keyword){
        this.keywords = keyword;
    }

    private void setListener() {
        popHelper.setOnDropDownListener(this);
        layoutEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentGoods != null) {
                    fragmentGoods.refresh();
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
        if (fragmentGoods != null) {
            fragmentGoods.refresh();
        }
    }

    private void handleItemSelectNearBy(Pair<SingleMode, SingleMode> pair){
        fragmentGoods.area_one = isFirstEmpty(pair) ? "" : pair.first.getSingleId();
        fragmentGoods.area_two = isSecondEmpty(pair) ? "" : pair.second.getSingleId();
    }

    private void handleItemSelectCategory(Pair<SingleMode, SingleMode> pair){
        fragmentGoods.category_one = isFirstEmpty(pair) ? "" : pair.first.getSingleId();
        fragmentGoods.category_two = isSecondEmpty(pair) ? "" : pair.second.getSingleId();
    }

    private void handleItemSelectsIntel(Pair<SingleMode, SingleMode> pair){
        fragmentGoods.sort_type = isFirstEmpty(pair) ? "" : pair.first.getSingleId();
    }

    private void handleItemSelectFilter(List<SingleMode> items){
        String ids = "";
        if(!SDCollectionUtil.isEmpty(items)){
            for(int i = 0; i < items.size(); i++){
                ids = ids + (i == 0 ? "" : ",") + items.get(i).getSingleId();
            }
        }
        fragmentGoods.filter = ids;
    }

    private boolean isSecondEmpty(Pair<SingleMode, SingleMode> pair){
        return null == pair.second;
    }

    private boolean isFirstEmpty(Pair<SingleMode, SingleMode> pair){
        return null == pair.first;
    }

    private FragmentGoodsList fragmentGoods;
    private FragmentFeaturedGroupon fragmentFeatured;
    private android.support.v4.app.FragmentManager fm;

    /**
     * Fragment初始化
     */
    private void initFragment() {
        fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentGoods == null) {
            fragmentGoods = new FragmentGoodsList();
            fragmentGoods.setIDataInterface(this);
            fragmentGoods.keyword = keywords;
            fragmentGoods.refresh();
            ft.add(R.id.content_frag_search, fragmentGoods);
        } else {
            ft.show(fragmentGoods);
        }
        ft.commit();
    }

    @Override
    public void verifyData(boolean isValid) {
        if (isValid) {
            layoutEmpty.setVisibility(View.GONE);
            addGoodsFragment();
        } else {
            layoutEmpty.setVisibility(View.VISIBLE);
            addFeaturedFragment();
        }
    }

    /**
     * 商品列表
     */
    private void addGoodsFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentFeatured != null) {
            ft.hide(fragmentFeatured);
        }
        if (fragmentGoods != null) {
            ft.show(fragmentGoods);
        }
        ft.commit();
    }

    /**
     * 为你推荐
     */
    private void addFeaturedFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentGoods != null && !fragmentGoods.isHidden()) {
            ft.hide(fragmentGoods);
        }
        if(fragmentFeatured == null){
            fragmentFeatured = new FragmentFeaturedGroupon();
            ft.add(R.id.content_frag_search, fragmentFeatured);
        }
        ft.show(fragmentFeatured);
        fragmentFeatured.refresh();
        ft.commit();
    }
}
