package com.miguo.view;

import com.miguo.live.model.generateSign.ModelGenerateSign;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface TencentSignView extends BaseView{

    void getTencentSignSuccess(ModelGenerateSign sign);
    void getTencentSignError();

}
