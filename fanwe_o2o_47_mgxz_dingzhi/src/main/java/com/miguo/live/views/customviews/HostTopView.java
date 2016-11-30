package com.miguo.live.views.customviews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.constant.Constant;
import com.fanwe.customview.SharePopHelper;
import com.fanwe.o2o.miguo.R;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.live.adapters.HeadTopAdapter;
import com.miguo.live.model.getAudienceList.ModelAudienceInfo;
import com.miguo.live.presenters.LiveCommonHelper;
import com.miguo.live.views.LiveActivity;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.List;

/**
 * Created by didik on 2016/7/30.
 */
public class HostTopView extends RelativeLayout implements IViewGroup, View.OnClickListener {

    private TextView mAV_members_num;//房间人数
    private RecyclerView mRecyclerView;//观众头像
    private ImageView iv_share;//分享功能
    private ImageView iv_photo;//前置后置摄像头切换
    private ImageView iv_close;//关闭按钮
    private TextView mTv_location;//店铺的地址
    private TextView mTv_arrive_num; //多少人来过
    private TextView mTv_keywords;//关键词
    private RelativeLayout mRel_layout;
    private Context mContext;
    private LiveActivity mActivity;//直播的引用
    private LiveCommonHelper mLiveCommonHelper;//工具类
    private HeadTopAdapter mAdapter;
    private CommonHttpHelper commonHttpHelper;

    private String shareRecordId;

    public void setShareRecordId(String shareRecordId) {
        this.shareRecordId = shareRecordId;
    }

    public void setCommonHttpHelper(CommonHttpHelper commonHttpHelper) {
        this.commonHttpHelper = commonHttpHelper;
    }


    public HostTopView(Context context) {
        super(context);
        //init(context);
    }

    public HostTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // init(context);
    }

    public HostTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // init(context);
    }

    public void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.act_live_host_top_view, this);
        mRel_layout = ((RelativeLayout) this.findViewById(R.id.rel_layout));//文字布局界面的容器

        mAV_members_num = ((TextView) this.findViewById(R.id.tv_members_num));
        mRecyclerView = ((RecyclerView) this.findViewById(R.id.recycler_member));
        iv_share = ((ImageView) this.findViewById(R.id.iv_share));
        iv_photo = ((ImageView) this.findViewById(R.id.iv_photo));
        iv_close = ((ImageView) this.findViewById(R.id.iv_close));

        mTv_location = ((TextView) this.findViewById(R.id.tv_location));
        mTv_arrive_num = ((TextView) this.findViewById(R.id.tv_arrive_num));
        mTv_keywords = ((TextView) this.findViewById(R.id.tv_keywords));

        //click
        iv_close.setOnClickListener(this);
        iv_photo.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        mTv_location.setOnClickListener(this);

        //init recycler view
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llmanager = new LinearLayoutManager(mContext);
        llmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(llmanager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setNeed(LiveActivity activity, LiveCommonHelper helper) {
        this.mActivity = activity;
        this.mLiveCommonHelper = helper;
    }

    @Override
    public void onDestroy() {
        mContext = null;
        mActivity = null;
        mLiveCommonHelper = null;
    }

    @Override
    public void onClick(View v) {
        if (v == iv_close) {
            //关闭
            if (mActivity != null) {
                mActivity.showBackDialog();
            }
        } else if (v == iv_photo) {
            //切换摄像头
            if (mLiveCommonHelper != null) {
                mLiveCommonHelper.switchCamera();
            }
        } else if (v == iv_share) {
            getRecordId();
            SharePopHelper sharePopHelper = new SharePopHelper(mActivity, true, shareRecordId);
            sharePopHelper.show();
        } else if (v == mTv_location) {
            //去商家店铺
            String shop_id = CurLiveInfo.shopID;
            gotoShopDetailActivity(shop_id);
        }

    }

    /**
     * 清除弹幕后,可以显示
     */
    public void show() {
        if (mRel_layout != null && mRecyclerView != null && mTv_arrive_num != null) {
            mRel_layout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mTv_arrive_num.setVisibility(View.VISIBLE);
            mAV_members_num.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 清除弹幕,调用可以隐藏不需要的view
     */
    public void hide() {
        if (mRel_layout != null && mRecyclerView != null && mTv_arrive_num != null) {
            mRel_layout.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mTv_arrive_num.setVisibility(View.INVISIBLE);
            mAV_members_num.setVisibility(View.INVISIBLE);
        }
    }

    /*更新人数*/
    public void updateAudienceCount(String num) {
        if (!TextUtils.isEmpty(num)) {
            mAV_members_num.setText(num + "人");
        } else {
            mAV_members_num.setText("0 人");
        }
    }

    public void gotoShopDetailActivity(String shop_id) {
        Intent itemintent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(HiShopDetailActivity.EXTRA_MERCHANT_ID, shop_id);
        bundle.putInt("type", 0);
        itemintent.putExtras(bundle);
        itemintent.setClass(App.getApplication(), HiShopDetailActivity.class);
        if (mActivity != null) {
            mActivity.startActivity(itemintent);
        }
    }

    /**
     * 更新数据。
     *
     * @param mData
     */
    public void refreshData(List<ModelAudienceInfo> mData) {
        mAdapter.setmData(mData);
    }

    /*设置地址*/
    public void setLocation(String location) {
        mTv_location.setText(location);
    }

    /*设置关键字,格式还未定义*/
    public void setKeyWords(String str) {
        mTv_keywords.setText(str);
    }

    public void setArriveNum(String str) {
        mTv_arrive_num.setText(str);
    }

    public HeadTopAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(HeadTopAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    private void getRecordId() {
        if (commonHttpHelper != null) {
            commonHttpHelper.createShareRecord(Constant.ShareType.LIVE,  CurLiveInfo.getRoomNum() + "");
        }
    }
}
