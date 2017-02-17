package com.miguo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.didikee.uilibs.utils.StatusBarUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getGroupDeatilNew.ShareInfoBean;
import com.fanwe.umeng.UmengShareManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.ui.view.customviews.ArcDrawable;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class RedPacketOpenResultActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.tv_title)
    private TextView tvTitle;

    @ViewInject(R.id.tv_money)
    private TextView tvMoney;

    @ViewInject(R.id.iv_back)
    private ImageView ivBack;

    @ViewInject(R.id.iv_weixin_friend)
    private ImageView ivWeiXinFriend;

    @ViewInject(R.id.iv_weixin)
    private ImageView ivWeiXin;

    @ViewInject(R.id.iv_weibo)
    private ImageView ivWeiBo;

    @ViewInject(R.id.iv_qq)
    private ImageView ivQQ;

    @ViewInject(R.id.redPacket_view)
    private View redPacketView;

    private ShareInfoBean shareInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet_open_result);
        ViewUtils.inject(this);
        StatusBarUtil.setColor(this, Color.parseColor("#D23838"),0);
        setListener();
        initView();
    }

    private void initView() {
        redPacketView.setBackground(new ArcDrawable());
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        ivWeiXinFriend.setOnClickListener(this);
        ivWeiXin.setOnClickListener(this);
        ivWeiBo.setOnClickListener(this);
        ivQQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivBack){
            onBackPressed();
            return;
        }
        if (v == ivWeiXinFriend){
            onWeiXinFriendClick();
            return;
        }
        if (v == ivWeiXin){
            onWeiXinClick();
            return;
        }
        if ( v == ivWeiBo){
            onWeiBoClick();
            return;
        }
        if (v== ivQQ){
            onQQClick();
            return;
        }
    }

    private void onQQClick() {
        shareInfo = new ShareInfoBean();
        shareInfo.setClickurl("https://github.com/didikee");
        shareInfo.setImageurl("http://files.softicons.com/download/game-icons/super-mario-icons-by-sandro-pereira/png/48/Mushroom - 1UP.png");
        shareInfo.setSummary("hoisiodso就努力开发建设");
        shareInfo.setTitle("测试测试");
        UmengShareManager.share(SHARE_MEDIA.QQ, this,
                shareInfo.getTitle(),
                shareInfo.getSummary(),
                shareInfo.getClickurl(), UmengShareManager.getUMImage(this, shareInfo.getImageurl()), null);
    }

    private void onWeiBoClick() {

    }

    private void onWeiXinClick() {

    }

    private void onWeiXinFriendClick() {

    }
}
