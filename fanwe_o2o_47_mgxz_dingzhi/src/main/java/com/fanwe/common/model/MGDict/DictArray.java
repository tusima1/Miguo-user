package com.fanwe.common.model.MGDict;

import java.util.List;

/**
 * Created by didik on 2016/9/27.
 */

public class DictArray {

    /**
     * dic_mean : 60
     * dic_name : Captcha
     * dic_type : Client
     * dic_value : Interval
     * id : 55f48811-5605-11e6-98e2-6c92bf2c1775
     * is_enable : 1
     * order_id : 1
     * parent_id :
     * remark : 重新获取验证码间隔时间，单位秒
     * targert_obj :
     * version : 1
     */
    private List<DictModel> dict;

    public List<DictModel> getDict() {
        return dict;
    }

    public void setDict(List<DictModel> dict) {
        this.dict = dict;
    }

    @Override
    public String toString() {
        return "DictArray{" +
                "dict=" + dict +
                '}';
    }
}
