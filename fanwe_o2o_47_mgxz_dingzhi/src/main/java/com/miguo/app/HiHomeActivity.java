package com.miguo.app;

import android.content.Intent;
import android.text.TextUtils;

import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.StoreDetailActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.constant.EnumEventTag;
import com.fanwe.constant.ServerUrl;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getCityList.ModelCityList;
import com.fanwe.work.AppRuntimeWorker;
import com.miguo.category.Category;
import com.miguo.category.HiHomeCategory;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.definition.ResultCode;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.BaseUtils;
import com.miguo.utils.dev.DevUtil;
import com.sunday.eventbus.SDBaseEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by  zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeActivity extends HiBaseActivity {

    String currentCityId;
    /**
     * 商家id (int)
     */
    public static final String EXTRA_MERCHANT_ID = "extra_merchant_id";
    public static final String EXTRA_GOODS_ID = "extra_goods_id";
    /**
     * http://m.w2.mgxz.com/user/shop/uid/88025143-194f-4705-991b-7f5a3587dc9c
     * 门店详情
     */
    private final static String SHOP_DETAIL = "^https?://[^/]+.mgxz.com/index/retail/id/([^/\\s]+)";
    /**
     * 他的小店.
     */
    private final static String SHOP_PATTERN = "^https?://[^/]+.mgxz.com/user/shop/uid/([^/\\s]+)";
    /**
     * 团购详情.
     */
    private final static String SHOPPING_DETAIL = "^https?://[^/]+.mgxz.com/index/detail/id/([^/\\s]+)";
    /**
     * 到店买单.
     */
    private final static String OFFLINE_PAY = "^https?://[^/]+.mgxz.com/offline/paylist/shop/([^/\\s]+)";


    private DevUtil devUtil;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hihome_activity);
    }

    @Override
    protected Category initCategory() {

        setCurrentCityId(AppRuntimeWorker.getCity_id());
        JpushHelper.registerAll();
        setTwiceKeyDownToCloseActivity(true);
        if (ServerUrl.DEBUG) {
            devUtil = new DevUtil(this);
            devUtil.registerShakeListener();
        }
        return new HiHomeCategory(this);
    }


    @Override
    protected void doOnResume() {

        if(null != getCategory()){
            getCategory().onResume();
        }

        if (null != getCategory()) {
            getCategory().onRefreshGreeting();
        }
        if (null != getCategory()) {
            checkIfInMyFragment();
        }
        if (!TextUtils.isEmpty(AppRuntimeWorker.getCity_id())) {
            if (!AppRuntimeWorker.getCity_id().equals(getCurrentCityId())) {
                handlerReturnCityId(AppRuntimeWorker.getCityCurr());
            }
        }
    }

    @Override
    protected void doOnDestory() {
        super.doOnDestory();
        if (devUtil != null) {
            devUtil.unregisterShakeListener();
        }
    }

    private void checkIfInMyFragment() {
        getCategory().checkIfInMyFragment();
    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {
        switch (EnumEventTag.valueOf(event.getTagInt())) {
            default:
                break;
        }
    }

    private void handlerLoginSuccessFromDiamond() {
        if (null != getCategory()) {
            getCategory().handlerLoginSuccessFromDiamond();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                /**
                 * 城市列表返回回调
                 */
                case RequestCode.RESUTN_CITY_ID:
                    handlerReturnCityId(data);
                    break;
                case RequestCode.HOME_WEB_PAGE:
                    handlerFunnyFragment();
                    break;
                case RequestCode.LOGIN_SUCCESS_FOR_DIAMON:
                    handlerLoginSuccessFromDiamond();
                    break;
            }
        }

        /**
         * 扫码
         */
        if (resultCode == ResultCode.RESULT_CODE_SCAN_SUCCESS) {
            if (data != null) {
                handlerScanQrCode(data.getStringExtra(IntentKey.EXTRA_RESULT_SUCCESS_STRING));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * //获取完整的域名
     *
     * @param text 获取浏览器分享出来的text文本
     */
    public static boolean getCompleteUrl(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(text);

        boolean result = matcher.find();
        return result;

    }

    /**
     * 处理扫码
     *
     * @param extraString
     */
    private void handlerScanQrCode(String extraString) {
        //他的小店.
        if (getCompleteUrl(extraString, SHOP_PATTERN)) {
            String user_id = extraString.split("\\/")[extraString.split("\\/").length - 1];
            Intent intentStore = new Intent(this, DistributionStoreWapActivity.class);
            intentStore.putExtra("user_id", user_id);
            intentStore.putExtra("url", extraString);
            BaseUtils.jumpToNewActivity(this, intentStore);
        } else if (getCompleteUrl(extraString, SHOP_DETAIL)) {
            //门店详情
            String extra_merchant_id = extraString.split("\\/")[extraString.split("\\/").length - 1];
            Intent intentStore = new Intent(this, StoreDetailActivity.class);
            intentStore.putExtra(EXTRA_MERCHANT_ID, extra_merchant_id);
            BaseUtils.jumpToNewActivity(this, intentStore);
        } else if (getCompleteUrl(extraString, SHOPPING_DETAIL)) {
            //团购详情
            String mId = extraString.split("\\/")[extraString.split("\\/").length - 1];
            Intent intentStore = new Intent(this, TuanDetailActivity.class);
            intentStore.putExtra(EXTRA_GOODS_ID, mId);
            BaseUtils.jumpToNewActivity(this, intentStore);
        } else if (getCompleteUrl(extraString,OFFLINE_PAY)){
            String mId = extraString.split("\\/")[extraString.split("\\/").length - 1];
            Intent intentStore = new Intent(this, HiOfflinePayActivity.class);
            intentStore.putExtra(IntentKey.OFFLINE_SHOP_ID, mId);
            BaseUtils.jumpToNewActivity(this, intentStore);
        }else {
            MGToast.showToast("对不起，无法识别。");
        }
    }

    private void handlerReturnCityId(Intent data) {
        ModelCityList model = (ModelCityList) data.getSerializableExtra(IntentKey.RETURN_CITY_DATA);
        handlerReturnCityId(model);
    }

    private void handlerReturnCityId(ModelCityList model) {
        setCurrentCityId(AppRuntimeWorker.getCity_id());
        getCategory().updateFromCityChanged(model);
    }

    private void handlerFunnyFragment() {
        getCategory().handleFunnyFragment();
    }

    @Override
    public HiHomeCategory getCategory() {
        return (HiHomeCategory) super.getCategory();
    }

    public String getCurrentCityId() {
        return currentCityId;
    }

    public void setCurrentCityId(String currentCityId) {
        this.currentCityId = currentCityId;
    }
}
