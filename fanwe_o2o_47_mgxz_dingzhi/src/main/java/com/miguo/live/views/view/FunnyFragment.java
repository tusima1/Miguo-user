package com.miguo.live.views.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.adapter.HomeIndexPageAdapter;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CallbackView2;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.HomeFragmentLiveList;
import com.fanwe.home.model.Room;
import com.fanwe.library.common.SDFragmentManager;
import com.fanwe.library.customview.SDSlidingPlayView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.view.RecyclerScrollView;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.adapter.LiveSortTypeAdapter;
import com.miguo.live.views.customviews.SpaceItemDecoration;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by Administrator on 2016/10/20.
 */
public class FunnyFragment  extends Fragment implements PtrHandler, RecyclerScrollView.OnRecyclerScrollViewListener,CallbackView2, SDEventObserver,CallbackView {


    PtrFrameLayout ptrFrameLayout;

    RecyclerScrollView recyclerScrollView;
   //直播分类
    private RecyclerView mSpvAd;
    /**
     * 大字体。
     */
    private TextView titleText;
    /**
     * 小字体。
     */
    private TextView summaryText;

    private List<ModelHomeClassifyList> mList = new ArrayList<>();
    private LiveSortTypeAdapter mAdapter;



    private SDFragmentManager mFragmentManager;
    /**
     * 直播列表。
     */
    private HomeFragmentLiveList mHomeFragmentLiveList;


    private LiveHttpHelper liveHelper;
    /**
     * 直播点播 数据列表。
     */
    private List<Room> rooms;

    private boolean isRefresh = true;
    private int pageNum = 1;
    private int pageSize = 10;
    private String typeLiveHome = "";

    private CommonHttpHelper commonHttpHelper;

