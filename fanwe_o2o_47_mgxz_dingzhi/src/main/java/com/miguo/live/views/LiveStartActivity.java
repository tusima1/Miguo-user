package com.miguo.live.views;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fanwe.LoginActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.network.MgCallback;
import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveStartBinding;
import com.fanwe.user.model.UserInfoNew;
import com.fanwe.user.presents.LoginHelper;
import com.google.gson.Gson;
import com.miguo.live.model.DataBindingLiveStart;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.model.generateSign.ResultGenerateSign;
import com.miguo.live.model.generateSign.RootGenerateSign;
import com.miguo.live.presenters.LiveHttpHelper;
import com.miguo.live.presenters.TencentHttpHelper;
import com.tencent.qcloud.suixinbo.avcontrollers.QavsdkControl;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LiveStartActivity extends Activity implements CallbackView {

    DataBindingLiveStart dataBindingLiveStart;
    private  com.tencent.qcloud.suixinbo.presenters.LoginHelper mLoginHelper ;
    private TencentHttpHelper tencentHttpHelper;
    private String token;
    /**
     *
     * 签名后 的userid
     */
    String usersig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActLiveStartBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_start);
        dataBindingLiveStart = new DataBindingLiveStart();

        binding.setLive(dataBindingLiveStart);
        mLoginHelper = new com.tencent.qcloud.suixinbo.presenters.LoginHelper(this,this);
        tencentHttpHelper = new TencentHttpHelper(this);
        init();

    }
    public  void init(){
         token = App.getApplication().getToken();
        if(TextUtils.isEmpty(token)){
            Intent intent = new Intent(LiveStartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            String is_host = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getIs_host();
            if("0".equals(is_host)){
                SDToast.showToast("您还未成为主播");
                Intent intent = new Intent(this, LiveAuthActivity.class);
                startActivity(intent);
                 finish();
            }else{
                 dataBindingLiveStart.shopName.set("选择你的消费场所");
                 dataBindingLiveStart.isLiveRight.set(true);
                getSign();
            }
        }
    }

    /**
     * 取用户签名。
     */
    public void getSign(){
        MgCallback mgCallback = new MgCallback() {
            @Override
            public void onSuccessResponse(String responseBody) {
                 Gson gson=new Gson();
                RootGenerateSign rootGenerateSign = gson.fromJson(responseBody, RootGenerateSign.class);
                List<ResultGenerateSign> resultGenerateSigns = rootGenerateSign.getResult();
                if (SDCollectionUtil.isEmpty(resultGenerateSigns)) {
                  SDToast.showToast("获取用户签名失败。");
                    return;
                }
                ResultGenerateSign resultGenerateSign = resultGenerateSigns.get(0);
                List<ModelGenerateSign> modelGenerateSign = resultGenerateSign.getBody();

                if(modelGenerateSign!=null&& modelGenerateSign.size()>0&&modelGenerateSign.get(0)!=null) {
                     usersig = modelGenerateSign.get(0).getUsersig();
                    MySelfInfo.getInstance().setUserSig(usersig);
                    App.getInstance().setUserSign(usersig);
                }

            }

            @Override
            public void onErrorResponse(String message, String errorCode) {
                SDToast.showToast("获取用户签名失败。");
            }
        };
        tencentHttpHelper.getSign(token,mgCallback);
    }
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_shop_name:
                dataBindingLiveStart.shopName.set("米果小站");
                dataBindingLiveStart.isLiveRight.set(true);
                break;
            case R.id.iv_qq_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.QQ);
                break;
            case R.id.iv_weixin_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.WEIXIN);
                break;
            case R.id.iv_friend_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.FRIEND);
                break;
            case R.id.iv_sina_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.SINA);
                break;
            case R.id.iv_qqzone_live_start:
                dataBindingLiveStart.mode.set(dataBindingLiveStart.QQZONE);
                break;
            case R.id.btn_start_live_start:
                startLive();
                break;
        }
    }

    private void startLive() {
        if (dataBindingLiveStart.isLiveRight.get()) {
            //创建房间号并进入主播页
            createAvRoom();
            //已认证的，去直播
            if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.QQ) {
                Toast.makeText(this, "QQ", Toast.LENGTH_SHORT).show();
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.WEIXIN) {
                Toast.makeText(this, "WEIXIN", Toast.LENGTH_SHORT).show();
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.FRIEND) {
                Toast.makeText(this, "FRIEND", Toast.LENGTH_SHORT).show();
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.SINA) {
                Toast.makeText(this, "SINA", Toast.LENGTH_SHORT).show();
            } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.QQZONE) {
                Toast.makeText(this, "QQZONE", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
            }
        } else {
            //未认证的，去认证
            startActivity(new Intent(this, LiveAuthActivity.class));
        }
    }
    /**
     * 初始化AVSDK
     */
    private void startAVSDK() {
        String userid = MySelfInfo.getInstance().getId();
        String userSign =  MySelfInfo.getInstance().getUserSig();
        int  appId = Constants.SDK_APPID;

        int  ccType = Constants.ACCOUNT_TYPE;
        QavsdkControl.getInstance().setAvConfig(appId, ccType+"",userid, userSign);
        QavsdkControl.getInstance().startContext();

        Log.e("live","初始化AVSDK");
    }
    /**
     * 进入直播Activity(创建直播)
     */
    public void createAvRoom(){
        String userId = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id();
        //注册腾讯并申请房间号。
        if(TextUtils.isEmpty(userId)||TextUtils.isEmpty(usersig)){
            SDToast.showToast("用户名或者签名为空");
            return;
        }
        mLoginHelper.imLogin(userId,usersig);

    }

    /**
     * 进入主播页。
     */
    private void goToLive() {
        UserInfoNew userInfoNew = App.getInstance().getmUserCurrentInfo().getUserInfoNew();
        MySelfInfo.getInstance().setId(userInfoNew.getUser_id());
        Intent intent = new Intent(this, LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS, Constants.HOST);
        MySelfInfo.getInstance().setIdStatus(Constants.HOST);
        MySelfInfo.getInstance().setJoinRoomWay(true);
        CurLiveInfo.setTitle("直播");
        CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
        CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
        this.startActivity(intent);


    }

    @Override
    public void onSuccess(String responseBody) {
        //testLive();
    }

    @Override
    public void onSuccess(String method, List datas) {
//        switch (method) {
//            case LiveConstants.GENERATE_SIGN:
//                UserInfoNew userInfoNew = App.getInstance().getmUserCurrentInfo().getUserInfoNew();
//                ModelGenerateSign sign = (ModelGenerateSign) datas.get(0);
//                usersig = sign.getUsersig();
//                com.tencent.qcloud.suixinbo.presenters.LoginHelper tcLogin = new com.tencent.qcloud
//                        .suixinbo.presenters.LoginHelper(LiveStartActivity.this);
//                tcLogin.imLogin(userInfoNew.getUser_id(), usersig);
//                //请求房间号
////                http.applyRoom("4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b1");
//                break;
//            case LiveConstants.APPLY_ROOM:
////                ModelApplyRoom room = (ModelApplyRoom) datas.get(0);
////                String room_id = room.getRoom_id();
//                //开启直播
//
//                break;
//        }
    }

    @Override
    public void onFailue(String responseBody) {

    }
}
