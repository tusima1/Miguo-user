package com.miguo.view;

import com.miguo.entity.CreateShareRecordBean;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/13.
 */

public interface CreateShareRecordView extends BaseView {

    /**
     *
     * @param shareRecord 分享信息
     */
    void createShareRecordSuccess(CreateShareRecordBean.Result.Body shareRecord);

    /**
     *
     * @param message 失败消息
     */
    void createShareRecordError(String message);

}
