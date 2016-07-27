package com.fanwe.home.presents;

import android.content.Context;
import android.util.Log;

import com.fanwe.base.Presenter;
import com.fanwe.fragment.HomeFragment;
import com.fanwe.home.HomeConstants;
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
        params.put("method", HomeConstants.LIVE_LIST);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {

            @Override
            public void onSuccessResponse(String responseBody) {
                Log.d("responseBody LIVE_LIST:", responseBody);
                mHomeFragment.getLiveList(responseBody);
            }
        });
    }

    @Override
    public void onDestory() {
        mHomeFragment = null;
        mContext = null;
    }
}
