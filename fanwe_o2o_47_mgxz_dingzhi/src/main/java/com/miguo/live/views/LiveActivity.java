package com.miguo.live.views;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.LoginActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.constant.GiftId;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.SellerDetailInfo;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.utils.SDDateUtil;
import com.miguo.live.adapters.HeadTopAdapter;
import com.miguo.live.adapters.LiveChatMsgListAdapter;
import com.miguo.live.adapters.PagerBaoBaoAdapter;
import com.miguo.live.adapters.PagerRedPacketAdapter;
import com.miguo.live.interf.LiveRecordListener;
import com.miguo.live.interf.LiveSwitchScreenListener;
import com.miguo.live.model.LiveChatEntity;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getAudienceCount.ModelAudienceCount;
import com.miguo.live.model.getAudienceList.ModelAudienceInfo;
import com.miguo.live.model.getGiftInfo.GiftListBean;
import com.miguo.live.model.getHostInfo.ModelHostInfo;
import com.miguo.live.model.getReceiveCode.ModelReceiveCode;
import com.miguo.live.model.getStoresRandomComment.ModelStoresRandomComment;
import com.miguo.live.presenters.LiveCommonHelper;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.presenters.ShopAndProductView;
import com.miguo.live.presenters.TencentHttpHelper;
import com.miguo.live.receiver.NetWorkStateReceiver;
import com.miguo.live.views.category.dialog.LiveBackDialogCategory;
import com.miguo.live.views.customviews.HostBottomToolView;
import com.miguo.live.views.customviews.HostMeiToolView;
import com.miguo.live.views.customviews.HostRedPacketTimeView;
import com.miguo.live.views.customviews.HostTopView;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.customviews.UserBottomToolView;
import com.miguo.live.views.customviews.UserHeadTopView;
import com.miguo.live.views.danmu.DanmuBean;
import com.miguo.live.views.danmu.Danmukiller;
import com.miguo.live.views.definetion.IntentKey;
import com.miguo.live.views.dialog.LiveBackDialog;
import com.miguo.live.views.gift.BigGifView;
import com.miguo.live.views.gift.SmallGifView;
import com.miguo.utils.MGLog;
import com.miguo.utils.MGUIUtil;
import com.miguo.utils.test.MGTimer;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.tencent.TIMUserProfile;
import com.tencent.av.TIMAvManager;
import com.tencent.av.sdk.AVView;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.LiveInfoJson;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.presenters.EnterLiveHelper;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;
import com.tencent.qcloud.suixinbo.presenters.LoginHelper;
import com.tencent.qcloud.suixinbo.presenters.OKhttpHelper;
import com.tencent.qcloud.suixinbo.presenters.ProfileInfoHelper;
import com.tencent.qcloud.suixinbo.presenters.viewinface.EnterQuiteRoomView;
import com.tencent.qcloud.suixinbo.presenters.viewinface.LiveView;
import com.tencent.qcloud.suixinbo.presenters.viewinface.ProfileView;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.tencent.qcloud.suixinbo.utils.SxbLog;
import com.tencent.qcloud.suixinbo.views.customviews.BaseActivity;
import com.tencent.qcloud.suixinbo.views.customviews.HeartLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import master.flame.danmaku.ui.widget.DanmakuView;


/**
 * 直播类(用户+主播)
 * backup
 */
