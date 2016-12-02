package com.miguo.view;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface GetSMSCodeView extends BaseView {
    void getSMSCodeSuccess(String message);
    void getSMSCodeError(String message);
}
