package com.miguo.live.presents;

import android.content.Context;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.Presenter;
import com.fanwe.home.HomeConstants;
import com.fanwe.home.model.ResultLive;
import com.fanwe.home.model.Room;
import com.fanwe.home.model.RootLive;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.model.UserCurrentInfo;
import com.google.gson.Gson;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.applyRoom.ModelApplyRoom;
import com.miguo.live.model.applyRoom.ResultApplyRoom;
import com.miguo.live.model.applyRoom.RootApplyRoom;
import com.miguo.live.model.getAudienceCount.ModelAudienceCount;
import com.miguo.live.model.getAudienceCount.ResultAudienceCount;
import com.miguo.live.model.getAudienceCount.RootAudienceCount;
import com.umeng.socialize.utils.Log;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/7/30.
 */
public class LiveHelper extends Presenter {
    private static final String TAG = LiveHelper.class.getSimpleName();
    private Gson gson;
    private UserCurrentInfo userCurrentInfo;
    private CallbackView mView;
    private Context mContext;
    private String token = "3cd3d0e49b1d8637020e1e4a90e4514a";

    public LiveHelper(Context mContext, CallbackView mView) {
        this.mContext = mContext;
        this.mView = mView;
        gson = new Gson();
        userCurrentInfo = App.getInstance().getmUserCurrentInfo();
//        token = userCurrentInfo.getToken();
    }

    /**
     * 请求直播列表
     *
     * @param pageNum
     * @param pageSize
     */
    public void getLiveList(int pageNum, int pageSize) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("method", LiveConstants.LIVE_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {


            @Override
            public void onSuccessResponse(String responseBody) {
                RootLive rootLive = gson.fromJson(responseBody, RootLive.class);
                List<ResultLive> resultLives = rootLive.getResult();
                if (SDCollectionUtil.isEmpty(resultLives)) {
                    mView.onSuccess(LiveConstants.LIVE_LIST, null);
                    return;
                }
                ResultLive resultLive = resultLives.get(0);
                List<Room> rooms = resultLive.getBody();
                mView.onSuccess(LiveConstants.LIVE_LIST, rooms);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 申请直播房间ID
     *
     * @param shop_id
     */
    public void applyRoom(String shop_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
        params.put("shop_id", shop_id);
        params.put("method", LiveConstants.APPLY_ROOM);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {


            @Override
            public void onSuccessResponse(String responseBody) {
                RootApplyRoom rootApplyRoom = gson.fromJson(responseBody, RootApplyRoom.class);
                List<ResultApplyRoom> resultApplyRooms = rootApplyRoom.getResult();
                if (SDCollectionUtil.isEmpty(resultApplyRooms)) {
                    mView.onSuccess(LiveConstants.APPLY_ROOM, null);
                    return;
                }
                ResultApplyRoom resultApplyRoom = resultApplyRooms.get(0);
                List<ModelApplyRoom> modelApplyRooms = resultApplyRoom.getBody();
                mView.onSuccess(LiveConstants.APPLY_ROOM, modelApplyRooms);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    /**
     * 请求观众数
     *
     * @param room_id
     */
    public void getAudienceCount(String room_id) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
        params.put("room_id", room_id);
        params.put("method", LiveConstants.AUDIENCE_COUNT);

        OkHttpUtils.getInstance().post(null, params, new MgCallback() {


            @Override
            public void onSuccessResponse(String responseBody) {
                RootAudienceCount rootAudienceCount = gson.fromJson(responseBody, RootAudienceCount.class);
                List<ResultAudienceCount> resultAudienceCounts = rootAudienceCount.getResult();
                if (SDCollectionUtil.isEmpty(resultAudienceCounts)) {
                    mView.onSuccess(LiveConstants.AUDIENCE_COUNT, null);
                    return;
                }
                ResultAudienceCount resultAudienceCount = resultAudienceCounts.get(0);
                List<ModelAudienceCount> modelAudienceCounts = resultAudienceCount.getBody();
                mView.onSuccess(LiveConstants.AUDIENCE_COUNT, modelAudienceCounts);
                Log.d("onSuccess", responseBody);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    @Override
    public void onDestory() {
        mContext = null;
        mView = null;
        gson = null;
        userCurrentInfo = null;
    }
}
