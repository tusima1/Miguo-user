package com.miguo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.adapter.barry.BarryBaseRecyclerAdapter;
import com.fanwe.app.App;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.utils.DataFormat;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.model.getLiveListNew.ModelHost;
import com.miguo.live.model.getLiveListNew.ModelRecordFile;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.view.PlayBackActivity;
import com.miguo.utils.NetWorkStateUtil;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/21.
 * 商家主页 最底部 在现场
 */
public class HiShopDetailLiveAdapter extends BarryBaseRecyclerAdapter {

    private final String LIVE = "1";
    private final String LIVE_PLAY_BACK = "1";
    private final String PLAY_BACK = "2";


    public HiShopDetailLiveAdapter(Activity activity, List datas) {
        super(activity, datas);
    }

    @Override
    protected RecyclerView.ViewHolder initHolder(View view, int viewTyp) {
        return new ViewHolder(view);
    }

    @Override
    protected View inflatView(ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.activity_hishop_detail_live_item, parent, false);
    }

    @Override
    protected void findHolderViews(View view, RecyclerView.ViewHolder holder, int viewType) {
        ViewUtils.inject(holder, view);
    }

    @Override
    protected BarryListener initHolderListener(RecyclerView.ViewHolder holder, int position) {
        return new HiShopDetailLiveAdapterListener(this, holder, position);
    }

    @Override
    protected void setHolderListener(RecyclerView.ViewHolder holder, int position, BarryListener listener) {
        getHolder(holder).image.setOnClickListener(listener);
    }

    @Override
    protected void doThings(RecyclerView.ViewHolder holder, int position) {
        updateImageHeight(holder, position);
    }

    private void updateImageHeight(RecyclerView.ViewHolder holder, int position) {
        RelativeLayout.LayoutParams params = getRelativeLayoutParams(getImageHeight(), getImageHeight());
        getHolder(holder).image.setLayoutParams(params);
        getHolder(holder).image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    protected void setHolderViews(RecyclerView.ViewHolder holder, int position) {
        try {
            ModelRoom room = getItem(position);
            if (room.getCover().contains("http:")) {
                SDViewBinder.setImageView(getItem(position).getCover(), getHolder(holder).image);
            }
            getHolder(holder).type.setText(getLiveType(room));
            getHolder(holder).type.setBackgroundResource(getLiveTypeColor(room));
            SDViewBinder.setTextView(getHolder(holder).localtion, room.getLbs().getAddress(), "");
        } catch (Exception e) {

        }
    }

    public int getItemHeight() {
        return getImageHeight() * getLine();
    }

    public int getLine() {
        return getItemCount() / 3 + (getItemCount() % 3 > 0 ? 1 : 0);
    }

    public int getImageHeight() {
        int screenWidth = getScreenWidth();
        return screenWidth / 3;
    }

    @Override
    public ModelRoom getItem(int position) {
        return (ModelRoom) super.getItem(position);
    }

    @Override
    public ViewHolder getHolder(RecyclerView.ViewHolder holder) {
        return (ViewHolder) super.getHolder(holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @ViewInject(R.id.image)
        ImageView image;

        @ViewInject(R.id.type)
        TextView type;

        @ViewInject(R.id.location)
        TextView localtion;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    class HiShopDetailLiveAdapterListener extends BarryListener {


        public HiShopDetailLiveAdapterListener(BarryBaseRecyclerAdapter adapter, RecyclerView.ViewHolder holder, int position) {
            super(adapter, holder, position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image:
                    clickImage();
                    break;
            }
        }

        private void clickImage() {
            ModelRoom room = getAdapter().getItem(position);
            clickItem(room);
        }


        @Override
        public HiShopDetailLiveAdapter getAdapter() {
            return (HiShopDetailLiveAdapter) super.getAdapter();
        }
    }

    private void clickItem(ModelRoom room) {
        if (TextUtils.isEmpty(App.getInstance().getToken())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            BaseUtils.jumpToNewActivity(getActivity(), intent);
            return;
        }
        //判断网络环境
        boolean connected = NetWorkStateUtil.isConnected(getActivity());
        if (!connected) {
            MGToast.showToast("没有网络,请检测网络环境!");
            return;
        }
        //分点播和直播 直播类型  1 表示直播，2表示点播
        String live_type = room.getLive_type();
        if (LIVE.equals(live_type)) {
            if (LIVE_PLAY_BACK.equals(room.getPlayback_status())) {
                //直播回放
                gotoPlayBackActivity(room, true);
            } else {
                //直播
                gotoLiveActivity(room);
            }
        } else if (PLAY_BACK.equals(live_type)) {
            //点播
            gotoPlayBackActivity(room, false);
        } else {
            //异常数据
            MGToast.showToast("异常数据");
            return;
        }
    }

    private void gotoLiveActivity(ModelRoom room) {
        Intent intent = new Intent(getActivity(), LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.MEMBER);
        MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
        addCommonData(room);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    /**
     * 进入点播页面
     *
     * @param room
     * @param isLivePlayBack
     */
    private void gotoPlayBackActivity(ModelRoom room, boolean isLivePlayBack) {
        //点播；直播回放
        addCommonData(room);
        //im的id
        String room_id = room.getChat_room_id();
        if (isLivePlayBack) {
            room_id = room.getPlayback_room_id();
            CurLiveInfo.setRoomNum(DataFormat.toInt(room_id));
        }
        List<ModelRecordFile> fileSet = room.getFileset();

        Intent intent = new Intent(getActivity(), PlayBackActivity.class);
        Bundle data = new Bundle();
        data.putString("room_id", room_id);
        data.putSerializable("fileSet", (Serializable) fileSet);
        intent.putExtras(data);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    private void addCommonData(ModelRoom room) {
        ModelHost host = room.getHost();
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


    /**
     * 直播类型
     *
     * @return
     */
    private String getLiveType(ModelRoom room) {
        try {
            if (LIVE.equals(room.getLive_type())) {
                if (LIVE_PLAY_BACK.equals(room.getPlayback_status())) {
                    return "精彩视频";
                } else {
                    return "正在直播";
                }
            } else {
                return "精彩视频";
            }
        } catch (NullPointerException e) {
            return "精彩视频";
        }
    }

    /**
     * 直播类型
     *
     * @return
     */
    private int getLiveTypeColor(ModelRoom room) {
        try {
            return room.getLive_type().equals(LIVE) ? R.drawable.shape_cricle_bg_yellow : R.drawable.shape_cricle_bg_black_alphe_60;
        } catch (NullPointerException e) {
            return R.drawable.shape_cricle_bg_yellow;
        }
    }

}
