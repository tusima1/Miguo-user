package com.miguo.live.views.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.constant.GiftId;
import com.fanwe.model.GiftBean;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.MGStringFormatter;
import com.google.gson.Gson;
import com.miguo.live.adapters.GiftViewPagerAdapter;
import com.miguo.live.interf.IHelper;
import com.miguo.live.interf.OnPayGiftSuccessListener;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getGiftInfo.GiftListBean;
import com.miguo.live.model.getGiftInfo.ModelGiftInfo;
import com.miguo.live.presenters.GiftHttpHelper2;
import com.miguo.live.views.RechargeDiamondActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;
import com.miguo.utils.test.MGDialog;
import com.tencent.qcloud.suixinbo.model.CurLiveInfo;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static com.tencent.qcloud.suixinbo.model.CurLiveInfo.roomNum;

/**
 * Created by didik on 2016/9/11.
 */
public class UserSendGiftPopHelper implements IHelper, View.OnClickListener, CallbackView {

    private Activity mActivity;
    private PopupWindow popupWindow;
    private TextView mTvMoney;
    private TextView mTvSend;
    private ViewPager mGiftViewPager;
    private CircleIndicator mIndicator;
    private GiftViewPagerAdapter mGiftAdapter;
    private GiftListBean selectedItemInfo;//要发送的内容info
    private int position;
    private View mRecharge;
    private float money;
    private String liveType; //类型：1直播 2点播	播放类型
    private OnPayGiftSuccessListener mListener;
    private final GiftHttpHelper2 httpHelper2;
    private final int TIME_OFFSET = 300;

    public UserSendGiftPopHelper(Activity mActivity, String liveType) {
        this.mActivity = mActivity;
        this.liveType = liveType;
        httpHelper2 = new GiftHttpHelper2(this);
        httpHelper2.getGiftList();

        createPopWindow();
    }

    public void refreshGift() {
        if (httpHelper2 != null)
            httpHelper2.getGiftList();
    }

    @Override
    public void onDestroy() {

    }

    private void createPopWindow() {
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout
                .act_live_pop_send_gift, null);
        initContentView(contentView);
        //设置窗体的宽高属性
//        int height = DisplayUtil.dp2px(mActivity, 300);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        //设置可以点击
        popupWindow.setTouchable(true);
        //设置背景
        popupWindow.setAnimationStyle(R.style.pop_translate);
//        ColorDrawable background=new ColorDrawable(0x4F000000);
        BitmapDrawable background = new BitmapDrawable();
        //设置背景+
        popupWindow.setBackgroundDrawable(background);
//
        popupWindow.setFocusable(true);

