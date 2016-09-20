package com.fanwe.user.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fanwe.BaseActivity;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getShopAndUserCollect.ModelShopAndUserCollect;
import com.fanwe.user.presents.UserHttpHelper;

import java.util.List;

/**
 * 网红主页
 * Created by Administrator on 2016/9/20.
 */
public class UserHomeActivity extends BaseActivity implements CallbackView2 {
    private Context mContext = UserHomeActivity.this;
    private UserHttpHelper userHttpHelper;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.act_collect_list);
        initTitle();
        preWidget();
        setListener();
        userHttpHelper = new UserHttpHelper(this, this);
        preData();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("网红主页");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setImageLeft(R.drawable.ic_tuan_detail_share);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        switch (index) {
            case 0:
                clickShare();
                break;
            default:
                break;
        }
    }

    /**
     * 分享
     */
    private void clickShare() {
        UmengShareManager.share(this, "分享", "网红主页", ServerUrl.SERVER_H5 + "index/winnie/id/" + id, UmengShareManager.getUMImage(this, "http://www.mgxz.com/pcApp/Common/images/logo2.png"), null);
    }

    private void preData() {
    }

    private void setListener() {
    }

    private void preWidget() {
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelShopAndUserCollect> items;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (UserConstants.SHOP_AND_USER_COLLECT.equals(method)) {
            items = datas;
            msg.what = 0;
        }
        mHandler.sendMessage(msg);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
            }
        }
    };

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

}
