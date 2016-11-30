package com.fanwe.user.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.common.model.CommonConstants;
import com.fanwe.common.model.createShareRecord.ModelCreateShareRecord;
import com.fanwe.common.presenters.CommonHttpHelper;
import com.fanwe.constant.Constant;
import com.fanwe.constant.ServerUrl;
import com.fanwe.customview.MyGridView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.ImageAdapter;
import com.fanwe.user.adapters.UserHomeLiveImgAdapter;
import com.fanwe.user.model.getPersonHomePage.ModelPersonHomePage;
import com.fanwe.user.model.getProductList.ModelProductList;
import com.fanwe.user.model.getUserAttention.ModelUserAttention;
import com.fanwe.user.model.putAttention.ModelAttention;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.MGDictUtil;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.views.customviews.MGToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunday.eventbus.SDEventManager;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 网红主页
 * Created by Administrator on 2016/9/20.
 */
public class UserHomeActivity extends Activity implements CallbackView {
    private Context mContext = UserHomeActivity.this;
    private UserHttpHelper userHttpHelper;
    private String id;
    private String toastContent;
    private RecyclerView recyclerViewShop;
    private MyGridView gridViewLive;
    private CircleImageView circleImageView;
    private TextView tvName, tvAttention, tvFans, tvSupport, tvSign, tvAttentionStatus;

    private List<String> imgsProduct = new ArrayList<>();
    private List<ModelRoom> datasLive = new ArrayList<>();
    private RelativeLayout layoutShopEmpty, layoutLiveEmpty, layoutShop;
    private String nick;

