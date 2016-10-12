package com.fanwe.user.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.constant.ServerUrl;
import com.fanwe.customview.MyGridView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.home.model.Host;
import com.fanwe.home.model.Room;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.umeng.UmengShareManager;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.ImageAdapter;
import com.fanwe.user.adapters.UserHomeLiveImgAdapter;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.getPersonHomePage.ModelPersonHomePage;
import com.fanwe.user.model.getProductList.ModelProductList;
import com.fanwe.user.model.getSpokePlay.ModelSpokePlay;
import com.fanwe.user.model.getUserAttention.ModelUserAttention;
import com.fanwe.user.model.putAttention.ModelAttention;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.MGDictUtil;
import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.view.PlayBackActivity;
import com.miguo.utils.NetWorkStateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunday.eventbus.SDEventManager;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 网红主页
 * Created by Administrator on 2016/9/20.
 */
public class UserHomeActivity extends Activity implements CallbackView2 {
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
        ((TextView) findViewById(R.id.tv_middle)).setText("网红主页");
    }


    /**
     * 分享
     */
    private void clickShare() {
        String imageUrl = "http://www.mgxz.com/pcApp/Common/images/logo2.png";
        if (!TextUtils.isEmpty(strIcon)) {
            imageUrl = strIcon;
        } else if (!TextUtils.isEmpty(MGDictUtil.getShareIcon())) {
            imageUrl = MGDictUtil.getShareIcon();
        }
        UmengShareManager.share(this, "分享", "网红主页", ServerUrl.SERVER_H5 + "index/winnie/id/" + id,
                UmengShareManager.getUMImage(this, imageUrl), null);
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
        gridViewLive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (true) {
                    return;
                }
                //TODO 跳转暂时屏蔽
                ModelSpokePlay modelSpokePlay = datasLive.get(position);
                //判断网络环境
                boolean connected = NetWorkStateUtil.isConnected(mContext);
                if (!connected) {
                    MGToast.showToast("没有网络,请检测网络环境!");
                    return;
                }
                Room room = new Room();
                room.setAv_room_id(modelSpokePlay.getAv_room_id());
                room.setChat_room_id(modelSpokePlay.getChat_room_id());
                room.setCover(modelSpokePlay.getCover());
                room.setCreate_time(modelSpokePlay.getCreate_time());
                room.setId(modelSpokePlay.getId());
                room.setLive_type(modelSpokePlay.getStart_status());

                //分点播和直播 直播类型  1 表示直播，2表示点播
                String live_type = modelSpokePlay.getStart_status();
                if ("1".equals(live_type)) {
                    //直播
                    gotoLiveActivity(room);
                } else if ("2".equals(live_type)) {
                    //点播
                    gotoPlayBackActivity(room);
                } else {
                    //异常数据
                    MGToast.showToast("异常数据");
                    return;
                }
            }
        });
    }


    private void gotoLiveActivity(Room room) {
        Intent intent = new Intent(mContext, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.MEMBER);
        MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
        addCommonData(room);
        BaseUtils.jumpToNewActivity(UserHomeActivity.this, intent);
//            startActivity(intent);
    }

    /**
     * 进入点播页面
     *
     * @param room
     */
    private void gotoPlayBackActivity(Room room) {
        addCommonData(room);
        String chat_room_id = room.getChat_room_id();//im的id
        String file_size = room.getFile_size();//文件大小
        String duration = room.getDuration();//时长
        String file_id = room.getFile_id();
        String vid = room.getVid();
        String playset = room.getPlayset();

        Intent intent = new Intent(mContext, PlayBackActivity.class);
        Bundle data = new Bundle();
        data.putString("chat_room_id", chat_room_id);
        data.putString("file_size", file_size);
        data.putString("duration", duration);
        data.putString("file_id", file_id);
        data.putString("vid", vid);
        data.putString("playset", playset);
        intent.putExtras(data);
//            startActivity(intent);
        BaseUtils.jumpToNewActivity(UserHomeActivity.this, intent);
    }

    private void addCommonData(Room room) {
        Host host = room.getHost();
        String nickName = App.getInstance().getUserNickName();
        String avatar = "";
        if (App.getInstance().getmUserCurrentInfo() != null) {
            UserCurrentInfo currentInfo = App.getInstance().getmUserCurrentInfo();
            if (currentInfo.getUserInfoNew() != null) {
                avatar = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIcon();
            }
        }
        MySelfInfo.getInstance().setAvatar(avatar);
        MySelfInfo.getInstance().setNickName(nickName);
        MySelfInfo.getInstance().setJoinRoomWay(false);
        CurLiveInfo.setHostID(host.getHost_user_id());
        CurLiveInfo.setHostName(host.getNickname());

        CurLiveInfo.setHostAvator(room.getHost().getAvatar());
        App.getInstance().addLiveRoomIdList(room.getId());
        CurLiveInfo.setRoomNum(DataFormat.toInt(room.getId()));
        if (room.getLbs() != null) {
            CurLiveInfo.setShopID(room.getLbs().getShop_id());
            ModelStoreList modelStoreList = new ModelStoreList();
            modelStoreList.setShop_name(room.getLbs().getShop_name());
            modelStoreList.setId(room.getLbs().getShop_id());
            CurLiveInfo.setModelShop(modelStoreList);
        }
        CurLiveInfo.setLive_type(room.getLive_type());

        CurLiveInfo.setHostUserID(room.getHost().getUid());
//                CurLiveInfo.setMembers(item.getWatchCount() + 1); // 添加自己
        CurLiveInfo.setMembers(1); // 添加自己
//                CurLiveInfo.setAddress(item.getLbs().getAddress());
        if (room.getLbs() != null && !TextUtils.isEmpty(room.getLbs().getShop_id())) {
            CurLiveInfo.setShopID(room.getLbs().getShop_id());
        }
        CurLiveInfo.setAdmires(1);
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
                        SDViewBinder.setTextView(tvName, currModelPersonHomePage.getNick(), "");
                        SDViewBinder.setTextView(tvAttention, currModelPersonHomePage.getFocus(), "");
                        SDViewBinder.setTextView(tvFans, currModelPersonHomePage.getFans(), "");
                        SDViewBinder.setTextView(tvSupport, currModelPersonHomePage.getLove_count(), "");
                        SDViewBinder.setTextView(tvSign, currModelPersonHomePage.getPersonality(), "");
                        strIcon = currModelPersonHomePage.getIcon();
                        ImageLoader.getInstance().displayImage(strIcon, circleImageView);
                        if ("1".equals(currModelPersonHomePage.getFx_level())) {
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_rank_3);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            tvName.setCompoundDrawables(null, null, drawable, null);
                        } else if ("2".equals(currModelPersonHomePage.getFx_level())) {
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_rank_2);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            tvName.setCompoundDrawables(null, null, drawable, null);
                        } else if ("3".equals(currModelPersonHomePage.getFx_level())) {
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_rank_1);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            tvName.setCompoundDrawables(null, null, drawable, null);
                        }
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

}
