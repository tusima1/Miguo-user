package com.fanwe.user.model.getMyDistributionCorps;

import com.miguo.live.model.PageModel;

import java.util.List;

/**
 * Created by didik on 2016/8/22.
 */
public class ResultMyDistributionCorps {
    private String total;

    private String user_id;

    private String page_title;

    private String up_name;

    private String up_id;

    private String level1;
    private PageModel page;
    private List<ModelMyDistributionCorps> list;

    private String level3;

    private String level2;

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return this.total;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public String getPage_title() {
        return this.page_title;
    }

    public void setUp_name(String up_name) {
        this.up_name = up_name;
    }

    public String getUp_name() {
        return this.up_name;
    }

    public void setUp_id(String up_id) {
        this.up_id = up_id;
    }

    public String getUp_id() {
        return this.up_id;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getLevel1() {
        return this.level1;
    }

    public void setList(List<ModelMyDistributionCorps> list) {
        this.list = list;
    }

    public List<ModelMyDistributionCorps> getList() {
        return this.list;
    }

    public void setLevel3(String level3) {
        this.level3 = level3;
    }

    public String getLevel3() {
        return this.level3;
    }

    public void setLevel2(String level2) {
        this.level2 = level2;
    }

    public String getLevel2() {
        return this.level2;
    }

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }
}