    private SharedPreferences settings;
    private String interestingStr="";
    String cityId="";
    String currentData="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SDEventManager.register(this);
        cityId=AppRuntimeWorker.getCity_id();
        currentData = DateFormat.format("yyyy-MM-dd",new Date(System.currentTimeMillis())).toString();
        settings = getActivity().getSharedPreferences("miguo", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.act_funny, container, false);
            initView();// 控件初始化
        }
        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        requestLiveList();
    }

    /**
     * 取分类和欢迎的话。cityId-currentData-interesting 城市ID-当前日期-话语。
     */
    private void getTopData() {
        if (commonHttpHelper == null) {
            commonHttpHelper = new CommonHttpHelper(getActivity(), this);
        }
        commonHttpHelper.getHomeClassifyList();
        if(!TextUtils.isEmpty(interestingStr)){
            String[] list = interestingStr.split("-");
            if(list.length<3){
                settings.edit().putString("Interesting","").commit();
                commonHttpHelper.getInterestingString(cityId);
            }else{
                if(cityId.equals(list[0])&&currentData.equals(list[1])){
                    setInterestingStr(list[2]);
                }else{
                    settings.edit().putString("Interesting","").commit();
                    commonHttpHelper.getInterestingString(cityId);
                }
            }
        }else{
            commonHttpHelper.getInterestingString(cityId);
        }
    }

    private void initView() {
        interestingStr = settings.getString("Interesting","");
        mFragmentManager = new SDFragmentManager(getChildFragmentManager());

        liveHelper = new LiveHttpHelper(getActivity(),  this, "");
        initPtrLayout();
        init();
        if (mHomeFragmentLiveList == null) {
            mHomeFragmentLiveList = new HomeFragmentLiveList();
            mFragmentManager.replace(R.id.frag_home_new_fl_recommend_deals, mHomeFragmentLiveList);
        }
    }

    /**
     * 初始化下拉刷新，上拉翻页的控件。
     */
    protected void initPtrLayout() {
        ptrFrameLayout = (PtrFrameLayout)rootView.findViewById(R.id.ptr_layout);
        recyclerScrollView = (RecyclerScrollView)rootView.findViewById(R.id.recycler_scrollview);
        mSpvAd =(RecyclerView) rootView.findViewById(R.id.sort_type_list);
        titleText =(TextView)rootView.findViewById(R.id.title_text);
        summaryText = (TextView)rootView.findViewById(R.id.summary_text);

        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setEnabledNextPtrAtOnce(false);
        MaterialHeader ptrHead = new MaterialHeader(getContext());
        ptrHead.setPadding(0, 24, 0, 24);
        ptrFrameLayout.setHeaderView(ptrHead);
        ptrFrameLayout.addPtrUIHandler(ptrHead);
        /**
         * 设置下拉刷新回调
         */
        ptrFrameLayout.setPtrHandler(this);
        recyclerScrollView.setOnRecyclerScrollViewListener(this);
    }

    /**
     * 初始化分类。
     */

    protected void init() {
        initSlidingPlayView();
        bindData();
    }

    private void initSlidingPlayView() {

        mSpvAd.setHasFixedSize(true);

        LinearLayoutManager llmanager = new LinearLayoutManager(getContext());
        llmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSpvAd.setLayoutManager(llmanager);

        //设置间距
        mSpvAd.addItemDecoration(new SpaceItemDecoration(5));
        mSpvAd.setHasFixedSize(true);
        mSpvAd.setAdapter(mAdapter);


    }

    /**
     * 绑定分类的adapter 和 view.
     */
    private void bindData() {
        getTopData();
        mAdapter = new LiveSortTypeAdapter(mList, getActivity());
        mSpvAd.setAdapter(mAdapter);
    }

    public void parseInteresting(List<HashMap<String,String>> datas){
        if(datas==null||datas.size()<1){
            return;
        }else{
            String value = datas.get(0).get("value");
            if(!TextUtils.isEmpty(value)){
                setInterestingStr(value);
                settings.edit().putString("Interesting",cityId+"-"+currentData+"-"+value).commit();

            }
        }
    }
    /**
     * 显示有趣页欢迎的话。
     * @param interestingStr
     */
    public void setInterestingStr(String interestingStr) {
        if (interestingStr == null || interestingStr.length() < 1) {
            return;
        } else {
            String titleStr = "";
            String summaryStr = "";
            if (interestingStr.length() <= 6) {
                titleStr = interestingStr;
                summaryStr = "";
            } else {
                if (interestingStr.indexOf(",")==6||interestingStr.indexOf("，") == 6) {
                    titleStr = interestingStr.substring(0, 7);
                    summaryStr = interestingStr.substring(7, interestingStr.length());
                } else {
                    titleStr = interestingStr.substring(0, 6);
                    summaryStr = interestingStr.substring(6, interestingStr.length());
                }
                if (summaryStr.endsWith(",") || summaryStr.endsWith("。")) {
                    summaryStr = summaryStr.substring(0, summaryStr.length() - 1);
                }
            }
            titleText.setText(titleStr);
            summaryText.setText(summaryStr);
        }
    }
    /**
     * 获取分类数据。
     * @param datas
     */

    public void setHomeClassifyList(List<ModelHomeClassifyList> datas) {
        mList.clear();
        if (!SDCollectionUtil.isEmpty(datas)) {
            mList.addAll(datas);
        }

        if (mAdapter != null) {
            mAdapter.setmData(mList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void requestLiveList() {
        if (liveHelper != null) {
//        cityId = "1cf07dd4-51e0-48fc-b829-2f0a6de0b536"; //1cf07dd4-51e0-48fc-b829-2f0a6de0b536
        liveHelper.getLiveList(pageNum, pageSize, typeLiveHome, "",cityId);
        }
    }
    /**
     * 直播列表
     *
     * @param datas
     */
    public void getLiveList(ArrayList<Room> datas) {

        if (SDCollectionUtil.isEmpty(datas)) {
            rooms = null;
        }else{
            if(datas.size()>=pageSize){
                setPageNum(this.pageNum++);
            }
        }

        rooms = datas;
        //直播列表
        mHomeFragmentLiveList.updateView(isRefresh, rooms);
//                    mPtrsvAll.onRefreshComplete();
        loadComplete();
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    @Override
    public void onEvent(SDBaseEvent sdBaseEvent) {

    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case CITY_CHANGE:
                onRefreshBegin(ptrFrameLayout);
                break;
            case HOME_TYPE_CHANGE:
                ModelHomeClassifyList bean = (ModelHomeClassifyList) event.getData();
                if (mHomeFragmentLiveList != null) {
                    mHomeFragmentLiveList.updateTitle(bean.getName());
                }

                typeLiveHome = bean.getId();
                requestLiveList();
                break;

            default:
                break;
        }
    }

    @Override
    public void onEventBackgroundThread(SDBaseEvent sdBaseEvent) {

    }

    @Override
    public void onEventAsync(SDBaseEvent sdBaseEvent) {

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return recyclerScrollView.canRefresh();
    }


    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        pageNum = 1;
        isRefresh = true;
        requestLiveList();
    }

    /**
     * 加载更多
     */
    @Override
    public void onScrollToEnd() {
        isRefresh = false;
        requestLiveList();
    }
    public void loadComplete(){
        ptrFrameLayout.refreshComplete();
        recyclerScrollView.loadComplite();
    }
    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
//        Log.d("FunnyFragment","L:"+l +"  t:"+t +" oldL:"+oldl +"  oldt:"+oldt);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, final List datas) {
        if (LiveConstants.LIVE_LIST.equals(method)) {
            //直播列表
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    getLiveList((ArrayList<Room>) datas);
                }
            });
        } else if (CommonConstants.HOME_CLASSIFY_LIST.equals(method)) {
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setHomeClassifyList(datas);
                }
            });
        }else if(CommonConstants.INTERESTING.equals(method)){
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parseInteresting(datas);
                }
            });
        }
    }
    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

    @Override
    public void onDestroy() {
        SDEventManager.unregister(this);
        super.onDestroy();
    }
}