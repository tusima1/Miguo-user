package com.miguo.app;

import android.content.Intent;

import com.fanwe.model.CitylistModel;
import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiHomeCategory;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.definition.ResultCode;

/**
 * Created by  zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeActivity extends HiBaseActivity{

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hihome_activity);
    }

    @Override
    protected Category initCategory() {
        setTwiceKeyDownToCloseActivity(true);
        return new HiHomeCategory(this);
    }

    @Override
    protected void doOnResume() {
        if(null != getCategory()){
            getCategory().onRefreshGreeting();
        }
        if(null != getCategory()){
            checkIfInMyFragment();
        }
    }

    private void checkIfInMyFragment(){
        getCategory().checkIfInMyFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                /**
                 * 城市列表返回回调
                 */
                case RequestCode.RESUTN_CITY_ID:
                    handlerReturnCityId(data);
                    break;
                case RequestCode.HOME_WEB_PAGE:
                    handlerFunnyFragment();
                    break;
            }
        }

        /**
         * 扫码
         */
        if(resultCode == ResultCode.RESULT_CODE_SCAN_SUCCESS){
            if(data != null){
                handlerScanQrCode(data.getStringExtra(IntentKey.EXTRA_RESULT_SUCCESS_STRING));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 处理扫码
     * @param extraString
     */
    private void handlerScanQrCode(String extraString){

    }

    private void handlerReturnCityId(Intent data){
        CitylistModel model = (CitylistModel)data.getSerializableExtra(IntentKey.RETURN_CITY_DATA);
        getCategory().updateFromCityChanged(model);
    }

    private void handlerFunnyFragment(){
        getCategory().handlerFunnyFragment();
    }

    @Override
    public HiHomeCategory getCategory() {
        return (HiHomeCategory) super.getCategory();
    }

}
