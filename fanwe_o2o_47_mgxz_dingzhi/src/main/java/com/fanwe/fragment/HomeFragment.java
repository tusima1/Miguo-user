package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.base.CallbackView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.home.model.Room;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.dialog.SDDialogCustom.SDDialogCustomListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.IndexActAdvsModel;
import com.fanwe.model.Index_indexActModel;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.applyRoom.ModelApplyRoom;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.model.getAudienceCount.ModelAudienceCount;
import com.miguo.live.model.getAudienceList.ModelAudienceList;
import com.miguo.live.model.getHostInfo.ModelHostInfo;
import com.miguo.live.model.getHostTags.ModelHostTags;
import com.miguo.live.presenters.LiveHttpHelper;
import com.sunday.eventbus.SDBaseEvent;
import com.umeng.socialize.utils.Log;

/**
 * 首页fragment
 *
 * @author js02
 */
public class HomeFragment extends BaseFragment implements CallbackView {
    @ViewInject(R.id.frag_home_new_ptrsv_all)
    private PullToRefreshScrollView mPtrsvAll;

    private HomeTitleBarFragment mFragTitle;

    private List<GoodsModel> mListModel = new ArrayList<GoodsModel>();
    private List<GoodsModel> pageData_2 = new ArrayList<GoodsModel>();
    private List<GoodsModel> pageData_1 = null;

    //首页广告
    private HomeAdvsFragment mFragAdvs;
    private HomeIndexFragment mFragIndex;
    private HomeZtFragment mFragZt;
    private HomeRecommendStoreFragment mFragRecommendSupplier;
    //限时优惠
    private HomeRecommendEvnetFragment mFragRecommendEvent;
    private HomeRecommendTuanFragment mFragRecommendDeals;
    private HomeRecommendGoodsFragment mFragRecommendGoods;
    private HomeRecommendYouhuiFragment mFragRecommendCoupon;
    //直播列表
    private HomeFragmentLiveList mHomeFragmentLiveList;

    // 米果
    private HomeForenoticeYouhui mFragForenoticeYouhui;

    PageModel pageModel = new PageModel();
    private boolean isDown = true;
    protected Index_indexActModel mActModel;

    private LiveHttpHelper liveHelper;
    private boolean isRefresh = true;
    private int pageNum = 1;
    private int pageSize = 1;
    private List<Room> rooms;


