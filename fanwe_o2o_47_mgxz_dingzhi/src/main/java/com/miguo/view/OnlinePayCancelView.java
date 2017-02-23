package com.miguo.view;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/23.
 */

public interface OnlinePayCancelView extends BaseView {

    void cancelSuccess();
    void cancelError(String message);

}
