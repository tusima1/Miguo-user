package com.fanwe.user.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.BaseActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant;
import com.fanwe.constant.ServerUrl;
import com.fanwe.customview.MyGridView;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.ImageAdapter;
import com.fanwe.user.adapters.UserHomeLiveImgAdapter;
import com.fanwe.user.model.getPersonHomePage.ModelPersonHomePage;
import com.fanwe.user.model.getProductList.ModelProductList;
import com.fanwe.user.model.getSpokePlay.ModelSpokePlay;
import com.fanwe.user.model.getUserAttention.ModelUserAttention;
import com.fanwe.user.model.putAttention.ModelAttention;
import com.fanwe.user.presents.UserHttpHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private CircleImageView circleImageView;
    private TextView tvName, tvAttention, tvFans, tvSupport, tvSign, tvAttentionStatus;

    private List<String> imgsProduct = new ArrayList<>();
    private List<ModelSpokePlay> datasLive = new ArrayList<>();
    private RelativeLayout layoutShopEmpty, layoutLiveEmpty;

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
        preView();
    }

    private void preView() {
        if (id.equals(App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id())) {
            //当前用户
            tvAttentionStatus.setVisibility(View.GONE);
        } else {
            userHttpHelper.getUserAttention(id);
            tvAttentionStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("已关注".equals(tvAttentionStatus.getText().toString())) {
                        //取消关注
                        userHttpHelper.putAttention(id, "0");
                    } else {
                        //关注
                        userHttpHelper.putAttention(id, "1");
                    }
                }
            });
        }
    }

    private void getData() {
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

    ImageAdapter adapterShop;
    UserHomeLiveImgAdapter adapterLive;

    private void preData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewShop.setLayoutManager(layoutManager);
        adapterShop = new ImageAdapter(mContext, imgsProduct);
        recyclerViewShop.setAdapter(adapterShop);

        //GridLayout 3列
        adapterLive = new UserHomeLiveImgAdapter(mContext, getLayoutInflater(), datasLive);
        gridViewLive.setAdapter(adapterLive);
    }

    private void setListener() {
    }

    private void preWidget() {
        recyclerViewShop = (RecyclerView) findViewById(R.id.recyclerview_shop_act_user_home);
        gridViewLive = (MyGridView) findViewById(R.id.gridView_live_act_user_home);
        circleImageView = (CircleImageView) findViewById(R.id.iv_icon_act_user_home);
        tvName = (TextView) findViewById(R.id.tv_name_act_user_home);
        tvAttention = (TextView) findViewById(R.id.tv_num_attention_act_user_home);
        tvFans = (TextView) findViewById(R.id.tv_num_fans_act_user_home);
        tvSupport = (TextView) findViewById(R.id.tv_support_attention_act_user_home);
        tvSign = (TextView) findViewById(R.id.tv_sign_act_user_home);
        tvAttentionStatus = (TextView) findViewById(R.id.tv_attention_status_act_user_home);
        layoutShopEmpty = (RelativeLayout) findViewById(R.id.layout_shop_empty_act_user_home);
        layoutLiveEmpty = (RelativeLayout) findViewById(R.id.layout_live_empty_act_user_home);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelPersonHomePage> itemsPerson;
    List<ModelProductList> itemsProduct;
    List<ModelSpokePlay> itemsLive;
    List<ModelUserAttention> itemsAttention;
    List<ModelAttention> itemsForcus;

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
        } else if (UserConstants.USER_ATTENTION.equals(method)) {
            itemsAttention = datas;
            msg.what = 3;
        } else if (UserConstants.ATTENTION.equals(method)) {
            itemsForcus = datas;
            msg.what = 4;
        }
        mHandler.sendMessage(msg);
    }

    ModelPersonHomePage currModelPersonHomePage = new ModelPersonHomePage();
    ModelUserAttention modelUserAttention;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //个人信息
                    if (!SDCollectionUtil.isEmpty(itemsPerson)) {
                        currModelPersonHomePage = itemsPerson.get(0);
                        SDViewBinder.setTextView(tvName, currModelPersonHomePage.getNick(), "");
                        SDViewBinder.setTextView(tvAttention, currModelPersonHomePage.getFocus(), "");
                        SDViewBinder.setTextView(tvFans, currModelPersonHomePage.getFans(), "");
                        SDViewBinder.setTextView(tvSupport, currModelPersonHomePage.getLove_count(), "");
                        SDViewBinder.setTextView(tvSign, currModelPersonHomePage.getPersonality(), "");
                        ImageLoader.getInstance().displayImage(currModelPersonHomePage.getIcon(), circleImageView);
                    }
                    break;
                case 1:
                    //TA的最爱
                    imgsProduct.clear();
                    if (!SDCollectionUtil.isEmpty(itemsProduct)) {
                        for (ModelProductList bean : itemsProduct) {
                            if (!TextUtils.isEmpty(bean.getIcon())) {
                                imgsProduct.add(bean.getIcon());
                            }
                        }
                    }
                    if (SDCollectionUtil.isEmpty(imgsProduct)) {
                        recyclerViewShop.setVisibility(View.GONE);
                        layoutShopEmpty.setVisibility(View.VISIBLE);
                    } else {
                        recyclerViewShop.setVisibility(View.VISIBLE);
                        layoutShopEmpty.setVisibility(View.GONE);
                    }
                    adapterShop.notifyDataSetChanged();
                    break;
                case 2:
                    //TA的直播
                    datasLive.clear();
                    if (!SDCollectionUtil.isEmpty(itemsLive)) {
                        datasLive.addAll(itemsLive);
                    }
                    if (SDCollectionUtil.isEmpty(datasLive)) {
                        gridViewLive.setVisibility(View.GONE);
                        layoutLiveEmpty.setVisibility(View.VISIBLE);
                    } else {
                        gridViewLive.setVisibility(View.VISIBLE);
                        layoutLiveEmpty.setVisibility(View.GONE);
                    }
                    adapterLive.notifyDataSetChanged();
                    break;
                case 3:
                    if (!SDCollectionUtil.isEmpty(itemsAttention)) {
                        modelUserAttention = itemsAttention.get(0);
                        // 0：未关注 1：已关注
                        if ("0".equals(modelUserAttention.getAttention())) {
                            tvAttentionStatus.setText("+ 关注");
                        } else {
                            tvAttentionStatus.setText("已关注");
                        }
                    }
                    break;
                case 4:
                    if (!SDCollectionUtil.isEmpty(itemsForcus)) {
                        ModelAttention modelAttention = itemsForcus.get(0);
                        //操作后与该对象的状态1：未关注 2：已关注 3：互相关注
                        if ("1".equals(modelAttention.getAttention_status())) {
                            tvAttentionStatus.setText("+ 关注");
                        } else if ("2".equals(modelAttention.getAttention_status())) {
                            tvAttentionStatus.setText("已关注");
                        } else if ("3".equals(modelAttention.getAttention_status())) {
                            tvAttentionStatus.setText("已关注");
                        } else {
                            tvAttentionStatus.setText("+ 关注");
                        }
                    }
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
