package com.miguo.definition;

/**
 * Created by zlh on 2016/12/15.
 * 微信支付的状态回调
 */

public class WechatPayStatus {

    public static boolean SUCCESS = false;
    public static boolean ERROR = false;

    public static void reset(){
        setSUCCESS(false);
        setError(false);
    }

    public static boolean isERROR() {
        return ERROR;
    }

    public static void setError(boolean ERROR) {
        WechatPayStatus.ERROR = ERROR;
    }

    public static boolean isSuccess() {
        return SUCCESS;
    }

    public static void setSUCCESS(boolean SUCCESS) {
        WechatPayStatus.SUCCESS = SUCCESS;
    }
}
