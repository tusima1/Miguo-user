package com.miguo.live.presenters;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.google.gson.Gson;
import com.miguo.live.interf.IHelper;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getGiftInfo.ModelGiftInfo;
import com.miguo.live.model.getGiftInfo.ResultGiftInfo;
import com.miguo.live.model.getGiftInfo.RootGiftInfo;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/9/19.
 */
public class GiftHttpHelper2 implements IHelper {
    private Gson gson;
    private CallbackView2 mView2;

    public GiftHttpHelper2(CallbackView2 mView2) {
        this.gson = new Gson();
        this.mView2 = mView2;
    }

    public void getGiftList(){
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("method", LiveConstants.GET_GIFT_INFO);
        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                MGToast.showToast(message);
            }

            @Override
            public void onSuccessResponse(String responseBody) {
                ResultGiftInfo resultGiftInfo = gson.fromJson(responseBody, RootGiftInfo.class).getResult
                        ().get(0);
                if (resultGiftInfo!=null){
                    final List<ModelGiftInfo> body = resultGiftInfo.getBody();
                    if (body!=null && body.size()>0){
                        MGUIUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView2.onSuccess(LiveConstants.GET_GIFT_INFO,body);
                            }
                        });
                        return;
                    }
                }
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView2.onFailue(LiveConstants.GET_GIFT_INFO);
                    }
                });

            }
        });
    }

    public void putGiftPay(String live_type, String live_record_id, final String gift_num, String gift_id){
//        live_type	String	必须		类型：1直播 2点播	播放类型
//        live_record_id	String	必须		直播id	直播id(就是房间room_id)
//        gift_num	String	必须		礼物数量
//        gift_id	String	必须		礼物id
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", App.getInstance().getToken());
        params.put("live_type", live_type);
        params.put("live_record_id", live_record_id);
        params.put("gift_num", gift_num);
        params.put("gift_id", gift_id);
        params.put("method", LiveConstants.POST_GIFT_INFO);
        OkHttpUtils.getInstance().put(null, params, new MgCallback() {
            @Override
            public void onErrorResponse(String message, String errorCode) {
                mView2.onFailue("####"+message);
            }

            @Override
            public void onSuccessResponse(final String responseBody) {
//                {"result":[{"body":[]}],"message":"用户余额不足，无法购买","token":"8e0b891282e5d6758d06e6da8bb8fa8e","statusCode":"302"}
//                200  正常状态
//                302  参数错误，message中会有描述
//                300  处理错误，礼物未赠送成功
//                500  服务器配置错误
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView2.onSuccess(responseBody);
                    }
                });

            }
        });
    }

    @Override
    public void onDestroy() {
        gson=null;
        mView2=null;
    }
}
