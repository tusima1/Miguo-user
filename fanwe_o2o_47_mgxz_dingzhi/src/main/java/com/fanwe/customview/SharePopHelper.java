package com.fanwe.customview;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.miguo.live.interf.IHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 *
 */
public class SharePopHelper implements IHelper, View.OnClickListener {

    private Activity mActivity;
    private PopupWindow popupWindow;
    boolean isShowTitle;

    public SharePopHelper(Activity mActivity, boolean isShowTitle) {
        this.mActivity = mActivity;
        this.isShowTitle = isShowTitle;
        createPopWindow();
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

    private void initContentView(View contentView) {

    }

    /*显示*/
    public void show() {
        /**
         * 进去的时候选择哪个界面,tab与viewpager 需要保持一致
         */
        if (popupWindow != null) {
            ViewGroup contentView = (ViewGroup) mActivity.findViewById(android.R.id.content);
            popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        }
    }


    SHARE_MEDIA platform;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_cancel_pop_share:
                popupWindow.dismiss();
                break;
            case R.id.layout_weixin_pop_share:
                platform = SHARE_MEDIA.WEIXIN;
                share();
                break;
            case R.id.layout_friends_pop_share:
                platform = SHARE_MEDIA.WEIXIN_CIRCLE;
                share();
                break;
            case R.id.layout_qq_pop_share:
                platform = SHARE_MEDIA.QQ;
                share();
                break;
            case R.id.layout_qqzone_pop_share:
                platform = SHARE_MEDIA.QZONE;
                share();
                break;
            case R.id.layout_sina_pop_share:
                platform = SHARE_MEDIA.SINA;
                share();
                break;
        }
    }

    public void share() {
        UmengShareManager.share(platform, mActivity, "分享", "直播结束分享", "http://www.mgxz.com/", UmengShareManager.getUMImage(mActivity, "http://www.mgxz.com/pcApp/Common/images/logo2.png"), null);
    }
}