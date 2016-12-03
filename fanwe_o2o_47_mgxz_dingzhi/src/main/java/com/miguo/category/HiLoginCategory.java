package com.miguo.category;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.ModifyPasswordActivity;
import com.fanwe.RegisterActivity;
import com.fanwe.app.App;
import com.fanwe.constant.ServerUrl;
import com.fanwe.fragment.LoginPhoneFragment;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.common.SDFragmentManager;
import com.fanwe.library.customview.SDTabItemCorner;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.ThirdLoginInfo;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiHomeActivity;
import com.miguo.app.HiLoginActivity;
import com.miguo.dao.GetShareIdByCodeDao;
import com.miguo.dao.IMLoginDao;
import com.miguo.dao.IMUserInfoDao;
import com.miguo.dao.LoginByThirdDao;
import com.miguo.dao.TencentSignDao;
import com.miguo.dao.impl.GetShareIdByCodeDaoImpl;
import com.miguo.dao.impl.IMLoginDaoImpl;
import com.miguo.dao.impl.IMUserInfoDaoImpl;
import com.miguo.dao.impl.LoginByThirdDaoImpl;
import com.miguo.dao.impl.TencentSignDaoImpl;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.factory.ClassNameFactory;
import com.miguo.fragment.HiLoginByMobileFragment;
import com.miguo.fragment.HiLoginQuickByMobileFragment;
import com.miguo.listener.HiLoginListener;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.BaseUtils;
import com.miguo.utils.ClipboardUtils;
import com.miguo.view.GetShareIdByCodeView;
import com.miguo.view.IMLoginView;
import com.miguo.view.IMUserInfoView;
import com.miguo.view.LoginByThirdView;
import com.miguo.view.TencentSignView;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simbest.com.sharelib.ILoginCallback;
import simbest.com.sharelib.ShareUtils;

/**
 * Created by zlh on 2016/11/30.
 */

public class HiLoginCategory extends Category implements GetShareIdByCodeView, LoginByThirdView, TencentSignView, IMLoginView, IMUserInfoView {

    @ViewInject(R.id.title_layout)
    RelativeLayout titleLayout;

    @ViewInject(R.id.register)
    TextView register;

    @ViewInject(R.id.back)
    ImageView back;

    @ViewInject(R.id.tv_find_password)
    private TextView mTv_find_password;

    @ViewInject(R.id.act_login_new_tab_login_normal)
    private SDTabItemCorner mTabLoginNormal;

    @ViewInject(R.id.act_login_new_tab_login_phone)
    private SDTabItemCorner mTabLoginPhone;

    private SDViewNavigatorManager mViewManager;

    private List<Integer> mListSelectIndex;

    /**
     * qqq登录。
     */
    @ViewInject(R.id.qq_login)
    private Button qq_login;
    /**
     * 微博登录
     */

    @ViewInject(R.id.weibo_login)
    private Button weibo_login;
    /**
     * 微信登录
     */

    @ViewInject(R.id.weixin_login)
    private Button weixin_login;

    private String openId;

    //1:qq，2:微信，3：微博
    String platformType = "";
    private ShareUtils su;

    SHARE_MEDIA platform;
    /**
     * 头像。
     */
    String icon = "";
    /**
     * 密码。
     */
    String nick = "";
    /**
     * 分享ID。
     */
    String shareCode;

    long time;

    GetShareIdByCodeDao getShareIdByCodeDao;

    SDFragmentManager fragmentManager;

    LoginByThirdDao loginByThirdDao;

    /** 以下接口在登录成功后被调用 */
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
     */
    IMUserInfoDao imUserInfoDao;
    /** 以上接口在登录成功后被调用 */

