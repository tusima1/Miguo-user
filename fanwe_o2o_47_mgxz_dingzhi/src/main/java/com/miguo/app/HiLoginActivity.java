package com.miguo.app;

import android.content.Intent;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiLoginCategory;
import com.miguo.definition.IntentKey;
import com.miguo.definition.RequestCode;
import com.miguo.definition.ResultCode;
import com.miguo.utils.BaseUtils;

/**
 * Created by zlh on 2016/11/30.
 */

public class HiLoginActivity extends HiBaseActivity {

    /**
     * 如果是从领钻码对话框进来的，结束Activity的时候要设置requestcode用于HiHomeActivity接收
     */
    boolean isFromDiamond;

    private int mSelectTabIndex;

    /**
     * 如果是从注册界面跳转过来的，应该赋值到用户手机号码输入框
     */
    String userMobile;

    @Override
    protected Category initCategory() {
        return new HiLoginCategory(this);
    }

    @Override
    protected void setContentView() {
        getIntentData();
        setContentView(R.layout.activity_hilogin);
    }

    private void getIntentData(){
        if(getIntent() != null){
            isFromDiamond = getIntent().getBooleanExtra(IntentKey.FROM_DIAMOND_TO_LOGIN, false);
            mSelectTabIndex = getIntent().getIntExtra(IntentKey.EXTRA_SELECT_TAG_INDEX, 0);
            setUserMobile(getIntent().getStringExtra(IntentKey.LOGIN_MOBILE));
        }
    }

    @Override
    public void finishActivity(){
        if(isFromDiamond){
            Intent intent = new Intent(this, HiHomeActivity.class);
            setResult(ResultCode.RESUTN_OK, intent);
        }
        BaseUtils.finishActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != getCategory()){
            getCategory().onActivityResult(requestCode, resultCode, data);
        }
        if(resultCode == ResultCode.RESUTN_OK){
            switch (requestCode){
                case RequestCode.LOGIN_SUCCESS_FOR_DIAMON:
                    finishActivity();
                    break;
            }
        }
    }

    @Override
    public HiLoginCategory getCategory() {
        return (HiLoginCategory) super.getCategory();
    }

    public boolean isFromDiamond() {
        return isFromDiamond;
    }

    public int getmSelectTabIndex() {
        return mSelectTabIndex;
    }

    public void setmSelectTabIndex(int mSelectTabIndex) {
        this.mSelectTabIndex = mSelectTabIndex;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
