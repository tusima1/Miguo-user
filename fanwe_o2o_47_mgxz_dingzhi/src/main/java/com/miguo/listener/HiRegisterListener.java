package com.miguo.listener;

import android.content.Intent;
import android.view.View;

import com.fanwe.RegisterAgreementActivity;
import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiRegisterCategory;
import com.miguo.utils.BaseUtils;

/**
 * Created by Administrator on 2016/12/3.
 */

public class HiRegisterListener extends Listener {

    public HiRegisterListener(Category category) {
        super(category);
    }

    @Override
    public void onClickThis(View v) {
        switch (v.getId()){
            case R.id.tv_register:
                clickRegister();
                break;
            case R.id.ll_register_xieyi:
                clickAgreement();
                break;
            case R.id.btn_send_code:
                clickSendSMSCode();
                break;
        }
    }

    private void clickRegister(){
        getCategory().clickRegister();
    }

    private void clickAgreement(){
        Intent intent = new Intent(getActivity(), RegisterAgreementActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    private void clickSendSMSCode(){
        getCategory().clickSendSMSCode();
    }

    @Override
    public HiRegisterCategory getCategory() {
        return (HiRegisterCategory) super.getCategory();
    }
}
