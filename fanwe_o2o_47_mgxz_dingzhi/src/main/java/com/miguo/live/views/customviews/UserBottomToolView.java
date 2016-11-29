package com.miguo.live.views.customviews;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.constant.Constant;
import com.fanwe.customview.SharePopHelper;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerDetailInfo;
import com.fanwe.utils.MGStringFormatter;
import com.miguo.live.adapters.PagerBaoBaoAdapter;
import com.miguo.live.adapters.PagerRedPacketAdapter;
import com.miguo.live.interf.OnPayGiftSuccessListener;
import com.miguo.live.model.UserRedPacketInfo;
import com.miguo.live.model.getGiftInfo.GiftListBean;
import com.miguo.live.model.pagermodel.BaoBaoEntity;
import com.miguo.live.views.LiveInputDialogHelper;
import com.miguo.live.views.LiveUserPopHelper;
import com.miguo.live.views.UserRobRedPacketDialogHelper;
import com.miguo.live.views.UserRobRedPacketEndDialogHelper;
import com.miguo.live.views.view.UserSendGiftPopHelper;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.tencent.qcloud.suixinbo.views.customviews.HeartLayout;

import java.util.List;


/**
 * Created by didik on 2016/7/22.
 */
public class UserBottomToolView extends LinearLayout implements IViewGroup, View.OnClickListener {
    /**
     * 红包所在的VIEWPAGER 位置。
     */
    private final int RED_TYPE = 2;
    /**
     * 商品类型。
     */
    private final int GOODS_TYPE = 0;

    private Context mContext;
    private View mTvTalk2host;
    private TextView mGoods;
    private TextView mRob;
    private ImageView mGift;
    private ImageView mShare;
    private ImageView mLike;
    private Activity mAct;
    private LiveHelper mLiveHelper;
    private HeartLayout mHeartLayout;
    private long admireTime = 0;//♥的时间
    private View rootView;//父布局,pop定位用
    private LiveUserPopHelper popHelper;
    private UserRobRedPacketDialogHelper redPacketDialogHelper;
    private CallbackView mCallbackView;

    /**
     * 门店详情。
     */
    private SellerDetailInfo mSellerDetailInfo;

    /**
     * 门店商品列表。
     *
     * @param context
     */
    private List<BaoBaoEntity> baoBaoEntities;

    /**
     * 用户取得的红包列表。
     */
    private PagerRedPacketAdapter mRedPacketAdapter;

    private PagerBaoBaoAdapter mBaobaoAdapter;
    /**
     * 红包结束页。
     */
    private UserRobRedPacketEndDialogHelper userRobRedPacketEndDialogHelper;
    private UserSendGiftPopHelper giftPopHelper;


    public UserBottomToolView(Context context) {
        this(context, null);
    }

    public UserBottomToolView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    OnGiftSendListener onGiftSendListener;

    public UserBottomToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private CommonHttpHelper commonHttpHelper;

    private String shareRecordId;

    public void setShareRecordId(String shareRecordId) {
        this.shareRecordId = shareRecordId;
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.user_bottom_tool, this);
        mTvTalk2host = this.findViewById(R.id.tv_talk2host);
        mGoods = (TextView) this.findViewById(R.id.goods);
        mRob = (TextView) this.findViewById(R.id.rob);
        mGift = (ImageView) this.findViewById(R.id.iv_gift);
        mShare = (ImageView) this.findViewById(R.id.iv_share);
        mLike = (ImageView) this.findViewById(R.id.iv_like);

