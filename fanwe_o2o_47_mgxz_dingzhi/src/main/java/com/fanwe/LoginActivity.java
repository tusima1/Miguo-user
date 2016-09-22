package com.fanwe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.constant.Constant.EnumLoginState;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.LoginFragment;
import com.fanwe.fragment.LoginPhoneFragment;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.customview.SDTabItemCorner;
import com.fanwe.library.customview.SDTabItemCorner.EnumTabPosition;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.User_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.ThirdLoginInfo;
import com.fanwe.user.presents.LoginHelper;
import com.fanwe.work.AppRuntimeWorker;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simbest.com.sharelib.ILoginCallback;
import simbest.com.sharelib.ShareUtils;

public class LoginActivity extends BaseActivity implements CallbackView {

    public static final String EXTRA_SELECT_TAG_INDEX = "extra_select_tag_index";

    @ViewInject(R.id.ll_tabs)
    private LinearLayout mLl_tabs = null;

    @ViewInject(R.id.tv_find_password)
    private TextView mTv_find_password = null;

    @ViewInject(R.id.act_login_new_tab_login_normal)
    private SDTabItemCorner mTabLoginNormal = null;

    @ViewInject(R.id.act_login_new_tab_login_phone)
    private SDTabItemCorner mTabLoginPhone = null;

    private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();

    private int mSelectTabIndex = 0;

    private List<Integer> mListSelectIndex = new ArrayList<Integer>();

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

