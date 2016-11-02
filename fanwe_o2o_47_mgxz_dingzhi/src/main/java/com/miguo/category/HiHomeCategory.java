package com.miguo.category;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.fragment.MyFragment;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.model.CitylistModel;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.PageModel;
import com.fanwe.seller.views.SellerFragment;
import com.fanwe.service.AppUpgradeService;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.adapter.HomePagerAdapter;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.fragment.HiHomeFragment;
import com.miguo.listener.HiHomeListener;
import com.miguo.live.definition.TabId;
import com.miguo.live.views.view.FunnyFragment;
import com.miguo.ui.view.BarryTab;
import com.miguo.ui.view.HomeViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeCategory extends Category{


    /**
     * 低栏tab
     */
    @ViewInject(R.id.tab)
    BarryTab tab;

    /**
     * 城市
     */

    /**
     * 四大分栏的ViewPager
     */
    @ViewInject(R.id.home_view_pager)
    HomeViewPager homeViewPager;
    HomePagerAdapter homePagerAdapter;
    ArrayList<Fragment> fragments;



    public HiHomeCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {

    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initThisListener() {
        listener = new HiHomeListener(this);
    }

    @Override
    protected void setThisListener() {
        tab.setOnTabClickListener((HiHomeListener)listener);
    }

    @Override
    protected void init() {
        checkAppVersion();
        initJpush();
        initUserInfo();
        locationCity();
    }

    @Override
    protected void initViews() {
        initTab();
        initHomePagers();
    }

    /**
     * 初始化tab
     * author：zlh/Barry/狗蛋哥
     * create time:2016 10/13
     * modified time:null
     */
    private void initTab(){
        tab.
            /**
             * 首页
             * 名字 默认图标 按下后图标 tab id
             */
            addTab(getString(R.string.home), R.drawable.tab_home_normal, R.drawable.tab_home_pressed, TabId.TAB_A).
            addTab(getString(R.string.funny), R.drawable.tab_seller_normal, R.drawable.tab_seller_pressed, TabId.TAB_B).
            addTab("我要直播", R.drawable.tab_live_normal, R.drawable.tab_live_pressed, TabId.TAB_C, true).
            addTab(getString(R.string.find), R.drawable.tab_market_normal, R.drawable.tab_market_pressed, TabId.TAB_D).
            addTab(getString(R.string.mine), R.drawable.tab_my_normal, R.drawable.tab_my_pressed, TabId.TAB_E).
            /**
             * 设置为默认模式（图标+文字形式）
             */
            setTabType(BarryTab.Type.NORMAL).
            /**
             * 设置文字大小
             */
            setTextSize(12).
            /**
             * 设置图标大小，单位：dp
             * 设置了width等于设置了height
             */
            setIconWidht(20).
            /**
             * 设置中间图标的宽高
             */
            setCenterIconWidth(35).
            /**
             * 设置tab默认文字颜色
             */
            setNormalColor(R.color.text_home_menu_normal).
            /**
             * 设置tab选中时候的文字颜色
             */
            setPressColor(R.color.c_f5b830).
            /**
             * 绑定ViewPager
             */
            setViewPager(homeViewPager).
            /**
             * 生产
             */
            builder();
    }

    /**
     * 初始化首页四大板块
     */
    private void initHomePagers(){
        fragments = new ArrayList<>();
        fragments.add(new HiHomeFragment());
        fragments.add(new FunnyFragment());
        fragments.add(new SellerFragment());
        fragments.add(new MyFragment());

        homePagerAdapter = new HomePagerAdapter(getActivity().getSupportFragmentManager(), fragments);

        homeViewPager.setAdapter(homePagerAdapter);
        homeViewPager.setOffscreenPageLimit(5);
    }

    /**
     * 检查app版本
     */
    private void checkAppVersion(){
        getActivity().startService(new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.APP_UPGRADE_SERVICE)));
    }

    /**
     * 推送
     */
    private void initJpush(){
        JpushHelper.initJPushConfig();
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo(){

    }

    /**
     * 定位，城市处理
     */
    private List<GoodsModel> mListModel = new ArrayList<GoodsModel>();
    private List<GoodsModel> pageData_2 = new ArrayList<GoodsModel>();
    private List<GoodsModel> pageData_1 = null;

    PageModel pageModel = new PageModel();

    private void locationCity() {
        BaiduMapManager.getInstance().init(App.getInstance().getApplicationContext());
        BaiduMapManager.getInstance().startLocation(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                if (mListModel != null) {
                    mListModel.clear();
                }
                if (pageModel != null) {
                    pageModel.resetPage();
                }
                pageData_1 = null;
                if (pageData_2 != null) {
                    pageData_2.clear();
                }
//                onRefreshBegin(ptrFrameLayout);
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

    private void showChangeLocationDialog(final String location) {
        new SDDialogConfirm(getActivity())
                .setTextContent(
                        "当前定位位置为：" + location + "\n" + "是否切换到" + location + "?           ")
                .setmListener(new SDDialogCustom.SDDialogCustomListener() {
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
    /** 定位，城市处理 */

    public void clickTab(int position){
        homeViewPager.setCurrentItem(position);
    }

    public void updateFromCityChanged(CitylistModel model){
        ((HiHomeFragment)fragments.get(0)).updateFromCityChanged(model);
    }


}
