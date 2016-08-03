package com.miguo.live.views.customviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.HeadTopAdapter;
import com.miguo.live.presenters.LiveCommonHelper;
import com.miguo.live.views.LiveActivity;

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

    public HostTopView(Context context) {
        super(context);
        init(context);
    }

    public HostTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HostTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext=context;
        LayoutInflater.from(mContext).inflate(R.layout.act_live_host_top_view,this);
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

        //init recycler view
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llmanager=new LinearLayoutManager(mContext);
        llmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(llmanager);

        HeadTopAdapter mAdapter=new HeadTopAdapter(null,mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setNeed(LiveActivity activity, LiveCommonHelper helper){
        this.mActivity=activity;
        this.mLiveCommonHelper=helper;
    }

    @Override
    public void onDestroy() {
        mContext=null;
        mActivity=null;
        mLiveCommonHelper=null;
    }

    @Override
    public void onClick(View v) {
        if (v==iv_close){
            //关闭
            if (mActivity!=null){
                mActivity.onBackPressed();
            }
        }else if(v==iv_photo){
            //切换摄像头
            if (mLiveCommonHelper!=null){
                mLiveCommonHelper.switchCamera();
            }
        }else if(v==iv_share){
            //分享
//            UmengShareManager.share();
            MGToast.showToast("分享");
        }
    }

    /**
     * 清除弹幕后,可以显示
     */
    public void show(){
        if (mRel_layout !=null && mRecyclerView !=null && mTv_arrive_num !=null){
            mRel_layout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mTv_arrive_num.setVisibility(View.VISIBLE);
        }else {
            MGToast.showToast("初始化view错误!");
        }
    }

    /**
     * 清除弹幕,调用可以隐藏不需要的view
     */
    public void hide(){
        if (mRel_layout !=null && mRecyclerView !=null && mTv_arrive_num !=null){
            mRel_layout.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mTv_arrive_num.setVisibility(View.INVISIBLE);
        }else {
            MGToast.showToast("初始化view错误!");
        }
    }
    /*更新人数*/
    public void updateAudienceCount(String num){
        if(!TextUtils.isEmpty(num)) {
            mAV_members_num.setText(num + "人");
        }
    }
    /*设置地址*/
    public void setLocation(String location){
        mTv_location.setText(location);
    }
    /*设置关键字,格式还未定义*/
    public void setKeyWords(){
        //TODO keywords
    }
}
