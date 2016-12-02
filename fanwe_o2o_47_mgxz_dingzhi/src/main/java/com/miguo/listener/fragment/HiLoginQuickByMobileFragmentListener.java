package com.miguo.listener.fragment;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiLoginQuickByMobileFragmentCategory;

/**
 * Created by Administrator on 2016/12/1.
 */

public class HiLoginQuickByMobileFragmentListener extends FragmentListener{

    public HiLoginQuickByMobileFragmentListener(FragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_code:
                clickSendSMSCode();
                break;
            case R.id.btn_login:
                clickLogin();
                break;
        }
    }

    private void clickLogin(){
        getCategory().clickLogin();
    }

    private void clickSendSMSCode(){
        getCategory().clickSendSMSCode();
    }

    @Override
    public HiLoginQuickByMobileFragmentCategory getCategory() {
        return (HiLoginQuickByMobileFragmentCategory)super.getCategory();
    }
}
