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

import com.fanwe.o2o.miguo.R;
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

public class FragmentSearchGoods extends Fragment implements IDataInterface {
    private LinearLayout layoutEmpty;
    private FakeDropDownMenu ddm;
    private DropDownPopHelper popHelper;

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

    private void setListener() {
        popHelper.setOnDropDownListener(new OnDropDownListener() {
            @Override
            public void onItemSelected(int index, Pair<SingleMode, SingleMode> pair, List<SingleMode> items) {
                if (index == 4) {
                    if (items != null) {
                        MGToast.showToast("数量: " + items.size());
                    }

                } else {
                    SingleMode first = pair.first;
                    SingleMode second = pair.second;
                    String singleId1 = "无";
                    String singleId2 = "无";
                    if (first != null) {
                        singleId1 = first.getSingleId();
                    }
                    if (second != null) {
                        singleId2 = second.getSingleId();
                    }
                    MGToast.showToast("一级id: " + singleId1 + "   二级id: " + singleId2);

                    String titleName = "";
                    if (pair.second != null) {
                        titleName = pair.second.getName();
                    } else {
                        titleName = pair.first.getName();
                    }
                    popHelper.setTitleText(index, titleName);
                }

                popHelper.dismiss();
            }
        });
        layoutEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentGoods != null) {
                    fragmentGoods.refresh();
                }
            }
        });
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
        if (fragmentFeatured != null && !fragmentFeatured.isHidden()) {
            ft.hide(fragmentFeatured);
        }
        if (fragmentGoods.isHidden()) {
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
        fragmentFeatured = new FragmentFeaturedGroupon();
        fragmentFeatured.refresh();
        ft.add(R.id.content_frag_search, fragmentFeatured);
        ft.commit();
    }
}
