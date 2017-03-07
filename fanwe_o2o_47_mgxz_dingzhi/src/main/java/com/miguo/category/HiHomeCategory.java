package com.miguo.category;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.app.App;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.constant.ServerUrl;
import com.fanwe.fragment.AppWebViewFragment;
import com.fanwe.fragment.MyFragment;
import com.fanwe.fragment.ShopCartFragmentNew;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.library.fragment.WebViewFragment;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.PageModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getCityList.ModelCityList;
import com.fanwe.service.update.AppUpgradeService;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.user.view.UserHomeActivity;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HomePagerAdapter;
import com.miguo.app.HiBaseActivity;
import com.miguo.dao.GetUserReceiveCodeDao;
import com.miguo.dao.LoginByMobileDao;
import com.miguo.dao.impl.GetUserReceiveCodeDaoImpl;
import com.miguo.dao.impl.LoginByMobileDaoImpl;
import com.miguo.definition.ClassPath;
import com.miguo.definition.HomePageState;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.factory.ClassNameFactory;
import com.miguo.fragment.HiHomeFragment;
import com.miguo.fragment.HiRepresentFragment;
import com.miguo.fragment.HiShopFragment;
import com.miguo.listener.HiHomeListener;
import com.miguo.live.definition.TabId;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.LiveUtil;
import com.miguo.live.views.dialog.GetDiamondInputDialog;
import com.miguo.live.views.dialog.GetDiamondLoginDialog;
import com.miguo.presenters.TencentIMBindPresenter;
import com.miguo.presenters.impl.TencentIMBindPresenterImpl;
import com.miguo.ui.view.BarryTab;
import com.miguo.ui.view.HomeViewPager;
import com.miguo.utils.BaseUtils;
import com.miguo.utils.ClipboardUtils;
import com.miguo.utils.SharedPreferencesUtils;
import com.miguo.view.GetUserReceiveCodeView;
import com.miguo.view.LoginByMobileView;
import com.miguo.view.TencentIMBindPresenterView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeCategory extends Category implements
        LoginByMobileView, GetUserReceiveCodeView, TencentIMBindPresenterView {


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

    /**
     * 领取兑换码
     */
    private ClipboardManager clipboardManager;
    private String code;

    /**
     * 接口类
     */
    /**
     * 登录接口
     */
    LoginByMobileDao loginByMobileDao;
    /**
     * 根据领取码领钻
     */
    GetUserReceiveCodeDao getUseReceiveCode;

    TencentIMBindPresenter tencentIMBindPresenter;

    public HiHomeCategory(HiBaseActivity activity) {

        super(activity);
    }

    @Override
    protected void initFirst() {

        clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        loginByMobileDao = new LoginByMobileDaoImpl(this);
        getUseReceiveCode = new GetUserReceiveCodeDaoImpl(this);

        tencentIMBindPresenter = new TencentIMBindPresenterImpl(this);

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
        tab.setOnTabClickListener((HiHomeListener) listener);
    }

    @Override
    protected void init() {
        checkAppVersion();

        initUserInfo();
        locationCity();
        initDict();
    }

    public void getIntentData() {
        // 尝试获取WebApp页面上过来的URL
        Uri uri = getActivity().getIntent().getData();
        if (uri != null) {
            code = uri.getQueryParameter("code");
            App.getInstance().code = this.code;
        }
    }

    private void initDict() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new LiveHttpHelper(null, null).getBussDictionInfo("Client");
            }
        }).start();
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
    private void initTab() {
        tab.
                /**
                 * 首页
                 * 名字 默认图标 按下后图标 tab id
                 */
                        addTab(getString(R.string.home), R.drawable.tab_home_normal, R.drawable.tab_home_pressed, TabId.TAB_A).
                addTab(getString(R.string.category), R.drawable.tab_seller_normal, R.drawable.tab_seller_pressed, TabId.TAB_B).
                addTab("我要直播", R.drawable.tab_shop_normal, R.drawable.tab_shop_normal, TabId.TAB_C, true).
                addTab(getString(R.string.funny), R.drawable.tab_market_normal, R.drawable.tab_market_pressed, TabId.TAB_D).
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
    private void initHomePagers() {
        fragments = new ArrayList<>();
        fragments.add(new HiHomeFragment());
        fragments.add(new HiRepresentFragment());
        fragments.add(new HiShopFragment());
        fragments.add(new ShopCartFragmentNew());
        fragments.add(new MyFragment());

        homePagerAdapter = new HomePagerAdapter(getActivity().getSupportFragmentManager(), fragments);

        homeViewPager.setAdapter(homePagerAdapter);
        homeViewPager.setOffscreenPageLimit(5);
    }

    /**
     * 检查app版本
     */
    private void checkAppVersion() {
        getActivity().startService(new Intent(getActivity(), AppUpgradeService.class));
    }

    public void onRefreshGreeting() {
        if (null != getHomeFragment()) {
            getHomeFragment().onRefreshGreeting();
        }
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取剪切板的领取码
     */
    private void checkCode() {
        getIntentData();
        if (TextUtils.isEmpty(code)) {
            code = ClipboardUtils.checkCode(getActivity());
        }
        /**
         * 如果用户登录了直接调取接口兑换领取码领钻
         */
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            initCode();
        }

    }

    public void checkIfInMyFragment() {
        if (TextUtils.isEmpty(App.getInstance().getToken())) {
            if (homeViewPager.getCurrentItem() == HomePageState.MY) {
                homeViewPager.setCurrentItem(HomePageState.HOME);
            }
        }
    }

    public void handlerLoginSuccessFromDiamond() {
        checkCode();
    }

    /**
     * 检查用户是否登录过，如果登录过则自动登录
     */
    private void autoLogin() {
        /**
         * 全局里面没登录信息，未登录
         */
        if (TextUtils.isEmpty(App.getInstance().getToken())) {
            if (!SharedPreferencesUtils.getInstance().hasSave()) {
                showDialogLogin();
                return;
            }

            /**
             * 登录
             * {@link #loginSuccess(UserInfoNew)}
             * {@link #loginError(String)}
             */
            loginByMobileDao.loginByMobile(SharedPreferencesUtils.getInstance().getUserName(), SharedPreferencesUtils.getInstance().getPassword());
            MobclickAgent.onEvent(getActivity(), "login");
        }
    }

    GetDiamondLoginDialog getDiamondLoginDialog;

    public void showDialogLogin() {
        if (getDiamondLoginDialog != null && getDiamondLoginDialog.isShowing()) {
            return;
        }
        getDiamondLoginDialog = new GetDiamondLoginDialog(getActivity());
        getDiamondLoginDialog.setSubmitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
                intent.putExtra(IntentKey.FROM_DIAMOND_TO_LOGIN, true);
                BaseUtils.jumpToNewActivityForResult(getActivity(), intent, RequestCode.LOGIN_SUCCESS_FOR_DIAMON);
                getDiamondLoginDialog.dismiss();
            }
        });
        getDiamondLoginDialog.setCloseListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDiamondLoginDialog.dismiss();
            }
        });