public class LiveActivity extends BaseActivity implements ShopAndProductView, EnterQuiteRoomView,
        LiveView, View.OnClickListener, ProfileView, CallbackView, UserBottomToolView.OnGiftSendListener {
    public static final String TAG = LiveActivity.class.getSimpleName();
    private static final int GETPROFILE_JOIN = 0x200;
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    /**
     * 取商品和门店相关信息。
     */
    private SellerHttpHelper mSellerHttpHelper;
    private EnterLiveHelper mEnterRoomHelper;
    private ProfileInfoHelper mUserInfoHelper;
    private LiveHelper mLiveHelper;
    private LoginHelper mTLoginHelper;
    private ArrayList<LiveChatEntity> mArrayListChatEntity;
    private LiveChatMsgListAdapter mChatMsgListAdapter;
    private static final int MINFRESHINTERVAL = 500;
    private static final int UPDAT_WALL_TIME_TIMER_TASK = 1;
    private static final int TIMEOUT_INVITE = 2;
    private boolean mBoolRefreshLock = false;
    private boolean mBoolNeedRefresh = true;
    private final Timer mTimer = new Timer();
    private ArrayList<LiveChatEntity> mTmpChatList = new ArrayList<LiveChatEntity>();//缓冲队列
    private TimerTask mTimerTask = null;
    private static final int REFRESH_LISTVIEW = 5;

    /**
     * 更新红包上面的时间 。
     */
    private static final int REFRESH_RED_TIME = 20;
    /**
     * 邀请对话框
     */
    private Dialog mMemberDg, inviteDg;
    /**
     * 跳动红星
     */
    private HeartLayout mHeartLayout;
    private HeartBeatTask mHeartBeatTask;//心跳
    private GetAudienceTask mGetAudienceTask;//取观众 列表。


    private LinearLayout mHostLeaveLayout;
    private long mSecond = 0;
    private Timer mHearBeatTimer, mVideoTimer, mAudienceTimer, mRedPacketTimer;
    private VideoTimerTask mVideoTimerTask;//计时器
    private int thumbUp = 0;

    private int watchCount = 0;
    private boolean bCleanMode = false;
    private static boolean mBeatuy = false;//美颜
    private static boolean mWhite = true;//美白

    private boolean mProfile;//默认是美白
    private boolean bFirstRender = true;

    private String backGroundId;

    private ArrayList<String> mRenderUserList = new ArrayList<>();
    private View root;
    private UserHeadTopView mUserHeadTopView;
    private LiveRecordDialogHelper mRecordHelper;
    private LiveOrientationHelper mOrientationHelper;
    private LiveCommonHelper mCommonHelper;
    private HostTopView mHostTopView;
    private HostMeiToolView mHostBottomMeiView2;
    private TencentHttpHelper tencentHttpHelper;
    private LiveHttpHelper mLiveHttphelper;
    private HostRedPacketTimeView mHostRedPacketCountDownView;

    /**
     * 头部头像adapter.
     */
    private HeadTopAdapter mHeadTopAdapter;

    /**
     * 用户的红包列表。
     *
     * @param savedInstanceState
     */
    private PagerRedPacketAdapter mRedPacketAdapter;

    private PagerBaoBaoAdapter mBaoBaoAdapter;

    boolean isAnchor = false;
    boolean isRecording = false;


    /**
     * danmu
     *
     * @param savedInstanceState
     */
    DanmakuView danmakuView;
    Danmukiller danmukiller;

    /**
     * 小礼物动画view
     *
     * @param savedInstanceState
     */
    SmallGifView smallGifView;
    /**
     * 大礼物动画
     */
    BigGifView bigGifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setActivityParams();
        getIntentData();
        findViews();
        initHelper();
        checkUserAndPermission();
        SDEventManager.register(this);
    }

    public void onEventMainThread(SDBaseEvent event) {
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            case RECHARGE_DIAMOND:
                if (mUserBottomTool != null) {
                    mUserBottomTool.refreshGift();
                }
                break;
            case FOCUS_CHANGE_YES:
                if (mUserHeadTopView != null) {
                    mUserHeadTopView.setFocusStatus(true);
                }
                break;
            case FOCUS_CHANGE_NO:
                if (mUserHeadTopView != null) {
                    mUserHeadTopView.setFocusStatus(false);
                }
                break;
            default:
                break;
        }
    }

    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WAKE_LOCK);
            if ((checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        }
    }

    private void getIntentData() {
        isAnchor = getIntent().getBooleanExtra(IntentKey.IS_ANCHOR, false);
    }

    private void findViews() {
        root = findViewById(R.id.root);
        mHostBottomToolView1 = (HostBottomToolView) findViewById(R.id.host_bottom_layout);//主播的工具栏1
        mHostBottomMeiView2 = ((HostMeiToolView) findViewById(R.id.host_mei_layout));//主播的美颜工具2
        mUserBottomTool = (UserBottomToolView) findViewById(R.id.normal_user_bottom_tool);//用户的工具栏
        mUserBottomTool.setOnGiftSendListener(this);

        mVideoMemberCtrlView = (LinearLayout) findViewById(R.id.video_member_bottom_layout);
        //直播2的工具栏
        mHostLeaveLayout = (LinearLayout) findViewById(R.id.ll_host_leave);//主播离开(断开)界面
        // mVideoChat = (TextView) findViewById(R.id.video_interact);//(腾讯)互动连线图标
        mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);//飘心区域

        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);

        //video_member_bottom_layout 直播2的工具栏
        BtnCtrlVideo = (TextView) findViewById(R.id.camera_controll);
        BtnCtrlMic = (TextView) findViewById(R.id.mic_controll);
        BtnHungup = (TextView) findViewById(R.id.close_member_video);

        /**
         * 弹幕
         */
        danmakuView = (DanmakuView) findViewById(R.id.danmuku);
        smallGifView = (SmallGifView) findViewById(R.id.small_gift_view);
        bigGifView = (BigGifView) findViewById(R.id.biggift_view);

    }

    private void setActivityParams() {
        MGTimer.showTime();
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager
                .LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_live_mg);
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_SURFACE_CREATED);
        intentFilter.addAction(Constants.ACTION_HOST_ENTER);
        intentFilter.addAction(Constants.ACTION_CAMERA_OPEN_IN_LIVE);
        intentFilter.addAction(Constants.ACTION_CAMERA_CLOSE_IN_LIVE);
        intentFilter.addAction(Constants.ACTION_SWITCH_VIDEO);
        //主播离开
        intentFilter.addAction(Constants.ACTION_HOST_LEAVE);
        registerReceiver(mBroadcastReceiver, intentFilter);

        //注册网络连接监听
        registerReceiver(mNetWorkReceiver, NetWorkStateReceiver.NETWORK_FILTER);
    }

    private void initHelper() {
        mTLoginHelper = new LoginHelper(this, this);
        mEnterRoomHelper = new EnterLiveHelper(this, this);
        mSellerHttpHelper = new SellerHttpHelper(this, this);
        //房间内的交互协助类
        mLiveHelper = new LiveHelper(this, this);
        // 用户资料类
        mUserInfoHelper = new ProfileInfoHelper(this);
        tencentHttpHelper = new TencentHttpHelper(this);
//        root = findViewById(R.id.root);
        //屏幕方向管理,初始化
        mOrientationHelper = new LiveOrientationHelper();
        //公共功能管理类
        mCommonHelper = new LiveCommonHelper(mLiveHelper, this);

        mHostBottomToolView1.setmLiveView(this);
        mHostBottomToolView1.setNeed(mCommonHelper, mLiveHelper, this);
        mHostBottomMeiView2.setNeed(this, mCommonHelper);

        /**mNetWorkReceiver
         * 弹幕
         */

        danmukiller = new Danmukiller(this.getApplicationContext());
        danmukiller.setDanmakuView(danmakuView);
    }


    MgCallback imLoginSuccessCallback = new MgCallback() {
        @Override
        public void onErrorResponse(String message, String errorCode) {
            MGToast.showToast("进入房间失败");
            finish();
        }

        @Override
        public void onSuccessResponse(String responseBody) {
            super.onSuccessResponse(responseBody);
            if (QavsdkControl.getInstance().getAVContext() == null) {
                startAVSDK();
            }
            enterRoom();
        }
    };

    /**
     * 判断用户是否已经登录，并注册 了腾讯 号。
     */
    public void checkUserAndPermission() {
        String token = App.getInstance().getToken();
        boolean imLoginSuccess = App.getInstance().isImLoginSuccess();
//      boolean isAvStart = App.getInstance().isAvStart();
        boolean isHost = isAnchor;
        String useSign = App.getInstance().getUserSign();


        if (TextUtils.isEmpty(token)) {
            goToLoginActivity();
        }
        //直播已经初始化。
        String userid = MySelfInfo.getInstance().getId();
        if (TextUtils.isEmpty(userid)) {
            UserCurrentInfo userCurrentInfo = App.getInstance().getmUserCurrentInfo();
            //userCurrentInfo 一定不为null
            if (userCurrentInfo.getUserInfoNew() != null) {
                userid = userCurrentInfo.getUserInfoNew().getUser_id();
                if (TextUtils.isEmpty(userid)) {
                    MySelfInfo.getInstance().setId(userid);
                    goToLoginActivity();
                }
            }
        }

        //im login callback code...
        Log.e(TAG, "user sign: " + useSign);
        if (isHost) {
            //主播进来
            if (QavsdkControl.getInstance().getAVContext() == null) {
                startAVSDK();
            }
            enterRoom();
        } else {
            //用户进来
            if (imLoginSuccess) {
                boolean value = MySelfInfo.getInstance().isCreateRoom();
                mTLoginHelper.getToRoomAndStartAV(imLoginSuccessCallback, value);
            } else {
                if (!TextUtils.isEmpty(useSign)) {
                    doImLogin(userid, useSign, imLoginSuccessCallback, MySelfInfo.getInstance()
                            .isCreateRoom());
                } else {
                    goToLoginActivity();
                }
            }
        }
    }

    public void enterRoom() {
        mLiveHelper.setCameraPreviewChangeCallback();
        backGroundId = CurLiveInfo.getHostID();
        //进入房间流程
        mEnterRoomHelper.startEnterRoom();
        //初始化view
        initView();
    }

    /**
     * 登录
     *
     * @param userid
     * @param useSign
     * @param callback
     * @param host
     */
    public void doImLogin(String userid, String useSign, final MgCallback callback, boolean host) {
        mTLoginHelper.imLogin(userid, useSign, callback, host);
    }

    /**
     * 跳转到登录界面
     */
    public void goToLoginActivity() {
        Intent intent = new Intent(LiveActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化AVSDK
     */
    private void startAVSDK() {
        String userid = MySelfInfo.getInstance().getId();
        String userSign = MySelfInfo.getInstance().getUserSig();
        int appId = Constants.SDK_APPID;

        int ccType = Constants.ACCOUNT_TYPE;
        QavsdkControl.getInstance().setAvConfig(appId, ccType + "", userid, userSign);
        QavsdkControl.getInstance().startContext();
        Log.e("liveActivity", "初始化AVSDK");
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDAT_WALL_TIME_TIMER_TASK:
                    String formatTime = LiveUtil.updateWallTime(mSecond);
                    //        if (Constants.HOST == MySelfInfo.getInstance().getIdStatus() &&
                    // null != mVideoTime) {
//            SxbLog.e(TAG, " refresh time ");
//            mVideoTime.setText(formatTime);
//        }
                    break;
                case REFRESH_LISTVIEW:
                    doRefreshListView();
                    break;
                case TIMEOUT_INVITE:
                    String id = "" + msg.obj;
                    cancelInviteView(id);
                    mLiveHelper.sendGroupMessage(Constants.AVIMCMD_MULTI_HOST_CANCELINVITE, id);
                    break;
                case REFRESH_RED_TIME:
                    Long timeLong = (Long) msg.obj;
                    String timeStr = SDDateUtil.milToStringlong(timeLong);
                    mHostRedPacketCountDownView.setTime(timeStr);
                    break;

            }
            return false;
        }
    });

    private NetWorkStateReceiver mNetWorkReceiver = new NetWorkStateReceiver() {
        @Override
        protected void onDisconnected() {
            MGToast.showToast("网络已经断开!", Toast.LENGTH_LONG);
            Log.e("test", "断开连接");
        }

        @Override
        protected void onConnected() {
            Log.e("test", "网络连接ing");
        }
    };


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //AvSurfaceView 初始化成功
            if (action.equals(Constants.ACTION_SURFACE_CREATED)) {
                //打开摄像头
                if (LiveUtil.checkIsHost()) {
                    mLiveHelper.openCameraAndMic();
                }
            }

            if (action.equals(Constants.ACTION_CAMERA_OPEN_IN_LIVE)) {//有人打开摄像头
                ArrayList<String> ids = intent.getStringArrayListExtra("ids");
                //如果是自己本地直接渲染
                for (String id : ids) {
                    if (id.equals(backGroundId)) {
                        mHostLeaveLayout.setVisibility(View.GONE);
                    }
                    if (!mRenderUserList.contains(id)) {
                        mRenderUserList.add(id);
                    }

                    if (id.equals(MySelfInfo.getInstance().getId())) {
                        showVideoView(true, id);
                        return;
//                        ids.remove(id);
                    }
                }
                //其他人一并获取
                int requestCount = CurLiveInfo.getCurrentRequestCount();
                mLiveHelper.requestViewList(ids);
                requestCount = requestCount + ids.size();
                CurLiveInfo.setCurrentRequestCount(requestCount);
//                }
            }

            if (action.equals(Constants.ACTION_CAMERA_CLOSE_IN_LIVE)) {//有人关闭摄像头
                ArrayList<String> ids = intent.getStringArrayListExtra("ids");
                //如果是自己本地直接渲染
                for (String id : ids) {
                    mRenderUserList.remove(id);
                    if (id.equals(backGroundId)) {
                        mHostLeaveLayout.setVisibility(View.VISIBLE);
                        return;
                    }
                }
            }

            if (action.equals(Constants.ACTION_SWITCH_VIDEO)) {//点击成员回调
                backGroundId = intent.getStringExtra(Constants.EXTRA_IDENTIFIER);
                SxbLog.v(TAG, "switch video enter with id:" + backGroundId);

                if (mRenderUserList.contains(backGroundId)) {
                    mHostLeaveLayout.setVisibility(View.GONE);
                } else {
                    mHostLeaveLayout.setVisibility(View.VISIBLE);
                }

                if (LiveUtil.checkIsHost()) {//自己是主播
                    if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//背景是自己
                        mHostBottomToolView1.setVisibility(View.VISIBLE);
                        mHostBottomMeiView2.setVisibility(View.VISIBLE);
                        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
                    } else {//背景是其他成员
                        mHostBottomToolView1.setVisibility(View.INVISIBLE);
                        mHostBottomMeiView2.setVisibility(View.INVISIBLE);
                        mVideoMemberCtrlView.setVisibility(View.VISIBLE);
                    }
                } else {//自己成员方式
                    if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//背景是自己
                        mVideoMemberCtrlView.setVisibility(View.VISIBLE);
                        mUserBottomTool.setVisibility(View.INVISIBLE);
                    } else if (backGroundId.equals(CurLiveInfo.getHostID())) {//主播自己
                        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
                        mUserBottomTool.setVisibility(View.VISIBLE);
                    } else {
                        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
                        mUserBottomTool.setVisibility(View.INVISIBLE);
                    }

                }

            }
            if (action.equals(Constants.ACTION_HOST_LEAVE)) {//主播结束
                quiteLivePassively();
                Log.e("test", "主播退出" + "广播色的速度");
            }
        }
    };


    private void unregisterReceiver() {
        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(mNetWorkReceiver);
    }

    /**
     * 初始化UI
     */
    private View avView;
    private TextView BtnCtrlVideo, BtnCtrlMic, BtnHungup;
    private TextView inviteView1, inviteView2, inviteView3;
    private ListView mListViewMsgItems;
    private LinearLayout mVideoMemberCtrlView;
    private FrameLayout mFullControllerUi;
    /**
     * 主播底部栏目
     */
    private HostBottomToolView mHostBottomToolView1;
    /**
     * 用户底部栏目
     */
    private UserBottomToolView mUserBottomTool;

    /**
     * GetAudienceTask
     * 初始化界面
     */
    private void initView() {
//        root = findViewById(R.id.root);
//        mHostBottomToolView1 = (HostBottomToolView) findViewById(R.id.host_bottom_layout);
// 主播的工具栏1
//        mHostBottomToolView1.setmLiveView(this);
//        mHostBottomMeiView2 = ((HostMeiToolView) findViewById(R.id.host_mei_layout));//主播的美颜工具2
//        mHostBottomToolView1.setNeed(mCommonHelper, mLiveHelper, this);
//        mHostBottomMeiView2.setNeed(this, mCommonHelper);
//
//        mUserBottomTool = (UserBottomToolView) findViewById(R.id.normal_user_bottom_tool);//用户的工具栏
//
//        mVideoMemberCtrlView = (LinearLayout) findViewById(R.id.video_member_bottom_layout);
// 直播2的工具栏
//        mHostLeaveLayout = (LinearLayout) findViewById(R.id.ll_host_leave);//主播离开(断开)界面
//        // mVideoChat = (TextView) findViewById(R.id.video_interact);//(腾讯)互动连线图标
//        mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);//飘心区域
//
//        mVideoMemberCtrlView.setVisibility(View.INVISIBLE);
        //top view


//        //video_member_bottom_layout 直播2的工具栏
//        BtnCtrlVideo = (TextView) findViewById(R.id.camera_controll);
//        BtnCtrlMic = (TextView) findViewById(R.id.mic_controll);
//        BtnHungup = (TextView) findViewById(R.id.close_member_video);
        BtnCtrlVideo.setOnClickListener(this);
        BtnCtrlMic.setOnClickListener(this);
        BtnHungup.setOnClickListener(this);
        //-----

//        TextView roomId = (TextView) findViewById(R.id.room_id);//房间room id
//        roomId.setText(CurLiveInfo.getChatRoomId());
        //顶部view

        //主播-->加载的view
        mHeadTopAdapter = new HeadTopAdapter(null, this);
        mLiveHttphelper = new LiveHttpHelper(this, this);
        if (LiveUtil.checkIsHost()) {
            initLiveAnchor();
        } else {//普通用户加载的view
            initLiveUser();
        }

        mFullControllerUi = (FrameLayout) findViewById(R.id.controll_ui);
        avView = findViewById(R.id.av_video_layer_ui);//surfaceView;

        mListViewMsgItems = (ListView) findViewById(R.id.im_msg_listview);
        mArrayListChatEntity = new ArrayList<LiveChatEntity>();
        mChatMsgListAdapter = new LiveChatMsgListAdapter(this, mListViewMsgItems,
                mArrayListChatEntity);
        mListViewMsgItems.setAdapter(mChatMsgListAdapter);


        //开启后台业务服务器请求管理类

        //----
        mLiveHttphelper.getAudienceCount(CurLiveInfo.getRoomNum() + "", "1");
        //主播清屏操作
        mHostBottomToolView1.setLiveSwitchScreenListener(new LiveSwitchScreenListener() {
            @Override
            public void onSwitchScreen() {
                bCleanMode = !bCleanMode;
                switchScreen();
            }
        });
        initViewNeed();
        //获取领取码
        App.getInstance().setReceiveCode("");
        mLiveHttphelper.getReceiveCode(CurLiveInfo.getRoomNum() + "", "1");
        mLiveHttphelper.getStoresRandomComment(CurLiveInfo.getShopID(), "3");
    }


    /**
     * 初始化主播相关的界面
     */
    private void initLiveAnchor() {
        //房间创建成功,向后台注册信息
        int i = new Random().nextInt();
        int roomId = MySelfInfo.getInstance().getMyRoomNum();
        MGToast.showToast(roomId + "");
        LogUtil.d("roomId: " + roomId);
        String url = "http://pic1.mofang.com.tw/2014/0516/20140516051344912.jpg";
        String title = "米果小站";
        if (App.getInstance().getmUserCurrentInfo() != null) {
            UserCurrentInfo userInfoNew = App.getInstance().getmUserCurrentInfo();
            if (userInfoNew != null) {
                UserInfoNew infoNew = userInfoNew.getUserInfoNew();
                if (infoNew != null) {
                    if (!TextUtils.isEmpty(infoNew.getIcon())) {
                        url = infoNew.getIcon();
                        title = TextUtils.isEmpty(infoNew.getNick()) == true ? infoNew
                                .getUser_name() : infoNew.getNick();
                    }
                }
            }

        }
        OKhttpHelper.getInstance().registerRoomInfo(title, url, roomId + "", roomId + "", roomId
                + "");
        //host的views
        mHostBottomToolView1.setVisibility(View.VISIBLE);

        mHostBottomMeiView2.setVisibility(View.VISIBLE);
        mUserBottomTool.setVisibility(View.GONE);
        //host的topview
        mHostTopView = ((HostTopView) findViewById(R.id.host_top_layout));
        mHostTopView.setmAdapter(mHeadTopAdapter);
        mHostTopView.init(this);
        mHostTopView.setVisibility(View.VISIBLE);
        mHostTopView.setNeed(this, mCommonHelper);

        doUpdateMembersCount();

        if (CurLiveInfo.getModelShop() != null && !TextUtils.isEmpty(CurLiveInfo.getModelShop()
                .getShop_name())) {
            mHostTopView.setLocation(CurLiveInfo.getModelShop().getShop_name());
            if (!TextUtils.isEmpty(CurLiveInfo.getModelShop().getCons_count()))
                mHostTopView.setArriveNum(CurLiveInfo.getModelShop().getCons_count() + "人到过");
        }

        //红包倒计时小view
        mHostRedPacketCountDownView = ((HostRedPacketTimeView) findViewById(R.id
                .host_red_countdown));

        mHostRedPacketCountDownView.setVisibility(View.VISIBLE);
//            mRecordBall = (ImageView) findViewById(R.id.record_ball);
//            BtnBeauty = (TextView) findViewById(R.id.beauty_btn);
//            BtnWhite = (TextView) findViewById(R.id.white_btn);
//            mVideoChat.setVisibility(View.VISIBLE);
//            BtnBeauty.setOnClickListener(this);
//            BtnWhite.setOnClickListener(this);
//            mVideoChat.setOnClickListener(this);

            /*邀请直播*/
        inviteView1 = (TextView) findViewById(R.id.invite_view1);
        inviteView2 = (TextView) findViewById(R.id.invite_view2);
        inviteView3 = (TextView) findViewById(R.id.invite_view3);
        inviteView1.setOnClickListener(this);
        inviteView2.setOnClickListener(this);
        inviteView3.setOnClickListener(this);


//            pushBtn = (TextView) findViewById(R.id.push_btn);
//            pushBtn.setVisibility(View.VISIBLE);
//            pushBtn.setOnClickListener(this);
//
//            recordBtn = (TextView) findViewById(R.id.record_btn);
//            recordBtn.setVisibility(View.VISIBLE);
//            recordBtn.setOnClickListener(this);

//        showBackDialog();//退出的第一个界面,问你是否退出
//        initPushDialog();
//            initRecordDialog();
        //录制功能
        mRecordHelper = new LiveRecordDialogHelper(this, mLiveHelper);
        mRecordHelper.setOnLiveRecordListener(new LiveRecordListener() {
            @Override
            public void startRecord() {
                isRecording = true;
                mOrientationHelper.startOrientationListener();
            }

            @Override
            public void stopRecord() {
                mOrientationHelper.stopOrientationListener();
            }
        });
        mRecordHelper.show();

//            mMemberDg = new MembersDialog(this, R.style.floag_dialog, this);
//            mBeautySettings = (LinearLayout) findViewById(R.id.qav_beauty_setting);
//            mBeautyConfirm = (TextView) findViewById(R.id.qav_beauty_setting_finish);
//            mBeautyConfirm.setOnClickListener(this);
//            mBeautyBar = (SeekBar) (findViewById(R.id.qav_beauty_progress));
//            mBeautyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                    SxbLog.e("SeekBar", "onStopTrackingTouch");
//                    if (mProfile == mBeatuy) {
//                        Toast.makeText(LiveActivity.this, "beauty " + mBeautyRate + "%", Toast
// .LENGTH_SHORT).show();//美颜度
//                    } else {
//                        Toast.makeText(LiveActivity.this, "white " + mWhiteRate + "%", Toast
// .LENGTH_SHORT).show();//美白度
//                    }
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//                    SxbLog.e("SeekBar", "onStartTrackingTouch");
//                }
//
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress,
//                                              boolean fromUser) {
//                    Log.i(TAG, "onProgressChanged " + progress);
//                    if (mProfile == mBeatuy) {
//                        mBeautyRate = progress;
//                        QavsdkControl.getInstance().getAVContext().getVideoCtrl()
// .inputBeautyParam(LiveUtil.getBeautyProgress(progress));//美颜
//                    } else {
//                        mWhiteRate = progress;
//                        QavsdkControl.getInstance().getAVContext().getVideoCtrl()
// .inputWhiteningParam(LiveUtil.getBeautyProgress(progress));//美白
//                    }
//                }
//            });
    }

    /**
     * 初始化观看直播用户的界面
     */
    private void initLiveUser() {
        initInviteDialog();

        mBaoBaoAdapter = new PagerBaoBaoAdapter(this);
        mUserBottomTool.setVisibility(View.VISIBLE);
        mUserBottomTool.setmBaobaoAdapter(mBaoBaoAdapter);

        mRedPacketAdapter = new PagerRedPacketAdapter();
        mUserBottomTool.setmRedPacketAdapter(mRedPacketAdapter);

        mUserHeadTopView = (UserHeadTopView) findViewById(R.id.user_top_layout);//观众的topview
        mUserHeadTopView.setmLiveView(this);
        mUserHeadTopView.setmAdapter(mHeadTopAdapter);
        mUserHeadTopView.init();

        mUserHeadTopView.setVisibility(View.VISIBLE);
        mUserHeadTopView.initNeed(this);

        mHostBottomToolView1.setVisibility(View.GONE);
        mHostBottomMeiView2.setVisibility(View.GONE);
        String hostImg = CurLiveInfo.getHostAvator();
        mUserHeadTopView.setHostImg(hostImg);
        mUserHeadTopView.setHostName(CurLiveInfo.getHostName());
        doUpdateMembersCount();


        if (CurLiveInfo.getModelShop() != null && !TextUtils.isEmpty(CurLiveInfo.getModelShop()
                .getShop_name())) {
            mUserHeadTopView.setLocation(CurLiveInfo.getModelShop().getShop_name());
        }
        if (mLiveHttphelper != null) {
            mLiveHttphelper.enterRoom(CurLiveInfo.getRoomNum() + "", "1", App.getInstance().code);
//            mLiveHttphelper.checkFocus(CurLiveInfo.getHostID());

        }
        mUserHeadTopView.setViews();
    }

    /**
     * 观众 底部操作view需要的参数
     */
    private void initViewNeed() {
        //初始化底部
        if (mUserBottomTool != null) {
            mUserBottomTool.initView(this, mLiveHelper, mHeartLayout, root, this);
            if (!LiveUtil.checkIsHost()) {
                mUserBottomTool.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mUserBottomTool != null) {
                            //TODO 在低配置的会来不及初始化,先这样,等有了更好的解决方案再更改
                            mUserBottomTool.clickBaoBao();
                        }
                    }
                }, 2000);
            }
        }
        if (!TextUtils.isEmpty(CurLiveInfo.shopID) && !LiveUtil.checkIsHost()) {
            getShopDetail(CurLiveInfo.shopID);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MGTimer.showTime();
        mLiveHelper.resume();
        QavsdkControl.getInstance().onResume();
    }

    private boolean showBaoBao = false;

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && !showBaoBao && !LiveUtil.checkIsHost()) {
//            //弹出宝宝
//            mUserBottomTool.clickBaoBao();
//            showBaoBao = true;
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        mLiveHelper.pause();
        QavsdkControl.getInstance().onPause();
    }

    /**
     * 发起请求商店详情和商品列表的请求。
     *
     * @param shopId 门店ID
     */
    @Override
    public void getShopDetail(String shopId) {
        mSellerHttpHelper.getSellerDetail(shopId);
    }


    /**
     * 直播心跳
     */
    private class HeartBeatTask extends TimerTask {
        @Override
        public void run() {
            String host = CurLiveInfo.getHostID();
            SxbLog.e(TAG, "HeartBeatTask " + host);
            mLiveHelper.sendHeartBeat();

        }
    }

    /**
     * 取观众 列表
     */
    private class GetAudienceTask extends TimerTask {
        @Override
        public void run() {
            mLiveHttphelper.getAudienceList(CurLiveInfo.getRoomNum() + "");
        }
    }

    /**
     * 记时器
     */
    private class VideoTimerTask extends TimerTask {
        public void run() {

            ++mSecond;
            if (LiveUtil.checkIsHost())
                mHandler.sendEmptyMessage(UPDAT_WALL_TIME_TIMER_TASK);
        }
    }

    @Override
    protected void onDestroy() {
        SDEventManager.unregister(this);
        try {
            /**
             * 一定要退出聊天室
             */
//            userExit();
            /**
             * 如果不设置成false则用户再进来会变成主播身份
             */
            App.getInstance().setAvStart(false);
            MGLog.e("onDestroy");
            watchCount = 0;
            super.onDestroy();
            if (null != mHearBeatTimer) {
                mHearBeatTimer.cancel();
                mHearBeatTimer = null;
            }
            if (null != mVideoTimer) {
                mVideoTimer.cancel();
                mVideoTimer = null;
            }
            if (null != mAudienceTimer) {
                mAudienceTimer.cancel();
                mAudienceTimer = null;
            }
            if (null != mVideoTimerTask) {
                mVideoTimerTask.cancel();
                mVideoTimerTask = null;
            }
            inviteViewCount = 0;
            thumbUp = 0;
            CurLiveInfo.setMembers(0);
            CurLiveInfo.setAdmires(0);
            CurLiveInfo.setCurrentRequestCount(0);
            unregisterReceiver();
            if (mLiveHelper != null) {
                if (isRecording) {
                    mLiveHelper.stopRecord();
                }
                mLiveHelper.closeCameraAndMic();
                mLiveHelper.onDestory();
            }
            if (mEnterRoomHelper != null) {
                mEnterRoomHelper.onDestory();
            }
            //mLiveHelper;
            if (mTLoginHelper != null) {
                mTLoginHelper.onDestory();
            }
            if (tencentHttpHelper != null) {
                tencentHttpHelper.onDestroy();
            }
            if (mUserHeadTopView != null) {
                mUserHeadTopView.ondestroy();
                mUserHeadTopView = null;
            }
            if (mHostTopView != null) {
                mHostTopView = null;
            }
            QavsdkControl.getInstance().clearVideoMembers();
            QavsdkControl.getInstance().onDestroy();
            MySelfInfo.getInstance().setMyRoomNum(-1);
        } catch (Exception e) {

        }


    }


    /**
     * 点击Back键
     */
    @Override
    public void onBackPressed() {
        if (LiveUtil.checkIsHost()) {
            showBackDialog();
        } else {
            userExit();
        }
    }

    /**
     * 普通用户退出
     */
    public void userExit() {
        userClickOut = true;
        mLiveHelper.perpareQuitRoom(true);
        App.getInstance().setAvStart(false);
        if (mLiveHttphelper != null) {
            mLiveHttphelper.exitRoom(CurLiveInfo.getRoomNum() + "", "1");
        }
        finish();
    }

    /**
     * 退出直播对话框
     */
    public void showBackDialog() {
        final LiveBackDialog dialog = new LiveBackDialog(this);
        dialog.setOnLiveBackClickListener(new LiveBackDialogCategory.OnLiveBackClickListener() {
            @Override
            public void clickSure() {
                //如果是直播，发消息
                if (null != mLiveHelper) {
                    //向后台发送主播退出
                    mLiveHelper.perpareQuitRoom(true);
                    App.getInstance().setAvStart(false);
                    if (isPushed) {
                        mLiveHelper.stopPushAction();
                    }
                    startActivity(new Intent(LiveActivity.this, LiveEndActivity.class));
                    finish();
                    dialog.dismiss();
                }
            }

            @Override
            public void clickCancel() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 被动退出直播
     */
    private void quiteLivePassively() {
        Toast.makeText(this, "主播退出了房间", Toast.LENGTH_SHORT);
        Log.e("tet", "主播退出了房间----广播");
        mLiveHelper.perpareQuitRoom(false);
        mEnterRoomHelper.quiteLive();
    }

    @Override
    public void readyToQuit() {
        mEnterRoomHelper.quiteLive();
    }


    /**
     * 进入房间成功
     *
     * @param id_status
     * @param isSucc
     */
    @Override
    public void enterRoomComplete(int id_status, boolean isSucc) {
        Log.e("LiveActivity", "");
        //必须得进入房间之后才能初始化UI
        mEnterRoomHelper.initAvUILayer(avView);

        //设置预览回调，修正摄像头镜像
        mLiveHelper.setCameraPreviewChangeCallback();
        if (isSucc == true) {
            //IM初始化
            mLiveHelper.initTIMListener("" + CurLiveInfo.getRoomNum());

            if (id_status == Constants.HOST) {//主播方式加入房间成功
                //开启摄像头渲染画面
                MGLog.e("主播:createlive enterRoomComplete");
            } else {
                //发消息通知上线
                mLiveHelper.sendGroupMessage(Constants.AVIMCMD_EnterLive, "");
                MGLog.e("观众:enterRoomComplete");
            }
        }
    }


    boolean userClickOut = false;

    private boolean showExit = false;

    /**
     * 完全退出房间了
     * 這裡需要判斷是否為主播退出導致的退出事件
     *
     * @param id_status
     * @param succ
     * @param liveinfo
     */
    @Override
    public void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo) {
        Log.e("LiveActivity", "");
        Log.e(TAG, "quiteRoomComplete 退出房间...");
        if (LiveUtil.checkIsHost()) {
//            MGToast.showToast("主播退出!");
//            if (backDialog != null) {
//                backDialog.dismiss();
//            }
            finish();
        } else {
            /**
             * 如果不是用戶點擊退出導致的調用，則需要彈出主播已退出的窗口
             */

            Log.e(LiveActivity.TAG, "quite: " + id_status + "userClickOut: " + userClickOut);

            if (mUserHeadTopView != null && !mUserHeadTopView.isExitDialogShowing() &&
                    !mUserHeadTopView.isUserClose) {
                mUserHeadTopView.showExitDialog();
            } else {
                finish();
            }
        }
        App.getInstance().setAvStart(false);
    }

    /**
     * 成员状态变更
     *
     * @param id
     * @param name
     * @param faceUrl
     */
    @Override
    public void memberJoin(String id, String name, String faceUrl) {
        watchCount++;
        refreshTextListView(faceUrl, TextUtils.isEmpty(name) ? id : name, "进入房间", Constants
                .MEMBER_ENTER);
        int members = CurLiveInfo.getMembers() + 1;
        CurLiveInfo.setMembers(members);

        //人数加1,可以设置到界面上
        doUpdateMembersCount();
    }

    @Override
    public void memberQuit(String id, String name, String faceUrl) {
        String text = TextUtils.isEmpty(name) ? id : name;
//        refreshTextListView(faceUrl, text, "退出房间了", Constants
//                .MEMBER_EXIT);
        watchCount--;

        if (CurLiveInfo.getMembers() > 1) {
            int members = CurLiveInfo.getMembers() - 1;
            CurLiveInfo.setMembers(members);
            doUpdateMembersCount();
        }

        //如果存在视频互动，取消
        //  QavsdkControl.getInstance().closeMemberView(id);
    }

    @Override
    public void hostLeave(String id, String name, String faceUrl) {
        refreshTextListView(faceUrl, TextUtils.isEmpty(name) ? id : name, "离开一会", Constants
                .HOST_LEAVE);
    }

    @Override
    public void hostBack(String id, String name, String faceUrl) {
        refreshTextListView(faceUrl, TextUtils.isEmpty(name) ? id : name, "回来了", Constants
                .HOST_BACK);
    }

    @Override
    public void getHostRedPacket(HashMap<String, String> params) {
        if (!LiveUtil.checkIsHost()) {
            if (mUserBottomTool != null) {
                String red_packet_key = params.get("red_packet_id");
                String durationStr = params.get("red_packet_duration");
                int duration = 30;
                if (!TextUtils.isEmpty(durationStr)) {
                    duration = Integer.valueOf(duration);
                }
                mUserBottomTool.clickRob(red_packet_key, duration);
            }
        }
    }

    CountDownTimer robLiftTimer = new CountDownTimer(20 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mHostRedPacketCountDownView.setTime(SDDateUtil.secondesToMMSS(millisUntilFinished));
        }

        @Override
        public void onFinish() {
            mHostRedPacketCountDownView.setTime("00:00");
            mHostBottomToolView1.setClickable(true);
        }
    };


    private void createNewTimer(final Integer duration) {

        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                robLiftTimer = new CountDownTimer(duration, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mHostRedPacketCountDownView.setTime(SDDateUtil.secondesToMMSS(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        mHostRedPacketCountDownView.setTime("00:00");
                        mHostBottomToolView1.setClickable(true);
                    }
                };
            }
        });

    }

    @Override
    public void sendHostRedPacket(String id, String duration) {
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(duration)) {
            //启动红包倒计时。
            Integer values = Integer.valueOf(duration);
//            final String timeStr = SDDateUtil.milToStringlong(new Long(values));
            createNewTimer(values);

            robLiftTimer.start();
            mHostBottomToolView1.setClickable(false);
        }
    }


    @Override
    public void tokenInvalidateAndQuit() {
        onBackPressed();
    }


    /**
     * 有成员退群
     *
     * @param list 成员ID 列表
     */
    @Override
    public void memberQuiteLive(String[] list) {

        if (list == null) return;
        for (String id : list) {
            SxbLog.e(TAG, "memberQuiteLive id " + id);
            if (CurLiveInfo.getHostID().equals(id)) {
                if (MySelfInfo.getInstance().getIdStatus() == Constants.MEMBER)
                    quiteLivePassively();
            }
        }
        int roomId = CurLiveInfo.getRoomNum();
        if (roomId != -1 && roomId != 0) {
            mLiveHttphelper.getAudienceCount(CurLiveInfo.getRoomNum() + "", "1");
        }
    }

    /**
     * 主播退出 。
     *
     * @param type
     * @param responseBody
     */
    @Override
    public void hostQuiteLive(String type, String responseBody) {
        Log.e("test", "主播退出" + "type" + type + "responseBody" + responseBody);
    }


    /**
     * 有成员入群
     *
     * @param list 成员ID 列表
     */
    @Override
    public void memberJoinLive(final String[] list) {
    }

    @Override
    public void alreadyInLive(String[] list) {
        for (String id : list) {
            if (id.equals(MySelfInfo.getInstance().getId())) {
                QavsdkControl.getInstance().setSelfId(MySelfInfo.getInstance().getId());
                QavsdkControl.getInstance().setLocalHasVideo(true, MySelfInfo.getInstance().getId
                        ());
            } else {
                QavsdkControl.getInstance().setRemoteHasVideo(true, id, AVView
                        .VIDEO_SRC_TYPE_CAMERA);
            }
        }

    }

    @Override
    public void exitActivity() {
        userExit();
        //如果是主播
        if (LiveUtil.checkIsHost()) {
            mLiveHttphelper.stopLive(MySelfInfo.getInstance().getMyRoomNum() + "");
        }
    }

    /**
     * update 观众 数量 。
     */
    public void doUpdateMembersCount() {

        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (LiveUtil.checkIsHost() && mHostTopView != null) {
                    mHostTopView.updateAudienceCount(CurLiveInfo.getMembers() + "");
                } else {
                    if (mUserHeadTopView != null) {
                        mUserHeadTopView.updateAudienceCount(CurLiveInfo.getMembers() + "");
                    }
                }
            }
        });
    }


    /**
     * 加载视频数据
     *
     * @param isLocal 是否是本地数据
     * @param id      身份
     */
    @Override
    public void showVideoView(boolean isLocal, String id) {

        //渲染本地Camera
        if (isLocal == true) {
            SxbLog.e(TAG, "showVideoView host :" + MySelfInfo.getInstance().getId());
            QavsdkControl.getInstance().setSelfId(MySelfInfo.getInstance().getId());
            QavsdkControl.getInstance().setLocalHasVideo(true, MySelfInfo.getInstance().getId());
            //主播通知用户服务器
            if (LiveUtil.checkIsHost()) {
                if (bFirstRender) {
                    mEnterRoomHelper.notifyServerCreateRoom();
                    //主播心跳
                    mHearBeatTimer = new Timer(true);
                    mHeartBeatTask = new HeartBeatTask();
                    mHearBeatTimer.schedule(mHeartBeatTask, 1000, 30 * 1000);

                    //直播时间
                    mVideoTimer = new Timer(true);
                    mVideoTimerTask = new VideoTimerTask();
                    mVideoTimer.schedule(mVideoTimerTask, 1000, 1000);
                    bFirstRender = false;
                }
            }

        } else {

//            QavsdkControl.getInstance().addRemoteVideoMembers(id);
            QavsdkControl.getInstance().setRemoteHasVideo(true, id, AVView.VIDEO_SRC_TYPE_CAMERA);
        }
        mAudienceTimer = new Timer(true);
        mGetAudienceTask = new GetAudienceTask();
        mAudienceTimer.schedule(mGetAudienceTask, 1000, 30 * 1000);

    }

    @Override
    public void showInviteDialog() {
        if ((inviteDg != null) && (getBaseContext() != null) && (inviteDg.isShowing() != true)) {
            inviteDg.show();
        }
    }

    @Override
    public void hideInviteDialog() {
        if ((inviteDg != null) && (inviteDg.isShowing() == true)) {
            inviteDg.dismiss();
        }
    }


    @Override
    public void refreshText(String text, String name) {
        if (text != null) {
            refreshTextListView("", name, text, Constants.TEXT_TYPE);
        }
    }

    @Override
    public void refreshText(String text, String name, String faceUrl) {
        if (text != null) {
            refreshTextListView(faceUrl, name, text, Constants.TEXT_TYPE);
        }
    }

    @Override
    public void refreshThumbUp() {
        CurLiveInfo.setAdmires(CurLiveInfo.getAdmires() + 1);
        if (!bCleanMode) {      // 纯净模式下不播放飘星动画
            mHeartLayout.addFavor();
        }
//        tvAdmires.setText("" + CurLiveInfo.getAdmires());
    }

    @Override
    public void refreshUI(String id) {
        //当主播选中这个人，而他主动退出时需要恢复到正常状态
        if (LiveUtil.checkIsHost())
            if (!backGroundId.equals(CurLiveInfo.getHostID()) && backGroundId.equals(id)) {
                backToNormalCtrlView();
            }
    }


    private int inviteViewCount = 0;

    @Override
    public boolean showInviteView(String id) {
        int index = QavsdkControl.getInstance().getAvailableViewIndex(1);
        if (index == -1) {
            Toast.makeText(LiveActivity.this, "the invitation's upper limit is 3", Toast
                    .LENGTH_SHORT).show();
            return false;
        }
        int requetCount = index + inviteViewCount;
        if (requetCount > 3) {
            Toast.makeText(LiveActivity.this, "the invitation's upper limit is 3", Toast
                    .LENGTH_SHORT).show();
            return false;
        }

        if (hasInvited(id)) {
            Toast.makeText(LiveActivity.this, "it has already invited", Toast.LENGTH_SHORT).show();
            return false;
        }
        switch (requetCount) {
            case 1:
                inviteView1.setText(id);
                inviteView1.setVisibility(View.VISIBLE);
                inviteView1.setTag(id);

                break;
            case 2:
                inviteView2.setText(id);
                inviteView2.setVisibility(View.VISIBLE);
                inviteView2.setTag(id);
                break;
            case 3:
                inviteView3.setText(id);
                inviteView3.setVisibility(View.VISIBLE);
                inviteView3.setTag(id);
                break;
        }
        mLiveHelper.sendC2CMessage(Constants.AVIMCMD_MUlTI_HOST_INVITE, "", id);
        inviteViewCount++;
        //30s超时取消
        Message msg = new Message();
        msg.what = TIMEOUT_INVITE;
        msg.obj = id;
        mHandler.sendMessageDelayed(msg, 30 * 1000);
        return true;
    }


    /**
     * 判断是否邀请过同一个人
     *
     * @param id
     * @return
     */
    private boolean hasInvited(String id) {
        if (id.equals(inviteView1.getTag())) {
            return true;
        }
        if (id.equals(inviteView2.getTag())) {
            return true;
        }
        if (id.equals(inviteView3.getTag())) {
            return true;
        }
        return false;
    }

    @Override
    public void cancelInviteView(String id) {
        if ((inviteView1 != null) && (inviteView1.getTag() != null)) {
            if (inviteView1.getTag().equals(id)) {
            }
            if (inviteView1.getVisibility() == View.VISIBLE) {
                inviteView1.setVisibility(View.INVISIBLE);
                inviteView1.setTag("");
                inviteViewCount--;
            }
        }

        if (inviteView2 != null && inviteView2.getTag() != null) {
            if (inviteView2.getTag().equals(id)) {
                if (inviteView2.getVisibility() == View.VISIBLE) {
                    inviteView2.setVisibility(View.INVISIBLE);
                    inviteView2.setTag("");
                    inviteViewCount--;
                }
            } else {
                Log.i(TAG, "cancelInviteView inviteView2 is null");
            }
        } else {
            Log.i(TAG, "cancelInviteView inviteView2 is null");
        }

        if (inviteView3 != null && inviteView3.getTag() != null) {
            if (inviteView3.getTag().equals(id)) {
                if (inviteView3.getVisibility() == View.VISIBLE) {
                    inviteView3.setVisibility(View.INVISIBLE);
                    inviteView3.setTag("");
                    inviteViewCount--;
                }
            } else {
                Log.i(TAG, "cancelInviteView inviteView3 is null");
            }
        } else {
            Log.i(TAG, "cancelInviteView inviteView3 is null");
        }


    }

    @Override
    public void cancelMemberView(String id) {
        if (LiveUtil.checkIsHost()) {
        } else {
            //TODO 主动下麦 下麦；
            mLiveHelper.changeAuthandRole(false, Constants.NORMAL_MEMBER_AUTH, Constants
                    .NORMAL_MEMBER_ROLE);
//            mLiveHelper.closeCameraAndMic();//是自己成员关闭
        }
        mLiveHelper.sendGroupMessage(Constants.AVIMCMD_MULTI_CANCEL_INTERACT, id);
        QavsdkControl.getInstance().closeMemberView(id);
        backToNormalCtrlView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//                new PopWindow().show(this,R.layout.host_beauty_setting,-1,-2,0,false,root);
//            case R.id.fullscreen_btn:
//                //显示或者清除屏幕
//                switchScreen();
//                break;

            //host2的控制台---------------
            case R.id.camera_controll:

                Toast.makeText(LiveActivity.this, "切换" + backGroundId + "camrea 状态", Toast
                        .LENGTH_SHORT).show();
                if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//自己关闭自己
                    mLiveHelper.toggleCamera();
                } else {
                    mLiveHelper.sendC2CMessage(Constants.AVIMCMD_MULTI_HOST_CONTROLL_CAMERA,
                            backGroundId, backGroundId);//主播关闭自己
                }
                break;
            case R.id.mic_controll:
                Toast.makeText(LiveActivity.this, "切换" + backGroundId + "mic 状态", Toast
                        .LENGTH_SHORT).show();
                if (backGroundId.equals(MySelfInfo.getInstance().getId())) {//自己关闭自己
                    mLiveHelper.toggleMic();
                } else {
                    mLiveHelper.sendC2CMessage(Constants.AVIMCMD_MULTI_HOST_CONTROLL_MIC,
                            backGroundId, backGroundId);//主播关闭自己
                }

                break;
            case R.id.close_member_video:
                //主动关闭成员摄像头(他本身是被邀请的,为host2)
                cancelMemberView(backGroundId);
                break;
            //host2的控制台 end-------------
