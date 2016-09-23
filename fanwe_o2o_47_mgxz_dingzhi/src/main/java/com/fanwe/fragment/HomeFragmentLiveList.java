package com.fanwe.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.adapter.HomeLiveListAdapter;
import com.fanwe.adapter.barry.MainActivityHomeFragmentLiveListAdapter;
import com.fanwe.adapter.barry.MainActivityHomeFragmentTuanAdapter;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.customview.SDGridViewInScroll;
import com.fanwe.home.model.Host;
import com.fanwe.home.model.Room;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.model.CommandGroupBuyBean;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.utils.DataFormat;
import com.fanwe.view.HomeLiveFragmentRecyclerView;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.view.PlayBackActivity;
import com.miguo.utils.NetWorkStateUtil;
import com.tencent.imcore.Context;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class HomeFragmentLiveList extends BaseFragment implements CallbackView {
    private View view;
    private TextView tvTitle;
    private ArrayList<Room> datas = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private LiveHttpHelper liveHttpHelper;

    /**
     * 直播列表
     */
    HomeLiveFragmentRecyclerView recyclerView;
    MainActivityHomeFragmentLiveListAdapter mainActivityHomeFragmentLiveListAdapter;

    /**
     * 团购列表
     * @param savedInstanceState
     */
    HomeLiveFragmentRecyclerView recyclerView2;
    MainActivityHomeFragmentTuanAdapter mainActivityHomeFragmentTuanAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        // 可以初始化除了view之外的东西
        liveHttpHelper = new LiveHttpHelper(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            initView(inflater, container);
            initRecyclerView();
            preView();
        }
        /**
         * 缓存的rootView需要判断是否已经被加过parent，
         * 如果有parent需要从mView删除，要不然会发生这个mView已经有parent的错误。
         */
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    private void preView() {
        mainActivityHomeFragmentLiveListAdapter = new MainActivityHomeFragmentLiveListAdapter(getActivity(), datas);
        recyclerView.setAdapter(mainActivityHomeFragmentLiveListAdapter);

        mainActivityHomeFragmentTuanAdapter = new MainActivityHomeFragmentTuanAdapter(getActivity(), new ArrayList());
        recyclerView2.setAdapter(mainActivityHomeFragmentTuanAdapter);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_home_live_list, container, false);
        recyclerView = (HomeLiveFragmentRecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView2 = (HomeLiveFragmentRecyclerView) view.findViewById(R.id.recyclerview_tuan);
        tvTitle = (TextView) view.findViewById(R.id.tv_title_live_list);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void updateTitle(String title) {
        if (tvTitle != null) {
            if (TextUtils.isEmpty(title)) {
                title = "";
            }
            tvTitle.setText(title);
        }
    }

    public void updateView(boolean isRefresh, List<Room> rooms) {
        if (isRefresh) {
            datas.clear();
        }
        if (!SDCollectionUtil.isEmpty(rooms))
            datas.addAll(rooms);
//        mLiveViewAdapter.notifyDataSetChanged();
        mainActivityHomeFragmentLiveListAdapter.notifyDataSetChanged(rooms);
        updateRecyclerViewHeight();
    }


    public void onRefreshTuan(boolean refresh,List<CommandGroupBuyBean.Result.Body> bodys){
//        List<CommandGroupBuyBean.Result.Body> bodys = new ArrayList<>();
//        for(int i = 0; i<12; i++){
//            bodys.addAll(bodys1);
//        }
        if(bodys != null){
            if(refresh){
                mainActivityHomeFragmentTuanAdapter.notifyDataSetChanged(bodys);
            }else {
                mainActivityHomeFragmentTuanAdapter.notifyDataSetChangedLoadmore(bodys);
            }
            updateRecyclerView2Height();
        }
    }

    private void updateRecyclerViewHeight(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mainActivityHomeFragmentLiveListAdapter.getHeight());
        params.setMargins(0, 0, 0, BaseUtils.dip2px(getContext(), 10));
        recyclerView.setLayoutParams(params);
    }

    private void updateRecyclerView2Height(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mainActivityHomeFragmentTuanAdapter.getHeight());
        params.setMargins(0, 0, 0, BaseUtils.dip2px(getContext(), 10));
        recyclerView2.setLayoutParams(params);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
    }

    @Override
    public void onFailue(String responseBody) {

    }
}
