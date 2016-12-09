package com.miguo.live.presenters;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.content.Context;

import com.fanwe.app.App;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.user.model.UserInfoNew;
import com.google.gson.Gson;
import com.miguo.live.interf.ITencentResult;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.model.generateSign.ResultGenerateSign;
import com.miguo.live.model.generateSign.RootGenerateSign;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by didik on 2016/8/2.
 */
public class TencentHttpHelper implements ITencentResult {
    private Activity mActivity;
    private ITencentResult mListener;
    private Context mContext;

    @Override
    public void onResult(boolean succ) {

    }

    public TencentHttpHelper(Activity activity){
        this.mActivity=activity;

    }

    public TencentHttpHelper(Context context){
        this.mContext=context;

    }
    public TencentHttpHelper(Activity activity,ITencentResult iTencentResult){
        this.mActivity=activity;
        this.mListener=iTencentResult;
        initUser2Myinfo();
    }

    private void initUser2Myinfo() {
        UserInfoNew userInfoNew = App.getInstance().getCurrentUser();
        if (userInfoNew!=null && !TextUtils.isEmpty(userInfoNew.getUser_id())){
            MySelfInfo.getInstance().setId(userInfoNew.getUser_id());
        }else {
            Log.e("live","这是一个未登录用户吗?");
        }
    }

    public void setOnTencentResult(ITencentResult iTencentResult){
        this.mListener=iTencentResult;
    }
    /**
     * 身IM 注册 用户。
     * @param usersig
     */
    public void requestRoomID(String usersig){
        new com.tencent.qcloud.suixinbo.presenters.LoginHelper(mActivity).imLogin(MySelfInfo.getInstance().getId(),usersig);
    }

    /**
     * 获取签名。
     * @param token
     * @param mgCallback
     */
    public void getSign(String token ,MgCallback mgCallback){



            TreeMap<String, String> params = new TreeMap<String, String>();
            params.put("token", token);
            params.put("method", LiveConstants.GENERATE_SIGN);

            OkHttpUtils.getInstance().get(null, params, mgCallback);

    }

    /**
     * 直播登录，返回用户直播签名  GenerateSign
     */
    public void generateSign() {
        String token = App.getInstance().getToken();
        final Gson gson=new Gson();
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("token", token);
        params.put("method", LiveConstants.GENERATE_SIGN);

        OkHttpUtils.getInstance().get(null, params, new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                RootGenerateSign rootGenerateSign = gson.fromJson(responseBody, RootGenerateSign.class);
                List<ResultGenerateSign> resultGenerateSigns = rootGenerateSign.getResult();
                if (SDCollectionUtil.isEmpty(resultGenerateSigns)) {
//                    onSuccess(mView,LiveConstants.GENERATE_SIGN, null);
                    mListener.onResult(false);
                    return;
                }
                ResultGenerateSign resultGenerateSign = resultGenerateSigns.get(0);
                List<ModelGenerateSign> modelGenerateSign = resultGenerateSign.getBody();
//                onSuccess(mView,LiveConstants.GENERATE_SIGN, modelGenerateSign);
                if(modelGenerateSign!=null&& modelGenerateSign.size()>0) {
                    String usersig = modelGenerateSign.get(0).getUsersig();
                    MySelfInfo.getInstance().setUserSig(usersig);
                    App.getInstance().setUserSign(usersig);
                }

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                Log.e("live","message:"+message+"errorCode:"+errorCode);
                mListener.onResult(false);
            }
        });

    }

    @Override
    public void onDestroy() {
        mContext = null;
        mActivity = null;

    }
}
