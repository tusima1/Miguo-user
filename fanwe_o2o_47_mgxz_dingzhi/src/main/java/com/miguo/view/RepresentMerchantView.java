package com.miguo.view;

/**
 * Created by Administrator on 2016/10/24.
 */
public interface RepresentMerchantView extends BaseView{

    void getRepresentMerchantSuccess();
    void getRepresentMerchantError(String message);
    void onFinish();

}
