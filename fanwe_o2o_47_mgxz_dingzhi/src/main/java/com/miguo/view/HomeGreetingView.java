package com.miguo.view;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface HomeGreetingView extends BaseView{

    void getHomeGreetingSuccess(String httpUuid,String greeting);
    void getHomeGreetingError(String httpUuid);

}
