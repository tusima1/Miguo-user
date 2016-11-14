package com.miguo.live.views.view;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerConstants;
import com.fanwe.seller.model.SellerDetailInfo;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.live.adapters.HeadTopAdapter;
import com.miguo.live.adapters.LiveChatMsgListAdapter;
import com.miguo.live.adapters.PagerBaoBaoAdapter;
import com.miguo.live.adapters.PagerRedPacketAdapter;
import com.miguo.live.model.LiveChatEntity;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.PlaySetInfo;
import com.miguo.live.model.getAudienceCount.ModelAudienceCount;
import com.miguo.live.model.getAudienceList.ModelAudienceInfo;
import com.miguo.live.model.getHostInfo.ModelHostInfo;
import com.miguo.live.model.getReceiveCode.ModelReceiveCode;
import com.miguo.live.model.getStoresRandomComment.ModelStoresRandomComment;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.presenters.ShopAndProductView;
import com.miguo.live.presenters.TencentHttpHelper;
import com.miguo.live.receiver.NetWorkStateReceiver;
import com.miguo.live.views.LiveUtil;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.customviews.PlayBackBottomToolView;
import com.miguo.live.views.customviews.PlayBackSeekBarView;
import com.miguo.live.views.customviews.UserHeadTopView;
import com.miguo.utils.MGLog;
import com.miguo.utils.MGUIUtil;
import com.miguo.utils.RTMPUtils;
import com.miguo.utils.test.MGTimer;
import com.tencent.TIMCallBack;
import com.tencent.TIMGroupManager;
import com.tencent.av.TIMAvManager;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.LiveInfoJson;
import com.tencent.qcloud.suixinbo.presenters.EnterLiveHelper;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;
import com.tencent.qcloud.suixinbo.presenters.LoginHelper;
import com.tencent.qcloud.suixinbo.presenters.viewinface.EnterQuiteRoomView;
import com.tencent.qcloud.suixinbo.presenters.viewinface.LiveView;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.tencent.qcloud.suixinbo.views.customviews.BaseActivity;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 点播页面。
 * Created by Administrator on 2016/9/20.
 */
public class PlayBackActivity extends BaseActivity implements ITXLivePlayListener, View.OnClickListener, LiveView, CallbackView, EnterQuiteRoomView, ShopAndProductView {


    private TXLivePlayer mLivePlayer = null;
    private boolean mVideoPlay;
    private TXCloudVideoView mPlayerView;
    private ImageView mLoadingView;
    private boolean mHWDecode = false;
    private View root;
    private ImageView mBtnPlay;
    private SeekBar mSeekBar;
    private TextView mTextDuration;
    private TextView mTextStart;
    private int mCurrentRenderMode;
    private int mCurrentRenderRotation;
    private TXLivePlayConfig mPlayConfig;
    /**
     * 取商品和门店相关信息。
     */
    private SellerHttpHelper mSellerHttpHelper;
    private LiveHelper mLiveHelper;
    private EnterLiveHelper mEnterRoomHelper;
    private GetAudienceTask mGetAudienceTask;//取观众 列表。
    private LoginHelper mTLoginHelper;
    private TencentHttpHelper tencentHttpHelper;
    private LiveHttpHelper mLiveHttphelper;
    private ArrayList<LiveChatEntity> mArrayListChatEntity;
    private LiveChatMsgListAdapter mChatMsgListAdapter;

    private static final int MINFRESHINTERVAL = 500;


    private boolean mBoolRefreshLock = false;
    private boolean mBoolNeedRefresh = true;
    private final Timer mTimer = new Timer();
    private long mTrackingTouchTS = 0;
    private boolean mStartSeek = false;
    private boolean mVideoPause = false;
    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    private ArrayList<LiveChatEntity> mTmpChatList = new ArrayList<LiveChatEntity>();//缓冲队列
    private TimerTask mTimerTask = null;

    private ArrayList<String> mRenderUserList = new ArrayList<>();
    private ListView mListViewMsgItems;
    private static final int REFRESH_LISTVIEW = 5;


    private UserHeadTopView mUserHeadTopView;

