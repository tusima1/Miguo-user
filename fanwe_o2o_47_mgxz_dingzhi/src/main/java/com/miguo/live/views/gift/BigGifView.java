package com.miguo.live.views.gift;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.model.getGiftInfo.GiftListBean;
import com.miguo.live.views.base.BaseRelativeLayout;

import java.util.Random;

/**
 * Created by zlh on 2016/9/21.
 */
public class BigGifView extends BaseRelativeLayout{

    public static final int GIFT_WIDTH = 750;
    public static final int GIFT_HEIGHT = 680;
    public static final long DURATION = 5000;
    public static final int KISS_WIDTH = 200;
    public static final int KISS_HEIGHT = 200;



    public BigGifView(Context context) {
        super(context);
    }

    public BigGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BigGifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        setBackgroundColor(Color.TRANSPARENT);

    }

    /**
     * 增加么么哒动画
     * @param bean
     */
    public void addKiss(GiftListBean bean){
        if(bean == null || bean.getId() == null || bean.getId().equals("")){
            return;
        }

        int width = dip2px(150);
        int height = width;
        ImageView gift = new ImageView(getContext());
        RelativeLayout.LayoutParams giftParams = getRelativeLayoutParams(width, height);
        giftParams.setMargins(new Random().nextInt(getScreenWidth() - width), new Random().nextInt(geScreenHeight() - height), 0, 0);
        giftParams.addRule(RelativeLayout.CENTER_VERTICAL);
        gift.setLayoutParams(giftParams);
        gift.setImageResource(R.drawable.kiss);
        addView(gift);
        startLeaveAnimation(gift, bean);
    }

    /**
     * 增加红包/福气冲天
     * @param bean
     */
    public void addRedPacket(GiftListBean bean){
        if(bean == null || bean.getId() == null || bean.getId().equals("")){
            return;
        }

        int width = dip2px(150);
        int height = width;
        ImageView gift = new ImageView(getContext());
        RelativeLayout.LayoutParams giftParams = getRelativeLayoutParams(width, height);
        giftParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        giftParams.addRule(RelativeLayout.CENTER_VERTICAL);
        gift.setLayoutParams(giftParams);
        gift.setImageResource(R.drawable.redpacket);
        addView(gift);
        startLeaveAnimation(gift, bean);
    }


    /**
     * 大礼物
     * @param bean
     */
    public void addBigGift(GiftListBean bean){
        if(bean == null || bean.getId() == null || bean.getId().equals("")){
            return;
        }

        ImageView gift = new ImageView(getContext());
        RelativeLayout.LayoutParams giftParams = getRelativeLayoutParams(getGiftWidth(), getGiftHeight());
        giftParams.addRule(RelativeLayout.CENTER_VERTICAL);
        gift.setLayoutParams(giftParams);
        addView(gift);
        gift.postDelayed(new BigGiftRunnable(gift, bean), getSpeed(bean.getId()));

    }

    private void startLeaveAnimation(final View view,GiftListBean bean){
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(300);
        animation.setStartOffset(2000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                BigGifView.this.removeView(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    /**
     * 礼物宽度
     * @return
     */
    public int getGiftWidth(){
        return getScreenWidth();
    }

    /**
     * 礼物高度
     * @return
     */
    public int getGiftHeight(){
        return getGiftWidth() * GIFT_HEIGHT / GIFT_WIDTH;
    }

    public class BigGiftRunnable implements Runnable{

        ImageView gift;
        GiftListBean bean;
        int currentIndex;

        public BigGiftRunnable(ImageView gift, GiftListBean bean){
            this.gift = gift;
            this.bean = bean;
            setCurrentIndex(0);
        }

        @Override
        public void run() {
            if(getCurrentIndex() < getCount()){
                gift.setImageResource(GiftPictures.getItem(bean.getId(), getCurrentIndex()));
                setCurrentIndex(getCurrentIndex() + 1);
                gift.postDelayed(this,getSpeed(bean.getId()));
            }else {
                removeView(gift);
            }
        }

        public int getCount(){
            return GiftPictures.getCount(bean.getId());
        }

        public int getCurrentIndex() {
            return currentIndex;
        }

        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        private void startLeaveAnimation(){
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
            alphaAnimation.setDuration(200);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    removeView(gift);
                    invalidate();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            gift.startAnimation(alphaAnimation);
        }

    }

    public long getSpeed(String giftId){
        return DURATION / GiftPictures.getCount(giftId);
    }

}
