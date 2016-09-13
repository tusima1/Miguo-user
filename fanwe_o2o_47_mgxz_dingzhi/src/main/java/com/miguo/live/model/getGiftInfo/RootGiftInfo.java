package com.miguo.live.model.getGiftInfo;

import java.util.List;

/**
 * Created by didik on 2016/9/12.
 */
public class RootGiftInfo {

    /**
     * result : [{"body":[{"giftList":[{"price":"30.00","icon":"1","name":"测试礼物1","id":"1",
     * "type":"2","is_delete":"0"},{"price":"2.00","icon":"2","name":"测试礼物2","id":"2","type":"1",
     * "is_delete":"0"},{"price":"2.00","icon":"4","name":"测试礼物4","id":"3","type":"1",
     * "is_delete":"0"},{"price":"2.00","icon":"3","name":"测试礼物3","id":"4","type":"1",
     * "is_delete":"0"},{"price":"2.00","icon":"8","name":"测试礼物8","id":"5","type":"1",
     * "is_delete":"0"},{"price":"2.00","icon":"6","name":"测试礼物6","id":"6","type":"1",
     * "is_delete":"0"},{"price":"2.00","icon":"9","name":"测试礼物9","id":"7","type":"1",
     * "is_delete":"0"},{"price":"2.00","icon":"5","name":"测试礼物5","id":"8","type":"1",
     * "is_delete":"0"},{"price":"2.00","icon":"7","name":"测试礼物7","id":"9","type":"1",
     * "is_delete":"0"}]}]}]
     * message : 操作成功
     * statusCode : 200
     * token : 6793a4b7a37c305ae11610cca878f9e6
     */

    private String message;
    private String statusCode;
    private String token;
    private List<ResultGiftInfo> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ResultGiftInfo> getResult() {
        return result;
    }

    public void setResult(List<ResultGiftInfo> result) {
        this.result = result;
    }
}
