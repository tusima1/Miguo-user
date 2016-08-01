package com.miguo.live.views;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveStartBinding;
import com.miguo.live.model.DataBindingLiveStart;
import com.miguo.live.views.customviews.MGToast;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.Constants;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LiveStartActivity extends Activity {
    DataBindingLiveStart dataBindingLiveStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActLiveStartBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_start);
        dataBindingLiveStart = new DataBindingLiveStart();
        dataBindingLiveStart.shopName.set("选择你的消费场所");
        binding.setLive(dataBindingLiveStart);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_shop_name:
                dataBindingLiveStart.shopName.set("米果小站");
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
            case R.id.btn_start_live_start:
                startLive();
                break;
        }
    }

    private void startLive() {
        if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.QQ) {
            Toast.makeText(this, "QQ", Toast.LENGTH_SHORT).show();
        } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.WEIXIN) {
            Toast.makeText(this, "WEIXIN", Toast.LENGTH_SHORT).show();
        } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.FRIEND) {
            Toast.makeText(this, "FRIEND", Toast.LENGTH_SHORT).show();
        } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.SINA) {
            Toast.makeText(this, "SINA", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(MySelfInfo.getInstance().getId()) || MySelfInfo.getInstance().getMyRoomNum()==-1){
            MGToast.showToast("创建房间失败,用户异常!");
        }
        Intent intent=new Intent(this,LiveActivity.class);
        intent.putExtra(Constants.ID_STATUS,Constants.HOST);
        MySelfInfo.getInstance().setIdStatus(Constants.HOST);
        MySelfInfo.getInstance().setJoinRoomWay(true);//创建房间
        CurLiveInfo.setTitle("直播");
        CurLiveInfo.setHostID(MySelfInfo.getInstance().getId());
        CurLiveInfo.setRoomNum(MySelfInfo.getInstance().getMyRoomNum());
        startActivity(intent);
        this.finish();
    }
}
