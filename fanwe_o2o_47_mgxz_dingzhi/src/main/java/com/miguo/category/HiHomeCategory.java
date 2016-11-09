package com.miguo.category;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.fragment.MyFragment;
import com.fanwe.home.model.Host;
import com.fanwe.home.model.Room;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.dialog.SDDialogConfirm;
import com.fanwe.library.dialog.SDDialogCustom;
import com.fanwe.model.CitylistModel;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.PageModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.seller.views.SellerFragment;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.user.view.UserHomeActivity;
import com.fanwe.utils.DataFormat;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.adapter.HomePagerAdapter;
import com.miguo.app.HiBaseActivity;
import com.miguo.dao.GetUserReceiveCodeDao;
import com.miguo.dao.IMLoginDao;
import com.miguo.dao.IMUserInfoDao;
import com.miguo.dao.LoginByMobileDao;
import com.miguo.dao.TencentSignDao;
import com.miguo.dao.impl.GetUserReceiveCodeDaoImpl;
import com.miguo.dao.impl.IMLoginDaoImpl;
import com.miguo.dao.impl.IMUserInfoDaoImpl;
import com.miguo.dao.impl.LoginByMobileDaoImpl;
import com.miguo.dao.impl.TencentSignDaoImpl;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.fragment.HiHomeFragment;
import com.miguo.listener.HiHomeListener;
import com.miguo.live.definition.TabId;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.dialog.GetDiamondInputDialog;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.view.FunnyFragment;
import com.miguo.live.views.view.PlayBackActivity;
import com.miguo.ui.view.BarryTab;
import com.miguo.ui.view.HomeViewPager;
import com.miguo.utils.SharedPreferencesUtils;
import com.miguo.view.GetUserReceiveCodeView;
import com.miguo.view.IMLoginView;
import com.miguo.view.IMUserInfoView;
import com.miguo.view.LoginByMobileView;
import com.miguo.view.TencentSignView;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeCategory extends Category implements
        LoginByMobileView, GetUserReceiveCodeView, TencentSignView, IMLoginView, IMUserInfoView {


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
    /**
     * 腾讯获取签名
     */
    TencentSignDao tencentSignDao;
    /**
     * IM登录接口
     */
    IMLoginDao imLoginDao;

    /**
     * 绑定用户信息到IM接口
     *
     */
    IMUserInfoDao imUserInfoDao;

    public HiHomeCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        loginByMobileDao = new LoginByMobileDaoImpl(this);
        getUseReceiveCode = new GetUserReceiveCodeDaoImpl(this);
        tencentSignDao = new TencentSignDaoImpl(this);
        imLoginDao = new IMLoginDaoImpl(this);
        imUserInfoDao = new IMUserInfoDaoImpl(this);

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
        initDict();
    }

    @Override
    protected void initViews() {
        initTab();
        initHomePagers();
    }

    private void initDict() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new LiveHttpHelper(null, null).getBussDictionInfo("Client");
            }
        }).start();
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

    public void onRefreshGreeting(){
        if(null != getHomeFragment()){
            getHomeFragment().onRefreshGreeting();
        }
    }

    /**
     * 获取剪切板的领取码
     */
    private void checkCode() {
        clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip()) {
            code = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
        }

        /**
         * 如果用户登录了直接调取接口兑换领取码领钻
         */
        if(!TextUtils.isEmpty(App.getInstance().getToken())){
            initCode();
        }

    }

    /**
     * 检查用户是否登录过，如果登录过则自动登录
     */
    private void autoLogin(){
        /**
         * 全局里面没登录信息，未登录
         */
        if(TextUtils.isEmpty(App.getInstance().getToken())){
            LocalUserModel userModel = AppHelper.getLocalUser();
            if(userModel == null){
                return;
            }
            String userid = userModel.getUser_mobile();
            String password = userModel.getUser_pwd();
            if(TextUtils.isEmpty(userid) || TextUtils.isEmpty(password)){
                return;
            }

            /**
             * 登录
             */
            loginByMobileDao.loginByMobile(userid, password);
        }
    }

    /**
     * 展示领取码对话框
     */
    private void initCode(){
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
//                            liveHttpHelper.getUseReceiveCode(dialog.getCode());
                            getUseReceiveCode.getUserReceiveCode(dialog.getCode());
                            code = dialog.getCode();
//                            App.getInstance().code = dialog.getCode();
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
                dialog.show();
            } else {
//                liveHttpHelper.getUseReceiveCode(code);
                getUseReceiveCode.getUserReceiveCode(code);
            }
        }
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

    public void handlerFunnyFragment(){
        homeViewPager.setCurrentItem(1);
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
     * @param user
     */
    @Override
    public void loginSuccess(UserInfoNew user, String mobile, String password) {
        Log.d(tag, "login success.. ");
        /**
         * 检查是否有兑换码
         */
        initCode();
        /**
         * 保存用户信息到本地
         */
        saveUserToLocal(user, mobile, password);
        /**
         * 获取腾讯sign签名
         */
        handlerTencentSign(App.getApplication().getToken());
        /**
         * 保存用户信息SharedPreferences
         */
        handlerSaveUser(mobile, password);
    }

    /**
     *
     * 保存用户信息SharedPreferences
     * @param mobile
     * @param password
     */
    private void handlerSaveUser(String mobile, String password){
        SharedPreferencesUtils.getInstance(getActivity()).saveUserNameAndUserPassword(mobile, password);
    }

    /**
     * 将用户信息保存到本地以及全局
     * @param user
     */
    private void saveUserToLocal(UserInfoNew user, String mobile, String password){
        UserInfoNew userInfoNew = user;
        if (userInfoNew != null) {
            App.getInstance().getmUserCurrentInfo().setUserInfoNew(userInfoNew);
            User_infoModel model = new User_infoModel();
            model.setUser_id(userInfoNew.getUser_id());
            MySelfInfo.getInstance().setId(userInfoNew.getUser_id());
            if (!TextUtils.isEmpty(mobile)) {
                model.setMobile(mobile);
            }
            if (!TextUtils.isEmpty(password)) {
                model.setUser_pwd(password);
            }
            if (!TextUtils.isEmpty(userInfoNew.getPwd())) {
                model.setUser_pwd(userInfoNew.getPwd());
            }
            model.setUser_name(userInfoNew.getUser_name());

            LocalUserModel.dealLoginSuccess(model, true);
        }
    }

    /**
     * 获取腾讯签名
     * @param token
     */
    private void handlerTencentSign(String token){
        if(TextUtils.isEmpty(token)){
            Log.d(tag, "handler tencent sign token is null...");
            return;
        }
        tencentSignDao.getTencentSign(token);
    }

    private void handlerIMLogin(String userId,String usersig){
        imLoginDao.imLogin(userId, usersig);
    }

    /**
     * 领取码回调
     * 成功
     */
    @Override
    public void getUserReceiveCodeSuccess(Room room) {
        Log.d(tag, "get user receive vode success...");
        if(null != room){
            if (clipboardManager != null)
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "mgxz"));
            //分点播和直播 直播类型  1 表示直播，2表示点播
            String live_type = room.getLive_type();
            if ("1".equals(live_type)) {
                //直播
                gotoLiveActivity(room);
            } else if ("2".equals(live_type)) {
                //点播
                gotoPlayBackActivity(room);
            } else {
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
                //异常数据
//                MGToast.showToast("异常数据");
                return;
            }
        }
    }

    /**
     * 领取码回调
     * 失败
     * @param message
     */
    @Override
    public void getUserReceiveCodeError(String message) {

    }

    /**
     * 获取腾讯签名
     * 获取成功后需要调用IM登录
     */
    @Override
    public void getTencentSignSuccess(ModelGenerateSign sign) {
        if(null == sign){
            return;
        }
        String usersig = sign.getUsersig();
        App.getInstance().setUserSign(usersig);
        MySelfInfo.getInstance().setUserSig(usersig);
        App.getInstance().setUserSign(usersig);
        String userId = MySelfInfo.getInstance().getId();

        if (TextUtils.isEmpty(userId)) {
            UserCurrentInfo currentInfo = App.getInstance().getmUserCurrentInfo();
            if (currentInfo != null && currentInfo.getUserInfoNew() != null) {
                userId = currentInfo.getUserInfoNew().getUser_id();
            } else {
                return;
            }
        }
        handlerIMLogin(userId, usersig);
    }

    @Override
    public void getTencentSignError() {
        Log.d(tag, "get tencent sign error..");
    }

    /**
     * IM登陆回调
     */
    @Override
    public void imLoginError(String message) {
        Log.d(tag, "im login error and the message is: " + message);
    }

    /**
     * IM登录成功
     * 登录成功后要将用户名和头像绑定到IM
     */
    @Override
    public void imLoginSuccess() {
        if(!TextUtils.isEmpty(App.getInstance().getToken())){
            imUserInfoDao.updateTencentNickName(App.getInstance().getmUserCurrentInfo().getUserInfoNew().getNick());
            imUserInfoDao.updateTencentAvatar(App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon());
        }
        /**
         * 开始直播AVSDK
         */
        startAVSDK();
        App.getInstance().setImLoginSuccess(true);
    }
    /**
     * 初始化AVSDK
     */
    public void startAVSDK() {
        String userid = MySelfInfo.getInstance().getId();
        String userSign = MySelfInfo.getInstance().getUserSig();
        int appId = Constants.SDK_APPID;

        int ccType = Constants.ACCOUNT_TYPE;
        Log.e("LoginHelper", "初始化AVSDK");
        QavsdkControl.getInstance().setAvConfig(appId, ccType + "", userid, userSign);
        QavsdkControl.getInstance().startContext();

        Log.e("LoginHelper", "初始化AVSDK");
    }


    /**
     * 进入点播
     */
    /**
     * 进入点播页面
     *
     * @param room
     */
    private void gotoPlayBackActivity(Room room) {
        addCommonData(room);
        String chat_room_id = room.getChat_room_id();//im的id
        String file_size = room.getFile_size();//文件大小
        String duration = room.getDuration();//时长
        String file_id = room.getFile_id();
        String vid = room.getVid();
        String playset = room.getPlayset();

        Intent intent = new Intent(getActivity(), PlayBackActivity.class);
        Bundle data = new Bundle();
        data.putString("chat_room_id", chat_room_id);
        data.putString("file_size", file_size);
        data.putString("duration", duration);
        data.putString("file_id", file_id);
        data.putString("vid", vid);
        data.putString("playset", playset);
        intent.putExtras(data);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    private void addCommonData(Room room) {
        Host host = room.getHost();
        String nickName = App.getInstance().getUserNickName();
        String avatar = "";
        if (App.getInstance().getmUserCurrentInfo() != null) {
            UserCurrentInfo currentInfo = App.getInstance().getmUserCurrentInfo();
            if (currentInfo.getUserInfoNew() != null) {
                avatar = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon();
            }
        }
        MySelfInfo.getInstance().setAvatar(avatar);
        MySelfInfo.getInstance().setNickName(nickName);
        MySelfInfo.getInstance().setJoinRoomWay(false);
        CurLiveInfo.setHostID(host.getHost_user_id());
        CurLiveInfo.setHostName(host.getNickname());

        CurLiveInfo.setHostAvator(room.getHost().getAvatar());
        App.getInstance().setCurrentRoomId(room.getId());
        CurLiveInfo.setRoomNum(DataFormat.toInt(room.getId()));
        if (room.getLbs() != null) {
            CurLiveInfo.setShopID(room.getLbs().getShop_id());
            ModelStoreList modelStoreList = new ModelStoreList();
            modelStoreList.setShop_name(room.getLbs().getShop_name());
            modelStoreList.setId(room.getLbs().getShop_id());
            CurLiveInfo.setModelShop(modelStoreList);
        }
        CurLiveInfo.setLive_type(room.getLive_type());

        CurLiveInfo.setHostUserID(room.getHost().getUid());
//                CurLiveInfo.setMembers(item.getWatchCount() + 1); // 添加自己
        CurLiveInfo.setMembers(1); // 添加自己
//                CurLiveInfo.setAddress(item.getLbs().getAddress());
        if (room.getLbs() != null && !TextUtils.isEmpty(room.getLbs().getShop_id())) {
            CurLiveInfo.setShopID(room.getLbs().getShop_id());
        }
        CurLiveInfo.setAdmires(1);
    }

    /**
     * 进入直播
     */
    private void gotoLiveActivity(Room room) {
        Intent intent = new Intent(getActivity(), LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.MEMBER);
        MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
        addCommonData(room);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    public HiHomeFragment getHomeFragment(){
        return null != fragments && fragments.size() > 0 ? (HiHomeFragment)fragments.get(0) : null;
    }


}
