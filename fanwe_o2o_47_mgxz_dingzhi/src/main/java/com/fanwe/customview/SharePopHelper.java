package com.fanwe.customview;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getGroupDeatilNew.ShareInfoBean;
import com.fanwe.umeng.UmengShareManager;
import com.miguo.live.interf.IHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 普通分享
 */
public class SharePopHelper implements IHelper, View.OnClickListener {

    private Activity mActivity;
    private PopupWindow popupWindow;
    private ShareInfoBean shareInfoBean;
    private String type;

    public SharePopHelper(Activity mActivity) {
        this.mActivity = mActivity;
        createPopWindow();
    }

    public SharePopHelper(Activity mActivity, String type) {
        this.mActivity = mActivity;
        this.type = type;
        createPopWindow();
    }

    public void setShareInfoBean(ShareInfoBean shareInfoBean) {
        this.shareInfoBean = shareInfoBean;
    }

    @Override
    public void onDestroy() {

    }

    private void createPopWindow() {
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_share, null);
        initContentView(contentView);
        //设置窗体的宽高属性
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置可以点击
        popupWindow.setTouchable(true);
        //设置背景
        popupWindow.setAnimationStyle(R.style.pop_translate);
        BitmapDrawable background = new BitmapDrawable();
        //设置背景+
        popupWindow.setBackgroundDrawable(background);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
    }

    private LinearLayout layoutWeixin, layoutFriends, layoutQQ, layoutQQZone, layoutSina;
    private Button btnCancle;
    private TextView tvTitleUp, tvTitleDown;

    private void initContentView(View contentView) {
        layoutWeixin = ((LinearLayout) contentView.findViewById(R.id.layout_weixin_pop_share));
        layoutFriends = ((LinearLayout) contentView.findViewById(R.id.layout_friends_pop_share));
        layoutQQ = ((LinearLayout) contentView.findViewById(R.id.layout_qq_pop_share));
        layoutQQZone = ((LinearLayout) contentView.findViewById(R.id.layout_qqzone_pop_share));
        layoutSina = ((LinearLayout) contentView.findViewById(R.id.layout_sina_pop_share));
        btnCancle = (Button) contentView.findViewById(R.id.btn_cancel_pop_share);
        tvTitleUp = (TextView) contentView.findViewById(R.id.tv_title_up_pop_share);
        tvTitleDown = (TextView) contentView.findViewById(R.id.tv_title_pop_share);
        tvTitleUp.setVisibility(View.GONE);
        tvTitleDown.setVisibility(View.GONE);
        if ("SpecialTopic".equals(type) || "Goods".equals(type) || "Shop".equals(type)) {
            tvTitleUp.setVisibility(View.VISIBLE);
            tvTitleUp.setText("分享给朋友，朋友购买后大家都能领现金");
            tvTitleDown.setVisibility(View.VISIBLE);
            tvTitleDown.setText("（朋友得80%，你得20%）");
        }

        layoutWeixin.setOnClickListener(this);
        layoutFriends.setOnClickListener(this);
        layoutQQ.setOnClickListener(this);
        layoutQQZone.setOnClickListener(this);
        layoutSina.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    /*显示*/
    public void show() {
        if (popupWindow != null) {
            ViewGroup contentView = (ViewGroup) mActivity.findViewById(android.R.id.content);
            popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        }
    }


    SHARE_MEDIA platform;

    @Override
    public void onClick(View v) {
        if (v == btnCancle) {
            popupWindow.dismiss();
        } else if (v == layoutWeixin) {
            platform = SHARE_MEDIA.WEIXIN;
            share();
            popupWindow.dismiss();
        } else if (v == layoutFriends) {
            platform = SHARE_MEDIA.WEIXIN_CIRCLE;
            share();
            popupWindow.dismiss();
        } else if (v == layoutQQ) {
            platform = SHARE_MEDIA.QQ;
            share();
            popupWindow.dismiss();
        } else if (v == layoutQQZone) {
            platform = SHARE_MEDIA.QZONE;
            share();
            popupWindow.dismiss();
        } else if (v == layoutSina) {
            platform = SHARE_MEDIA.SINA;
            share();
            popupWindow.dismiss();
        }
    }

    public void share() {
        if (shareInfoBean == null) {
            return;
        }
        UmengShareManager.share(platform, mActivity, shareInfoBean.getTitle(), shareInfoBean.getSummary(), shareInfoBean.getClickurl(), UmengShareManager.getUMImage(mActivity, shareInfoBean.getImageurl()), null);
    }
}