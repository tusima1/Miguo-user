package com.miguo.live.views.customviews;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.presenters.LiveCommonHelper;
import com.miguo.utils.DisplayUtil;
import com.miguo.utils.MGLog;

/**
 * Created by didik on 2016/7/30.
 *
 * 主播的美化功能
 */
public class HostMeiToolView extends RelativeLayout implements IViewGroup, View.OnClickListener {

    private Context mContext;
    private ImageView mIv_lighting;
    private ImageView mIv_meiyan;
    private ImageView mIv_meibai;
    private ImageView mIv_mei;
    private boolean isShow = true;
    private LiveCommonHelper mLiveCommonHelper;//工具类
    private ObjectAnimator object1;
    private ObjectAnimator object2;
    private ObjectAnimator object3;

    private float dx=0f;

    public HostMeiToolView(Context context) {
        super(context);
        init(context);
    }

    public HostMeiToolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HostMeiToolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.act_live_host_mei_tool_view, this);
        mIv_lighting = ((ImageView) this.findViewById(R.id.iv_lighting));
        mIv_meiyan = ((ImageView) this.findViewById(R.id.iv_meiyan));
        mIv_meibai = ((ImageView) this.findViewById(R.id.iv_meibai));
        mIv_mei = ((ImageView) this.findViewById(R.id.iv_mei));

        mIv_lighting.setOnClickListener(this);
        mIv_mei.setOnClickListener(this);
        mIv_meiyan.setOnClickListener(this);
        mIv_meibai.setOnClickListener(this);

        dx=DisplayUtil.dp2px(mContext, 60f);
        initExitAnimation(mIv_lighting,mIv_meiyan,mIv_meibai);

    }


    public void setNeed(LiveCommonHelper helper) {
        this.mLiveCommonHelper = helper;

    }

    @Override
    public void onDestroy() {

    }

    /**
     * 是否是隐藏状态
     * @return true 显示
     *          false 隐藏
     */
    public boolean isShow(){
        int visibility = mIv_lighting.getVisibility();
        if (visibility==View.INVISIBLE){
            return false;
        }else if (visibility==View.VISIBLE){
            return true;
        }
        return false;
    }

    /**
     * 隐藏按钮
     */
    public void hide() {
        if (mIv_lighting != null && mIv_meibai != null && mIv_meiyan != null) {
            mIv_lighting.setVisibility(View.INVISIBLE);
            mIv_meibai.setVisibility(View.INVISIBLE);
            mIv_meiyan.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 显示按钮
     */
    public void show() {
        if (mIv_lighting != null && mIv_meibai != null && mIv_meiyan != null) {
            mIv_lighting.setVisibility(View.VISIBLE);
            mIv_meibai.setVisibility(View.VISIBLE);
            mIv_meiyan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mIv_meibai) {
            clickMeiBai();
        } else if (v == mIv_meiyan) {
            clickMeiYan();
        } else if (v == mIv_mei) {
            clickMei();
        } else if (v == mIv_lighting) {
            clickLighting();
        }
    }

    private boolean isFighting = false;
    /*开关闪关灯*/
    private void clickLighting() {
        if (mLiveCommonHelper != null) {
            boolean result = mLiveCommonHelper.openLighting();
            if (result) {
                if (isFighting) {
                    isFighting = false;
                    mIv_lighting.setBackgroundResource(R.drawable.ic_lighting_off);
                } else {
                    isFighting = true;
                    mIv_lighting.setBackgroundResource(R.drawable.ic_lighting_on);
                }

            }

        }
    }

    private void clickMei() {
//        if (isShow) {
//            hide();
//        } else {
//            show();
//        }
//        isShow = !isShow;
        object1.start();
        object2.start();
        object3.start();
    }

    /**
     * 美颜
     */
    private void clickMeiYan() {
        MGToast.showToast("美颜");
    }

    /**
     * 美白
     */
    private void clickMeiBai() {
        MGToast.showToast("美白");
    }


    //-------------- animation---------------

    private void initStartAnimation(View view1,View view2,View view3) {
        show();
        float translationX = view1.getTranslationX();
        int i = DisplayUtil.dp2px(mContext, 60f);
        MGLog.e("initStartAnimation:"+translationX);
        MGLog.e("dxAnimation:"+dx);
        object1 = ObjectAnimator.ofFloat(view1,"translationX",dx,0);
        object1.setDuration(1000);
        object1.setInterpolator(new AnticipateOvershootInterpolator(1.0f));

        object2 = ObjectAnimator.ofFloat(view2,"translationX",dx,0);
        object2.setDuration(1000);
        object2.setInterpolator(new AnticipateOvershootInterpolator(1.0f));

        object3 = ObjectAnimator.ofFloat(view3,"translationX",dx,0);
        object3.setDuration(1000);
        object3.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        object3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                initExitAnimation(mIv_lighting,mIv_meiyan,mIv_meibai);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void initExitAnimation(View view1,View view2,View view3) {
        float translationX = view1.getTranslationX();
        MGLog.e("initExitAnimation"+translationX);
        object1 = ObjectAnimator.ofFloat(view1,"translationX",0,dx);
        object1.setDuration(1000);
        object1.setInterpolator(new AnticipateInterpolator(1.0f));

        object2 = ObjectAnimator.ofFloat(view2,"translationX",0,dx);
        object2.setDuration(1000);
        object2.setInterpolator(new AnticipateInterpolator(1.0f));

        object3 = ObjectAnimator.ofFloat(view3,"translationX",0,dx);
        object3.setDuration(1000);
        object3.setInterpolator(new AnticipateInterpolator(1.0f));
        object3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hide();
                initStartAnimation(mIv_lighting,mIv_meiyan,mIv_meibai);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }




}
