package com.miguo.view;

/**
 * Created by zlh on 2016/12/6.
 */

public interface ShoppingCartMultiAddView extends BaseView {
    /**
     * 添加成功
     */
    void multiAddSuccess();

    /**
     * 添加失败
     * @param message 回调消息
     */
    void multiAddError(String message);

}