//            case R.id.beauty_btn:
//                //美颜
//                Log.i(TAG, "onClick " + mBeautyRate);
//
//                mProfile = mBeatuy;
//                if (mBeautySettings != null) {
//                    if (mBeautySettings.getVisibility() == View.GONE) {
//                        mBeautySettings.setVisibility(View.VISIBLE);
//                        mFullControllerUi.setVisibility(View.INVISIBLE);
//                        mBeautyBar.setProgress(mBeautyRate);
//                    } else {
//                        mBeautySettings.setVisibility(View.GONE);
//                        mFullControllerUi.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    SxbLog.e(TAG, "beauty_btn mTopBar  is null ");
//                }
//                break;

//            case R.id.white_btn:
//                //美白
//                Log.i(TAG, "onClick " + mWhiteRate);
//                mProfile = mWhite;
//                if (mBeautySettings != null) {
//                    if (mBeautySettings.getVisibility() == View.GONE) {
//                        mBeautySettings.setVisibility(View.VISIBLE);
//                        mFullControllerUi.setVisibility(View.INVISIBLE);
//                        mBeautyBar.setProgress(mWhiteRate);
//                    } else {
//                        mBeautySettings.setVisibility(View.GONE);
//                        mFullControllerUi.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    SxbLog.e(TAG, "beauty_btn mTopBar  is null ");
//                }
//                break;
            case R.id.qav_beauty_setting_finish:
