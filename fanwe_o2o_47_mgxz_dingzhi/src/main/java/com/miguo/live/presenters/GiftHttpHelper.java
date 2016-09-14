package com.miguo.live.presenters;

import com.fanwe.base.CallbackView2;
import com.miguo.live.interf.IHelper;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getGiftInfo.ModelGiftInfo;
import com.miguo.live.model.getGiftInfo.ResultGiftInfo;
import com.miguo.live.model.getGiftInfo.RootGiftInfo;
import com.miguo.utils.test.MGHttpHelper;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/9/12.
 */
public class GiftHttpHelper extends MGHttpHelper implements IHelper {
    private CallbackView2 mView2;

    public GiftHttpHelper(CallbackView2 mView2) {
        this.mView2=mView2;
    }
    @Override
    protected TreeMap addParams(String method, TreeMap params) {
        switch (method){
            case LiveConstants.GET_GIFT_INFO:
            break;
        }
        return params;
    }

    @Override
    protected void onSuccess(String method, String responseBody) {
        switch (method) {
            case LiveConstants.GET_GIFT_INFO:
                getGiftInfo(method,responseBody);
                break;
        }
    }

    @Override
    protected void onFinished(String method) {
        mView2.onFinish(method);
    }

    private void getGiftInfo(String method, String responseBody) {
        ResultGiftInfo resultGiftInfo = gson.fromJson(responseBody, RootGiftInfo.class).getResult
                ().get(0);
        if (resultGiftInfo!=null){
            List<ModelGiftInfo> body = resultGiftInfo.getBody();
            if (body!=null && body.size()>0){
                mView2.onSuccess(method,body);
                return;
            }
        }
        mView2.onFailue(method);
    }

    @Override
    public void onDestroy() {
        mView2=null;
    }
}
