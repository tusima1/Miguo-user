package com.fanwe.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.base.CallbackView;
import com.fanwe.base.CallbackView2;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.dao.barry.CommandGroupBuyDao;
import com.fanwe.dao.barry.impl.CommandGroupBuyDaoImpl;
import com.fanwe.dao.barry.view.CommandGroupBuyView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.home.model.Room;
import com.fanwe.home.views.FragmentHomeTimeLimit;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.model.CommandGroupBuyBean;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.Index_indexActModel;
import com.fanwe.model.PageModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.view.FixRequestDisallowTouchEventPtrFrameLayout;
import com.fanwe.view.RecyclerScrollView;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.presenters.LiveHttpHelper;
import com.sunday.eventbus.SDBaseEvent;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * 首页fragment
 *
 * @author js02
 */
public class HomeFragment extends BaseFragment implements CallbackView, CallbackView2, PtrHandler, RecyclerScrollView.OnRecyclerScrollViewListener, CommandGroupBuyView {

    @ViewInject(R.id.ptr_layout)
    FixRequestDisallowTouchEventPtrFrameLayout ptrFrameLayout;
    @ViewInject(R.id.recycler_scrollview)
    RecyclerScrollView recyclerScrollView;

    private HomeTitleBarFragment mFragTitle;

    private List<GoodsModel> mListModel = new ArrayList<GoodsModel>();
    private List<GoodsModel> pageData_2 = new ArrayList<GoodsModel>();

    //首页广告
    private HomeIndexFragment mFragIndex;
    //限时优惠
    private FragmentHomeTimeLimit mFragmentHomeTimeLimit;

    //直播列表
    private HomeFragmentLiveList mHomeFragmentLiveList;


    PageModel pageModel = new PageModel();
    protected Index_indexActModel mActModel;

    private LiveHttpHelper liveHelper;
    private CommonHttpHelper commonHttpHelper;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private int pageSize = 10;
    private List<Room> rooms;
    private String typeLiveHome = "";

    CommandGroupBuyDao commandGroupBuyDao;

    @Override
    protected View onCreateContentView(LayoutInflater inflater,
                                       ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_home);
    }

    @Override
    protected void init() {
        super.init();
        initDao();
        getHomeClassify();
//		initPageModel();
        locationCity();
        addTitleBarFragment();
        initPullToRefreshListView();
        addTimeLimitFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNum = 1;
        requestLiveList();
    }

    /**
     * 添加限时特惠fragment
     */
    private void addTimeLimitFragment() {
        mFragmentHomeTimeLimit = new FragmentHomeTimeLimit();
        mFragmentHomeTimeLimit.setParent(ptrFrameLayout);
        getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_event,
                mFragmentHomeTimeLimit);
    }

    private void initDao() {
        commandGroupBuyDao = new CommandGroupBuyDaoImpl(this);
    }

    private void getTuanList(int page) {
        try {
            commandGroupBuyDao.getCommandGroupBuyDaoList(pageNum, pageSize, typeLiveHome, "", BaiduMapManager.getInstance().getBDLocation().getLongitude() + "", BaiduMapManager.getInstance().getBDLocation().getLatitude() + "", AppRuntimeWorker.getCity_id());
        } catch (Exception e) {

        }
    }

    private void getHomeClassify() {
        if (commonHttpHelper == null) {
            commonHttpHelper = new CommonHttpHelper(getActivity(), this);
        }
        commonHttpHelper.getHomeClassifyList();
    }

    private void locationCity() {
        BaiduMapManager.getInstance().startLocation(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                if (mListModel != null) {
                    mListModel.clear();
                }
                if (pageModel != null) {
                    pageModel.resetPage();
                }
                if (pageData_2 != null) {
                    pageData_2.clear();
                }
                onRefreshBegin(ptrFrameLayout);
                if (location != null) {
                    dealLocationSuccess();
                }
                BaiduMapManager.getInstance().stopLocation();
            }
        });
    }

    private void dealLocationSuccess() {
        String defaultCity = AppRuntimeWorker.getCity_name();
        if (TextUtils.isEmpty(defaultCity)) {
            return;
        }
        if (!BaiduMapManager.getInstance().hasLocationSuccess()) {
            return;
        }
        String dist = BaiduMapManager.getInstance().getDistrictShort();
        String cityId = AppRuntimeWorker.getCityIdByCityName(dist);
        if (!TextUtils.isEmpty(cityId)) // 区域存在于城市列表中
        {
            if (!dist.equals(defaultCity)) // 区域不是默认的
            {
                showChangeLocationDialog(dist);
            }
        } else {
            String city = BaiduMapManager.getInstance().getCityShort();
            cityId = AppRuntimeWorker.getCityIdByCityName(city);
            if (!TextUtils.isEmpty(cityId)) // 城市存在于城市列表中
            {
                if (!city.equals(defaultCity)) // 城市不是默认的
                {
                    showChangeLocationDialog(city);
                }
            }
        }
    }

    SDDialogConfirm dialog;

    private void showChangeLocationDialog(final String location) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new SDDialogConfirm();
        dialog.setTextContent(
                "当前定位位置为：" + location + "\n" + "是否切换到" + location + "?           ")
                .setmListener(new SDDialogCustomListener() {
                    @Override
                    public void onDismiss(SDDialogCustom dialog) {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogCustom dialog) {
                        AppRuntimeWorker.setCity_name(location);
                    }

                    @Override
                    public void onClickCancel(View v, SDDialogCustom dialog) {
                    }
                }).show();
    }

    private void initPullToRefreshListView() {
        liveHelper = new LiveHttpHelper(getActivity(), this, "");
        initPtrLayout(this.ptrFrameLayout);
    }

    protected void initPtrLayout(PtrFrameLayout ptrFrameLayout) {
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.setEnabledNextPtrAtOnce(false);
        MaterialHeader ptrHead = new MaterialHeader(getActivity());
        ptrHead.setPadding(0, 24, 0, 24);
        ptrFrameLayout.setHeaderView(ptrHead);
        ptrFrameLayout.addPtrUIHandler(ptrHead);
        /**
         * 设置下拉刷新回调
         */
        ptrFrameLayout.setPtrHandler(this);
        recyclerScrollView.setOnRecyclerScrollViewListener(this);
    }

    private void requestLiveList() {
        if (liveHelper != null) {
            liveHelper.getLiveList(pageNum, pageSize, typeLiveHome, "", AppRuntimeWorker.getCity_id());
        }
    }

    private void addTitleBarFragment() {
        mFragTitle = new HomeTitleBarFragment();
        getSDFragmentManager().replace(R.id.frag_home_new_fl_title_bar,
                mFragTitle);
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case CITY_CHANGE:
                onRefreshBegin(ptrFrameLayout);
                break;
            case RETRY_INIT_SUCCESS:
                dealLocationSuccess();
                break;
            case HOME_TYPE_CHANGE:
                ModelHomeClassifyList bean = (ModelHomeClassifyList) event.getData();
                if (mHomeFragmentLiveList != null) {
                    mHomeFragmentLiveList.updateTitle(bean.getName());
                }
                typeLiveHome = bean.getId();
                onRefreshBegin(ptrFrameLayout);
                break;
            default:
                break;
        }
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

    /**
     * 直播列表
     *
     * @param datas
     */
    public void getLiveList(ArrayList<Room> datas) {
        if (mHomeFragmentLiveList == null) {
            mHomeFragmentLiveList = new HomeFragmentLiveList();
            getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_deals, mHomeFragmentLiveList);
        }
        if (SDCollectionUtil.isEmpty(datas)) {
            rooms = null;
        }
        rooms = datas;
        Message message = new Message();
        message.what = 1;
        handlerMessage(message.what);
