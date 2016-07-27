package com.fanwe.base;

/**
 * 基本类，实现基本的业务请求的返回的响应。
 * Created by Administrator on 2016/7/26.
 */
public interface  CallbackView {

      void onSuccess(String responseBody);
      void onFailue(String responseBody);
}
