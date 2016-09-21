package com.miguo.live.views.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.SellerDetailInfo;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.miguo.live.adapters.HeadTopAdapter;
import com.miguo.live.adapters.LiveChatMsgListAdapter;
import com.miguo.live.adapters.PagerBaoBaoAdapter;
import com.miguo.live.adapters.PagerRedPacketAdapter;
import com.miguo.live.model.LiveChatEntity;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getAudienceCount.ModelAudienceCount;
import com.miguo.live.model.getAudienceList.ModelAudienceInfo;
import com.miguo.live.model.getHostInfo.ModelHostInfo;
import com.miguo.live.model.getReceiveCode.ModelReceiveCode;
import com.miguo.live.presenters.LiveCommonHelper;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.presenters.ShopAndProductView;
import com.miguo.live.presenters.TencentHttpHelper;
import com.miguo.live.receiver.NetWorkStateReceiver;
import com.miguo.live.views.LiveOrientationHelper;
import com.miguo.live.views.LiveUtil;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.customviews.PlayBackBottomToolView;
import com.miguo.live.views.customviews.UserHeadTopView;
import com.miguo.utils.MGLog;
import com.miguo.utils.MGUIUtil;
import com.miguo.utils.test.MGTimer;
import com.tencent.av.TIMAvManager;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.LiveInfoJson;
import com.tencent.qcloud.suixinbo.presenters.EnterLiveHelper;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;
import com.tencent.qcloud.suixinbo.presenters.LoginHelper;
import com.tencent.qcloud.suixinbo.presenters.viewinface.EnterQuiteRoomView;
import com.tencent.qcloud.suixinbo.presenters.viewinface.LiveView;
import com.tencent.qcloud.suixinbo.views.customviews.BaseActivity;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 点播页面。
 * Created by Administrator on 2016/9/20.
 */
public class PlayBackActivity  extends BaseActivity implements ITXLivePlayListener, View.OnClickListener,LiveView,CallbackView,EnterQuiteRoomView ,ShopAndProductView {


    private TXLivePlayer     mLivePlayer = null;
    private boolean          mVideoPlay;
    private TXCloudVideoView mPlayerView;
    private ImageView mLoadingView;
    private boolean          mHWDecode   = false;
    private View root;
    private Button mBtnLog;
    private Button           mBtnPlay;
    private Button           mBtnRenderRotation;
    private Button           mBtnRenderMode;
    private Button           mBtnHWDecode;
    private ScrollView     mScrollView;
    private SeekBar      mSeekBar;
    private TextView      mTextDuration;
    private TextView         mTextStart;
    private int              mCurrentRenderMode;
    private int              mCurrentRenderRotation;
    private TXLivePlayConfig mPlayConfig;
    /**
     * 取商品和门店相关信息。
     */
    private SellerHttpHelper mSellerHttpHelper;
    private LiveHelper mLiveHelper;
    private EnterLiveHelper mEnterRoomHelper;
    private GetAudienceTask mGetAudienceTask;//取观众 列表。

    private LoginHelper mTLoginHelper;
    private LiveCommonHelper mCommonHelper;
    private LiveOrientationHelper mOrientationHelper;
    private TencentHttpHelper tencentHttpHelper;
    private LiveHttpHelper mLiveHttphelper;
    private ArrayList<LiveChatEntity> mArrayListChatEntity;
    private LiveChatMsgListAdapter mChatMsgListAdapter;

    private static final int MINFRESHINTERVAL = 500;
    private static final int UPDAT_WALL_TIME_TIMER_TASK = 1;
    private static final int TIMEOUT_INVITE = 2;
    private boolean mBoolRefreshLock = false;
    private boolean mBoolNeedRefresh = true;
    private final Timer mTimer = new Timer();
    private long             mTrackingTouchTS = 0;
    private boolean          mStartSeek = false;
    private boolean          mVideoPause = false;
    private int              mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    private ArrayList<LiveChatEntity> mTmpChatList = new ArrayList<LiveChatEntity>();//缓冲队列
    private TimerTask mTimerTask = null;

    private ArrayList<String> mRenderUserList = new ArrayList<>();


    private UserHeadTopView mUserHeadTopView;

