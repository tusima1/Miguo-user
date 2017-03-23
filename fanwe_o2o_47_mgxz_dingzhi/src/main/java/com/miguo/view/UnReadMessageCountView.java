package com.miguo.view;

import com.miguo.entity.UnReadMessageCountBean;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/23.
 */

public interface UnReadMessageCountView extends BaseView {

    void getUnReadMessageCountSuccess(UnReadMessageCountBean.Result.Body count);
    void getUnReadMessageCountError(String message);

}
