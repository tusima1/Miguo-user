package com.miguo.live.views.view.frag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.fanwe.app.App;
import com.fanwe.network.HttpCallback;
import com.fanwe.network.OkHttpUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.GuideLiveOutRVAdapter;
import com.miguo.model.guidelive.GuideOutModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
        requestGuideLives();
    }

    private void bindDataView() {
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




    private void requestGuideLives(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("tab_id", "");
        params.put("page_size", "10");
        params.put("page", "1");
        params.put("method", "InterestingGuideVideo");
        OkHttpUtil.getInstance().get(params, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                Log.e("test",response);
            }

            @Override
            public void onFinish() {

            }
        });
    }

}