    private PlayBackBottomToolView playBackBottomToolView;

    private PagerBaoBaoAdapter mBaoBaoAdapter;
    private PagerRedPacketAdapter mRedPacketAdapter;

    private Timer mVideoTimer, mAudienceTimer ;

    /**
     * 头部头像adapter.
     */
    private HeadTopAdapter mHeadTopAdapter;
    private String chat_room_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityParams();
        setContentView(R.layout.act_play_back);
        getIntentData();
        initHelper();
        initView();
        mCurrentRenderMode     = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;

        mPlayConfig = new TXLivePlayConfig();
        if (mLivePlayer == null){
            mLivePlayer = new TXLivePlayer(this);
        }
    }

    private void getIntentData() {
        Bundle data=getIntent().getExtras();
        if (data==null){
            MGToast.showToast("数据传输错误!");
            finish();
            return;
        }
        chat_room_id = data.getString("chat_room_id", "");
        String file_size = data.getString("file_size", "");
        String duration = data.getString("duration", "");
        String file_id = data.getString("file_id", "");
        String vid = data.getString("vid", "");
        String playset = data.getString("playset", "");
        Log.e("test", chat_room_id +"--"+file_size+"--"+duration+"--"+file_id+"--"+vid+"--"+playset);

    }

    public void initView(){
        root = findViewById(R.id.root);

        mPlayerView = (TXCloudVideoView) findViewById(R.id.video_view);
        mLoadingView = (ImageView) findViewById(R.id.loadingImageView);

        playBackBottomToolView =(PlayBackBottomToolView) findViewById(R.id.normal_user_bottom_tool);
        mBaoBaoAdapter = new PagerBaoBaoAdapter(this);
        playBackBottomToolView.setmBaobaoAdapter(mBaoBaoAdapter);

        mUserHeadTopView = (UserHeadTopView) findViewById(R.id.user_top_layout);//观众的topview
        mUserHeadTopView.setmLiveView(this);
        mHeadTopAdapter = new HeadTopAdapter(null, this);
        mUserHeadTopView.setmAdapter(mHeadTopAdapter);
        mUserHeadTopView.init();
        mUserHeadTopView.setVisibility(View.VISIBLE);
        mUserHeadTopView.initNeed(this);
        doUpdateMembersCount();

        String hostImg = CurLiveInfo.getHostAvator();
        mUserHeadTopView.setHostImg(hostImg);
        mUserHeadTopView.setHostName(CurLiveInfo.getHostName());
        if (CurLiveInfo.getModelShop() != null && !TextUtils.isEmpty(CurLiveInfo.getModelShop()
                .getShop_name())) {
            mUserHeadTopView.setLocation(CurLiveInfo.getModelShop().getShop_name());
        }


        mRedPacketAdapter = new PagerRedPacketAdapter();
        playBackBottomToolView.setmRedPacketAdapter(mRedPacketAdapter);
        initViewNeed();
        mLiveHttphelper.getAudienceCount(CurLiveInfo.getRoomNum() + "", "1");

        //顶部用户头像列表
        mAudienceTimer = new Timer(true);
        mGetAudienceTask = new GetAudienceTask();
        mAudienceTimer.schedule(mGetAudienceTask, 1000, 30 * 1000);
    }

    /**
     * 观众 底部操作view需要的参数
     */
    private void initViewNeed() {
        //初始化底部
        if (playBackBottomToolView != null) {
            playBackBottomToolView.initView(this, mLiveHelper, root, this);
        }
        if (!TextUtils.isEmpty(CurLiveInfo.shopID) && !LiveUtil.checkIsHost()) {
            getShopDetail(CurLiveInfo.shopID);
        }
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

        //注册网络连接监听
        registerReceiver(mNetWorkReceiver, NetWorkStateReceiver.NETWORK_FILTER);
    }
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

    private void initHelper() {
        mTLoginHelper = new LoginHelper(this, this);
        mEnterRoomHelper = new EnterLiveHelper(this, this);
        mSellerHttpHelper = new SellerHttpHelper(this, this);
        mLiveHttphelper = new LiveHttpHelper(this, this);
        //房间内的交互协助类
        mLiveHelper = new LiveHelper(this, this);
        // 用户资料类
        tencentHttpHelper = new TencentHttpHelper(this);
        //屏幕方向管理,初始化
        mOrientationHelper = new LiveOrientationHelper();
        //公共功能管理类
        mCommonHelper = new LiveCommonHelper(mLiveHelper, this);
    }

    /**
     * 取观众 列表
     */
    private class GetAudienceTask extends TimerTask {
        @Override
        public void run() {
            mLiveHttphelper.getAudienceList(chat_room_id);
        }
    }
    private void unregisterReceiver() {

        unregisterReceiver(mNetWorkReceiver);
    }
    @Override
    public void onPlayEvent(int i, Bundle bundle) {

    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showVideoView(boolean isHost, String id) {

    }

    @Override
    public void showInviteDialog() {

    }

    @Override
    public void refreshText(String text, String name) {

    }

    @Override
    public void refreshText(String text, String name, String faceUrl) {

    }

    @Override
    public void refreshThumbUp() {

    }

    @Override
    public void refreshUI(String id) {

    }

    @Override
    public boolean showInviteView(String id) {
        return false;
    }

    @Override
    public void cancelInviteView(String id) {

    }

    @Override
    public void cancelMemberView(String id) {

    }

    @Override
    public void memberJoin(String id, String name, String faceUrl) {

    }

    @Override
    public void memberQuit(String id, String name, String faceUrl) {

    }

    @Override
    public void readyToQuit() {

    }

    @Override
    public void hideInviteDialog() {

    }

    @Override
    public void pushStreamSucc(TIMAvManager.StreamRes streamRes) {

    }

    @Override
    public void stopStreamSucc() {

    }

    @Override
    public void startRecordCallback(boolean isSucc) {

    }

    @Override
    public void stopRecordCallback(boolean isSucc, List<String> files) {

    }

    @Override
    public void hostLeave(String id, String name, String faceUrl) {

    }

    @Override
    public void hostBack(String id, String name, String faceUrl) {

    }

    @Override
    public void getHostRedPacket(HashMap<String, String> params) {

    }

    @Override
    public void getDanmu(HashMap<String, String> params) {

    }

    @Override
    public void sendHostRedPacket(String id, String duration) {

    }

    @Override
    public void tokenInvalidateAndQuit() {

    }

    @Override
    public void userExit() {

    }

    @Override
    public void withoutEnoughMoney(String msg) {

    }
    @Override
    public void enterRoomComplete(int id_status, boolean succ) {

    }

    @Override
    public void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo) {

    }

    @Override
    public void memberQuiteLive(String[] list) {

    }

    @Override
    public void hostQuiteLive(String type, String responseBody) {

    }

    @Override
    public void memberJoinLive(String[] list) {

    }

    @Override
    public void alreadyInLive(String[] list) {

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
//                            if (mHostTopView != null) {
//                                mHostTopView.refreshData(datas);
//                            }
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
                            playBackBottomToolView.setmSellerDetailInfo((SellerDetailInfo) datas.get(0));
                            playBackBottomToolView.notifyDataChange();
                        }
                    }
                });
                break;
            case LiveConstants.GET_USER_RED_PACKETS:
//                MGLog.e("test: 直播过程用户抢到的红包数据: " + datas.size());
//                MGUIUtil.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        List<UserRedPacketInfo> userRedPacketInfos = testDatas();
//                        if (datas == null) {
//                            mRedPacketAdapter.setMdatas(null);
//                        } else {
//                            mRedPacketAdapter.setMdatas(datas);
//                        }
//                        mRedPacketAdapter.notifyDataSetChanged();
//                    }
//                });
                break;
//            case LiveConstants.GET_PACKET_RESULT:
//                MGUIUtil.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mUserBottomTool.showRedPacketResult(datas);
//                    }
//                });
        }
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
     * update 观众 数量 。
     */
    public void doUpdateMembersCount() {

        MGUIUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    if (mUserHeadTopView != null) {
                        mUserHeadTopView.updateAudienceCount(CurLiveInfo.getMembers() + "");
                    }
            }
        });
    }

    @Override
    public void showDanmuSelf(HashMap<String, String> params) {

    }

    @Override
    public void getGift(HashMap<String, String> params) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}
