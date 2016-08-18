package com.fanwe.seller.model.getCroupBuyByMerchant;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultCroupBuyByMerchant {

    private List<ModelCroupBuyByMerchant> body;

    private String title;

    public void setBody(List<ModelCroupBuyByMerchant> body) {
        this.body = body;
    }

    public List<ModelCroupBuyByMerchant> getBody() {
        return this.body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
