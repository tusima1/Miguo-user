package com.miguo.live.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.base.Presenter;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.model.UserCurrentInfo;
import com.google.gson.Gson;
import com.miguo.live.model.RedPacketInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/2.
 */
public class RedPacketHelper extends Presenter {



    private static final String TAG = RedPacketHelper.class.getSimpleName();
    private Gson gson;
    private UserCurrentInfo userCurrentInfo;
    private CallbackView mView;
    private Context mContext;
    private String token;

    public RedPacketHelper(Context mContext) {
        this.mContext = mContext;
        gson = new Gson();

    }

    /**
     * 获取当前主播拥有的红包列表。
     * @param userid
     * @param shopId
     * @param callback
     */
    public void getHostRedPacketList(String userid, String shopId, MgCallback<RedPacketInfo> callback){
        if(TextUtils.isEmpty(userid) || TextUtils.isEmpty(shopId)){
            SDToast.showToast("主播ID或者店铺ID 为空。");
            return;
        }
        TreeMap<String,String> params = new TreeMap<String,String>();
        params.put("",userid);
        params.put("userid",shopId);
        params.put("method","");
        OkHttpUtils.getInstance().get(null,params,callback);
    }
    public List<RedPacketInfo> getTestData(){
        List<RedPacketInfo>  redList= new ArrayList<RedPacketInfo>();

        for(int i = 0 ; i < 15; i ++) {
            RedPacketInfo redPacketInfo = new RedPacketInfo();
            redPacketInfo.setCount(10+i);
            int type = i%9;
            redPacketInfo.setType(type);
            redList.add(redPacketInfo);
        }
        return redList;
    }
    @Override
    public void onDestory() {

    }
}
