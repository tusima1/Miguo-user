package com.fanwe.base;

import java.util.List;

/**
 * Created by didik on 2016/11/4.
 */

public interface IRequestCallback {
    void onStart(String method,String msg);
    void onSuccess(String method,List data);
    void onFailure(String method,String msg);
    void onFinish(String method,String msg);
}
