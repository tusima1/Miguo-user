package com.miguo.view;

import com.miguo.entity.CheckCitySignBean;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/9.
 */
public interface CheckCityView extends BaseView{
    void checkCitySignSuccess();
    void checkCitySignError(CheckCitySignBean.Result.Body citySign);
    void networkError();
}
