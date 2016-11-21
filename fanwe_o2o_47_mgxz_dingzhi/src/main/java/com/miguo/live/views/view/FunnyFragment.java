package com.miguo.live.views.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.base.CallbackView2;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.HomeFragmentLiveList;
import com.fanwe.library.common.SDFragmentManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.ChineseCharClassifier;
import com.fanwe.view.RecyclerScrollView;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.PageModel;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.adapter.LiveSortTypeAdapter;
import com.miguo.live.views.customviews.SpaceItemDecoration;
import com.miguo.ui.view.FunnyTypeHorizantalScrollView;
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
public class FunnyFragment  extends Fragment implements PtrHandler, RecyclerScrollView.OnRecyclerScrollViewListener,CallbackView2, SDEventObserver,CallbackView, FunnyTypeHorizantalScrollView.OnFunnyTypeChangeListener {


    PtrFrameLayout ptrFrameLayout;

    RecyclerScrollView recyclerScrollView;
    //直播分类
//    private RecyclerView mSpvAd;
    /**
     * 大字体。
     */
    private TextView titleText;
    /**
     * 小字体。
     */
    private TextView summaryText;

    private List<ModelHomeClassifyList> mList = new ArrayList<>();

    FunnyTypeHorizantalScrollView funnyType;

    private SDFragmentManager mFragmentManager;
    /**
     * 直播列表。
     */
    private HomeFragmentLiveList mHomeFragmentLiveList;


    private LiveHttpHelper liveHelper;
    /**
     * 直播点播 数据列表。
     */
    private List<ModelRoom> rooms;

    private boolean isRefresh = true;
    private int pageNum = 1;
    private int pageSize = 10;
    private int maxPageTotal=1;
    private String typeLiveHome = "";

    private CommonHttpHelper commonHttpHelper;

    private SharedPreferences settings;
    private String interestingStr = "";
    String cityId = "";
    String currentData = "";
    /**
     * 是否加载过。

     */
    boolean  hasLoad=false;

