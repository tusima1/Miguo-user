package com.fanwe.home.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.TimeLimitActivity;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.dao.barry.GetSpecialListDao;
import com.fanwe.dao.barry.impl.GetSpecialListDaoImpl;
import com.fanwe.dao.barry.view.GetSpecialListView;
import com.fanwe.fragment.BaseFragment;
import com.fanwe.model.EventModel_List;
import com.fanwe.model.SpecialListModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.view.HomeTuanTimeLimitView;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 限时特惠
 * Created by zlh/狗蛋哥/Barry on 2016/8/11.
 */
public class FragmentHomeTimeLimit extends BaseFragment implements GetSpecialListView, HomeTuanTimeLimitView.OnTimeLimitClickListener{

    @ViewInject(R.id.home_tuan)
    private HomeTuanTimeLimitView homeTuanHorizontalScrollView;
    private List<EventModel_List> mListModel = new ArrayList<EventModel_List>();
    GetSpecialListDao getSpecialListDao;
    String tag = "FragmentHomeTimeLimit";

    PtrFrameLayout parent;
    @ViewInject(R.id.content_layout)
    LinearLayout contentLayout;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_home_time_limit);
    }

    @Override
    protected void init() {
        super.init();
        initData();
        onRefresh();
    }

    private void initData(){
        getSpecialListDao = new GetSpecialListDaoImpl(this);
    }

    public void onRefresh(){
        try{
            getSpecialListDao.getSpecialList("",
                    AppRuntimeWorker.getCity_id(),
                    BaiduMapManager.getInstance().getBDLocation().getLongitude() + "",
                    BaiduMapManager.getInstance().getBDLocation().getLatitude() + "",
                    "0");
        }catch (Exception e){
            getSpecialListDao.getSpecialList("",
                    "",
                    "",
                    "",
                    "0");
        }
    }


    @Override
    public void getSpecialListSuccess(String httpUuid,final SpecialListModel.Result result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(result != null){
                    contentLayout.setVisibility(View.GONE);
                    homeTuanHorizontalScrollView.removeAllViews();
                    if(result.getCount_down() != null){
                        contentLayout.setVisibility(View.VISIBLE);
                        homeTuanHorizontalScrollView.init(result);
                        homeTuanHorizontalScrollView.setParent(parent);
                        homeTuanHorizontalScrollView.setOnTimeLimitClickListener(FragmentHomeTimeLimit.this);
                    }
                }
            }
        });

    }

    @Override
    public void getSpecialListLoadmoreSuccess(String httpUuid,SpecialListModel.Result result) {

    }

    @Override
    public void getSpecialListError(String httpUuid,String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void getSpecialListNoData(String httpUuid,String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                contentLayout.setVisibility(View.GONE);
            }
        });
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
