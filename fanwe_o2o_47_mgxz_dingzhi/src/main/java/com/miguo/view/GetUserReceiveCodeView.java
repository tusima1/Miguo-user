package com.miguo.view;


import com.miguo.live.model.getLiveListNew.ModelRoom;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface GetUserReceiveCodeView extends BaseView{

    void getUserReceiveCodeSuccess(ModelRoom room);
    void getUserReceiveCodeError(String message);

}
