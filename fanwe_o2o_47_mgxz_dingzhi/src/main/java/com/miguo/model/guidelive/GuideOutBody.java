package com.miguo.model.guidelive;

import java.util.List;

/**
 * Created by didik on 2016/11/18.
 */

public class GuideOutBody {
    private String page_total;
    private String page;
    private String page_count;
    private String page_size;
    /**
     * extend : 59898
     * img : http://cdn.mgxz.com/2016/11/FqkG5fcNVOzoW_6i6W-khFic2TkM
     * create_time : 1451577600000
     * is_effect : 1
     * id : 2dafd92e-a0e9-40eb-b491-a48db1901e5d
     * video : http://cdn.mgxz.com/job_video/source/media.test.mp4
     * sort : 0
     * title : 12312213
     * descript : 123
     */

    private List<GuideOutModel> guide_list;

    public String getPage_total() {
        return page_total;
    }

    public void setPage_total(String page_total) {
        this.page_total = page_total;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public List<GuideOutModel> getGuide_list() {
        return guide_list;
    }

    public void setGuide_list(List<GuideOutModel> guide_list) {
        this.guide_list = guide_list;
    }
}