//        getDiamondLoginDialog.show();
    }


    /**
     * 展示领取码对话框
     */
    private void initCode() {
        if (App.getInstance().isShowCode) {
            if ("mgxz".equals(code) || TextUtils.isEmpty(code)) {
                if (App.getInstance().isAlreadyShowCode) {
                    return;
                } else {
                    App.getInstance().isAlreadyShowCode = true;
                }
                final GetDiamondInputDialog dialog = new GetDiamondInputDialog(getActivity());
                dialog.setSubmitListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //分享码
                        if (TextUtils.isEmpty(dialog.getCode())) {
                            App.getInstance().isShowCode = false;
                        } else {
                            getUseReceiveCode.getUserReceiveCode(dialog.getCode());
                            code = dialog.getCode();
                            App.getInstance().code = dialog.getCode();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.setCloseListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.getInstance().isShowCode = false;
                        dialog.dismiss();
                    }
                });
//                dialog.show();
            } else {
                if (!TextUtils.isEmpty(App.getApplication().getToken())) {
                    getUseReceiveCode.getUserReceiveCode(code);
                    App.getInstance().code = code;
                }
            }
        }
    }

    /**
     * 推送
     */
    private void initJpush() {
        JpushHelper.registerAll();
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo() {
        checkCode();
        autoLogin();
    }

    /**
     * 定位，城市处理
     */
    private List<GoodsModel> mListModel = new ArrayList<>();
    private List<GoodsModel> pageData_2 = new ArrayList<>();

    PageModel pageModel = new PageModel();

    /**
     * 定位城市
     */
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
                if (pageData_2 != null) {
                    pageData_2.clear();
                }

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

    SDDialogConfirm sdDialogConfirm;

    private void showChangeLocationDialog(final String location) {
        if (getActivity() == null) {
            return;
        }
        if (sdDialogConfirm != null && sdDialogConfirm.isShowing()) {
            return;
        }
        sdDialogConfirm =
                new SDDialogConfirm(getActivity());
        sdDialogConfirm.setTextContent(
                "当前定位位置为：" + location + "\n" + "是否切换到" + location + "?           ")
                .setmListener(new SDDialogCustom.SDDialogCustomListener() {
                    @Override
                    public void onDismiss(SDDialogCustom dialog) {
                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogCustom dialog) {
                        ModelCityList bean = AppRuntimeWorker.getCityByCityName(location);
                        AppRuntimeWorker.setCityNameByModel(bean);
                        updateFromCityChanged(bean);
                    }

                    @Override
                    public void onClickCancel(View v, SDDialogCustom dialog) {
                    }
                }).show();
    }

    /**
     * 定位，城市处理
     */

    public void clickTab(int position) {
        homeViewPager.setCurrentItem(position);
    }

    public void updateFromCityChanged(ModelCityList model) {
        ((HiHomeFragment) fragments.get(HomePageState.HOME)).updateFromCityChanged(model);
        ((HiRepresentFragment) fragments.get(HomePageState.CATEGORY)).updateFromCityChanged();
    }

    public void onResume(){
        fragments.get(HomePageState.CATEGORY).onResume();
    }

    /**
     * 从活动页点击过来跳转到有趣页
     */
    public void handleFunnyFragment() {
//        homeViewPager.setCurrentItem(1);
    }

    /**
     * 登录回调
     */
    @Override
    public void loginError(String message) {
        Log.d(tag, "login error message is: " + message);
    }

    /**
     * 登录成功
     *
     * @param user
     */
    @Override
    public void loginSuccess(UserInfoNew user) {

        /**
         * 检查是否有兑换码
         */
        initCode();
        /**
         * 获取腾讯sign签名
         */
        handlerTencentSign();
    }

    /**
     * 获取腾讯签名
     */
    private void handlerTencentSign() {
        /**
         * {@link com.miguo.presenters.impl.TencentIMBindPresenterImpl}
         * {@link com.miguo.view.TencentIMBindPresenterView}
         * {@link #tencentIMBindFinish()}
         */
        tencentIMBindPresenter.tencentIMBindingWithPushLocalCart();
    }

    @Override
    public void tencentIMBindFinish() {

    }

    private final String LIVE = "1";
    private final String LIVE_PLAY_BACK = "1";
    private final String PLAY_BACK = "2";

    /**
     * 领取码回调
     * 成功
     */
    @Override
    public void getUserReceiveCodeSuccess(ModelRoom room) {
        if (null != room) {
            if (clipboardManager != null)
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "mgxz"));
            String live_type = room.getLive_type();
            if (!LIVE.equals(live_type) && !(PLAY_BACK.equals(live_type))) {
                if (TextUtils.isEmpty(live_type) && TextUtils.isEmpty(room.getChat_room_id())) {
                    if (room.getHost() != null) {
                        if (!TextUtils.isEmpty(room.getHost().getUid())) {
                            //提示用户直播结束，跳转到网红主页
                            Intent intent = new Intent(getActivity(), UserHomeActivity.class);
                            intent.putExtra("id", room.getHost().getUid());
                            intent.putExtra("toastContent", "直播已结束，钻石发放失败");
                            BaseUtils.jumpToNewActivity(getActivity(), intent);
                            return;
                        }
                    }
                }
                return;
            } else {
                LiveUtil.clickRoom(room, getActivity());
            }
        } else {
            //未请求到数据
            if (App.getInstance().isAlreadyShowCode) {
                return;
            } else {
                App.getInstance().isAlreadyShowCode = true;
            }
            final GetDiamondInputDialog dialog = new GetDiamondInputDialog(getActivity());
            dialog.setSubmitListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //分享码
                    if (TextUtils.isEmpty(dialog.getCode())) {
                        App.getInstance().isShowCode = false;
                    } else {
                        getUseReceiveCode.getUserReceiveCode(dialog.getCode());
                        App.getInstance().code = dialog.getCode();
                    }
                    dialog.dismiss();
                }
            });
            dialog.setCloseListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.getInstance().isShowCode = false;
                    dialog.dismiss();
                }
            });
//            dialog.show();
        }
    }

    /**
     * 领取码回调
     * 失败
     *
     * @param message
     */
    @Override
    public void getUserReceiveCodeError(String message) {

    }


    public HiHomeFragment getHomeFragment() {
        return null != fragments && fragments.size() > 0 ? (HiHomeFragment) fragments.get(0) : null;
    }

}
