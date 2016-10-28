package com.fanwe.groupon.model.getFeaturedGroupBuy;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultFeaturedGroupBuy implements Serializable {

    private List<BodyFeaturedGroupBuy> body;

    public List<BodyFeaturedGroupBuy> getBody() {
        return body;
    }

    public void setBody(List<BodyFeaturedGroupBuy> body) {
        this.body = body;
    }
}
