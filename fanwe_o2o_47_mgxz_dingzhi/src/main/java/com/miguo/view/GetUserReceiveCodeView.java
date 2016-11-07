package com.miguo.view;

import com.fanwe.home.model.Room;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface GetUserReceiveCodeView extends BaseView{

    void getUserReceiveCodeSuccess(Room room);
    void getUserReceiveCodeError(String message);

}
