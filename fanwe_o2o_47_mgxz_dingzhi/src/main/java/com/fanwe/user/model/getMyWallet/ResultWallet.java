package com.fanwe.user.model.getMyWallet;

import java.util.List;

/**
 * Created by didik on 2016/9/18.
 */
public class ResultWallet {
    private List<ModelWallet> body;

    public List<ModelWallet> getBody() {
        return body;
    }

    public void setBody(List<ModelWallet> body) {
        this.body = body;
    }
}
