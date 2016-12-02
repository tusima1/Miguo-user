package com.fanwe.network.okhttp3;

/**
 * Created by didik on 2016/12/2.
 */

public class OkHttpException extends Exception {
    private int errorCode;
    private Object errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
        this.errorMsg = errorMsg;
    }
}
