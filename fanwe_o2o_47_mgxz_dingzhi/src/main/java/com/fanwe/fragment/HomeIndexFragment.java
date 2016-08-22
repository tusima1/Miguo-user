package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.adapter.HomeIndexPageAdapter;
import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.library.customview.SDSlidingPlayView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页分类fragment
 *
 * @author js02
 */
public class HomeIndexFragment extends BaseFragment {

    @ViewInject(R.id.spv_content)
    private SDSlidingPlayView mSpvAd;

    private List<ModelHomeClassifyList> mList = new ArrayList<>();
    private HomeIndexPageAdapter mAdapter;

    public void setHomeClassifyList(List<ModelHomeClassifyList> datas) {
        mList.clear();
        if (!SDCollectionUtil.isEmpty(datas))
            mList.addAll(datas);
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_home_index);
    }

    @Override
    protected void init() {
        super.init();
        initSlidingPlayView();
        bindData();


    }

    private void initSlidingPlayView() {
        mSpvAd.setmImageNormalResId(R.drawable.ic_small_dot_normal);
        mSpvAd.setmImageSelectedResId(R.drawable.ic_small_dot_slecet);
    }

    private void bindData() {
        if (!toggleFragmentView(mList)) {
            return;
        }

        if (mList.size() > 10) {
            SDViewUtil.setViewMarginBottom(mSpvAd.mVpgContent, SDViewUtil.dp2px(20));
        }

        mAdapter = new HomeIndexPageAdapter(SDCollectionUtil.splitList(mList, 10), getActivity());
        mSpvAd.setAdapter(mAdapter);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }
}