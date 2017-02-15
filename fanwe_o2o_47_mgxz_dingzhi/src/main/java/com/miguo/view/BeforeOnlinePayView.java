package com.miguo.view;

import com.miguo.entity.BeforeOnlinePayBean;

/**
 * Created by Barry/狗蛋哥/zlg on 2017/2/15.
 */

public interface BeforeOnlinePayView extends BaseView {

    void getOfflinePayInfoSuccess(BeforeOnlinePayBean.Result.Body offlinePayInfo);
    void getOfflinePayInfoError(String message);

}
