package com.fanwe.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.adapter.barry.MainActivityHomeFragmentLiveListAdapter;
import com.fanwe.adapter.barry.MainActivityHomeFragmentTuanAdapter;
import com.fanwe.base.CallbackView;
import com.fanwe.home.model.Room;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.model.CommandGroupBuyBean;
import com.fanwe.o2o.miguo.R;
import com.fanwe.view.HomeLiveFragmentRecyclerView;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.utils.BaseUtils;
import com.tencent.imcore.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 狗蛋哥/zlh on 2016/7/25.
 */
public class HomeFragmentLiveList extends BaseFragment implements CallbackView {
    private View view;
    private TextView tvTitle;
    private ArrayList<Room> datas = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private LiveHttpHelper liveHttpHelper;
    private LinearLayout ll_empty;

    /**
     * 直播列表
     */
    HomeLiveFragmentRecyclerView recyclerView;
    MainActivityHomeFragmentLiveListAdapter mainActivityHomeFragmentLiveListAdapter;



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

    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_home_live_list, container, false);
        recyclerView = (HomeLiveFragmentRecyclerView) view.findViewById(R.id.recyclerview);
        ll_empty = (LinearLayout)view.findViewById(R.id.ll_empty);
        tvTitle = (TextView) view.findViewById(R.id.tv_title_live_list);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        if (!SDCollectionUtil.isEmpty(rooms)) {
            datas.addAll(rooms);
        }
        if(datas==null||datas.size()<1){
            ll_empty.setVisibility(View.VISIBLE);
        }else{
            ll_empty.setVisibility(View.GONE);
        }
        if(mainActivityHomeFragmentLiveListAdapter != null){
            mainActivityHomeFragmentLiveListAdapter.notifyDataSetChanged(rooms);
            updateRecyclerViewHeight();
        }

    }



    private void updateRecyclerViewHeight(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mainActivityHomeFragmentLiveListAdapter.getHeight());
        params.setMargins(0, 0, 0, BaseUtils.dip2px(getContext(), 10));
        recyclerView.setLayoutParams(params);
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
