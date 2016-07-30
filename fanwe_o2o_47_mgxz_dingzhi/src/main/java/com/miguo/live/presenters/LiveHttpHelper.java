package com.miguo.live.presenters;

import com.fanwe.base.Result;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.model.RoomIDEntity;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/7/28.
 */
public class LiveHttpHelper implements IHelper {


    public void getAvRoomId(String shop_id,String token){
            TreeMap<String, String> params = new TreeMap<String, String>();
            params.put("shop_id", shop_id);
            params.put("token",token);
            params.put("method", "ApplyRoom");
            OkHttpUtils.getInstance().post(null, params, new MgCallback() {


                @Override
                public void onSuccessListResponse(List<Result> resultList) {

                }

                @Override
                public void onErrorResponse(String message, String errorCode) {

                }

                @Override
                public void onSuccessResponse(String responseBody) {
                    Gson gson=new Gson();
                    RoomIDEntity testModel = gson.fromJson(responseBody, RoomIDEntity.class);
                    if (testModel==null){
                        MGToast.showToast("为空了!");
                        return;
                    }
                    String room_id = testModel.getRoom_id();
                    String token1 = testModel.getToken();
//                    MySelfInfo.getInstance().setMyRoomNum();
//                    MySelfInfo.getInstance().writeToCache(context.getApplicationContext());
                    MGToast.showToast("mRoomId"+room_id);
                }
            });
    }

    @Override
    public void onDestroy() {

    }
}