        popupWindow.setOutsideTouchable(true);

    }

    private void initContentView(View contentView) {
        mRecharge = contentView.findViewById(R.id.ll_recharge);//充值
        mTvMoney = ((TextView) contentView.findViewById(R.id.tv_money));
        mTvSend = ((TextView) contentView.findViewById(R.id.tv_send));

        mGiftViewPager = ((ViewPager) contentView.findViewById(R.id.viewpager_gift));
        mIndicator = ((CircleIndicator) contentView.findViewById(R.id.indicator_circle));

        /*init viewpager*/
        mGiftAdapter = new GiftViewPagerAdapter(mActivity, null, mTvSend);
        mGiftViewPager.setAdapter(mGiftAdapter);
        mIndicator.setViewPager(mGiftViewPager);
        mGiftAdapter.registerDataSetObserver(mIndicator.getDataSetObserver());

        mGiftViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                UserSendGiftPopHelper.this.position = position;
                GiftViewPagerAdapter adapter = (GiftViewPagerAdapter) mGiftViewPager.getAdapter();
                boolean selected = adapter.getSelected(position);
                selectedItemInfo = adapter.getSelectedItemInfo(position);
                if (selectedItemInfo != null) {
                    if (selected) {
                        mTvSend.setEnabled(true);
                    } else {
                        mTvSend.setEnabled(false);
                    }
                } else {
                    mTvSend.setEnabled(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRecharge.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
    }

    /*显示*/
    public void show() {
        /**
         * 进去的时候选择哪个界面,tab与viewpager 需要保持一致
         */
        if (popupWindow != null) {
            ViewGroup contentView = (ViewGroup) mActivity.findViewById(android.R.id.content);
            popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mRecharge) {
            clickRecharge();
        } else if (v == mTvSend) {
            clickSend();
        }
    }

    private int totalCount = 0;
    private int sendCount = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    endTime = System.currentTimeMillis();
                    long offset = endTime - preTime;
                    totalCount++;
                    if (offset < TIME_OFFSET) {
                        //连击
                        Log.e("test", offset + "");
                    } else {
                        //TODO 支付
//                        Log.e("test","roomId: "+MySelfInfo.getInstance().getMyRoomNum()+"");
//                        Log.e("test","数量: "+totalCount+"");
//                        Log.e("test","id: "+selectedItemInfo.getId());
//                        Log.e("test","liveType: "+liveType);
                        if (selectedItemInfo != null && selectedItemInfo.getId() != null) {
                            sendCount = totalCount;
                            int roomNum = CurLiveInfo.getRoomNum();
                            if (roomNum == -1) {
                                MGToast.showToast("异常直播房间");
                                resetSend();
                                return;
                            }
                            Log.e("test", "小礼物连发:" + sendCount);
                            httpHelper2.putGiftPay(liveType, roomNum + "", sendCount + "", selectedItemInfo.getId());
                            //reset
                            resetSend();
                        }

                    }
                    break;
            }
        }
    };

    /**
     * 重置计时
     */
    private void resetSend() {
        totalCount = 0;
        preTime = 0;
    }

    /*打赏*/
    private void clickSend() {
        try {
            if (mTvSend.isEnabled()) {
                if (preTime == 0) {
                    selectedItemInfo = mGiftAdapter.getSelectedItemInfo(position);
                }
                //判断余额足不足
                float unitPrice = MGStringFormatter.getFloat(selectedItemInfo.getPrice());
                if (money < unitPrice) {
                    showDialog();
                    resetSend();
                    return;
                }
                float leftMoney = money - unitPrice * totalCount;
                if (leftMoney < 0) {
                    showDialog();
                    resetSend();
                    return;
                }

                String id = selectedItemInfo.getId();
                if (GiftId.STAR.equals(id) || GiftId.FLOWER.equals(id) || GiftId.SWEET.equals(id) || GiftId.MIGUO_BABY.equals(id) || GiftId.KISS.equals(id) || GiftId.GOOD_FORTUNE.equals(id)) {
                    //发小礼物,要连发
                    preTime = System.currentTimeMillis();
                    mHandler.sendEmptyMessageDelayed(0, TIME_OFFSET);
                } else {
                    //除了小礼物只能单发
                    Log.e("test", "大礼物发送");
                    httpHelper2.putGiftPay(liveType, roomNum + "", "1", selectedItemInfo.getId());
                }
            }
        } catch (Exception e) {
            Log.e("test", "userSendGift: " + e.toString());
            MGToast.showToast("操作太频繁，请稍后再试！");
            resetSend();
        }

    }

    private void showDialog() {
        new MGDialog(mActivity)
                .setMGTitle("余额不足")
                .setContentText("当前余额不足,充值才能继续送礼")
                .setOnSureClickListener(new MGDialog.OnSureClickListener() {
                    @Override
                    public void onSure(MGDialog dialog) {
                        clickRecharge();
                        dialog.dismiss();
                    }
                }).show();
    }

    private void clickRecharge() {
        if (mActivity != null) {
            mActivity.startActivity(new Intent(mActivity, RechargeDiamondActivity.class));
        }
    }

    private long preTime = 0;
    private long endTime;

    /*展示礼物的回调*/
    public void setOnPayGiftSuccessListener(OnPayGiftSuccessListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (LiveConstants.GET_GIFT_INFO.equals(method)) {
            ModelGiftInfo modelGiftInfo = (ModelGiftInfo) datas.get(0);
            if (modelGiftInfo != null) {
                List<GiftListBean> giftList = modelGiftInfo.getGiftList();
                String userdiamond = modelGiftInfo.getUserdiamond();
                money = MGStringFormatter.getFloat(userdiamond);
//                    mTvMoney.setText(MGStringFormatter.getFloat1(userdiamond));
                mTvMoney.setText(userdiamond);
                mGiftAdapter.setData(giftList);
            }
        }else if (LiveConstants.POST_GIFT_INFO.equals(method)){
            if (datas==null || datas.size()<=0)return;
            String str = datas.get(0).toString();
            Gson gson = new Gson();
            final GiftBean root = gson.fromJson(str, GiftBean.class);
            int statusCode = root.getStatusCode();
            String message = root.getMessage();
//                {"result":[{"body":[]}],"message":"用户余额不足，无法购买","token":"8e0b891282e5d6758d06e6da8bb8fa8e","statusCode":"302"}
//                200  正常状态
//                302  参数错误，message中会有描述
//                300  处理错误，礼物未赠送成功
//                500  服务器配置错误
            if (statusCode == 200) {
                //TODO 成功
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String userdiamond = root.getResult().get(0).getBody().get(0).getUserdiamond();
                        int gift_num = root.getResult().get(0).getBody().get(0).getGift_num();
                        if (!TextUtils.isEmpty(userdiamond)) {
                            money = MGStringFormatter.getFloat(userdiamond);
                            mTvMoney.setText(userdiamond);
                        }
                        if (mListener != null) {
                            mListener.onPaySuc(selectedItemInfo, gift_num);
                            Log.e("test", "给狗蛋的--->礼物数量: " + gift_num);
                        }
                    }
                });
            } else if (statusCode == 369) {
//            if (message.contains("余额不足")){
//
//            }else {
//                MGToast.showToast("ERROR_OK:"+message);
//            }
                showDialog();
            }else if(statusCode==399){
                MGToast.showToast(message);
            }
            sendCount = 0;
        }
    }

    @Override
    public void onFailue(String responseBody) {
        if (!TextUtils.isEmpty(responseBody) && responseBody.startsWith("####")) {
            int length = responseBody.length();
            String msg = responseBody.substring(4, length);
            if (msg.contains("余额不足")) {
                showDialog();
            } else {
                MGToast.showToast(msg);
            }
            sendCount = 0;
        }
    }

    @Override
    public void onFinish(String method) {

    }
}