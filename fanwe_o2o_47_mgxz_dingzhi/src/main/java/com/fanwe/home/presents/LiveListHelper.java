package com.fanwe.home.presents;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.fanwe.base.Presenter;
import com.fanwe.base.Result;
import com.fanwe.fragment.HomeFragment;
import com.fanwe.home.HomeConstants;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;

import java.util.TreeMap;


/**
 * 直播列表
 */
public class LiveListHelper extends Presenter {
    private Context mContext;
    private static final String TAG = LiveListHelper.class.getSimpleName();
    private HomeFragment mHomeFragment;
    private int RoomId = -1;

    public LiveListHelper(Context context) {
        mContext = context;
    }

    public LiveListHelper(Context context, HomeFragment mHomeFragment) {
        mContext = context;
        this.mHomeFragment = mHomeFragment;
    }


    public void getLiveList() {
        TreeMap<String, String> params = new TreeMap<String, String>();
//        params.put("token", "4ac83bbd1ff9183efe32276c8a56bea9");
        params.put("page", "1");
        params.put("page_size", "1");
        params.put("method", HomeConstants.LIVE_LIST);

//        OkHttpUtils.getInstance().get(null, params, new MgCallback<JSONObject>() {
//
//            @Override
//            public void onSuccessResponse(Result<JSONObject> responseBody) {
//                if (responseBody == null || responseBody.getBody() == null) {
//                    onErrorResponse("请求直播列表失败", null);
//                }
//                if (responseBody.getBody().size() > 0) {
//                    mHomeFragment.getLiveList(responseBody.getBody().get(0));
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String message, String errorCode) {
//                SDToast.showToast(message);
//            }
//        });

    }

    @Override
    public void onDestory() {
        mHomeFragment = null;
        mContext = null;
    }
}
