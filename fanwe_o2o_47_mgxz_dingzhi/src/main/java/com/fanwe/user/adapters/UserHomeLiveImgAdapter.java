package com.fanwe.user.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.LoginActivity;
import com.fanwe.app.App;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.user.model.getSpokePlay.ModelSpokePlay;
import com.fanwe.utils.DataFormat;
import com.miguo.live.model.getLiveListNew.ModelHost;
import com.miguo.live.model.getLiveListNew.ModelRecordFile;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.views.LiveActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.view.PlayBackActivity;
import com.miguo.utils.NetWorkStateUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * 网红主页直播列表适配器
 */

public class UserHomeLiveImgAdapter extends BaseAdapter {
    private Activity mContext;
    private LayoutInflater inflater;
    private List<ModelSpokePlay> datas;
    private ModelSpokePlay currModelSpokePlay;

    public UserHomeLiveImgAdapter(Activity mContext, LayoutInflater layoutInflater, List<ModelSpokePlay> datas) {
        this.mContext = mContext;
        this.inflater = layoutInflater;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if (null == convertView) {
            mHolder = new Holder();
            convertView = inflater.inflate(
                    R.layout.item_user_home_live, null);
            mHolder.ivBg = (ImageView) convertView.findViewById(R.id.iv_bg_item_user_home);
            mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_shop_item_live_list_user_home);
            mHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status_item_live_list_user_home);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        setData(mHolder, position);
        return convertView;
    }

    private void setData(Holder mHolder, final int position) {
        currModelSpokePlay = datas.get(position);
        SDViewBinder.setTextView(mHolder.tvName, currModelSpokePlay.getShop_name(), "");
        ImageLoader.getInstance().displayImage(currModelSpokePlay.getCover(), mHolder.ivBg);
        // 开始状态,0:未开始，1:直播中，2:已结束
        if ("0".equals(currModelSpokePlay.getStart_status())) {
            SDViewBinder.setTextView(mHolder.tvStatus, "未开始");
        } else if ("1".equals(currModelSpokePlay.getStart_status())) {
            SDViewBinder.setTextView(mHolder.tvStatus, "直播中");
        } else if ("2".equals(currModelSpokePlay.getStart_status())) {
            SDViewBinder.setTextView(mHolder.tvStatus, "已结束");
        }
    }

    private static class Holder {
        ImageView ivBg;
        TextView tvName, tvStatus;
    }

    private final String LIVE = "1";
    private final String LIVE_PLAY_BACK = "1";
    private final String PLAY_BACK = "2";

    private void clickItem(int position) {
        if (TextUtils.isEmpty(App.getInstance().getToken())) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            BaseUtils.jumpToNewActivity(mContext, intent);
            return;
        }
        //判断网络环境
        boolean connected = NetWorkStateUtil.isConnected(mContext);
        if (!connected) {
            MGToast.showToast("没有网络,请检测网络环境!");
            return;
        }
        ModelRoom room = (ModelRoom) getItem(position);
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
        Intent intent = new Intent(mContext, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.MEMBER);
        MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
        addCommonData(room);
        BaseUtils.jumpToNewActivity(mContext, intent);
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

        Intent intent = new Intent(mContext, PlayBackActivity.class);
        Bundle data = new Bundle();
        data.putString("room_id", room_id);
        data.putSerializable("fileSet", (Serializable) fileSet);
        intent.putExtras(data);
        BaseUtils.jumpToNewActivity(mContext, intent);
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
}