        mTvTalk2host.setOnClickListener(this);
        mGoods.setOnClickListener(this);
        mRob.setOnClickListener(this);
        mGift.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mLike.setOnClickListener(this);
    }

    public void refreshGift() {
        if (giftPopHelper != null) {
            giftPopHelper.refreshGift();
        }
    }

    @Override
    public void onDestroy() {
        if (redPacketDialogHelper != null) {
            redPacketDialogHelper.dismiss();
        }
    }

    public void initView(Activity mAct, LiveHelper liveHelper, HeartLayout heartLayout, View rootView, CallbackView mCallbackView) {
        this.mAct = mAct;
        this.mLiveHelper = liveHelper;
        this.mHeartLayout = heartLayout;
        this.rootView = rootView;
        this.mCallbackView = mCallbackView;
    }

    @Override
    public void onClick(View v) {
        if (v == mTvTalk2host) {
            talk2host();
        } else if (v == mGoods) {
            clickGoods(GOODS_TYPE);
        } else if (v == mRob) {
            clickGoods(RED_TYPE);
        } else if (v == mGift) {
            clickGift();
        } else if (v == mShare) {
            clickShare();
            getRecordId();
        } else if (v == mLike) {
            clickLike2ShowHeart();
        }
    }

    /**
     * 进入页面后就立刻弹出宝宝
     */
    public void clickBaoBao() {
        mGoods.performClick();
    }

    /**
     * 点击出星星
     */
    private boolean clickLike2ShowHeart() {
        mHeartLayout.addFavor();
        if (checkInterval()) {
            mLiveHelper.sendGroupMessage(Constants.AVIMCMD_Praise, "");
            CurLiveInfo.setAdmires(CurLiveInfo.getAdmires() + 1);
            return true;
        } else {

            return false;
        }
    }

    /**
     * 分享
     */
    private void clickShare() {
        SharePopHelper sharePopHelper = new SharePopHelper(mAct, false, shareRecordId);
        sharePopHelper.show();
    }

    /**
     * 点击礼物
     */
    private void clickGift() {
        giftPopHelper = new UserSendGiftPopHelper(mAct, "1");
//        if (giftPopHelper == null) {
//            giftPopHelper = new UserSendGiftPopHelper(mAct, "1");
//        }else {
//            //每次都刷新呗
//            giftPopHelper.refreshGift();
//        }
        giftPopHelper.show();
        giftPopHelper.setOnPayGiftSuccessListener(new OnPayGiftSuccessListener() {
            @Override
            public void onPaySuc(GiftListBean giftInfo, int num) {
                if (onGiftSendListener != null) {
                    onGiftSendListener.requestSendGift(giftInfo, num);
                }
            }
        });
    }

    /**
     * 点击抢到
     */
    public void clickRob(String red_packet_key, int duration) {
//        if (mAct != null && redPacketDialogHelper == null) {
        if (mAct != null) {
            redPacketDialogHelper = new UserRobRedPacketDialogHelper(mAct, red_packet_key, duration, mCallbackView);
            redPacketDialogHelper.createDialog();
        }
        redPacketDialogHelper.show();
        redPacketDialogHelper.startTimeTask();

    }

    /**
     * 显示红包结果。
     *
     * @param datas
     */
    public void showRedPacketResult(List<UserRedPacketInfo> datas) {
        if (redPacketDialogHelper != null && redPacketDialogHelper.isShowing()) {
            redPacketDialogHelper.endTimeTask();
            redPacketDialogHelper.dismiss();
        }

        boolean isRobed;
        String type = "";
        String num = "";
        if (datas == null || datas.size() < 1) {
            isRobed = false;
        } else {
            UserRedPacketInfo info = datas.get(0);
            if (info != null && !TextUtils.isEmpty(info.getRed_packet_type())) {
                isRobed = true;
                type = info.getRed_packet_type();
                num = info.getRed_packet_amount();
            } else {
                isRobed = false;
            }
        }

        userRobRedPacketEndDialogHelper = new UserRobRedPacketEndDialogHelper(mAct, isRobed);
        //显示抢到界面(两种状态)
        if (isRobed) {
            num = MGStringFormatter.getFloat2(num);
            userRobRedPacketEndDialogHelper.setData(type, num);
        }
        userRobRedPacketEndDialogHelper.show();

    }

    /*dismiss*/
    public void dismissPop() {
        if (popHelper != null) {
            popHelper.dismissLiveUserPop();
        }
    }

    /**
     * 点击了商品(宝贝)
     */
    public void clickGoods(int type) {
        if (mAct != null && rootView != null && popHelper == null) {
            popHelper = new LiveUserPopHelper(mAct, rootView, mCallbackView, mRedPacketAdapter, mBaobaoAdapter, type);
        } else {
            popHelper.setCurrentPosition(type);
        }

        if (mSellerDetailInfo != null) {
            popHelper.setmSellerDetailInfo(mSellerDetailInfo);
        }

        popHelper.show();
    }

    /**
     * 点击了"跟主播聊聊"
     */
    private void talk2host() {
        inputMsgDialog();
    }

    /**
     * 发消息弹出框
     */
    private void inputMsgDialog() {
        if (mAct == null || mLiveHelper == null || mContext == null) {
            return;
        }
        LiveInputDialogHelper inputDialogHelper = new LiveInputDialogHelper(mLiveHelper, mAct);

        inputDialogHelper.show();
    }


    private boolean checkInterval() {
        if (0 == admireTime) {
            admireTime = System.currentTimeMillis();
            return true;
        }
        long newTime = System.currentTimeMillis();
        if (newTime >= admireTime + 1000) {
            admireTime = newTime;
            return true;
        }
        return false;
    }

    public SellerDetailInfo getmSellerDetailInfo() {
        return mSellerDetailInfo;
    }

    public void setmSellerDetailInfo(SellerDetailInfo mSellerDetailInfo) {
        this.mSellerDetailInfo = mSellerDetailInfo;
        if (popHelper != null) {
            popHelper.setmSellerDetailInfo(this.mSellerDetailInfo);
        }
    }

    public void notifyDataChange() {
        if (popHelper != null) {
            popHelper.setmSellerDetailInfo(this.mSellerDetailInfo);
            popHelper.refreshSellerDetailInfo();
        }
    }


    public List<BaoBaoEntity> getBaoBaoEntities() {
        return baoBaoEntities;
    }

    public void setBaoBaoEntities(List<BaoBaoEntity> baoBaoEntities) {
        this.baoBaoEntities = baoBaoEntities;
    }

    public PagerRedPacketAdapter getmRedPacketAdapter() {
        return mRedPacketAdapter;
    }

    public void setmRedPacketAdapter(PagerRedPacketAdapter mRedPacketAdapter) {
        this.mRedPacketAdapter = mRedPacketAdapter;
    }

    public PagerBaoBaoAdapter getmBaobaoAdapter() {
        return mBaobaoAdapter;
    }

    public void setmBaobaoAdapter(PagerBaoBaoAdapter mBaobaoAdapter) {
        this.mBaobaoAdapter = mBaobaoAdapter;
    }

    public interface OnGiftSendListener {
        void requestSendGift(GiftListBean giftInfo, int num);
    }

    public void setOnGiftSendListener(OnGiftSendListener onGiftSendListener) {
        this.onGiftSendListener = onGiftSendListener;
    }

    //    private boolean showBaoBao=false;
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        if (!showBaoBao && !LiveUtil.checkIsHost()) {
//            //弹出宝宝
//            clickBaoBao();
//            showBaoBao = true;
//        }
//    }
    private void getRecordId() {
        if (commonHttpHelper == null) {
            commonHttpHelper = new CommonHttpHelper(mAct, mCallbackView);
        }
        commonHttpHelper.createShareRecord(Constant.ShareType.LIVE, CurLiveInfo.getRoomNum() + "");
    }
}