    LoginHelper mLoginHelper;
    SHARE_MEDIA platform = null;
    /**
     * 头像。
     */
    String icon = "";
    /**
     * 密码。
     */
    String nick = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_login);
        init();
        //友盟授权登录初始化。
        su = new ShareUtils(this);
        mLoginHelper = new LoginHelper(this);
    }

    private void init() {

        getIntentData();
        initTitle();
        changeViewUnLogin();
        registerClick();

    }

    private void registerClick() {
        mTv_find_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);

            }
        });

        qq_login.setOnClickListener(this);
        weibo_login.setOnClickListener(this);
        weixin_login.setOnClickListener(this);
    }

    private void getIntentData() {
        mListSelectIndex.add(0);
        mListSelectIndex.add(1);

        mSelectTabIndex = getIntent().getIntExtra(EXTRA_SELECT_TAG_INDEX, 0);
        if (!mListSelectIndex.contains(mSelectTabIndex)) {
            mSelectTabIndex = 0;
        }
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("登录");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("注册");
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        startRegisterActivity(false, "", "", "");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        init();
        super.onNewIntent(intent);
    }

    private void changeViewState() {
        EnumLoginState state = AppRuntimeWorker.getLoginState();
        switch (state) {
            case LOGIN_EMPTY_PHONE:
                changeViewUnLogin();
                break;
            case UN_LOGIN:
                changeViewUnLogin();
                break;
            case LOGIN_NEED_BIND_PHONE:
                changeViewUnLogin();
                showBindPhoneDialog();
                break;
            case LOGIN_NEED_VALIDATE:
                changeViewUnLogin();
                break;

            default:
                break;
        }
    }

    private void showBindPhoneDialog() {
        Intent intent = new Intent(getApplicationContext(), BindMobileActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeViewLoginEmptyPhone() {
        mLl_tabs.setVisibility(View.GONE);
        clickLoginNormal();
    }

    private void changeViewUnLogin() {
        mTabLoginNormal.setTabName("账号登录");
        mTabLoginNormal.setTabTextSizeSp(18);
        mTabLoginNormal.setmPosition(EnumTabPosition.FIRST);

        mTabLoginPhone.setTabName("快捷登录");
        mTabLoginPhone.setTabTextSizeSp(18);
        mTabLoginPhone.setmPosition(EnumTabPosition.LAST);

        mViewManager.setItems(new SDViewBase[]{mTabLoginNormal, mTabLoginPhone});

        mViewManager.setmListener(new SDViewNavigatorManagerListener() {
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

        mViewManager.setSelectIndex(mSelectTabIndex, null, true);
    }

    @Override
    public void onClick(View v) {
        SHARE_MEDIA platform = null;
        switch (v.getId()) {
            case R.id.qq_login:
                platform = SHARE_MEDIA.QQ;
                goToAuth(platform);
                break;
            case R.id.weibo_login:
                platform = SHARE_MEDIA.SINA;
                //	startRegisterActivity(true,"1",icon,nick);
                goToAuth(platform);
                break;
            case R.id.weixin_login:
                platform = SHARE_MEDIA.WEIXIN;
                goToAuth(platform);
                break;
            default:
                break;
        }
    }

    public void printData(Map<String, String> data) {
        StringBuffer str = new StringBuffer();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            str.append(entry.getKey() + "--->" + entry.getValue() + "\n");
        }
        Log.d("11", str.toString());
      //  testViews.setText(str.toString());

    }

    public void printData(String datas){
       // ((EditText)findViewById(R.id.testViews2)).setText(datas);
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
           //     printData(data);
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
                    String returnData = (String) data.get("result");
                    Gson gson = new Gson();
                    HashMap<String, Object> maps = gson.fromJson(returnData, HashMap.class);
                    if (maps.get("id") != null) {
                        openId = maps.get("id").toString();
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
              //  printData("icon:"+icon+" nick:"+nick +" openid:"+openId);
               mLoginHelper.thirdLogin(openId, platformType, icon, nick, LoginActivity.this);

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

    /**
     * 正常登录的选项卡被选中
     */
    protected void clickLoginNormal() {
        SDViewUtil.show(mTv_find_password);
        getSDFragmentManager().toggle(R.id.act_login_fl_content, null, LoginFragment.class);
    }

    /**
     * 手机号快捷登录的选项卡被选中
     */
    protected void clickLoginPhone() {
        SDViewUtil.hide(mTv_find_password);
        getSDFragmentManager().toggle(R.id.act_login_fl_content, null, LoginPhoneFragment.class);
    }

    protected void startRegisterActivity(boolean third, ThirdLoginInfo thirdLoginInfo) {
        String openId = thirdLoginInfo.getOpenId();
        String type = thirdLoginInfo.getPlatformType();
        String icon = thirdLoginInfo.getIcon();
        String nick = thirdLoginInfo.getNick();
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        if (third && !TextUtils.isEmpty(openId)) {
            intent.putExtra(UserConstants.THIRD_OPENID, openId);
            intent.putExtra(UserConstants.THIRD_PLATFORM, type);
            intent.putExtra(UserConstants.THIRD_ICON, icon);
            intent.putExtra(UserConstants.THIRD_NICK, nick);
        }
        startActivity(intent);
        finish();

    }

    protected void startRegisterActivity(boolean third, String type, String icon, String nick) {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        if (third && !TextUtils.isEmpty(openId)) {
            intent.putExtra(UserConstants.THIRD_OPENID, openId);
            intent.putExtra(UserConstants.THIRD_PLATFORM, type);
            intent.putExtra(UserConstants.THIRD_ICON, icon);
            intent.putExtra(UserConstants.THIRD_NICK, nick);
        }
        startActivity(intent);
        finish();

    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case LOGIN_SUCCESS:
                JpushHelper.registerAll();
                finish();
                break;

            default:
                break;
        }
    }


    protected void dealLoginSuccess(User_infoModel actModel) {
        LocalUserModel.dealLoginSuccess(actModel, true);
        Activity lastActivity = SDActivityManager.getInstance().getLastActivity();
        if (lastActivity instanceof MainActivity) {
            finish();
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, final List datas) {
        switch (method) {
            case UserConstants.THIRD_LOGIN_SUCCESS:
                if (datas != null && datas.size() > 0) {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dealLoginSuccess((User_infoModel) datas.get(0));
                        }
                    });

                }

                break;
            case UserConstants.THIRD_LOGIN_UNREGISTER:
                if (datas != null && datas.size() > 0) {
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ThirdLoginInfo thirdLoginInfo = (ThirdLoginInfo) datas.get(0);
                            startRegisterActivity(true, thirdLoginInfo);
                        }
                    });

                }

                break;

        }

    }

    @Override
    public void onFailue(String responseBody) {
        MGToast.showToast(responseBody);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        su.onActivityResult(requestCode, resultCode, data);
    }
}