package com.miguo.live.views.gift;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.constant.GiftId;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.model.getGiftInfo.GiftListBean;
import com.miguo.live.views.base.BaseLinearLayout;
import com.miguo.live.views.customviews.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zlh on 2016/9/18.
 */
public class SmallGifView extends BaseLinearLayout{

    List<RelativeLayout> gifts;
    int GIFT_HEIGHT = 45;
    int AVATAR_ID = 0x13416513;
    int LEFT_ID = 0x4211513;
    int GIFT_ICON_ID = 0x5111513;
    int X_ICON_ID = 0x5184513;

    int[] X_IMAGES = {
            R.drawable.x_0,
            R.drawable.x_1,
            R.drawable.x_2,
            R.drawable.x_3,
            R.drawable.x_4,
            R.drawable.x_5,
            R.drawable.x_6,
            R.drawable.x_7,
            R.drawable.x_8,
            R.drawable.x_9
    };

    int[] GIFTS;
    String[] GIFS_ID;
    HashMap<String,Integer> giftsMap;
    List<GiftListBean> beans;

    public SmallGifView(Context context) {
        super(context);
    }

    public SmallGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmallGifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void init() {
        setBackgroundColor(Color.TRANSPARENT);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setParams();
        gifts = new ArrayList<>();
        giftsMap = new HashMap<>();
        beans = new ArrayList<>();
        initMap();
    }

    private void initMap(){
        GIFTS = new int[4];
        GIFTS[0] = R.drawable.star;
        GIFTS[1] = R.drawable.flower;
        GIFTS[2] = R.drawable.sweet;
        GIFTS[3] = R.drawable.miguo_baby;

        GIFS_ID = new String[4];
        GIFS_ID[0] = GiftId.STAR;
        GIFS_ID[1] = GiftId.FLOWER;
        GIFS_ID[2] = GiftId.SWEET;
        GIFS_ID[3] = GiftId.MIGUO_BABY;

        for(int i = 0; i<GIFTS.length; i++){
            giftsMap.put(GIFS_ID[i], GIFTS[i]);
        }
    }

    private void setParams(){
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(wrapContent(),wrapContent());
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        setLayoutParams(params);
    }

