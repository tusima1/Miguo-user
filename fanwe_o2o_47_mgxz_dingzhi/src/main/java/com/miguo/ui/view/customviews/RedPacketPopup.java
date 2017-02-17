package com.miguo.ui.view.customviews;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.miguo.utils.Rotate3dAnimation;

/**
 * Created by didik 
 * Created time 2017/2/13
 * Description: 
 */

public class RedPacketPopup extends BasePopupWindow implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvName;
    private TextView tvSubTitle;
    private ImageView ivClose;
    private ImageView ivOpen;
    private ImageView ivFace;

    public RedPacketPopup(Activity mHoldActivity, View mAnchor) {
        super(mHoldActivity, mAnchor);
    }

    @Override
    protected View setPopupWindowContentView() {
        View contentView = LayoutInflater.from(mHoldActivity).inflate(R.layout
                .layout_pop_redpacket_open, null, false);
        tvTitle = ((TextView) contentView.findViewById(R.id.tv_title));
        tvName = ((TextView) contentView.findViewById(R.id.tv_name));
        tvSubTitle = ((TextView) contentView.findViewById(R.id.tv_sub_title));

        ivClose = ((ImageView) contentView.findViewById(R.id.iv_close));
        ivOpen = ((ImageView) contentView.findViewById(R.id.iv_open));
        ivFace = ((ImageView) contentView.findViewById(R.id.iv_face));

        ivClose.setOnClickListener(this);
        ivOpen.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        if (v== ivClose){
            dismiss();
            return;
        }
        if (v==ivOpen){
            playAnimation2();
            return;
        }
    }

    private void playAnimation() {
        float centerX= ivOpen.getWidth()/2;
        float centerY= ivOpen.getHeight()/2;
        Rotate3dAnimation rotate3dAnimation =new Rotate3dAnimation(0,360,centerX,centerY,0,true);
        rotate3dAnimation.setDuration(1200);
        rotate3dAnimation.setRepeatMode(Animation.RESTART);
        rotate3dAnimation.setRepeatCount(Integer.MAX_VALUE);
//        rotate3dAnimation.setInterpolator(new DecelerateInterpolator());
//        rotate3dAnimation.setFillAfter(true);
        ivOpen.startAnimation(rotate3dAnimation);
    }

    private void playAnimation2(){
        int defaultRepeat = 1;
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(ivOpen,"RotationY",0, - defaultRepeat * 360);
        rotationAnimator.setDuration(600 * defaultRepeat);
        rotationAnimator.setRepeatCount(Integer.MAX_VALUE);
        rotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.start();
    }
}
