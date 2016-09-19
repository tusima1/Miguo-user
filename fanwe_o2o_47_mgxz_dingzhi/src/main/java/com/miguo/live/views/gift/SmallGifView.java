package com.miguo.live.views.gift;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.model.getGiftInfo.GiftListBean;
import com.miguo.live.views.base.BaseLinearLayout;
import com.miguo.live.views.customviews.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlh on 2016/9/18.
 */
public class SmallGifView extends BaseLinearLayout{

    List<RelativeLayout> gifts;
    int GIFT_HEIGHT = 60;
    int AVATAR_ID = 0x13416513;
    int LEFT_ID = 0x4211513;

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
        gifts = new ArrayList<>();
    }

    public void addGift(GiftListBean gift){
        if(gift == null){
            return;
        }

        /**
         * 单个Item
         */
        int groupHeight = dip2px(GIFT_HEIGHT);
        RelativeLayout group = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams groupParams = getRelativeLayoutParams(wrapContent(), groupHeight);
        group.setLayoutParams(groupParams);

        /**
         * 左边的部分
         * 没有礼物图标
         */
        int leftHeight = dip2px(GIFT_HEIGHT) - dip2px(5 * 2);
        RelativeLayout left = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams leftpParams = getRelativeLayoutParams(wrapContent(), leftHeight); // 上下间距
        left.setLayoutParams(leftpParams);
        left.setPadding(0, 0, leftHeight, 0);
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
        avatar.setBorderWidth(dip2px(2));
        avatar.setBorderColor(Color.WHITE);
        SDViewBinder.setImageView(gift.getUserAvatar(), avatar);

//        RoundingParams params = new RoundingParams();
//        params.setRoundAsCircle(true);
//        params.setBorder(Color.WHITE, dip2px(1));

//        avatar.getHierarchy().setRoundingParams(params);
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
        int textSize = 14;
        TextView nickname = new TextView(getContext());
        RelativeLayout.LayoutParams nicknameParams = getRelativeLayoutParams(wrapContent(), wrapContent());
        nicknameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        nicknameParams.addRule(RelativeLayout.RIGHT_OF, AVATAR_ID);
        nicknameParams.setMargins(dip2px(2), dip2px(2), 0, 0);
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
        contentParams.addRule(RelativeLayout.RIGHT_OF, avatar.getId());
        contentParams.setMargins(dip2px(2), 0, 0, dip2px(2));
        content.setLayoutParams(nicknameParams);
        content.setTextColor(Color.WHITE);
        content.setTextSize(textSize);
        content.setText("送了" + gift.getName());
        left.addView(content);
        group.addView(left);



        int giftIconWidth = leftHeight + dip2px(3 * 2);
        int giftIconHeight = giftIconWidth;
        ImageView giftIcon = new ImageView(getContext());
        RelativeLayout.LayoutParams giftIconParams = getRelativeLayoutParams(giftIconWidth, giftIconHeight);
        giftIconParams.addRule(RelativeLayout.ALIGN_LEFT, LEFT_ID);
        giftIcon.setLayoutParams(giftIconParams);
        SDViewBinder.setImageView(gift.getIcon(), giftIcon);

        group.addView(giftIcon);
        addView(group);

    }

}
