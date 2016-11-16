package com.miguo.live.views.view.frag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.GuideLiveOutRVAdapter;
import com.miguo.live.model.guidelive.GuideOutModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by didik on 2016/11/16.
 */

public class GuideLiveFragment extends BaseFragment {

    private RecyclerView mRV;
    private View mEmptyLayout;
//    private LinearLayout ll_root_Layout;

    @Override
    protected int setLayoutResId() {
        return R.layout.frag_guide_live;
    }

    @Override
    protected void initView(View content) {
        mRV = (RecyclerView) content.findViewById(R.id.recyclerview);
        mEmptyLayout = content.findViewById(R.id.common_empty);
//        ll_root_Layout = (LinearLayout) content.findViewById(R.id.ll_root);
    }

    @Override
    protected void startFlow() {
//        addActGuideLayout();
        List<GuideOutModel> data=new ArrayList<>();
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        data.add(new GuideOutModel());
        GuideLiveOutRVAdapter adapter=new GuideLiveOutRVAdapter();
        adapter.setEmptyLayout(mEmptyLayout);
        adapter.setData(data);
        mRV.setHasFixedSize(true);
        mRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRV.setAdapter(adapter);
    }

//    private void addActGuideLayout() {
//        ActGuideLayout mGuideLayout=new ActGuideLayout(getContext());
//        ll_root_Layout.addView(mGuideLayout);
//    }
}