    private CommonHttpHelper commonHttpHelper;
    private String shareRecordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_home);
        initTitle();
        getIntentData();
        preWidget();
        setListener();
        userHttpHelper = new UserHttpHelper(this, this);
        getData();
        preData();
        preView();
        getRecordId();
    }


    private void preView() {
        if (App.getInstance().getmUserCurrentInfo().getUserInfoNew() == null) {
            return;
        }
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
        toastContent = getIntent().getStringExtra("toastContent");
        if (!TextUtils.isEmpty(toastContent)) {
            MGToast.showToast(toastContent);
        }
    }

    private void initTitle() {
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.iv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickShare();
            }
        });
        ((TextView) findViewById(R.id.tv_middle)).setText("个人主页");
    }


    /**
     * 分享
     */
    private void clickShare() {
        getRecordId();
        String imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
        if (!TextUtils.isEmpty(strIcon)) {
            imageUrl = strIcon;
        } else if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
            imageUrl = MGDictUtil.getShareIcon();
        }
        String title = "精彩推荐";
        if (!TextUtils.isEmpty(nick)) {
            title = nick + "的精彩推荐";
        }
        String clickUrl = ServerUrl.getAppH5Url() + "index/winnie/id/" + id + "/share_record_id/" + shareRecordId;
        UmengShareManager.share(this, title, "跟随我，过更好的生活~ ", clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
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
        adapterLive = new UserHomeLiveImgAdapter(this, getLayoutInflater(), datasLive);
        gridViewLive.setAdapter(adapterLive);
    }

    private void setListener() {
        layoutShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, DistributionStoreWapActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    private void preWidget() {
        recyclerViewShop = (RecyclerView) findViewById(R.id.recyclerview_shop_act_user_home);
        gridViewLive = (MyGridView) findViewById(R.id.gridView_live_act_user_home);
        gridViewLive.setFocusable(false);
        circleImageView = (CircleImageView) findViewById(R.id.iv_icon_act_user_home);
        tvName = (TextView) findViewById(R.id.tv_name_act_user_home);
        tvAttention = (TextView) findViewById(R.id.tv_num_attention_act_user_home);
        tvFans = (TextView) findViewById(R.id.tv_num_fans_act_user_home);
        tvSupport = (TextView) findViewById(R.id.tv_support_attention_act_user_home);
        tvSign = (TextView) findViewById(R.id.tv_sign_act_user_home);
        tvAttentionStatus = (TextView) findViewById(R.id.tv_attention_status_act_user_home);
        layoutShopEmpty = (RelativeLayout) findViewById(R.id.layout_shop_empty_act_user_home);
        layoutLiveEmpty = (RelativeLayout) findViewById(R.id.layout_live_empty_act_user_home);
        layoutShop = (RelativeLayout) findViewById(R.id.layout_shop_act_user_home);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    List<ModelPersonHomePage> itemsPerson;
    List<ModelProductList> itemsProduct;
    List<ModelRoom> itemsLive;
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
        } else if (CommonConstants.CREATE_SHARE_RECORD.equals(method)) {
            if (!SDCollectionUtil.isEmpty(datas)) {
                ModelCreateShareRecord bean = (ModelCreateShareRecord) datas.get(0);
                shareRecordId = bean.getId();
            }
        }
        mHandler.sendMessage(msg);
    }

    ModelPersonHomePage currModelPersonHomePage = new ModelPersonHomePage();
    ModelUserAttention modelUserAttention;
    private String strIcon;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //个人信息
                    if (!SDCollectionUtil.isEmpty(itemsPerson)) {
                        currModelPersonHomePage = itemsPerson.get(0);
                        nick = currModelPersonHomePage.getNick();
                        SDViewBinder.setTextView(tvName, currModelPersonHomePage.getNick(), "");
                        SDViewBinder.setTextView(tvAttention, currModelPersonHomePage.getFocus(), "");
                        SDViewBinder.setTextView(tvFans, currModelPersonHomePage.getFans(), "");
                        SDViewBinder.setTextView(tvSupport, currModelPersonHomePage.getLove_count(), "");
                        SDViewBinder.setTextView(tvSign, currModelPersonHomePage.getPersonality(), " 一生很难，一辈子很长，所以不能停止探寻有趣的生活。一个爱吃爱玩爱享受的人。");
                        strIcon = currModelPersonHomePage.getIcon();
                        ImageLoader.getInstance().displayImage(strIcon, circleImageView);
                        //取消用户等级图标的显示。
//                        if ("1".equals(currModelPersonHomePage.getFx_level())) {
//                            Drawable drawable = getResources().getDrawable(R.drawable.ic_rank_3);
//                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                            tvName.setCompoundDrawables(null, null, drawable, null);
//                        } else if ("2".equals(currModelPersonHomePage.getFx_level())) {
//                            Drawable drawable = getResources().getDrawable(R.drawable.ic_rank_2);
//                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                            tvName.setCompoundDrawables(null, null, drawable, null);
//                        } else if ("3".equals(currModelPersonHomePage.getFx_level())) {
//                            Drawable drawable = getResources().getDrawable(R.drawable.ic_rank_1);
//                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                            tvName.setCompoundDrawables(null, null, drawable, null);
//                        }
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
                            SDEventManager.post(EnumEventTag.FOCUS_CHANGE_NO.ordinal());
                        } else if ("2".equals(modelAttention.getAttention_status())) {
                            tvAttentionStatus.setText("已关注");
                            SDEventManager.post(EnumEventTag.FOCUS_CHANGE_YES.ordinal());
                        } else if ("3".equals(modelAttention.getAttention_status())) {
                            tvAttentionStatus.setText("已关注");
                            SDEventManager.post(EnumEventTag.FOCUS_CHANGE_YES.ordinal());
                        } else {
                            tvAttentionStatus.setText("+ 关注");
                            SDEventManager.post(EnumEventTag.FOCUS_CHANGE_NO.ordinal());
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

    private void getRecordId() {
        if (commonHttpHelper == null) {
            commonHttpHelper = new CommonHttpHelper(mContext, this);
        }
        commonHttpHelper.createShareRecord(Constant.ShareType.USER_HOME, id);
    }
}