    private PlayBackSeekBarView playBackSeekBarView;
    private PlayBackBottomToolView playBackBottomToolView;

    private PagerBaoBaoAdapter mBaoBaoAdapter;
    private PagerRedPacketAdapter mRedPacketAdapter;

    private Timer mVideoTimer, mAudienceTimer;

    /**
     * 头部头像adapter.
     */
    private HeadTopAdapter mHeadTopAdapter;
    private HashMap<Integer, PlaySetInfo> playUrlList;

    PlaySetInfo currentPlayInfo;
    /**
     * 拉流地址。
     */
    String playUrl = "";

    /**
     * 聊天室ID。
     */
    String chat_room_id;
    String file_size;

    String duration;


    String playset;

    private boolean isFirstLogin = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   int roomNum=87654321;
        //  CurLiveInfo.setRoomNum(roomNum);
        setActivityParams();
        setContentView(R.layout.act_play_back);
        getIntentData();
        mPlayConfig = new TXLivePlayConfig();
        if (mLivePlayer == null) {
            mLivePlayer = new TXLivePlayer(this);
        }
        getPlayUrlList();
        initHelper();
        initView();

//        mCurrentRenderMode = TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;
//        mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;



    }

    /**
     * 解析直播流地址。
     */
    public void getPlayUrlList() {
        if (playUrlList == null) {
            playUrlList = new HashMap<>();
        }
        Type listType = new TypeToken<List<PlaySetInfo>>() {
        }.getType();
        Gson gson = new Gson();
        List<PlaySetInfo> playSetInfoList = gson.fromJson(playset, listType);
        if(playSetInfoList==null||playSetInfoList.size()<1){
            playError();
            return;
        }
        for (Iterator iterator = playSetInfoList.iterator(); iterator.hasNext(); ) {
            PlaySetInfo o = (PlaySetInfo) iterator.next();
            playUrlList.put(o.getDefinition(), o);
        }
         currentPlayInfo = RTMPUtils.checkUrlByWIFI(playUrlList);
        if (currentPlayInfo == null) {
            playError();
            return;
        }
        playUrl = currentPlayInfo.getUrl();
        changeOrientation();
    }

    private void playError(){
        showInvalidateToast("网络有问题或者当前点播地址错误。");
        finish();
    }


    private void getIntentData() {
        Bundle data = getIntent().getExtras();
        if (data == null) {
            MGToast.showToast("数据传输错误!");
            finish();
            return;
        }
        chat_room_id = data.getString("chat_room_id", "");
        file_size = data.getString("file_size", "");
        duration = data.getString("duration", "");

        playset = data.getString("playset", "");
        Log.e("test", chat_room_id + "--" + file_size + "--" + duration + "--" + playset);
    }

    public void initView() {
        root = findViewById(R.id.root);
        mVideoPlay = false;
        mPlayerView = (TXCloudVideoView) findViewById(R.id.video_view);
        mLoadingView = (ImageView) findViewById(R.id.loadingImageView);

        //播放进度条。
        playBackSeekBarView = (PlayBackSeekBarView) findViewById(R.id.play_seekbar);
        //播放、暂停播放。
        mBtnPlay = (ImageView) playBackSeekBarView.findViewById(R.id.btnPlay);
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoPlay) {
                    if (mPlayType == TXLivePlayer.PLAY_TYPE_VOD_FLV || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_HLS || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_MP4) {
                        if (mVideoPause) {
                            mLivePlayer.resume();
                            mBtnPlay.setBackgroundResource(R.drawable.play_pause);
                        } else {
                            mLivePlayer.pause();
                            mBtnPlay.setBackgroundResource(R.drawable.play_start);
                        }
                        mVideoPause = !mVideoPause;

                    } else {
                        stopPlayRtmp();
                        mVideoPlay = !mVideoPlay;
                    }

                } else {
                    if (startPlayRtmp()) {
                        mVideoPlay = !mVideoPlay;
                    }
                }
            }
        });
        mSeekBar = (SeekBar) playBackSeekBarView.findViewById(R.id.seekbar);
        initSeekBar();
        mTextStart = (TextView) playBackSeekBarView.findViewById(R.id.play_start);
        mTextDuration = (TextView) playBackSeekBarView.findViewById(R.id.duration);
        mBaoBaoAdapter = new PagerBaoBaoAdapter(this);
        //底部IM + 其它。

        playBackBottomToolView = (PlayBackBottomToolView) findViewById(R.id.normal_user_bottom_tool);

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

        mListViewMsgItems = (ListView) findViewById(R.id.im_msg_listview);
        mArrayListChatEntity = new ArrayList<LiveChatEntity>();
        mChatMsgListAdapter = new LiveChatMsgListAdapter(this, mListViewMsgItems,
                mArrayListChatEntity);
        mListViewMsgItems.setAdapter(mChatMsgListAdapter);

        mLiveHttphelper.getStoresRandomComment(CurLiveInfo.getShopID(), "3");
    }

    public void initSeekBar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean bFromUser) {
                mTextStart.setText(String.format("%02d:%02d", progress / 60, progress % 60));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mStartSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mLivePlayer != null) {
                    mLivePlayer.seek(seekBar.getProgress());
                }
                mTrackingTouchTS = System.currentTimeMillis();
                mStartSeek = false;
            }
        });
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
        enterImGroup();
    }

    /**
     * 报地址不合法错误。
     */
    public void showInvalidateToast(String message) {
        String defaultMessage = "播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!";
        if (TextUtils.isEmpty(message)) {
            message = defaultMessage;
        }
        SDToast.showToast(message, Toast.LENGTH_LONG);


    }

    private boolean checkPlayUrl(final String playUrl) {
        if (TextUtils.isEmpty(playUrl) || (!playUrl.startsWith("http://") && !playUrl.startsWith("https://") && !playUrl.startsWith("rtmp://"))) {
            showInvalidateToast("");
            finish();
            return false;
        }
        if (playUrl.startsWith("http://") || playUrl.startsWith("https://")) {
            if (playUrl.contains(".flv")) {
                mPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
            } else if (playUrl.contains(".m3u8")) {
                mPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
            } else if (playUrl.toLowerCase().contains(".mp4")) {
                mPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
            } else {
                showInvalidateToast("");
                finish();
                return false;
            }
        } else {
            showInvalidateToast("");
            finish();
            return false;
        }
        return true;
    }

    private boolean startPlayRtmp() {
        if (!checkPlayUrl(playUrl)) {
            return false;
        }
        mBtnPlay.setBackgroundResource(R.drawable.play_pause);
        mLivePlayer.setPlayerView(mPlayerView);
        mLivePlayer.setPlayListener(this);
        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
        // (1) 只有 4.3 以上android系统才支持
        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
        mLivePlayer.enableHardwareDecode(mHWDecode);
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
        mLivePlayer.setRenderMode(mCurrentRenderMode);
        //设置播放器缓存策略
        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
        //mLivePlayer.setCacheTime(5);
        mLivePlayer.setConfig(mPlayConfig);

        int result = mLivePlayer.startPlay(playUrl, mPlayType); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
        if (result == -2) {
            showInvalidateToast("非腾讯云链接地址。");
        }
        if (result != 0) {
            mBtnPlay.setBackgroundResource(R.drawable.play_start);
            return false;
        }
        mLivePlayer.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);

        startLoadingAnimation();
        return true;
    }

    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            stopLoadingAnimation();
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            if (mStartSeek) {
                return;
            }
            int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
            int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
            long curTS = System.currentTimeMillis();

            // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
            if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                return;
            }
            mTrackingTouchTS = curTS;

            if (mSeekBar != null) {
                mSeekBar.setProgress(progress);
            }
            if (mTextStart != null) {
                mTextStart.setText(String.format("%02d:%02d", progress / 60, progress % 60));
            }
            if (mTextDuration != null) {
                mTextDuration.setText(String.format("%02d:%02d", duration / 60, duration % 60));
            }
            if (mSeekBar != null) {
                mSeekBar.setMax(duration);
            }
            return;
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            stopPlayRtmp();
            mVideoPlay = false;
            mVideoPause = false;
            if (mTextStart != null) {
                mTextStart.setText("00:00");
            }
            if (mSeekBar != null) {
                mSeekBar.setProgress(0);
            }
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
            startLoadingAnimation();
        }
        if (event < 0) {
            SDToast.showToast(param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT);
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            stopLoadingAnimation();
        }
    }

    /**
     * 根据宽高比设置当前的方向。
     */
    public void changeOrientation() {
        if (mLivePlayer == null || currentPlayInfo==null) {
            return;
        }
        float width = currentPlayInfo.getVwidth();
        float height = currentPlayInfo.getVheight();

        //横屏。
        if (width - height > 0) {
            mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_LANDSCAPE;
        } else {
            mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
        }
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
    }


    /**
     * 进入chartroom.
     */
    public void enterImGroup() {
        TIMGroupManager.getInstance().applyJoinGroup(CurLiveInfo.getRoomNum() + "", "点播用户", new TIMCallBack() {
            @java.lang.Override
            public void onError(int code, String desc) {
                //接口返回了错误码code和错误描述desc，可用于原因
                //错误码code列表请参见错误码表
                if (10013 == code) {
                    Log.e("进来了errorcode:" + code + ",", desc);

                    if (mLiveHttphelper != null) {
                        mLiveHttphelper.enterRoom(CurLiveInfo.getRoomNum() + "", "2", "");
                    }
                    mLiveHelper.initTIMListener("" + CurLiveInfo.getRoomNum());
                    //发消息通知上线
                    if (mLiveHttphelper != null) {
                        mLiveHttphelper.enterRoom(CurLiveInfo.getRoomNum() + "", "2", "");
                        mLiveHelper.sendGroupMessage(Constants.PALYBACK_ENTERROOM, "进来了");
                    }
                }
                Log.e("进来了errorcode:" + code + ",", desc);

            }

            @java.lang.Override
            public void onSuccess() {
                Log.e("进来了success:", "desc");

                mLiveHelper.initTIMListener("" + CurLiveInfo.getRoomNum());
                //发消息通知上线
                if (mLiveHttphelper != null) {
                    mLiveHttphelper.enterRoom(CurLiveInfo.getRoomNum() + "", "2", "");
                    mLiveHelper.sendGroupMessage(Constants.PALYBACK_ENTERROOM, "进来了");

                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();


        if (mVideoPlay && !mVideoPause) {
            if (mPlayType == TXLivePlayer.PLAY_TYPE_VOD_FLV || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_HLS || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_MP4) {
                if (mLivePlayer != null) {
                    mLivePlayer.resume();
                }
            } else if (Build.VERSION.SDK_INT >= 23) { //目前android6.0以上暂不支持后台播放
                startPlayRtmp();

            }
        }

        if (mPlayerView != null) {
            mPlayerView.onResume();
        }
        if (isFirstLogin) {
            isFirstLogin = false;
            startPlayRtmp();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPlayType == TXLivePlayer.PLAY_TYPE_VOD_FLV || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_HLS || mPlayType == TXLivePlayer.PLAY_TYPE_VOD_MP4) {
            if (mLivePlayer != null) {
                mLivePlayer.pause();
            }
        } else if (Build.VERSION.SDK_INT >= 23) { //目前android6.0以上暂不支持后台播放
            stopPlayRtmp();
        }

        if (mPlayerView != null) {
            mPlayerView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userExit();
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay(true);
        }
        if (mPlayerView != null) {
            mPlayerView.onDestroy();
        }


        if (null != mVideoTimer) {
            mVideoTimer.cancel();
            mVideoTimer = null;
        }
        if (null != mAudienceTimer) {
            mAudienceTimer.cancel();
            mAudienceTimer = null;
        }

        CurLiveInfo.setMembers(0);
        CurLiveInfo.setAdmires(0);
        CurLiveInfo.setCurrentRequestCount(0);
        unregisterReceiver();
        if (mLiveHelper != null) {

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
        if (playBackSeekBarView != null) {
            playBackSeekBarView.onDestroy();
            playBackSeekBarView = null;
        }
        if (playBackBottomToolView != null) {
            playBackBottomToolView.onDestroy();
            playBackBottomToolView = null;
        }


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void stopPlayRtmp() {
        mBtnPlay.setBackgroundResource(R.drawable.play_start);
        stopLoadingAnimation();
        if (mLivePlayer != null) {
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(true);
        }
    }

    private void startLoadingAnimation() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
            ((AnimationDrawable) mLoadingView.getDrawable()).start();
        }
        mVideoPlay = true;
    }

    private void stopLoadingAnimation() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
            ((AnimationDrawable) mLoadingView.getDrawable()).stop();
        }
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
        refreshTextListView(faceUrl, TextUtils.isEmpty(name) ? id : name, "进入房间", Constants
                .MEMBER_ENTER);
        int members = CurLiveInfo.getMembers() + 1;
        CurLiveInfo.setMembers(members);

        //人数加1,可以设置到界面上
        doUpdateMembersCount();

    }

    @Override
    public void memberQuit(String id, String name, String faceUrl) {
//        refreshTextListView(faceUrl, TextUtils.isEmpty(name) ? id : name, "退出房间了", Constants
//                .MEMBER_EXIT);

        if (CurLiveInfo.getMembers() > 1) {
            int members = CurLiveInfo.getMembers() - 1;
            CurLiveInfo.setMembers(members);
            doUpdateMembersCount();
        }
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

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_LISTVIEW:
                    doRefreshListView();
                    break;
                default:
                    break;

            }
            return false;
        }
    });

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

        if (mLiveHttphelper != null) {
            mLiveHttphelper.exitRoom(CurLiveInfo.getRoomNum() + "", "2");
        }
        //退出群
        TIMGroupManager.getInstance().quitGroup(CurLiveInfo.getRoomNum() + "", new TIMCallBack() {
            @java.lang.Override
            public void onError(int code, String desc) {
                //接口返回了错误码code和错误描述desc，可用于原因
                //错误码code列表请参见错误码表
                Log.e("退出了。" + CurLiveInfo.getRoomNum() + "errorcode:" + CurLiveInfo.getRoomNum() + ",", desc);
            }

            @java.lang.Override
            public void onSuccess() {
                Log.e("退出了。" + CurLiveInfo.getRoomNum() + "success:", "desc");
            }
        });
        finish();

    }

    @Override
    public void withoutEnoughMoney(String msg) {

    }

    @Override
    public void hostExitByForce() {
        userExit();
    }

    @Override
    public void enterRoomComplete(int id_status, boolean isSucc) {

        if (isSucc == true) {
            //IM初始化
            mLiveHelper.initTIMListener("" + CurLiveInfo.getRoomNum());

            if (id_status == Constants.HOST) {//主播方式加入房间成功
                //开启摄像头渲染画面

            } else {
                //发消息通知上线
                //  mLiveHelper.sendGroupMessage(Constants.PALYBACK_ENTERROOM, MySelfInfo.getInstance().getNickName()+"进入房间。");

            }
        }

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

    @Override
    public void exitActivity() {

    }

    //--------------- http请求 -------------
    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, final List datas) {
        try{
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
                            if (mUserHeadTopView != null) {
                                mUserHeadTopView.refreshData(datas);
                            }
                            doUpdateMembersCount();
                            if(mHeadTopAdapter != null){
                                mHeadTopAdapter.notifyDataSetChanged();
                            }
                        }
                    });


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
                            if(mBaoBaoAdapter == null){
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

                                if (datas != null && datas.size() > 0 && playBackBottomToolView != null) {
                                    playBackBottomToolView.setmSellerDetailInfo((SellerDetailInfo) datas.get(0));
                                    playBackBottomToolView.notifyDataChange();
                                }

                        }
                    });
                    break;

                case LiveConstants.STORES_RANDOM_COMMENT:
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mUserHeadTopView == null || datas == null){
                                return;
                            }
                            mUserHeadTopView.setKeyWord(getKeyWord((List<ModelStoresRandomComment>) datas));
                        }
                    });
                    break;
            }
        }catch (Exception e){
            Log.e("PlaybackActivity", "excetion..");
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


}
