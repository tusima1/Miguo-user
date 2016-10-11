package com.fanwe;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.base.CallbackView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.HomeFragment;
import com.fanwe.fragment.MarketFragment;
import com.fanwe.fragment.MyFragment2;
import com.fanwe.fragment.StoreListContainerFragment;
import com.fanwe.home.model.Host;
import com.fanwe.home.model.Room;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.customview.SDTabItemBottom;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.model.LocalUserModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.service.AppUpgradeService;
import com.fanwe.umeng.UmengEventStatistics;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.presents.LoginHelper;
import com.fanwe.user.view.UserHomeActivity;
import com.fanwe.utils.DataFormat;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.LiveStartActivity;
import com.miguo.live.views.LiveStartAuthActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.dialog.GetDiamondInputDialog;
import com.miguo.live.views.dialog.GetDiamondLoginDialog;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.view.PlayBackActivity;
import com.miguo.utils.MGUIUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class MainActivity extends BaseActivity implements CallbackView {
    private Context mContext = MainActivity.this;

    /**
     * 商家id (int)
     */
    public static final String EXTRA_MERCHANT_ID = "extra_merchant_id";
    public static final String EXTRA_SHOP_ID = "extra_shop_id";
    public static final String EXTRA_GOODS_ID = "extra_goods_id";

    /**
     * http://m.w2.mgxz.com/user/shop/uid/88025143-194f-4705-991b-7f5a3587dc9c
     * 门店详情
     */
    private final static String SHOP_DETAIL = "^https?://[^/]+.mgxz.com/index/retail/id/([^/\\s]+)";
    /**
     * 他的小店.
     */
    private final static String SHOP_PATTERN = "^https?://[^/]+.mgxz.com/user/shop/uid/([^/\\s]+)";
    /**
     * 团购详情.
     */
    private final static String SHOPPING_DETAIL = "^https?://[^/]+.mgxz.com/index/detail/id/([^/\\s]+)";


    @SuppressWarnings("deprecation")
    @ViewInject(R.id.act_main_tab_0)
    private SDTabItemBottom mTab0 = null;

    @SuppressWarnings("deprecation")
    @ViewInject(R.id.act_main_tab_1)
    private SDTabItemBottom mTab1 = null;


    @SuppressWarnings("deprecation")
    @ViewInject(R.id.act_main_tab_2)
    private SDTabItemBottom mTab2 = null;


    @SuppressWarnings("deprecation")
    @ViewInject(R.id.act_main_tab_3)
    private SDTabItemBottom mTab3 = null;

    @SuppressWarnings("deprecation")
    @ViewInject(R.id.act_main_tab_4)
    private SDTabItemBottom mTab4 = null;

    private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();

    private MyFragment2 mFragMyAccount = new MyFragment2();

    private long mExitTime = 0;
    private int preTab = 0;// 上次点击的tab标签页
    private String preHomeCityID = "";//记录首页cityid-->0为异常
    private LoginHelper mLoginHelper;
    private LiveHttpHelper liveHttpHelper;
    private ClipboardManager clipboardManager;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaiduMapManager.getInstance().init(App.getInstance().getApplicationContext());
        setContentView(R.layout.act_main);
        mLoginHelper = new LoginHelper(MainActivity.this);
        liveHttpHelper = new LiveHttpHelper(this, this);
        init();
    }

    private void init() {
        //检测更新
        startService(new Intent(MainActivity.this, AppUpgradeService.class));
        initBottom();
        JpushHelper.initJPushConfig();
//        MessageHelper.updateMessageCount();
        initOthers();
        initUserInfo();
        initDict();
    }

    private void initDict() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new LiveHttpHelper(null, null).getBussDictionInfo("Client");
            }
        }).start();
    }


    //初始化用户信息。
    private void initUserInfo() {
        //取剪切板中的领取码
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip()) {
            code = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
            App.getInstance().code = code;
        }
        LocalUserModel userModel = AppHelper.getLocalUser();

        //当前还未登录，并且用户存储中的用户信息不为空。
        if (TextUtils.isEmpty(App.getInstance().getToken()) && userModel != null) {
            //需要登录
            String userid = userModel.getUser_mobile();
            String password = userModel.getUser_pwd();
            if (!TextUtils.isEmpty(userid) && !TextUtils.isEmpty(password)) {
                mLoginHelper.doLogin(userid, password, 0, true, false);
            } else {
                showDialogLogin();
            }
        } else {
            if (!TextUtils.isEmpty(App.getInstance().getToken())) {
                //不需要登录
                //分享码
                doCode();
            }
            if (userModel == null) {
                //需要登录
                showDialogLogin();
            }
        }
    }

    private void doCode() {
        if (App.getInstance().isShowCode) {
            if ("mgxz".equals(code) || TextUtils.isEmpty(code)) {
                if (App.getInstance().isAlreadyShowCode) {
                    return;
                } else {
                    App.getInstance().isAlreadyShowCode = true;
                }
                final GetDiamondInputDialog dialog = new GetDiamondInputDialog(MainActivity.this);
                dialog.setSubmitListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //分享码
                        if (TextUtils.isEmpty(dialog.getCode())) {
                            App.getInstance().isShowCode = false;
                        } else {
                            liveHttpHelper.getUseReceiveCode(dialog.getCode());
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
                dialog.show();
            } else {
                liveHttpHelper.getUseReceiveCode(code);
            }
        }
    }

    public void showDialogLogin() {
        final GetDiamondLoginDialog dialog = new GetDiamondLoginDialog(MainActivity.this);
        dialog.setSubmitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                dialog.dismiss();
            }
        });
        dialog.setCloseListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initOthers() {
        // 初始化上次的cityID
        preHomeCityID = AppRuntimeWorker.getCity_id();
        umengTag = "首页";
    }

    private void initBottom() {
        mTab0.setBackgroundTextTitleNumber(R.drawable.bg_number);
        mTab1.setBackgroundTextTitleNumber(R.drawable.bg_number);
        mTab2.setBackgroundTextTitleNumber(R.drawable.bg_number);
        mTab3.setBackgroundTextTitleNumber(R.drawable.bg_number);
        mTab4.setBackgroundTextTitleNumber(R.drawable.bg_number);

        mTab0.setTextTitle(SDResourcesUtil.getString(R.string.home));
        mTab1.setTextTitle(SDResourcesUtil.getString(R.string.supplier));
        mTab2.setTextTitle("我要直播");
        mTab3.setTextTitle(SDResourcesUtil.getString(R.string.market));
        mTab4.setTextTitle(SDResourcesUtil.getString(R.string.mine));

        mTab0.getmAttr().setmImageNormalResId(R.drawable.tab_home_normal);
        mTab1.getmAttr().setmImageNormalResId(R.drawable.tab_seller_normal);
        mTab2.getmAttr().setmImageNormalResId(R.drawable.tab_live_normal);
        mTab3.getmAttr().setmImageNormalResId(R.drawable.tab_market_normal);
        mTab4.getmAttr().setmImageNormalResId(R.drawable.tab_my_normal);

        mTab0.getmAttr().setmImageSelectedResId(R.drawable.tab_home_pressed);
        mTab1.getmAttr().setmImageSelectedResId(R.drawable.tab_seller_pressed);
        mTab2.getmAttr().setmImageSelectedResId(R.drawable.tab_live_pressed);
        mTab3.getmAttr().setmImageSelectedResId(R.drawable.tab_market_pressed);
        mTab4.getmAttr().setmImageSelectedResId(R.drawable.tab_my_pressed);


        mTab0.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);
        mTab1.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);
        mTab2.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);
        mTab3.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);
        mTab4.getmAttr().setmTextColorNormalResId(R.color.text_home_menu_normal);

        mTab0.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);
        mTab1.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);
        mTab2.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);
        mTab3.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);
        mTab4.getmAttr().setmTextColorSelectedResId(R.color.text_home_menu_selected);

        SDViewBase[] items = new SDViewBase[]{mTab0, mTab1, mTab2, mTab3, mTab4};

        mViewManager.setItems(items);
        mViewManager.setmListener(new SDViewNavigatorManagerListener() {
            @Override
            public void onItemClick(View v, int index) {
                switch (index) {
                    case 0:
                        click0();
                        break;
                    case 1:
                        click1();
                        break;
                    case 2:
                        click2();
                        break;
                    case 3:
                        click3();
                        break;
                    case 4:
                        click4();
                }
                if (index != 2)
                    preTab = index;
            }
        });
        // 如果要加入guideActivity请注释这行代码,并打开initGuideResult();方法...=.=
        mViewManager.setSelectIndex(0, mTab0, true);
    }

    public void setSelectIndex(int index, View view, boolean notify) {
        if (mViewManager != null) {
            mViewManager.setSelectIndex(index, view, notify);
        }
    }

    /**
     * 首页
     */
    protected void click0() {
        UmengEventStatistics.sendEvent(this, UmengEventStatistics.MAIN_1);
        getSDFragmentManager().toggle(R.id.act_main_fl_content, null, HomeFragment.class);
        refreshHomeFragment();
    }

    /**
     * 商家
     */
    protected void click1() {
        UmengEventStatistics.sendEvent(this, UmengEventStatistics.MAIN_2);
        getSDFragmentManager().toggle(R.id.act_main_fl_content, null, StoreListContainerFragment
                .class);

    }

    /**
     * 直播
     */
    protected void click2() {
        UmengEventStatistics.sendEvent(this, UmengEventStatistics.MAIN_2);

        if (TextUtils.isEmpty(App.getInstance().getToken()))// 未登录 以后加入是不是主播的判断。
        {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            String is_host = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIs_host();
            if ("0".equals(is_host)) {
                //未认证
                Intent intent = new Intent(this, LiveStartAuthActivity.class);
                intent.putExtra("pageType", "start");
                startActivity(intent);
            } else if ("1".equals(is_host)) {
                //已认证
                startActivity(new Intent(this, LiveStartActivity.class));
            } else {
                //认证中
                Intent intent = new Intent(this, LiveStartAuthActivity.class);
                intent.putExtra("pageType", "wait");
                startActivity(intent);
            }
        }

        switch (preTab) {
            case 0:
                mViewManager.setSelectIndex(preTab, mTab0, true);
                break;
            case 1:
                mViewManager.setSelectIndex(preTab, mTab1, true);
                break;
            case 3:
                mViewManager.setSelectIndex(preTab, mTab3, true);
                break;
            case 4:
                mViewManager.setSelectIndex(preTab, mTab4, true);
                mFragMyAccount.refreshFragment();
                break;
            default:
                break;
        }

    }

    /**
     * 市场
     */
    protected void click3() {
        UmengEventStatistics.sendEvent(this, UmengEventStatistics.MAIN_3);
        if (TextUtils.isEmpty(App.getInstance().getToken()))  // 未登录
        {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            getSDFragmentManager().toggle(R.id.act_main_fl_content, null, MarketFragment.class);
            if (preTab == 0 || preTab == 2 || preTab == 3) {
                if (((MarketFragment) getSDFragmentManager().getmFragmentLastToggle())
                        .mPtrlv_content != null) {
                    ((MarketFragment) getSDFragmentManager().getmFragmentLastToggle())
                            .mPtrlv_content.setRefreshing();
                }
            }
        }

    }

    /**
     * 我的账户
     */
    protected void click4() {
        UmengEventStatistics.sendEvent(this, UmengEventStatistics.MAIN_4);

        if (TextUtils.isEmpty(App.getInstance().getToken()))  // 未登录
        {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
//            mFragMyAccount = (MyFragment2) getSDFragmentManager().toggle(R.id.act_main_fl_content,
//                    null,
//                    MyFragment2.class);
            getSDFragmentManager().toggle(R.id.act_main_fl_content, null, mFragMyAccount);
        }
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        super.onEventMainThread(event);
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case LOGIN_SUCCESS:
                mViewManager.setSelectIndex(preTab, mTab0, true);
                //分享码
                doCode();
                break;
            case LOGOUT:
                mTab3.setTextTitleNumber(null);
                removeMyAccountFragment();
                mViewManager.setSelectIndex(0, mTab0, true);
                break;
            case TEMP_LOGIN:
                removeMyAccountFragment();
                mViewManager.setSelectIndex(0, mTab0, true);
                break;
            case UN_LOGIN:
                removeMyAccountFragment();
                mViewManager.setSelectIndex(0, mTab0, true);
                break;
            default:
                break;
        }

    }

    private void removeMyAccountFragment() {
        getSDFragmentManager().remove(mFragMyAccount);
//        mFragMyAccount = null;
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    private void exitApp() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            MGToast.showToast("再按一次退出!");
        } else {
            App.getApplication().exitApp(true);
        }
        mExitTime = System.currentTimeMillis();
    }


    /**
     * //获取完整的域名
     *
     * @param text 获取浏览器分享出来的text文本
     */
    public static boolean getCompleteUrl(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(text);

        boolean result = matcher.find();
        return result;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == MyCaptureActivity.RESULT_CODE_SCAN_SUCCESS) {
                String result = data.getStringExtra("extra_result_success_string");

                //他的小店.
                if (getCompleteUrl(result, SHOP_PATTERN)) {
                    String user_id = result.split("\\/")[result.split("\\/").length - 1];

                    Intent intentStore = new Intent(this, DistributionStoreWapActivity.class);
                    intentStore.putExtra("user_id", user_id);
                    intentStore.putExtra("url", result);

                    startActivity(intentStore);
                } else if (getCompleteUrl(result, SHOP_DETAIL)) {
                    //门店详情
                    String extra_merchant_id = result.split("\\/")[result.split("\\/").length - 1];
                    Intent intentStore = new Intent(this, StoreDetailActivity.class);
                    intentStore.putExtra(EXTRA_MERCHANT_ID, extra_merchant_id);
                    startActivity(intentStore);

                } else if (getCompleteUrl(result, SHOPPING_DETAIL)) {
                    //团购详情
                    String mId = result.split("\\/")[result.split("\\/").length - 1];
                    Intent intentStore = new Intent(this, TuanDetailActivity.class);
                    intentStore.putExtra(EXTRA_GOODS_ID, mId);
                    startActivity(intentStore);
                } else {
                    MGToast.showToast("无法识别。");
                }

            }
        } else if (resultCode == MarketFragment.CITY_RESULT) {
            Bundle cityData = data.getExtras();
            if (cityData == null) {
                MGToast.showToast("位置信息获取失败!");
                return;
            }
            String cityName = cityData.getString("city", "");
            String cityID = cityData.getString("cityID", "");
            preHomeCityID = "";
            if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(cityID)) {
                // 通知城市的name和id获取正常
                Fragment lastToggle = getSDFragmentManager().getmFragmentLastToggle();
                if (lastToggle instanceof MarketFragment) {
                    ((MarketFragment) lastToggle).setCityIdTitle(cityName, cityID);
                }
            }
        } else if (resultCode == 8888) {
            //从首页的城市选择Activity回来
            Fragment lastToggle = getSDFragmentManager().getmFragmentLastToggle();
            if (lastToggle instanceof HomeFragment) {
                ((HomeFragment) lastToggle).refreshData();
                preHomeCityID = AppRuntimeWorker.getCity_id();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!AppHelper.isLogin()) {
            // 如果未登录,跳转到首页
            if (preTab == 2 || preTab == 4) {
                mViewManager.setSelectIndex(0, mTab0, true);
            }
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        judgeCode(intent);
        // onNewIntent在onRestart 前调用.
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int index = bundle.getInt("index", 0);
            switch (index) {
                case 0:
                    mViewManager.setSelectIndex(index, mTab0, true);
                    break;
                case 1:
                    mViewManager.setSelectIndex(index, mTab1, true);
                    break;
                case 2:
                    mViewManager.setSelectIndex(index, mTab2, true);
                    break;
                case 3:
                    mViewManager.setSelectIndex(index, mTab3, true);
                    break;
                case 4:
                    mViewManager.setSelectIndex(index, mTab4, true);
                    mFragMyAccount.refreshFragment();
                    break;
                default:
                    break;
            }
        }
    }

    private void judgeCode(Intent intent) {
        if (intent != null) {
            String dataString = intent.getDataString();
            if (!TextUtils.isEmpty(dataString)) {
                if (dataString.contains("miguoxiaozhan")) {
                    App.getInstance().isShowCode = true;
                    App.getInstance().isAlreadyShowCode = false;
                    initUserInfo();
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int index = bundle.getInt("index", 0);
            switch (index) {
                case 0:
                    mViewManager.setSelectIndex(index, mTab0, true);
                    break;
                case 1:
                    mViewManager.setSelectIndex(index, mTab1, true);
                    break;
                case 2:
                    mViewManager.setSelectIndex(index, mTab2, true);
                    break;
                case 3:
                    mViewManager.setSelectIndex(index, mTab3, true);
                    break;
                case 4:
                    mViewManager.setSelectIndex(index, mTab4, true);
                    mFragMyAccount.refreshFragment();
                    break;
                default:
                    break;
            }
        }
        refreshHomeFragment();
    }

    private void refreshHomeFragment() {
        /** 城市变动的自动刷新 **/
        String city_id = AppRuntimeWorker.getCity_id();
        Fragment isHomeFragment = getSDFragmentManager().getmFragmentLastToggle();
        if (!TextUtils.isEmpty(preHomeCityID) && !preHomeCityID.equals(city_id) && isHomeFragment instanceof
                HomeFragment) {
            ((HomeFragment) isHomeFragment).refreshData();
            preHomeCityID = city_id;
        }
    }


    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (LiveConstants.USE_RECEIVE_CODE.equals(method)) {
            List<Room> items = datas;
            if (!SDCollectionUtil.isEmpty(items)) {
                if (clipboardManager != null)
                    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "mgxz"));
                Room room = items.get(0);
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
                                Intent intent = new Intent(MainActivity.this, UserHomeActivity.class);
                                intent.putExtra("id", room.getHost().getUid());
                                startActivity(intent);
                                return;
                            }
                        }
                    }
                    //异常数据
                    MGToast.showToast("异常数据");
                    return;
                }
            } else {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (App.getInstance().isAlreadyShowCode) {
                            return;
                        } else {
                            App.getInstance().isAlreadyShowCode = true;
                        }
                        final GetDiamondInputDialog dialog = new GetDiamondInputDialog(MainActivity.this);
                        dialog.setSubmitListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //分享码
                                if (TextUtils.isEmpty(dialog.getCode())) {
                                    App.getInstance().isShowCode = false;
                                } else {
                                    liveHttpHelper.getUseReceiveCode(dialog.getCode());
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
                        dialog.show();
                    }
                });
            }
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    protected void onDestroy() {
        App.getInstance().isShowCode = true;
        App.getInstance().isAlreadyShowCode = false;
        super.onDestroy();

    }

    private void gotoLiveActivity(Room room) {
        Intent intent = new Intent(mContext, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.MEMBER);
        MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
        addCommonData(room);
        BaseUtils.jumpToNewActivity(MainActivity.this, intent);
    }

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

        Intent intent = new Intent(mContext, PlayBackActivity.class);
        Bundle data = new Bundle();
        data.putString("chat_room_id", chat_room_id);
        data.putString("file_size", file_size);
        data.putString("duration", duration);
        data.putString("file_id", file_id);
        data.putString("vid", vid);
        data.putString("playset", playset);
        intent.putExtras(data);
        BaseUtils.jumpToNewActivity(MainActivity.this, intent);
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

}
