package com.fanwe.network.okhttp3;

/**
 * Created by didik 
 * Created time 2016/12/5
 * Description: 
 */

public interface ICallbackView {
      void onBefore(String method);

      void onSuccess(String method,Object bean);

      void onFailure(String method,OkHttpException error);

      void onAfter(String method);

}
