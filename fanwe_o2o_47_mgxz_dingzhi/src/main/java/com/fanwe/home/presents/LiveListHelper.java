package com.fanwe.home.presents;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.fanwe.base.Presenter;
import com.fanwe.base.Result;
import com.fanwe.fragment.HomeFragment;
import com.fanwe.home.HomeConstants;
import com.fanwe.home.model.ResultLive;
import com.fanwe.home.model.Room;
import com.fanwe.home.model.RootLive;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;

import java.util.List;
import java.util.TreeMap;


/**
 * 直播列表
 */
public class LiveListHelper extends Presenter {
    private Context mContext;
    private static final String TAG = LiveListHelper.class.getSimpleName();
    private HomeFragment mHomeFragment;
    private int RoomId = -1;
    private Gson gson;

    public LiveListHelper(Context context) {
        mContext = context;
    }

    public LiveListHelper(Context context, HomeFragment mHomeFragment) {
        mContext = context;
        this.mHomeFragment = mHomeFragment;
        gson = new Gson();
    }


    public void getLiveList(int pageNum, int pageSize) {
        TreeMap<String, String> params = new TreeMap<String, String>();
//        params.put("token", "4ac83bbd1ff9183efe32276c8a56bea9");
        params.put("page", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(pageSize));
        params.put("method", HomeConstants.LIVE_LIST);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {



            @Override
            public void onSuccessResponse(String responseBody) {
                RootLive rootLive = gson.fromJson(responseBody, RootLive.class);
                List<ResultLive> resultLives = rootLive.getResult();
                ResultLive resultLive = resultLives.get(0);
                mHomeFragment.getLiveList(resultLive);
            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast(message);
            }
        });

    }

    @Override
    public void onDestory() {
        mHomeFragment = null;
        mContext = null;
    }
}