    /**
     * 是否能下拉刷新
     */
    boolean touchDisableMove = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SDEventManager.register(this);
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
    }

    /**
     * 取分类和欢迎的话。cityId-currentData-interesting 城市ID-当前日期-话语。
     */
    private void getTopData() {
        if (commonHttpHelper == null) {
            commonHttpHelper = new CommonHttpHelper(getActivity(), this);
        }
        commonHttpHelper.getHomeClassifyList();
        if (!TextUtils.isEmpty(interestingStr)) {
            String[] list = interestingStr.split("-");
            if (list.length < 3) {
                settings.edit().putString("Interesting", "").commit();
                commonHttpHelper.getInterestingString(cityId);
            } else {
                if (cityId.equals(list[0]) && currentData.equals(list[1])) {
                    setInterestingStr(list[2]);
                } else {
                    settings.edit().putString("Interesting", "").commit();
                    commonHttpHelper.getInterestingString(cityId);
                }
            }
        } else {
            commonHttpHelper.getInterestingString(cityId);
        }
    }

    private void initView() {
        requestLiveList();
        interestingStr = settings.getString("Interesting","");
        mFragmentManager = new SDFragmentManager(getChildFragmentManager());

        liveHelper = new LiveHttpHelper(getActivity(), this, "");
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
        funnyType =(FunnyTypeHorizantalScrollView) rootView.findViewById(R.id.funny_type);
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

        funnyType.setOnFunnyTypeChangeListener(this);
        funnyType.setFunnyFragment(this);
    }

    @Override
    public void onTypeChanged(ModelHomeClassifyList model) {
        SDEventManager.post(model, EnumEventTag.HOME_TYPE_CHANGE.ordinal());
    }

    /**
     * 初始化分类。
     */

    protected void init() {
        bindData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(!hasLoad&&isVisibleToUser){
            requestLiveList();
            hasLoad = true;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 绑定分类的adapter 和 view.
     */
    private void bindData() {
        getTopData();
    }

    public void parseInteresting(List<HashMap<String, String>> datas) {
        if (datas == null || datas.size() < 1) {
            return;
        } else {
            String value = datas.get(0).get("value");
            if (!TextUtils.isEmpty(value)) {
                setInterestingStr(value);
                settings.edit().putString("Interesting", cityId + "-" + currentData + "-" + value).commit();

            }
        }
    }

    /**
     * 显示有趣页欢迎的话。
     *
     * @param interestingStr
     */
    public void setInterestingStr(String interestingStr) {
        if (interestingStr == null || interestingStr.length() < 1) {
            return;
        } else {
            String titleStr = "";
            String summaryStr = "";
            char[] chars = interestingStr.toCharArray();
            if (interestingStr.length() <= 6) {
                titleStr = interestingStr;
                summaryStr = "";
            } else {
                if (interestingStr.indexOf(",")==6|| ChineseCharClassifier.isChinesePunctuation(chars[6])) {
                    titleStr = interestingStr.substring(0, 7);
                    summaryStr = interestingStr.substring(7, interestingStr.length());
                } else {
                    titleStr = interestingStr.substring(0, 6);
                    summaryStr = interestingStr.substring(6, interestingStr.length());
                }
                if (summaryStr.endsWith(",") ||  ChineseCharClassifier.isChinesePunctuation(chars[summaryStr.length()-1])) {
                    summaryStr = summaryStr.substring(0, summaryStr.length() - 1);
                }
            }
            titleText.setText(titleStr);
            summaryText.setText(summaryStr);
        }
    }

    /**
     * 获取分类数据。
     *
     * @param datas
     */

    public void setHomeClassifyList(List<ModelHomeClassifyList> datas) {
        mList.clear();
        if (!SDCollectionUtil.isEmpty(datas)) {
            mList.addAll(datas);
        }

        funnyType.init(datas);
    }

    private void requestLiveList() {
        cityId = AppRuntimeWorker.getCity_id();
        if (liveHelper != null) {
            liveHelper.getLiveListNew(pageNum, pageSize, typeLiveHome, "", AppRuntimeWorker.getCity_id());
        }
    }

    /**
     * 直播列表
     *
     * @param datas
     */
    public void getLiveList(List<Object> datas) {
        if(datas==null||datas.size()<1){
            return;
        }
        PageModel page = (PageModel)datas.get(0);
        if(!TextUtils.isEmpty(page.getPage_total())){
            maxPageTotal = Integer.valueOf(page.getPage_total());
        }
        if(!TextUtils.isEmpty(page.getPage())){
            pageNum = Integer.valueOf(page.getPage());
        }
        if(this.pageNum==1){
            isRefresh = true;
        }else{
            isRefresh = false;
        }

            pageNum +=1;
            setPageNum(this.pageNum);

        datas.remove(0);
        //直播列表
        if(mHomeFragmentLiveList!=null) {
            mHomeFragmentLiveList.updateView(isRefresh, datas);
        }
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
                this.pageNum = 1;
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
        return recyclerScrollView.canRefresh() && !isTouchDisableMove();
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
        if(this.pageNum>maxPageTotal) {
           return;
        }
        requestLiveList();
    }

    public void loadComplete() {
        if(ptrFrameLayout!=null&&recyclerScrollView!=null) {
            ptrFrameLayout.refreshComplete();
            recyclerScrollView.loadComplite();
        }
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
        if (LiveConstants.LIVE_LIST_NEW.equals(method)) {
            //直播列表
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getLiveList(datas);
                }
            });
        } else if (CommonConstants.HOME_CLASSIFY_LIST.equals(method)) {
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setHomeClassifyList(datas);
                }
            });
        } else if (CommonConstants.INTERESTING.equals(method)) {
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
        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadComplete();
            }
        });
    }

    @Override
    public void onDestroy() {
        SDEventManager.unregister(this);
        super.onDestroy();
    }

    public boolean isTouchDisableMove() {
        return touchDisableMove;
    }

    public void setTouchDisableMove(boolean touchDisableMove) {
        this.touchDisableMove = touchDisableMove;
    }
}