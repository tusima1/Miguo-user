package com.miguo.live.views.view;

import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
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

import com.fanwe.library.utils.SDToast;
import com.fanwe.seller.presenters.SellerHttpHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguo.live.adapters.HeadTopAdapter;
import com.miguo.live.adapters.LiveChatMsgListAdapter;
import com.miguo.live.adapters.PagerBaoBaoAdapter;
import com.miguo.live.model.LiveChatEntity;
import com.miguo.live.model.PlaySetInfo;
import com.miguo.live.presenters.LiveCommonHelper;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.presenters.TencentHttpHelper;
import com.miguo.live.receiver.NetWorkStateReceiver;
import com.miguo.live.views.LiveOrientationHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.customviews.PlayBackBottomToolView;
import com.miguo.live.views.customviews.PlayBackSeekBarView;
import com.miguo.live.views.customviews.UserHeadTopView;
import com.miguo.live.views.danmu.Danmukiller;
import com.miguo.utils.test.MGTimer;
import com.tencent.av.TIMAvManager;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.LiveInfoJson;
import com.tencent.qcloud.suixinbo.presenters.EnterLiveHelper;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;
import com.tencent.qcloud.suixinbo.presenters.LoginHelper;
import com.tencent.qcloud.suixinbo.presenters.ProfileInfoHelper;
import com.tencent.qcloud.suixinbo.presenters.viewinface.EnterQuiteRoomView;
import com.tencent.qcloud.suixinbo.presenters.viewinface.LiveView;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.tencent.qcloud.suixinbo.views.customviews.BaseActivity;
import com.fanwe.base.CallbackView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.fanwe.o2o.miguo.R;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;

import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 点播页面。
 * Created by Administrator on 2016/9/20.
 */
public class PlayBackActivity extends BaseActivity implements ITXLivePlayListener, View.OnClickListener, LiveView, CallbackView, EnterQuiteRoomView {


    private TXLivePlayer mLivePlayer = null;
    private boolean mVideoPlay;
    private TXCloudVideoView mPlayerView;
    private ImageView mLoadingView;
    private boolean mHWDecode = false;
    private View root;
    private Button mBtnLog;
    private ImageView mBtnPlay;
    private Button mBtnRenderRotation;
    private Button mBtnRenderMode;
    private Button mBtnHWDecode;
    private ScrollView mScrollView;
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
    private long mTrackingTouchTS = 0;
    private boolean mStartSeek = false;
    private boolean mVideoPause = false;
    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    private ArrayList<LiveChatEntity> mTmpChatList = new ArrayList<LiveChatEntity>();//缓冲队列
    private TimerTask mTimerTask = null;

    private ArrayList<String> mRenderUserList = new ArrayList<>();


    private UserHeadTopView mUserHeadTopView;

    private PlayBackSeekBarView playBackSeekBarView;
    private PlayBackBottomToolView playBackBottomToolView;

    private PagerBaoBaoAdapter mBaoBaoAdapter;

    private Timer mVideoTimer, mAudienceTimer;

    /**
     * 头部头像adapter.
     */
    private HeadTopAdapter mHeadTopAdapter;
    private HashMap<Integer,PlaySetInfo> playUrlList;
    /**
     * 拉流地址。
     */
    String playUrl = "";
    float width = 0.0f;
    float height = 0.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityParams();
        setContentView(R.layout.act_play_back);
        initHelper();
        initView();
        mCurrentRenderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;

        mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;

        mPlayConfig = new TXLivePlayConfig();
        if (mLivePlayer == null) {
            mLivePlayer = new TXLivePlayer(this);
        }
    }
    public void getPlayUrlList(){
        if(playUrlList ==null){
            playUrlList = new HashMap<>();
          }
        String playSet = "";
        Type listType = new TypeToken<List<PlaySetInfo>>(){}.getType();
        Gson gson = new Gson();
        List<PlaySetInfo> playSetInfoList = gson.fromJson(playSet, listType);
        for (Iterator iterator = playSetInfoList.iterator(); iterator.hasNext();) {
            PlaySetInfo o = (PlaySetInfo) iterator.next();
            playUrlList.put(o.getDefinition(),o);
        }
    }

    public void initView() {
        root = findViewById(R.id.root);

        mPlayerView = (TXCloudVideoView) findViewById(R.id.video_view);
        mLoadingView = (ImageView) findViewById(R.id.loadingImageView);

        //播放进度条。
        playBackSeekBarView = (PlayBackSeekBarView) findViewById(R.id.play_seekbar);
        mBtnPlay = (ImageView) playBackSeekBarView.findViewById(R.id.btnPlay);
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
        mUserHeadTopView.setmAdapter(mHeadTopAdapter);
        mUserHeadTopView.init();
        mUserHeadTopView.setVisibility(View.VISIBLE);
        mUserHeadTopView.initNeed(this);
        mLiveHttphelper.getAudienceCount(CurLiveInfo.getRoomNum() + "", "1");
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
     * 报地址不合法错误。
     */
    public void showInvalidateToast(String message) {
        String defaultMessage = "播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!";
        if (TextUtils.isEmpty(message)) {
            message = defaultMessage;
        }
        SDToast.showToast(message, Toast.LENGTH_SHORT);


    }

    private boolean checkPlayUrl(final String playUrl) {
        if (TextUtils.isEmpty(playUrl) || (!playUrl.startsWith("http://") && !playUrl.startsWith("https://") && !playUrl.startsWith("rtmp://"))) {
            showInvalidateToast("");
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
                return false;
            }
        } else {
            showInvalidateToast("");
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
            showInvalidateToast("非腾讯云链接地址，若要放开限制，请联系腾讯云商务团队");
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
     *  根据宽高比设置当前的方向。
     */
    public void changeOrientation() {
        if (mLivePlayer == null) {
            return;
        }
        //横屏。
        if (width - height > 0) {
            mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_LANDSCAPE;
        } else {
            mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
        }
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
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
            mLiveHttphelper.getAudienceList(CurLiveInfo.getRoomNum() + "");
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
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {

    }

    @Override
    public void onFailue(String responseBody) {

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
}