    public void addGift(){
        GiftListBean gift = beans.get(0);
        if(gift == null){
            return;
        }

        /**
         * 单个Item
         */
        int groupHeight = dip2px(GIFT_HEIGHT) + dip2px(10);
        RelativeLayout group = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams groupParams = getRelativeLayoutParams(wrapContent(), groupHeight);
        group.setLayoutParams(groupParams);
        /**
         * 先设置为不可见
         */
        setGone(group);

        /**
         * 左边的部分
         * 右边的礼物礼物图标
         */
        int leftHeight = dip2px(GIFT_HEIGHT) - dip2px(5 * 2);

        int giftIconWidth = leftHeight + dip2px(3 * 2);
        int giftIconHeight = giftIconWidth;


        RelativeLayout left = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams leftpParams = getRelativeLayoutParams(wrapContent(), leftHeight); // 上下间距
        leftpParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftpParams.setMargins(dip2px(10), 0, 0, 0);
        left.setLayoutParams(leftpParams);
        /**
         * 距离右边的padding是为了给右边的礼物预留位置
         */
        left.setPadding(0, 0, (int)(giftIconHeight * 1.2) , 0);
        left.setId(LEFT_ID);
        left.setBackground(getDrawable(R.drawable.shape_cricle_small_gift));

        /**
         * 头像
         */
        int avatarWidth = leftHeight - dip2px(2 * 2);
        int avatarHeight = avatarWidth;
        RoundedImageView avatar = new RoundedImageView(getContext());
        RelativeLayout.LayoutParams avatarParams = getRelativeLayoutParams(avatarWidth, avatarHeight);
        avatarParams.addRule(RelativeLayout.CENTER_VERTICAL);
        avatarParams.setMargins(dip2px(2), 0, 0, 0);
        avatar.setLayoutParams(avatarParams);
        avatar.setBorderWidth((float)dip2px(2));
        avatar.setBorderColor(Color.WHITE);
        avatar.setOval(true);
        avatar.setId(AVATAR_ID);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        SDViewBinder.setImageView(gift.getUserAvatar(), avatar);

        String avatarUrl = App.getApplication().getmUserCurrentInfo().getUserInfoNew().getIcon();
        if(avatarUrl.equals("") || avatarUrl == null){
            avatar.setImageResource(R.drawable.userlogo);
        }else {
            avatar.setImageURI(Uri.parse(avatarUrl));
        }
        left.addView(avatar);

        /**
         * 昵称
         */
        int textSize = 12;
        TextView nickname = new TextView(getContext());
        RelativeLayout.LayoutParams nicknameParams = getRelativeLayoutParams(wrapContent(), wrapContent());
        nicknameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        nicknameParams.addRule(RelativeLayout.RIGHT_OF, AVATAR_ID);
        nicknameParams.setMargins(dip2px(5), dip2px(2), 0, 0);
        nickname.setLayoutParams(nicknameParams);
        nickname.setTextColor(Color.WHITE);
        nickname.setTextSize(textSize);
        nickname.setText(gift.getUserName());
        left.addView(nickname);


        /**
         * 内容
         */
        TextView content = new TextView(getContext());
        RelativeLayout.LayoutParams contentParams = getRelativeLayoutParams(wrapContent(), wrapContent());
        contentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        contentParams.addRule(RelativeLayout.RIGHT_OF, AVATAR_ID);
        contentParams.setMargins(dip2px(5), 0, 0, dip2px(2));
        content.setLayoutParams(contentParams);
        content.setTextColor(Color.WHITE);
        content.setTextSize(textSize);
        content.setText("送了" + gift.getName());
        left.addView(content);
        group.addView(left);

        /**
         * 礼物图标
         */
        ImageView giftIcon = new ImageView(getContext());
        RelativeLayout.LayoutParams giftIconParams = getRelativeLayoutParams(giftIconWidth, giftIconHeight);
        giftIconParams.addRule(RelativeLayout.ALIGN_RIGHT, LEFT_ID);
        giftIconParams.addRule(RelativeLayout.CENTER_VERTICAL);
        giftIcon.setLayoutParams(giftIconParams);
        giftIcon.setId(GIFT_ICON_ID);
        giftIcon.setImageResource(giftsMap.get(gift.getId()));
        group.addView(giftIcon);

        /**
         *  X号图片
         */
        int xIconWidth = (int)(giftIconWidth * 0.7);
        int xIconHeight = xIconWidth;

        LinearLayout numLayout = getNumber(gift.getNum(),groupHeight, xIconWidth, xIconHeight);
        group.addView(numLayout);
        numLayout.setTag(gift.getNum());

        addView(group);
        gifts.add(group);
//        if(gifts.size() == 3){
//            gifts.remove(0);
//            removeViewAt(0);
////            startRemoveAnimation(getChildAt(0), groupHeight / 2, 0);
//            startMoveAnimation(getChildAt(0), groupHeight / 2 , 0);
//            startAddAnimation(group, numLayout, group, 0, 0);
//        }else {
//            startAddAnimation(group, numLayout,group, 0, 0);
//        }

        startAddAnimation(group, numLayout,group, 0, 0);

    }

    public void addGift(GiftListBean gift){
        if(gift == null){
            return;
        }
        if(noCurrentGift(gift)){
            addGift();
        }
    }

    private boolean noCurrentGift(GiftListBean gift){
        beans.add(gift);
        return gifts.size() < 2;
    }

    private void onCurrentAnimationEnd(){
        if(beans.size() > 0){
            addGift();
        }
    }