//                mBeautySettings.setVisibility(View.GONE);
//                mFullControllerUi.setVisibility(View.VISIBLE);
//                new PopWindow().show(this,R.layout.qav_beauty_setting,-1,-2,0,false,root);
                break;

            //邀请直播 start
            case R.id.invite_view1:
                inviteView1.setVisibility(View.INVISIBLE);
                mLiveHelper.sendGroupMessage(Constants.AVIMCMD_MULTI_CANCEL_INTERACT, "" +
                        inviteView1.getTag());
                break;
            case R.id.invite_view2:
                inviteView2.setVisibility(View.INVISIBLE);
                mLiveHelper.sendGroupMessage(Constants.AVIMCMD_MULTI_CANCEL_INTERACT, "" +
                        inviteView2.getTag());
                break;
            case R.id.invite_view3:
                inviteView3.setVisibility(View.INVISIBLE);
                mLiveHelper.sendGroupMessage(Constants.AVIMCMD_MULTI_CANCEL_INTERACT, "" +
                        inviteView3.getTag());
                break;


        }
    }

    /**
     * 清除屏幕(清屏)
     * 或者开启屏幕内容
     */
    private void switchScreen() {
        if (bCleanMode) {
//            mFullControllerUi.setVisibility(View.INVISIBLE);
            //主播清屏
            if (LiveUtil.checkIsHost()) {
                mHostTopView.hide();
                mListViewMsgItems.setVisibility(View.INVISIBLE);
                if (mHostBottomMeiView2.isShow()) {
                    mHostBottomMeiView2.hide();
                }
            } else {
                //用户
            }

        } else {
            if (LiveUtil.checkIsHost()) {
                mHostTopView.show();
                mListViewMsgItems.setVisibility(View.VISIBLE);
                if (!mHostBottomMeiView2.isShow()) {
                    mHostBottomMeiView2.show();
                }
            } else {
                //用户
            }
            mFullControllerUi.setVisibility(View.VISIBLE);
        }
    }

    private void backToNormalCtrlView() {
        if (LiveUtil.checkIsHost()) {
            backGroundId = CurLiveInfo.getHostID();
            mHostBottomToolView1.setVisibility(View.VISIBLE);
            mHostBottomMeiView2.setVisibility(View.VISIBLE);
            mVideoMemberCtrlView.setVisibility(View.GONE);
        } else {
            backGroundId = CurLiveInfo.getHostID();
            mUserBottomTool.setVisibility(View.VISIBLE);
            mVideoMemberCtrlView.setVisibility(View.GONE);
        }
    }


    /**
     * 主播邀请应答框
     */
    private void initInviteDialog() {
        inviteDg = new Dialog(this, R.style.dialog);
        inviteDg.setContentView(R.layout.invite_dialog);
        TextView hostId = (TextView) inviteDg.findViewById(R.id.host_id);
        hostId.setText(CurLiveInfo.getHostID());
        TextView agreeBtn = (TextView) inviteDg.findViewById(R.id.invite_agree);
        TextView refusebtn = (TextView) inviteDg.findViewById(R.id.invite_refuse);
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mVideoMemberCtrlView.setVisibility(View.VISIBLE);
//                mNomalUserBottomTools.setVisibility(View.INVISIBLE);

                //上麦 ；TODO 上麦 上麦 上麦 ！！！！！；
                mLiveHelper.changeAuthandRole(true, Constants.VIDEO_MEMBER_AUTH, Constants
                        .VIDEO_MEMBER_ROLE);
                inviteDg.dismiss();
            }
        });

        refusebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLiveHelper.sendC2CMessage(Constants.AVIMCMD_MUlTI_REFUSE, "", CurLiveInfo
                        .getHostID());
                inviteDg.dismiss();
            }
        });

        Window dialogWindow = inviteDg.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }


    /**
     * 消息刷新显示
     *
     * @param name    发送者
     * @param context 内容
     * @param type    类型 （上线线消息和 聊天消息）
     */
    public void refreshTextListView(String faceUrl, String name, String context, int type) {
        LiveChatEntity entity = new LiveChatEntity();
        entity.setFaceUrl(faceUrl);
        entity.setSenderName(name);
        entity.setContent(context);
        entity.setType(type);
        notifyRefreshListView(entity);


        mListViewMsgItems.setVisibility(View.VISIBLE);
        SxbLog.e(TAG, "refreshTextListView height " + mListViewMsgItems.getHeight());

        if (mListViewMsgItems.getCount() > 1) {
            if (true)
                mListViewMsgItems.setSelection(0);
            else
                mListViewMsgItems.setSelection(mListViewMsgItems.getCount() - 1);
        }
    }


    /**
     * 通知刷新消息ListView
     */
    private void notifyRefreshListView(LiveChatEntity entity) {
        mBoolNeedRefresh = true;
        mTmpChatList.add(entity);
        if (mBoolRefreshLock) {
            return;
        } else {
            doRefreshListView();
        }
    }


    /**
     * 刷新ListView并重置状态
     */
    private void doRefreshListView() {
        if (mBoolNeedRefresh) {
            mBoolRefreshLock = true;
            mBoolNeedRefresh = false;
            mArrayListChatEntity.addAll(mTmpChatList);
            mTmpChatList.clear();
            mChatMsgListAdapter.notifyDataSetChanged();

            if (null != mTimerTask) {
                mTimerTask.cancel();
            }
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    SxbLog.v(TAG, "doRefreshListView->task enter with need:" + mBoolNeedRefresh);
                    mHandler.sendEmptyMessage(REFRESH_LISTVIEW);
                }
            };
            //mTimer.cancel();
            mTimer.schedule(mTimerTask, MINFRESHINTERVAL);
        } else {
            mBoolRefreshLock = false;
        }
    }

    @Override
    public void updateProfileInfo(TIMUserProfile profile) {

    }

    @Override
    public void updateUserInfo(int requestCode, List<TIMUserProfile> profiles) {
        if (null != profiles) {
            switch (requestCode) {
                case GETPROFILE_JOIN:
                    for (TIMUserProfile user : profiles) {
                        doUpdateMembersCount();

                        if (!TextUtils.isEmpty(user.getNickName())) {
                            refreshTextListView(user.getFaceUrl(), user.getNickName(), "加入直播",
                                    Constants.MEMBER_ENTER);
                        } else {
                            refreshTextListView(user.getFaceUrl(), user.getIdentifier(), "加入直播",
                                    Constants.MEMBER_ENTER);
                        }
                    }
                    break;
            }

        }
    }

    //旁路直播
    private static boolean isPushed = false;

    /**
     * 旁路直播 退出房间时必须退出推流。否则会占用后台channel。
     */
    public void pushStream() {
        if (!isPushed) {
            if (mPushDialog != null)
                mPushDialog.show();
        } else {
            mLiveHelper.stopPushAction();
        }
    }

    private Dialog mPushDialog;

    private void initPushDialog() {
        mPushDialog = new Dialog(this, R.style.dialog);
        mPushDialog.setContentView(R.layout.push_dialog_layout);
        final TIMAvManager.StreamParam mStreamParam = TIMAvManager.getInstance().new StreamParam();
        final EditText pushfileNameInput = (EditText) mPushDialog.findViewById(R.id.push_filename);
        final RadioGroup radgroup = (RadioGroup) mPushDialog.findViewById(R.id.push_type);


        Button recordOk = (Button) mPushDialog.findViewById(R.id.btn_record_ok);
        recordOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pushfileNameInput.getText().toString().equals("")) {
                    Toast.makeText(LiveActivity.this, "name can't be empty", Toast.LENGTH_SHORT);
                    return;
                } else {
                    mStreamParam.setChannelName(pushfileNameInput.getText().toString());
                }

                if (radgroup.getCheckedRadioButtonId() == R.id.hls) {
                    mStreamParam.setEncode(TIMAvManager.StreamEncode.HLS);
                } else {
                    mStreamParam.setEncode(TIMAvManager.StreamEncode.RTMP);
                }
