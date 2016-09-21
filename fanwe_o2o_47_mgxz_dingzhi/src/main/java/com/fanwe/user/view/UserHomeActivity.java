package com.fanwe.user.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fanwe.BaseActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant;
import com.fanwe.constant.ServerUrl;
import com.fanwe.customview.MyGridView;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.ImageAdapter;
import com.fanwe.user.adapters.UserHomeLiveImgAdapter;
import com.fanwe.user.model.getPersonHomePage.ModelPersonHomePage;
import com.fanwe.user.model.getProductList.ModelProductList;
import com.fanwe.user.model.getSpokePlay.ModelSpokePlay;
import com.fanwe.user.presents.UserHttpHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 网红主页
 * Created by Administrator on 2016/9/20.
 */
public class UserHomeActivity extends BaseActivity implements CallbackView2 {
    private Context mContext = UserHomeActivity.this;
    private UserHttpHelper userHttpHelper;
    private String id;
    private RecyclerView recyclerViewShop;
    private MyGridView gridViewLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(Constant.TitleType.TITLE);
        setContentView(R.layout.act_user_home);
        initTitle();
        getIntentData();
        preWidget();
        setListener();
        userHttpHelper = new UserHttpHelper(this, this);
        getData();
        preData();
    }

    private List<String> imgs = new ArrayList<>();

    private void getData() {
        for (int i = 0; i < 10; i++) {
            imgs.add("http://b.hiphotos.baidu.com/image/pic/item/a686c9177f3e670900d880193fc79f3df9dc5578.jpg");
        }

        userHttpHelper.getPersonHomePage(id);
        userHttpHelper.getProductList(id);
        userHttpHelper.getSpokePlay(id);
    }

    private void getIntentData() {
        id = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(id)) {
            id = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id();
        }
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
        UmengShareManager.share(this, "分享", "网红主页", ServerUrl.SERVER_H5 + "index/winnie/id/" + id,
                UmengShareManager.getUMImage(this, "http://www.mgxz.com/pcApp/Common/images/logo2.png"), null);
    }

    private void preData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewShop.setLayoutManager(layoutManager);
        ImageAdapter adapterShop = new ImageAdapter(mContext, imgs);
        recyclerViewShop.setAdapter(adapterShop);

        //GridLayout 3列
        UserHomeLiveImgAdapter adapterLive = new UserHomeLiveImgAdapter(mContext, getLayoutInflater(), imgs);
        gridViewLive.setAdapter(adapterLive);
    }

    private void setListener() {
    }

    private void preWidget() {
        recyclerViewShop = (RecyclerView) findViewById(R.id.recyclerview_shop_act_user_home);
        gridViewLive = (MyGridView) findViewById(R.id.gridView_live_act_user_home);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelPersonHomePage> itemsPerson;
    List<ModelProductList> itemsProduct;
    List<ModelSpokePlay> itemsLive;

    @Override
    public void onSuccess(String method, List datas) {
        Message msg = new Message();
        if (UserConstants.PERSON_HOME_PAGE.equals(method)) {
            itemsPerson = datas;
            msg.what = 0;
        } else if (UserConstants.GET_PRODUCT_LIST.equals(method)) {
            itemsProduct = datas;
            msg.what = 1;
        } else if (UserConstants.GET_SPOKE_PLAY.equals(method)) {
            itemsLive = datas;
            msg.what = 2;
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
