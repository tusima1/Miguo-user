package com.miguo.live.views;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveStartBinding;
import com.fanwe.user.model.UserInfoNew;
import com.miguo.live.model.DataBindingLiveStart;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.generateSign.ModelGenerateSign;
import com.miguo.live.presenters.LiveHttpHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LiveStartActivity extends Activity implements CallbackView {

    private LiveHttpHelper http;
    private String usersig;
    DataBindingLiveStart dataBindingLiveStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActLiveStartBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_start);
        dataBindingLiveStart = new DataBindingLiveStart();
        dataBindingLiveStart.shopName.set("选择你的消费场所");
        dataBindingLiveStart.isLiveRight.set(true);
        binding.setLive(dataBindingLiveStart);
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
            testLive();
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
     * 进入直播Activity(创建直播)
     */
    public void createAvRoom(){
        ////如果是自己
//        MySelfInfo.getInstance().setId(mUser_id);
//        Intent intent = new Intent(this, LiveActivity.class);
//        intent.putExtra(Constants.ID_STATUS, Constants.HOST);
//        MySelfInfo.getInstance().setIdStatus(Constants.HOST);
//        MySelfInfo.getInstance().setJoinRoomWay(true);
//        CurLiveInfo.setTitle("直播");
//        CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
//        CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
//        startActivity(intent);
//        this.finish();
//        if (TextUtils.isEmpty(MySelfInfo.getInstance().getId()) || MySelfInfo.getInstance().getMyRoomNum()==-1){
//            MGToast.showToast("创建房间失败,用户异常!");
//        }
//        Intent intent=new Intent(this,LiveActivity.class);
//        intent.putExtra(Constants.ID_STATUS,Constants.HOST);
//        MySelfInfo.getInstance().setIdStatus(Constants.HOST);
//        MySelfInfo.getInstance().setJoinRoomWay(true);//创建房间
//        CurLiveInfo.setTitle("直播");
//        CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
//        CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
//        startActivity(intent);
//        this.finish();

    }
    private void testLive() {
        //获取sig
        http = new LiveHttpHelper(this, this);
        http.generateSign();


    }

    @Override
    public void onSuccess(String responseBody) {
    }

    @Override
    public void onSuccess(String method, List datas) {
        switch (method) {
            case LiveConstants.GENERATE_SIGN:
                UserInfoNew userInfoNew = App.getInstance().getmUserCurrentInfo().getUserInfoNew();
                ModelGenerateSign sign = (ModelGenerateSign) datas.get(0);
                usersig = sign.getUsersig();
                com.tencent.qcloud.suixinbo.presenters.LoginHelper tcLogin = new com.tencent.qcloud
                        .suixinbo.presenters.LoginHelper(LiveStartActivity.this);
                tcLogin.imLogin(userInfoNew.getUser_id(), usersig);
                //请求房间号
//                http.applyRoom("4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b1");
                break;
            case LiveConstants.APPLY_ROOM:
//                ModelApplyRoom room = (ModelApplyRoom) datas.get(0);
//                String room_id = room.getRoom_id();
                //开启直播

                break;
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }
}
