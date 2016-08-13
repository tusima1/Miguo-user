package com.fanwe.seller.model.getShopList;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ModelShopListNavs {
    private String name;
    private String code;

    private boolean isSelect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String value) {
        this.code = value;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
