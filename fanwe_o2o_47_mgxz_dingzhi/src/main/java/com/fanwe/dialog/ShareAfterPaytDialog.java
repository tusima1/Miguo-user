package com.fanwe.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.constant.ServerUrl;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.model.Share_info;
import com.fanwe.umeng.UmengShareManager;
import com.miguo.live.views.category.dialog.DialogCategory;
import com.miguo.live.views.dialog.BaseDialog;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 购物后分享得佣金
 */
public class ShareAfterPaytDialog extends BaseDialog {
    Context mContext;
    RelativeLayout layoutDialog;
    LinearLayout layoutShare;
    Button btnNo, btnYes;
    TextView tvMoney;
    ImageView ivQQ, ivWeixin, ivFriend, ivSina;
    SHARE_MEDIA platform = SHARE_MEDIA.QQ;
    Share_info share_info;
    UMShareListener shareResultCallback;
    boolean flag;

    public ShareAfterPaytDialog(Context mContext, Share_info share_info, UMShareListener shareResultCallback, boolean flag) {
        super(mContext, R.style.floag_dialog, Gravity.TOP);
        this.mContext = mContext;
        this.share_info = share_info;
        this.shareResultCallback = shareResultCallback;
        this.flag = flag;
        preView();
        setListener();
    }

    private void setListener() {
//        btnNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnNo.setBackgroundResource(R.drawable.ic_btn_yes_share_normal);
//                btnNo.setTextColor(Color.parseColor("#C32836"));
//                btnYes.setBackgroundResource(R.drawable.ic_btn_no_share_normal);
//                btnYes.setTextColor(Color.parseColor("#FFFFFF"));
//                dismiss();
//            }
//        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnYes.setBackgroundResource(R.drawable.ic_btn_yes_share_normal);
                btnYes.setTextColor(Color.parseColor("#C32836"));
                btnNo.setBackgroundResource(R.drawable.ic_btn_no_share_normal);
                btnNo.setTextColor(Color.parseColor("#FFFFFF"));
                layoutDialog.setBackgroundResource(R.drawable.ic_bg_yes_share_normal);
                layoutShare.setVisibility(View.VISIBLE);
            }
        });
        ivQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBtn();
                platform = SHARE_MEDIA.QQ;
                ivQQ.setImageResource(R.drawable.ic_qq_share_select);
                doShare();
            }
        });
        ivWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBtn();
                platform = SHARE_MEDIA.WEIXIN;
                ivWeixin.setImageResource(R.drawable.ic_weixin_share_select);
                doShare();
            }
        });
        ivFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBtn();
                platform = SHARE_MEDIA.WEIXIN_CIRCLE;
                ivFriend.setImageResource(R.drawable.ic_friend_share_select);
                doShare();
            }
        });
        ivSina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBtn();
                platform = SHARE_MEDIA.SINA;
                ivSina.setImageResource(R.drawable.ic_weibo_share_select);
                doShare();
            }
        });
    }

    private void clearBtn() {
        ivQQ.setImageResource(R.drawable.ic_qq_share_normal);
        ivWeixin.setImageResource(R.drawable.ic_weixin_share_normal);
        ivFriend.setImageResource(R.drawable.ic_friend_share_normal);
        ivSina.setImageResource(R.drawable.ic_weibo_share_normal);
    }

    private void doShare() {
        dismiss();
        String imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
        if (!TextUtils.isEmpty(share_info.getImageurl())) {
            imageUrl = share_info.getImageurl();
        }
        String title = "能省又会赚，你的吃喝玩乐我包了！";
        if (!TextUtils.isEmpty(share_info.getTitle())) {
            title = share_info.getTitle();
        }
        String content = "老妈说能省不如会赚！我在米果小站不仅省钱，还能赚钱！你也来试试？";
        if (!TextUtils.isEmpty(share_info.getSummary())) {
            content = share_info.getSummary();
        }
        String clickUrl = ServerUrl.SERVER_H5;
        if (!TextUtils.isEmpty(share_info.getClickurl())) {
            clickUrl = share_info.getClickurl();
        }
        UmengShareManager.share(platform, (Activity) mContext, title, content, clickUrl, UmengShareManager.getUMImage(mContext, imageUrl), shareResultCallback);
    }

    public void setSubmitListener(View.OnClickListener onClickListenerSubmit) {
        btnYes.setOnClickListener(onClickListenerSubmit);
    }

    public void setCloseListener(View.OnClickListener onClickListenerClose) {
        btnNo.setOnClickListener(onClickListenerClose);
    }


    private void preView() {
        layoutDialog = (RelativeLayout) findViewById(R.id.layout_dialog_share_after_pay);
        layoutShare = (LinearLayout) findViewById(R.id.layout_share_dialog_share_after_pay);
        tvMoney = (TextView) findViewById(R.id.tv_money_dialog_share_after_pay);
        btnNo = (Button) findViewById(R.id.btn_no_dialog_share_after_pay);
        btnYes = (Button) findViewById(R.id.btn_yes_dialog_share_after_pay);
        //share
        ivQQ = (ImageView) findViewById(R.id.iv_qq_dialog_share_after_pay);
        ivWeixin = (ImageView) findViewById(R.id.iv_weixin_dialog_share_after_pay);
        ivFriend = (ImageView) findViewById(R.id.iv_friend_dialog_share_after_pay);
        ivSina = (ImageView) findViewById(R.id.iv_sina_dialog_share_after_pay);
        if (!TextUtils.isEmpty(share_info.getSalarySum())) {
            SDViewBinder.setTextView(tvMoney, share_info.getSalarySum() + "元");
        } else {
            SDViewBinder.setTextView(tvMoney, "0元");
        }
        if (flag) {
            btnYes.setBackgroundResource(R.drawable.ic_btn_yes_share_normal);
            btnYes.setTextColor(Color.parseColor("#C32836"));
            btnNo.setBackgroundResource(R.drawable.ic_btn_no_share_normal);
            btnNo.setTextColor(Color.parseColor("#FFFFFF"));
            layoutDialog.setBackgroundResource(R.drawable.ic_bg_yes_share_normal);
            layoutShare.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected DialogCategory initCategory() {
        return null;
    }

    @Override
    protected int setDialogContentView() {
        return R.layout.dialog_share_after_pay;
    }

}
