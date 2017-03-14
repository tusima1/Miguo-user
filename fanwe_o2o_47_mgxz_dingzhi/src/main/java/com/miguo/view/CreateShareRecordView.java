package com.miguo.view;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/13.
 */

public interface CreateShareRecordView extends BaseView {

    /**
     * @param shareRecordId 分享id
     */
    void createShareRecordSuccess(String shareRecordId);

    /**
     *
     * @param message 失败消息
     */
    void createShareRecordError(String message);

}
