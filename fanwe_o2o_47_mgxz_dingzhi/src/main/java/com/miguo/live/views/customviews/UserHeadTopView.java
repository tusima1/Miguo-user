package com.miguo.live.views.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.StoreDetailActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.HeadTopAdapter;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.checkFocus.ModelCheckFocus;
import com.miguo.live.model.getAudienceList.ModelAudienceInfo;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.views.LiveUserExitDialogHelper;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by didik on 2016/7/22.
 * 显示的是主播的数据
 */
public class UserHeadTopView extends RelativeLayout implements View.OnClickListener, IViewGroup, CallbackView {
    private Context mContext;
    private CircleImageView mUserIamge;//头像
    private TextView mMembers;//头像下方人数量
    private TextView mUserName;//主播名
    private TextView mFollow;//关注
    private ImageView mClose;//关闭
    private TextView mUserLocation;//地理位置
    private TextView mKeywords;//关键词
    private RecyclerView mMemberList;//room的观众头像列表(取前N位展示)
    private Activity mActivity;
    private LiveUserExitDialogHelper userExitDialogHelper;
    private LinearLayout location_ic_location;

    public boolean isUserClose = false;//是不是用户点击的关闭
    private List<ModelAudienceInfo> mData;

    private HeadTopAdapter mAdapter;
    private LiveHttpHelper liveHttpHelper;

    public UserHeadTopView(Context context) {
        this(context, null);
    }

    public UserHeadTopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserHeadTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

    }

    public void init() {
        liveHttpHelper = new LiveHttpHelper(mActivity, this);
        liveHttpHelper.checkFocus(CurLiveInfo.getHostID());

        LayoutInflater.from(mContext).inflate(R.layout.head_top_layout, this);
        mUserIamge = ((CircleImageView) findViewById(R.id.civ_user_image));
        mMembers = ((TextView) findViewById(R.id.tv_members));
        mUserName = ((TextView) findViewById(R.id.tv_username));
        mFollow = ((TextView) findViewById(R.id.tv_follow));
        mUserLocation = ((TextView) findViewById(R.id.tv_user_location));
        mKeywords = ((TextView) findViewById(R.id.tv_keywords));
        mClose = ((ImageView) findViewById(R.id.iv_close));
        mMemberList = ((RecyclerView) findViewById(R.id.member_image_list));
        location_ic_location = (LinearLayout)findViewById(R.id.location_ic_location);

        final String shop_id = CurLiveInfo.shopID;
        if(!TextUtils.isEmpty(shop_id)){
            location_ic_location.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoShopDetailActivity(shop_id);
                }
            });
        }
        //onclick
        mFollow.setOnClickListener(this);
        mUserIamge.setOnClickListener(this);
        mClose.setOnClickListener(this);

        //绑定假数据
        bindData();
    }

    public void initNeed(Activity activity) {
        this.mActivity = activity;
        userExitDialogHelper = new LiveUserExitDialogHelper(mActivity);
    }


    @Override
    public void onClick(View v) {
        if (v == mFollow) {
            follow();
        } else if (v == mUserIamge) {
            userInfo();
        } else if (v == mClose) {
            close();
        }
    }

    /**
     * 关闭操作
     */
    private void close() {
        //用户主动退出，不需要弹窗
//        if (mActivity != null && userExitDialogHelper != null && !userExitDialogHelper.isShowing()) {
//            userExitDialogHelper.show();
//        }
        if (mActivity != null)
            mActivity.finish();
        isUserClose = true;
    }

    public void refreshData(List<ModelAudienceInfo> mData) {
        if (this.mData != mData) {
            this.mData = mData;
            mAdapter.setmData(mData);
        }
    }

    /**
     * 绑定数据(会刷新所有的数据)
     */
    public void bindData() {

        /*inflate list*/
        mMemberList.setHasFixedSize(true);

        LinearLayoutManager llmanager = new LinearLayoutManager(mContext);
        llmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMemberList.setLayoutManager(llmanager);

        //设置间距
        mMemberList.addItemDecoration(new SpaceItemDecoration(2));
        mMemberList.setHasFixedSize(true);
        mMemberList.setAdapter(mAdapter);
    }

    /**
     * 更新观众列表
     *
     * @return
     */
    public boolean updateMemberList() {
        return true;
    }


    /**
     * 查看主播信息
     */
    private void userInfo() {
        MGToast.showToast("查看主播信息");
    }

    /**
     * 关注主播
     */
    private void follow() {
        liveHttpHelper.userFocus(CurLiveInfo.getHostID());
    }


    @Override
    public void onDestroy() {
        //TODO 释放资源
    }

    /*更新人数*/
    public void updateAudienceCount(String num) {
        if (!TextUtils.isEmpty(num)) {
            mMembers.setText(num + "人");
        } else {
            mMembers.setText("0 人");
        }
    }

    /*设置头像*/
    public void setHostImg(String imgurl) {
        if (TextUtils.isEmpty(imgurl)) {
            mUserIamge.setImageResource(R.drawable.bg_empty);
        } else {
            SDViewBinder.setImageView(imgurl, mUserIamge);
        }
    }

    /*设置名称*/
    public void setHostName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mUserName.setText(name);
        } else {
            mUserName.setText("主播");
        }
    }

    /*设置地理位置*/
    public void setLocation(String location) {
        if (!TextUtils.isEmpty(location)) {
            mUserLocation.setText(location);
        }
    }

    /*设置关键词*/
    public void setKeyWords() {
//        if (){}
        mKeywords.setText("富国  明主  哈哈  大小  乌拉拉");
    }

    public boolean isExitDialogShowing() {
        if (userExitDialogHelper != null) {
            return userExitDialogHelper.isShowing();
        }
        return true;
    }


    public void showExitDialog() {
        if (userExitDialogHelper != null) {
            userExitDialogHelper.show();
        }
    }

    public HeadTopAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(HeadTopAdapter mAdapter) {
        this.mAdapter = mAdapter;

    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        Message message = new Message();
        if (LiveConstants.CHECK_FOCUS.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                ModelCheckFocus modelCheckFocus = (ModelCheckFocus) datas.get(0);
                if ("1".equals(modelCheckFocus.getFocus())) {
                    //已关注
                    message.what = 1;
                } else {
                    //去关注
                    message.what = 0;
                }
            }
        } else if (LiveConstants.USER_FOCUS.equals(method)) {
            message.what = 1;
        }
        mHandler.sendMessage(message);
    }

    @Override
    public void onFailue(String responseBody) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //关注主播
                    mFollow.setVisibility(VISIBLE);
                    break;
                case 1:
                    //已关注
                    mFollow.setVisibility(GONE);
                    break;
            }
        }
    };

    public void gotoShopDetailActivity(String shop_id){
        Intent itemintent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(StoreDetailActivity.EXTRA_MERCHANT_ID, shop_id);
        bundle.putInt("type",0);
        itemintent.putExtras(bundle);
        itemintent.setClass(App.getApplication(), StoreDetailActivity.class);
        if(mActivity!=null) {
            mActivity.startActivity(itemintent);
        }
    }
}
