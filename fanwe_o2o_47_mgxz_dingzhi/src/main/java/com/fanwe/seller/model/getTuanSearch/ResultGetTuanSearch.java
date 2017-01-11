package com.fanwe.seller.model.getTuanSearch;

import com.fanwe.base.PageBean;
import com.fanwe.seller.model.getGroupList.ModelGroupList;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ResultGetTuanSearch {
    private List<ModelGroupList> tuan_list;

    private PageBean page;

    public List<ModelGroupList> getTuan_list() {
        return tuan_list;
    }

    public void setTuan_list(List<ModelGroupList> tuan_list) {
        this.tuan_list = tuan_list;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }
}
