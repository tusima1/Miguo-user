package com.miguo.view;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface HomeGreetingView extends BaseView{

    void getHomeGreetingSuccess(String greeting);
    void getHomeGreetingError();

}
