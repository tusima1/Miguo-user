package com.fanwe.user.model.wallet;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouhy on 2016/11/23.
 */

public class ExchangeListModel implements Serializable {


    private List<Convert_list> convert_list;

    private String bean;

    public void setConvert_list(List<Convert_list> convert_list) {
        this.convert_list = convert_list;
    }

    public List<Convert_list> getConvert_list() {
        return this.convert_list;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getBean() {
        return this.bean;
    }

}