    @Override
    protected View onCreateContentView(LayoutInflater inflater,
                                       ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_home);
    }

    @Override
    protected void init() {
        super.init();

//		initPageModel();
        locationCity();
        addTitleBarFragment();
        initPullToRefreshListView();
    }

    private void initPageModel(int totalPage) {
        pageModel.setPage_total(totalPage);
    }

    private void locationCity() {
        BaiduMapManager.getInstance().startLocation(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                if (mListModel != null) {
                    mListModel.clear();
                }
                pageModel.resetPage();
                pageData_1 = null;
                pageData_2.clear();
                requestIndex();
                requestLiveList();
//                requestIndex2(false);
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
        int cityId = AppRuntimeWorker.getCityIdByCityName(dist);
        if (cityId > 0) // 区域存在于城市列表中
        {
            if (!dist.equals(defaultCity)) // 区域不是默认的
            {
                showChangeLocationDialog(dist);
            }
        } else {
            String city = BaiduMapManager.getInstance().getCityShort();
            cityId = AppRuntimeWorker.getCityIdByCityName(city);
            if (cityId > 0) // 城市存在于城市列表中
            {
                if (!city.equals(defaultCity)) // 城市不是默认的
                {
                    showChangeLocationDialog(city);
                }
            }
        }
    }

    private void showChangeLocationDialog(final String location) {
        new SDDialogConfirm()
                .setTextContent(
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

    public void scrollToTop() {
        if (isResumed() && mPtrsvAll != null) {
            SDViewUtil.scrollToViewY(mPtrsvAll.getRefreshableView(), 0, 0);

        }
    }

    private void initPullToRefreshListView() {
        liveHelper = new LiveHttpHelper(getActivity(), this);
        mPtrsvAll.setMode(Mode.BOTH);
        mPtrsvAll.setOnRefreshListener(mOnRefresherListener2);
        mPtrsvAll.setRefreshing();
    }

    public void refreshData() {
        mPtrsvAll.setRefreshing();
    }

    private OnRefreshListener2<ScrollView> mOnRefresherListener2 = new OnRefreshListener2<ScrollView>() {
        @Override
        public void onPullDownToRefresh(
                PullToRefreshBase<ScrollView> refreshView) {
            isRefresh = true;
            pageNum = 1;
            //重置数据集
            pageModel.resetPage();
            mListModel.clear();
            pageData_1 = null;
            pageData_2.clear();

            requestIndex();
            requestLiveList();
//            requestIndex2(false);
            mPtrsvAll.setMode(Mode.BOTH);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            isRefresh = false;
            if (!SDCollectionUtil.isEmpty(rooms)) {
                pageNum++;
            }
            requestLiveList();
            mPtrsvAll.setMode(Mode.BOTH);
//            if (pageModel.increment()) {
//                // 添加第二页数据
//                requestIndex2(true);
//                if (pageModel.getPage() == 3) {
//                    mPtrsvAll.setMode(Mode.PULL_FROM_START);
//                }
//            } else {
//                mFragRecommendDeals = new HomeRecommendTuanFragment();
//                mFragRecommendDeals.setmIndexModel(mListModel, 3);
////                getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_deals, mFragRecommendDeals);
//                mPtrsvAll.setMode(Mode.PULL_FROM_START);
//                mPtrsvAll.onRefreshComplete();
//            }
        }

    };

    private void requestLiveList() {
        liveHelper.getLiveList(pageNum, pageSize, "", "", "e1b2911e-3a23-4630-9213-d317d200d9dc");
    }


    private void requestIndex() {
        RequestModel model = new RequestModel();
        final long timecurrentTimeMillis = System.currentTimeMillis() / 1000;
        model.putCtl("index");
        model.putAct("index");
//		model.put("time_local_request", timecurrentTimeMillis);
        SDRequestCallBack<Index_indexActModel> handler = new SDRequestCallBack<Index_indexActModel>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (actModel.getStatus() == 1) {

                    if (pageData_1 == null) {// 第一次
                        if (actModel.getDeal_list() != null && !SDCollectionUtil.isEmpty(actModel.getDeal_list()) && actModel.getDeal_list().size() > 0) {
                            //请求的数据为空,该城市没有可用的数据,显示默认图片;
                            pageData_1 = actModel.getDeal_list();

                            for (int i = 10; i < pageData_1.size(); i++) {
                                pageData_2.add(pageData_1.get(i));
                            }
                            pageData_1.removeAll(pageData_2);
                            mListModel.clear();
                            mListModel.addAll(pageData_1);

                            pageModel.update(pageModel);
                            mFragRecommendDeals = new HomeRecommendTuanFragment();
                            mFragRecommendDeals.setmIndexModel(mListModel, 1);
//                getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_deals, mFragRecommendDeals);
                        } else {
                            mFragRecommendDeals = new HomeRecommendTuanFragment();
                            mFragRecommendDeals.setmIndexModel(null, 1);
//                getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_deals, mFragRecommendDeals);
                        }
                    }
                    actModel.getSpecial().getInfo().setTime_local_request(String.valueOf(timecurrentTimeMillis));
                    addFragmentsByActModel(actModel);
                }
            }

            @Override
            public void onFinish() {
                mPtrsvAll.onRefreshComplete();
                SDDialogManager.dismissProgressDialog();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
    }

    private void requestIndex2(final boolean isLoadMore) {
        RequestModel model = new RequestModel();
        model.putCtl("index");
        model.putAct("index2");
        model.putPage(pageModel.getPage() - 1);
        SDRequestCallBack<Index_indexActModel> handler = new SDRequestCallBack<Index_indexActModel>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 这是第二页和第三页的数据
                mActModel = actModel;
                if (pageData_1 == null) {//第一页都没有显示,我还显示个P啊
                    return;
                }
                if (actModel.getStatus() == 1) {
                    initPageModel(actModel.getPage().getPage_total());
                    if (pageModel.getPage() == 2) {

                        if (pageData_2.size() > 0) {
                            mListModel.addAll(pageData_2);
                        }
                    } else if (pageModel.getPage() == 3) {

                        mListModel.addAll(actModel.getDeal_list());
                        pageData_1 = null;
                        pageData_2.clear();
                    }
                    pageModel.update(pageModel);

                    // 推荐团购
                    mFragRecommendDeals = new HomeRecommendTuanFragment();
                    mFragRecommendDeals.setmIndexModel(mListModel,
                            pageModel.getPage());
//                getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_deals, mFragRecommendDeals);
                }
            }

            @Override
            public void onFinish() {
                mPtrsvAll.onRefreshComplete();
                SDDialogManager.dismissProgressDialog();
            }
        };
        InterfaceServer.getInstance().requestInterface(model, handler);
    }

    protected void addFragmentsByActModel(Index_indexActModel actModel) {
        if (actModel == null) {
            return;
        }

        // 首页广告
        mFragAdvs = new HomeAdvsFragment();
        List<IndexActAdvsModel> listAdvs = actModel.getAdvs();
        mFragAdvs.setListIndexActAdvsModel(listAdvs);
        getSDFragmentManager().replace(R.id.frag_home_new_fl_advs, mFragAdvs);

        // 首页分类
        mFragIndex = new HomeIndexFragment();
        mFragIndex.setmIndexModel(actModel);
        getSDFragmentManager().replace(R.id.frag_home_new_fl_index, mFragIndex);

        // 米果优惠预告
        mFragForenoticeYouhui = new HomeForenoticeYouhui();
        mFragForenoticeYouhui.setmIndexModel(actModel);
        getSDFragmentManager().replace(R.id.frag_home_new_fl_forenotice_youhui,
                mFragForenoticeYouhui);

        // 首页专题
        mFragZt = new HomeZtFragment();
        mFragZt.setmIndexModel(actModel);
        getSDFragmentManager().replace(R.id.frag_home_new_fl_zt, mFragZt);

        // 推荐商家
        mFragRecommendSupplier = new HomeRecommendStoreFragment();
        mFragRecommendSupplier.setmIndexModel(actModel);
        getSDFragmentManager().replace(
                R.id.frag_home_new_fl_recommend_supplier,
                mFragRecommendSupplier);

        // 推荐活动
        mFragRecommendEvent = new HomeRecommendEvnetFragment();
        mFragRecommendEvent.setmIndexModel(actModel);
        getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_event,
                mFragRecommendEvent);

        // 推荐团购
        if (pageModel.getPage() == 1) {
            // mListModel = actModel.getDeal_list();
            pageModel.update(pageModel);
            mFragRecommendDeals = new HomeRecommendTuanFragment();
            mFragRecommendDeals.setmIndexModel(mListModel, 1);
//            getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_deals, mFragRecommendDeals);
        }
        // 推荐商品
        mFragRecommendGoods = new HomeRecommendGoodsFragment();
        mFragRecommendGoods.setmIndexModel(actModel);
        getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_goods,
                mFragRecommendGoods);

        // 推荐优惠券
        mFragRecommendCoupon = new HomeRecommendYouhuiFragment();
        mFragRecommendCoupon.setmIndexModel(actModel);
        getSDFragmentManager().replace(R.id.frag_home_new_fl_recommend_coupon,
                mFragRecommendCoupon);

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
                requestIndex();
                break;
            case RETRY_INIT_SUCCESS:
                dealLocationSuccess();
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
        mHandler.sendMessage(message);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //直播列表
                    mHomeFragmentLiveList.updateView(isRefresh, rooms);
                    mPtrsvAll.onRefreshComplete();
                    break;
            }
        }
    };


    @Override
    public void onSuccess(String responseBody) {

    }


    @Override
    public void onSuccess(String method, List datas) {
        if (LiveConstants.LIVE_LIST.equals(method)) {
            //直播列表
            getLiveList((ArrayList<Room>) datas);
        } else if (LiveConstants.APPLY_ROOM.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                ModelApplyRoom modelApplyRoom = (ModelApplyRoom) datas.get(0);
                Log.d("onSuccess", modelApplyRoom.getRoom_id());
            }
        } else if (LiveConstants.AUDIENCE_COUNT.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                ModelAudienceCount modelAudienceCount = (ModelAudienceCount) datas.get(0);
                Log.d("onSuccess", modelAudienceCount.getCount());
            }
        } else if (LiveConstants.AUDIENCE_LIST.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                ModelAudienceList modelAudienceList = (ModelAudienceList) datas.get(0);
                Log.d("onSuccess", modelAudienceList.getStart_time());
            }
        } else if (LiveConstants.END_INFO.equals(method)) {
            Log.d("onSuccess", "end success");
        } else if (LiveConstants.ENTER_ROOM.equals(method)) {
            Log.d("onSuccess", "enter success");
        } else if (LiveConstants.EXIT_ROOM.equals(method)) {
            Log.d("onSuccess", "exit success");
        } else if (LiveConstants.GENERATE_SIGN.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                ModelGenerateSign modelGenerateSign = (ModelGenerateSign) datas.get(0);
                Log.d("onSuccess", modelGenerateSign.getUsersig());
            }
        } else if (LiveConstants.HOST_INFO.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                ModelHostInfo modelHostInfo = (ModelHostInfo) datas.get(0);
                Log.d("onSuccess", modelHostInfo.getId());
            }
        } else if (LiveConstants.HOST_TAGS.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                ModelHostTags modelHostTags = (ModelHostTags) datas.get(0);
                Log.d("onSuccess", modelHostTags.getDic_mean());
            }
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }
}