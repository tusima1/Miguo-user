package com.fanwe.base;

import java.util.List;

/**
 * 基本类，实现基本的业务请求的返回的响应。
 * Created by Administrator on 2016/7/26.
 */
public interface CallbackView {

    void onSuccess(List<Result> responseBody);

    void onSuccess(String responseBody);

    void onSuccess(String method, List datas);

    void onFailue(String responseBody);
}
