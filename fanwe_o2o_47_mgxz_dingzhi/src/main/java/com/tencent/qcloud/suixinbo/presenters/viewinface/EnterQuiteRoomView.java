package com.tencent.qcloud.suixinbo.presenters.viewinface;


import com.tencent.qcloud.suixinbo.model.LiveInfoJson;

/**
 * 进出房间回调接口
 */
public interface EnterQuiteRoomView extends MvpView {


    void enterRoomComplete(int id_status, boolean succ);

    void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo);

    void memberQuiteLive(String[] list);

    /**
     * 主播退出 房间。
     * @param type
     * @param responseBody
     */
    void hostQuiteLive(String type,String responseBody);

    void memberJoinLive(String[] list);

    void alreadyInLive(String[] list);


}
