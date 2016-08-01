package com.miguo.live.views.customviews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.adapters.HeadTopAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by didik on 2016/7/22.
 */
public class UserHeadTopView extends RelativeLayout implements View.OnClickListener,IViewGroup {
    private Context mContext;
    private CircleImageView mUserIamge;//用户头像
    private TextView mMembers;//头像下方人数量
    private TextView mUserName;//用户名
    private TextView mFollow;//关注
    private ImageView mClose;//关闭
    private TextView mUserLocation;//用户的地理位置
    private TextView mKeywords;//关键词
    private RecyclerView mMemberList;//room的观众头像列表(取前N位展示)

    public UserHeadTopView(Context context) {
        this(context,null);
    }

    public UserHeadTopView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UserHeadTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.head_top_layout,this);
        mUserIamge = ((CircleImageView) findViewById(R.id.civ_user_image));
        mMembers = ((TextView) findViewById(R.id.tv_members));
        mUserName = ((TextView) findViewById(R.id.tv_username));
        mFollow = ((TextView) findViewById(R.id.tv_follow));
        mUserLocation = ((TextView) findViewById(R.id.tv_user_location));
        mKeywords = ((TextView) findViewById(R.id.tv_keywords));
        mClose = ((ImageView) findViewById(R.id.iv_close));
        mMemberList = ((RecyclerView) findViewById(R.id.member_image_list));

        //onclick
        mFollow.setOnClickListener(this);
        mUserIamge.setOnClickListener(this);
        mClose.setOnClickListener(this);

        //绑定假数据
        bindData();
    }


    @Override
    public void onClick(View v) {
        if (v==mFollow){
            follow();
        }else if(v==mUserIamge){
            userInfo();
        }else if(v==mClose){
            close();
        }
    }

    /**
     * 关闭操作
     */
    private void close() {
        MGToast.showToast("关闭");
    }

    /**
     * 绑定数据(会刷新所有的数据)
     */
    public void bindData(){

        /*inflate list*/
        mMemberList.setHasFixedSize(true);

        LinearLayoutManager llmanager=new LinearLayoutManager(mContext);
        llmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMemberList.setLayoutManager(llmanager);

        HeadTopAdapter mAdapter=new HeadTopAdapter(null,mContext);
        mMemberList.setAdapter(mAdapter);
    }

    /**
     * 更新观众列表
     * @return
     */
    public boolean updateMemberList(){
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
        MGToast.showToast("关注");
    }


    @Override
    public void onDestroy() {
        //TODO 释放资源
    }

    /*更新人数*/
    public void updateNum(String num){
        mMembers.setText(num+"人");
    }
}