    public HiLoginCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        mViewManager = new SDViewNavigatorManager();
        mListSelectIndex = new ArrayList<>();
        getShareIdByCodeDao = new GetShareIdByCodeDaoImpl(this);
        loginByThirdDao = new LoginByThirdDaoImpl(this);
        /** 以下接口在登录成功后被调用 */
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
        listener = new HiLoginListener(this);
    }

    @Override
    protected void setThisListener() {
        back.setOnClickListener(listener);
        register.setOnClickListener(listener);
        mTv_find_password.setOnClickListener(listener);
        qq_login.setOnClickListener(listener);
        weibo_login.setOnClickListener(listener);
        weixin_login.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        getIntentData();
        initFragmentManager();
        initUmeng();
        initShareId();
        changeViewUnLogin();
    }

    @Override
    protected void initViews() {
        setTitlePadding(titleLayout);
    }

    private void getIntentData() {
        mListSelectIndex.add(0);
        mListSelectIndex.add(1);

        if (!mListSelectIndex.contains(getActivity().getmSelectTabIndex())) {
            getActivity().setmSelectTabIndex(0);
        }
    }

    private void initFragmentManager(){
        fragmentManager = new SDFragmentManager(getActivity().getSupportFragmentManager());
    }

    /**
     * 初始化umeng
     */
    private void initUmeng(){
        su = new ShareUtils(getActivity());
    }

    /**
     * 如果是从领取码进来的，要通过分享码，获取分享id
     * 回调View
     * {@link com.miguo.view.GetShareIdByCodeView}
     * {@link }
     */
    private void initShareId(){
        shareCode = "";
        String diamondCode = ClipboardUtils.checkCode(getActivity());
        if(!TextUtils.isEmpty(diamondCode)){
            getShareIdByCodeDao.getShareIdByCode(diamondCode);
        }
    }

    private void changeViewUnLogin() {
        mTabLoginNormal.setTabName("账号登录");
        mTabLoginNormal.setTabTextSizeSp(18);
        mTabLoginNormal.setmPosition(SDTabItemCorner.EnumTabPosition.FIRST);

        mTabLoginPhone.setTabName("快捷登录");
        mTabLoginPhone.setTabTextSizeSp(18);
        mTabLoginPhone.setmPosition(SDTabItemCorner.EnumTabPosition.LAST);

        mViewManager.setItems(new SDViewBase[]{mTabLoginNormal, mTabLoginPhone});

        mViewManager.setmListener(new SDViewNavigatorManager.SDViewNavigatorManagerListener() {
            @Override
            public void onItemClick(View v, int index) {
                switch (index) {
                    case 0: // 正常登录
                        clickLoginNormal();
                        break;
                    case 1: // 快捷登录
                        clickLoginPhone();
                        break;
                    default:
                        break;
                }
            }
        });

        mViewManager.setSelectIndex(getActivity().getmSelectTabIndex(), null, true);
    }

    /**
     * 找回密码
     */
    public void clickFindPassword(){
        Intent intent = new Intent(getActivity(), ModifyPasswordActivity.class);
        intent.putExtra("pageType", "forget");
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    /**
     * 点击返回
     */
    public void clickBack(){
        BaseUtils.finishActivity(getActivity());
    }

    /**
     * 点击注册
     */
    public void clickRegister(){
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        intent.putExtra(UserConstants.SHARE_ID, getShareCode());
        goRegisterActivity(intent);
    }

    public void clickQQLogin(){
        platform = SHARE_MEDIA.QQ;
        goToAuth(platform);
    }

    public void clickWeiboLogin(){
        platform = SHARE_MEDIA.SINA;
        goToAuth(platform);
    }

    public void clickWechatLogin(){
        platform = SHARE_MEDIA.WEIXIN;
        goToAuth(platform);
    }

    /**
     * 正常登录的选项卡被选中
     */
    protected void clickLoginNormal() {
        SDViewUtil.show(mTv_find_password);
        getSDFragmentManager().toggle(R.id.act_login_fl_content, null, HiLoginByMobileFragment.class);
    }

    /**
     * 手机号快捷登录的选项卡被选中
     */
    protected void clickLoginPhone() {
        SDViewUtil.hide(mTv_find_password);
        getSDFragmentManager().toggle(R.id.act_login_fl_content, null, HiLoginQuickByMobileFragment.class);
    }

    /**
     * 跳转到相应的授权页。
     *
     * @param platform
     */
    public void goToAuth(final SHARE_MEDIA platform) {
        su.login(platform, new ILoginCallback() {
            @Override
            public void onSuccess(Map<String, String> data) {
                SDDialogManager.showProgressDialog("正在登录,请稍候...");
//                   printData(data);
                if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                    platformType = "2";
                    openId = data.get("unionid");
                    nick = data.get("nickname");
                    icon = data.get("headimgurl");
                } else if (platform.equals(SHARE_MEDIA.QQ)) {
                    platformType = "1";
                    openId = data.get("openid");
                    icon = data.get("profile_image_url");
                    nick = data.get("screen_name");
                } else if (platform.equals(SHARE_MEDIA.SINA)) {
                    platformType = "3";
                    String returnData = data.get("result");
                    Gson gson = new Gson();

                    HashMap<String, Object> maps = gson.fromJson(returnData, HashMap.class);
                    if (maps.get("id") != null) {

                        openId= maps.get("idstr").toString();
                    }
                    if (maps.get("profile_image_url") != null) {
                        icon = maps.get("profile_image_url").toString();
                    }
                    if (maps.get("screen_name") != null) {
                        nick = maps.get("screen_name").toString();
                    }

                }
                if (TextUtils.isEmpty(openId)) {
                    MGToast.showToast("登录失败");
                    return;
                }
                //防止过快请求
                if ((System.currentTimeMillis() - time) < 100) {
                    return;
                } else {
                    time = System.currentTimeMillis();
                }
                loginByThirdDao.thirdLogin(openId, platformType, icon, nick);
            }

            @Override
            public void onFaild(String msg) {
                MGToast.showToast("登录失败");
            }

            @Override
            public void onCancel() {
                MGToast.showToast("取消登录");
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        su.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 手机号 + 密码登录
     * 手机号 + 验证码登录
     * 成功后的回调
     */
    public void handleLoginSuccess(UserInfoNew user){
        /**
         * 保存用户信息到本地
         */
//        saveUserToLocal(user, mobile, password);
        /**
         * 获取腾讯sign签名
         */
        handlerTencentSign(App.getApplication().getToken());
        /**
         * 保存用户信息SharedPreferences
         */
//        handlerSaveUser(mobile, password);
//        initJpush();
        finishActivity();
    }

    private void finishActivity(){
        if(getActivity() instanceof HiLoginActivity){
            getActivity().finishActivity();
            return;
        }
        BaseUtils.finishActivity(getActivity());
    }

    /**
     * 获取腾讯签名
     *
     * @param token
     */
    private void handlerTencentSign(String token) {
        if (TextUtils.isEmpty(token)) {
            Log.d(tag, "handler tencent sign token is null...");
            return;
        }
        /**
         * {@link #getTencentSignSuccess(ModelGenerateSign)}
         * {@link #getTencentSignError()}
         */
        tencentSignDao.getTencentSign(token);
    }

    /**
     * IM登录
     * @param userId
     * @param usersig
     */
    private void handlerIMLogin(String userId, String usersig) {
        /**
         * {@link #imLoginSuccess()}
         * {@link #imLoginError(String)}
         */
        imLoginDao.imLogin(userId, usersig);
    }

    /**
     * 获取腾讯签名
     * 获取成功后需要调用IM登录
     */
    @Override
    public void getTencentSignSuccess(ModelGenerateSign sign) {
        if (null == sign) {
            return;
        }
        String usersig = sign.getUsersig();
        App.getInstance().setUserSign(usersig);
        MySelfInfo.getInstance().setUserSig(usersig);
        App.getInstance().setUserSign(usersig);
        String userId = MySelfInfo.getInstance().getId();

        if (TextUtils.isEmpty(userId)) {
            UserInfoNew currentInfo = App.getInstance().getCurrentUser();
            if (currentInfo != null) {
                userId = currentInfo.getUser_id();
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
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            /**
             * 不需要回调
             */
            imUserInfoDao.updateTencentNickName(App.getInstance().getCurrentUser().getNick());
            imUserInfoDao.updateTencentAvatar(App.getInstance().getCurrentUser().getIcon());
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
        if (ServerUrl.DEBUG) {
            appId = Constants.SDK_APPID_TEST;
            ccType = Constants.ACCOUNT_TYPE_Test;
        }
        QavsdkControl.getInstance().setAvConfig(appId, ccType + "", userid, userSign);
        QavsdkControl.getInstance().startContext();
    }

    /**
     * 根据领取码获取分享id回调
     */
    @Override
    public void getShareIdError(String message) {
    }

    @Override
    public void getShareIdSucess(String shareId) {
        setShareCode(shareId);
    }

    /**
     * 第三方登录回调
     * @return
     */
    @Override
    public void thirdLoginError(String message) {
        showToast(message);
    }

    @Override
    public void thirdLoginSuccess(User_infoModel userInfoModel, UserInfoNew userInfoNew) {
        if(null != userInfoNew){
            App.getInstance().setCurrentUser(userInfoNew);
        }
        getActivity().finishActivity();
    }

    @Override
    public void thirdLoginUnRegister(ThirdLoginInfo thirdLoginInfo) {
        if(null != thirdLoginInfo){
            handleThirdLoginUnRegister(true, thirdLoginInfo);
        }
    }

    protected void handleThirdLoginUnRegister(boolean third, ThirdLoginInfo thirdLoginInfo) {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        String openId = thirdLoginInfo.getOpenId();
        String type = thirdLoginInfo.getPlatformType();
        String icon = thirdLoginInfo.getIcon();
        String nick = thirdLoginInfo.getNick();

        if (third && !TextUtils.isEmpty(openId)) {
            intent.putExtra(UserConstants.THIRD_OPENID, openId);
            intent.putExtra(UserConstants.THIRD_PLATFORM, type);
            intent.putExtra(UserConstants.THIRD_ICON, icon);
            intent.putExtra(UserConstants.THIRD_NICK, nick);
        }

        intent.putExtra(UserConstants.SHARE_ID, getShareCode());
        goRegisterActivity(intent);
    }

    private void goRegisterActivity(Intent intent){
        /**
         * 如果是从领钻码进来的，则跳注册界面的时候不要销毁当前activity，应该注册成功后回调回来再结束，把结果传给HiHomeActivity
         */
        if(getActivity().isFromDiamond()){
            intent.putExtra(IntentKey.FROM_DIAMOND_TO_LOGIN, getActivity().isFromDiamond());
            BaseUtils.jumpToNewActivityForResult(getActivity(), intent, RequestCode.LOGIN_SUCCESS_FOR_DIAMON);
            return;
        }
        BaseUtils.jumpToNewActivityWithFinish(getActivity(), intent);
    }

    public SDFragmentManager getSDFragmentManager(){
        return fragmentManager;
    }

    public Intent getIntent(){
        return getActivity().getIntent();
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    @Override
    public HiLoginActivity getActivity() {
        return (HiLoginActivity) super.getActivity();
    }
}
