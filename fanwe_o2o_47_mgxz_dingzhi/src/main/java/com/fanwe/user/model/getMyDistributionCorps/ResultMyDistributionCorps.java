package com.fanwe.user.model.getMyDistributionCorps;

import java.util.List;

/**
 * Created by didik on 2016/8/22.
 */
public class ResultMyDistributionCorps {
    private String user_id;

    private String pname;

    private String page_title;

    private List<ModelMyDistributionCorps> list;

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPname() {
        return this.pname;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public String getPage_title() {
        return this.page_title;
    }

    public void setList(List<ModelMyDistributionCorps> list) {
        this.list = list;
    }

    public List<ModelMyDistributionCorps> getList() {
        return this.list;
    }

}
