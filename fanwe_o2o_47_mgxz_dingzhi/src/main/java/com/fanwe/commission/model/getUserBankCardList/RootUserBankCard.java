package com.fanwe.commission.model.getUserBankCardList;

import java.util.List;

/**
 * Created by didik on 2016/9/9.
 */
public class RootUserBankCard {
    private String message;
    private String token;
    private String statusCode;

    private List<ResultUserBankCard> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<ResultUserBankCard> getResult() {
        return result;
    }

    public void setResult(List<ResultUserBankCard> result) {
        this.result = result;
    }
}
