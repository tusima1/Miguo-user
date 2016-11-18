package com.miguo.view.video;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.miguo.live.views.customviews.PlayBackSeekBarView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Created by Administrator on 2016/11/16.
 */

public class VideoPlayerView extends FrameLayout implements ITXLivePlayListener {
    private Context mContext;
    /////////////////////////////////播放相关///////////////////////////////
    private TXLivePlayer mLivePlayer = null;
    private TXLivePlayConfig mPlayConfig;
    private int mCurrentRenderRotation;
    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    //拉流地址
    String playUrl = "";
    private boolean mVideoPlay = false;
    private boolean mVideoPause = false;
    private boolean mStartSeek = false;
    private long mTrackingTouchTS = 0;
    //////////////////////////////////控件相关///////////////////////////////////
    //播放界面
    private RelativeLayout layoutPlayer;
    private TXCloudVideoView mPlayerView;
    private PlayBackSeekBarView playBackSeekBarView;
    private ImageView mLoadingView;
    private ImageView mBtnPlay;
    private SeekBar mSeekBar;
    private TextView mTextStart;
    private TextView mTextDuration;
    //封面界面
    private RelativeLayout layoutCover;
    private ImageView ivCover;
    private TextView tvTitle;
    /////////////////////////////////其他参数///////////////////////////////
    private IPlayEvent iPlayEvent;

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        // 将自定义组合控件的布局渲染成View
        View view = View.inflate(context, R.layout.view_video_player, this);
        layoutPlayer = (RelativeLayout) view.findViewById(R.id.layout_player);
        mPlayerView = (TXCloudVideoView) view.findViewById(R.id.view_video);
        playBackSeekBarView = (PlayBackSeekBarView) view.findViewById(R.id.seekbar_play);
        mLoadingView = (ImageView) view.findViewById(R.id.iv_loading);
        //播放、暂停播放
        mBtnPlay = (ImageView) playBackSeekBarView.findViewById(R.id.btnPlay);
        mSeekBar = (SeekBar) playBackSeekBarView.findViewById(R.id.seekbar);
        mTextStart = (TextView) playBackSeekBarView.findViewById(R.id.play_start);
        mTextDuration = (TextView) playBackSeekBarView.findViewById(R.id.duration);
        //封面
        layoutCover = (RelativeLayout) view.findViewById(R.id.layout_cover);
        ivCover = (ImageView) view.findViewById(R.id.iv_cover_view_video_player);
        tvTitle = (TextView) view.findViewById(R.id.tv_title_view_video_player);

        setListener();
    }

    public void setIPlayEvent(IPlayEvent iPlayEvent) {
        this.iPlayEvent = iPlayEvent;
    }

    private void setListener() {
        layoutCover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutPlayer.setVisibility(VISIBLE);
                layoutCover.setVisibility(GONE);
                if (startPlayRtmp()) {
                    mVideoPlay = true;
                }
            }
        });
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
                            if (iPlayEvent != null) {
                                iPlayEvent.onAvChange(iPlayEvent.PAUSE, null);
                            }
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
        initSeekBar();
    }

    private ModelVideoPlayer model = new ModelVideoPlayer();

    public void setData(ModelVideoPlayer model) {
        this.model = model;
        if (mPlayConfig == null) {
            mPlayConfig = new TXLivePlayConfig();
        }
        if (mLivePlayer == null) {
            mLivePlayer = new TXLivePlayer(mContext);
        }
        getPlayUrlList();

        ImageLoader.getInstance().displayImage(model.getCoverUrl(), ivCover, null, null, null);
        SDViewBinder.setTextView(tvTitle, model.getTitle(), "");
    }

    private void initSeekBar() {
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
        mLivePlayer.enableHardwareDecode(false);
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
        mLivePlayer.setRenderMode(0);
        //设置播放器缓存策略
        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
        //mLivePlayer.setCacheTime(5);
        mLivePlayer.setConfig(mPlayConfig);
        int result = mLivePlayer.startPlay(playUrl, mPlayType); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
        if (result == -2) {
            showInvalidateToast("链接地址有误");
        }
        if (result != 0) {
            mBtnPlay.setBackgroundResource(R.drawable.play_start);
            return false;
        }
        mLivePlayer.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
        startLoadingAnimation();
        return true;
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
     * 解析直播流地址。
     */
    private void getPlayUrlList() {
        playUrl = model.getPlayUrl();
        if (!TextUtils.isEmpty(playUrl)) {
            changeOrientation();
        } else {
            playError();
        }
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

    /**
     * 根据宽高比设置当前的方向。
     */
    private void changeOrientation() {
        if (mLivePlayer == null || model == null) {
            return;
        }
        float width = DataFormat.toFloat(model.getW());
        float height = DataFormat.toFloat(model.getH());
        //横屏。
        if (width - height > 0) {
            mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_LANDSCAPE;
        } else {
            mCurrentRenderRotation = TXLiveConstants.RENDER_ROTATION_PORTRAIT;
        }
        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
    }

    private void playError() {
        return;
    }


    /**
     * 报地址不合法错误。
     */
    private void showInvalidateToast(String message) {
        String defaultMessage = "播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!";
        if (TextUtils.isEmpty(message)) {
            message = defaultMessage;
        }
        SDToast.showToast(message, Toast.LENGTH_LONG);
        playDone();
    }


    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            stopLoadingAnimation();
            if (iPlayEvent != null) {
                iPlayEvent.onAvChange(iPlayEvent.START, null);
            }
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            //播放过程中的回调
            if (mStartSeek) {
                return;
            }
            int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
            int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
            long curTS = System.currentTimeMillis();
            if (iPlayEvent != null) {
                iPlayEvent.onAvChange(iPlayEvent.PLAYING, progress);
            }
            // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
            if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                return;
            }
            mTrackingTouchTS = curTS;
            if (mSeekBar != null) {
                mSeekBar.setProgress(progress);
            }
            if (progress >= duration) {
                //时间轴异常，结束播放
                playDone();
                return;
            }
            //设置播放时间，需要把已经播放完成的文件的时间加上来
            if (mTextStart != null) {
                mTextStart.setText(String.format("%02d:%02d", progress / 60, progress % 60));
            }
            //设置总时长，duration
            if (mTextDuration != null) {
                mTextDuration.setText(String.format("%02d:%02d", duration / 60, duration % 60));
            }
            //设置最大时长，duration
            if (mSeekBar != null) {
                mSeekBar.setMax(duration);
            }
            return;
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
            //连接中断
            playDone();
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            //播放完毕
            playDone();
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
     * 播放完毕
     */
    private void playDone() {
        getPlayUrlList();
        stopPlayRtmp();
        mVideoPlay = false;
        mVideoPause = false;
        if (mTextStart != null) {
            mTextStart.setText("00:00");
        }
        if (mSeekBar != null) {
            mSeekBar.setProgress(0);
        }
        //显示封面
        layoutPlayer.setVisibility(GONE);
        layoutCover.setVisibility(VISIBLE);
        if (iPlayEvent != null) {
            iPlayEvent.onAvChange(iPlayEvent.FINISH, null);
        }
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        playDone();
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