//                mStreamParam.setEncode(TIMAvManager.StreamEncode.HLS);
                mLiveHelper.pushAction(mStreamParam);
                mPushDialog.dismiss();
            }
        });


        Button recordCancel = (Button) mPushDialog.findViewById(R.id.btn_record_cancel);
        recordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPushDialog.dismiss();
            }
        });

        Window dialogWindow = mPushDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        mPushDialog.setCanceledOnTouchOutside(false);
    }


    /**
     * 推流成功
     *
     * @param streamRes
     */
    @Override
    public void pushStreamSucc(TIMAvManager.StreamRes streamRes) {
        List<TIMAvManager.LiveUrl> liveUrls = streamRes.getUrls();
        isPushed = true;
        int length = liveUrls.size();
        String url = null;
        String url2 = null;
        if (length == 1) {
            TIMAvManager.LiveUrl avUrl = liveUrls.get(0);
            url = avUrl.getUrl();
        } else if (length == 2) {
            TIMAvManager.LiveUrl avUrl = liveUrls.get(0);
            url = avUrl.getUrl();
            TIMAvManager.LiveUrl avUrl2 = liveUrls.get(1);
            url2 = avUrl2.getUrl();
        }
        ClipToBoard(url, url2);
    }

    /**
     * 将地址黏贴到黏贴版
     *
     * @param url
     * @param url2
     */
    private void ClipToBoard(final String url, final String url2) {
        if (url == null) return;
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.clip_dialog);
        TextView urlText = ((TextView) dialog.findViewById(R.id.url1));
        TextView urlText2 = ((TextView) dialog.findViewById(R.id.url2));
        Button btnClose = ((Button) dialog.findViewById(R.id.close_dialog));
        urlText.setText(url);
        urlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clip = (ClipboardManager) getApplicationContext()
                        .getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                clip.setText(url);
                Toast.makeText(LiveActivity.this, getResources().getString(R.string.clip_tip),
                        Toast.LENGTH_SHORT).show();
            }
        });
        if (url2 == null) {
            urlText2.setVisibility(View.GONE);
        } else {
            urlText2.setText(url2);
            urlText2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clip = (ClipboardManager) getApplicationContext()
                            .getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                    clip.setText(url2);
                    Toast.makeText(LiveActivity.this, getResources().getString(R.string.clip_tip)
                            , Toast.LENGTH_SHORT).show();
                }
            });
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private boolean mRecord = false;

    /**
     * 停止推流成功
     */
    @Override
    public void stopStreamSucc() {
        isPushed = false;
    }

    @Override
    public void startRecordCallback(boolean isSucc) {
        mRecord = true;


    }

    @Override
    public void stopRecordCallback(boolean isSucc, List<String> files) {
        if (isSucc == true) {
            mRecord = false;

        }
    }

    //--------------- http请求 -------------
    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, final List datas) {
        switch (method) {
            case LiveConstants.RECEIVE_CODE:
                if (!SDCollectionUtil.isEmpty(datas)) {
                    String code = ((ModelReceiveCode) datas.get(0)).getReceive_code();
                    if (!TextUtils.isEmpty(code)) {
                        App.getInstance().setReceiveCode(code);
                    }
                }
                break;
            case LiveConstants.AUDIENCE_LIST:
                //观众列表
                final List<ModelAudienceInfo> audienceList = datas;


                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final boolean isHost = LiveUtil.checkIsHost();
                        final int size = datas.size();
                        if (audienceList != null && audienceList.size() >= 0) {

                            CurLiveInfo.setMembers(size);
                        }
                        if (isHost) {
                            if (mHostTopView != null) {
                                mHostTopView.refreshData(datas);
                            }
                        } else {
                            if (mUserHeadTopView != null) {
                                mUserHeadTopView.refreshData(datas);
                            }
                        }
                        doUpdateMembersCount();
                        mHeadTopAdapter.notifyDataSetChanged();
                    }
                });


                break;
            case LiveConstants.END_INFO:
                //直播结束
                break;
            case LiveConstants.ENTER_ROOM:
                //观众进入房间
                if (!checkDataIsNull(datas)) {
                    //成功
                }
                break;
            case LiveConstants.EXIT_ROOM:
                //观众退出房间
                break;
            case LiveConstants.HOST_INFO:
                //获取host资料
                if (checkDataIsNull(datas)) {
                    MGLog.e("LiveConstants.HOST_INFO 返回数据失败!");
                    return;
                }
                ModelHostInfo hostInfo = (ModelHostInfo) datas.get(0);
                if (hostInfo != null && mUserHeadTopView != null) {
                    //TODO 主播的资料放哪里?
                }
                break;
            case LiveConstants.HOST_TAGS:
                //获取主播标签
                break;
            case LiveConstants.AUDIENCE_COUNT:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //获取观众人数
                        if (checkDataIsNull(datas)) {
                            MGLog.e("LiveConstants.AUDIENCE_COUNT 返回数据失败!");
                            return;
                        }
                        ModelAudienceCount audienceCount = (ModelAudienceCount) datas.get(0);
                        //更新观众人数
                        if (audienceCount != null && !TextUtils.isEmpty(audienceCount.getCount())) {

                            CurLiveInfo.setMembers(Integer.valueOf(audienceCount.getCount()));
                            doUpdateMembersCount();
                        }
                    }
                });
                break;
            case LiveConstants.LIST_OF_STORES:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mBaoBaoAdapter == null) {
                            return;
                        }
                        if (datas == null) {
                            mBaoBaoAdapter.setData(null);
                        } else {
                            mBaoBaoAdapter.setData(datas);
                        }
                        mBaoBaoAdapter.notifyDataSetChanged();
                    }
                });

                break;
            case SellerConstants.LIVE_BIZ_SHOP:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (datas != null && datas.size() > 0) {
                            mUserBottomTool.setmSellerDetailInfo((SellerDetailInfo) datas.get(0));
                            mUserBottomTool.notifyDataChange();
                        }
                    }
                });
                break;
            case LiveConstants.GET_USER_RED_PACKETS:
                MGLog.e("test: 直播过程用户抢到的红包数据: " + datas.size());
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        List<UserRedPacketInfo> userRedPacketInfos = testDatas();
                        if (datas == null) {
                            mRedPacketAdapter.setMdatas(null);
                        } else {
                            mRedPacketAdapter.setMdatas(datas);
                        }
                        mRedPacketAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case LiveConstants.GET_PACKET_RESULT:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUserBottomTool.showRedPacketResult(datas);
                    }
                });
                break;
            case LiveConstants.STORES_RANDOM_COMMENT:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isAnchor) {
                            mHostTopView.setKeyWords(getKeyWord((List<ModelStoresRandomComment>) datas));
                        } else {
                            mUserHeadTopView.setKeyWord(getKeyWord((List<ModelStoresRandomComment>) datas));
                        }
                    }
                });
                break;
