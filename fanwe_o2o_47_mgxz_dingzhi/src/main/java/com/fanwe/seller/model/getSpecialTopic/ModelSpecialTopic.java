package com.fanwe.seller.model.getSpecialTopic;

import com.fanwe.seller.model.getGroupDeatilNew.ShareInfoBean;

import java.util.List;

/**
 * Created by didik on 2016/10/18.
 */

public class ModelSpecialTopic {
    private TopicBean topic;


    private PageBean page;

    private List<DetailListBean> detail_list;

    /**********12-12*********/
    private ShareInfoBean share;

    public ShareInfoBean getShare() {
        return share;
    }

    public void setShare(ShareInfoBean share) {
        this.share = share;
    }

    public TopicBean getTopic() {
        return topic;
    }

    public void setTopic(TopicBean topic) {
        this.topic = topic;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }


    public List<DetailListBean> getDetail_list() {
        return detail_list;
    }

    public void setDetail_list(List<DetailListBean> detail_list) {
        this.detail_list = detail_list;
    }
}
