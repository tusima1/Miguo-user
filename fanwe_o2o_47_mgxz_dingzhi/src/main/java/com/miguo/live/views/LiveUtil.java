package com.miguo.live.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.fanwe.LoginActivity;
import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getStoreList.ModelStoreList;
import com.fanwe.user.model.UserCurrentInfo;
import com.fanwe.utils.DataFormat;
import com.miguo.live.model.getLiveListNew.ModelHost;
import com.miguo.live.model.getLiveListNew.ModelRecordFile;
import com.miguo.live.model.getLiveListNew.ModelRoom;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.live.views.view.PlayBackActivity;
import com.miguo.utils.NetWorkStateUtil;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;
import com.tencent.qcloud.suixinbo.utils.SxbLog;

import java.io.Serializable;
import java.util.List;

/**
 * Created by didik on 2016/7/25.
 */
public class LiveUtil {
    //for 测试 解析参数
    public static String praseString(String video) {
        if (video.length() == 0) {
            return "";
        }
        String result = "";
        String splitItems[];
        String tokens[];
        splitItems = video.split("\\n");
        for (int i = 0; i < splitItems.length; ++i) {
            if (splitItems[i].length() < 2)
                continue;

            tokens = splitItems[i].split(":");
            if (tokens[0].length() == "mainVideoSendSmallViewQua".length()) {
                continue;
            }
            if (tokens[0].endsWith("BigViewQua")) {
                tokens[0] = "mainVideoSendViewQua";
            }
            if (tokens[0].endsWith("BigViewQos")) {
                tokens[0] = "mainVideoSendViewQos";
            }
            result += tokens[0] + ":\n" + "\t\t";
            for (int j = 1; j < tokens.length; ++j)
                result += tokens[j];
            result += "\n\n";
            //Log.d(TAG, "test:" + result);
        }
        //Log.d(TAG, "test:" + result);
        return result;
    }

    public static float getBeautyProgress(int progress) {
        SxbLog.d("shixu", "progress: " + progress);
        return (9.0f * progress / 100.0f);
    }

    /**
     * 时间格式化
     */
    public static String updateWallTime(long second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        if (hs.equals("00")) {
            formatTime = ms + ":" + ss;
        } else {
            formatTime = hs + ":" + ms + ":" + ss;
        }
        return formatTime;
    }

    /**
     * 判断是否是主播
     *
     * @return true 是主播
     */
    public static boolean checkIsHost() {
        return MySelfInfo.getInstance().getIdStatus() == Constants.HOST ? true : false;
    }

    private static final String LIVE = "1";
    private static final String LIVE_PLAY_BACK = "1";
    private static final String PLAY_BACK = "2";

    /**
     * 直播类型
     *
     * @return
     */
    public static String getLiveType(ModelRoom room) {
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
     * 颜色
     *
     * @return
     */
    public static int getLiveTypeColor(ModelRoom room) {
        try {
            return room.getLive_type().equals(LIVE) ? R.drawable.shape_cricle_bg_yellow : R.drawable.shape_cricle_bg_black_alphe_60;
        } catch (NullPointerException e) {
            return R.drawable.shape_cricle_bg_yellow;
        }
    }

    public static void clickRoom(ModelRoom room, Activity mActivity) {
        if (TextUtils.isEmpty(App.getInstance().getToken())) {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            BaseUtils.jumpToNewActivity(mActivity, intent);
            return;
        }
        //判断网络环境
        boolean connected = NetWorkStateUtil.isConnected(mActivity);
        if (!connected) {
            MGToast.showToast("没有网络,请检测网络环境!");
            return;
        }
        //分点播和直播 直播类型  1 表示直播，2表示点播
        String live_type = room.getLive_type();
        if (LIVE.equals(live_type)) {
            if (LIVE_PLAY_BACK.equals(room.getPlayback_status())) {
                //直播回放
                gotoPlayBackActivity(room, true, mActivity);
            } else {
                //直播
                gotoLiveActivity(room, mActivity);
            }
        } else if (PLAY_BACK.equals(live_type)) {
            //点播
            gotoPlayBackActivity(room, false, mActivity);
        } else {
            //异常数据
            MGToast.showToast("异常数据");
            return;
        }
    }

    private static void gotoLiveActivity(ModelRoom room, Activity mActivity) {
        Intent intent = new Intent(mActivity, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.MEMBER);
        MySelfInfo.getInstance().setIdStatus(Constants.MEMBER);
        addCommonData(room);
        BaseUtils.jumpToNewActivity(mActivity, intent);
    }

    /**
     * 进入点播页面
     *
     * @param room
     * @param isLivePlayBack
     */
    private static void gotoPlayBackActivity(ModelRoom room, boolean isLivePlayBack, Activity mActivity) {
        //点播；直播回放
        addCommonData(room);
        //im的id
        String room_id = room.getChat_room_id();
        String is_playback = "0";
        if (isLivePlayBack) {
            room_id = room.getPlayback_room_id();
            CurLiveInfo.setRoomNum(DataFormat.toInt(room_id));
            is_playback = "1";
        }
        List<ModelRecordFile> fileSet = room.getFileset();

        Intent intent = new Intent(mActivity, PlayBackActivity.class);
        Bundle data = new Bundle();
        data.putString("room_id", room_id);
        data.putString("is_playback", is_playback);
        data.putSerializable("fileSet", (Serializable) fileSet);
        intent.putExtras(data);
        BaseUtils.jumpToNewActivity(mActivity, intent);
    }

    private static void addCommonData(ModelRoom room) {
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
