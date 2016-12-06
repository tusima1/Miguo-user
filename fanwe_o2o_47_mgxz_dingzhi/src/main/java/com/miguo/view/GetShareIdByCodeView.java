package com.miguo.view;

/**
 * Created by Administrator on 2016/11/30.
 */

public interface GetShareIdByCodeView extends BaseView {
    void getShareIdSucess(String shareId);
    void getShareIdError(String message);
}