//            case LiveConstants.CHECK_FOCUS:
//                if (!SDCollectionUtil.isEmpty(datas)) {
//                    ModelCheckFocus modelCheckFocus = (ModelCheckFocus) datas.get(0);
//                    if ("1".equals(modelCheckFocus.getFocus())) {
//                        //已关注
//                        MGUIUtil.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mUserHeadTopView.setHasFocus();
//                            }
//                        });
//                    } else {
//                        //TODO 未关注  doNothing
//                    }
//                }
//                break;
        }
    }

    private String getKeyWord(List<ModelStoresRandomComment> datas) {
        String keyWord = "";
        if (!SDCollectionUtil.isEmpty(datas)) {
            for (ModelStoresRandomComment bean : datas) {
                if (!TextUtils.isEmpty(bean.getContent()))
                    keyWord = keyWord + " " + bean.getContent();
            }
        }
        return keyWord;
    }

    /*校验数据*/
    public boolean checkDataIsNull(List datas) {
        if (datas != null && datas.size() > 0) {
            return false;//不为空
        } else {
            return true;//为null
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    /**
     * 显示用户自己的弹幕
     *
     * @param params
     */
    @Override
    public void showDanmuSelf(HashMap<String, String> params) {
        getDanmu(params);
    }

    /**
     * 收到弹幕
     */
    @Override
    public void getDanmu(HashMap<String, String> params) {
        if (params != null && danmukiller != null) {
            danmukiller.addDanmu(new DanmuBean(params.get(Constants.DANMU_USER_AVATAR_URL), params.get(Constants.DANMU_MESSAGE), params.get(Constants.DANMU_USER_USER_NAME)));
        }
    }

    @Override
    public void withoutEnoughMoney(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MGToast.showToast(msg);
            }
        });
    }

    /**
     * 发礼物回调
     */
    @Override
    public void requestSendGift(GiftListBean giftInfo, int num) {
        giftInfo.setNum(num);
        giftInfo.setUserAvatar(App.getApplication().getmUserCurrentInfo().getUserInfoNew().getIcon());
        giftInfo.setUserId(App.getApplication().getmUserCurrentInfo().getUserInfoNew().getUser_id());
        giftInfo.setUserName(App.getApplication().getmUserCurrentInfo().getUserInfoNew().getNick());
        showGift(giftInfo);
        mLiveHelper.sendGift(giftInfo);
    }

    /**
     * 接收到IM礼物消息，需要处理小礼物大礼物的id，作不同的展现方式
     *
     * @param params
     */
    @Override
    public void getGift(HashMap<String, String> params) {
        GiftListBean bean = new GiftListBean();
        bean.setId(params.get("id"));
        bean.setName(params.get("name"));
        bean.setIcon(params.get("icon"));
        bean.setType(params.get("type"));
        bean.setNum(Integer.parseInt(params.get("count")));
        bean.setUserAvatar(params.get("avatar"));
        bean.setUserName(params.get("nickname"));
        showGift(bean);
    }

    private void showGift(GiftListBean bean) {
        switch (bean.getId()) {
            /**
             * 小礼物 随弹幕出现
             */
            case GiftId.STAR:
            case GiftId.FLOWER:
            case GiftId.SWEET:
            case GiftId.MIGUO_BABY:

            case GiftId.KISS:
            case GiftId.GOOD_FORTUNE:
                showSmallGift(bean);
                break;
            /**
             * 么么哒 屏幕随机出现
             * 福气临门 屏幕中心出现，慢慢消失
             */
//            case GiftId.KISS:
//                showRandomGift(bean);
//                break;
//            case GiftId.GOOD_FORTUNE:
//                showRedPacket(bean);
//                break;
            /**
             * 大礼物，动图序列帧
             */
            case GiftId.BEST_LOVE:
            case GiftId.FIREWORKS:
            case GiftId.BASKETS:
            case GiftId.BOMBARDIER:
            case GiftId.FERRARI:
            case GiftId.SOAR:
                showBigGift(bean);
                break;
        }
    }

    /**
     * 小礼物
     * //test commit and push branch
     *
     * @param bean
     */
    private void showSmallGift(GiftListBean bean) {
        Log.d("smallgift", "there is a small gift...");
        smallGifView.addGift(bean);
    }

    /**
     * 屏幕随机出现的礼物
     */
    private void showRandomGift(GiftListBean bean) {
        bigGifView.addKiss(bean);
    }

    private void showRedPacket(GiftListBean bean) {
        bigGifView.addRedPacket(bean);
    }

    /**
     * 大礼物
     *
     * @param bean
     */
    private void showBigGift(GiftListBean bean) {
        bigGifView.addBigGift(bean);
    }

}
