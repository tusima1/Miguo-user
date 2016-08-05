package com.miguo.live.views.customviews;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.interf.LiveSwitchScreenListener;
import com.miguo.live.presenters.LiveCommonHelper;
import com.miguo.live.views.LiveInputDialogHelper;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;

/**
 * Created by didik on 2016/7/29.
 * 主播的底部工具栏
 */
public class HostBottomToolView extends LinearLayout implements IViewGroup, View.OnClickListener {

    private Context mContext;
    private ImageView mIv_Msg;//消息按钮
    private ImageView mIv_redpacket;//红包按钮(数字)
    private ImageView mIv_shopCart;//购物袋子(数字)
    private CheckBox mCb_Voice;//checkbox 默认为false,false时开启,true为关闭,下同
    private CheckBox mCb_DanMu;//checkbox 弹幕状态

    private int mRedNum=0;//红包按钮(数字)
    private int mShopCartNum=0;////购物袋子(数字)
    private BadgeView redPacketDot;//红包的小红点
    private BadgeView redShopCartDot;//购物袋的小红点
    private LiveCommonHelper mLiveCommonHelper;
    private LiveHelper mLiveHelper;
    private Activity mActivity;
    private LiveSwitchScreenListener mScreenListener;

    public HostBottomToolView(Context context) {
        super(context);
        init(context);
    }

    public HostBottomToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HostBottomToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setNeed(LiveCommonHelper helper, LiveHelper liveHelper, Activity activity){
        this.mLiveCommonHelper=helper;
        this.mLiveHelper=liveHelper;
        this.mActivity=activity;
    }

    public void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.act_live_host_bottom_tool_view, this);

        mIv_Msg = ((ImageView) this.findViewById(R.id.iv_msg));
        mIv_redpacket = ((ImageView) this.findViewById(R.id.iv_red_packet));
        mIv_shopCart = ((ImageView) this.findViewById(R.id.iv_shop_cart));

        mCb_Voice = ((CheckBox) this.findViewById(R.id.cb_voice));
        mCb_DanMu = ((CheckBox) this.findViewById(R.id.cb_danmu));

        mIv_Msg.setOnClickListener(this);
        mIv_redpacket.setOnClickListener(this);
        mIv_shopCart.setOnClickListener(this);

        //checkbox
        //弹幕
        mCb_DanMu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    //执行清除弹幕(类似清屏)
////                    MGToast.showToast("执行清除弹幕");
//
//                }else {
//                    //执行开启弹幕
////                    MGToast.showToast("执行开启弹幕");
//                }
                if (mScreenListener!=null){
                    mScreenListener.onSwitchScreen();
                }
            }
        });

        //语音是否关闭
        mCb_Voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //执行关闭语音
//                    MGToast.showToast("执行关闭语音");
                    if (mLiveCommonHelper!=null){
                        mLiveCommonHelper.closeMic();
                    }
                }else {
                    //执行开启语音
//                    MGToast.showToast("执行开启语音");
                    if (mLiveCommonHelper!=null){
                        mLiveCommonHelper.openMic();
                    }
                }
            }
        });

        //test
        setRedPacketNum(99);
        setShopCartNum(0);
    }

    /**
     * 发消息弹出框
     */
    private void inputMsgDialog() {
        if (mActivity == null || mLiveHelper == null || mContext == null) {
            return;
        }
        LiveInputDialogHelper inputDialogHelper=new LiveInputDialogHelper(mLiveHelper,mActivity);
        inputDialogHelper.show();
    }

    public void setLiveSwitchScreenListener(LiveSwitchScreenListener liveSwitchScreenListener){
        this.mScreenListener=liveSwitchScreenListener;
    }
    @Override
    public void onDestroy() {
        //释放资源
    }

    @Override
    public void onClick(View v) {
        if (v == mIv_Msg) {
            clickMsg();
        } else if (v == mIv_redpacket) {
            clickRedpacket();
        } else if (v == mIv_shopCart) {
            clickShopCart();
        }
    }

    /**
     * 点击那个长的像购物车的袋子
     */
    private void clickShopCart() {
        MGToast.showToast("点击那个长的像购物车的袋子");
    }

    /**
     * 点击红包
     */
    private void clickRedpacket() {
        MGToast.showToast("点击红包");
    }

    /**
     * 点击消息
     */
    private void clickMsg() {
        MGToast.showToast("点击消息");
        inputMsgDialog();
    }

    /**
     * 设置红包的数量
     * @param num 数量为0不显示
     */
    public void setRedPacketNum(int num){
        if (redPacketDot==null){
            redPacketDot = new BadgeView(mContext,mIv_redpacket);
        }
        redPacketDot.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        redPacketDot.setTextSize(9);
        if (num==0){
            redPacketDot.hide();
            return;
        }

        if (num>=99){
            redPacketDot.setText("99+");
        }else {
            redPacketDot.setText(""+num);
        }
        redPacketDot.show();
    }

    /**
     * 设置红包的数量
     * @param num 0不显示
     */
    public void setShopCartNum(int num){
        if (redShopCartDot==null){
            redShopCartDot = new BadgeView(mContext,mIv_shopCart);
        }
        redShopCartDot.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        redShopCartDot.setTextSize(9);
        if (num==0){
            redShopCartDot.hide();
            return;
        }

        if (num>=99){
            redShopCartDot.setText("99+");
        }else {
            redShopCartDot.setText(""+num);
        }
        redShopCartDot.show();
    }
}