    /**
     * 拼接x1 x2 x10数字
     * @param number
     * @param width
     * @param height
     * @return
     */
    private LinearLayout getNumber(int number,int groupHeight, int width, int height){
        LinearLayout linearLayout = new LinearLayout(getContext());
        RelativeLayout.LayoutParams layoutParams = getRelativeLayoutParams(wrapContent(), groupHeight);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, GIFT_ICON_ID);
        layoutParams.setMargins(dip2px(2), 0, 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setPadding(0, 0, dip2px(20), 0);


        ImageView xIcon = new ImageView(getContext());
        RelativeLayout.LayoutParams xIconParams = getRelativeLayoutParams(width, height);
        xIconParams.setMargins(dip2px(5), 0, 0,0);
        xIcon.setLayoutParams(xIconParams);
        xIcon.setId(X_ICON_ID);
        xIcon.setImageResource(R.drawable.x);
        linearLayout.addView(xIcon);


        String countString = number + "";

        for(int i = 0; i<countString.length(); i++){
            ImageView x1 = new ImageView(getContext());
            LinearLayout.LayoutParams x1Params = getLinearLayoutParams(width, height);
            x1.setLayoutParams(x1Params);
            x1.setImageResource(X_IMAGES[1]);
            x1.setVisibility(i == 0 ? View.VISIBLE : View.INVISIBLE);
            linearLayout.addView(x1);
        }
        return linearLayout;
    }

    private void startRemoveAnimation(final View view, int fromY, int toY){
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,fromY, toY );
        translateAnimation.setDuration(500);
        animationSet.addAnimation(translateAnimation);

        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
//        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gifts.remove(view);
                removeView(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        view.setAnimation(animation);
        animationSet.addAnimation(animation);

        view.setAnimation(animationSet);
        animationSet.start();
    }

    private void startLeaveAnimation(final RelativeLayout group){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setStartOffset(2500);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                try{
                    gifts.remove(0);
                    removeViewAt(0);
//                    beans.remove(0);
                }catch (IndexOutOfBoundsException e){
                    gifts.clear();
                    beans.clear();
                    removeAllViews();
                }
                onCurrentAnimationEnd();

//                if(gifts.size() == 1){
//                    removeAllViews();
//                    gifts.clear();
//                }else {
//                    removeView(group);
//                    gifts.remove(group);
//                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        group.startAnimation(alphaAnimation);
    }

    private void startAddAnimation(final RelativeLayout item,final LinearLayout group, View view, int fromY, int toY){
        beans.remove(0);
        setVisible(view);
        TranslateAnimation animation = new TranslateAnimation(getWidth() * - 1, 0, fromY, toY);
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(group != null){
                    group.startAnimation(getScaleAnimtion(item, group, Integer.parseInt(group.getTag().toString())));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(animation);
        animation.start();
    }
    private ScaleAnimation getScaleAnimtion(final RelativeLayout item,final LinearLayout group, final int count){
        final int co = count;
        int number = Integer.parseInt(group.getTag().toString()) + 1;
        setNumLayouts(group, number - co);
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(100);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(group != null){
                    int c = co;
//                    int number = Integer.parseInt(group.getTag().toString()) + 1;
                    c--;
//                    setNumLayouts(group, number - c);
                    animation.cancel();
//                    Log.d(tag, "count: " + count + " ,co: " + co + " ,c: " + c + " ,number: " + number);
                    if(c > 0){
                        group.startAnimation(getScaleAnimtion(item, group, c));
                    }else{
                        startLeaveAnimation(item);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    public void setNumLayouts(LinearLayout group,int count){
        Log.d(tag, "set num layout: " + count);
        String countString = count + "";
        ImageView[] childs = new ImageView[countString.length()];
        int[] counts = new int[countString.length()];
        for(int i = 0; i<countString.length(); i++){
            childs[i] = (ImageView) group.getChildAt(i + 1);
            if(group != null && childs[i] != null){
                counts[i] = Integer.parseInt(countString.substring(i, i + 1));
                childs[i].setVisibility(View.VISIBLE);
                childs[i].setImageResource(X_IMAGES[counts[i]]);
            }
        }
        for(int j = countString.length() + 1; j<group.getChildCount(); j++){
            if(group != null && group.getChildAt(j) != null){
                group.getChildAt(j).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void startMoveAnimation(View view, int distance, int toDistance){
        TranslateAnimation animation = new TranslateAnimation(0, 0, distance, toDistance);
        animation.setDuration(500);
        animation.setFillAfter(true);
        view.setAnimation(animation);
        animation.start();
    }


    private void setVisibility(View view){
        view.setVisibility(View.VISIBLE);
    }

    private void setVisible(View view){
        view.setVisibility(View.VISIBLE);
    }

    private void setGone(View view){
        view.setVisibility(View.GONE);
    }
    private void setInvisible(View view){
        view.setVisibility(View.INVISIBLE);
    }

}
