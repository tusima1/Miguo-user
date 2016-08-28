package com.tencent.qcloud.suixinbo.presenters.viewinface;


import com.tencent.qcloud.suixinbo.model.LiveInfoJson;

/**
 * 进出房间回调接口
 */
public interface EnterQuiteRoomView extends MvpView {


    /**
     * 进入房间成功
     * @param id_status
     * @param succ
     */
    void enterRoomComplete(int id_status, boolean succ);

    /**
     * 退出房间成功
     * @param id_status
     * @param succ
     * @param liveinfo
     */
    void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo);

    /**
     * 有成员退群
     * @param list
     */
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
