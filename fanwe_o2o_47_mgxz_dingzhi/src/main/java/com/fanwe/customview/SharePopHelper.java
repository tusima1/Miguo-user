package com.fanwe.customview;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.fanwe.app.App;
import com.fanwe.constant.ServerUrl;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.utils.MGDictUtil;
import com.miguo.live.interf.IHelper;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 *
 */
public class SharePopHelper implements IHelper, View.OnClickListener {

    private Activity mActivity;
    private PopupWindow popupWindow;
    private boolean isHost;

    public SharePopHelper(Activity mActivity, boolean isHost) {
        this.mActivity = mActivity;
        this.isHost = isHost;
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

    private LinearLayout layoutWeixin, layoutFriends, layoutQQ, layoutQQZone, layoutSina;
    private Button btnCancle;

    private void initContentView(View contentView) {
        layoutWeixin = ((LinearLayout) contentView.findViewById(R.id.layout_weixin_pop_share));
        layoutFriends = ((LinearLayout) contentView.findViewById(R.id.layout_friends_pop_share));
        layoutQQ = ((LinearLayout) contentView.findViewById(R.id.layout_qq_pop_share));
        layoutQQZone = ((LinearLayout) contentView.findViewById(R.id.layout_qqzone_pop_share));
        layoutSina = ((LinearLayout) contentView.findViewById(R.id.layout_sina_pop_share));
        btnCancle = (Button) contentView.findViewById(R.id.btn_cancel_pop_share);

        layoutWeixin.setOnClickListener(this);
        layoutFriends.setOnClickListener(this);
        layoutQQ.setOnClickListener(this);
        layoutQQZone.setOnClickListener(this);
        layoutSina.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
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
        String content;
        String title = "送你钻石";
        String imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
        if (isHost) {
            if ("1".equals(CurLiveInfo.getLive_type())) {
                content = "直接领钻石，打赏有底气！我送你钻石，来陪我吧？" + App.getInstance().getmUserCurrentInfo().getUserInfoNew().getNick() + "正在直播中.....";
            } else {
                content = "直接领钻石，打赏有底气！我送你钻石，来陪我吧？" + App.getInstance().getmUserCurrentInfo().getUserInfoNew().getNick() + "的精彩记录片.....";
            }
            if (!TextUtils.isEmpty(App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon())) {
                imageUrl = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon();
            } else if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                imageUrl = MGDictUtil.getShareIcon();
            }
        } else {
            if ("1".equals(CurLiveInfo.getLive_type())) {
                content = "直接领钻石，打赏有底气！我送你钻石，来陪我吧？" + CurLiveInfo.getHostName() + "正在直播中.....";
            } else {
                content = "直接领钻石，打赏有底气！我送你钻石，来陪我吧？" + CurLiveInfo.getHostName() + "的精彩记录片.....";
            }
            if (!TextUtils.isEmpty(CurLiveInfo.getHostAvator())) {
                imageUrl = CurLiveInfo.getHostAvator();
            } else if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
                imageUrl = MGDictUtil.getShareIcon();
            }
        }
        if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            //朋友圈
            title = content;
        }

        UmengShareManager.share(platform, mActivity, title, content,
                ServerUrl.SERVER_H5 + "share/live/rid/" + CurLiveInfo.getRoomNum() + "/uid/" + App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id(),
                UmengShareManager.getUMImage(mActivity, imageUrl), null);
    }
}