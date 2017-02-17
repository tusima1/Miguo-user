package com.miguo.ui.view.customviews;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fanwe.o2o.miguo.R;
import com.miguo.entity.OnlinePayOrderPaymentBean;
import com.miguo.ui.RedPacketOpenResultActivity;
import com.miguo.utils.MGUIUtil;
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
    private OnlinePayOrderPaymentBean.Result.Body.Share share;
    private String money;

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
            startRedPacketOpenResultActivity();
            return;
        }
    }

    private void startRedPacketOpenResultActivity() {
        int delay = (int) (Math.random() * 1500);
        MGUIUtil.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                ivOpen.clearAnimation();
                startActivity();

            }
        },delay);
    }

    private void startActivity() {
        Intent intent =new Intent(mHoldActivity, RedPacketOpenResultActivity.class);
        intent.putExtra("money",money);
        intent.putExtra("order_id",order_id);
        intent.putExtra("share",share);
        mHoldActivity.startActivity(intent);
        dismiss();
        if (dismissListener!=null){
            dismissListener.whenDismiss();
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

    private String name;
    private String faceIcon;
    private String showContent;
    private String order_id;
    public void setNeedData(OnlinePayOrderPaymentBean.Result.Body.Share share, String name, String faceIcon, String showContent, String order_id,String money) {
//        this.name = name;
//        this.faceIcon = faceIcon;
//        this.showContent = showContent;
        tvSubTitle.setText(showContent);
        Glide.with(mHoldActivity).load(faceIcon).into(ivFace);
        tvName.setText(name);
        this.order_id = order_id;
        this.share= share;
        this.money = money;
    }

    public interface OnPopupWindowDismissListener{
        void whenDismiss();
    }
    private RedPacketPopup.OnPopupWindowDismissListener dismissListener;

    public void setDismissListener(RedPacketPopup.OnPopupWindowDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }
}
