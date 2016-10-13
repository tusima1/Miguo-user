package com.fanwe.home.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.TimeLimitActivity;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.dao.barry.GetSpecialListDao;
import com.fanwe.dao.barry.impl.GetSpecialListDaoImpl;
import com.fanwe.dao.barry.view.GetSpecialListView;
import com.fanwe.fragment.BaseFragment;
import com.fanwe.model.EventModel_List;
import com.fanwe.model.Index_indexActModel;
import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.view.HomeTuanTimeLimitView;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 限时特惠
 * Created by Administrator on 2016/8/11.
 */
public class FragmentHomeTimeLimit extends BaseFragment implements GetSpecialListView, HomeTuanTimeLimitView.OnTimeLimitClickListener{
//    @ViewInject(R.id.recyclerView_fragment_time_limit)
//    private RecyclerView mRecyclerView;

    @ViewInject(R.id.home_tuan)
    private HomeTuanTimeLimitView homeTuanHorizontalScrollView;
    private List<EventModel_List> mListModel = new ArrayList<EventModel_List>();
    GetSpecialListDao getSpecialListDao;
    String tag = "FragmentHomeTimeLimit";

    PtrFrameLayout parent;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_home_time_limit);
    }

    @Override
    protected void init() {
        super.init();
//        bindData();
        initData();
    }

    public void setmIndexModel(Index_indexActModel indexModel) {
        this.mListModel = indexModel.getSpecial().getList();
    }

    private void initData(){
        getSpecialListDao = new GetSpecialListDaoImpl(this);
    }

    public void onRefresh(){
        getSpecialListDao.getSpecialList(
                AppRuntimeWorker.getCity_id(),
                BaiduMapManager.getInstance().getBDLocation().getLongitude() + "",
                BaiduMapManager.getInstance().getBDLocation().getLatitude() + "",
                "0");
    }


    @Override
    public void getSpecialListSuccess(final SpecialListModel.Result result) {
        if(result != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    homeTuanHorizontalScrollView.init(result);
                    homeTuanHorizontalScrollView.setParent(parent);
                    homeTuanHorizontalScrollView.setOnTimeLimitClickListener(FragmentHomeTimeLimit.this);
                }
            });
        }
    }

    @Override
    public void getSpecialListLoadmoreSuccess(SpecialListModel.Result result) {

    }

    @Override
    public void getSpecialListError(String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void onTimeLimitClick() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), TimeLimitActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    public void setParent(PtrFrameLayout parent) {
        this.parent = parent;
    }
}