//        mHandler.sendMessage(message);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //直播列表
                    mHomeFragmentLiveList.updateView(isRefresh, rooms);
                    loadComplete();
                    break;
                case 2:
                    loadComplete();
                    break;
                case 3:
                    // 首页分类
                    if (mFragIndex == null) {
                        mFragIndex = new HomeIndexFragment();
                        getSDFragmentManager().replace(R.id.frag_home_new_fl_index, mFragIndex);
                    }
                    mFragIndex.setHomeClassifyList(itemsHomeClassify);
                    break;
            }
        }
    };

    private void handlerMessage(int what) {
        if (getContext() == null) {
            return;
        }
        mHandler.sendEmptyMessage(what);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelHomeClassifyList> itemsHomeClassify;

    @Override
    public void onSuccess(String method, List datas) {
        if (LiveConstants.LIVE_LIST.equals(method)) {
            //直播列表
            getLiveList((ArrayList<Room>) datas);
        } else if (CommonConstants.HOME_CLASSIFY_LIST.equals(method)) {
            itemsHomeClassify = datas;
            Message message = new Message();
            message.what = 3;
            handlerMessage(message.what);
        }
    }

    @Override
    public void onFailue(String responseBody) {
        //刷新页面
        Message message = new Message();
        message.what = 2;
        handlerMessage(message.what);
    }

    @Override
    public void onFinish(String method) {
        //刷新页面
        Message message = new Message();
        message.what = 2;
        handlerMessage(message.what);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return recyclerScrollView.canRefresh();
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        pageNum = 1;
        requestLiveList();
        getTuanList(pageNum);
        mFragmentHomeTimeLimit.onRefresh();
        //判断是否要请求头部
        if (SDCollectionUtil.isEmpty(itemsHomeClassify)) {
            getHomeClassify();
        }
    }

    /**
     * 加载更多
     */
    @Override
    public void onScrollToEnd() {
        getTuanList(pageNum);
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
    }

    public void loadComplete() {
        ptrFrameLayout.refreshComplete();
        recyclerScrollView.loadComplite();
    }

    /**
     * 首页团购列表回调
     */
    @Override
    public void getCommandGroupBuyDaoListSuccess(final CommandGroupBuyBean.Result result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPageNum(result.getPage());
                if (mHomeFragmentLiveList != null) {
                    mHomeFragmentLiveList.onRefreshTuan(true, result.getBody());
                    setPageNum(result.getPage() + 1);
                }
                loadComplete();
            }
        });
    }

    @Override
    public void getCommandGroupBuyDaoListLoadMore(final CommandGroupBuyBean.Result result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPageNum(result.getPage());
                if (mHomeFragmentLiveList != null) {
                    mHomeFragmentLiveList.onRefreshTuan(false, result.getBody());
                }
                if (result.getBody() != null && result.getBody().size() > 0) {
                    setPageNum(result.getPage() + 1);
                }
                loadComplete();
            }
        });
    }

    @Override
    public void getCommandGroupBuyDaoListError(String msg) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadComplete();
                }
            });
        }
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}