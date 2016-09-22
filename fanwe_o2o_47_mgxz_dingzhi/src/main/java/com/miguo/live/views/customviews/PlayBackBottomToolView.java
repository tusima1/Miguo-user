package com.miguo.live.views.customviews;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.customview.SharePopHelper;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.SellerDetailInfo;
import com.miguo.live.adapters.PagerBaoBaoAdapter;
import com.miguo.live.adapters.PagerRedPacketAdapter;
import com.miguo.live.model.pagermodel.BaoBaoEntity;
import com.miguo.live.views.LiveInputDialogHelper;
import com.miguo.live.views.LiveUserPopHelper;
import com.tencent.qcloud.suixinbo.presenters.LiveHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class PlayBackBottomToolView extends LinearLayout implements IViewGroup, View.OnClickListener {

    /**
     * 商品类型。
     */
    private final int GOODS_TYPE = 0;

    private Context mContext;
    private View mTvTalk2host;
    private TextView mGoods;


    private ImageView mShare;

    private Activity mAct;
    private LiveHelper mLiveHelper;

    private long admireTime = 0;//♥的时间
    private View rootView;//父布局,pop定位用
    private LiveUserPopHelper popHelper;
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




    public PlayBackBottomToolView(Context context) {
        this(context, null);
    }

    public PlayBackBottomToolView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public PlayBackBottomToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.playback_bottom_tool, this);
        mTvTalk2host = this.findViewById(R.id.tv_talk2host);
        mGoods = (TextView) this.findViewById(R.id.goods);
        mShare = (ImageView) this.findViewById(R.id.iv_share);

        mTvTalk2host.setOnClickListener(this);
        mGoods.setOnClickListener(this);

        mShare.setOnClickListener(this);

    }

    @Override
    public void onDestroy() {

    }

    public void initView(Activity mAct, LiveHelper liveHelper, View rootView, CallbackView mCallbackView) {
        this.mAct = mAct;
        this.mLiveHelper = liveHelper;
        this.rootView = rootView;
        this.mCallbackView = mCallbackView;
    }

    @Override
    public void onClick(View v) {
        if (v == mTvTalk2host) {
            talk2host();
        } else if (v == mGoods) {
            clickGoods(GOODS_TYPE);
        } else if (v == mShare) {
            clickShare();
        } else {
            return;
        }
    }


    /**
     * 分享
     */
    private void clickShare() {
        SharePopHelper sharePopHelper = new SharePopHelper(mAct);
        sharePopHelper.show();
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
        LiveInputDialogHelper inputDialogHelper = new LiveInputDialogHelper(mLiveHelper, mAct,true);
        inputDialogHelper.dismissDanmu();

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


}

