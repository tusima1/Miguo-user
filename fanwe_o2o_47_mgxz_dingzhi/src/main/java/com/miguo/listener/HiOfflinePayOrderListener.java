package com.miguo.listener;

import android.view.View;
import android.widget.CompoundButton;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiOfflinePayOrderCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class HiOfflinePayOrderListener extends Listener {

    public HiOfflinePayOrderListener(Category category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_order:
                clickPay();
                break;
            case R.id.wechat_layout:
                clickWechatLayout();
                break;
            case R.id.alipay_layout:
                clickAlipayLayout();
                break;
            case R.id.account_layout:
                clickAccountLayout();
                break;
            case R.id.back:
                clickBack();
                break;
        }
    }

    private void clickBack(){
        getCategory().clickBack();
    }

    private void clickWechatLayout(){
        getCategory().handleClickWechatLayout();
    }

    private void clickAlipayLayout(){
        getCategory().handleClickAlipayLayout();
    }

    private void clickAccountLayout(){
        getCategory().handleClickAccountLayout();
    }

    private void clickPay(){
        getCategory().clickPay();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        getCategory().onCheckedChanged(buttonView, isChecked);
    }

    @Override
    public HiOfflinePayOrderCategory getCategory() {
        return (HiOfflinePayOrderCategory)super.getCategory();
    }
}
