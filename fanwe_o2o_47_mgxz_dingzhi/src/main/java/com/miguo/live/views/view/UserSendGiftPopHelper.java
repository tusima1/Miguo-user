package com.miguo.live.views.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.Root;
import com.fanwe.network.MgCallback;
import com.fanwe.network.OkHttpUtils;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.MGStringFormatter;
import com.google.gson.Gson;
import com.miguo.live.adapters.GiftViewPagerAdapter;
import com.miguo.live.interf.IHelper;
import com.miguo.live.interf.OnPayGiftSuccessListener;
import com.miguo.live.model.LiveConstants;
import com.miguo.live.model.getGiftInfo.GiftListBean;
import com.miguo.live.model.getGiftInfo.ModelGiftInfo;
import com.miguo.live.presenters.GiftHttpHelper;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;
import com.miguo.utils.test.MGDialog;
import com.miguo.utils.test.MGHttpHelper;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;

import java.util.List;
import java.util.TreeMap;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by didik on 2016/9/11.
 */
public class UserSendGiftPopHelper implements IHelper, View.OnClickListener, CallbackView2 {

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

    public UserSendGiftPopHelper(Activity mActivity,String liveType) {
        this.mActivity = mActivity;
        this.liveType=liveType;
        GiftHttpHelper giftHttpHelper = new GiftHttpHelper(this);
        giftHttpHelper.doHttpMethod(LiveConstants.GET_GIFT_INFO, MGHttpHelper.GET);

        createPopWindow();
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

    private int totalCount=0;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    endTime = System.currentTimeMillis();
                    long offset = endTime - preTime;
                    totalCount++;
                    if (offset<2000){
                        //连击
                        Log.e("test",offset+"");
                    }else {
                        //TODO 支付
                        Log.e("test","roomId: "+MySelfInfo.getInstance().getMyRoomNum()+"");
                        Log.e("test","数量: "+totalCount+"");
                        Log.e("test","id: "+selectedItemInfo.getId());
                        Log.e("test","liveType: "+liveType);
                        postGiftInfo(liveType, MySelfInfo.getInstance().getMyRoomNum()+"",totalCount+"",selectedItemInfo.getId());
                        //reset
                        totalCount=0;
                        preTime=0;
                    }
                    break;
            }
        }
    };

    /*打赏*/
    private void clickSend() {
        //是否有钱
//        int unit = 9;
//        if (money < unit) {
//            showDialog();
//        }

        if (mTvSend.isEnabled()) {
            if (preTime==0){
                selectedItemInfo = mGiftAdapter.getSelectedItemInfo(position);
            }
//            MGToast.showToast(selectedItemInfo.getName() + "[=.=]" + selectedItemInfo.getPrice());
            preTime = System.currentTimeMillis();
            mHandler.sendEmptyMessageDelayed(0,2000);
        } else {
            MGToast.showToast("Not Enable!");
        }
    }

    private void showDialog() {
        new MGDialog(mActivity)
                .setMGTitle("余额不足")
                .setContentText("当前余额不足,充值才能继续送礼")
                .setOnSureClickListener(new MGDialog.OnSureClickListener() {
                    @Override
                    public void onSure(MGDialog dialog) {
                        MGToast.showToast("充值");
                    }
                }).show();
    }

    private void clickRecharge() {
        showDialog();
    }

    private long preTime = 0;
    private long endTime;


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
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

    /**
     * 支付礼物
     */
    private void postGiftInfo(String live_type, String live_record_id, final String gift_num, String gift_id) {
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

            }

            @Override
            public void onSuccessResponse(String responseBody) {
//                {"result":[{"body":[]}],"message":"用户余额不足，无法购买","token":"8e0b891282e5d6758d06e6da8bb8fa8e","statusCode":"302"}
//                200  正常状态
//                302  参数错误，message中会有描述
//                300  处理错误，礼物未赠送成功
//                500  服务器配置错误
                Gson gson=new Gson();
                Root root = gson.fromJson(responseBody, Root.class);
                String statusCode = root.getStatusCode();
                String message = root.getMessage();
                if ("200".equals(statusCode)){
                    //TODO 成功
                    MGUIUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener!=null){
                                mListener.onPaySuc(selectedItemInfo,MGStringFormatter.getInt(gift_num));
                            }
                        }
                    });
                }else {
                    MGToast.showToast(message);
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }

    /*展示礼物的回调*/
    public void setOnPayGiftSuccessListener(OnPayGiftSuccessListener listener){
        this.mListener=listener;
    }

}